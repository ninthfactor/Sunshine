package com.ninthfactor.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ninthfactor.sunshine.data.WeatherContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public ForecastFragment() {
    }

    public final static String EXTRA_MESSAGE = "com.ninthfactor.sunshine.MESSAGE";
    private static final int FORECAST_LOADER = 0;
    private ForecastAdapter  mForecastadapter;

    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line for fragment to handle menu.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection here.Action bar will automatically handle clicks on home/up
        //buttons as long as we specify parent activity in Android Manifest file.

        int id = item.getItemId();
        if (id == R.id.action_refresh){
            updateWeather();
        }

        return super.onOptionsItemSelected(item);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the fragmentview

//        String locationSetting = Utility.getPreferredLocation(getActivity());


        /*   Refactoring. Remove the initial dummy data load.

        String[] forecastArray = {
                "Today- Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Wednesday - Cloudy - 72/63",
                "Thursday - Asteroids - 75/63",
                "Friday - Heavy Rain - 65/55",
                "Sunday - Sunny - 90/85"
        };

        //Create an array list from above array

        List<String> weekForecast = new ArrayList<String>(
                Arrays.asList(forecastArray));
        */

        // Initialize adapter

//        mForecastadapter = new ArrayAdapter<String>(
//                // use getActivity to get the current context
//                getActivity(),
//                //Id of the list item layout. This is the layout itself
//                R.layout.list_item_forecast,
//                //  Id of textview inside list item layout
//                R.id.list_item_forecast_textview,
//                //forecast data
//                new ArrayList<String>());  // Send an empty array list


//        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
//        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
//                               locationSetting, System.currentTimeMillis());
//
//        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
//                                null, null, null, sortOrder);

        // The CursorAdapter will take data from our cursor and populate the ListView
        // However, we cannot use FLAG_AUTO_REQUERY since it is deprecated, so we will end
        // up with an empty list the first time we run.
        //  mForecastadapter = new ForecastAdapter(getActivity(), cur, 0);

        mForecastadapter = new ForecastAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get reference to listview

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastadapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                String forecast = mForecastadapter.getItem(position);
//                // intead of accessing mForecastadapater from inner class,we can also do below
//                //String forecast = (String) adapterView.getItemAtPosition(position);
//                Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), Details.class);
//                intent.putExtra(EXTRA_MESSAGE, forecast);
//                startActivity(intent);
//
//
//            }
//
//
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String locationSetting = Utility.getPreferredLocation(getActivity());
                    Intent intent = new Intent(getActivity(), Details.class)
                            .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                    locationSetting, cursor.getLong(COL_WEATHER_DATE)
                            ));
                    startActivity(intent);
                }
            }
        });


        return rootView;
    }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
                getLoaderManager().initLoader(FORECAST_LOADER, null, this);
                super.onActivityCreated(savedInstanceState);
            }


    // since we read the location when we create the loader, all we need to do is restart things
    void onLocationChanged( ) {
        updateWeather();
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }


    private void updateWeather(){

// After we implemented content provider,we refactor the code to use the new FetchWeatherTask class.

//        FetchWeatherTask weatherTask = new FetchWeatherTask();

//          FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastadapter);

        // Get the location from preference.xml

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String location = prefs.getString(getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
        // Pass the zip code to execute method.
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);
    }

    @Override

//    public void onStart(){
//        super.onStart();
//        updateWeather();
//    }


        //@Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                String locationSetting = Utility.getPreferredLocation(getActivity());

                        // Sort order:  Ascending, by date.
                                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                                locationSetting, System.currentTimeMillis());

                        return new CursorLoader(getActivity(),
                                weatherForLocationUri,
                                FORECAST_COLUMNS,
                                null,
                                null,
                                sortOrder);
            }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                mForecastadapter.swapCursor(cursor);
            }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
                mForecastadapter.swapCursor(null);
            }

    // AS PART OF REFACTRORING AFTER IMPLEMENTING WEATHER PROVIDER, WE REMOVE BELOW INNER CLASS



