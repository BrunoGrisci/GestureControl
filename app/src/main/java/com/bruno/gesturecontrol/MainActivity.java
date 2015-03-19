package com.bruno.gesturecontrol;

import android.app.TabActivity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TabHost;


public class MainActivity extends TabActivity {

    SensorManager sensorManager;

    private final float SHAKE_THRESHOLD = 1.5f;

    private int accuracyAccelerometer;

    private float xAccelerometer;
    private float yAccelerometer;
    private float zAccelerometer;

    private float xPreviousAccelerometer;
    private float yPreviousAccelerometer;
    private float zPreviousAccelerometer;

    private boolean firstShake = false;
    private boolean shakeInitialized = false;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            updateAccelerometerParameters(se.values[0], se.values[1], se.values[2]);
            if ((!shakeInitialized) && isAccelerationChanged()) {
                shakeInitialized = true;
            } else if ((shakeInitialized) && isAccelerationChanged()) {
                GestureFunctions.startActionLaunchPhone(getApplicationContext());
            } else if ((shakeInitialized) && (!isAccelerationChanged())) {
                shakeInitialized = false;
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
        float deltaX = Math.abs(xPreviousAccelerometer - xAccelerometer);
        float deltaY = Math.abs(yPreviousAccelerometer - yAccelerometer);
        float deltaZ = Math.abs(zPreviousAccelerometer - zAccelerometer);
        return (deltaX > SHAKE_THRESHOLD && deltaY > SHAKE_THRESHOLD) || (deltaX > SHAKE_THRESHOLD && deltaZ > SHAKE_THRESHOLD) || (deltaY > SHAKE_THRESHOLD && deltaZ > SHAKE_THRESHOLD);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //It creates the tabs
        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("first").setIndicator("Gesture activation").setContent(new Intent(this, GestureActivation.class )));
        mTabHost.addTab(mTabHost.newTabSpec("second").setIndicator("Commands").setContent(new Intent(this, Commands.class )));
        mTabHost.setCurrentTab(0);

        startService(new Intent(getApplicationContext(), FloatingButtonService.class));

        sensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), FloatingButtonService.class));
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

}
