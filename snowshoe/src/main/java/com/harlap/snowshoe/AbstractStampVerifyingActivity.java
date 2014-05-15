package com.harlap.snowshoe;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * An abstract base activity that senses SnowShoe stamp touches and verifies them.
 * Set the values for SNOWSHOE_APP_KEY and SNOWSHOE_APP_SECRET as part of onCreate(), and
 * override the onStampVerified(SnowShoeResponse) method to handle the verification result.
 */
public abstract class AbstractStampVerifyingActivity extends AbstractStampSensingActivity {

    /**
     * Event handler for stamp verification.
     *
     * @param response The response from SnowShoe, or null if there was an error.
     */
    abstract protected void onStampVerified(SnowShoeResponse response);

    private Boolean stampBeingChecked = false;

    StampVerifier stampVerifier = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setSnowShoeKeys(String appKey, String appSecret) {
        stampVerifier = new StampVerifier(appKey, appSecret);
    }

    @Override
    protected void onStampTouch(StampTouch stampTouch) {
        if (stampVerifier == null) {
            throw new RuntimeException("You must call setSnowShoeKeys in onCreate()");
        }
        if (!stampBeingChecked) {
            stampBeingChecked = true;
            new VerifyStampTask().execute(stampTouch);
        }
    }

    private class VerifyStampTask extends AsyncTask<StampTouch, Void, SnowShoeResponse> {
        private final ProgressDialog dialog;

        public VerifyStampTask() {
            this.dialog = new ProgressDialog(AbstractStampVerifyingActivity.this);
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Verifying stamp...");
            this.dialog.show();
        }

        @Override
        protected SnowShoeResponse doInBackground(StampTouch... stampTouches) {
            if (stampTouches.length != 1) return null;

            return stampVerifier.verifyStamp(stampTouches[0]);
        }

        @Override
        protected void onPostExecute(SnowShoeResponse response) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

            stampBeingChecked = false;
            onStampVerified(response);
        }
    }

}
