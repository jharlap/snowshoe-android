package com.harlap.snowshoe.example.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.harlap.snowshoe.AbstractStampVerifyingActivity;
import com.harlap.snowshoe.SnowShoeResponse;


public class MainActivity extends AbstractStampVerifyingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toggleSensingButton = (Button) findViewById(R.id.toggle_sensing_button);
        final MainActivity self = this;
        toggleSensingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newSetting = ! self.getSensingEnabled();
                self.setSensingEnabled(newSetting);
                Toast.makeText(self, "Sensing is: " + (newSetting ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
            }
        });

        // setSnowShoeKeys("MY APP KEY", "MY APP SECRET");
        // These are keys for a toy app that only has dev stamps registered - misuse will lead to the keys changing.
        setSnowShoeKeys("f170c376472ba0380156", "4ff5c2c01d6834c5968e74e4826e30ac2b020487");
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
