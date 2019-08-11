package com.sr7d.mitplacement;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerViewPlacement;
    List<PlacementItem> placementItems;
    PlacementAdapter placementAdapter;

    public UpcomingFragment() {
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
        View v = inflater.inflate(R.layout.fragment_upcoming, container, false);
        recyclerViewPlacement = v.findViewById(R.id.recycler_view_placement_track);

        initFirebase();
        setupRecycler();
        return v;
    }

    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewPlacement.setLayoutManager(linearLayoutManager);
        placementItems = new ArrayList<>();
        placementAdapter = new PlacementAdapter(getContext(),placementItems);
        recyclerViewPlacement.setAdapter(placementAdapter);

        getData();
    }

    private void getData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                placementItems.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    PlacementItem pi = snapshot.getValue(PlacementItem.class);
                    placementItems.add(pi);
                }
                placementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("admin/placements");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
