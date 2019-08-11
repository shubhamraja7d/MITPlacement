package com.sr7d.mitplacement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    CardView crdEducation, crdSkills;
    ConstraintLayout layoutAddEducation;
    Button btnAddEducation;
    FloatingActionButton fabDone;
    ImageButton btnUploadImage;
    private static final int PICK_IMAGE_REQUEST = 234;
    Uri filePath;

    AutoCompleteTextView tvAddSkills;
    ArrayAdapter<String> skillPopupAdapter;
    String skills[] = {"C programming", "C++ Programming", "Java", "Advance Java", "Operating System", "DBMS", "SQL", "NoSQL"};

    RecyclerView recyclerViewEducation, recyclerViewSkill;
    EducationAdapter educationAdapter;
    SkillAdapter skillAdapter;
    List<EducationItem> educationItems;
    List<String> skillItems;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    StorageReference mStorageRef;

    int picFlag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        bindViews();
        initFirebase();
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("students");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void bindViews() {
        crdEducation = findViewById(R.id.card_education);
        crdEducation.setOnClickListener(this);

        crdSkills = findViewById(R.id.card_skills);
        crdSkills.setOnClickListener(this);
        layoutAddEducation = findViewById(R.id.constraints_layout_add_education);
        btnAddEducation = findViewById(R.id.btn_add_education);
        btnAddEducation.setOnClickListener(this);

        btnUploadImage = findViewById(R.id.img_btn_setup_profile);
        btnUploadImage.setOnClickListener(this);
        fabDone = findViewById(R.id.fab_done);
        fabDone.setOnClickListener(this);
        tvAddSkills = findViewById(R.id.auto_text_view_add_skill);
        skillPopupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, skills);
        tvAddSkills.setThreshold(1);
        tvAddSkills.setAdapter(skillPopupAdapter);
        tvAddSkills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                skillItems.add(parent.getItemAtPosition(position).toString());
                skillAdapter.notifyDataSetChanged();
                tvAddSkills.setText("");
            }
        });
        setupRecyclerEducation();
        setupRecyclerSkill();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(SetupActivity.this, SplashActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_education:
                if (layoutAddEducation.getVisibility() == View.VISIBLE) {
                    layoutAddEducation.setVisibility(View.GONE);
                } else {
                    layoutAddEducation.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_add_education:

                getAddEducationData();
                break;
            case R.id.card_skills:
                if (tvAddSkills.getVisibility() == View.VISIBLE) {
                    tvAddSkills.setVisibility(View.GONE);
                } else {
                    tvAddSkills.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.fab_done:
                uploadProfileImage();
                break;
            case R.id.img_btn_setup_profile:
                showFileChooser();
                break;
        }
    }

    private void showFileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i.createChooser(i, "Select image to upload"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            picFlag =1;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                btnUploadImage.setImageBitmap(bitmap);
                btnUploadImage.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadStudentDetails() {
        EditText etName, etRoll, etPhone, etEmail, etAddress;
        etName = findViewById(R.id.et_setup_name);
        etRoll = findViewById(R.id.et_setup_roll);
        etPhone = findViewById(R.id.et_setup_phone);
        etEmail = findViewById(R.id.et_setup_email);
        etAddress = findViewById(R.id.et_setup_address);
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("Field must not be empty");
            etName.requestFocus();
        } else if (TextUtils.isEmpty(etRoll.getText())) {
            etRoll.setError("Field must not be empty");
            etRoll.requestFocus();
        } else if (TextUtils.isEmpty(etPhone.getText())) {
            etPhone.setError("Phone must not be empty");
            etPhone.requestFocus();
        } else if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("email must not be empty");
            etEmail.requestFocus();
        } else if (TextUtils.isEmpty(etAddress.getText())) {
            etAddress.setError("Address must not be empty");
            etAddress.requestFocus();
        } else if (etPhone.getText().toString().length() != 10) {
            etPhone.setError("Phone number is not valid");
            etPhone.requestFocus();
        } else if (picFlag == 1) {
            Student s = new Student(etName.getText().toString(), etRoll.getText().toString(), etPhone.getText().toString(),
                    etEmail.getText().toString(), etAddress.getText().toString(), educationItems, skillItems, "ok");
            Toast.makeText(this, "uploading details...", Toast.LENGTH_SHORT).show();
            myRef.child(currentUser.getUid()).setValue(s);
            Intent i = new Intent(SetupActivity.this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, " Profile image not selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadProfileImage() {
        final ProgressBar uploadprogress;
        uploadprogress = findViewById(R.id.progress_bar_upload);
        uploadprogress.setVisibility(View.VISIBLE);

        StorageReference riversRef = mStorageRef.child("images/" + currentUser.getUid() + "/profile.jpg");
        riversRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        uploadprogress.setVisibility(View.GONE);
                        uploadStudentDetails();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        long pr = taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                        uploadprogress.setProgress((int) pr);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        uploadprogress.setVisibility(View.GONE);
                        Toast.makeText(SetupActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRecyclerEducation() {
        recyclerViewEducation = findViewById(R.id.recycler_view_education);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewEducation.setLayoutManager(linearLayoutManager);

        educationItems = new ArrayList<>();
        educationAdapter = new EducationAdapter(this, educationItems, new EducationAdapter.EducationClickListener() {
            @Override
            public void OnItemClick(final int position) {
                View dialogView = getLayoutInflater().inflate(R.layout.layout_education_edit, null);
                final EditText etDegree, etCompletionyear, etPercentage, etCollege, etUniv;
                etDegree = dialogView.findViewById(R.id.et_degree);
                etCompletionyear = dialogView.findViewById(R.id.et_complete_year);
                etPercentage = dialogView.findViewById(R.id.et_percentage);
                etCollege = dialogView.findViewById(R.id.et_college);
                etUniv = dialogView.findViewById(R.id.et_univ);

                etDegree.setText(educationItems.get(position).getDegree());
                etCompletionyear.setText(educationItems.get(position).getCompletionYear());
                etPercentage.setText(educationItems.get(position).getPercentage());
                etCollege.setText(educationItems.get(position).getCollege());
                etUniv.setText(educationItems.get(position).getUniversity());

                final AlertDialog alert = new AlertDialog.Builder(SetupActivity.this)
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                educationItems.get(position).setDegree(etDegree.getText().toString());
                                educationItems.get(position).setCompletionYear(etCompletionyear.getText().toString());
                                educationItems.get(position).setPercentage(etPercentage.getText().toString() + "%");
                                educationItems.get(position).setCollege(etCollege.getText().toString());
                                educationItems.get(position).setUniversity(etUniv.getText().toString());
                                educationAdapter.notifyDataSetChanged();
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .create();
                alert.show();

            }
        });
        recyclerViewEducation.setAdapter(educationAdapter);
    }

    private void setupRecyclerSkill() {
        recyclerViewSkill = findViewById(R.id.recycler_view_skills);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerViewSkill.setLayoutManager(staggeredGridLayoutManager);

        skillItems = new ArrayList<>();

        skillAdapter = new SkillAdapter(this, skillItems, new SkillAdapter.SkillClickListener() {
            @Override
            public void OnItemClick(int position) {
                skillItems.remove(position);
                skillAdapter.notifyDataSetChanged();
            }
        });
        recyclerViewSkill.setAdapter(skillAdapter);

    }

    private void getAddEducationData() {
        TextInputEditText etDegreeMain, etCompletionYearMain, etCollegeMain, etUniversityMain, etPercentageMain;
        etDegreeMain = findViewById(R.id.et_degree_main);
        etCompletionYearMain = findViewById(R.id.et_complete_year_main);
        etCollegeMain = findViewById(R.id.et_college_main);
        etUniversityMain = findViewById(R.id.et_univ_main);
        etPercentageMain = findViewById(R.id.et_percentage_main);
        if (TextUtils.isEmpty(etDegreeMain.getText())) {
            etDegreeMain.setError("Field must not be empty");
            etDegreeMain.requestFocus();
        } else if (TextUtils.isEmpty(etCompletionYearMain.getText())) {
            etCompletionYearMain.setError("Field must not be empty");
            etCompletionYearMain.requestFocus();
        } else if (TextUtils.isEmpty(etCollegeMain.getText())) {
            etCollegeMain.setError("Phone must not be empty");
            etCollegeMain.requestFocus();
        } else if (TextUtils.isEmpty(etUniversityMain.getText())) {
            etUniversityMain.setError("email must not be empty");
            etUniversityMain.requestFocus();
        } else if (TextUtils.isEmpty(etPercentageMain.getText())) {
            etPercentageMain.setError("Address must not be empty");
            etPercentageMain.requestFocus();
        } else {
            educationItems.add(new EducationItem(etDegreeMain.getText().toString(), etCompletionYearMain.getText().toString(),
                    etCollegeMain.getText().toString(), etUniversityMain.getText().toString(), etPercentageMain.getText().toString() + "%"));
            educationAdapter.notifyDataSetChanged();
            etDegreeMain.setText("");
            etCompletionYearMain.setText("");
            etCollegeMain.setText("");
            etUniversityMain.setText("");
            etPercentageMain.setText("");
            layoutAddEducation.setVisibility(View.GONE);
        }

    }
}
