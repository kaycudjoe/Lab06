package edu.calvin.cs262.lab06;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Reads openweathermap's RESTful API for weather forecasts.
 * The code is based on Deitel's WeatherViewer (Chapter 17), simplified based on Murach's NewsReader (Chapter 10).
 * <p>
 * for CS 262, lab 6\
 * @author Karen Cudjoe
 *
 * @author kvlinden
 * @version summer, 2016
 *
 * Answers to the questions
 * 1. What does the application do for invalid cities?
 *      The application catches the exception thrown and displays a toast with the message 'Failed to connect to service...' to the user.
 *      (When no value is entered into the edit Text box, and the fetch button is clicked on, a toast is displayed with the error message - 'Failed to connect to service...'.
 *      When just punctuations were entered in, and the fetch button is clicked on, a toast is displayed with the error message - 'Failed to connect to service...'.
 *      However, for the most part, the results displayed were inconsistent.
 *      The emulator displayed weather results sometimes when numbers were entered in and at other times, it displayed a toast with the same error message.
 *      Also, when I entered in non-real words, non-cities or a random set of letters which were obviously not places (cities), it actually produced some weather results.
 *      What I can conclude from this is that for the most part, the results are inconsistent and inaccurate. However, leaving the edit text box blank and using punctuations only, displays the error message I mentioned above).
 *
 * 2. What is the API key? What does it do?
 *      The API key is openweather_api_key.
 *      The API key identifies the user or requester of information and also insures a secure connection.
 *
 * 3. What does the full JSON response look like?
 *     This is the full JSON response. 10-14 17:38:30.831 2645-2774/edu.calvin.cs262.lab06 I/System.out: {"city":{"id":4994358,"name":"Grand Rapids","coord":{"lon":-85.668091,"lat":42.96336},"country":"US","population":0},"cod":"200","message":0.3409,"cnt":7,"list":[{"dt":1476464400,"temp":{"day":60.98,"min":48.16,"max":60.98,"night":48.16,"eve":54.39,"morn":60.98},"pressure":1008.53,"humidity":60,"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"speed":11.05,"deg":199,"clouds":0},{"dt":1476550800,"temp":{"day":65.88,"min":48.29,"max":70.9,"night":65.95,"eve":68.14,"morn":48.29},"pressure":1000.92,"humidity":80,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":16.8,"deg":209,"clouds":92,"rain":6.68},{"dt":1476637200,"temp":{"day":68.4,"min":56.08,"max":68.59,"night":61.09,"eve":56.77,"morn":60.58},"pressure":1001.19,"humidity":88,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":4.74,"deg":248,"clouds":36,"rain":2.4},{"dt":1476723600,"temp":{"day":65.52,"min":56.26,"max":66.99,"night":56.26,"eve":59.88,"morn":66.99},"pressure":992.7,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":14.52,"deg":279,"clouds":0,"rain":9.07},{"dt":1476810000,"temp":{"day":67.77,"min":56.52,"max":69.22,"night":59.11,"eve":69.22,"morn":56.52},"pressure":988.47,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":10.07,"deg":141,"clouds":37,"rain":9.97},{"dt":1476896400,"temp":{"day":58.05,"min":51.31,"max":58.05,"night":51.31,"eve":56.12,"morn":56.43},"pressure":990.01,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":15.73,"deg":279,"clouds":51,"rain":4.8},{"dt":1476982800,"temp":{"day":52.83,"min":41.67,"max":52.83,"night":41.67,"eve":48.85,"morn":48.6},"pressure":1004.63,"humidity":0,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":8.01,"deg":20,"clouds":58,"rain":0.92}]}
        10-14 17:38:39.631 2645-2667/edu.calvin.cs262.lab06 I/System.out: {"city":{"id":4994358,"name":"Grand Rapids","coord":{"lon":-85.668091,"lat":42.96336},"country":"US","population":0},"cod":"200","message":0.3409,"cnt":7,"list":[{"dt":1476464400,"temp":{"day":60.98,"min":48.16,"max":60.98,"night":48.16,"eve":54.39,"morn":60.98},"pressure":1008.53,"humidity":60,"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"speed":11.05,"deg":199,"clouds":0},{"dt":1476550800,"temp":{"day":65.88,"min":48.29,"max":70.9,"night":65.95,"eve":68.14,"morn":48.29},"pressure":1000.92,"humidity":80,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":16.8,"deg":209,"clouds":92,"rain":6.68},{"dt":1476637200,"temp":{"day":68.4,"min":56.08,"max":68.59,"night":61.09,"eve":56.77,"morn":60.58},"pressure":1001.19,"humidity":88,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":4.74,"deg":248,"clouds":36,"rain":2.4},{"dt":1476723600,"temp":{"day":65.52,"min":56.26,"max":66.99,"night":56.26,"eve":59.88,"morn":66.99},"pressure":992.7,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":14.52,"deg":279,"clouds":0,"rain":9.07},{"dt":1476810000,"temp":{"day":67.77,"min":56.52,"max":69.22,"night":59.11,"eve":69.22,"morn":56.52},"pressure":988.47,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":10.07,"deg":141,"clouds":37,"rain":9.97},{"dt":1476896400,"temp":{"day":58.05,"min":51.31,"max":58.05,"night":51.31,"eve":56.12,"morn":56.43},"pressure":990.01,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":15.73,"deg":279,"clouds":51,"rain":4.8},{"dt":1476982800,"temp":{"day":52.83,"min":41.67,"max":52.83,"night":41.67,"eve":48.85,"morn":48.6},"pressure":1004.63,"humidity":0,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":8.01,"deg":20,"clouds":58,"rain":0.92}]}
        10-14 17:39:05.460 2645-2670/edu.calvin.cs262.lab06 I/System.out: {"city":{"id":4994358,"name":"Grand Rapids","coord":{"lon":-85.668091,"lat":42.96336},"country":"US","population":0},"cod":"200","message":0.3409,"cnt":7,"list":[{"dt":1476464400,"temp":{"day":60.98,"min":48.16,"max":60.98,"night":48.16,"eve":54.39,"morn":60.98},"pressure":1008.53,"humidity":60,"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"speed":11.05,"deg":199,"clouds":0},{"dt":1476550800,"temp":{"day":65.88,"min":48.29,"max":70.9,"night":65.95,"eve":68.14,"morn":48.29},"pressure":1000.92,"humidity":80,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":16.8,"deg":209,"clouds":92,"rain":6.68},{"dt":1476637200,"temp":{"day":68.4,"min":56.08,"max":68.59,"night":61.09,"eve":56.77,"morn":60.58},"pressure":1001.19,"humidity":88,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":4.74,"deg":248,"clouds":36,"rain":2.4},{"dt":1476723600,"temp":{"day":65.52,"min":56.26,"max":66.99,"night":56.26,"eve":59.88,"morn":66.99},"pressure":992.7,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":14.52,"deg":279,"clouds":0,"rain":9.07},{"dt":1476810000,"temp":{"day":67.77,"min":56.52,"max":69.22,"night":59.11,"eve":69.22,"morn":56.52},"pressure":988.47,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":10.07,"deg":141,"clouds":37,"rain":9.97},{"dt":1476896400,"temp":{"day":58.05,"min":51.31,"max":58.05,"night":51.31,"eve":56.12,"morn":56.43},"pressure":990.01,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":15.73,"deg":279,"clouds":51,"rain":4.8},{"dt":1476982800,"temp":{"day":52.83,"min":41.67,"max":52.83,"night":41.67,"eve":48.85,"morn":48.6},"pressure":1004.63,"humidity":0,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":8.01,"deg":20,"clouds":58,"rain":0.92}]}
        10-14 17:39:06.569 2645-2774/edu.calvin.cs262.lab06 I/System.out: {"city":{"id":4994358,"name":"Grand Rapids","coord":{"lon":-85.668091,"lat":42.96336},"country":"US","population":0},"cod":"200","message":0.3409,"cnt":7,"list":[{"dt":1476464400,"temp":{"day":60.98,"min":48.16,"max":60.98,"night":48.16,"eve":54.39,"morn":60.98},"pressure":1008.53,"humidity":60,"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],"speed":11.05,"deg":199,"clouds":0},{"dt":1476550800,"temp":{"day":65.88,"min":48.29,"max":70.9,"night":65.95,"eve":68.14,"morn":48.29},"pressure":1000.92,"humidity":80,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":16.8,"deg":209,"clouds":92,"rain":6.68},{"dt":1476637200,"temp":{"day":68.4,"min":56.08,"max":68.59,"night":61.09,"eve":56.77,"morn":60.58},"pressure":1001.19,"humidity":88,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":4.74,"deg":248,"clouds":36,"rain":2.4},{"dt":1476723600,"temp":{"day":65.52,"min":56.26,"max":66.99,"night":56.26,"eve":59.88,"morn":66.99},"pressure":992.7,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":14.52,"deg":279,"clouds":0,"rain":9.07},{"dt":1476810000,"temp":{"day":67.77,"min":56.52,"max":69.22,"night":59.11,"eve":69.22,"morn":56.52},"pressure":988.47,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":10.07,"deg":141,"clouds":37,"rain":9.97},{"dt":1476896400,"temp":{"day":58.05,"min":51.31,"max":58.05,"night":51.31,"eve":56.12,"morn":56.43},"pressure":990.01,"humidity":0,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":15.73,"deg":279,"clouds":51,"rain":4.8},{"dt":1476982800,"temp":{"day":52.83,"min":41.67,"max":52.83,"night":41.67,"eve":48.85,"morn":48.6},"pressure":1004.63,"humidity":0,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":8.01,"deg":20,"clouds":58,"rain":0.92}]}
 *
 * 4. What does the system do with the JSON data?
 *    The system converts it into an array list called Weather and pulls out the information needed to assign the variables with the appropriate information.
 *
 * 5. What is the Weather class designed to do?
 *    The Weather class contains a Weather object that is used to display the Weather forecast. It also contains parameters that is needed for the Weather Forecast.
 */
public class MainActivity extends AppCompatActivity {

    private EditText cityText;
    private Button fetchButton;

    private List<Weather> weatherList = new ArrayList<>();
    private ListView itemsListView;

    /* This formater can be used as follows to format temperatures for display.
     *     numberFormat.format(SOME_DOUBLE_VALUE)
     */
    private NumberFormat numberFormat = NumberFormat.getInstance();

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityText = (EditText) findViewById(R.id.cityText);
        fetchButton = (Button) findViewById(R.id.fetchButton);
        itemsListView = (ListView) findViewById(R.id.weatherListView);

        // See comments on this formatter above.
        numberFormat.setMaximumFractionDigits(0);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(cityText);
                new GetWeatherTask().execute(createURL(cityText.getText().toString()));
            }
        });
    }

    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param city the target city
     * @return URL formatted for openweathermap.com
     */
    private URL createURL(String city) {
        try {
            String urlString = getString(R.string.web_service_url) +
                    URLEncoder.encode(city, "UTF-8") +
                    "&units=" + getString(R.string.openweather_units) +
                    "&cnt=" + getString(R.string.openweather_count) +
                    "&APPID=" + getString(R.string.openweather_api_key);
            return new URL(urlString);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    /**
     * Deitel's method for programmatically dismissing the keyboard.
     *
     * @param view the TextView currently being edited
     */
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Inner class for GETing the current weather data from openweathermap.org asynchronously
     */
    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println(result.toString());
                    return new JSONObject(result.toString());
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather) {
            if (weather != null) {
                //Log.d(TAG, weather.toString());
                convertJSONtoArrayList(weather);
                MainActivity.this.updateDisplay();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Converts the JSON weather forecast data to an arraylist suitable for a listview adapter
     *
     * @param forecast
     */
    private void convertJSONtoArrayList(JSONObject forecast) {
        weatherList.clear(); // clear old weather data
        try {
            JSONArray list = forecast.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);
                JSONObject temperatures = day.getJSONObject("temp");
                JSONObject weather = day.getJSONArray("weather").getJSONObject(0);
                weatherList.add(new Weather(
                        day.getLong("dt"),
                        temperatures.getDouble("min"),
                        temperatures.getDouble("max"),
                        weather.getString("description")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh the weather data on the forecast ListView through a simple adapter
     */
    private void updateDisplay() {
        if (weatherList == null) {
            Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (Weather item : weatherList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("day", item.getDay());
            map.put("description", item.getSummary());
            map.put("min", numberFormat.format(item.getMin()));
            map.put("max", numberFormat.format(item.getMax()));
            data.add(map);
        }

        int resource = R.layout.weather_item;
        String[] from = {"day", "description", "min", "max"};
        int[] to = {R.id.dayTextView, R.id.summaryTextView, R.id.mintextView, R.id.maxtextView};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }

}
