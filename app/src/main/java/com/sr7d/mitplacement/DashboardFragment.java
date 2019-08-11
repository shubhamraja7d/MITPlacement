package com.sr7d.mitplacement;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {


    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    StorageReference mStorageRef;

    TextView tvName, tvRoll, tvEmail, tvPhone, tvAddress;
    ImageView imgIcon;
    EducationAdapterShow educationAdapter;
    SkillAdapterShow skillAdapter;
    RecyclerView educationRecycler, skillRecycler;
    List<EducationItem> educationItems;
    List<String> skills;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        bindViews(view);

        initFirebase();
        return view;
    }

    private void bindViews(View view) {
        tvName = view.findViewById(R.id.tv_name);
        tvRoll = view.findViewById(R.id.tv_roll);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvAddress = view.findViewById(R.id.tv_address);
        imgIcon = view.findViewById(R.id.img_icon);

        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view) {
        educationRecycler = view.findViewById(R.id.recycler_view_education);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        educationRecycler.setLayoutManager(linearLayoutManager);
        educationItems = new ArrayList<>();
        educationAdapter = new EducationAdapterShow(getContext(),educationItems);
        educationRecycler.setAdapter(educationAdapter);

        skillRecycler = view.findViewById(R.id.recycler_view_skills);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL);
        skillRecycler.setLayoutManager(staggeredGridLayoutManager);
        skills = new ArrayList<>();
        skillAdapter = new SkillAdapterShow(getContext(),skills);
        skillRecycler.setAdapter(skillAdapter);
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("students");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        if (currentUser != null) {
            getDetails();
        }
    }

    private void getDetails() {
        myRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student s = dataSnapshot.getValue(Student.class);
                tvName.setText(s.getName());
                tvRoll.setText(s.getRoll());
                tvPhone.setText(s.getPhone());
                tvEmail.setText(s.getEmail());
                tvAddress.setText(s.getAddress());
                skills.addAll(s.getSkills());
                educationItems.addAll(s.getEducations());
                skillAdapter.notifyDataSetChanged();
                educationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mStorageRef.child("/images/"+currentUser.getUid()+"/profile.jpg").getFile(localFile)
//                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        // Successfully downloaded data to local file
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle failed download
//                // ...
//            }
//        });
        mStorageRef.child("/images/"+currentUser.getUid()+"/profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(getContext()).load(uri.toString()).into(imgIcon);
                    imgIcon.setScaleType(ImageView.ScaleType.FIT_XY);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }

}
