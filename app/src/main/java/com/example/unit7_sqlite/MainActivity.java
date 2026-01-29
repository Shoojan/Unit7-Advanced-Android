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

    ListView listView;
    SubjectHandler subjectHandler;

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

        listView = findViewById(R.id.subject_list);

        // Handle list item long-click to show context menu
        registerForContextMenu(listView);

        subjectHandler = new SubjectHandler(this, listView);

        // Handle FAB click to add a new subject
        findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectHandler.showAddSubjectDialog(getLayoutInflater());
            }
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
            VolleyHelper volleyHelper = new VolleyHelper(this);
            volleyHelper.fetchFromAPIUsingVolley(listView);
            return true;
        } else if (itemId == R.id.fetchDb) {
            subjectHandler.loadSubjects();
            return true;
        } else
            return super.onOptionsItemSelected(item);
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
        if (info == null) {
            Toast.makeText(this, "Context not found!", Toast.LENGTH_SHORT).show();
            return super.onContextItemSelected(item);
        }

        Subject selectedSubject = subjectHandler.getSubject(info.position);

        int itemId = item.getItemId();
        if (itemId == R.id.edit) {
            //Show Edit Dialog
            subjectHandler.showEditSubjectDialog(getLayoutInflater(), selectedSubject);
            return true;
        } else if (itemId == R.id.delete) {
            //Delete the data from the database
            subjectHandler.deleteSubject(selectedSubject.getId());
            return true;
        } else
            return super.onContextItemSelected(item);
    }

}