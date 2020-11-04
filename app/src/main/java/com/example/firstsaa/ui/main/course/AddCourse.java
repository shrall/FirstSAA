package com.example.firstsaa.ui.main.course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.firstsaa.R;
import com.example.firstsaa.model.Course;
import com.example.firstsaa.ui.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCourse extends AppCompatActivity {

    @BindView(R.id.addCourseSubject)
    EditText addCourseInput;
    @BindView(R.id.addCourseButton)
    Button addCourseButton;
    @BindView(R.id.addCourseDays)
    Spinner spinnerDay;
    @BindView(R.id.addCourseTimeStart)
    Spinner spinnerTimeStart;
    @BindView(R.id.addCourseTimeEnd)
    Spinner spinnerTimeEnd;
    @BindView(R.id.addCourseLecturer)
    Spinner spinnerLecturer;
    @BindView(R.id.addCourseToolbar)
    Toolbar addCourseToolbar;

    String subject = "", day = "", start = "", end = "", lecturer = "", action = "";
    Course course;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbCourse = FirebaseDatabase.getInstance().getReference("course");
    ArrayAdapter<CharSequence> adapterTimeEnd;
    List<String> lecturerNames, lecturerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        ButterKnife.bind(this);

        setSupportActionBar(addCourseToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showSpinnerLecturer();

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

        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adapterDay);

        ArrayAdapter<CharSequence> adapterTimeStart = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapterTimeStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeStart.setAdapter(adapterTimeStart);

        spinnerTimeStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapterTimeEnd = null;
                setSpinner_end(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("add")) {
            getSupportActionBar().setTitle(R.string.addcourse);
            addCourseButton.setText(R.string.addcourse);
            addCourseButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getInputValue();
                    try {
                        checkCourseTime("");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (action.equalsIgnoreCase("edit")) {
            getSupportActionBar().setTitle(R.string.edit_course);
            course = intent.getParcelableExtra("editCourse");
            addCourseInput.setText(course.getSubject());
            int dayIndex = adapterDay.getPosition(course.getDay());
            spinnerDay.setSelection(dayIndex);
            int startIndex = adapterTimeStart.getPosition(course.getStart());
            spinnerTimeStart.setSelection(startIndex);
            setSpinner_end(startIndex);
            final int endIndex = adapterTimeEnd.getPosition(course.getEnd());
            spinnerTimeEnd.setSelection(endIndex);
            addCourseButton.setText(R.string.edit_course);

            addCourseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        checkCourseTime(course.getId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        lecturerNames = new ArrayList<>();
        lecturerID = new ArrayList<>();
    }

    public void getInputValue() {
        subject = addCourseInput.getEditableText().toString().trim();
        day = spinnerDay.getSelectedItem().toString();
        start = spinnerTimeStart.getSelectedItem().toString();
        end = spinnerTimeEnd.getSelectedItem().toString();
        lecturer = lecturerID.get(spinnerLecturer.getSelectedItemPosition());
    }

    public void addCourse(String msubject, String mday, String mstart, String mend, String mlecture) {
        String mid = mDatabase.child("course").push().getKey();
        Course course = new Course(mid, msubject, mday, mstart, mend, mlecture);
        mDatabase.child("course").child(mid).setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddCourse.this, "Add Course Successfully", Toast.LENGTH_SHORT).show();
                addCourseInput.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddCourse.this, "Add Course Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkCourseTime(final String courseID) throws ParseException {
        getInputValue();
        final MutableLiveData<Boolean> checkOverlap = new MutableLiveData<>();
        final int courseStart = Integer.parseInt(start.replace(":", ""));
        final int courseEnd = Integer.parseInt(end.replace(":", ""));
        dbCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    checkOverlap.setValue(false);
                } else {
                    int i = 0;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        i++;
                        Course course = childSnapshot.getValue(Course.class);
                        if (!course.getId().equals(courseID)) {
                            if (day.equals(course.getDay()) && lecturer.equals(course.getLecturer())) {
                                int checkCoursesDataStart = Integer.parseInt(course.getStart().replace(":", ""));
                                int checkCoursesDataEnd = Integer.parseInt(course.getEnd().replace(":", ""));

                                if (courseStart >= checkCoursesDataStart && courseStart < checkCoursesDataEnd) {
                                    checkOverlap.setValue(true);
                                    break;
                                }
                                if (courseEnd > checkCoursesDataStart && courseEnd <= checkCoursesDataEnd) {
                                    checkOverlap.setValue(true);
                                    break;
                                }
                            }
                        }
                        if (snapshot.getChildrenCount() == i && checkOverlap.getValue() == null) {
                            checkOverlap.setValue(false);
                        }
                    }
                }
                if (checkOverlap.getValue()) {
                    Toast.makeText(AddCourse.this, "That lecturer is already teaching another class at that time!", Toast.LENGTH_SHORT).show();
                } else {
                    if (action.equals("add")) {
                        addCourse(subject, day, start, end, lecturer);
                        Intent intent = new Intent(AddCourse.this, CourseData.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Map<String, Object> params = new HashMap<>();
                        params.put("subject", subject);
                        params.put("day", day);
                        params.put("start", start);
                        params.put("end", end);
                        params.put("lecturer", lecturer);
                        mDatabase.child("course").child(course.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(AddCourse.this, CourseData.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void setSpinner_end(int position) {
        if (position == 0) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0730, android.R.layout.simple_spinner_item);
        } else if (position == 1) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0800, android.R.layout.simple_spinner_item);
        } else if (position == 2) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0830, android.R.layout.simple_spinner_item);
        } else if (position == 3) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0900, android.R.layout.simple_spinner_item);
        } else if (position == 4) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end0930, android.R.layout.simple_spinner_item);
        } else if (position == 5) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1000, android.R.layout.simple_spinner_item);
        } else if (position == 6) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1030, android.R.layout.simple_spinner_item);
        } else if (position == 7) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1100, android.R.layout.simple_spinner_item);
        } else if (position == 8) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1130, android.R.layout.simple_spinner_item);
        } else if (position == 9) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1200, android.R.layout.simple_spinner_item);
        } else if (position == 10) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1230, android.R.layout.simple_spinner_item);
        } else if (position == 11) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1300, android.R.layout.simple_spinner_item);
        } else if (position == 12) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1330, android.R.layout.simple_spinner_item);
        } else if (position == 13) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1400, android.R.layout.simple_spinner_item);
        } else if (position == 14) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1430, android.R.layout.simple_spinner_item);
        } else if (position == 15) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1500, android.R.layout.simple_spinner_item);
        } else if (position == 16) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1530, android.R.layout.simple_spinner_item);
        } else if (position == 17) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1600, android.R.layout.simple_spinner_item);
        } else if (position == 18) {
            adapterTimeEnd = ArrayAdapter.createFromResource(AddCourse.this, R.array.jam_end1630, android.R.layout.simple_spinner_item);
        }
        adapterTimeEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeEnd.setAdapter(adapterTimeEnd);
    }

    public void showSpinnerLecturer() {
        mDatabase.child("lecturer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String spinnerID = childSnapshot.child("id").getValue(String.class);
                    String spinnerName = childSnapshot.child("name").getValue(String.class);
                    lecturerNames.add(spinnerName);
                    lecturerID.add(spinnerID);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddCourse.this, android.R.layout.simple_spinner_dropdown_item, lecturerNames);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerLecturer.setAdapter(arrayAdapter);
                if (action.equalsIgnoreCase("edit")) {
                    int lecturerIndex = arrayAdapter.getPosition(course.getLecturer());
                    spinnerLecturer.setSelection(lecturerIndex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(AddCourse.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.course_list) {
            Intent intent = new Intent(AddCourse.this, CourseData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddCourse.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
