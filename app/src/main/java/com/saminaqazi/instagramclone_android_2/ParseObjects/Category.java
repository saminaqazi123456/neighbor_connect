package com.saminaqazi.instagramclone_android_2.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Categories")
public class Category extends ParseObject {

    public Category() {

    }

    public static final String KEY_NAME = "name";
    public static final String KEY_DESC = "description";
    public static final String KEY_IMAGE = "image_category";

    public String getName() {
        return getString(KEY_NAME);
    }

    public String getDescription() {
        return getString(KEY_DESC);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }
}
