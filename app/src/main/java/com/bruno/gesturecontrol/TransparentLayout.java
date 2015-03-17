package com.bruno.gesturecontrol;

import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;


public class TransparentLayout extends ActionBarActivity implements GestureOverlayView.OnGesturePerformedListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat mDetector;
    private GestureLibrary mLibrary;

    private final String GESTURE_CHECK_MARK = "CHECK_MARK";
    private final String GESTURE_CIRCULAR_CLOCKWISE = "CIRCULAR_CLOCKWISE";
    private final String GESTURE_CIRCULAR_COUNTERCLOCKWISE = "CIRCULAR_COUNTERCLOCKWISE";
    private final String GESTURE_HEART = "HEART";
    private final String GESTURE_TRIANGLE = "TRIANGLE";

    private final double SCORE_THRESHOLD = 2.0;

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
                GestureFunctions.startActionPostTwitter(getApplicationContext());
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
                GestureFunctions.startActionNavigate(getApplicationContext(), "geo:37.7749,-122.4194");
                killActivity();
            }
            else {
                Toast.makeText(this, "Gesture not recognized", Toast.LENGTH_SHORT).show();
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
        System.out.println("onSingleTapConfirmed: " + event.toString());
        Toast.makeText(this, "Gesture not recognized", Toast.LENGTH_SHORT).show();
        killActivity();
        return true;
    }

    public void killActivity()
    {
        finish();
    }
}
