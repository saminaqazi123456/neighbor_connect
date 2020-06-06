package com.saminaqazi.instagramclone_android_2.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.saminaqazi.instagramclone_android_2.ParseObjects.Category;
import com.saminaqazi.instagramclone_android_2.Post;
import com.saminaqazi.instagramclone_android_2.PostsAdapter;
import com.saminaqazi.instagramclone_android_2.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";
    EditText etSearch;
    Button btnSearchPosts;

    ArrayAdapter<String> adapter;
    protected PostsAdapter rvAdapter;


    protected RecyclerView rvSearchPosts;
    protected List<Post> allPosts;


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

        etSearch = view.findViewById(R.id.etSearch);
        rvSearchPosts = view.findViewById(R.id.rvSearchPosts);
        btnSearchPosts = view.findViewById(R.id.btnSearchPosts);

        allPosts = new ArrayList<>();

        rvAdapter = new PostsAdapter(getContext(), allPosts);
        rvSearchPosts.setAdapter(rvAdapter);
        rvSearchPosts.setLayoutManager(new LinearLayoutManager(getContext()));


        btnSearchPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Finding Search");
                String selection = etSearch.getText().toString();
                queryPosts(selection);
            }
        });
/*
        ssComposeCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String)parent.getItemAtPosition(position);
                Toast.makeText(getContext(), selection, Toast.LENGTH_SHORT).show();
                queryPostsByCategory(selection);
            }
        });
  */
        //ssComposeCategory.onSearchableItemClicked(adapter.getItem(adapter.getPosition(ssComposeCategory.getSelectedItem().toString())), 0);
    }

    protected void queryPosts(final String catName) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.include(Post.KEY_CATEGORIES);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.whereContains(Post.KEY_DESCRIPTION, catName);

        allPosts.clear();
        rvAdapter.notifyDataSetChanged();

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() +
                            ", username: " + post.getUser().getUsername());
                    allPosts.add(post);
                }
                rvAdapter.notifyDataSetChanged();
            }
            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }


}