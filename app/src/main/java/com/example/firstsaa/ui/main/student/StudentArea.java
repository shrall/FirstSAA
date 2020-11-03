package com.example.firstsaa.ui.main.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.firstsaa.ui.main.student.account.AccountFragment;
import com.example.firstsaa.ui.main.student.course.CourseFragment;
import com.example.firstsaa.R;
import com.example.firstsaa.ui.main.student.schedule.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentArea extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bnv;

    @BindView(R.id.StudentAreaToolbar)
    Toolbar mbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_area);
        ButterKnife.bind(this);
        setSupportActionBar(mbar);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_schedule:
                        fragment = new ScheduleFragment();
                        mbar.setTitle("My Schedule");
                        loadFragment(fragment);
                        return true;

                    case R.id.nav_courses:
                        fragment = new CourseFragment();
                        mbar.setTitle("My Courses");
                        loadFragment(fragment);
                        return true;

                    case R.id.nav_account:
                        fragment = new AccountFragment();
                        mbar.setTitle("My Account");
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }



    @Override
    protected void onStart() {
        super.onStart();
            bnv.setSelectedItemId(R.id.nav_account);
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
        Toast.makeText(StudentArea.this, "Press back again to close the app!", Toast.LENGTH_SHORT).show();
    }
}