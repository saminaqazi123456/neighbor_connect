package com.saminaqazi.instagramclone_android_2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.saminaqazi.instagramclone_android_2.ParseObjects.Category;
import com.saminaqazi.instagramclone_android_2.Post;
import com.saminaqazi.instagramclone_android_2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";
    AutoCompleteTextView actvSearch;

    List<String> allCategories;
    ArrayAdapter<String> adapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actvSearch = view.findViewById(R.id.actvSearch);

        allCategories = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), R.layout.fragment_search, allCategories);

    }

    protected void queryCategories() {
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        //query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        allCategories.clear();

        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> categories, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Category category : categories) {
                    Log.i(TAG, "Category: " + category.getName() + ", Description: " + category.getDescription());
                    allCategories.add(category.getName());
                }
            }
            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }

}