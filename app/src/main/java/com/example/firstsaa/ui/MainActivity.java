package com.example.firstsaa.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.firstsaa.ui.main.course.AddCourse;
import com.example.firstsaa.ui.main.lecturer.AddLecturer;
import com.example.firstsaa.ui.main.student.LoginStudent;
import com.example.firstsaa.R;
import com.example.firstsaa.ui.main.student.StudentRegister;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.starterAddStudentButton)
    Button addStudentBtn;
    @BindView(R.id.starterAddLecturerButton)
    Button addLecturerBtn;
    @BindView(R.id.starterAddCourseButton)
    Button addCourseBtn;
    @BindView(R.id.starterLoginStudentButton)
    Button studentLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StudentRegister.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });
        addLecturerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddLecturer.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCourse.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });
        studentLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginStudent.class);
                startActivity(intent);
            }
        });
    }


    public boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainActivity.this, "Press back again to close the app!", Toast.LENGTH_SHORT).show();
    }
}
