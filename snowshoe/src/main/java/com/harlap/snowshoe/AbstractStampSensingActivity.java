package com.harlap.snowshoe;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

/**
 * An abstract base activity that senses SnowShoe stamps. Override onStampTouch(StampTouch) to handle the stamp touch event.
 */
public abstract class AbstractStampSensingActivity extends Activity {

    private static final String TAG = "AbstractStampSensingActivity";
    private long lastHandledEventDownTime = 0;
    private boolean isSensingEnabled = true;

    /**
     * Event handler for stamp sense events.
     * @param stampTouch The stamp touch points
     */
    abstract protected void onStampTouch(StampTouch stampTouch);

    /**
     * Gets the stamp touch sensing enabled state.
     * @return True if the activity is listening for stamp events.
     */
    protected boolean getSensingEnabled() {
        return isSensingEnabled;
    }

    /**
     * Enables or disables stamp touch sensing.
     * @param enabled Is stamp sensing enabled? Default: true.
     */
    protected void setSensingEnabled(boolean enabled) {
        isSensingEnabled = enabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager packageManager = getPackageManager();
        if (packageManager != null &&
                (!packageManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND) ||
                        !packageManager.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT))) {
            onJazzHandsNotSupported();
        }
    }

    protected void onJazzHandsNotSupported() {
        Log.i(TAG, "Warning: Device does not support simultaneous 5 point touch events");
    }

    // function that interprets the 5-touch action
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isSensingEnabled) return false;

        int actionMasked = event.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_MOVE ||
                actionMasked == MotionEvent.ACTION_DOWN ||
                actionMasked == MotionEvent.ACTION_POINTER_DOWN) {
            if (event.getPointerCount() == 5) {

                if (lastHandledEventDownTime != event.getDownTime()) {
                    lastHandledEventDownTime = event.getDownTime();
                    StampTouch stampTouch = new StampTouch()
                            .addPoint(event.getX(0), event.getY(0))
                            .addPoint(event.getX(1), event.getY(1))
                            .addPoint(event.getX(2), event.getY(2))
                            .addPoint(event.getX(3), event.getY(3))
                            .addPoint(event.getX(4), event.getY(4));

                    onStampTouch(stampTouch);
                    return true;
                }
            }
        }
        return false;
    }
}
