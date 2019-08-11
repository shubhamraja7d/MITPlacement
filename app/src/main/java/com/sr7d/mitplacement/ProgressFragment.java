package com.sr7d.mitplacement;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.util.ArrayList;

import devlight.io.library.ArcProgressStackView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    ColorfulRingProgressView colorfulRingProgressView;

    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        colorfulRingProgressView = view.findViewById(R.id.crpv);
        initFirebase();
        final String[] stringColors = getResources().getStringArray(R.array.devlight);
        final String[] stringBgColors = getResources().getStringArray(R.array.bg);

        final int[] colors = new int[4];
        final int[] bgColors = new int[4];
        for (int i = 0; i < 4; i++) {
            colors[i] = Color.parseColor(stringColors[i]);
            bgColors[i] = Color.parseColor(stringBgColors[i]);
        }

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new ArcProgressStackView.Model("C/C++/Java", 80, bgColors[0], colors[0]));
        models.add(new ArcProgressStackView.Model("NETWORKING", 40, bgColors[1], colors[1]));
        models.add(new ArcProgressStackView.Model("DBMS", 60, bgColors[2], colors[2]));
        models.add(new ArcProgressStackView.Model("AI/DATA SCIENCE", 50, bgColors[3], colors[3]));

        final ArcProgressStackView arcProgressStackView = (ArcProgressStackView) view.findViewById(R.id.apsv);
        arcProgressStackView.setModels(models);


        return view;
    }
    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("admin");
        myRef.child("/placed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pr = dataSnapshot.getValue(String.class);
                colorfulRingProgressView.setPercent(Float.parseFloat(pr));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
