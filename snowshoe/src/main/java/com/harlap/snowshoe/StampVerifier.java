package com.harlap.snowshoe;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public StampVerifier(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public SnowShoeResponse verifyStamp(StampTouch stampTouch) {
        HttpClient client = new DefaultHttpClient();

        try {

            // put together a query String
            StringBuilder queryBuilder = new StringBuilder();
            List<Pair<Float, Float>> points = stampTouch.getPoints();

            queryBuilder.append("https://beta.snowshoestamp.com/api/v2/stamp?");

            queryBuilder.append("&x1=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(0).first, "UTF-8"));

            queryBuilder.append("&x2=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(1).first, "UTF-8"));

            queryBuilder.append("&x3=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(2).first, "UTF-8"));

            queryBuilder.append("&x4=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(3).first, "UTF-8"));

            queryBuilder.append("&x5=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(4).first, "UTF-8"));

            queryBuilder.append("&y1=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(0).second, "UTF-8"));

            queryBuilder.append("&y2=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(1).second, "UTF-8"));

            queryBuilder.append("&y3=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(2).second, "UTF-8"));

            queryBuilder.append("&y4=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(3).second, "UTF-8"));

            queryBuilder.append("&y5=");
            queryBuilder
                    .append(URLEncoder.encode("" + points.get(4).second, "UTF-8"));

            Log.i("query", queryBuilder.toString());

            HttpGet httpGet = new HttpGet(queryBuilder.toString());

            if (appKey.isEmpty() || appSecret.isEmpty()) {
                Log.e(TAG, "ERROR: You must set SNOWSHOE_APP_KEY and SNOWSHOE_APP_SECRET");
                return null;
            }
            OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
                    appKey,
                    appSecret);
            consumer.setTokenWithSecret("", "");

            try {
                consumer.sign(httpGet);
            } catch (OAuthMessageSignerException e) {
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                e.printStackTrace();
            }

            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(SnowShoeResponse.class, new SnowShoeResponseDeserializer())
                    .create();
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
