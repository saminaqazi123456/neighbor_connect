package com.saminaqazi.instagramclone_android_2.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.saminaqazi.instagramclone_android_2.ParseObjects.Category;
import com.saminaqazi.instagramclone_android_2.Post;
import com.saminaqazi.instagramclone_android_2.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeFragment extends Fragment {

    private static final String TAG = "ComposeFragment";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File photoFile;
    private String photoFileName = "photo.jpg";

    EditText etComposeBody;
    SearchableSpinner ssComposeCategory;
    private ImageView ivPostImage;
    Button btnShare;
    Button btnCaptureImage;

    List<String> categoryNames;
    List<Category> allCategories;

    ArrayAdapter spinnerAdapter;

    public ComposeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etComposeBody = view.findViewById(R.id.etComposeBody);
        ssComposeCategory = view.findViewById(R.id.ssComposeCategory);
        btnShare = view.findViewById(R.id.btnShare);
        btnCaptureImage = view.findViewById(R.id.btnImageCapture);
        ivPostImage = view.findViewById(R.id.ivPostImage);

        categoryNames = new ArrayList<>();
        allCategories = new ArrayList<>();

        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                            categoryNames);

        ssComposeCategory.setAdapter(spinnerAdapter);

        queryCategories();

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Posting: ");
                Log.i(TAG, "Text:" + etComposeBody.getText());
                Log.i(TAG, "Category:" + ssComposeCategory.getSelectedItem().toString());
                Log.i(TAG, "Location " + ParseUser.getCurrentUser().get("location_user"));
                String description = etComposeBody.getText().toString();
                if (description.length() <= 0) {
                    Toast.makeText(getContext(), "Description Cannot Be Empty", Toast.LENGTH_SHORT).show();
                }
                if (photoFile == null || ivPostImage.getDrawable() == null)
                {
                    Toast.makeText(getContext(), "Photo can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                savePost(etComposeBody.getText().toString(), ParseUser.getCurrentUser(),
                            ssComposeCategory.getSelectedItem().toString(), photoFile);
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "launchCamera() starting");
                launchCamera();
            }
        });

    }

    protected void queryCategories() {
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        //query.include(Post.KEY_USER);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        categoryNames.clear();

        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> categories, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Category category : categories) {
                    Log.i(TAG, "Category: " + category.getName() + ", Description: " + category.getDescription());
                    allCategories.add(category);
                    categoryNames.add(category.getName());
                }
                spinnerAdapter.notifyDataSetChanged();
            }
            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }

    private void savePost(String description, ParseUser currentUser, String cString, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setCategory(getCategoryFromArray(cString));
        String location = (String) currentUser.get("location_user");
        Log.i(TAG, "Location post: " + location);
        post.setLocation(location);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error While Saving", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Post save was successful!");
                etComposeBody.setText("");
                ivPostImage.setImageResource(0);
            }
        });
    }

    private Category getCategoryFromArray(String cString) {
        for (Category c : allCategories)
        {
            if (c.getName().equals(cString))
                return c;
        }
        return null;
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application\
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        Log.i(TAG, "INSIDE launchCamera");
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath2.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            Log.i(TAG, "startActivityForResult starting");
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else {
            Log.i(TAG, "startActivityForResult NOT started");
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "INSIDE onActivityResult");
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*

    private File photoFile;
    public String photoFileName = "photo.jpg";

    public ComposeFragment() {
        // Required empty public constructor
    }


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription = view.findViewById(R.id.etDescription);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "launchCamera() starting");
                launchCamera();
            }
        });

        // queryPosts();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description,currentUser, photoFile);
            }
        });



    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application\
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        Log.i(TAG, "INSIDE launchCamera");
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            Log.i(TAG, "startActivityForResult starting");
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else {
            Log.i(TAG, "startActivityForResult NOT started");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "INSIDE onActivityResult");
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }


    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!!");
                Toast.makeText(getContext(), "Post save was successful!!", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
                ivPostImage.setImageResource(0);  // empty resource id
            }
        });
    }
*/
}
