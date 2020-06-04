package com.saminaqazi.instagramclone_android_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
        //user.setEmail(email);
        // Set custom properties
        //user.put("phone", "650-253-0000");
        //zipcode
        user.put("location_user", zipCode);
        //radius
        user.put("radius", radius);
        //interests
        user.put("category", ParseObject.createWithoutData("Categories", interests));

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(SignupActivity.this, "Signup Success!", Toast.LENGTH_SHORT).show();
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
