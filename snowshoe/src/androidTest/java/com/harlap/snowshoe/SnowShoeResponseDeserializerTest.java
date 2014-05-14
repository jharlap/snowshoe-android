package com.harlap.snowshoe;

import android.test.suitebuilder.annotation.SmallTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

import uk.co.it.modular.hamcrest.date.DateMatchers;
import uk.co.it.modular.hamcrest.date.Months;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class SnowShoeResponseDeserializerTest extends TestCase {

    @SmallTest
    public void testDeserializingAValidStampWorks() {
        // Given valid json from SnowShoe
        String validJson = "{\n" +
                "        \"stamp\": {\"serial\": \"DEV-STAMP\"},\n" +
                "        \"receipt\": \"o8FLLwYW7TVjtO4VTUP4IgQvvvI=\",\n" +
                "        \"secure\": true,\n" +
                "        \"created\": \"2013-05-24 23:01:23.274\"\n" +
                "     }";

        // And GSON configured with the deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SnowShoeResponse.class, new SnowShoeResponseDeserializer())
                .create();

        // When deserializing the json
        SnowShoeResponse result = gson.fromJson(validJson, SnowShoeResponse.class);

        // Then the result is a response object containing the stamp serial, secure, created date and receipt
        assertThat(result, notNullValue());
        assertThat(result.getStamp().getSerial(), equalTo("DEV-STAMP"));
        assertThat(result.isSecure(), is(true));
        assertThat(result.getReceipt(), equalTo("o8FLLwYW7TVjtO4VTUP4IgQvvvI="));
        assertThat(result.getCreated(), DateMatchers.within(1, TimeUnit.MINUTES, 2013, Months.MAY, 24, 23, 1, 0, 0));

        // And the result should not contain an error
        assertThat(result.getError(), nullValue());
    }

    @SmallTest
    public void testDeserializingAnErrorWorks() {
        // Given error json from SnowShoe
        String validJson = "{\n" +
                "        \"receipt\": \"O9a2SXJxAqJ1KElX6EvIxZgS50I=\",\n" +
                "        \"created\": \"2013-05-29 22:59:29.891925\",\n" +
                "        \"secure\": false,\n" +
                "        \"error\": {\"message\": \"Stamp not found, try stamping again.\", \"code\": 32}\n" +
                "     }";

        // And GSON configured with the deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SnowShoeResponse.class, new SnowShoeResponseDeserializer())
                .create();

        // When deserializing the json
        SnowShoeResponse result = gson.fromJson(validJson, SnowShoeResponse.class);

        // Then the result is a response object containing the error, created date and receipt
        assertThat(result, notNullValue());
        assertThat(result.getError().getCode(), equalTo(32));
        assertThat(result.getError().getMessage(), equalTo("Stamp not found, try stamping again."));
        assertThat(result.isSecure(), is(false));
        assertThat(result.getReceipt(), equalTo("O9a2SXJxAqJ1KElX6EvIxZgS50I="));
        assertThat(result.getCreated(), DateMatchers.within(1, TimeUnit.MINUTES, 2013, Months.MAY, 29, 22, 59, 0, 0));

        // And the result should not contain a stamp
        assertThat(result.getStamp(), nullValue());
    }
}
