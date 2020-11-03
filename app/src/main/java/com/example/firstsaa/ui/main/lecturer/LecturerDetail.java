package com.example.firstsaa.ui.main.lecturer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstsaa.R;
import com.example.firstsaa.model.Course;
import com.example.firstsaa.model.Lecturer;
import com.example.firstsaa.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LecturerDetail extends AppCompatActivity {


    @BindView(R.id.lecturerDetailToolbar)
    Toolbar lecturerDetailToolbar;
    @BindView(R.id.lecturerDetailName)
    TextView lecturerDetailName;
    @BindView(R.id.lecturerDetailGender)
    TextView lecturerDetailGender;
    @BindView(R.id.lecturerDetailExpertise)
    TextView lecturerDetailExpertise;
    @BindView(R.id.lecturerDetailEditButton)
    ImageView lecturerDetailEdit;
    @BindView(R.id.lecturerDetailDeleteButton)
    ImageView lecturerDetailDelete;
    DatabaseReference dbLecturer, dbStudent, dbCourse;
    int pos = 0;
    Lecturer lecturer;
    Course course;
    Student student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_detail);
        ButterKnife.bind(this);
        setSupportActionBar(lecturerDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbLecturer = FirebaseDatabase.getInstance().getReference("lecturer");
        dbStudent = FirebaseDatabase.getInstance().getReference("student");
        dbCourse = FirebaseDatabase.getInstance().getReference("course");


        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);
        lecturer = intent.getParcelableExtra("data_lecturer");
        lecturerDetailName.setText(lecturer.getName());
        lecturerDetailGender.setText(lecturer.getGender());
        lecturerDetailExpertise.setText(lecturer.getExpertise());


        lecturerDetailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LecturerDetail.this)
                        .setTitle(R.string.konfirmasi)
                        .setIcon(R.drawable.ic_logo_logomark)
                        .setMessage("Are you sure to remove " + lecturer.getName() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                Log.d("cekmasukataugakyes", "yesisyes");
                                dbCourse.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            course = childSnapshot.getValue(Course.class);
                                            Log.d("cekmasukataugakyes2", "yesisyesaewfdawef");
                                            if (course.getLecturer().equals(lecturer.getId())) {
                                                deleteStudentCourse(course, dbStudent);
                                                Log.d("cekmasukataugak", "masok bos" + course.getLecturer() + lecturer.getId());
                                                dbCourse.child(course.getId()).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                dbLecturer.child(lecturer.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Intent in = new Intent(LecturerDetail.this, LecturerData.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        Toast.makeText(LecturerDetail.this, "Delete success!", Toast.LENGTH_SHORT).show();
                                        startActivity(in);
                                        finish();
                                        dialogInterface.cancel();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
        });

        lecturerDetailEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LecturerDetail.this, AddLecturer.class);
                in.putExtra("action", "edit");
                in.putExtra("edit_data_lect", lecturer);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }
        });
    }

    private void deleteStudentCourse(final Course selectedCourse, final DatabaseReference dbStudent) {
        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    student = childSnapshot.getValue(Student.class);
                    if (!student.getUid().equals("")) {
                        dbStudent.child(student.getUid()).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    String courseID = (String) childSnapshot.child("id").getValue();
                                    if (courseID.equals(selectedCourse.getId())) {
                                        dbStudent.child(student.getUid()).child("courses").child(courseID).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(LecturerDetail.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LecturerDetail.this, LecturerData.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
