package com.example.firstsaa.ui.main.lecturer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.firstsaa.R;
import com.example.firstsaa.model.Lecturer;
import com.example.firstsaa.utils.ItemClickSupport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LecturerData extends AppCompatActivity {


    @BindView(R.id.lecturerDataToolbar)
    Toolbar tb;
    @BindView(R.id.lecturerDataRV)
    RecyclerView lecturerDataRV;

    DatabaseReference dbLecturer;
    ArrayList<Lecturer> listLecturer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_data);
        ButterKnife.bind(this);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dbLecturer = FirebaseDatabase.getInstance().getReference("lecturer");
        lecturerDataRV = findViewById(R.id.lecturerDataRV);

        showLecturerData();
    }

    public void showLecturerData() {
        dbLecturer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLecturer.clear();
                lecturerDataRV.setAdapter(null);
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Lecturer lecturer = childSnapshot.getValue(Lecturer.class);
                    listLecturer.add(lecturer);
                }
                showData(listLecturer);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showData(final ArrayList<Lecturer> list) {
        lecturerDataRV.setLayoutManager(new LinearLayoutManager(LecturerData.this));
        LecturerAdapter lecturerAdapter = new LecturerAdapter(LecturerData.this);
        lecturerAdapter.setListLecturer(list);
        lecturerDataRV.setAdapter(lecturerAdapter);

        ItemClickSupport.addTo(lecturerDataRV).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(LecturerData.this, LecturerDetail.class);
                Lecturer lecturer = new Lecturer(list.get(position).getId(),list.get(position).getName(), list.get(position).getGender(), list.get(position).getExpertise());
                intent.putExtra("data_lecturer", lecturer);
                intent.putExtra("position", position);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(LecturerData.this, AddLecturer.class);
            intent.putExtra("action", "add");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LecturerData.this, AddLecturer.class);
        intent.putExtra("action", "add");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
