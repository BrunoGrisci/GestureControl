package com.bruno.gesturecontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class TransparentLayout extends ActionBarActivity implements GestureOverlayView.OnGesturePerformedListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat mDetector;
    private GestureLibrary mLibrary;

    private final String GESTURE_CHECK_MARK = "CHECK_MARK";
    private final String GESTURE_CIRCULAR_CLOCKWISE = "CIRCULAR_CLOCKWISE";
    private final String GESTURE_CIRCULAR_COUNTERCLOCKWISE = "CIRCULAR_COUNTERCLOCKWISE";
    private final String GESTURE_HEART = "HEART";
    private final String GESTURE_TRIANGLE = "TRIANGLE";
    private final String GESTURE_M = "M";
    private final String GESTURE_V = "V";
    private final String GESTURE_CLEF = "CLEF";
    private final String GESTURE_CLEF2 = "CLEF2";

    private final double SCORE_THRESHOLD = 2.0;

    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    //static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
    static final String TWITTER_CALLBACK_URL = "oauth://www.cs.bham.ac.uk/internal/modules/2014/25689/";
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;
    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_layout);

        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);

        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mLibrary.load()) {
            killActivity();
        }

        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transparent_layout, menu);
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

    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

        if (predictions.size() > 0 && predictions.get(0).score > SCORE_THRESHOLD) {
            String result = predictions.get(0).name;
            double score = predictions.get(0).score;

            if (GESTURE_CHECK_MARK.equalsIgnoreCase(result) && score > 5.0) {
                //GestureFunctions.startActionPostTwitter(getApplicationContext());

                SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
                if (savedSwitchStatus.getBoolean(getResources().getString(R.string.switch_twitter), false)) {
                    playConfirmationSound();

                    Location userLocation;
                    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);;
                    userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    Geocoder geocoder = new Geocoder(TransparentLayout.this, Locale.getDefault());
                    String address = "";
                    try {
                        List<Address> addLines;
                        addLines = geocoder.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), 1);
                        if (addLines.isEmpty()) {
                            address = getResources().getString(R.string.unknown_address);
                        }
                        else {
                            for (int i = 0; i < addLines.get(0).getMaxAddressLineIndex(); i++) {
                                address = address + addLines.get(0).getAddressLine(i) + ", ";
                            }
                            address = address + addLines.get(0).getCountryCode();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final String address_final = getResources().getString(R.string.my_location_is) + " " + address;
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), address_final, Toast.LENGTH_LONG).show();
                        }
                    });

                    loginToTwitter();

                    /*mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    if (!isTwitterLoggedInAlready()) {
                        Uri uri = getIntent().getData();
                        if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                            // oAuth verifier
                            String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

                            try {
                                // Get the access token
                                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                                // Shared Preferences
                                SharedPreferences.Editor e = mSharedPreferences.edit();

                                // After getting access token, access token secret
                                // store them in application preferences
                                e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                                e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
                                // Store login status - true
                                e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                                e.commit(); // save changes

                                Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

                                // Hide login button
                        /*btnLoginTwitter.setVisibility(View.GONE);

                        // Show Update Twitter
                        lblUpdate.setVisibility(View.VISIBLE);
                        txtUpdate.setVisibility(View.VISIBLE);
                        btnUpdateStatus.setVisibility(View.VISIBLE);
                        btnLogoutTwitter.setVisibility(View.VISIBLE);/

                                // Getting user details from twitter
                                // For now i am getting his name only
                                long userID = accessToken.getUserId();
                                User user = twitter.showUser(userID);
                                String username = user.getName();

                                // Displaying in xml ui
                                //lblUserName.setText(Html.fromHtml("<b>Welcome " + username + "</b>"));
                            } catch (Exception e) {
                                // Check log for login errors
                                Log.e("Twitter Login Error", "> " + e.getMessage());
                            }
                        }
                    }*/

                }
                else {
                    informGestureDisabled();
                }




                killActivity();
            }
            else if (GESTURE_CIRCULAR_CLOCKWISE.equalsIgnoreCase(result) && score > 3.0) {
                GestureFunctions.startActionIncreaseVolume(getApplicationContext());
                killActivity();
            }
            else if (GESTURE_CIRCULAR_COUNTERCLOCKWISE.equalsIgnoreCase(result) && score > 3.0) {
                GestureFunctions.startActionDecreaseVolume(getApplicationContext());
                killActivity();
            }
            else if (GESTURE_HEART.equalsIgnoreCase(result)) {
                SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
                String tel = savedSwitchStatus.getString("contactNumber", "tel:955538002");
                GestureFunctions.startActionCallContact(getApplicationContext(), tel);
                killActivity();
            }
            else if (GESTURE_TRIANGLE.equalsIgnoreCase(result)) {
                SharedPreferences savedSwitchStatus = getSharedPreferences("saved_switch_status", MODE_PRIVATE);
                String latitude = savedSwitchStatus.getString("locationLatitude", "64.282062");
                String longitude = savedSwitchStatus.getString("locationLongitude", "-20.346240");
                GestureFunctions.startActionNavigate(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
                killActivity();
            }
            else if (GESTURE_M.equalsIgnoreCase(result)) {
                GestureFunctions.startActionLaunchMessage(getApplicationContext());
                killActivity();
            }
            else if (GESTURE_V.equalsIgnoreCase(result)) {
                GestureFunctions.startActionLaunchVoice(getApplicationContext());
                killActivity();
            }
            else if (GESTURE_CLEF.equalsIgnoreCase(result) || GESTURE_CLEF2.equalsIgnoreCase(result)) {
                GestureFunctions.startActionLaunchMusic(getApplicationContext());
                killActivity();
            }
            else {
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
                Toast.makeText(this, getResources().getString(R.string.gesture_not_recognized), Toast.LENGTH_SHORT).show();
                killActivity();
            }

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        this.mDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        System.out.println(event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        System.out.println("onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        System.out.println("onLongPress: " + event.toString());
        GestureFunctions.startActionLaunchPhone(getApplicationContext());
        killActivity();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        System.out.println("onScroll: " + e1.toString() + e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        System.out.println("onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        System.out.println("onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        System.out.println("onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        System.out.println("onDoubleTapEvent: " + event.toString());
        GestureFunctions.startActionLaunchCamera(getApplicationContext());
        killActivity();
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
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
        System.out.println("onSingleTapConfirmed: " + event.toString());
        Toast.makeText(this, "Gesture not recognized", Toast.LENGTH_SHORT).show();
        killActivity();
        return true;
    }

    public void killActivity()
    {
        finish();
    }

    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(getResources().getString(R.string.twitter_consumer_key));
            builder.setOAuthConsumerSecret(getResources().getString(R.string.twitter_secret_key));
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                Log.d("twitter", "requestToken");
                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
                Log.d("twitter", "start");
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
            }
            catch (TwitterException e) {
                e.printStackTrace();
            }
        }
        else {
            // user already logged into twitter
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Already Logged into twitter", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
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
}
