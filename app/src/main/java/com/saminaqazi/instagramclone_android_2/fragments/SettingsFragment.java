package com.saminaqazi.instagramclone_android_2.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.saminaqazi.instagramclone_android_2.ParseObjects.Category;
import com.saminaqazi.instagramclone_android_2.R;

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
    private EditText etChangeInterests;
    private Button btnSaveChanges;


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
        etChangeEmail = view.findViewById(R.id.etChangeEmail);
        etChangeZipCode = view.findViewById(R.id.etChangePostalCode);
        //etChangeRadius = view.findViewById(R.id.etChangeDistance);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);

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


        //update information in db here
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i(TAG, "SaveChanges button clicked");
                /*try {
                    String username = etChangeUsername.getText().toString();
                    String password = etChangePassword.getText().toString();
                    String email = etChangeEmail.getText().toString();
                    String zipCode = etChangeZipCode.getText().toString();
                    Float radius = Float.parseFloat(etChangeRadius.getText().toString());
                    String interests = etChangeInterests.getText().toString();
                    updateUser(username, password, email,zipCode, radius, interests);
                }
                catch (NullPointerException) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }*/

                //finish();
                Toast.makeText(getContext(), "This is not yet implemented, sorry!", Toast.LENGTH_SHORT).show();
            }

            private void updateUser(String username, String password, String email, String zipCode, String interests) {
                //implement this
            }
        });


        return view;
    }
}

//onClick listener
