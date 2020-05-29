package com.saminaqazi.instagramclone_android_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.saminaqazi.instagramclone_android_2.fragments.ComposeFragment;
import com.saminaqazi.instagramclone_android_2.fragments.LogoutFragment;
import com.saminaqazi.instagramclone_android_2.fragments.PostsFragment;
import com.saminaqazi.instagramclone_android_2.fragments.ProfileFragment_JavaClass;

/*
Parstagram links:
https://dashboard.heroku.com/apps/samina-parstagram/access
https://dashboard.heroku.com/apps/samina-parstagram/settings
https://samina-parstagram.herokuapp.com/

Terminal: parse-dashboard --appId samina-parstagram --masterKey CodepathMoveFastParse --serverURL "https://samina-parstagram.herokuapp.com/parse"

http://0.0.0.0:4040/apps/samina-parstagram/browser/Post

 */

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    //private File photoFile;
    //public String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        // Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                        fragment = new PostsFragment();
                        break;
                    case R.id.action_compose:
                        // Toast.makeText(MainActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_logout:
                        // Toast.makeText(MainActivity.this, "Compose!", Toast.LENGTH_SHORT).show();
                        fragment = new LogoutFragment();
                        //MainActivity.this.finish(); // Don't use this way because it works async and logs out user in the middle of the app
                        break;
                    case R.id.action_profile:
                    default:
                        // Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                        fragment = new ProfileFragment_JavaClass();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}
