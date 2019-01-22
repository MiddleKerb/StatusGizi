package com.fbasegizi.statusgizi.count;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.BeratBadanTinggiBadan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BBTBCount extends BaseActivity implements View.OnClickListener {

    private TextInputLayout fieldWeight;
    private TextInputLayout fieldHeight;
    private TextInputLayout fieldAge;

    private TextView textViewNameCountBBTB;
    private TextView childScoreBBTB;
    private TextView childStatusBBTB;

    private EditText childDateCountBBTB;
    private EditText childGenderCountBBTB;
    private EditText childMonthCountBBTB;
    private EditText childWeightCountBBTB;
    private EditText childHeightCountBBTB;

    private Button buttonCountBBTB;
    private Button buttonSaveBBTB;

    private String Tanggal, FormatDate;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_bbtb);

        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference("BeratBadanTinggiBadan")
                .child(getUid()).child(intent.getStringExtra("id"));

        textViewNameCountBBTB = findViewById(R.id.textViewNameCountBBTB);
        childDateCountBBTB = findViewById(R.id.ChildDateCountBBTB);
        childGenderCountBBTB = findViewById(R.id.ChildGenderCountBBTB);
        childMonthCountBBTB = findViewById(R.id.ChildMonthCountBBTB);
        childWeightCountBBTB = findViewById(R.id.ChildWeightCountBBTB);
        childHeightCountBBTB = findViewById(R.id.ChildHeightCountBBTB);
        buttonCountBBTB = findViewById(R.id.ButtonCountBBTB);
        childScoreBBTB = findViewById(R.id.ChildScoreBBTB);
        childStatusBBTB = findViewById(R.id.ChildStatusBBTB);
        buttonSaveBBTB = findViewById(R.id.ButtonSaveBBTB);
        fieldHeight = findViewById(R.id.HeightFieldBBTB);
        fieldWeight = findViewById(R.id.WeightFieldBBTB);
        fieldAge = findViewById(R.id.AgeFieldBBTB);

        buttonCountBBTB.setOnClickListener(this);
        buttonSaveBBTB.setOnClickListener(this);
        buttonSaveBBTB.setVisibility(View.GONE);

        textViewNameCountBBTB.setText(intent.getStringExtra("nama"));
        childGenderCountBBTB.setText(intent.getStringExtra("gender"));
        Tanggal = intent.getStringExtra("tanggal");
        childDateCountBBTB.setText(Tanggal);

        //Get children age in month
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date birthdate = null;
        try {
            birthdate = sdf.parse(Tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long time = currentDate.getTime().getTime() / 1000 - birthdate.getTime() / 1000;
        int years = Math.round(time) / 31536000;
        int months = years * 12 + (Math.round(time - years * 31536000) / 2628000);
        childMonthCountBBTB.setText(String.valueOf(months));
        //Get current date
        Date c = Calendar.getInstance().getTime();
        FormatDate = sdf.format(c);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Berat Badan Tinggi Badan");
        }
    }

    private void countBBTB() {
        String Gender = childGenderCountBBTB.getText().toString();
        String Tinggi = childHeightCountBBTB.getText().toString();
        String Berat = childWeightCountBBTB.getText().toString();
        String Umur = childMonthCountBBTB.getText().toString();

        Double TinggiValue = !Tinggi.equals("") ? Double.parseDouble(Tinggi) : 0;
        Integer UmurValue = !Umur.equals("") ? Integer.parseInt(Umur) : 0;
        Double BeratValue = !Berat.equals("") ? Double.parseDouble(Berat) : 0;
        Double Result = 0.0;

        if (Berat.isEmpty()) {
            fieldWeight.setError("Masukkan berat badan");
        } else if (BeratValue < 1) {
            fieldWeight.setError("Berat badan minimal 1 Kg!");
        } else if (BeratValue > 50) {
            fieldWeight.setError("Berat badan maksimal 50 Kg!");
        } else if (Tinggi.isEmpty()) {
            fieldHeight.setError("Masukkan tinggi badan");
        } else if (TinggiValue < 25) {
            fieldHeight.setError("Tinggi badan minimal 25 Cm!");
        } else if (TinggiValue > 150) {
            fieldHeight.setError("Tinggi badan maksimal 150 Cm!");
        } else if (Umur.isEmpty()) {
            fieldAge.setError("Masukkan umur");
        } else {
            fieldWeight.setErrorEnabled(false);
            fieldHeight.setErrorEnabled(false);
            fieldAge.setErrorEnabled(false);
            buttonSaveBBTB.setVisibility(View.VISIBLE);
            if (Gender.equals("Laki-laki")) {
                if (UmurValue <= 24) {
                    if (TinggiValue <= 45.0) {
                        if (BeratValue <= 2.5) {
                            Result = (BeratValue - 2.5) / (2.5 - 2.3);
                        } else {
                            Result = (BeratValue - 2.5) / (2.7 - 2.5);
                        }
                    } else if (TinggiValue <= 45.5) {
                        if (BeratValue <= 2.5) {
                            Result = (BeratValue - 2.5) / (2.5 - 2.3);
                        } else {
                            Result = (BeratValue - 2.5) / (2.8 - 2.5);
                        }
                    } else if (TinggiValue <= 46.0) {
                        if (BeratValue <= 2.6) {
                            Result = (BeratValue - 2.6) / (2.6 - 2.4);
                        } else {
                            Result = (BeratValue - 2.6) / (2.9 - 2.6);
                        }
                    } else if (TinggiValue <= 46.5) {
                        if (BeratValue <= 2.7) {
                            Result = (BeratValue - 2.7) / (2.7 - 2.5);
                        } else {
                            Result = (BeratValue - 2.7) / (3.0 - 2.7);
                        }
                    } else if (TinggiValue <= 47.0) {
                        if (BeratValue <= 2.8) {
                            Result = (BeratValue - 2.8) / (2.8 - 2.5);
                        } else {
                            Result = (BeratValue - 2.8) / (3.1 - 2.8);
                        }
                    } else if (TinggiValue <= 47.5) {
                        if (BeratValue <= 2.9) {
                            Result = (BeratValue - 2.9) / (2.9 - 2.6);
                        } else {
                            Result = (BeratValue - 2.9) / (3.2 - 2.9);
                        }
                    } else if (TinggiValue <= 48.0) {
                        if (BeratValue <= 3.0) {
                            Result = (BeratValue - 3.0) / (3.0 - 2.7);
                        } else {
                            Result = (BeratValue - 3.0) / (3.3 - 3.0);
                        }
                    } else if (TinggiValue <= 48.5) {
                        if (BeratValue <= 3.1) {
                            Result = (BeratValue - 3.1) / (3.1 - 2.8);
                        } else {
                            Result = (BeratValue - 3.1) / (3.4 - 3.1);
                        }
                    } else if (TinggiValue <= 49.0) {
                        if (BeratValue <= 3.2) {
                            Result = (BeratValue - 3.2) / (3.2 - 2.9);
                        } else {
                            Result = (BeratValue - 3.2) / (3.5 - 3.2);
                        }
                    } else if (TinggiValue <= 49.5) {
                        if (BeratValue <= 3.3) {
                            Result = (BeratValue - 3.3) / (3.3 - 3.0);
                        } else {
                            Result = (BeratValue - 3.3) / (3.6 - 3.3);
                        }
                    } else if (TinggiValue <= 50.0) {
                        if (BeratValue <= 3.4) {
                            Result = (BeratValue - 3.4) / (3.4 - 3.1);
                        } else {
                            Result = (BeratValue - 3.4) / (3.6 - 3.4);
                        }
                    } else if (TinggiValue <= 50.5) {
                        if (BeratValue <= 3.4) {
                            Result = (BeratValue - 3.4) / (3.4 - 3.1);
                        } else {
                            Result = (BeratValue - 3.4) / (3.8 - 3.4);
                        }
                    } else if (TinggiValue <= 51.0) {
                        if (BeratValue <= 3.5) {
                            Result = (BeratValue - 3.5) / (3.5 - 3.2);
                        } else {
                            Result = (BeratValue - 3.5) / (3.9 - 3.5);
                        }
                    } else if (TinggiValue <= 51.5) {
                        if (BeratValue <= 3.6) {
                            Result = (BeratValue - 3.6) / (3.6 - 3.3);
                        } else {
                            Result = (BeratValue - 3.6) / (4.0 - 3.6);
                        }
                    } else if (TinggiValue <= 52.0) {
                        if (BeratValue <= 3.8) {
                            Result = (BeratValue - 3.8) / (3.8 - 3.5);
                        } else {
                            Result = (BeratValue - 3.8) / (4.1 - 3.8);
                        }
                    } else if (TinggiValue <= 52.5) {
                        if (BeratValue <= 3.9) {
                            Result = (BeratValue - 3.9) / (3.9 - 3.6);
                        } else {
                            Result = (BeratValue - 3.9) / (4.2 - 3.9);
                        }
                    } else if (TinggiValue <= 53.0) {
                        if (BeratValue <= 4.0) {
                            Result = (BeratValue - 4.0) / (4.0 - 3.7);
                        } else {
                            Result = (BeratValue - 4.0) / (4.4 - 4.0);
                        }
                    } else if (TinggiValue <= 53.5) {
                        if (BeratValue <= 4.1) {
                            Result = (BeratValue - 4.1) / (4.1 - 3.8);
                        } else {
                            Result = (BeratValue - 4.1) / (4.5 - 4.1);
                        }
                    } else if (TinggiValue <= 54.0) {
                        if (BeratValue <= 4.3) {
                            Result = (BeratValue - 4.3) / (4.3 - 3.9);
                        } else {
                            Result = (BeratValue - 4.3) / (4.7 - 4.3);
                        }
                    } else if (TinggiValue <= 54.5) {
                        if (BeratValue <= 4.4) {
                            Result = (BeratValue - 4.4) / (4.4 - 4.0);
                        } else {
                            Result = (BeratValue - 4.4) / (4.8 - 4.4);
                        }
                    } else if (TinggiValue <= 55.0) {
                        if (BeratValue <= 4.5) {
                            Result = (BeratValue - 4.5) / (4.5 - 4.2);
                        } else {
                            Result = (BeratValue - 4.5) / (5.0 - 4.5);
                        }
                    } else if (TinggiValue <= 55.5) {
                        if (BeratValue <= 4.7) {
                            Result = (BeratValue - 4.7) / (4.7 - 4.3);
                        } else {
                            Result = (BeratValue - 4.7) / (5.1 - 4.7);
                        }
                    } else if (TinggiValue <= 56.0) {
                        if (BeratValue <= 4.8) {
                            Result = (BeratValue - 4.8) / (4.8 - 4.4);
                        } else {
                            Result = (BeratValue - 4.8) / (5.3 - 4.8);
                        }
                    } else if (TinggiValue <= 56.5) {
                        if (BeratValue <= 5.0) {
                            Result = (BeratValue - 5.0) / (5.0 - 4.6);
                        } else {
                            Result = (BeratValue - 5.0) / (5.4 - 5.0);
                        }
                    } else if (TinggiValue <= 57.0) {
                        if (BeratValue <= 5.1) {
                            Result = (BeratValue - 5.1) / (5.1 - 4.7);
                        } else {
                            Result = (BeratValue - 5.1) / (5.6 - 5.1);
                        }
                    } else if (TinggiValue <= 57.5) {
                        if (BeratValue <= 5.3) {
                            Result = (BeratValue - 5.3) / (5.3 - 4.9);
                        } else {
                            Result = (BeratValue - 5.3) / (5.7 - 5.3);
                        }
                    } else if (TinggiValue <= 58.0) {
                        if (BeratValue <= 5.4) {
                            Result = (BeratValue - 5.4) / (5.4 - 5.0);
                        } else {
                            Result = (BeratValue - 5.4) / (5.9 - 5.4);
                        }
                    } else if (TinggiValue <= 58.5) {
                        if (BeratValue <= 5.6) {
                            Result = (BeratValue - 5.6) / (5.6 - 5.1);
                        } else {
                            Result = (BeratValue - 5.6) / (6.1 - 5.6);
                        }
                    } else if (TinggiValue <= 59.0) {
                        if (BeratValue <= 5.7) {
                            Result = (BeratValue - 5.7) / (5.7 - 5.1);
                        } else {
                            Result = (BeratValue - 5.7) / (6.1 - 5.7);
                        }
                    } else if (TinggiValue <= 59.5) {
                        if (BeratValue <= 5.9) {
                            Result = (BeratValue - 5.9) / (5.9 - 5.4);
                        } else {
                            Result = (BeratValue - 5.9) / (6.4 - 5.9);
                        }
                    } else if (TinggiValue <= 60.0) {
                        if (BeratValue <= 6.0) {
                            Result = (BeratValue - 6.0) / (6.0 - 5.5);
                        } else {
                            Result = (BeratValue - 6.0) / (6.5 - 6.0);
                        }
                    } else if (TinggiValue <= 60.5) {
                        if (BeratValue <= 6.1) {
                            Result = (BeratValue - 6.1) / (6.1 - 5.6);
                        } else {
                            Result = (BeratValue - 6.1) / (6.7 - 6.1);
                        }
                    } else if (TinggiValue <= 61.0) {
                        if (BeratValue <= 6.3) {
                            Result = (BeratValue - 6.3) / (6.3 - 5.8);
                        } else {
                            Result = (BeratValue - 6.3) / (6.8 - 6.3);
                        }
                    } else if (TinggiValue <= 61.5) {
                        if (BeratValue <= 6.4) {
                            Result = (BeratValue - 6.4) / (6.4 - 5.9);
                        } else {
                            Result = (BeratValue - 6.4) / (7.0 - 6.4);
                        }
                    } else if (TinggiValue <= 62.0) {
                        if (BeratValue <= 6.5) {
                            Result = (BeratValue - 6.5) / (6.5 - 6.0);
                        } else {
                            Result = (BeratValue - 6.5) / (7.1 - 6.5);
                        }
                    } else if (TinggiValue <= 62.5) {
                        if (BeratValue <= 6.7) {
                            Result = (BeratValue - 6.7) / (6.7 - 6.1);
                        } else {
                            Result = (BeratValue - 6.7) / (7.2 - 6.7);
                        }
                    } else if (TinggiValue <= 63.0) {
                        if (BeratValue <= 6.8) {
                            Result = (BeratValue - 6.8) / (6.8 - 6.2);
                        } else {
                            Result = (BeratValue - 6.8) / (7.4 - 6.8);
                        }
                    } else if (TinggiValue <= 63.5) {
                        if (BeratValue <= 6.9) {
                            Result = (BeratValue - 6.9) / (6.9 - 6.4);
                        } else {
                            Result = (BeratValue - 6.9) / (7.5 - 6.9);
                        }
                    } else if (TinggiValue <= 64.0) {
                        if (BeratValue <= 7.0) {
                            Result = (BeratValue - 7.0) / (7.0 - 6.5);
                        } else {
                            Result = (BeratValue - 7.0) / (7.6 - 7.0);
                        }
                    } else if (TinggiValue <= 64.5) {
                        if (BeratValue <= 7.1) {
                            Result = (BeratValue - 7.1) / (7.1 - 6.6);
                        } else {
                            Result = (BeratValue - 7.1) / (7.8 - 7.1);
                        }
                    } else if (TinggiValue <= 65.0) {
                        if (BeratValue <= 7.3) {
                            Result = (BeratValue - 7.3) / (7.3 - 6.7);
                        } else {
                            Result = (BeratValue - 7.3) / (7.9 - 7.3);
                        }
                    } else if (TinggiValue <= 65.5) {
                        if (BeratValue <= 7.4) {
                            Result = (BeratValue - 7.4) / (7.4 - 6.8);
                        } else {
                            Result = (BeratValue - 7.4) / (8.0 - 7.4);
                        }
                    } else if (TinggiValue <= 66.0) {
                        if (BeratValue <= 7.5) {
                            Result = (BeratValue - 7.5) / (7.5 - 6.9);
                        } else {
                            Result = (BeratValue - 7.5) / (8.2 - 7.5);
                        }
                    } else if (TinggiValue <= 66.5) {
                        if (BeratValue <= 7.6) {
                            Result = (BeratValue - 7.6) / (7.6 - 7.0);
                        } else {
                            Result = (BeratValue - 7.6) / (8.3 - 7.6);
                        }
                    } else if (TinggiValue <= 67.0) {
                        if (BeratValue <= 7.7) {
                            Result = (BeratValue - 7.7) / (7.7 - 7.1);
                        } else {
                            Result = (BeratValue - 7.7) / (8.4 - 7.7);
                        }
                    } else if (TinggiValue <= 67.5) {
                        if (BeratValue <= 7.9) {
                            Result = (BeratValue - 7.9) / (7.9 - 7.2);
                        } else {
                            Result = (BeratValue - 7.9) / (8.5 - 7.9);
                        }
                    } else if (TinggiValue <= 68.0) {
                        if (BeratValue <= 8.0) {
                            Result = (BeratValue - 8.0) / (8.0 - 7.3);
                        } else {
                            Result = (BeratValue - 8.0) / (8.7 - 8.0);
                        }
                    } else if (TinggiValue <= 68.5) {
                        if (BeratValue <= 8.1) {
                            Result = (BeratValue - 8.1) / (8.1 - 7.5);
                        } else {
                            Result = (BeratValue - 8.1) / (8.8 - 8.1);
                        }
                    } else if (TinggiValue <= 69.0) {
                        if (BeratValue <= 8.2) {
                            Result = (BeratValue - 8.2) / (8.2 - 7.6);
                        } else {
                            Result = (BeratValue - 8.2) / (8.9 - 8.2);
                        }
                    } else if (TinggiValue <= 69.5) {
                        if (BeratValue <= 8.3) {
                            Result = (BeratValue - 8.3) / (8.3 - 7.7);
                        } else {
                            Result = (BeratValue - 8.3) / (9.0 - 8.3);
                        }
                    } else if (TinggiValue <= 70.0) {
                        if (BeratValue <= 8.4) {
                            Result = (BeratValue - 8.4) / (8.4 - 7.8);
                        } else {
                            Result = (BeratValue - 8.4) / (9.2 - 8.4);
                        }
                    } else if (TinggiValue <= 70.5) {
                        if (BeratValue <= 8.5) {
                            Result = (BeratValue - 8.5) / (8.5 - 7.9);
                        } else {
                            Result = (BeratValue - 8.5) / (9.3 - 8.5);
                        }
                    } else if (TinggiValue <= 71.0) {
                        if (BeratValue <= 8.6) {
                            Result = (BeratValue - 8.6) / (8.6 - 8.0);
                        } else {
                            Result = (BeratValue - 8.6) / (9.4 - 8.6);
                        }
                    } else if (TinggiValue <= 71.5) {
                        if (BeratValue <= 8.8) {
                            Result = (BeratValue - 8.8) / (8.8 - 8.1);
                        } else {
                            Result = (BeratValue - 8.8) / (9.5 - 8.8);
                        }
                    } else if (TinggiValue <= 72.0) {
                        if (BeratValue <= 8.9) {
                            Result = (BeratValue - 8.9) / (8.9 - 8.2);
                        } else {
                            Result = (BeratValue - 8.9) / (9.6 - 8.9);
                        }
                    } else if (TinggiValue <= 72.5) {
                        if (BeratValue <= 9.0) {
                            Result = (BeratValue - 9.0) / (9.0 - 8.3);
                        } else {
                            Result = (BeratValue - 9.0) / (9.8 - 9.0);
                        }
                    } else if (TinggiValue <= 73.0) {
                        if (BeratValue <= 9.1) {
                            Result = (BeratValue - 9.1) / (9.1 - 8.4);
                        } else {
                            Result = (BeratValue - 9.1) / (9.9 - 9.1);
                        }
                    } else if (TinggiValue <= 73.5) {
                        if (BeratValue <= 9.2) {
                            Result = (BeratValue - 9.2) / (9.2 - 8.5);
                        } else {
                            Result = (BeratValue - 9.2) / (10.0 - 9.2);
                        }
                    } else if (TinggiValue <= 74.0) {
                        if (BeratValue <= 9.3) {
                            Result = (BeratValue - 9.3) / (9.3 - 8.6);
                        } else {
                            Result = (BeratValue - 9.3) / (10.1 - 9.3);
                        }
                    } else if (TinggiValue <= 74.5) {
                        if (BeratValue <= 9.4) {
                            Result = (BeratValue - 9.4) / (9.4 - 8.7);
                        } else {
                            Result = (BeratValue - 9.4) / (10.2 - 9.4);
                        }
                    } else if (TinggiValue <= 75.0) {
                        if (BeratValue <= 9.5) {
                            Result = (BeratValue - 9.5) / (9.5 - 8.8);
                        } else {
                            Result = (BeratValue - 9.5) / (10.3 - 9.5);
                        }
                    } else if (TinggiValue <= 75.5) {
                        if (BeratValue <= 9.6) {
                            Result = (BeratValue - 9.6) / (9.6 - 8.8);
                        } else {
                            Result = (BeratValue - 9.6) / (10.4 - 9.6);
                        }
                    } else if (TinggiValue <= 76.0) {
                        if (BeratValue <= 9.7) {
                            Result = (BeratValue - 9.7) / (9.7 - 8.9);
                        } else {
                            Result = (BeratValue - 9.7) / (10.6 - 9.7);
                        }
                    } else if (TinggiValue <= 76.5) {
                        if (BeratValue <= 9.8) {
                            Result = (BeratValue - 9.8) / (9.8 - 9.0);
                        } else {
                            Result = (BeratValue - 9.8) / (10.7 - 9.8);
                        }
                    } else if (TinggiValue <= 77.0) {
                        if (BeratValue <= 9.9) {
                            Result = (BeratValue - 9.9) / (9.9 - 9.1);
                        } else {
                            Result = (BeratValue - 9.9) / (10.8 - 9.9);
                        }
                    } else if (TinggiValue <= 77.5) {
                        if (BeratValue <= 10.0) {
                            Result = (BeratValue - 10.0) / (10.0 - 9.2);
                        } else {
                            Result = (BeratValue - 10.0) / (10.9 - 10.0);
                        }
                    } else if (TinggiValue <= 78.0) {
                        if (BeratValue <= 10.1) {
                            Result = (BeratValue - 10.1) / (10.1 - 9.3);
                        } else {
                            Result = (BeratValue - 10.1) / (11.0 - 10.1);
                        }
                    } else if (TinggiValue <= 78.5) {
                        if (BeratValue <= 10.2) {
                            Result = (BeratValue - 10.2) / (10.2 - 9.4);
                        } else {
                            Result = (BeratValue - 10.2) / (11.1 - 10.2);
                        }
                    } else if (TinggiValue <= 79.0) {
                        if (BeratValue <= 10.3) {
                            Result = (BeratValue - 10.3) / (10.3 - 9.5);
                        } else {
                            Result = (BeratValue - 10.3) / (11.2 - 10.3);
                        }
                    } else if (TinggiValue <= 79.5) {
                        if (BeratValue <= 10.4) {
                            Result = (BeratValue - 10.4) / (10.4 - 9.5);
                        } else {
                            Result = (BeratValue - 10.4) / (11.3 - 10.4);
                        }
                    } else if (TinggiValue <= 80.0) {
                        if (BeratValue <= 10.4) {
                            Result = (BeratValue - 10.4) / (10.4 - 9.6);
                        } else {
                            Result = (BeratValue - 10.4) / (11.4 - 10.4);
                        }
                    } else if (TinggiValue <= 80.5) {
                        if (BeratValue <= 10.5) {
                            Result = (BeratValue - 10.5) / (10.5 - 9.7);
                        } else {
                            Result = (BeratValue - 10.5) / (11.5 - 10.5);
                        }
                    } else if (TinggiValue <= 81.0) {
                        if (BeratValue <= 10.6) {
                            Result = (BeratValue - 10.6) / (10.6 - 9.8);
                        } else {
                            Result = (BeratValue - 10.6) / (11.6 - 10.6);
                        }
                    } else if (TinggiValue <= 81.5) {
                        if (BeratValue <= 10.7) {
                            Result = (BeratValue - 10.7) / (10.7 - 9.9);
                        } else {
                            Result = (BeratValue - 10.7) / (11.7 - 10.7);
                        }
                    } else if (TinggiValue <= 82.0) {
                        if (BeratValue <= 10.8) {
                            Result = (BeratValue - 10.8) / (10.8 - 10.0);
                        } else {
                            Result = (BeratValue - 10.8) / (11.8 - 10.8);
                        }
                    } else if (TinggiValue <= 82.5) {
                        if (BeratValue <= 10.9) {
                            Result = (BeratValue - 10.9) / (10.9 - 10.1);
                        } else {
                            Result = (BeratValue - 10.9) / (11.9 - 10.9);
                        }
                    } else if (TinggiValue <= 83.0) {
                        if (BeratValue <= 11.0) {
                            Result = (BeratValue - 11.0) / (11.0 - 10.2);
                        } else {
                            Result = (BeratValue - 11.0) / (12.0 - 11.0);
                        }
                    } else if (TinggiValue <= 83.5) {
                        if (BeratValue <= 11.2) {
                            Result = (BeratValue - 11.2) / (11.2 - 10.3);
                        } else {
                            Result = (BeratValue - 11.2) / (12.1 - 11.2);
                        }
                    } else if (TinggiValue <= 84.0) {
                        if (BeratValue <= 11.3) {
                            Result = (BeratValue - 11.3) / (11.3 - 10.4);
                        } else {
                            Result = (BeratValue - 11.3) / (12.2 - 11.3);
                        }
                    } else if (TinggiValue <= 84.5) {
                        if (BeratValue <= 11.4) {
                            Result = (BeratValue - 11.4) / (11.4 - 10.5);
                        } else {
                            Result = (BeratValue - 11.4) / (12.4 - 11.4);
                        }
                    } else if (TinggiValue <= 85.0) {
                        if (BeratValue <= 11.5) {
                            Result = (BeratValue - 11.5) / (11.5 - 10.6);
                        } else {
                            Result = (BeratValue - 11.5) / (12.5 - 11.5);
                        }
                    } else if (TinggiValue <= 85.5) {
                        if (BeratValue <= 11.6) {
                            Result = (BeratValue - 11.6) / (11.6 - 10.7);
                        } else {
                            Result = (BeratValue - 11.6) / (12.6 - 11.6);
                        }
                    } else if (TinggiValue <= 86.0) {
                        if (BeratValue <= 11.7) {
                            Result = (BeratValue - 11.7) / (11.7 - 10.8);
                        } else {
                            Result = (BeratValue - 11.7) / (12.8 - 11.7);
                        }
                    } else if (TinggiValue <= 86.5) {
                        if (BeratValue <= 11.9) {
                            Result = (BeratValue - 11.9) / (11.9 - 11.0);
                        } else {
                            Result = (BeratValue - 11.9) / (12.9 - 11.9);
                        }
                    } else if (TinggiValue <= 87.0) {
                        if (BeratValue <= 12.0) {
                            Result = (BeratValue - 12.0) / (12.0 - 11.1);
                        } else {
                            Result = (BeratValue - 12.0) / (13.0 - 12.0);
                        }
                    } else if (TinggiValue <= 87.5) {
                        if (BeratValue <= 12.1) {
                            Result = (BeratValue - 12.1) / (12.1 - 11.2);
                        } else {
                            Result = (BeratValue - 12.1) / (13.2 - 12.1);
                        }
                    } else if (TinggiValue <= 88.0) {
                        if (BeratValue <= 12.2) {
                            Result = (BeratValue - 12.2) / (12.2 - 11.3);
                        } else {
                            Result = (BeratValue - 12.2) / (13.3 - 12.2);
                        }
                    } else if (TinggiValue <= 88.5) {
                        if (BeratValue <= 12.4) {
                            Result = (BeratValue - 12.4) / (12.4 - 11.4);
                        } else {
                            Result = (BeratValue - 12.4) / (13.4 - 12.4);
                        }
                    } else if (TinggiValue <= 89.0) {
                        if (BeratValue <= 12.5) {
                            Result = (BeratValue - 12.5) / (12.5 - 11.5);
                        } else {
                            Result = (BeratValue - 12.5) / (13.5 - 12.5);
                        }
                    } else if (TinggiValue <= 89.5) {
                        if (BeratValue <= 12.6) {
                            Result = (BeratValue - 12.6) / (12.6 - 11.6);
                        } else {
                            Result = (BeratValue - 12.6) / (13.7 - 12.6);
                        }
                    } else if (TinggiValue <= 90.0) {
                        if (BeratValue <= 12.7) {
                            Result = (BeratValue - 12.7) / (12.7 - 11.8);
                        } else {
                            Result = (BeratValue - 12.7) / (13.8 - 12.7);
                        }
                    } else if (TinggiValue <= 90.5) {
                        if (BeratValue <= 12.8) {
                            Result = (BeratValue - 12.8) / (12.8 - 11.9);
                        } else {
                            Result = (BeratValue - 12.8) / (13.9 - 12.8);
                        }
                    } else if (TinggiValue <= 91.0) {
                        if (BeratValue <= 13.0) {
                            Result = (BeratValue - 13.0) / (13.0 - 12.0);
                        } else {
                            Result = (BeratValue - 13.0) / (14.1 - 13.0);
                        }
                    } else if (TinggiValue <= 91.5) {
                        if (BeratValue <= 13.1) {
                            Result = (BeratValue - 13.1) / (13.1 - 12.1);
                        } else {
                            Result = (BeratValue - 13.1) / (14.2 - 13.1);
                        }
                    } else if (TinggiValue <= 92.0) {
                        if (BeratValue <= 13.2) {
                            Result = (BeratValue - 13.2) / (13.2 - 12.2);
                        } else {
                            Result = (BeratValue - 13.2) / (14.3 - 13.2);
                        }
                    } else if (TinggiValue <= 92.5) {
                        if (BeratValue <= 13.3) {
                            Result = (BeratValue - 13.3) / (13.3 - 12.3);
                        } else {
                            Result = (BeratValue - 13.3) / (14.4 - 13.3);
                        }
                    } else if (TinggiValue <= 93.0) {
                        if (BeratValue <= 13.4) {
                            Result = (BeratValue - 13.4) / (13.4 - 12.4);
                        } else {
                            Result = (BeratValue - 13.4) / (14.6 - 13.4);
                        }
                    } else if (TinggiValue <= 93.5) {
                        if (BeratValue <= 13.5) {
                            Result = (BeratValue - 13.5) / (13.5 - 12.5);
                        } else {
                            Result = (BeratValue - 13.5) / (14.7 - 13.5);
                        }
                    } else if (TinggiValue <= 94.0) {
                        if (BeratValue <= 13.7) {
                            Result = (BeratValue - 13.7) / (13.7 - 12.6);
                        } else {
                            Result = (BeratValue - 13.7) / (14.8 - 13.7);
                        }
                    } else if (TinggiValue <= 94.5) {
                        if (BeratValue <= 13.8) {
                            Result = (BeratValue - 13.8) / (13.8 - 12.7);
                        } else {
                            Result = (BeratValue - 13.8) / (14.9 - 13.8);
                        }
                    } else if (TinggiValue <= 95.0) {
                        if (BeratValue <= 13.9) {
                            Result = (BeratValue - 13.9) / (13.9 - 12.8);
                        } else {
                            Result = (BeratValue - 13.9) / (15.1 - 13.9);
                        }
                    } else if (TinggiValue <= 95.5) {
                        if (BeratValue <= 14.0) {
                            Result = (BeratValue - 14.0) / (14.0 - 12.9);
                        } else {
                            Result = (BeratValue - 14.0) / (15.2 - 14.0);
                        }
                    } else if (TinggiValue <= 96.0) {
                        if (BeratValue <= 14.1) {
                            Result = (BeratValue - 14.1) / (14.1 - 13.1);
                        } else {
                            Result = (BeratValue - 14.1) / (15.3 - 14.1);
                        }
                    } else if (TinggiValue <= 96.5) {
                        if (BeratValue <= 14.3) {
                            Result = (BeratValue - 14.3) / (14.3 - 13.2);
                        } else {
                            Result = (BeratValue - 14.3) / (15.5 - 14.3);
                        }
                    } else if (TinggiValue <= 97.0) {
                        if (BeratValue <= 14.4) {
                            Result = (BeratValue - 14.4) / (14.4 - 13.3);
                        } else {
                            Result = (BeratValue - 14.4) / (15.6 - 14.4);
                        }
                    } else if (TinggiValue <= 97.5) {
                        if (BeratValue <= 14.5) {
                            Result = (BeratValue - 14.5) / (14.5 - 13.4);
                        } else {
                            Result = (BeratValue - 14.5) / (15.7 - 14.5);
                        }
                    } else if (TinggiValue <= 98.0) {
                        if (BeratValue <= 14.6) {
                            Result = (BeratValue - 14.6) / (14.6 - 13.5);
                        } else {
                            Result = (BeratValue - 14.6) / (15.9 - 14.6);
                        }
                    } else if (TinggiValue <= 98.5) {
                        if (BeratValue <= 14.8) {
                            Result = (BeratValue - 14.8) / (14.8 - 13.6);
                        } else {
                            Result = (BeratValue - 14.8) / (16.0 - 14.8);
                        }
                    } else if (TinggiValue <= 99.0) {
                        if (BeratValue <= 14.9) {
                            Result = (BeratValue - 14.9) / (14.9 - 13.7);
                        } else {
                            Result = (BeratValue - 14.9) / (16.2 - 14.9);
                        }
                    } else if (TinggiValue <= 99.5) {
                        if (BeratValue <= 15.0) {
                            Result = (BeratValue - 15.0) / (15.0 - 13.9);
                        } else {
                            Result = (BeratValue - 15.0) / (16.3 - 15.0);
                        }
                    } else if (TinggiValue <= 100.0) {
                        if (BeratValue <= 15.2) {
                            Result = (BeratValue - 15.2) / (15.2 - 14.0);
                        } else {
                            Result = (BeratValue - 15.2) / (16.5 - 15.2);
                        }
                    } else if (TinggiValue <= 100.5) {
                        if (BeratValue <= 15.3) {
                            Result = (BeratValue - 15.3) / (15.3 - 14.1);
                        } else {
                            Result = (BeratValue - 15.3) / (16.6 - 15.3);
                        }
                    } else if (TinggiValue <= 101.0) {
                        if (BeratValue <= 15.4) {
                            Result = (BeratValue - 15.4) / (15.4 - 14.2);
                        } else {
                            Result = (BeratValue - 15.4) / (16.8 - 15.4);
                        }
                    } else if (TinggiValue <= 101.5) {
                        if (BeratValue <= 15.6) {
                            Result = (BeratValue - 15.6) / (15.6 - 14.4);
                        } else {
                            Result = (BeratValue - 15.6) / (16.9 - 15.6);
                        }
                    } else if (TinggiValue <= 102.0) {
                        if (BeratValue <= 15.7) {
                            Result = (BeratValue - 15.7) / (15.7 - 14.5);
                        } else {
                            Result = (BeratValue - 15.7) / (17.1 - 15.7);
                        }
                    } else if (TinggiValue <= 102.5) {
                        if (BeratValue <= 15.9) {
                            Result = (BeratValue - 15.9) / (15.9 - 14.6);
                        } else {
                            Result = (BeratValue - 15.9) / (17.3 - 15.9);
                        }
                    } else if (TinggiValue <= 103.0) {
                        if (BeratValue <= 16.0) {
                            Result = (BeratValue - 16.0) / (16.0 - 14.8);
                        } else {
                            Result = (BeratValue - 16.0) / (17.4 - 16.0);
                        }
                    } else if (TinggiValue <= 103.5) {
                        if (BeratValue <= 16.2) {
                            Result = (BeratValue - 16.2) / (16.2 - 14.9);
                        } else {
                            Result = (BeratValue - 16.2) / (17.6 - 16.2);
                        }
                    } else if (TinggiValue <= 104.0) {
                        if (BeratValue <= 16.3) {
                            Result = (BeratValue - 16.3) / (16.3 - 15.0);
                        } else {
                            Result = (BeratValue - 16.3) / (17.8 - 16.3);
                        }
                    } else if (TinggiValue <= 104.5) {
                        if (BeratValue <= 16.5) {
                            Result = (BeratValue - 16.5) / (16.5 - 15.2);
                        } else {
                            Result = (BeratValue - 16.5) / (17.9 - 16.5);
                        }
                    } else if (TinggiValue <= 105.0) {
                        if (BeratValue <= 16.6) {
                            Result = (BeratValue - 16.6) / (16.6 - 15.3);
                        } else {
                            Result = (BeratValue - 16.6) / (18.1 - 16.6);
                        }
                    } else if (TinggiValue <= 105.5) {
                        if (BeratValue <= 16.8) {
                            Result = (BeratValue - 16.8) / (16.8 - 15.4);
                        } else {
                            Result = (BeratValue - 16.8) / (18.3 - 16.8);
                        }
                    } else if (TinggiValue <= 106.0) {
                        if (BeratValue <= 16.9) {
                            Result = (BeratValue - 16.9) / (16.9 - 15.6);
                        } else {
                            Result = (BeratValue - 16.9) / (18.5 - 16.9);
                        }
                    } else if (TinggiValue <= 106.5) {
                        if (BeratValue <= 17.1) {
                            Result = (BeratValue - 17.1) / (17.1 - 15.7);
                        } else {
                            Result = (BeratValue - 17.1) / (18.6 - 17.1);
                        }
                    } else if (TinggiValue <= 107.0) {
                        if (BeratValue <= 17.3) {
                            Result = (BeratValue - 17.3) / (17.3 - 15.9);
                        } else {
                            Result = (BeratValue - 17.3) / (18.8 - 17.3);
                        }
                    } else if (TinggiValue <= 107.5) {
                        if (BeratValue <= 17.4) {
                            Result = (BeratValue - 17.4) / (17.4 - 16.0);
                        } else {
                            Result = (BeratValue - 17.4) / (19.0 - 17.4);
                        }
                    } else if (TinggiValue <= 108.0) {
                        if (BeratValue <= 17.6) {
                            Result = (BeratValue - 17.6) / (17.6 - 16.2);
                        } else {
                            Result = (BeratValue - 17.6) / (19.2 - 17.6);
                        }
                    } else if (TinggiValue <= 108.5) {
                        if (BeratValue <= 17.8) {
                            Result = (BeratValue - 17.8) / (17.8 - 16.3);
                        } else {
                            Result = (BeratValue - 17.8) / (19.4 - 17.8);
                        }
                    } else if (TinggiValue <= 109.0) {
                        if (BeratValue <= 17.9) {
                            Result = (BeratValue - 17.9) / (17.9 - 16.5);
                        } else {
                            Result = (BeratValue - 17.9) / (19.6 - 17.9);
                        }
                    } else if (TinggiValue <= 109.5) {
                        if (BeratValue <= 18.1) {
                            Result = (BeratValue - 18.1) / (18.1 - 16.6);
                        } else {
                            Result = (BeratValue - 18.1) / (19.8 - 18.1);
                        }
                    } else if (TinggiValue <= 110.0) {
                        if (BeratValue <= 18.3) {
                            Result = (BeratValue - 18.3) / (18.3 - 16.8);
                        } else {
                            Result = (BeratValue - 18.3) / (20.0 - 18.3);
                        }
                    }
                } else if (UmurValue <= 60) {
                    if (TinggiValue <= 65.0) {
                        if (BeratValue <= 7.4) {
                            Result = (BeratValue - 7.4) / (7.4 - 6.9);
                        } else {
                            Result = (BeratValue - 7.4) / (8.1 - 7.4);
                        }
                    } else if (TinggiValue <= 65.5) {
                        if (BeratValue <= 7.6) {
                            Result = (BeratValue - 7.6) / (7.6 - 7.0);
                        } else {
                            Result = (BeratValue - 7.6) / (8.2 - 7.6);
                        }
                    } else if (TinggiValue <= 66.0) {
                        if (BeratValue <= 7.7) {
                            Result = (BeratValue - 7.7) / (7.7 - 7.1);
                        } else {
                            Result = (BeratValue - 7.7) / (8.3 - 7.7);
                        }
                    } else if (TinggiValue <= 66.5) {
                        if (BeratValue <= 7.8) {
                            Result = (BeratValue - 7.8) / (7.8 - 7.2);
                        } else {
                            Result = (BeratValue - 7.8) / (8.5 - 7.8);
                        }
                    } else if (TinggiValue <= 67.0) {
                        if (BeratValue <= 7.9) {
                            Result = (BeratValue - 7.9) / (7.9 - 7.3);
                        } else {
                            Result = (BeratValue - 7.9) / (8.6 - 7.9);
                        }
                    } else if (TinggiValue <= 67.5) {
                        if (BeratValue <= 8.0) {
                            Result = (BeratValue - 8.0) / (8.0 - 7.4);
                        } else {
                            Result = (BeratValue - 8.0) / (8.7 - 8.0);
                        }
                    } else if (TinggiValue <= 68.0) {
                        if (BeratValue <= 8.1) {
                            Result = (BeratValue - 8.1) / (8.1 - 7.5);
                        } else {
                            Result = (BeratValue - 8.1) / (8.8 - 8.1);
                        }
                    } else if (TinggiValue <= 68.5) {
                        if (BeratValue <= 8.2) {
                            Result = (BeratValue - 8.2) / (8.2 - 7.6);
                        } else {
                            Result = (BeratValue - 8.2) / (9.0 - 8.2);
                        }
                    } else if (TinggiValue <= 69.0) {
                        if (BeratValue <= 8.4) {
                            Result = (BeratValue - 8.4) / (8.4 - 7.7);
                        } else {
                            Result = (BeratValue - 8.4) / (9.1 - 8.4);
                        }
                    } else if (TinggiValue <= 69.5) {
                        if (BeratValue <= 8.5) {
                            Result = (BeratValue - 8.5) / (8.5 - 7.8);
                        } else {
                            Result = (BeratValue - 8.5) / (9.2 - 8.5);
                        }
                    } else if (TinggiValue <= 70.0) {
                        if (BeratValue <= 8.6) {
                            Result = (BeratValue - 8.6) / (8.6 - 7.9);
                        } else {
                            Result = (BeratValue - 8.6) / (9.3 - 8.6);
                        }
                    } else if (TinggiValue <= 70.5) {
                        if (BeratValue <= 8.7) {
                            Result = (BeratValue - 8.7) / (8.7 - 8.0);
                        } else {
                            Result = (BeratValue - 8.7) / (9.5 - 8.7);
                        }
                    } else if (TinggiValue <= 71.0) {
                        if (BeratValue <= 8.8) {
                            Result = (BeratValue - 8.8) / (8.8 - 8.1);
                        } else {
                            Result = (BeratValue - 8.8) / (9.6 - 8.8);
                        }
                    } else if (TinggiValue <= 71.5) {
                        if (BeratValue <= 8.9) {
                            Result = (BeratValue - 8.9) / (8.9 - 8.2);
                        } else {
                            Result = (BeratValue - 8.9) / (9.7 - 8.9);
                        }
                    } else if (TinggiValue <= 72.0) {
                        if (BeratValue <= 9.0) {
                            Result = (BeratValue - 9.0) / (9.0 - 8.3);
                        } else {
                            Result = (BeratValue - 9.0) / (9.8 - 9.0);
                        }
                    } else if (TinggiValue <= 72.5) {
                        if (BeratValue <= 9.1) {
                            Result = (BeratValue - 9.1) / (9.1 - 8.4);
                        } else {
                            Result = (BeratValue - 9.1) / (9.9 - 9.1);
                        }
                    } else if (TinggiValue <= 73.0) {
                        if (BeratValue <= 9.2) {
                            Result = (BeratValue - 9.2) / (9.2 - 8.5);
                        } else {
                            Result = (BeratValue - 9.2) / (10.0 - 9.2);
                        }
                    } else if (TinggiValue <= 73.5) {
                        if (BeratValue <= 9.3) {
                            Result = (BeratValue - 9.3) / (9.3 - 8.6);
                        } else {
                            Result = (BeratValue - 9.3) / (10.2 - 9.3);
                        }
                    } else if (TinggiValue <= 74.0) {
                        if (BeratValue <= 9.4) {
                            Result = (BeratValue - 9.4) / (9.4 - 8.7);
                        } else {
                            Result = (BeratValue - 9.4) / (10.3 - 9.4);
                        }
                    } else if (TinggiValue <= 74.5) {
                        if (BeratValue <= 9.5) {
                            Result = (BeratValue - 9.5) / (9.5 - 8.8);
                        } else {
                            Result = (BeratValue - 9.5) / (10.4 - 9.5);
                        }
                    } else if (TinggiValue <= 75.0) {
                        if (BeratValue <= 9.6) {
                            Result = (BeratValue - 9.6) / (9.6 - 8.9);
                        } else {
                            Result = (BeratValue - 9.6) / (10.5 - 9.6);
                        }
                    } else if (TinggiValue <= 75.5) {
                        if (BeratValue <= 9.7) {
                            Result = (BeratValue - 9.7) / (9.7 - 9.0);
                        } else {
                            Result = (BeratValue - 9.7) / (10.6 - 9.7);
                        }
                    } else if (TinggiValue <= 76.0) {
                        if (BeratValue <= 9.8) {
                            Result = (BeratValue - 9.8) / (9.8 - 9.1);
                        } else {
                            Result = (BeratValue - 9.8) / (10.7 - 9.8);
                        }
                    } else if (TinggiValue <= 76.5) {
                        if (BeratValue <= 9.9) {
                            Result = (BeratValue - 9.9) / (9.9 - 9.2);
                        } else {
                            Result = (BeratValue - 9.9) / (10.8 - 9.9);
                        }
                    } else if (TinggiValue <= 77.0) {
                        if (BeratValue <= 10.0) {
                            Result = (BeratValue - 10.0) / (10.0 - 9.2);
                        } else {
                            Result = (BeratValue - 10.0) / (10.9 - 10.0);
                        }
                    } else if (TinggiValue <= 77.5) {
                        if (BeratValue <= 10.1) {
                            Result = (BeratValue - 10.1) / (10.1 - 9.3);
                        } else {
                            Result = (BeratValue - 10.1) / (11.0 - 10.1);
                        }
                    } else if (TinggiValue <= 78.0) {
                        if (BeratValue <= 10.2) {
                            Result = (BeratValue - 10.2) / (10.2 - 9.4);
                        } else {
                            Result = (BeratValue - 10.2) / (11.1 - 10.2);
                        }
                    } else if (TinggiValue <= 78.5) {
                        if (BeratValue <= 10.3) {
                            Result = (BeratValue - 10.3) / (10.3 - 9.5);
                        } else {
                            Result = (BeratValue - 10.3) / (11.2 - 10.3);
                        }
                    } else if (TinggiValue <= 79.0) {
                        if (BeratValue <= 10.4) {
                            Result = (BeratValue - 10.4) / (10.4 - 9.6);
                        } else {
                            Result = (BeratValue - 10.4) / (11.3 - 10.4);
                        }
                    } else if (TinggiValue <= 79.5) {
                        if (BeratValue <= 10.5) {
                            Result = (BeratValue - 10.5) / (10.5 - 9.7);
                        } else {
                            Result = (BeratValue - 10.5) / (11.4 - 10.5);
                        }
                    } else if (TinggiValue <= 80.0) {
                        if (BeratValue <= 10.6) {
                            Result = (BeratValue - 10.6) / (10.6 - 9.7);
                        } else {
                            Result = (BeratValue - 10.6) / (11.5 - 10.6);
                        }
                    } else if (TinggiValue <= 80.5) {
                        if (BeratValue <= 10.7) {
                            Result = (BeratValue - 10.7) / (10.7 - 9.8);
                        } else {
                            Result = (BeratValue - 10.7) / (11.6 - 10.7);
                        }
                    } else if (TinggiValue <= 81.0) {
                        if (BeratValue <= 10.8) {
                            Result = (BeratValue - 10.8) / (10.8 - 9.9);
                        } else {
                            Result = (BeratValue - 10.8) / (11.7 - 10.8);
                        }
                    } else if (TinggiValue <= 81.5) {
                        if (BeratValue <= 10.9) {
                            Result = (BeratValue - 10.9) / (10.9 - 10.0);
                        } else {
                            Result = (BeratValue - 10.9) / (11.8 - 10.9);
                        }
                    } else if (TinggiValue <= 82.0) {
                        if (BeratValue <= 11.0) {
                            Result = (BeratValue - 11.0) / (11.0 - 10.1);
                        } else {
                            Result = (BeratValue - 11.0) / (11.9 - 11.0);
                        }
                    } else if (TinggiValue <= 82.5) {
                        if (BeratValue <= 11.1) {
                            Result = (BeratValue - 11.1) / (11.1 - 10.2);
                        } else {
                            Result = (BeratValue - 11.1) / (12.1 - 11.1);
                        }
                    } else if (TinggiValue <= 83.0) {
                        if (BeratValue <= 11.2) {
                            Result = (BeratValue - 11.2) / (11.2 - 10.3);
                        } else {
                            Result = (BeratValue - 11.2) / (12.2 - 11.2);
                        }
                    } else if (TinggiValue <= 83.5) {
                        if (BeratValue <= 11.3) {
                            Result = (BeratValue - 11.3) / (11.3 - 10.4);
                        } else {
                            Result = (BeratValue - 11.3) / (12.3 - 11.3);
                        }
                    } else if (TinggiValue <= 84.0) {
                        if (BeratValue <= 11.4) {
                            Result = (BeratValue - 11.4) / (11.4 - 10.5);
                        } else {
                            Result = (BeratValue - 11.4) / (12.4 - 11.4);
                        }
                    } else if (TinggiValue <= 84.5) {
                        if (BeratValue <= 11.5) {
                            Result = (BeratValue - 11.5) / (11.5 - 10.7);
                        } else {
                            Result = (BeratValue - 11.5) / (12.5 - 11.5);
                        }
                    } else if (TinggiValue <= 85.0) {
                        if (BeratValue <= 11.7) {
                            Result = (BeratValue - 11.7) / (11.7 - 10.8);
                        } else {
                            Result = (BeratValue - 11.7) / (12.7 - 11.7);
                        }
                    } else if (TinggiValue <= 85.5) {
                        if (BeratValue <= 11.8) {
                            Result = (BeratValue - 11.8) / (11.8 - 10.9);
                        } else {
                            Result = (BeratValue - 11.8) / (12.8 - 11.8);
                        }
                    } else if (TinggiValue <= 86.0) {
                        if (BeratValue <= 11.9) {
                            Result = (BeratValue - 11.9) / (11.9 - 11.0);
                        } else {
                            Result = (BeratValue - 11.9) / (12.9 - 11.9);
                        }
                    } else if (TinggiValue <= 86.5) {
                        if (BeratValue <= 12.0) {
                            Result = (BeratValue - 12.0) / (12.0 - 11.1);
                        } else {
                            Result = (BeratValue - 12.0) / (13.1 - 12.0);
                        }
                    } else if (TinggiValue <= 87.0) {
                        if (BeratValue <= 12.2) {
                            Result = (BeratValue - 12.2) / (12.2 - 11.2);
                        } else {
                            Result = (BeratValue - 12.2) / (13.2 - 12.2);
                        }
                    } else if (TinggiValue <= 87.5) {
                        if (BeratValue <= 12.3) {
                            Result = (BeratValue - 12.3) / (12.3 - 11.3);
                        } else {
                            Result = (BeratValue - 12.3) / (13.3 - 12.3);
                        }
                    } else if (TinggiValue <= 88.0) {
                        if (BeratValue <= 12.4) {
                            Result = (BeratValue - 12.4) / (12.4 - 11.5);
                        } else {
                            Result = (BeratValue - 12.4) / (13.5 - 12.4);
                        }
                    } else if (TinggiValue <= 88.5) {
                        if (BeratValue <= 12.5) {
                            Result = (BeratValue - 12.5) / (12.5 - 11.6);
                        } else {
                            Result = (BeratValue - 12.5) / (13.6 - 12.5);
                        }
                    } else if (TinggiValue <= 89.0) {
                        if (BeratValue <= 12.6) {
                            Result = (BeratValue - 12.6) / (12.6 - 11.7);
                        } else {
                            Result = (BeratValue - 12.6) / (13.7 - 12.6);
                        }
                    } else if (TinggiValue <= 89.5) {
                        if (BeratValue <= 12.8) {
                            Result = (BeratValue - 12.8) / (12.8 - 11.8);
                        } else {
                            Result = (BeratValue - 12.8) / (13.9 - 12.8);
                        }
                    } else if (TinggiValue <= 90.0) {
                        if (BeratValue <= 12.9) {
                            Result = (BeratValue - 12.9) / (12.9 - 11.9);
                        } else {
                            Result = (BeratValue - 12.9) / (14.0 - 12.9);
                        }
                    } else if (TinggiValue <= 90.5) {
                        if (BeratValue <= 13.0) {
                            Result = (BeratValue - 13.0) / (13.0 - 12.0);
                        } else {
                            Result = (BeratValue - 13.0) / (14.1 - 13.0);
                        }
                    } else if (TinggiValue <= 91.0) {
                        if (BeratValue <= 13.1) {
                            Result = (BeratValue - 13.1) / (13.1 - 12.1);
                        } else {
                            Result = (BeratValue - 13.1) / (14.2 - 13.1);
                        }
                    } else if (TinggiValue <= 91.5) {
                        if (BeratValue <= 13.2) {
                            Result = (BeratValue - 13.2) / (13.2 - 12.2);
                        } else {
                            Result = (BeratValue - 13.2) / (14.4 - 13.2);
                        }
                    } else if (TinggiValue <= 92.0) {
                        if (BeratValue <= 13.4) {
                            Result = (BeratValue - 13.4) / (13.4 - 12.3);
                        } else {
                            Result = (BeratValue - 13.4) / (14.5 - 13.4);
                        }
                    } else if (TinggiValue <= 92.5) {
                        if (BeratValue <= 13.5) {
                            Result = (BeratValue - 13.5) / (13.5 - 12.4);
                        } else {
                            Result = (BeratValue - 13.5) / (14.6 - 13.5);
                        }
                    } else if (TinggiValue <= 93.0) {
                        if (BeratValue <= 13.6) {
                            Result = (BeratValue - 13.6) / (13.6 - 12.6);
                        } else {
                            Result = (BeratValue - 13.6) / (14.7 - 13.6);
                        }
                    } else if (TinggiValue <= 93.5) {
                        if (BeratValue <= 13.7) {
                            Result = (BeratValue - 13.7) / (13.7 - 12.7);
                        } else {
                            Result = (BeratValue - 13.7) / (14.9 - 13.7);
                        }
                    } else if (TinggiValue <= 94.0) {
                        if (BeratValue <= 13.8) {
                            Result = (BeratValue - 13.8) / (13.8 - 12.8);
                        } else {
                            Result = (BeratValue - 13.8) / (15.0 - 13.8);
                        }
                    } else if (TinggiValue <= 94.5) {
                        if (BeratValue <= 13.9) {
                            Result = (BeratValue - 13.9) / (13.9 - 12.9);
                        } else {
                            Result = (BeratValue - 13.9) / (15.1 - 13.9);
                        }
                    } else if (TinggiValue <= 95.0) {
                        if (BeratValue <= 14.1) {
                            Result = (BeratValue - 14.1) / (14.1 - 13.0);
                        } else {
                            Result = (BeratValue - 14.1) / (15.3 - 14.1);
                        }
                    } else if (TinggiValue <= 95.5) {
                        if (BeratValue <= 14.2) {
                            Result = (BeratValue - 14.2) / (14.2 - 13.1);
                        } else {
                            Result = (BeratValue - 14.2) / (15.4 - 14.2);
                        }
                    } else if (TinggiValue <= 96.0) {
                        if (BeratValue <= 14.3) {
                            Result = (BeratValue - 14.3) / (14.3 - 13.2);
                        } else {
                            Result = (BeratValue - 14.3) / (15.5 - 14.3);
                        }
                    } else if (TinggiValue <= 96.5) {
                        if (BeratValue <= 14.4) {
                            Result = (BeratValue - 14.4) / (14.4 - 13.3);
                        } else {
                            Result = (BeratValue - 14.4) / (15.7 - 14.4);
                        }
                    } else if (TinggiValue <= 97.0) {
                        if (BeratValue <= 14.6) {
                            Result = (BeratValue - 14.6) / (14.6 - 13.4);
                        } else {
                            Result = (BeratValue - 14.6) / (15.8 - 14.6);
                        }
                    } else if (TinggiValue <= 97.5) {
                        if (BeratValue <= 14.7) {
                            Result = (BeratValue - 14.7) / (14.7 - 13.6);
                        } else {
                            Result = (BeratValue - 14.7) / (15.9 - 14.7);
                        }
                    } else if (TinggiValue <= 98.0) {
                        if (BeratValue <= 14.8) {
                            Result = (BeratValue - 14.8) / (14.8 - 13.7);
                        } else {
                            Result = (BeratValue - 14.8) / (16.1 - 14.8);
                        }
                    } else if (TinggiValue <= 98.5) {
                        if (BeratValue <= 14.9) {
                            Result = (BeratValue - 14.9) / (14.9 - 13.8);
                        } else {
                            Result = (BeratValue - 14.9) / (16.2 - 14.9);
                        }
                    } else if (TinggiValue <= 99.0) {
                        if (BeratValue <= 15.1) {
                            Result = (BeratValue - 15.1) / (15.1 - 13.9);
                        } else {
                            Result = (BeratValue - 15.1) / (16.4 - 15.1);
                        }
                    } else if (TinggiValue <= 99.5) {
                        if (BeratValue <= 15.2) {
                            Result = (BeratValue - 15.2) / (15.2 - 14.0);
                        } else {
                            Result = (BeratValue - 15.2) / (16.5 - 15.2);
                        }
                    } else if (TinggiValue <= 100.0) {
                        if (BeratValue <= 15.4) {
                            Result = (BeratValue - 15.4) / (15.4 - 14.2);
                        } else {
                            Result = (BeratValue - 15.4) / (16.7 - 15.4);
                        }
                    } else if (TinggiValue <= 100.5) {
                        if (BeratValue <= 15.5) {
                            Result = (BeratValue - 15.5) / (15.5 - 14.3);
                        } else {
                            Result = (BeratValue - 15.5) / (16.9 - 15.5);
                        }
                    } else if (TinggiValue <= 101.0) {
                        if (BeratValue <= 15.6) {
                            Result = (BeratValue - 15.6) / (15.6 - 14.4);
                        } else {
                            Result = (BeratValue - 15.6) / (17.0 - 15.6);
                        }
                    } else if (TinggiValue <= 101.5) {
                        if (BeratValue <= 15.8) {
                            Result = (BeratValue - 15.8) / (15.8 - 14.5);
                        } else {
                            Result = (BeratValue - 15.8) / (17.2 - 15.8);
                        }
                    } else if (TinggiValue <= 102.0) {
                        if (BeratValue <= 15.9) {
                            Result = (BeratValue - 15.9) / (15.9 - 14.7);
                        } else {
                            Result = (BeratValue - 15.9) / (17.3 - 15.9);
                        }
                    } else if (TinggiValue <= 102.5) {
                        if (BeratValue <= 16.1) {
                            Result = (BeratValue - 16.1) / (16.1 - 14.8);
                        } else {
                            Result = (BeratValue - 16.1) / (17.5 - 16.1);
                        }
                    } else if (TinggiValue <= 103.0) {
                        if (BeratValue <= 16.2) {
                            Result = (BeratValue - 16.2) / (16.2 - 14.9);
                        } else {
                            Result = (BeratValue - 16.2) / (17.7 - 16.2);
                        }
                    } else if (TinggiValue <= 103.5) {
                        if (BeratValue <= 16.4) {
                            Result = (BeratValue - 16.4) / (16.4 - 15.1);
                        } else {
                            Result = (BeratValue - 16.4) / (17.8 - 16.4);
                        }
                    } else if (TinggiValue <= 104.0) {
                        if (BeratValue <= 16.5) {
                            Result = (BeratValue - 16.5) / (16.5 - 15.2);
                        } else {
                            Result = (BeratValue - 16.5) / (18.0 - 16.5);
                        }
                    } else if (TinggiValue <= 104.5) {
                        if (BeratValue <= 16.7) {
                            Result = (BeratValue - 16.7) / (16.7 - 15.4);
                        } else {
                            Result = (BeratValue - 16.7) / (18.2 - 16.7);
                        }
                    } else if (TinggiValue <= 105.0) {
                        if (BeratValue <= 16.8) {
                            Result = (BeratValue - 16.8) / (16.8 - 15.5);
                        } else {
                            Result = (BeratValue - 16.8) / (18.4 - 16.8);
                        }
                    } else if (TinggiValue <= 105.5) {
                        if (BeratValue <= 17.0) {
                            Result = (BeratValue - 17.0) / (17.0 - 15.6);
                        } else {
                            Result = (BeratValue - 17.0) / (18.5 - 17.0);
                        }
                    } else if (TinggiValue <= 106.0) {
                        if (BeratValue <= 17.2) {
                            Result = (BeratValue - 17.2) / (17.2 - 15.8);
                        } else {
                            Result = (BeratValue - 17.2) / (18.7 - 17.2);
                        }
                    } else if (TinggiValue <= 106.5) {
                        if (BeratValue <= 17.3) {
                            Result = (BeratValue - 17.3) / (17.3 - 15.9);
                        } else {
                            Result = (BeratValue - 17.3) / (18.9 - 17.3);
                        }
                    } else if (TinggiValue <= 107.0) {
                        if (BeratValue <= 17.5) {
                            Result = (BeratValue - 17.5) / (17.5 - 16.1);
                        } else {
                            Result = (BeratValue - 17.5) / (19.1 - 17.5);
                        }
                    } else if (TinggiValue <= 107.5) {
                        if (BeratValue <= 17.7) {
                            Result = (BeratValue - 17.7) / (17.7 - 16.2);
                        } else {
                            Result = (BeratValue - 17.7) / (19.3 - 17.7);
                        }
                    } else if (TinggiValue <= 108.0) {
                        if (BeratValue <= 17.8) {
                            Result = (BeratValue - 17.8) / (17.8 - 16.4);
                        } else {
                            Result = (BeratValue - 17.8) / (19.5 - 17.8);
                        }
                    } else if (TinggiValue <= 108.5) {
                        if (BeratValue <= 18.0) {
                            Result = (BeratValue - 18.0) / (18.0 - 16.5);
                        } else {
                            Result = (BeratValue - 18.0) / (19.7 - 18.0);
                        }
                    } else if (TinggiValue <= 109.0) {
                        if (BeratValue <= 18.2) {
                            Result = (BeratValue - 18.2) / (18.2 - 16.7);
                        } else {
                            Result = (BeratValue - 18.2) / (19.8 - 18.2);
                        }
                    } else if (TinggiValue <= 109.5) {
                        if (BeratValue <= 18.3) {
                            Result = (BeratValue - 18.3) / (18.3 - 16.8);
                        } else {
                            Result = (BeratValue - 18.3) / (20.0 - 18.3);
                        }
                    } else if (TinggiValue <= 110.0) {
                        if (BeratValue <= 18.5) {
                            Result = (BeratValue - 18.5) / (18.5 - 17.0);
                        } else {
                            Result = (BeratValue - 18.5) / (20.2 - 18.5);
                        }
                    } else if (TinggiValue <= 110.5) {
                        if (BeratValue <= 18.7) {
                            Result = (BeratValue - 18.7) / (18.7 - 17.1);
                        } else {
                            Result = (BeratValue - 18.7) / (20.4 - 18.7);
                        }
                    } else if (TinggiValue <= 111.0) {
                        if (BeratValue <= 18.9) {
                            Result = (BeratValue - 18.9) / (18.9 - 17.3);
                        } else {
                            Result = (BeratValue - 18.9) / (20.7 - 18.9);
                        }
                    } else if (TinggiValue <= 111.5) {
                        if (BeratValue <= 19.1) {
                            Result = (BeratValue - 19.1) / (19.1 - 17.5);
                        } else {
                            Result = (BeratValue - 19.1) / (20.9 - 19.1);
                        }
                    } else if (TinggiValue <= 112.0) {
                        if (BeratValue <= 19.2) {
                            Result = (BeratValue - 19.2) / (19.2 - 17.6);
                        } else {
                            Result = (BeratValue - 19.2) / (21.1 - 19.2);
                        }
                    } else if (TinggiValue <= 112.5) {
                        if (BeratValue <= 19.4) {
                            Result = (BeratValue - 19.4) / (19.4 - 17.8);
                        } else {
                            Result = (BeratValue - 19.4) / (21.3 - 19.4);
                        }
                    } else if (TinggiValue <= 113.0) {
                        if (BeratValue <= 19.6) {
                            Result = (BeratValue - 19.6) / (19.6 - 18.0);
                        } else {
                            Result = (BeratValue - 19.6) / (21.5 - 19.6);
                        }
                    } else if (TinggiValue <= 113.5) {
                        if (BeratValue <= 19.8) {
                            Result = (BeratValue - 19.8) / (19.8 - 18.1);
                        } else {
                            Result = (BeratValue - 19.8) / (21.7 - 19.8);
                        }
                    } else if (TinggiValue <= 114.0) {
                        if (BeratValue <= 20.0) {
                            Result = (BeratValue - 20.0) / (20.0 - 18.3);
                        } else {
                            Result = (BeratValue - 20.0) / (21.9 - 20.0);
                        }
                    } else if (TinggiValue <= 114.5) {
                        if (BeratValue <= 20.2) {
                            Result = (BeratValue - 20.2) / (20.2 - 18.5);
                        } else {
                            Result = (BeratValue - 20.2) / (22.1 - 20.2);
                        }
                    } else if (TinggiValue <= 115.0) {
                        if (BeratValue <= 20.4) {
                            Result = (BeratValue - 20.4) / (20.4 - 18.6);
                        } else {
                            Result = (BeratValue - 20.4) / (22.4 - 20.4);
                        }
                    } else if (TinggiValue <= 115.5) {
                        if (BeratValue <= 20.6) {
                            Result = (BeratValue - 20.6) / (20.6 - 18.8);
                        } else {
                            Result = (BeratValue - 20.6) / (22.6 - 20.6);
                        }
                    } else if (TinggiValue <= 116.0) {
                        if (BeratValue <= 20.8) {
                            Result = (BeratValue - 20.8) / (20.8 - 19.0);
                        } else {
                            Result = (BeratValue - 20.8) / (22.8 - 20.8);
                        }
                    } else if (TinggiValue <= 116.5) {
                        if (BeratValue <= 21.0) {
                            Result = (BeratValue - 21.0) / (21.0 - 19.2);
                        } else {
                            Result = (BeratValue - 21.0) / (23.0 - 21.0);
                        }
                    } else if (TinggiValue <= 117.0) {
                        if (BeratValue <= 21.2) {
                            Result = (BeratValue - 21.2) / (21.2 - 19.3);
                        } else {
                            Result = (BeratValue - 21.2) / (23.3 - 21.2);
                        }
                    } else if (TinggiValue <= 117.5) {
                        if (BeratValue <= 21.4) {
                            Result = (BeratValue - 21.4) / (21.4 - 19.5);
                        } else {
                            Result = (BeratValue - 21.4) / (23.5 - 21.4);
                        }
                    } else if (TinggiValue <= 118.0) {
                        if (BeratValue <= 21.6) {
                            Result = (BeratValue - 21.6) / (21.6 - 19.7);
                        } else {
                            Result = (BeratValue - 21.6) / (23.7 - 21.6);
                        }
                    } else if (TinggiValue <= 118.5) {
                        if (BeratValue <= 21.8) {
                            Result = (BeratValue - 21.8) / (21.8 - 19.9);
                        } else {
                            Result = (BeratValue - 21.8) / (23.9 - 21.8);
                        }
                    } else if (TinggiValue <= 119.0) {
                        if (BeratValue <= 22.0) {
                            Result = (BeratValue - 22.0) / (22.0 - 20.0);
                        } else {
                            Result = (BeratValue - 22.0) / (24.1 - 22.0);
                        }
                    } else if (TinggiValue <= 119.5) {
                        if (BeratValue <= 22.2) {
                            Result = (BeratValue - 22.2) / (22.2 - 20.2);
                        } else {
                            Result = (BeratValue - 22.2) / (24.4 - 22.2);
                        }
                    } else if (TinggiValue <= 120.0) {
                        if (BeratValue <= 22.4) {
                            Result = (BeratValue - 22.4) / (22.4 - 20.4);
                        } else {
                            Result = (BeratValue - 22.4) / (24.6 - 22.4);
                        }
                    }
                }
            } else if (Gender.equals("Perempuan")) {
                if (UmurValue <= 24) {
                    if (TinggiValue <= 45.0) {
                        if (BeratValue <= 2.5) {
                            Result = (BeratValue - 2.5) / (2.5 - 2.3);
                        } else {
                            Result = (BeratValue - 2.5) / (2.7 - 2.5);
                        }
                    } else if (TinggiValue <= 45.5) {
                        if (BeratValue <= 2.5) {
                            Result = (BeratValue - 2.5) / (2.5 - 2.3);
                        } else {
                            Result = (BeratValue - 2.5) / (2.8 - 2.5);
                        }
                    } else if (TinggiValue <= 46.0) {
                        if (BeratValue <= 2.6) {
                            Result = (BeratValue - 2.6) / (2.6 - 2.4);
                        } else {
                            Result = (BeratValue - 2.6) / (2.9 - 2.6);
                        }
                    } else if (TinggiValue <= 46.5) {
                        if (BeratValue <= 2.7) {
                            Result = (BeratValue - 2.7) / (2.7 - 2.5);
                        } else {
                            Result = (BeratValue - 2.7) / (3.0 - 2.7);
                        }
                    } else if (TinggiValue <= 47.0) {
                        if (BeratValue <= 2.8) {
                            Result = (BeratValue - 2.8) / (2.8 - 2.6);
                        } else {
                            Result = (BeratValue - 2.8) / (3.1 - 2.8);
                        }
                    } else if (TinggiValue <= 47.5) {
                        if (BeratValue <= 2.9) {
                            Result = (BeratValue - 2.9) / (2.9 - 2.6);
                        } else {
                            Result = (BeratValue - 2.9) / (3.2 - 2.9);
                        }
                    } else if (TinggiValue <= 48.0) {
                        if (BeratValue <= 3.0) {
                            Result = (BeratValue - 3.0) / (3.0 - 2.7);
                        } else {
                            Result = (BeratValue - 3.0) / (3.3 - 3.0);
                        }
                    } else if (TinggiValue <= 48.5) {
                        if (BeratValue <= 3.1) {
                            Result = (BeratValue - 3.1) / (3.1 - 2.8);
                        } else {
                            Result = (BeratValue - 3.1) / (3.4 - 3.1);
                        }
                    } else if (TinggiValue <= 49.0) {
                        if (BeratValue <= 3.2) {
                            Result = (BeratValue - 3.2) / (3.2 - 2.9);
                        } else {
                            Result = (BeratValue - 3.2) / (3.5 - 3.2);
                        }
                    } else if (TinggiValue <= 49.5) {
                        if (BeratValue <= 3.3) {
                            Result = (BeratValue - 3.3) / (3.3 - 3.0);
                        } else {
                            Result = (BeratValue - 3.3) / (3.6 - 3.3);
                        }
                    } else if (TinggiValue <= 50.0) {
                        if (BeratValue <= 3.4) {
                            Result = (BeratValue - 3.4) / (3.4 - 3.1);
                        } else {
                            Result = (BeratValue - 3.4) / (3.7 - 3.4);
                        }
                    } else if (TinggiValue <= 50.5) {
                        if (BeratValue <= 3.5) {
                            Result = (BeratValue - 3.5) / (3.5 - 3.2);
                        } else {
                            Result = (BeratValue - 3.5) / (3.8 - 3.5);
                        }
                    } else if (TinggiValue <= 51.0) {
                        if (BeratValue <= 3.6) {
                            Result = (BeratValue - 3.6) / (3.6 - 3.3);
                        } else {
                            Result = (BeratValue - 3.6) / (3.9 - 3.6);
                        }
                    } else if (TinggiValue <= 51.5) {
                        if (BeratValue <= 3.7) {
                            Result = (BeratValue - 3.7) / (3.7 - 3.4);
                        } else {
                            Result = (BeratValue - 3.7) / (4.0 - 3.7);
                        }
                    } else if (TinggiValue <= 52.0) {
                        if (BeratValue <= 3.8) {
                            Result = (BeratValue - 3.8) / (3.8 - 3.5);
                        } else {
                            Result = (BeratValue - 3.8) / (4.2 - 3.8);
                        }
                    } else if (TinggiValue <= 52.5) {
                        if (BeratValue <= 3.9) {
                            Result = (BeratValue - 3.9) / (3.9 - 3.6);
                        } else {
                            Result = (BeratValue - 3.9) / (4.3 - 3.9);
                        }
                    } else if (TinggiValue <= 53.0) {
                        if (BeratValue <= 4.0) {
                            Result = (BeratValue - 4.0) / (4.0 - 3.7);
                        } else {
                            Result = (BeratValue - 4.0) / (4.4 - 4.0);
                        }
                    } else if (TinggiValue <= 53.5) {
                        if (BeratValue <= 4.2) {
                            Result = (BeratValue - 4.2) / (4.2 - 3.8);
                        } else {
                            Result = (BeratValue - 4.2) / (4.6 - 4.2);
                        }
                    } else if (TinggiValue <= 54.0) {
                        if (BeratValue <= 4.3) {
                            Result = (BeratValue - 4.3) / (4.3 - 3.9);
                        } else {
                            Result = (BeratValue - 4.3) / (4.7 - 4.3);
                        }
                    } else if (TinggiValue <= 54.5) {
                        if (BeratValue <= 4.4) {
                            Result = (BeratValue - 4.4) / (4.4 - 4.0);
                        } else {
                            Result = (BeratValue - 4.4) / (4.8 - 4.4);
                        }
                    } else if (TinggiValue <= 55.0) {
                        if (BeratValue <= 4.5) {
                            Result = (BeratValue - 4.5) / (4.5 - 4.2);
                        } else {
                            Result = (BeratValue - 4.5) / (5.0 - 4.5);
                        }
                    } else if (TinggiValue <= 55.5) {
                        if (BeratValue <= 4.7) {
                            Result = (BeratValue - 4.7) / (4.7 - 4.3);
                        } else {
                            Result = (BeratValue - 4.7) / (5.1 - 4.7);
                        }
                    } else if (TinggiValue <= 56.0) {
                        if (BeratValue <= 4.8) {
                            Result = (BeratValue - 4.8) / (4.8 - 4.4);
                        } else {
                            Result = (BeratValue - 4.8) / (5.3 - 4.8);
                        }
                    } else if (TinggiValue <= 56.5) {
                        if (BeratValue <= 5.0) {
                            Result = (BeratValue - 5.0) / (5.0 - 4.5);
                        } else {
                            Result = (BeratValue - 5.0) / (5.4 - 5.0);
                        }
                    } else if (TinggiValue <= 57.0) {
                        if (BeratValue <= 5.1) {
                            Result = (BeratValue - 5.1) / (5.1 - 4.6);
                        } else {
                            Result = (BeratValue - 5.1) / (5.6 - 5.1);
                        }
                    } else if (TinggiValue <= 57.5) {
                        if (BeratValue <= 5.2) {
                            Result = (BeratValue - 5.2) / (5.2 - 4.8);
                        } else {
                            Result = (BeratValue - 5.2) / (5.7 - 5.2);
                        }
                    } else if (TinggiValue <= 58.0) {
                        if (BeratValue <= 5.4) {
                            Result = (BeratValue - 5.4) / (5.4 - 4.9);
                        } else {
                            Result = (BeratValue - 5.4) / (5.9 - 5.4);
                        }
                    } else if (TinggiValue <= 58.5) {
                        if (BeratValue <= 5.5) {
                            Result = (BeratValue - 5.5) / (5.5 - 5.0);
                        } else {
                            Result = (BeratValue - 5.5) / (6.0 - 5.5);
                        }
                    } else if (TinggiValue <= 59.0) {
                        if (BeratValue <= 5.6) {
                            Result = (BeratValue - 5.6) / (5.6 - 5.1);
                        } else {
                            Result = (BeratValue - 5.6) / (6.2 - 5.6);
                        }
                    } else if (TinggiValue <= 59.5) {
                        if (BeratValue <= 5.7) {
                            Result = (BeratValue - 5.7) / (5.7 - 5.3);
                        } else {
                            Result = (BeratValue - 5.7) / (6.3 - 5.7);
                        }
                    } else if (TinggiValue <= 60.0) {
                        if (BeratValue <= 5.9) {
                            Result = (BeratValue - 5.9) / (5.9 - 5.4);
                        } else {
                            Result = (BeratValue - 5.9) / (6.4 - 5.9);
                        }
                    } else if (TinggiValue <= 60.5) {
                        if (BeratValue <= 6.0) {
                            Result = (BeratValue - 6.0) / (6.0 - 5.5);
                        } else {
                            Result = (BeratValue - 6.0) / (6.6 - 6.0);
                        }
                    } else if (TinggiValue <= 61.0) {
                        if (BeratValue <= 6.1) {
                            Result = (BeratValue - 6.1) / (6.1 - 5.6);
                        } else {
                            Result = (BeratValue - 6.1) / (6.7 - 6.1);
                        }
                    } else if (TinggiValue <= 61.5) {
                        if (BeratValue <= 6.3) {
                            Result = (BeratValue - 6.3) / (6.3 - 5.7);
                        } else {
                            Result = (BeratValue - 6.3) / (6.9 - 6.3);
                        }
                    } else if (TinggiValue <= 62.0) {
                        if (BeratValue <= 6.4) {
                            Result = (BeratValue - 6.4) / (6.4 - 5.8);
                        } else {
                            Result = (BeratValue - 6.4) / (7.0 - 6.4);
                        }
                    } else if (TinggiValue <= 62.5) {
                        if (BeratValue <= 6.5) {
                            Result = (BeratValue - 6.5) / (6.5 - 5.9);
                        } else {
                            Result = (BeratValue - 6.5) / (7.1 - 6.5);
                        }
                    } else if (TinggiValue <= 63.0) {
                        if (BeratValue <= 6.6) {
                            Result = (BeratValue - 6.6) / (6.6 - 6.0);
                        } else {
                            Result = (BeratValue - 6.6) / (7.3 - 6.6);
                        }
                    } else if (TinggiValue <= 63.5) {
                        if (BeratValue <= 6.7) {
                            Result = (BeratValue - 6.7) / (6.7 - 6.2);
                        } else {
                            Result = (BeratValue - 6.7) / (7.4 - 6.7);
                        }
                    } else if (TinggiValue <= 64.0) {
                        if (BeratValue <= 6.9) {
                            Result = (BeratValue - 6.9) / (6.9 - 6.3);
                        } else {
                            Result = (BeratValue - 6.9) / (7.5 - 6.9);
                        }
                    } else if (TinggiValue <= 64.5) {
                        if (BeratValue <= 7.0) {
                            Result = (BeratValue - 7.0) / (7.0 - 6.4);
                        } else {
                            Result = (BeratValue - 7.0) / (7.6 - 7.0);
                        }
                    } else if (TinggiValue <= 65.0) {
                        if (BeratValue <= 7.1) {
                            Result = (BeratValue - 7.1) / (7.1 - 6.5);
                        } else {
                            Result = (BeratValue - 7.1) / (7.8 - 7.1);
                        }
                    } else if (TinggiValue <= 65.5) {
                        if (BeratValue <= 7.2) {
                            Result = (BeratValue - 7.2) / (7.2 - 6.6);
                        } else {
                            Result = (BeratValue - 7.2) / (7.9 - 7.2);
                        }
                    } else if (TinggiValue <= 66.0) {
                        if (BeratValue <= 7.3) {
                            Result = (BeratValue - 7.3) / (7.3 - 6.7);
                        } else {
                            Result = (BeratValue - 7.3) / (8.0 - 7.3);
                        }
                    } else if (TinggiValue <= 66.5) {
                        if (BeratValue <= 7.4) {
                            Result = (BeratValue - 7.4) / (7.4 - 6.8);
                        } else {
                            Result = (BeratValue - 7.4) / (8.1 - 7.4);
                        }
                    } else if (TinggiValue <= 67.0) {
                        if (BeratValue <= 7.5) {
                            Result = (BeratValue - 7.5) / (7.5 - 6.9);
                        } else {
                            Result = (BeratValue - 7.5) / (8.3 - 7.5);
                        }
                    } else if (TinggiValue <= 67.5) {
                        if (BeratValue <= 7.6) {
                            Result = (BeratValue - 7.6) / (7.6 - 7.0);
                        } else {
                            Result = (BeratValue - 7.6) / (8.4 - 7.6);
                        }
                    } else if (TinggiValue <= 68.0) {
                        if (BeratValue <= 7.7) {
                            Result = (BeratValue - 7.7) / (7.7 - 7.1);
                        } else {
                            Result = (BeratValue - 7.7) / (8.5 - 7.7);
                        }
                    } else if (TinggiValue <= 68.5) {
                        if (BeratValue <= 7.9) {
                            Result = (BeratValue - 7.9) / (7.9 - 7.2);
                        } else {
                            Result = (BeratValue - 7.9) / (8.6 - 7.9);
                        }
                    } else if (TinggiValue <= 69.0) {
                        if (BeratValue <= 8.0) {
                            Result = (BeratValue - 8.0) / (8.0 - 7.3);
                        } else {
                            Result = (BeratValue - 8.0) / (8.7 - 8.0);
                        }
                    } else if (TinggiValue <= 69.5) {
                        if (BeratValue <= 8.1) {
                            Result = (BeratValue - 8.1) / (8.1 - 7.4);
                        } else {
                            Result = (BeratValue - 8.1) / (8.8 - 8.1);
                        }
                    } else if (TinggiValue <= 70.0) {
                        if (BeratValue <= 8.2) {
                            Result = (BeratValue - 8.2) / (8.2 - 7.5);
                        } else {
                            Result = (BeratValue - 8.2) / (9.0 - 8.2);
                        }
                    } else if (TinggiValue <= 70.5) {
                        if (BeratValue <= 8.3) {
                            Result = (BeratValue - 8.3) / (8.3 - 7.6);
                        } else {
                            Result = (BeratValue - 8.3) / (9.1 - 8.3);
                        }
                    } else if (TinggiValue <= 71.0) {
                        if (BeratValue <= 8.4) {
                            Result = (BeratValue - 8.4) / (8. - 7.7);
                        } else {
                            Result = (BeratValue - 8.4) / (9.2 - 8.4);
                        }
                    } else if (TinggiValue <= 71.5) {
                        if (BeratValue <= 8.5) {
                            Result = (BeratValue - 8.5) / (8.5 - 7.7);
                        } else {
                            Result = (BeratValue - 8.5) / (9.3 - 8.5);
                        }
                    } else if (TinggiValue <= 72.0) {
                        if (BeratValue <= 8.6) {
                            Result = (BeratValue - 8.6) / (8.6 - 7.8);
                        } else {
                            Result = (BeratValue - 8.6) / (9.4 - 8.6);
                        }
                    } else if (TinggiValue <= 72.5) {
                        if (BeratValue <= 8.7) {
                            Result = (BeratValue - 8.7) / (8.7 - 7.9);
                        } else {
                            Result = (BeratValue - 8.7) / (9.5 - 8.7);
                        }
                    } else if (TinggiValue <= 73.0) {
                        if (BeratValue <= 8.8) {
                            Result = (BeratValue - 8.8) / (8.8 - 8.0);
                        } else {
                            Result = (BeratValue - 8.8) / (9.6 - 8.8);
                        }
                    } else if (TinggiValue <= 73.5) {
                        if (BeratValue <= 8.9) {
                            Result = (BeratValue - 8.9) / (8.9 - 8.1);
                        } else {
                            Result = (BeratValue - 8.9) / (9.7 - 8.9);
                        }
                    } else if (TinggiValue <= 74.0) {
                        if (BeratValue <= 9.0) {
                            Result = (BeratValue - 9.0) / (9.0 - 8.2);
                        } else {
                            Result = (BeratValue - 9.0) / (9.8 - 9.0);
                        }
                    } else if (TinggiValue <= 74.5) {
                        if (BeratValue <= 9.1) {
                            Result = (BeratValue - 9.1) / (9.1 - 8.3);
                        } else {
                            Result = (BeratValue - 9.1) / (9.9 - 9.1);
                        }
                    } else if (TinggiValue <= 75.0) {
                        if (BeratValue <= 9.1) {
                            Result = (BeratValue - 9.1) / (9.1 - 8.4);
                        } else {
                            Result = (BeratValue - 9.1) / (10.0 - 9.1);
                        }
                    } else if (TinggiValue <= 75.5) {
                        if (BeratValue <= 9.2) {
                            Result = (BeratValue - 9.2) / (9.2 - 8.5);
                        } else {
                            Result = (BeratValue - 9.2) / (10.1 - 9.2);
                        }
                    } else if (TinggiValue <= 76.0) {
                        if (BeratValue <= 9.3) {
                            Result = (BeratValue - 9.3) / (9.3 - 8.5);
                        } else {
                            Result = (BeratValue - 9.3) / (10.2 - 9.3);
                        }
                    } else if (TinggiValue <= 76.5) {
                        if (BeratValue <= 9.4) {
                            Result = (BeratValue - 9.4) / (9.4 - 8.6);
                        } else {
                            Result = (BeratValue - 9.4) / (10.3 - 9.4);
                        }
                    } else if (TinggiValue <= 77.0) {
                        if (BeratValue <= 9.5) {
                            Result = (BeratValue - 9.5) / (9.5 - 8.7);
                        } else {
                            Result = (BeratValue - 9.5) / (10.4 - 9.5);
                        }
                    } else if (TinggiValue <= 77.5) {
                        if (BeratValue <= 9.6) {
                            Result = (BeratValue - 9.6) / (9.6 - 8.8);
                        } else {
                            Result = (BeratValue - 9.6) / (10.5 - 9.6);
                        }
                    } else if (TinggiValue <= 78.0) {
                        if (BeratValue <= 9.7) {
                            Result = (BeratValue - 9.7) / (9.7 - 8.9);
                        } else {
                            Result = (BeratValue - 9.7) / (10.6 - 9.7);
                        }
                    } else if (TinggiValue <= 78.5) {
                        if (BeratValue <= 9.8) {
                            Result = (BeratValue - 9.8) / (9.8 - 9.0);
                        } else {
                            Result = (BeratValue - 9.8) / (10.7 - 9.8);
                        }
                    } else if (TinggiValue <= 79.0) {
                        if (BeratValue <= 9.9) {
                            Result = (BeratValue - 9.9) / (9.9 - 9.1);
                        } else {
                            Result = (BeratValue - 9.9) / (10.8 - 9.9);
                        }
                    } else if (TinggiValue <= 79.5) {
                        if (BeratValue <= 10.0) {
                            Result = (BeratValue - 10.0) / (10.0 - 9.1);
                        } else {
                            Result = (BeratValue - 10.0) / (10.9 - 10.0);
                        }
                    } else if (TinggiValue <= 80.0) {
                        if (BeratValue <= 10.1) {
                            Result = (BeratValue - 10.1) / (10.1 - 9.2);
                        } else {
                            Result = (BeratValue - 10.1) / (11.0 - 10.1);
                        }
                    } else if (TinggiValue <= 80.5) {
                        if (BeratValue <= 10.2) {
                            Result = (BeratValue - 10.2) / (10.2 - 9.3);
                        } else {
                            Result = (BeratValue - 10.2) / (11.2 - 10.2);
                        }
                    } else if (TinggiValue <= 81.0) {
                        if (BeratValue <= 10.3) {
                            Result = (BeratValue - 10.3) / (10.3 - 9.4);
                        } else {
                            Result = (BeratValue - 10.3) / (11.3 - 10.3);
                        }
                    } else if (TinggiValue <= 81.5) {
                        if (BeratValue <= 10.4) {
                            Result = (BeratValue - 10.4) / (10.4 - 9.5);
                        } else {
                            Result = (BeratValue - 10.4) / (11.4 - 10.4);
                        }
                    } else if (TinggiValue <= 82.0) {
                        if (BeratValue <= 10.5) {
                            Result = (BeratValue - 10.5) / (10.5 - 9.6);
                        } else {
                            Result = (BeratValue - 10.5) / (11.5 - 10.5);
                        }
                    } else if (TinggiValue <= 82.5) {
                        if (BeratValue <= 10.6) {
                            Result = (BeratValue - 10.6) / (10.6 - 9.7);
                        } else {
                            Result = (BeratValue - 10.6) / (11.6 - 10.6);
                        }
                    } else if (TinggiValue <= 83.0) {
                        if (BeratValue <= 10.7) {
                            Result = (BeratValue - 10.7) / (10.7 - 9.8);
                        } else {
                            Result = (BeratValue - 10.7) / (11.8 - 10.7);
                        }
                    } else if (TinggiValue <= 83.5) {
                        if (BeratValue <= 10.9) {
                            Result = (BeratValue - 10.9) / (10.9 - 9.9);
                        } else {
                            Result = (BeratValue - 10.9) / (11.9 - 10.9);
                        }
                    } else if (TinggiValue <= 84.0) {
                        if (BeratValue <= 11.0) {
                            Result = (BeratValue - 11.0) / (11.0 - 10.1);
                        } else {
                            Result = (BeratValue - 11.0) / (12.0 - 11.0);
                        }
                    } else if (TinggiValue <= 84.5) {
                        if (BeratValue <= 11.1) {
                            Result = (BeratValue - 11.1) / (11.1 - 10.2);
                        } else {
                            Result = (BeratValue - 11.1) / (12.1 - 11.1);
                        }
                    } else if (TinggiValue <= 85.0) {
                        if (BeratValue <= 11.2) {
                            Result = (BeratValue - 11.2) / (11.2 - 10.3);
                        } else {
                            Result = (BeratValue - 11.2) / (12.3 - 11.2);
                        }
                    } else if (TinggiValue <= 85.5) {
                        if (BeratValue <= 11.3) {
                            Result = (BeratValue - 11.3) / (11.3 - 10.4);
                        } else {
                            Result = (BeratValue - 11.3) / (12.4 - 11.3);
                        }
                    } else if (TinggiValue <= 86.0) {
                        if (BeratValue <= 11.5) {
                            Result = (BeratValue - 11.5) / (11.5 - 10.5);
                        } else {
                            Result = (BeratValue - 11.5) / (12.6 - 11.5);
                        }
                    } else if (TinggiValue <= 86.5) {
                        if (BeratValue <= 11.6) {
                            Result = (BeratValue - 11.6) / (11.6 - 10.6);
                        } else {
                            Result = (BeratValue - 11.6) / (12.7 - 11.6);
                        }
                    } else if (TinggiValue <= 87.0) {
                        if (BeratValue <= 11.7) {
                            Result = (BeratValue - 11.7) / (11.7 - 10.7);
                        } else {
                            Result = (BeratValue - 11.7) / (12.8 - 11.7);
                        }
                    } else if (TinggiValue <= 87.5) {
                        if (BeratValue <= 11.8) {
                            Result = (BeratValue - 11.8) / (11.8 - 10.9);
                        } else {
                            Result = (BeratValue - 11.8) / (13.0 - 11.8);
                        }
                    } else if (TinggiValue <= 88.0) {
                        if (BeratValue <= 12.0) {
                            Result = (BeratValue - 12.0) / (12.0 - 11.0);
                        } else {
                            Result = (BeratValue - 12.0) / (13.1 - 12.0);
                        }
                    } else if (TinggiValue <= 88.5) {
                        if (BeratValue <= 12.1) {
                            Result = (BeratValue - 12.1) / (12.1 - 11.1);
                        } else {
                            Result = (BeratValue - 12.1) / (13.2 - 12.1);
                        }
                    } else if (TinggiValue <= 89.0) {
                        if (BeratValue <= 12.2) {
                            Result = (BeratValue - 12.2) / (12.2 - 11.2);
                        } else {
                            Result = (BeratValue - 12.2) / (13.4 - 12.2);
                        }
                    } else if (TinggiValue <= 89.5) {
                        if (BeratValue <= 12.3) {
                            Result = (BeratValue - 12.3) / (12.3 - 11.3);
                        } else {
                            Result = (BeratValue - 12.3) / (13.5 - 12.3);
                        }
                    } else if (TinggiValue <= 90.0) {
                        if (BeratValue <= 12.5) {
                            Result = (BeratValue - 12.5) / (12.5 - 11.4);
                        } else {
                            Result = (BeratValue - 12.5) / (13.7 - 12.5);
                        }
                    } else if (TinggiValue <= 90.5) {
                        if (BeratValue <= 12.6) {
                            Result = (BeratValue - 12.6) / (12.6 - 11.5);
                        } else {
                            Result = (BeratValue - 12.6) / (13.8 - 12.6);
                        }
                    } else if (TinggiValue <= 91.0) {
                        if (BeratValue <= 12.7) {
                            Result = (BeratValue - 12.7) / (12.7 - 11.7);
                        } else {
                            Result = (BeratValue - 12.7) / (13.9 - 12.7);
                        }
                    } else if (TinggiValue <= 91.5) {
                        if (BeratValue <= 12.8) {
                            Result = (BeratValue - 12.8) / (12.8 - 11.8);
                        } else {
                            Result = (BeratValue - 12.8) / (14.1 - 12.8);
                        }
                    } else if (TinggiValue <= 92.0) {
                        if (BeratValue <= 13.0) {
                            Result = (BeratValue - 13.0) / (13.0 - 11.9);
                        } else {
                            Result = (BeratValue - 13.0) / (14.2 - 13.0);
                        }
                    } else if (TinggiValue <= 92.5) {
                        if (BeratValue <= 13.1) {
                            Result = (BeratValue - 13.1) / (13.1 - 12.0);
                        } else {
                            Result = (BeratValue - 13.1) / (14.3 - 13.1);
                        }
                    } else if (TinggiValue <= 93.0) {
                        if (BeratValue <= 13.2) {
                            Result = (BeratValue - 13.2) / (13.2 - 12.1);
                        } else {
                            Result = (BeratValue - 13.2) / (14.5 - 13.2);
                        }
                    } else if (TinggiValue <= 93.5) {
                        if (BeratValue <= 13.3) {
                            Result = (BeratValue - 13.3) / (13.3 - 12.2);
                        } else {
                            Result = (BeratValue - 13.3) / (14.6 - 13.3);
                        }
                    } else if (TinggiValue <= 94.0) {
                        if (BeratValue <= 13.5) {
                            Result = (BeratValue - 13.5) / (13.5 - 12.3);
                        } else {
                            Result = (BeratValue - 13.5) / (14.7 - 13.5);
                        }
                    } else if (TinggiValue <= 94.5) {
                        if (BeratValue <= 13.6) {
                            Result = (BeratValue - 13.6) / (13.6 - 12.4);
                        } else {
                            Result = (BeratValue - 13.6) / (14.9 - 13.6);
                        }
                    } else if (TinggiValue <= 95.0) {
                        if (BeratValue <= 13.7) {
                            Result = (BeratValue - 13.7) / (13.7 - 12.6);
                        } else {
                            Result = (BeratValue - 13.7) / (15.0 - 13.7);
                        }
                    } else if (TinggiValue <= 95.5) {
                        if (BeratValue <= 13.8) {
                            Result = (BeratValue - 13.8) / (13.8 - 12.7);
                        } else {
                            Result = (BeratValue - 13.8) / (15.2 - 13.8);
                        }
                    } else if (TinggiValue <= 96.0) {
                        if (BeratValue <= 14.0) {
                            Result = (BeratValue - 14.0) / (14.0 - 12.8);
                        } else {
                            Result = (BeratValue - 14.0) / (15.3 - 14.0);
                        }
                    } else if (TinggiValue <= 96.5) {
                        if (BeratValue <= 14.1) {
                            Result = (BeratValue - 14.1) / (14.1 - 12.9);
                        } else {
                            Result = (BeratValue - 14.1) / (15.4 - 14.1);
                        }
                    } else if (TinggiValue <= 97.0) {
                        if (BeratValue <= 14.2) {
                            Result = (BeratValue - 14.2) / (14.2 - 13.0);
                        } else {
                            Result = (BeratValue - 14.2) / (15.6 - 14.2);
                        }
                    } else if (TinggiValue <= 97.5) {
                        if (BeratValue <= 14.4) {
                            Result = (BeratValue - 14.4) / (14.4 - 13.1);
                        } else {
                            Result = (BeratValue - 14.4) / (15.7 - 14.4);
                        }
                    } else if (TinggiValue <= 98.0) {
                        if (BeratValue <= 14.5) {
                            Result = (BeratValue - 14.5) / (14.5 - 13.3);
                        } else {
                            Result = (BeratValue - 14.5) / (15.9 - 14.5);
                        }
                    } else if (TinggiValue <= 98.5) {
                        if (BeratValue <= 14.6) {
                            Result = (BeratValue - 14.6) / (14.6 - 13.4);
                        } else {
                            Result = (BeratValue - 14.6) / (16.0 - 14.6);
                        }
                    } else if (TinggiValue <= 99.0) {
                        if (BeratValue <= 14.8) {
                            Result = (BeratValue - 14.8) / (14.8 - 13.5);
                        } else {
                            Result = (BeratValue - 14.8) / (16.2 - 14.8);
                        }
                    } else if (TinggiValue <= 99.5) {
                        if (BeratValue <= 14.9) {
                            Result = (BeratValue - 14.9) / (14.9 - 13.6);
                        } else {
                            Result = (BeratValue - 14.9) / (16.3 - 14.9);
                        }
                    } else if (TinggiValue <= 100.0) {
                        if (BeratValue <= 15.0) {
                            Result = (BeratValue - 15.0) / (15.0 - 13.7);
                        } else {
                            Result = (BeratValue - 15.0) / (16.5 - 15.0);
                        }
                    } else if (TinggiValue <= 100.5) {
                        if (BeratValue <= 15.2) {
                            Result = (BeratValue - 15.2) / (15.2 - 13.9);
                        } else {
                            Result = (BeratValue - 15.2) / (16.6 - 15.2);
                        }
                    } else if (TinggiValue <= 101.0) {
                        if (BeratValue <= 15.3) {
                            Result = (BeratValue - 15.3) / (15.3 - 14.0);
                        } else {
                            Result = (BeratValue - 15.3) / (16.8 - 15.3);
                        }
                    } else if (TinggiValue <= 101.5) {
                        if (BeratValue <= 15.5) {
                            Result = (BeratValue - 15.5) / (15.5 - 14.1);
                        } else {
                            Result = (BeratValue - 15.5) / (17.0 - 15.5);
                        }
                    } else if (TinggiValue <= 102.0) {
                        if (BeratValue <= 15.6) {
                            Result = (BeratValue - 15.6) / (15.6 - 14.3);
                        } else {
                            Result = (BeratValue - 15.6) / (17.1 - 15.6);
                        }
                    } else if (TinggiValue <= 102.5) {
                        if (BeratValue <= 15.8) {
                            Result = (BeratValue - 15.8) / (15.8 - 14.4);
                        } else {
                            Result = (BeratValue - 15.8) / (17.3 - 15.8);
                        }
                    } else if (TinggiValue <= 103.0) {
                        if (BeratValue <= 15.9) {
                            Result = (BeratValue - 15.9) / (15.9 - 14.5);
                        } else {
                            Result = (BeratValue - 15.9) / (17.5 - 15.9);
                        }
                    } else if (TinggiValue <= 103.5) {
                        if (BeratValue <= 16.1) {
                            Result = (BeratValue - 16.1) / (16.1 - 14.7);
                        } else {
                            Result = (BeratValue - 16.1) / (17.6 - 16.1);
                        }
                    } else if (TinggiValue <= 104.0) {
                        if (BeratValue <= 16.2) {
                            Result = (BeratValue - 16.2) / (16.2 - 14.8);
                        } else {
                            Result = (BeratValue - 16.2) / (17.8 - 16.2);
                        }
                    } else if (TinggiValue <= 104.5) {
                        if (BeratValue <= 16.4) {
                            Result = (BeratValue - 16.4) / (16.4 - 15.0);
                        } else {
                            Result = (BeratValue - 16.4) / (18.0 - 16.4);
                        }
                    } else if (TinggiValue <= 105.0) {
                        if (BeratValue <= 16.5) {
                            Result = (BeratValue - 16.5) / (16.5 - 15.1);
                        } else {
                            Result = (BeratValue - 16.5) / (18.2 - 16.5);
                        }
                    } else if (TinggiValue <= 105.5) {
                        if (BeratValue <= 16.7) {
                            Result = (BeratValue - 16.7) / (16.7 - 15.3);
                        } else {
                            Result = (BeratValue - 16.7) / (18.4 - 16.7);
                        }
                    } else if (TinggiValue <= 106.0) {
                        if (BeratValue <= 16.9) {
                            Result = (BeratValue - 16.9) / (16.9 - 15.4);
                        } else {
                            Result = (BeratValue - 16.9) / (18.5 - 16.9);
                        }
                    } else if (TinggiValue <= 106.5) {
                        if (BeratValue <= 17.1) {
                            Result = (BeratValue - 17.1) / (17.1 - 15.6);
                        } else {
                            Result = (BeratValue - 17.1) / (18.7 - 17.1);
                        }
                    } else if (TinggiValue <= 107.0) {
                        if (BeratValue <= 17.2) {
                            Result = (BeratValue - 17.2) / (17.2 - 15.7);
                        } else {
                            Result = (BeratValue - 17.2) / (18.9 - 17.2);
                        }
                    } else if (TinggiValue <= 107.5) {
                        if (BeratValue <= 17.4) {
                            Result = (BeratValue - 17.4) / (17.4 - 15.9);
                        } else {
                            Result = (BeratValue - 17.4) / (19.1 - 17.4);
                        }
                    } else if (TinggiValue <= 108.0) {
                        if (BeratValue <= 17.6) {
                            Result = (BeratValue - 17.6) / (17.6 - 16.0);
                        } else {
                            Result = (BeratValue - 17.6) / (19.3 - 17.6);
                        }
                    } else if (TinggiValue <= 108.5) {
                        if (BeratValue <= 17.8) {
                            Result = (BeratValue - 17.8) / (17.8 - 16.2);
                        } else {
                            Result = (BeratValue - 17.8) / (19.5 - 17.8);
                        }
                    } else if (TinggiValue <= 109.0) {
                        if (BeratValue <= 18.0) {
                            Result = (BeratValue - 18.0) / (18.0 - 16.4);
                        } else {
                            Result = (BeratValue - 18.0) / (19.7 - 18.0);
                        }
                    } else if (TinggiValue <= 109.5) {
                        if (BeratValue <= 18.1) {
                            Result = (BeratValue - 18.1) / (18.1 - 16.5);
                        } else {
                            Result = (BeratValue - 18.1) / (20.0 - 18.1);
                        }
                    } else if (TinggiValue <= 110.0) {
                        if (BeratValue <= 18.3) {
                            Result = (BeratValue - 18.3) / (18.3 - 16.7);
                        } else {
                            Result = (BeratValue - 18.3) / (20.2 - 18.3);
                        }
                    }
                } else if (UmurValue <= 60) {
                    if (TinggiValue <= 65.0) {
                        if (BeratValue <= 7.2) {
                            Result = (BeratValue - 7.2) / (7.2 - 6.6);
                        } else {
                            Result = (BeratValue - 7.2) / (7.9 - 7.2);
                        }
                    } else if (TinggiValue <= 65.5) {
                        if (BeratValue <= 7.4) {
                            Result = (BeratValue - 7.4) / (7.4 - 6.7);
                        } else {
                            Result = (BeratValue - 7.4) / (8.1 - 7.4);
                        }
                    } else if (TinggiValue <= 66.0) {
                        if (BeratValue <= 7.5) {
                            Result = (BeratValue - 7.5) / (7.5 - 6.8);
                        } else {
                            Result = (BeratValue - 7.5) / (8.2 - 7.5);
                        }
                    } else if (TinggiValue <= 66.5) {
                        if (BeratValue <= 7.6) {
                            Result = (BeratValue - 7.6) / (7.6 - 6.9);
                        } else {
                            Result = (BeratValue - 7.6) / (8.3 - 7.6);
                        }
                    } else if (TinggiValue <= 67.0) {
                        if (BeratValue <= 7.7) {
                            Result = (BeratValue - 7.7) / (7.7 - 7.0);
                        } else {
                            Result = (BeratValue - 7.7) / (8.4 - 7.7);
                        }
                    } else if (TinggiValue <= 67.5) {
                        if (BeratValue <= 7.8) {
                            Result = (BeratValue - 7.8) / (7.8 - 7.1);
                        } else {
                            Result = (BeratValue - 7.8) / (8.5 - 7.8);
                        }
                    } else if (TinggiValue <= 68.0) {
                        if (BeratValue <= 7.9) {
                            Result = (BeratValue - 7.9) / (7.9 - 7.2);
                        } else {
                            Result = (BeratValue - 7.9) / (8.7 - 7.9);
                        }
                    } else if (TinggiValue <= 68.5) {
                        if (BeratValue <= 8.0) {
                            Result = (BeratValue - 8.0) / (8.0 - 7.3);
                        } else {
                            Result = (BeratValue - 8.0) / (8.8 - 8.0);
                        }
                    } else if (TinggiValue <= 69.0) {
                        if (BeratValue <= 8.1) {
                            Result = (BeratValue - 8.1) / (8.1 - 7.4);
                        } else {
                            Result = (BeratValue - 8.1) / (8.9 - 8.1);
                        }
                    } else if (TinggiValue <= 69.5) {
                        if (BeratValue <= 8.2) {
                            Result = (BeratValue - 8.2) / (8.2 - 7.5);
                        } else {
                            Result = (BeratValue - 8.2) / (9.0 - 8.2);
                        }
                    } else if (TinggiValue <= 70.0) {
                        if (BeratValue <= 8.3) {
                            Result = (BeratValue - 8.3) / (8.3 - 7.6);
                        } else {
                            Result = (BeratValue - 8.3) / (9.1 - 8.3);
                        }
                    } else if (TinggiValue <= 70.5) {
                        if (BeratValue <= 8.4) {
                            Result = (BeratValue - 8.4) / (8.4 - 7.7);
                        } else {
                            Result = (BeratValue - 8.4) / (9.2 - 8.4);
                        }
                    } else if (TinggiValue <= 71.0) {
                        if (BeratValue <= 8.5) {
                            Result = (BeratValue - 8.5) / (8.5 - 7.8);
                        } else {
                            Result = (BeratValue - 8.5) / (9.3 - 8.5);
                        }
                    } else if (TinggiValue <= 71.5) {
                        if (BeratValue <= 8.6) {
                            Result = (BeratValue - 8.6) / (8.6 - 7.9);
                        } else {
                            Result = (BeratValue - 8.6) / (9.4 - 8.6);
                        }
                    } else if (TinggiValue <= 72.0) {
                        if (BeratValue <= 8.7) {
                            Result = (BeratValue - 8.7) / (8.7 - 8.0);
                        } else {
                            Result = (BeratValue - 8.7) / (9.5 - 8.7);
                        }
                    } else if (TinggiValue <= 72.5) {
                        if (BeratValue <= 8.8) {
                            Result = (BeratValue - 8.8) / (8.8 - 8.1);
                        } else {
                            Result = (BeratValue - 8.8) / (9.7 - 8.8);
                        }
                    } else if (TinggiValue <= 73.0) {
                        if (BeratValue <= 8.9) {
                            Result = (BeratValue - 8.9) / (8.9 - 8.1);
                        } else {
                            Result = (BeratValue - 8.9) / (9.8 - 8.9);
                        }
                    } else if (TinggiValue <= 73.5) {
                        if (BeratValue <= 9.0) {
                            Result = (BeratValue - 9.0) / (9.0 - 8.2);
                        } else {
                            Result = (BeratValue - 9.0) / (9.9 - 9.0);
                        }
                    } else if (TinggiValue <= 74.0) {
                        if (BeratValue <= 9.1) {
                            Result = (BeratValue - 9.1) / (9.1 - 8.3);
                        } else {
                            Result = (BeratValue - 9.1) / (10.0 - 9.1);
                        }
                    } else if (TinggiValue <= 74.5) {
                        if (BeratValue <= 9.2) {
                            Result = (BeratValue - 9.2) / (9.2 - 8.4);
                        } else {
                            Result = (BeratValue - 9.2) / (10.1 - 9.2);
                        }
                    } else if (TinggiValue <= 75.0) {
                        if (BeratValue <= 9.3) {
                            Result = (BeratValue - 9.3) / (9.3 - 8.5);
                        } else {
                            Result = (BeratValue - 9.3) / (10.2 - 9.3);
                        }
                    } else if (TinggiValue <= 75.5) {
                        if (BeratValue <= 9.4) {
                            Result = (BeratValue - 9.4) / (9.4 - 8.6);
                        } else {
                            Result = (BeratValue - 9.4) / (10.3 - 9.4);
                        }
                    } else if (TinggiValue <= 76.0) {
                        if (BeratValue <= 9.5) {
                            Result = (BeratValue - 9.5) / (9.5 - 8.7);
                        } else {
                            Result = (BeratValue - 9.5) / (10.4 - 9.5);
                        }
                    } else if (TinggiValue <= 76.5) {
                        if (BeratValue <= 9.6) {
                            Result = (BeratValue - 9.6) / (9.6 - 8.7);
                        } else {
                            Result = (BeratValue - 9.6) / (10.5 - 9.6);
                        }
                    } else if (TinggiValue <= 77.0) {
                        if (BeratValue <= 9.6) {
                            Result = (BeratValue - 9.6) / (9.6 - 8.8);
                        } else {
                            Result = (BeratValue - 9.6) / (10.6 - 9.6);
                        }
                    } else if (TinggiValue <= 77.5) {
                        if (BeratValue <= 9.7) {
                            Result = (BeratValue - 9.7) / (9.7 - 8.9);
                        } else {
                            Result = (BeratValue - 9.7) / (10.7 - 9.7);
                        }
                    } else if (TinggiValue <= 78.0) {
                        if (BeratValue <= 9.8) {
                            Result = (BeratValue - 9.8) / (9.8 - 9.0);
                        } else {
                            Result = (BeratValue - 9.8) / (10.8 - 9.8);
                        }
                    } else if (TinggiValue <= 78.5) {
                        if (BeratValue <= 9.9) {
                            Result = (BeratValue - 9.9) / (9.9 - 9.1);
                        } else {
                            Result = (BeratValue - 9.9) / (10.9 - 9.9);
                        }
                    } else if (TinggiValue <= 79.0) {
                        if (BeratValue <= 10.0) {
                            Result = (BeratValue - 10.0) / (10.0 - 9.2);
                        } else {
                            Result = (BeratValue - 10.0) / (11.0 - 10.0);
                        }
                    } else if (TinggiValue <= 79.5) {
                        if (BeratValue <= 10.1) {
                            Result = (BeratValue - 10.1) / (10.1 - 9.3);
                        } else {
                            Result = (BeratValue - 10.1) / (11.1 - 10.1);
                        }
                    } else if (TinggiValue <= 80.0) {
                        if (BeratValue <= 10.2) {
                            Result = (BeratValue - 10.2) / (10.2 - 9.4);
                        } else {
                            Result = (BeratValue - 10.2) / (11.2 - 10.2);
                        }
                    } else if (TinggiValue <= 80.5) {
                        if (BeratValue <= 10.3) {
                            Result = (BeratValue - 10.3) / (10.3 - 9.5);
                        } else {
                            Result = (BeratValue - 10.3) / (11.3 - 10.3);
                        }
                    } else if (TinggiValue <= 81.0) {
                        if (BeratValue <= 10.4) {
                            Result = (BeratValue - 10.4) / (10.4 - 9.6);
                        } else {
                            Result = (BeratValue - 10.4) / (11.4 - 10.4);
                        }
                    } else if (TinggiValue <= 81.5) {
                        if (BeratValue <= 10.6) {
                            Result = (BeratValue - 10.6) / (10.6 - 9.7);
                        } else {
                            Result = (BeratValue - 10.6) / (11.6 - 10.6);
                        }
                    } else if (TinggiValue <= 82.0) {
                        if (BeratValue <= 10.7) {
                            Result = (BeratValue - 10.7) / (10.7 - 9.8);
                        } else {
                            Result = (BeratValue - 10.7) / (11.7 - 10.7);
                        }
                    } else if (TinggiValue <= 82.5) {
                        if (BeratValue <= 10.8) {
                            Result = (BeratValue - 10.8) / (10.8 - 9.9);
                        } else {
                            Result = (BeratValue - 10.8) / (11.8 - 10.8);
                        }
                    } else if (TinggiValue <= 83.0) {
                        if (BeratValue <= 10.9) {
                            Result = (BeratValue - 10.9) / (10.9 - 10.0);
                        } else {
                            Result = (BeratValue - 10.9) / (11.9 - 10.9);
                        }
                    } else if (TinggiValue <= 83.5) {
                        if (BeratValue <= 11.0) {
                            Result = (BeratValue - 11.0) / (11.0 - 10.1);
                        } else {
                            Result = (BeratValue - 11.0) / (12.1 - 11.0);
                        }
                    } else if (TinggiValue <= 84.0) {
                        if (BeratValue <= 11.1) {
                            Result = (BeratValue - 11.1) / (11.1 - 10.2);
                        } else {
                            Result = (BeratValue - 11.1) / (12.2 - 11.1);
                        }
                    } else if (TinggiValue <= 84.5) {
                        if (BeratValue <= 11.3) {
                            Result = (BeratValue - 11.3) / (11.3 - 10.3);
                        } else {
                            Result = (BeratValue - 11.3) / (12.3 - 11.3);
                        }
                    } else if (TinggiValue <= 85.0) {
                        if (BeratValue <= 11.4) {
                            Result = (BeratValue - 11.4) / (11.4 - 10.4);
                        } else {
                            Result = (BeratValue - 11.4) / (12.5 - 11.4);
                        }
                    } else if (TinggiValue <= 85.5) {
                        if (BeratValue <= 11.5) {
                            Result = (BeratValue - 11.5) / (11.5 - 10.6);
                        } else {
                            Result = (BeratValue - 11.5) / (12.6 - 11.5);
                        }
                    } else if (TinggiValue <= 86.0) {
                        if (BeratValue <= 11.6) {
                            Result = (BeratValue - 11.6) / (11.6 - 10.7);
                        } else {
                            Result = (BeratValue - 11.6) / (12.7 - 11.6);
                        }
                    } else if (TinggiValue <= 86.5) {
                        if (BeratValue <= 11.8) {
                            Result = (BeratValue - 11.8) / (11.8 - 10.8);
                        } else {
                            Result = (BeratValue - 11.8) / (12.9 - 11.8);
                        }
                    } else if (TinggiValue <= 87.0) {
                        if (BeratValue <= 11.9) {
                            Result = (BeratValue - 11.9) / (11.9 - 10.9);
                        } else {
                            Result = (BeratValue - 11.9) / (13.0 - 11.9);
                        }
                    } else if (TinggiValue <= 87.5) {
                        if (BeratValue <= 12.0) {
                            Result = (BeratValue - 12.0) / (12.0 - 11.0);
                        } else {
                            Result = (BeratValue - 12.0) / (13.2 - 12.0);
                        }
                    } else if (TinggiValue <= 88.0) {
                        if (BeratValue <= 12.1) {
                            Result = (BeratValue - 12.1) / (12.1 - 11.1);
                        } else {
                            Result = (BeratValue - 12.1) / (13.3 - 12.1);
                        }
                    } else if (TinggiValue <= 88.5) {
                        if (BeratValue <= 12.3) {
                            Result = (BeratValue - 12.3) / (12.3 - 11.2);
                        } else {
                            Result = (BeratValue - 12.3) / (13.4 - 12.3);
                        }
                    } else if (TinggiValue <= 89.0) {
                        if (BeratValue <= 12.4) {
                            Result = (BeratValue - 12.4) / (12.4 - 11.4);
                        } else {
                            Result = (BeratValue - 12.4) / (13.6 - 12.4);
                        }
                    } else if (TinggiValue <= 89.5) {
                        if (BeratValue <= 12.5) {
                            Result = (BeratValue - 12.5) / (12.5 - 11.5);
                        } else {
                            Result = (BeratValue - 12.5) / (13.7 - 12.5);
                        }
                    } else if (TinggiValue <= 90.0) {
                        if (BeratValue <= 12.6) {
                            Result = (BeratValue - 12.6) / (12.6 - 11.6);
                        } else {
                            Result = (BeratValue - 12.6) / (13.8 - 12.6);
                        }
                    } else if (TinggiValue <= 90.5) {
                        if (BeratValue <= 12.8) {
                            Result = (BeratValue - 12.8) / (12.8 - 11.7);
                        } else {
                            Result = (BeratValue - 12.8) / (14.0 - 12.8);
                        }
                    } else if (TinggiValue <= 91.0) {
                        if (BeratValue <= 12.9) {
                            Result = (BeratValue - 12.9) / (12.9 - 11.8);
                        } else {
                            Result = (BeratValue - 12.9) / (14.1 - 12.9);
                        }
                    } else if (TinggiValue <= 91.5) {
                        if (BeratValue <= 13.0) {
                            Result = (BeratValue - 13.0) / (13.0 - 11.9);
                        } else {
                            Result = (BeratValue - 13.0) / (14.3 - 13.0);
                        }
                    } else if (TinggiValue <= 92.0) {
                        if (BeratValue <= 13.1) {
                            Result = (BeratValue - 13.1) / (13.1 - 12.0);
                        } else {
                            Result = (BeratValue - 13.1) / (14.4 - 13.1);
                        }
                    } else if (TinggiValue <= 92.5) {
                        if (BeratValue <= 13.3) {
                            Result = (BeratValue - 13.3) / (13.3 - 12.1);
                        } else {
                            Result = (BeratValue - 13.3) / (14.5 - 13.3);
                        }
                    } else if (TinggiValue <= 93.0) {
                        if (BeratValue <= 13.4) {
                            Result = (BeratValue - 13.4) / (13.4 - 12.3);
                        } else {
                            Result = (BeratValue - 13.4) / (14.7 - 13.4);
                        }
                    } else if (TinggiValue <= 93.5) {
                        if (BeratValue <= 13.5) {
                            Result = (BeratValue - 13.5) / (13.5 - 12.4);
                        } else {
                            Result = (BeratValue - 13.5) / (14.8 - 13.5);
                        }
                    } else if (TinggiValue <= 94.0) {
                        if (BeratValue <= 13.6) {
                            Result = (BeratValue - 13.6) / (13.6 - 12.5);
                        } else {
                            Result = (BeratValue - 13.6) / (14.9 - 13.6);
                        }
                    } else if (TinggiValue <= 94.5) {
                        if (BeratValue <= 13.8) {
                            Result = (BeratValue - 13.8) / (13.8 - 12.6);
                        } else {
                            Result = (BeratValue - 13.8) / (15.1 - 13.8);
                        }
                    } else if (TinggiValue <= 95.0) {
                        if (BeratValue <= 13.9) {
                            Result = (BeratValue - 13.9) / (13.9 - 12.7);
                        } else {
                            Result = (BeratValue - 13.9) / (15.2 - 13.9);
                        }
                    } else if (TinggiValue <= 95.5) {
                        if (BeratValue <= 14.0) {
                            Result = (BeratValue - 14.0) / (14.0 - 12.8);
                        } else {
                            Result = (BeratValue - 14.0) / (15.4 - 14.0);
                        }
                    } else if (TinggiValue <= 96.0) {
                        if (BeratValue <= 14.1) {
                            Result = (BeratValue - 14.1) / (14.1 - 12.9);
                        } else {
                            Result = (BeratValue - 14.1) / (15.5 - 14.1);
                        }
                    } else if (TinggiValue <= 96.5) {
                        if (BeratValue <= 14.3) {
                            Result = (BeratValue - 14.3) / (14.3 - 13.1);
                        } else {
                            Result = (BeratValue - 14.3) / (15.6 - 14.3);
                        }
                    } else if (TinggiValue <= 97.0) {
                        if (BeratValue <= 14.4) {
                            Result = (BeratValue - 14.4) / (14.4 - 13.2);
                        } else {
                            Result = (BeratValue - 14.4) / (15.8 - 14.4);
                        }
                    } else if (TinggiValue <= 97.5) {
                        if (BeratValue <= 14.5) {
                            Result = (BeratValue - 14.5) / (14.5 - 13.3);
                        } else {
                            Result = (BeratValue - 14.5) / (15.9 - 14.5);
                        }
                    } else if (TinggiValue <= 98.0) {
                        if (BeratValue <= 14.7) {
                            Result = (BeratValue - 14.7) / (14.7 - 13.4);
                        } else {
                            Result = (BeratValue - 14.7) / (16.1 - 14.7);
                        }
                    } else if (TinggiValue <= 98.5) {
                        if (BeratValue <= 14.8) {
                            Result = (BeratValue - 14.8) / (14.8 - 13.5);
                        } else {
                            Result = (BeratValue - 14.8) / (16.2 - 14.8);
                        }
                    } else if (TinggiValue <= 99.0) {
                        if (BeratValue <= 14.9) {
                            Result = (BeratValue - 14.9) / (14.9 - 13.7);
                        } else {
                            Result = (BeratValue - 14.9) / (16.4 - 14.9);
                        }
                    } else if (TinggiValue <= 99.5) {
                        if (BeratValue <= 15.1) {
                            Result = (BeratValue - 15.1) / (15.1 - 13.8);
                        } else {
                            Result = (BeratValue - 15.1) / (16.5 - 15.1);
                        }
                    } else if (TinggiValue <= 100.0) {
                        if (BeratValue <= 15.2) {
                            Result = (BeratValue - 15.2) / (15.2 - 13.9);
                        } else {
                            Result = (BeratValue - 15.2) / (16.7 - 15.2);
                        }
                    } else if (TinggiValue <= 100.5) {
                        if (BeratValue <= 15.4) {
                            Result = (BeratValue - 15.4) / (15.4 - 14.1);
                        } else {
                            Result = (BeratValue - 15.4) / (16.9 - 15.4);
                        }
                    } else if (TinggiValue <= 101.0) {
                        if (BeratValue <= 15.5) {
                            Result = (BeratValue - 15.5) / (15.5 - 14.2);
                        } else {
                            Result = (BeratValue - 15.5) / (17.0 - 15.5);
                        }
                    } else if (TinggiValue <= 101.5) {
                        if (BeratValue <= 15.7) {
                            Result = (BeratValue - 15.7) / (15.7 - 14.3);
                        } else {
                            Result = (BeratValue - 15.7) / (17.2 - 15.7);
                        }
                    } else if (TinggiValue <= 102.0) {
                        if (BeratValue <= 15.8) {
                            Result = (BeratValue - 15.8) / (15.8 - 14.5);
                        } else {
                            Result = (BeratValue - 15.8) / (17.4 - 15.8);
                        }
                    } else if (TinggiValue <= 102.5) {
                        if (BeratValue <= 16.0) {
                            Result = (BeratValue - 16.0) / (16.0 - 14.6);
                        } else {
                            Result = (BeratValue - 16.0) / (17.5 - 16.0);
                        }
                    } else if (TinggiValue <= 103.0) {
                        if (BeratValue <= 16.1) {
                            Result = (BeratValue - 16.1) / (16.1 - 14.7);
                        } else {
                            Result = (BeratValue - 16.1) / (17.7 - 16.1);
                        }
                    } else if (TinggiValue <= 103.5) {
                        if (BeratValue <= 16.3) {
                            Result = (BeratValue - 16.3) / (16.3 - 14.9);
                        } else {
                            Result = (BeratValue - 16.3) / (17.9 - 16.3);
                        }
                    } else if (TinggiValue <= 104.0) {
                        if (BeratValue <= 16.4) {
                            Result = (BeratValue - 16.4) / (16.4 - 15.0);
                        } else {
                            Result = (BeratValue - 16.4) / (18.1 - 16.4);
                        }
                    } else if (TinggiValue <= 104.5) {
                        if (BeratValue <= 16.6) {
                            Result = (BeratValue - 16.6) / (16.6 - 15.2);
                        } else {
                            Result = (BeratValue - 16.6) / (18.2 - 16.6);
                        }
                    } else if (TinggiValue <= 105.0) {
                        if (BeratValue <= 16.8) {
                            Result = (BeratValue - 16.8) / (16.8 - 15.3);
                        } else {
                            Result = (BeratValue - 16.8) / (18.4 - 16.8);
                        }
                    } else if (TinggiValue <= 105.5) {
                        if (BeratValue <= 16.9) {
                            Result = (BeratValue - 16.9) / (16.9 - 15.5);
                        } else {
                            Result = (BeratValue - 16.9) / (18.6 - 16.9);
                        }
                    } else if (TinggiValue <= 106.0) {
                        if (BeratValue <= 17.1) {
                            Result = (BeratValue - 17.1) / (17.1 - 15.6);
                        } else {
                            Result = (BeratValue - 17.1) / (18.8 - 17.1);
                        }
                    } else if (TinggiValue <= 106.5) {
                        if (BeratValue <= 17.3) {
                            Result = (BeratValue - 17.3) / (17.3 - 15.8);
                        } else {
                            Result = (BeratValue - 17.3) / (19.0 - 17.3);
                        }
                    } else if (TinggiValue <= 107.0) {
                        if (BeratValue <= 17.5) {
                            Result = (BeratValue - 17.5) / (17.5 - 15.9);
                        } else {
                            Result = (BeratValue - 17.5) / (19.2 - 17.5);
                        }
                    } else if (TinggiValue <= 107.5) {
                        if (BeratValue <= 17.7) {
                            Result = (BeratValue - 17.7) / (17.7 - 16.1);
                        } else {
                            Result = (BeratValue - 17.7) / (19.4 - 17.7);
                        }
                    } else if (TinggiValue <= 108.0) {
                        if (BeratValue <= 17.8) {
                            Result = (BeratValue - 17.8) / (17.8 - 16.3);
                        } else {
                            Result = (BeratValue - 17.8) / (19.6 - 17.8);
                        }
                    } else if (TinggiValue <= 108.5) {
                        if (BeratValue <= 18.0) {
                            Result = (BeratValue - 18.0) / (18.0 - 16.4);
                        } else {
                            Result = (BeratValue - 18.0) / (19.8 - 18.0);
                        }
                    } else if (TinggiValue <= 109.0) {
                        if (BeratValue <= 18.2) {
                            Result = (BeratValue - 18.2) / (18.2 - 16.6);
                        } else {
                            Result = (BeratValue - 18.2) / (20.0 - 18.2);
                        }
                    } else if (TinggiValue <= 109.5) {
                        if (BeratValue <= 18.4) {
                            Result = (BeratValue - 18.4) / (18.4 - 16.8);
                        } else {
                            Result = (BeratValue - 18.4) / (20.3 - 18.4);
                        }
                    } else if (TinggiValue <= 110.0) {
                        if (BeratValue <= 18.6) {
                            Result = (BeratValue - 18.6) / (18.6 - 17.0);
                        } else {
                            Result = (BeratValue - 18.6) / (20.5 - 18.6);
                        }
                    } else if (TinggiValue <= 110.5) {
                        if (BeratValue <= 18.8) {
                            Result = (BeratValue - 18.8) / (18.8 - 17.1);
                        } else {
                            Result = (BeratValue - 18.8) / (20.7 - 18.8);
                        }
                    } else if (TinggiValue <= 111.0) {
                        if (BeratValue <= 19.0) {
                            Result = (BeratValue - 19.0) / (19.0 - 17.3);
                        } else {
                            Result = (BeratValue - 19.0) / (20.9 - 19.0);
                        }
                    } else if (TinggiValue <= 111.5) {
                        if (BeratValue <= 19.2) {
                            Result = (BeratValue - 19.2) / (19.2 - 17.5);
                        } else {
                            Result = (BeratValue - 19.2) / (21.2 - 19.2);
                        }
                    } else if (TinggiValue <= 112.0) {
                        if (BeratValue <= 19.4) {
                            Result = (BeratValue - 19.4) / (19.4 - 17.7);
                        } else {
                            Result = (BeratValue - 19.4) / (21.4 - 19.4);
                        }
                    } else if (TinggiValue <= 112.5) {
                        if (BeratValue <= 19.6) {
                            Result = (BeratValue - 19.6) / (19.6 - 17.9);
                        } else {
                            Result = (BeratValue - 19.6) / (21.6 - 19.6);
                        }
                    } else if (TinggiValue <= 113.0) {
                        if (BeratValue <= 19.8) {
                            Result = (BeratValue - 19.8) / (19.8 - 18.0);
                        } else {
                            Result = (BeratValue - 19.8) / (21.8 - 19.8);
                        }
                    } else if (TinggiValue <= 113.5) {
                        if (BeratValue <= 20.0) {
                            Result = (BeratValue - 20.0) / (20.0 - 18.2);
                        } else {
                            Result = (BeratValue - 20.0) / (22.1 - 20.0);
                        }
                    } else if (TinggiValue <= 114.0) {
                        if (BeratValue <= 20.2) {
                            Result = (BeratValue - 20.2) / (20.2 - 18.4);
                        } else {
                            Result = (BeratValue - 20.2) / (22.3 - 20.2);
                        }
                    } else if (TinggiValue <= 114.5) {
                        if (BeratValue <= 20.5) {
                            Result = (BeratValue - 20.5) / (20.5 - 18.6);
                        } else {
                            Result = (BeratValue - 20.5) / (22.6 - 20.5);
                        }
                    } else if (TinggiValue <= 115.0) {
                        if (BeratValue <= 20.7) {
                            Result = (BeratValue - 20.7) / (20.7 - 18.8);
                        } else {
                            Result = (BeratValue - 20.7) / (22.8 - 20.7);
                        }
                    } else if (TinggiValue <= 115.5) {
                        if (BeratValue <= 20.9) {
                            Result = (BeratValue - 20.9) / (20.9 - 19.0);
                        } else {
                            Result = (BeratValue - 20.9) / (23.0 - 20.9);
                        }
                    } else if (TinggiValue <= 116.0) {
                        if (BeratValue <= 21.1) {
                            Result = (BeratValue - 21.1) / (21.1 - 19.2);
                        } else {
                            Result = (BeratValue - 21.1) / (23.3 - 21.1);
                        }
                    } else if (TinggiValue <= 116.5) {
                        if (BeratValue <= 21.3) {
                            Result = (BeratValue - 21.3) / (21.3 - 19.4);
                        } else {
                            Result = (BeratValue - 21.3) / (23.5 - 21.3);
                        }
                    } else if (TinggiValue <= 117.0) {
                        if (BeratValue <= 21.5) {
                            Result = (BeratValue - 21.5) / (21.5 - 19.6);
                        } else {
                            Result = (BeratValue - 21.5) / (23.8 - 21.5);
                        }
                    } else if (TinggiValue <= 117.5) {
                        if (BeratValue <= 21.7) {
                            Result = (BeratValue - 21.7) / (21.7 - 19.8);
                        } else {
                            Result = (BeratValue - 21.7) / (24.0 - 21.7);
                        }
                    } else if (TinggiValue <= 118.0) {
                        if (BeratValue <= 22.0) {
                            Result = (BeratValue - 22.0) / (22.0 - 19.9);
                        } else {
                            Result = (BeratValue - 22.0) / (24.2 - 22.0);
                        }
                    } else if (TinggiValue <= 118.5) {
                        if (BeratValue <= 22.2) {
                            Result = (BeratValue - 22.2) / (22.2 - 20.1);
                        } else {
                            Result = (BeratValue - 22.2) / (24.5 - 22.2);
                        }
                    } else if (TinggiValue <= 119.0) {
                        if (BeratValue <= 22.4) {
                            Result = (BeratValue - 22.4) / (22.4 - 20.3);
                        } else {
                            Result = (BeratValue - 22.4) / (24.7 - 22.4);
                        }
                    } else if (TinggiValue <= 119.5) {
                        if (BeratValue <= 22.6) {
                            Result = (BeratValue - 22.6) / (22.6 - 20.5);
                        } else {
                            Result = (BeratValue - 22.6) / (25.0 - 22.6);
                        }
                    } else if (TinggiValue <= 120.0) {
                        if (BeratValue <= 22.8) {
                            Result = (BeratValue - 22.8) / (22.8 - 20.7);
                        } else {
                            Result = (BeratValue - 22.8) / (25.2 - 22.8);
                        }
                    }
                }
            }
            childScoreBBTB.setText(String.format(Locale.ENGLISH, "%.1f", Result));
            if (Result < -3) {
                childStatusBBTB.setText("Sangat Kurus");
                childStatusBBTB.setTextColor(Color.parseColor("#F4511E"));
                childScoreBBTB.setTextColor(Color.parseColor("#F4511E"));
            } else if (Result >= -3 && Result < -2) {
                childStatusBBTB.setText("Kurus");
                childStatusBBTB.setTextColor(Color.parseColor("#000000"));
                childScoreBBTB.setTextColor(Color.parseColor("#000000"));
            } else if (Result >= -2 && Result <= 2) {
                childStatusBBTB.setText("Normal");
                childStatusBBTB.setTextColor(Color.parseColor("#000000"));
                childScoreBBTB.setTextColor(Color.parseColor("#000000"));
            } else if (Result > 2) {
                childStatusBBTB.setText("Gemuk");
                childStatusBBTB.setTextColor(Color.parseColor("#F4511E"));
                childScoreBBTB.setTextColor(Color.parseColor("#F4511E"));
            }
        }
    }

    private void saveBBTB() {
        String Score = String.valueOf(childScoreBBTB.getText().toString());
        String Status = String.valueOf(childStatusBBTB.getText().toString());
        String Berat = childWeightCountBBTB.getText().toString();
        String Tinggi = childHeightCountBBTB.getText().toString();
        final String UmurString = childMonthCountBBTB.getText().toString();
        Integer Umur = Integer.parseInt(UmurString);
        String DateSave = FormatDate;

        final String id = mDatabase.push().getKey();
        final BeratBadanTinggiBadan beratBadanTinggiBadan = new BeratBadanTinggiBadan(
                id, Score, Status, Berat, Tinggi, Umur, DateSave);

        showProgressDialog();
        mDatabase.orderByChild("bbtbAge").equalTo(Umur)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hideProgressDialog();
                        if (!dataSnapshot.exists()) {
                            mDatabase.child(id).setValue(beratBadanTinggiBadan);
                            Snackbar.make(BBTBCount.this.findViewById(android.R.id.content),
                                    "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(BBTBCount.this.findViewById(android.R.id.content),
                                    "Data di bulan ke " + UmurString + " sudah tersimpan",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void saveDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder
                .setMessage("Apakah data yang diisi sudah benar?")
                .setCancelable(false)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            dialog.cancel();
                            Snackbar.make(BBTBCount.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        saveBBTB();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ButtonCountBBTB) {
            countBBTB();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception ignored) {

            }
        } else if (i == R.id.ButtonSaveBBTB) {
            saveDialog();
        }
    }
}