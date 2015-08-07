package com.ninthfactor.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent(); // Activity gets the intent. So we need first
                                // get the activity and then intent in it.
        String message = intent.getStringExtra(ForecastFragment.EXTRA_MESSAGE);

        //  Get root view and then find the textview to update message from intent.
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        TextView detailWeather = (TextView) rootView.findViewById(R.id.detailWeather);
        detailWeather.setText(message);
        
        return rootView ;
    }
}
