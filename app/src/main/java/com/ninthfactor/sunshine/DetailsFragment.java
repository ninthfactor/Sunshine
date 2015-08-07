package com.ninthfactor.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    public DetailsFragment() {
    }

  //  private ShareActionProvider mShareActionProvider;
    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String message;
    private ShareActionProvider shareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line for fragment to handle menu.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_fragment, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_detail_item_share);
        shareActionProvider = new ShareActionProvider(this.getActivity());
        MenuItemCompat.setActionProvider(menuItem, shareActionProvider);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection here.Action bar will automatically handle clicks on home/up
        //buttons as long as we specify parent activity in Android Manifest file.

        int id = item.getItemId();
        if (id == R.id.menu_detail_item_share) {
            //TODO: Code for Share Action intent.

            //mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        //    ShareActionProvider mShareActionProvider =
         //           (ShareActionProvider) MenuItemCompat.getActionProvider(item);

            // Attach an intent to this ShareActionProvider.  You can update this at any time,
            // like when the user selects a new piece of data they might like to share.

            if (ShareActionProvider != null) {
                ShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null?");
            }
        }

        return super.onOptionsItemSelected(item);

    }

    private Intent createShareForecastIntent() {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                                      message + FORECAST_SHARE_HASHTAG);
                    return shareIntent;
                }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent(); // Activity gets the intent. So we need first
                                // get the activity and then intent in it.
        message = intent.getStringExtra(ForecastFragment.EXTRA_MESSAGE);

        //  Get root view and then find the textview to update message from intent.
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        TextView detailWeather = (TextView) rootView.findViewById(R.id.detailWeather);
        detailWeather.setText(message);
        
        return rootView ;
    }
}
