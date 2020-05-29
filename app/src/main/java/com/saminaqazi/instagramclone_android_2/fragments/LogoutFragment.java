package com.saminaqazi.instagramclone_android_2.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;
import com.saminaqazi.instagramclone_android_2.MainActivity;
import com.saminaqazi.instagramclone_android_2.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutFragment extends Fragment {

    private TextView txtCount;

    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtCount = view.findViewById(R.id.txtCount);

        // There's a TextView txtCount in Main Activity

        // https://stackoverflow.com/questions/31041884/execute-function-after-5-seconds-in-android

        final int secs = 5;
        new CountDownTimer((secs +1) * 1000, 1000) // Wait 5 secs, tick every 1 sec
        {
            @Override
            public final void onTick(final long millisUntilFinished)
            {
                txtCount.setText("" + (int) (millisUntilFinished * .001f));
            }
            @Override
            public final void onFinish()
            {

                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null

                // Can exit from here as below but better is to return to the activity and then exit.
                getActivity().finish();

                //txtCount.setText("GO!");
                //finish();
                // Time's up - Start the Login Activity
                //final Intent tnt =
                //        new Intent(getApplicationContext(), LoginActivity.class);
                //startActivity(tnt);
            }
        }.start();

        //ParseUser.logOut();
        //ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null

        // Can exit from here as below but better is to return to the activity and then exit.
        //getActivity().finish();


    }


}
