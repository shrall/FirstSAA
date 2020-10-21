package com.example.firstsaa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddCourse extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {

    EditText addCourseInput;
    String subject;
    Button addCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        addCourseButton = findViewById(R.id.addCourseButton);
        addCourseInput = findViewById(R.id.addCourseSubject);
        addCourseInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subject = addCourseInput.getEditableText().toString().trim();
                if (!subject.isEmpty()) {
                    addCourseButton.setEnabled(true);
                } else {
                    addCourseButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Spinner spinnerDay = findViewById(R.id.addCourseDays);
        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapterDay);
        spinnerDay.setOnItemSelectedListener(this);

        Spinner spinnerTimeStart = findViewById(R.id.addCourseTimeStart);
        ArrayAdapter<CharSequence> adapterTimeStart = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapterTimeStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeStart.setAdapter(adapterTimeStart);
        spinnerTimeStart.setOnItemSelectedListener(this);

        Spinner spinnerTimeEnd = findViewById(R.id.addCourseTimeEnd);
        ArrayAdapter<CharSequence> adapterTimeEnd = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapterTimeEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeEnd.setAdapter(adapterTimeEnd);
        spinnerTimeEnd.setOnItemSelectedListener(this);

        Spinner spinnerLecturer = findViewById(R.id.addCourseLecturer);
        ArrayAdapter<CharSequence> adapterLecturer = ArrayAdapter.createFromResource(this, R.array.lecturer, android.R.layout.simple_spinner_item);
        adapterLecturer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLecturer.setAdapter(adapterLecturer);
        spinnerLecturer.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
