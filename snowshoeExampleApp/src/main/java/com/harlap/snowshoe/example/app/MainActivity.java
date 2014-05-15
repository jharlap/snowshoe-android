package com.harlap.snowshoe.example.app;

import android.os.Bundle;
import android.widget.Toast;

import com.harlap.snowshoe.AbstractStampVerifyingActivity;
import com.harlap.snowshoe.SnowShoeResponse;


public class MainActivity extends AbstractStampVerifyingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSnowShoeKeys("MY APP KEY", "MY APP SECRET");
    }

    @Override
    protected void onStampVerified(SnowShoeResponse response) {
        String message;
        if (response.getError() != null) {
            message = "Error: " + response.getError().getMessage();
        } else {
            message = "You stamped: " + response.getStamp().getSerial();
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
