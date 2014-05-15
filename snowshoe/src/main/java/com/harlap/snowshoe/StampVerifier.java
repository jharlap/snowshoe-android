package com.harlap.snowshoe;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

class StampVerifier {

    private static final String TAG = "StampVerifier";

    private final String appKey;
    private final String appSecret;
    private String snowShoeApiUrl;

    public StampVerifier(String appKey, String appSecret) {
        if (appKey.isEmpty() || appSecret.isEmpty()) {
            Log.e(TAG, "ERROR: appKey and appSecret must not be empty!");
            throw new RuntimeException("ERROR: appKey and appSecret must not be empty!");
        }
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.snowShoeApiUrl = "https://beta.snowshoestamp.com/api/v2/stamp";
    }

    public void setSnowShoeApiUrl(String snowShoeApiUrl) {
        this.snowShoeApiUrl = snowShoeApiUrl;
    }

    public SnowShoeResponse verifyStamp(StampTouch stampTouch) {
        HttpClient client = new DefaultHttpClient();

        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(SnowShoeResponse.class, new SnowShoeResponseDeserializer())
                    .create();

            // Prepare the data
            List<float[]> points = stampTouch.getPoints();
            byte[] pointsJsonBytes = gson.toJson(points).getBytes("UTF-8");
            String encodedPoints = Base64.encodeToString(pointsJsonBytes, Base64.DEFAULT);

            HttpPost httpPost = new HttpPost(snowShoeApiUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("data", encodedPoints));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
                    appKey,
                    appSecret);
            consumer.setTokenWithSecret("", "");

            try {
                consumer.sign(httpPost);
            } catch (OAuthMessageSignerException e) {
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                e.printStackTrace();
            }

            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));
            return gson.fromJson(reader, SnowShoeResponse.class);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
