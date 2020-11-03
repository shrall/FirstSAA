package com.example.firstsaa.ui.main.student.schedule;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firstsaa.R;
import com.example.firstsaa.model.Course;
import com.example.firstsaa.ui.main.student.course.EnrollAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleFragment extends Fragment {

    @BindView(R.id.scheduleDataRV)
    RecyclerView scheduleDataRV;

    ArrayList<Course> listCourse = new ArrayList<>();
    static ArrayList<Course> listCourses = new ArrayList<>();
    ArrayList<String> studentCourseIDs = new ArrayList<>();
    DatabaseReference dbCourses = FirebaseDatabase.getInstance().getReference("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("courses");
    DatabaseReference dbCourse = FirebaseDatabase.getInstance().getReference("course");

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getStudentCourses();
    }

    public void getStudentCourses() {
        dbCourses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentCourseIDs.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String courseID = (String) childSnapshot.child("id").getValue();
                    studentCourseIDs.add(courseID);
                }
                getAllCourse();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllCourse() {
        dbCourse.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCourse.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Course course = childSnapshot.getValue(Course.class);
                    listCourse.add(course);
                }
                setStudentCoursesToRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setStudentCoursesToRV() {
        listCourses.clear();
        for (int i = 0; i < listCourse.size(); i++) {
            for (int j = 0; j < studentCourseIDs.size(); j++) {
                if (studentCourseIDs.get(j).equals(listCourse.get(i).getId())) {
                    listCourses.add(listCourse.get(i));
                }
            }
        }
        scheduleDataRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScheduleAdapter ScheduleAdapter = new ScheduleAdapter(getActivity());
        ScheduleAdapter.setListSchedule(listCourses);
        ScheduleAdapter.notifyDataSetChanged();
        scheduleDataRV.setAdapter(ScheduleAdapter);
    }
}