package com.harlap.snowshoe;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * GSON deserializer for SnowShoe API responses.
 *
 *  Example Success:
 * {
 * "stamp": {"serial": "DEV-STAMP"},
 * "receipt": "o8FLLwYW7TVjtO4VTUP4IgQvvvI=",
 * "secure": false,
 * "created": "2013-05-24 23:01:23.274865"
 * }
 *
 * Example Error:
 * {
 * "receipt": "O9a2SXJxAqJ1KElX6EvIxZgS50I=",
 * "created": "2013-05-29 22:59:29.891925",
 * "secure": false,
 * "error": {"message": "Stamp not found, try stamping again.", "code": 32}
 * }
 */
class SnowShoeResponseDeserializer implements JsonDeserializer<SnowShoeResponse> {

    @Override
    public SnowShoeResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String receipt = jsonObject.has("receipt") ? jsonObject.get("receipt").getAsString() : null;
        boolean secure = jsonObject.has("secure") && jsonObject.get("secure").getAsBoolean();

        Date created = null;
        if (jsonObject.has("created")) {
            try {
                String createdString = jsonObject.get("created").getAsString();

                // SDF expects fractional seconds to be milliseconds and SnowShoe is returning microseconds without a timezone,
                // so we will ignore the fractional seconds rather than introduce a dependency on Joda-Time
                // (which would handle this correctly) or do more munging of the created timestamp.
                // Hopefully SnowShoe will move to a proper ISO8601 string or an integer unix time in milliseconds.

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                created = sdf.parse(createdString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SnowShoeResponse.Error error = null;
        if (jsonObject.has("error")) {
            error = jsonDeserializationContext.deserialize(jsonObject.get("error"), SnowShoeResponse.Error.class);
        }

        SnowShoeResponse.Stamp stamp = null;
        if (jsonObject.has("stamp")) {
            stamp = jsonDeserializationContext.deserialize(jsonObject.get("stamp"), SnowShoeResponse.Stamp.class);
        }

        SnowShoeResponse snowShoeResponse = new SnowShoeResponse();
        snowShoeResponse.setReceipt(receipt);
        snowShoeResponse.setCreated(created);
        snowShoeResponse.setSecure(secure);
        snowShoeResponse.setError(error);
        snowShoeResponse.setStamp(stamp);
        return snowShoeResponse;
    }
}
