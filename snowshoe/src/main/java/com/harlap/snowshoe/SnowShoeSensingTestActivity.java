package com.harlap.snowshoe;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SnowShoeSensingTestActivity extends AbstractStampSensingActivity {

    private static final String TAG = "SnowShoeSensingTestActivity";

    private TextView mStampResultView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snowshoe_stamp);

        mStampResultView = (TextView) findViewById(R.id.stamp_result);
    }

    @Override
    protected void onStampTouch(StampTouch stampTouch) {
        Log.i(TAG, "onStampTouch(" + stampTouch + ")");
        mStampResultView.setText(stampTouch.toString());
    }

}
