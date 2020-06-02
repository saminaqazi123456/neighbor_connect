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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.saminaqazi.instagramclone_android_2.Post;
import com.saminaqazi.instagramclone_android_2.PostsAdapter;
import com.saminaqazi.instagramclone_android_2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int LIMIT_QUERY = 20;

    RecyclerView rvProfilePosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProfilePosts = view.findViewById(R.id.rvProfilePosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        rvProfilePosts.setAdapter(adapter);

        rvProfilePosts.setLayoutManager(new LinearLayoutManager(getContext()));

        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(LIMIT_QUERY); // set to 20
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        allPosts.clear();
        adapter.notifyDataSetChanged();

        List<Post> ProfilePosts = new ArrayList<>();

        try {
            List<Post> postRecord = query.whereContains("author", ParseUser.getCurrentUser().getObjectId().toString()).find();
            for (Post record: postRecord) {
                if (record != null)
                    {ProfilePosts.add(record);}
            }
            Log.i(TAG, "Got the posts len : " + ProfilePosts.size());
        } catch (ParseException e) {
            Log.e(TAG, "Issue with getting profile posts", e);
        }
        allPosts.addAll(ProfilePosts);
        adapter.notifyDataSetChanged();

    }



}
