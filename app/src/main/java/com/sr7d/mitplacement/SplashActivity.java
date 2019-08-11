package com.sr7d.mitplacement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mref;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initFirebase();
    }

    private void initFirebase() {
            firebaseDatabase = FirebaseDatabase.getInstance();

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();

            if (currentUser == null) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            } else {


                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference setupRef = database.getReference("/students/" + currentUser.getUid() + "/isSet");
                setupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String ans = "";
                        try {
                            ans = dataSnapshot.getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (ans.equals("ok")) {
                            mref = firebaseDatabase.getReference("userid");
                            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String adminId = dataSnapshot.getValue(String.class);
                                    String userId = currentUser.getUid();
                                    if (adminId.equals(userId)) {
                                        Toast.makeText(SplashActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                        i.putExtra("flag", "admin");
                                        startActivity(i);
                                    } else {
                                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                        i.putExtra("flag", "user");
                                        startActivity(i);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(SplashActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            startActivity(new Intent(SplashActivity.this, SetupActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

    }
}
