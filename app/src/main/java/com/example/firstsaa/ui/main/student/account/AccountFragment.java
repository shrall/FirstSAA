package com.example.firstsaa.ui.main.student.account;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.firstsaa.R;
import com.example.firstsaa.model.Student;
import com.example.firstsaa.ui.MainActivity;
import com.example.firstsaa.ui.main.student.StudentData;
import com.example.firstsaa.ui.main.student.StudentRegister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends Fragment {

    @BindView(R.id.studentFragmentEditButton)
    Button btnEdit;
    @BindView(R.id.studentFragmentLogoutButton)
    Button btnLogout;
    @BindView(R.id.studentFragmentName)
    TextView text_fname;
    @BindView(R.id.studentFragmentEmail)
    TextView text_email;
    @BindView(R.id.studentFragmentNIM)
    TextView text_nim;
    @BindView(R.id.studentFragmentGender)
    TextView text_gender;
    @BindView(R.id.studentFragmentAge)
    TextView text_age;
    @BindView(R.id.studentFragmentAddress)
    TextView text_address;
    Student student;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference dbStudent = FirebaseDatabase.getInstance().getReference("student").child(firebaseAuth.getCurrentUser().getUid());
    ;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    student = snapshot.getValue(Student.class);
                    setData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setData() {
        text_fname.setText(student.getName());
        text_email.setText(student.getEmail());
        text_nim.setText(student.getNim());
        text_gender.setText(student.getGender());
        text_age.setText(student.getAge());
        text_address.setText(student.getAddress());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudentRegister.class);
                intent.putExtra("data_student", student);
                intent.putExtra("action", "edit");
                intent.putExtra("source", "userAccount");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_logo_logomark)
                        .setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getActivity().finish();
                                startActivity(intent);
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

}