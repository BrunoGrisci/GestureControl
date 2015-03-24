package com.bruno.gesturecontrol;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.Calendar;

import static java.lang.Math.abs;


public class MainActivity extends TabActivity {

    SensorManager sensorManager;

    private final float SHAKE_THRESHOLD = 1.5f;
    private final long TIME_SHAKES_THRESHOLD = 2000;

    private int accuracyAccelerometer;

    private float xAccelerometer;
    private float yAccelerometer;
    private float zAccelerometer;

    private float xPreviousAccelerometer;
    private float yPreviousAccelerometer;
    private float zPreviousAccelerometer;

    private boolean firstShake = false;
    private Calendar shakeInitialized = Calendar.getInstance();

    private boolean muteChange = true;
    static final String TWITTER_CALLBACK_URL = "oauth://gesturecontroluob";

    public static boolean isCameraOn = false;
    static Camera cam = Camera.open();


    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            updateAccelerometerParameters(se.values[0], se.values[1], se.values[2]);
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

            if (se.values[2] <= -5 && muteChange) {
                muteNotifications(getApplicationContext());
                muteChange = !muteChange;
            }
            else if (se.values[2] > -5 && !muteChange) {
                unmuteNotifications(getApplicationContext());
                muteChange = !muteChange;
            }
            else if (((abs(shakeInitialized.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) >= TIME_SHAKES_THRESHOLD) && isAccelerationChangedX()) && pm.isScreenOn()) {
                if (accelerationWay() > 0) {
                    Log.d("shake", "LEFT");
                    GestureFunctions.startActionDecreaseVolume(getApplicationContext());
                }
                if (accelerationWay() < 0) {
                    Log.d("shake", "RIGHT");
                    GestureFunctions.startActionIncreaseVolume(getApplicationContext());
                }
                shakeInitialized = Calendar.getInstance();
            }
            else if (((abs(shakeInitialized.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) >= TIME_SHAKES_THRESHOLD) && isAccelerationChanged()) && pm.isScreenOn()) {
                Log.d("shake", "shaked");
                SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
                if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_flashlight), false)) {
                    playConfirmationSound();
                    turnCamera();
                }
                else {
                    informGestureDisabled();
                }
                shakeInitialized = Calendar.getInstance();
            } else if ((abs(shakeInitialized.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) >= TIME_SHAKES_THRESHOLD) && (!isAccelerationChanged())) {
                //shakeInitialized = Calendar.getInstance();
            }
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            accuracyAccelerometer = accuracy;
        }
    };

    private void updateAccelerometerParameters(float xNewAccelerometer, float yNewAccelerometer, float zNewAccelerometer) {
        if (firstShake) {
            xPreviousAccelerometer = xNewAccelerometer;
            yPreviousAccelerometer = yNewAccelerometer;
            zPreviousAccelerometer = zNewAccelerometer;
            firstShake = false;
        } else {
            xPreviousAccelerometer = xAccelerometer;
            yPreviousAccelerometer = yAccelerometer;
            zPreviousAccelerometer = zAccelerometer;
        }
        xAccelerometer = xNewAccelerometer;
        yAccelerometer = yNewAccelerometer;
        zAccelerometer = zNewAccelerometer;
    }

    private boolean isAccelerationChanged() {
        float deltaX = abs(xPreviousAccelerometer - xAccelerometer);
        float deltaY = abs(yPreviousAccelerometer - yAccelerometer);
        float deltaZ = abs(zPreviousAccelerometer - zAccelerometer);
        return (deltaX > SHAKE_THRESHOLD && deltaY > SHAKE_THRESHOLD) || (deltaX > SHAKE_THRESHOLD && deltaZ > SHAKE_THRESHOLD) || (deltaY > SHAKE_THRESHOLD && deltaZ > SHAKE_THRESHOLD);
    }

    private boolean isAccelerationChangedX() {
        float deltaX = abs(xPreviousAccelerometer - xAccelerometer);
        float deltaY = abs(yPreviousAccelerometer - yAccelerometer);
        float deltaZ = abs(zPreviousAccelerometer - zAccelerometer);
        return (deltaX > SHAKE_THRESHOLD) && (deltaY <= SHAKE_THRESHOLD && deltaZ <= SHAKE_THRESHOLD);
    }

    private float accelerationWay() {
        float deltaX = xPreviousAccelerometer - xAccelerometer;
        if (abs(deltaX) > SHAKE_THRESHOLD) {
            return deltaX;
        }
        else {
            return 0;
        }
    }

    public void muteNotifications(Context context) {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_mute_notifications), false)) {
            AudioManager audioManager = (AudioManager)getSystemService(context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
        }
    }

    public void unmuteNotifications(Context context) {
        SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
        if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_mute_notifications), false)) {
            AudioManager audioManager = (AudioManager)getSystemService(context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //It creates the tabs
        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("first").setIndicator("Gesture activation").setContent(new Intent(this, GestureActivation.class )));
        mTabHost.addTab(mTabHost.newTabSpec("second").setIndicator("Commands").setContent(new Intent(this, Commands.class )));
        mTabHost.setCurrentTab(0);

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
            Intent intent = new Intent(getApplicationContext(), TransparentLayout.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("item", 5);
            intent.setData(uri);
            startActivity(intent);
        }

        Intent i= new Intent(getApplicationContext(), FloatingButtonService.class);
        getApplicationContext().startService(i);

        sensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeInitialized = Calendar.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void informGestureDisabled() {
        AudioManager audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
        switch(audioManager.getRingerMode()){
            case AudioManager.RINGER_MODE_NORMAL:
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.noconfirmation);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.start();
                break;
            case AudioManager.RINGER_MODE_SILENT:
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
                v.vibrate(200);
                break;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(R.string.gesture_disabled), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playConfirmationSound() {
        AudioManager audioManager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
        switch(audioManager.getRingerMode()){
            case AudioManager.RINGER_MODE_NORMAL:
                final MediaPlayer mp = MediaPlayer.create(this, R.raw.confirmation);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.start();
                break;
            case AudioManager.RINGER_MODE_SILENT:
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                break;
        }
    }

    public static void turnCamera() {
        if (!isCameraOn) {
            Log.d("flashlight", "ON");
            if (cam == null) {
                cam = Camera.open();
            }
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            isCameraOn = true;
        }
        else {
            Log.d("flashlight", "OFF");
            Camera.Parameters p = cam.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cam.stopPreview();
            cam.release();
            cam = null;
            isCameraOn = false;
        }
    }

}