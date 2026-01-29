package com.example.unit7_sqlite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SubjectHandler {
    private Context context;
    private ListView subjectListView;

    SQLiteHelper dbHelper;
    private List<Subject> subjectList;
    SubjectAdapter adapter;

    public SubjectHandler(Context context, ListView subjectListView) {
        this.context = context;
        this.subjectListView = subjectListView;

        // Instantiate SQLiteHelper
        dbHelper = new SQLiteHelper(context);

        // Load subjects from the database
        loadSubjects();

    }

    public Subject getSubject(int position) {
        return subjectList.get(position);
    }

    public void loadSubjects() {
        //Fetch the data from the database
        subjectList = dbHelper.getAllSubjects();

        adapter = new SubjectAdapter(this.context, subjectList);
        subjectListView.setAdapter(adapter);
    }

    public void showAddSubjectDialog(LayoutInflater layoutInflater) {
        this.showSubjectDialog(layoutInflater, null);
    }

    public void showEditSubjectDialog(LayoutInflater layoutInflater, Subject subject) {
        this.showSubjectDialog(layoutInflater, subject);
    }

    private void showSubjectDialog(LayoutInflater layoutInflater, @Nullable Subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = layoutInflater.inflate(R.layout.dialog_add_subject, null);
        builder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.name_edit_text);
        Button colorButton = dialogView.findViewById(R.id.color_button);

        if (subject == null) {
            subject = new Subject(Color.RED); // Default color
        } else {
            nameEditText.setText(subject.getName());
            colorButton.setBackgroundColor(subject.getColor());
        }

        // Show Color Picker when button is clicked
        handleColorButton(colorButton, subject);

        Subject finalSubject = subject;

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                int color = finalSubject.getColor();

                if (!name.isEmpty()) {
                    if(finalSubject.getId() == 0){
                        dbHelper.addSubject(name, color);
                    }else{
                        dbHelper.updateSubject(finalSubject.getId(), name, color);
                    }

                    loadSubjects(); // Refresh the list
                } else {
                    Toast.makeText(context, "Please fill in the name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void handleColorButton(Button colorButton, Subject selectedSubject) {
        colorButton.setOnClickListener(v -> {
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(context, selectedSubject.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    // User selected a color
                    selectedSubject.setColor(color);
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


    public void deleteSubject(int subjectId) {
        //Delete the data from the database
        dbHelper.deleteSubject(subjectId);

        // Refresh the list
        this.loadSubjects();
    }
}
