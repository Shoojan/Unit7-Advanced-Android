package com.example.unit7_sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SubjectAdapter extends BaseAdapter {
    private final Context context;
    private final List<Subject> subjectList;

    public SubjectAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @Override
    public int getCount() {
        return subjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return subjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.subject_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.subject_name);
        View colorView = convertView.findViewById(R.id.color_view);

        Subject subject = subjectList.get(position);
        nameTextView.setText(subject.getName());
        colorView.setBackgroundColor(subject.getColor()); // Set the background color

        return convertView;
    }
}
