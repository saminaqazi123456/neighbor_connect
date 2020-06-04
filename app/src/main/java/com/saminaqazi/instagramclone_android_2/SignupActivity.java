package com.saminaqazi.instagramclone_android_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.parse.SignUpCallback;
import com.saminaqazi.instagramclone_android_2.ParseObjects.Category;

import java.util.List;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etZipCode;
    private EditText etRadius;
    private EditText etInterests;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etZipCode = findViewById(R.id.etPostalCode);
        etRadius = findViewById(R.id.etDistance);
        etInterests = findViewById(R.id.etInterests);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "CreateAccount button clicked");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                String zipCode = etZipCode.getText().toString();
                Float radius = Float.parseFloat(etRadius.getText().toString());
                String interests = etInterests.getText().toString();
                createUser(username, password, email,zipCode, radius, interests);
                finish();
            }
        });
    }

    private void createUser(String username, String password, String email, String zipCode, Float radius, String interests) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        //user.setUsername("joestevens");
        //user.setPassword("secret123");
        //user.setEmail("email@example.com");

        user.setUsername(username);
        user.setPassword(password);

        //check if null
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        // Set custom properties
        //user.put("phone", "650-253-0000");
        //zipcode
        user.put("location_user", zipCode);

        //radius
        user.put("radius", radius);

        //interests
        ParseQuery<Category> query = ParseQuery.getQuery(Category.class);
        String objectId = "Bj4eEnT0uH"; //default walking
        query.whereEqualTo("name", interests);

        query.findInBackground(new FindCallback<Category>() {
            public void done(List<Category> results, ParseException e) {
                if (e != null) {
                    // There was an error
                    Toast.makeText(SignupActivity.this, "Added category Walking instead.", Toast.LENGTH_SHORT).show();
                } else {
                    // results have all the Posts the current user liked.
                    for (ParseObject cat: results) {
                        String objectId = cat.getObjectId().toString();
                    }
                }
            }
        });

        user.put("category", ParseObject.createWithoutData("Categories", objectId));


        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(SignupActivity.this, "Signup Success!", Toast.LENGTH_SHORT).show();
                    //go to main screen
                    Intent i = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e(TAG, "Issue with signup", e);
                    Toast.makeText(SignupActivity.this, "Issue with signup!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
