package com.saminaqazi.instagramclone_android_2;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.saminaqazi.instagramclone_android_2.ParseObjects.Category;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Category.class);
        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("neighbor-connect") // should correspond to APP_ID env variable
                .clientKey("TeamOneCodePathSpring2020")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://neighbor-connect.herokuapp.com/parse/").build());
    }
}
