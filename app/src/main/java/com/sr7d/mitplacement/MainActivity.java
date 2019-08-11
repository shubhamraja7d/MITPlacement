package com.sr7d.mitplacement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mref;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handleIntent();
        initFirebase();

    }

    private void handleIntent() {
        try {
            String answer = getIntent().getStringExtra("flag");
            if (answer.equals("admin")) {
                setViewPager(1);
            } else {
                setViewPager(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference("userid");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String adminId = dataSnapshot.getValue(String.class);
                    String userId = currentUser.getUid();
                    if (adminId.equals(userId)) {
                        Toast.makeText(MainActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                        int flag = 1;
                        setViewPager(flag);
                    } else {
                        int flag =0;
                        setViewPager(flag);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setViewPager(int flag)
    {
        viewPager = findViewById(R.id.view_pager_frag);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),flag);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tab_frag);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                FirebaseUser user = auth.getCurrentUser();
//                DatabaseReference setupRef = database.getReference("/students/"+user.getUid()+"/isSet");
//                setupRef.setValue("done");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
