package com.sr7d.mitplacement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    Button btnLogin, btnRegister;
    EditText etEmail, etPassword;
    String email, password;
    ProgressBar progressBar;
    int result = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setFirebaseUser();
        bindViews();
    }

    private void bindViews() {
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_pwd);

        progressBar = findViewById(R.id.progress_bar);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void setFirebaseUser() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            updateUI(user);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:
                result = checkValidation();
                if (result == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                            etEmail.setError("invalid Email");
                                            etEmail.requestFocus();
                                        } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                            etPassword.setError("wrong Password");
                                            etPassword.requestFocus();
                                        } catch (Exception e) {
                                            Toast.makeText(LoginActivity.this, "Something went wrong...Try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }

                break;
            case R.id.btn_register:
                result = checkValidation();
                if (result == 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                            etPassword.setError("Weak Password...Try something strong");
                                            etPassword.requestFocus();
                                        } catch (FirebaseAuthUserCollisionException existEmail) {
                                            etEmail.setError("Email already in registered");
                                            etEmail.requestFocus();
                                        } catch (Exception e) {
                                            Toast.makeText(LoginActivity.this, "Something went wrong. Try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }

                break;
        }
    }

    private int checkValidation() {
        int ans = 0;
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Email must not be empty");
            etEmail.requestFocus();
            ans = 1;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Password must not be empty");
            etPassword.requestFocus();
            ans = 1;
        }
        return ans;
    }

    private void updateUI(FirebaseUser user) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference setupRef = database.getReference("/students/" + user.getUid() + "/isSet");
        setupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ans = "";
                try {
                    ans = dataSnapshot.getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.GONE);
                if (ans.equals("ok")) {
                    final AlertDialog alert = new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Logged in user")
                            .setMessage("Your account is setup")
                            .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.signOut();
                                }
                            })
                            .setNegativeButton("continue", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            })
                            .setCancelable(false)
                            .create();
                    alert.show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Details not uploaded")
                            .setMessage("Continue to upload data")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(LoginActivity.this, SetupActivity.class));
                                }
                            })
                            .setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.signOut();
                                }
                            })
                            .setCancelable(false)
                            .create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
