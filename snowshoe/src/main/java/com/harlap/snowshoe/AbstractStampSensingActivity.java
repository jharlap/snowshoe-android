package com.harlap.snowshoe;

import android.app.Activity;
import android.view.MotionEvent;

/**
 * An abstract base activity that senses SnowShoe stamps. Override onStampTouch(StampTouch) to handle the stamp touch event.
 */
public abstract class AbstractStampSensingActivity extends Activity {

    abstract protected void onStampTouch(StampTouch stampTouch);

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
