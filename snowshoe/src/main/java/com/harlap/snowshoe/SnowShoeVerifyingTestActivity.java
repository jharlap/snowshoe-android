package com.harlap.snowshoe;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SnowShoeVerifyingTestActivity extends AbstractStampVerifyingActivity {

    private static final String TAG = "SnowShoeTestActivity";

    private TextView mStampResultView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snowshoe_stamp);

        SNOWSHOE_APP_KEY = "MY APP KEY";
        SNOWSHOE_APP_SECRET = "MY APP SECRET";

        mStampResultView = (TextView) findViewById(R.id.stamp_result);
    }

    @Override
    protected void onStampTouch(StampTouch stampTouch) {
        Log.i(TAG, "onStampTouch(" + stampTouch + ")");
        super.onStampTouch(stampTouch);
    }

    @Override
    protected void onStampVerified(SnowShoeResponse response) {
        Log.i(TAG, "onStampVerified(): " + response);
        String message;
        if (response.getError() != null) {
            message = "Error: " + response.getError().getMessage();
        } else {
            message = "You stamped: " + response.getStamp().getSerial();
        }
        mStampResultView.setText(message);
    }
}
