package com.saminaqazi.instagramclone_android_2;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.saminaqazi.instagramclone_android_2.ParseObjects.Category;

import java.util.Objects;

@ParseClassName("Post")
public class Post extends ParseObject {

    public Post(){

    }

    public static final String KEY_USER = "author";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image_post";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_CATEGORIES = "categories";
    public static final String KEY_LOCATION = "location_post";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public Category getCategory() {
        return (Category) getParseObject(KEY_CATEGORIES);
    }

    public void setCategory(Category category) {
        put(KEY_CATEGORIES, category);
    }

    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public void setLocation(String location) {
        put(KEY_LOCATION, location);
    }
}
