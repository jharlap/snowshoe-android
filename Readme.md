# SnowShoe Android Library

This Android library makes it easy to add [SnowShoe stamp](http://www.snowshoestamp.com/) functionality to an app.

There are two main ways to use the library, depending on whether you use a SnowShoe SDK on your own server or whether you want the Android app to talk directly to SnowShoe's servers.

## Installation

1. Clone the repo
2. Import the snowshoe library project into Android Studio.
3. In your app project, add a dependency on the library project:

    ```gradle
    dependencies {
        compile project(':snowshoe')
    }
    ```

4. Subclass either abstract activity class.

## Using your own server to verify stamps

If you want to send the stamp coordinates to your own server for verification, subclass `AbstractStampSensingActivity` and override the `onStampTouch(StampTouch stamp)` method, and then use `stamp.getPoints()` to retrieve the list of points. Points are stored as a `Pair<Float,Float>`, so access the fields `first` and `second` for X and Y coordinates, respectively.

## Talking directly to SnowShoe's servers from the app

If you prefer to do stamp verification within the Android app, then subclass `AbstractStampVerifyingActivity`, set `SNOWSHOE_APP_KEY` and `SNOWSHOE_APP_SECRET` within `onCreate()`, and override `onStampVerified(SnowShoeResponse response)` to handle the data returned from SnowShoe's API. Note that the response is deserialized using Gson, and you can check for errors returned by the API with `response.getError()` and retrieve the stamp serial number with `response.getStamp().getSerial()`.

Also ensure that your app includes the `android.permission.INTERNET` permission.
