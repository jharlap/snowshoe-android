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

    abstract protected void onStampTouch(StampTouch stampTouch);

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

        if (event.getActionMasked() == MotionEvent.ACTION_MOVE ||
                event.getActionMasked() == MotionEvent.ACTION_DOWN ||
                event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {

            if (event.getPointerCount() == 5) {
                StampTouch stampTouch = new StampTouch()
                        .addPoint(event.getX(0), event.getY(0))
                        .addPoint(event.getX(1), event.getY(1))
                        .addPoint(event.getX(2), event.getY(2))
                        .addPoint(event.getX(3), event.getY(3))
                        .addPoint(event.getX(4), event.getY(4));

                onStampTouch(stampTouch);
            }
        }
        return true;
    }
}
