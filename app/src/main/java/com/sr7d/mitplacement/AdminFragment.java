package com.sr7d.mitplacement;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment {

    TextInputEditText placementDate, placementName;
    EditText etPlacedNumber;
    Button btnAddPlacement,btnSetPlaced;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    FirebaseDatabase database;
    DatabaseReference myRef;
    long dte;


    public AdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin, container, false);
        initFirebase();

        return v;
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("admin");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        placementDate = view.findViewById(R.id.text_input_placement_date);
        placementName = view.findViewById(R.id.text_input_placement_name);

        etPlacedNumber =  view.findViewById(R.id.edit_text_placed_number);
        btnSetPlaced = view.findViewById(R.id.btn_set_placed);
        btnAddPlacement = view.findViewById(R.id.btn_add_placement);

        initFirebase();
        placementDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONDAY);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                dte = year * 10000 + month * 100 + dayOfMonth;
                String date = month + "/" +dayOfMonth + "/"+year;
                placementDate.setText(date);
            }
        };
        btnAddPlacement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPlacement();
            }
        });
        btnSetPlaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPlaced();
            }
        });

    }

    private void uploadPlaced() {
        int flag = 0;
        if (TextUtils.isEmpty(etPlacedNumber.getText()))
        {
            etPlacedNumber.setError("Enter placed students number");
            etPlacedNumber.requestFocus();
            flag = 1;
        }
        if (flag == 0) {
            myRef.child("/placed").setValue(etPlacedNumber.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    etPlacedNumber.setText("");
                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void uploadPlacement() {
        int flag = 0;
        if (TextUtils.isEmpty(placementDate.getText()))
        {
            placementDate.setError("Choose Date first");
            placementDate.requestFocus();
            flag = 1;
        }
        if (TextUtils.isEmpty(placementName.getText()))
        {
            placementName.setError("Enter Company name");
            placementName.requestFocus();
            flag =1;
        }
        if (flag == 0) {
            PlacementItem pi = new PlacementItem(dte,placementName.getText().toString());
            myRef.child("/placements/").push().setValue(pi).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    placementDate.setText("");
                    placementName.setText("");
                }
            });

        }

    }


//    {
//        "rules": {
//        "students": {
//            "$uid": {
//                ".read": "$uid === auth.uid",
//                        ".write": "$uid === auth.uid"
//            }
//        },
//        "admin":{
//            ".read" : false,
//                    ".write" : false
//        },
//        "userid":{
//            ".read": "auth.uid != null",
//                    ".write": "auth.uid != null"
//        },"placements":{
//            ".read": "auth.uid != null",
//                    ".write": "auth.uid != null"
//        }
//    }
//
//    }

}
