package com.example.android.moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {


    public static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    int myOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//         myOrientation = display.getOrientation();
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.movie_detail_container) != null) {

            mTwoPane = true;
            Log.v("mTwopane ", "true");



        } else {
            mTwoPane = false;

            Log.v("mTwopane ", "false");
        }

    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if(newConfig.orientation != myOrientation)
//            Log.v("loool ", "rotated");
//        super.onConfigurationChanged(newConfig);
//
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

}
