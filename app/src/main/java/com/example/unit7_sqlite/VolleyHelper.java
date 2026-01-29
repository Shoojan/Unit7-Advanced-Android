package com.example.unit7_sqlite;

import android.content.Context;
import android.graphics.Color;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolleyHelper {
    private final Context context;
    private final String URL = "https://jsonplaceholder.typicode.com/users";  // Example API

    public VolleyHelper(Context context) {
        this.context = context;
    }

    public void fetchFromAPIUsingVolley(ListView listView) {
        //Fetch the data from the database
        ArrayList<Subject> list = new ArrayList<>();
        SubjectAdapter  adapter = new SubjectAdapter(context, list);
        listView.setAdapter(adapter);

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                this.URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                list.add(new Subject(id, name, Color.GRAY));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Parsing error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error fetching data! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

}
