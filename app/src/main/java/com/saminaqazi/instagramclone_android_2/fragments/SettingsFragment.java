package com.saminaqazi.instagramclone_android_2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.saminaqazi.instagramclone_android_2.ParseObjects.Category;
import com.saminaqazi.instagramclone_android_2.Post;
import com.saminaqazi.instagramclone_android_2.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";
    private EditText etChangeUsername;
    private EditText etChangePassword;
    private EditText etChangeEmail;
    private EditText etChangeZipCode;
    //private EditText etChangeRadius;
    private Button btnSaveChanges;

    // SearchableSpinner
    private SearchableSpinner ssEditCategory;
    private ArrayAdapter spinnerAdapter;
    List<String> categoryNames;
    List<Category> allCategories;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ParseUser user = ParseUser.getCurrentUser();

        //set previous variables
        etChangeUsername = view.findViewById(R.id.etChangeUsername);
        etChangePassword = view.findViewById(R.id.etChangePassword);
        etChangeEmail = view.findViewById(R.id.etChangeEmail);
        etChangeZipCode = view.findViewById(R.id.etChangePostalCode);
        //etChangeRadius = view.findViewById(R.id.etChangeDistance);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        ssEditCategory = view.findViewById(R.id.ssEditCategory);

        etChangeUsername.setText(user.getUsername().toString());
        Log.i(TAG, "Username: "+user.getUsername().toString());
        if (user.getEmail() != null) {
            etChangeEmail.setText(user.getEmail().toString());
            Log.i(TAG, "Email: "+user.getEmail().toString());
        }
        etChangeZipCode.setText(user.getString("location_user").toString());
        Log.i(TAG, "ZipCode: "+user.getString("location_user").toString());
        //etChangeRadius.setText(user.getNumber("radius").toString());
        Log.i(TAG, "Radius: "+user.getNumber("radius").toString());

        //interest/category stuffs here
        categoryNames = new ArrayList<>();
        allCategories = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                categoryNames);
        ssEditCategory.setAdapter(spinnerAdapter);
        queryCategories();

        //update information in db here
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "SaveChanges button clicked");
                try {
                    String username = etChangeUsername.getText().toString();
                    String password = etChangePassword.getText().toString();
                    String email = etChangeEmail.getText().toString();
                    String zipCode = etChangeZipCode.getText().toString();
                    updateUser(username, password, email,zipCode);
                    Log.i(TAG, "Updated User");

                }
                catch (NullPointerException e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    private void updateUser(final String username, final String password, final String email, final String zipCode) {
        Log.i(TAG, "Updating User");
        final Category objectId = getObjectFromArray(ssEditCategory.getSelectedItem().toString());
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.include("category");
        String id = ParseUser.getCurrentUser().getObjectId();

        query.getInBackground(id, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                if (!username.isEmpty()) {
                    object.put("username", username);
                    ParseUser.getCurrentUser().setUsername(username);
                }
                if (!password.isEmpty()) {
                    object.put("password", password);
                    ParseUser.getCurrentUser().setPassword(password);
                }
                if (!email.isEmpty()) {
                    object.put("email", email);
                    ParseUser.getCurrentUser().setEmail(email);
                }
                if (!zipCode.isEmpty()) {
                    object.put("location_user", zipCode);
                    ParseUser.getCurrentUser().put("location_user", zipCode);
                }
                if (objectId != null) {
                    object.put("category", objectId);
                    ParseUser.getCurrentUser().put("category", objectId);
                }
                object.saveInBackground();
                Log.i(TAG, "Saved User Settings");
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
                    Log.i(TAG, "Category: " + category.getName() + ", Description: " + category.getDescription() +
                            "ID: " + category.getObjectId());
                    allCategories.add(category);
                    categoryNames.add(category.getName());
                }
                spinnerAdapter.notifyDataSetChanged();
                ssEditCategory.setSelection(getIndex(ssEditCategory, getUserCategory()));
            }
            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }

    protected String getUserCategory(){
        ParseUser user = ParseUser.getCurrentUser();
        Category category = (Category)user.get("category");
        String result = null;
        try {
            result = category.fetchIfNeeded().getString("name");
            Log.i(TAG, "User Category = " + result);
            return result;
        } catch (ParseException e) {
        }
        return "Pets";
    }

    private int getIndex(SearchableSpinner spinner, String string)
    {
        for (int i = 0; i < spinner.getCount(); ++i)
        {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)) {
                Log.i(TAG, "Item at Position: " + i);
                return i;
            }
        }
        return 0;
    }

    private Category getObjectFromArray(String name)
    {
        for (int i = 0; i < allCategories.size(); ++i)
        {
            if (allCategories.get(i).getName().equalsIgnoreCase(name))
                return (allCategories.get(i));
        }
        return null;
    }

}

//onClick listener
