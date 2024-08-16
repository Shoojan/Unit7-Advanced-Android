package com.example.unit7_sqlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper dbHelper;
    ListView subjectListView;
    List<Subject> subjectList;
    SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        subjectListView = findViewById(R.id.subject_list);

        // Instantiate SQLiteHelper
        dbHelper = new SQLiteHelper(this);

        // Load subjects from the database
        loadSubjects();

        // Handle list item long-click to show context menu
        registerForContextMenu(subjectListView);

        // Handle FAB click to add a new subject
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSubjectDialog();
            }
        });
    }

    // Context menu for update and delete
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Subject selectedSubject = subjectList.get(info.position);

        int itemId = item.getItemId();
        if (itemId == R.id.edit) {
            //Show Edit Dialog
            showEditSubjectDialog(selectedSubject);
            return true;
        } else if (itemId == R.id.delete) {
            //Delete the data from the database
            dbHelper.deleteSubject(selectedSubject.getId());
            // Refresh the list
            loadSubjects();
            return true;
        } else
            return super.onContextItemSelected(item);
    }

    private void loadSubjects() {
        //Fetch the data from the database
        subjectList = dbHelper.getAllSubjects();

        adapter = new SubjectAdapter(this, subjectList);
        subjectListView.setAdapter(adapter);
    }

    private void showAddSubjectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        builder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.name_edit_text);
        Button colorButton = dialogView.findViewById(R.id.color_button);

        final int[] selectedColor = {Color.RED}; // Default color

        // Show Color Picker when button is clicked
        handleColorButton(colorButton, selectedColor);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                int color = selectedColor[0];

                if (!name.isEmpty()) {
                    dbHelper.addSubject(name, color);
                    loadSubjects(); // Refresh the list
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in the name", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }


    private void showEditSubjectDialog(Subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        builder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.name_edit_text);
        Button colorButton = dialogView.findViewById(R.id.color_button);

        nameEditText.setText(subject.getName());
        colorButton.setBackgroundColor(subject.getColor());

        final int[] selectedColor = {subject.getColor()}; // Default color

        // Show Color Picker when button is clicked
        handleColorButton(colorButton, selectedColor);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                if (!name.isEmpty()) {
                    dbHelper.updateSubject(subject.getId(), name, selectedColor[0]);
                    loadSubjects(); // Refresh the list
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in the name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }


    private void handleColorButton(Button colorButton, int[] selectedColor) {
        colorButton.setOnClickListener(v -> {
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, selectedColor[0], new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    // User selected a color
                    selectedColor[0] = color;
                    colorButton.setBackgroundColor(color); // Update button background with chosen color
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                    // Color picker canceled
                }
            });
            colorPicker.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.fetchApi) {
            //Show Edit Dialog
            fetchFromAPIUsingVolley();
            return true;
        } else if (itemId == R.id.fetchDb) {
            loadSubjects();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void fetchFromAPIUsingVolley() {
        //Fetch the data from the database
        subjectList = new ArrayList<>();
        adapter = new SubjectAdapter(this, subjectList);
        subjectListView.setAdapter(adapter);

        String url = "https://jsonplaceholder.typicode.com/users";  // Example API

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String name = jsonObject.getString("name");
                                subjectList.add(new Subject(id, name, Color.GRAY));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Parsing error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error fetching data! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }
}