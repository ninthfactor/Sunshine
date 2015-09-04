package com.ninthfactor.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninthfactor.sunshine.data.WeatherContract;
import com.ninthfactor.sunshine.data.WeatherContract.WeatherEntry;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public DetailsFragment() {
    }

  //  private ShareActionProvider mShareActionProvider;
    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
//    private String mForecastStr;

    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATE,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            WeatherEntry.COLUMN_HUMIDITY,
            WeatherEntry.COLUMN_PRESSURE,
            WeatherEntry.COLUMN_WIND_SPEED,
            WeatherEntry.COLUMN_DEGREES,
            WeatherEntry.COLUMN_WEATHER_ID,
            // This works because the WeatherProvider returns location data joined with
            // weather data, even though they're stored in two different tables.
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };


    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_WEATHER_HUMIDITY = 5;
    public static final int COL_WEATHER_PRESSURE = 6;
    public static final int COL_WEATHER_WIND_SPEED = 7;
    public static final int COL_WEATHER_DEGREES = 8;
    public static final int COL_WEATHER_CONDITION_ID = 9;


    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line for fragment to handle menu.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.

//        ShareActionProvider mShareActionProvider =
//                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
//        if (mShareActionProvider != null ) {
        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        } else {
//            Log.d(LOG_TAG, "Share Action Provider is null?");
        }

    }

    /*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection here.Action bar will automatically handle clicks on home/up
        //buttons as long as we specify parent activity in Android Manifest file.

        int id = item.getItemId();
        if (id == R.id.action_share) {
            //TODO: Code for Share Action intent.

        }

        return super.onOptionsItemSelected(item);

    }

    */

    private Intent createShareForecastIntent() {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(Intent.EXTRA_TEXT,
//                                      mForecastStr + FORECAST_SHARE_HASHTAG);
                  shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
                    return shareIntent;
                }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //  Get root view and then find the textview to update message from intent.
//        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
//
//        Intent intent = getActivity().getIntent(); // Activity gets the intent. So we need first
                                // get the activity and then intent in it.
//        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//            mForecastStr = intent.getStringExtra(ForecastFragment.EXTRA_MESSAGE);
//        }

//        if (intent != null) {
//            mForecastStr = intent.getDataString();
//        }
//
//        if (null != mForecastStr) {
//
//            ((TextView) rootView.findViewById(R.id.detailWeather)).setText(mForecastStr);
//        }
        
//        return rootView ;
      //  return inflater.inflate(R.layout.fragment_details, container, false);
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
        mHighTempView = (TextView) rootView.findViewById(R.id.detail_high_textview);
        mLowTempView = (TextView) rootView.findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mPressureView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                DETAIL_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
//        if (!data.moveToFirst()) { return; }
//
//        String dateString = Utility.formatDate(
//                data.getLong(COL_WEATHER_DATE));
//
//        String weatherDescription =
//                data.getString(COL_WEATHER_DESC);
//
//        boolean isMetric = Utility.isMetric(getActivity());
//
//        String high = Utility.formatTemperature(getActivity(),
//                data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
//
//        String low = Utility.formatTemperature(getActivity(),
//                data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
//
//        mForecast = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);
//
//        TextView detailTextView = (TextView)getView().findViewById(R.id.detailWeather);
//        detailTextView.setText(mForecast);
//
//        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }
        if (data != null && data.moveToFirst()) {
            // Read weather condition ID from cursor
            int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
            // Use placeholder Image
            //mIconView.setImageResource(R.drawable.ic_launcher);
			
			 // Use weather art image
             mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));
			 
            // Read date from cursor and update views for day of week and date
            long date = data.getLong(COL_WEATHER_DATE);
            String friendlyDateText = Utility.getDayName(getActivity(), date);
            String dateText = Utility.getFormattedMonthDay(getActivity(), date);
            mFriendlyDateView.setText(friendlyDateText);
            mDateView.setText(dateText);

            // Read description from cursor and update view
            String description = data.getString(COL_WEATHER_DESC);
            mDescriptionView.setText(description);

            // Read high temperature from cursor and update view
            boolean isMetric = Utility.isMetric(getActivity());

            double high = data.getDouble(COL_WEATHER_MAX_TEMP);
            String highString = Utility.formatTemperature(getActivity(), high, isMetric);
            mHighTempView.setText(highString);

            // Read low temperature from cursor and update view
            double low = data.getDouble(COL_WEATHER_MIN_TEMP);
            String lowString = Utility.formatTemperature(getActivity(), low, isMetric);
            mLowTempView.setText(lowString);

            // Read humidity from cursor and update view
            float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
            mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

            // Read wind speed and direction from cursor and update view
            float windSpeedStr = data.getFloat(COL_WEATHER_WIND_SPEED);
            float windDirStr = data.getFloat(COL_WEATHER_DEGREES);
            mWindView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));

            // Read pressure from cursor and update view
            float pressure = data.getFloat(COL_WEATHER_PRESSURE);
            mPressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

            // We still need this for the share intent
            mForecast = String.format("%s - %s - %s/%s", dateText, description, high, low);

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}

