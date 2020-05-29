package com.saminaqazi.instagramclone_android_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.module.AppGlideModule;
// Glide gives warning: Failed to find GeneratedAppGlideModule....silently ignored. Switching to ParseImageView.

import com.parse.ParseFile;
import com.parse.ui.widget.ParseImageView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        // private ImageView ivImage;
        private ParseImageView ivImage;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null) {
                //Glide.with(context).load(post.getImage().getUrl()).into(ivImage);

                ivImage.setParseFile(post.getImage());  // ivImage is ParseImageView, not ImageView
                ivImage.loadInBackground();

                // Parse Example:
                //imageView.setParseFile(post.getMedia());
                //imageView.loadInBackground()

                // Guide Example:
                //Glide.with(view.context).load(post.getMedia().getUrl()).into(imageView);
                // https://guides.codepath.org/android/Building-Data-driven-Apps-with-Parse#rendering-parsefile-objects

            }


        }
    }
}
