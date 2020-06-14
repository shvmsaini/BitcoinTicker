package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";
    private final String public_key= "enter your public key here";

    // Member Variables:
    TextView mPriceTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner =  findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Bitcoin", "" + adapterView.getItemAtPosition(i));
                String final_url = BASE_URL + adapterView.getItemAtPosition(i).toString();
                Log.d("Bitcoin", final_url);
                letsDoSomeNetworking(final_url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin","Nothing Selected");
            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("x-ba-key",public_key);

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                Log.d("Bitcoin","JSON:"+ responseBody.toString());
                String price = null;
                try {
                    price = responseBody.getString("last");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPriceTextView.setText(price);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error,JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Fail response: " + statusCode);
                Log.e("ERROR", error.toString());
            }
        });
    }


}
