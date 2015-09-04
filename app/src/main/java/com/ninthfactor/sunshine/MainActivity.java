package com.ninthfactor.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private String mLocation;
    private final String FORECASTFRAGMENT_TAG = "FFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("Life-Cycle","onCreate");

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new ForecastFragment(),
//                                    FORECASTFRAGMENT_TAG)
//                    .commit();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("Life-Cycle","onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Life-Cycle","onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Life-Cycle", "onResume");
        String location = Utility.getPreferredLocation( this );
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
        //    ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Life-Cycle","onStart");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Life-Cycle","onDestroy");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

//            return true;
        }

        // If clicked on Show on Map menu item.

        if (id == R.id.map_location) {

            // get the location from shared preference

//            SharedPreferences sharedPrefs =
//                    PreferenceManager.getDefaultSharedPreferences(this);
//            String location = sharedPrefs.getString(
//                    getString(R.string.pref_location_key),
//                    getString(R.string.pref_location_default));

            String location = Utility.getPreferredLocation(this);



            double latitude = 0;
            double longitude = 0;
            String uriBegin = "geo:" + latitude + "," + longitude;
            // String query = "cochin";
            String encodedQuery = Uri.encode(location);
            String uriString = uriBegin + "?q=" + encodedQuery;
            Uri uri = Uri.parse(uriString);


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
            }


//            return true;
        }




        return super.onOptionsItemSelected(item);
    }
}
