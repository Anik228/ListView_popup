package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView mListView;
    private NodeListAdapter adapter;
    private ArrayList<NodeData> nodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started.");


        AlertDialog.Builder alertDialog = new
                AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.row, null);

        mListView = rowList.findViewById(R.id.listViewnew);



        //mListView = findViewById(R.id.listView);
        nodeList = new ArrayList<>();

        adapter = new NodeListAdapter(this, R.layout.list_view, nodeList);
        mListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        AlertDialog dialog = alertDialog.create();
        dialog.show();


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Node ID " + nodeList.get(i).getID(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });



        if (isNetworkAvailable()) {
            FetchNodeDataTask fetchNodeDataTask = new FetchNodeDataTask();
            fetchNodeDataTask.execute();
        } else {
            Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show();
        }
    }



    private class FetchNodeDataTask extends AsyncTask<Void, Void, String> {
        private static final String API_URL = "https://lgorithmbd.com/php_rest_app/api/nodeinfo/read.php";

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String result = null;

            try {
                // Create the URL object
                URL url = new URL(API_URL);

                // Create the HTTP connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Connect to the API
                urlConnection.connect();

                // Read the response
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    result = builder.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close the connections and readers
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // Parse the JSON response
                    JSONObject response = new JSONObject(result);
                    JSONArray data = response.getJSONArray("data");

                    // Iterate over the JSON array and add nodes to the nodeList
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject nodeObject = data.getJSONObject(i);
                        String id = nodeObject.getString("id");
                        String nodeNumber = nodeObject.getString("node_number");
                        double nodeX = nodeObject.getDouble("node_x");
                        double nodeY = nodeObject.getDouble("node_y");
                        double nodeZ = nodeObject.getDouble("node_z");

                        NodeData nodeData = new NodeData(id, nodeNumber, nodeX, nodeY, nodeZ);
                        nodeList.add(nodeData);
                    }

                    // Notify the adapter of the data change
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch node data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Check if the device has an internet connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
