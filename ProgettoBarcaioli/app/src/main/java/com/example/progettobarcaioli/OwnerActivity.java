package com.example.progettobarcaioli;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

public class OwnerActivity extends AppCompatActivity {

    LinearLayout boatLayout;
    LinearLayout dateLayout;
    LinearLayout equipmentLayout;

    Button boatsManagemnt;
    Button dateManagemnt;
    Button equipmentManagemnt;

    Button displayBoat;

    int lastExpandedGroupPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_owner);

        boatLayout = findViewById(R.id.boatsLayout);
        dateLayout = findViewById(R.id.timeAndDateLayout);
        equipmentLayout = findViewById(R.id.equipmentLayout);
        boatLayout.setVisibility(View.GONE);
        dateLayout.setVisibility(View.GONE);
        equipmentLayout.setVisibility(View.GONE);

        boatsManagemnt = findViewById(R.id.boatsButton);
        dateManagemnt = findViewById(R.id.dateAndTimeButton);
        equipmentManagemnt = findViewById(R.id.equipmentButton);

        boatsManagemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boatLayout.getVisibility() == View.GONE){
                    boatLayout.setVisibility(View.VISIBLE);
                }else{
                    boatLayout.setVisibility(View.GONE);
                }
            }
        });

        dateManagemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateLayout.getVisibility() == View.GONE){
                    dateLayout.setVisibility(View.VISIBLE);
                }else{
                    dateLayout.setVisibility(View.GONE);
                }
            }
        });

        equipmentManagemnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(equipmentLayout.getVisibility() == View.GONE){
                    equipmentLayout.setVisibility(View.VISIBLE);
                }else{
                    equipmentLayout.setVisibility(View.GONE);
                }
            }
        });

        DisplayBoatsFragment displayBoatsFragment = new DisplayBoatsFragment();

        displayBoat = findViewById(R.id.displayBoat);
        displayBoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             ///   getSupportFragmentManager().beginTransaction().add(R.id.frame, displayBoatsFragment, DisplayBoatsFragment.class.getSimpleName()).commit();
            }
        });



    }
}