//    // Add code for url connection here.
//
//    // These two need to be declared outside the try/catch
//    // so that they can be closed in the finally block.
//
//
//    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {  // 1. String is added
//                    // to generic to accept the zip code from .execute
//                    // 3. String[] is added because we get an array of daily forecast from
//                    // doInBackground()
//
//        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
//
//
//
//        /* The date/time conversion code is going to be moved outside the asynctask later,
//         * so for convenience we're breaking it out into its own method now.
//         */
//        private String getReadableDateString(long time){
//            // Because the API returns a unix timestamp (measured in seconds),
//            // it must be converted to milliseconds in order to be converted to valid date.
//            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
//            return shortenedDateFormat.format(time);
//        }
//
//        /**
//         * Prepare the weather high/lows for presentation.
//         */
//
//        private String formatHighLows(double high, double low, String unitType) {
//
//            if (unitType.equals(getString(R.string.pref_units_imperial))) {
//                high = (high * 1.8) + 32;
//                low = (low * 1.8) + 32;
//            } else if (!unitType.equals(getString(R.string.pref_units_metric))) {
//                Log.d(LOG_TAG, "Unit type not found: " + unitType);
//            }
//
//
//            // For presentation, assume the user doesn't care about tenths of a degree.
//            long roundedHigh = Math.round(high);
//            long roundedLow = Math.round(low);
//
//            String highLowStr = roundedHigh + "/" + roundedLow;
//            return highLowStr;
//        }
//
//
//
//        /**
//        * Take the String representing the complete forecast in JSON Format and
//        * pull out the data we need to construct the Strings needed for the wireframes.
//                *
//                * Fortunately parsing is easy:  constructor takes the JSON string and converts it
//        * into an Object hierarchy for us.
//        */
//
//
//        private String[] getWeatherDataFromJson( String forecastJsonStr, int numDays)
//            throws JSONException {
//
//            final String OWM_LIST = "list";
//            final String OWM_WEATHER = "weather";
//            final String OWM_TEMPERATURE = "temp";
//            final String OWM_MAX = "max";
//            final String OWM_MIN ="min";
//            final String OWM_DESCRIPTION = "main";
//
//            JSONObject forecastJSON = new JSONObject(forecastJsonStr);
//            JSONArray weatherArray = forecastJSON.getJSONArray(OWM_LIST);
//
//            // OWM returns daily forecasts based upon the local time of the city that is being
//            // asked for, which means that we need to know the GMT offset to translate this data
//            // properly.
//
//            // Since this data is also sent in-order and the first day is always the
//            // current day, we're going to take advantage of that to get a nice
//            // normalized UTC date for all of our weather
//
//            Time dayTime = new Time();
//            dayTime.setToNow();
//
//            // we start at the day returned by local time. Otherwise this is a mess.
//            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
//
//            // now we work exclusively in UTC
//            dayTime = new Time();
//
//            String[] resultStrs = new String[numDays];
//
//
//            // Data is fetched in Celsius by default.
//            // If user prefers to see in Fahrenheit, convert the values here.
//            // We do this rather than fetching in Fahrenheit so that the user can
//            // change this option without us having to re-fetch the data once
//            // we start storing the values in a database.
//            SharedPreferences sharedPrefs =
//                    PreferenceManager.getDefaultSharedPreferences(getActivity());
//            String unitType = sharedPrefs.getString(
//                    getString(R.string.pref_units_key),
//                    getString(R.string.pref_units_metric));
//
//
//
//            for ( int i = 0; i < weatherArray.length();i++ ){
//
//                // For now, using the format "Day, description, hi/low"
//
//                String day;
//                String description;
//                String highAndLow;
//
//                // Get the JSON object representing the day
//
//                JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//
//                // The date/time is returned as a long.  We need to convert that
//                // into something human-readable, since most people won't read "1400356800" as
//                // "this saturday".
//                long dateTime;
//                // Cheating to convert this to UTC time, which is what we want anyhow
//
//                dateTime = dayTime.setJulianDay(julianStartDay+i);
//                day = getReadableDateString(dateTime);
//
//                // description is in a child array called "weather", which is 1 element long.
//                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//                description = weatherObject.getString(OWM_DESCRIPTION);
//
//                // Temperatures are in a child object called "temp".  Try not to name variables
//                // "temp" when working with temperature.  It confuses everybody.
//
//                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//
//                double high = temperatureObject.getDouble(OWM_MAX);
//                double low = temperatureObject.getDouble(OWM_MIN);
//
//                highAndLow = formatHighLows(high, low, unitType);
//
//                resultStrs[i] = day + " - " + description + " - " + highAndLow;
//
//            }
//
//            for (String s : resultStrs){
//                Log.v(LOG_TAG,"Forecast entry"+ s);
//            }
//
//            return resultStrs;
//        }
//
//
//        @Override
//        protected String[] doInBackground(String... params) {     // param will get the zip code.
//
//            // These two need to be declared outside the try/catch
//            // so that they can be closed in the finally block.
//
//
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//            // Will contain the raw JSON response as a string.
//            String forecastJsonStr = null;
//            String format = "json";
//            String units = "metric";
//            int numDays  = 7;
//            // This will be final summary string array from this method.
//            String[] dailyWeatherSummary = new  String[numDays];
//
//
//            try {
//                // Construct the URL for the OpenWeatherMap query
//                // Possible parameters are available at OWM's forecast API page, at
//                // http://openweathermap.org/API#forecast
//
//                // We will use Uri class to construct the url
//
//                final String FORECAST_BASE_URL =
//                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
//                final String QUERY_PARAM = "q" ;
//                final String FORMAT_PARAM = "mode" ;
//                final String UNITS_PARAM = "units";
//                final String DAYS_PARAM = "cnt";
//
//                Uri buildUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
//                        .appendQueryParameter(QUERY_PARAM, params[0])
//                        .appendQueryParameter(FORMAT_PARAM, format)
//                        .appendQueryParameter(UNITS_PARAM,units)
//                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
//                        .build();
//
//
//                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
//
//                URL url = new URL(buildUri.toString());
//
//
//                // Create the request to OpenWeatherMap, and open the connection
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // Nothing to do.
//                    forecastJsonStr = null;
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                    // But it does make debugging a *lot* easier if you print out the completed
//                    // buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // Stream was empty.  No point in parsing.
//                    forecastJsonStr = null;
//                }
//                forecastJsonStr = buffer.toString();
//
//
//                // Display weather data to the Log.
//
//                Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);
//
//                // Remove after debug
//
//
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error ", e);
//                // If the code didn't successfully get the weather data, there's no point in attempting
//                // to parse it.
//                forecastJsonStr = null;
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e(LOG_TAG, "Error closing stream", e);
//                    }
//                }
//
//            }
//
//            // Call JSON parser and pass the response from weather API. This must be added in a
//            // try - catch block because parsor method is written as if it will throw an exception
//            // So we can call it only only a try catch block.
//
//            try {
//
//                dailyWeatherSummary = getWeatherDataFromJson(forecastJsonStr, numDays);
//                return dailyWeatherSummary;
//
//            }catch (JSONException e){
//                Log.e(LOG_TAG, e.getMessage(), e);
//                e.printStackTrace();
//            }
//
//            // This will only happen if there was an error getting or parsing the forecast.
//            // In that case, we are sending back a null from doinbackground.
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String[] result) {
//            if (result != null) {
//                mForecastadapter.clear();
//                for(String dayForecastStr : result) {
//                    mForecastadapter.add(dayForecastStr);
//                                    }
//                // New data is back from the server.  Hooray!
//            }
//        }
//
//
//    }

}