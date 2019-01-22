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
import com.fbasegizi.statusgizi.model.BeratBadanUmur;
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

public class BBUCount extends BaseActivity implements View.OnClickListener {

    private TextInputLayout fieldWeight;
    private TextInputLayout fieldAge;

    private TextView textViewNameCountBBU;
    private TextView childScoreBBU;
    private TextView childStatusBBU;

    private EditText childDateCountBBU;
    private EditText childGenderCountBBU;
    private EditText childMonthCountBBU;
    private EditText childWeightCountBBU;

    private Button buttonCountBBU;
    private Button buttonSaveBBU;

    private String Tanggal, FormatDate;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_bbu);

        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference("BeratBadanUmur")
                .child(getUid()).child(intent.getStringExtra("id"));

        textViewNameCountBBU = findViewById(R.id.textViewNameCountBBU);
        childDateCountBBU = findViewById(R.id.ChildDateCountBBU);
        childGenderCountBBU = findViewById(R.id.ChildGenderCountBBU);
        childMonthCountBBU = findViewById(R.id.ChildMonthCountBBU);
        childWeightCountBBU = findViewById(R.id.ChildWeightCountBBU);
        buttonCountBBU = findViewById(R.id.ButtonCountBBU);
        childScoreBBU = findViewById(R.id.ChildScoreBBU);
        childStatusBBU = findViewById(R.id.ChildStatusBBU);
        buttonSaveBBU = findViewById(R.id.ButtonSaveBBU);
        fieldWeight = findViewById(R.id.WeightFieldBBU);
        fieldAge = findViewById(R.id.AgeFieldBBU);

        buttonCountBBU.setOnClickListener(this);
        buttonSaveBBU.setOnClickListener(this);

        buttonSaveBBU.setVisibility(View.GONE);

        textViewNameCountBBU.setText(intent.getStringExtra("nama"));
        childGenderCountBBU.setText(intent.getStringExtra("gender"));
        Tanggal = intent.getStringExtra("tanggal");
        childDateCountBBU.setText(Tanggal);

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
        childMonthCountBBU.setText(String.valueOf(months));

        //Get current date
        Date c = Calendar.getInstance().getTime();
        FormatDate = sdf.format(c);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Berat Badan Umur");
        }
    }

    private void countBBU() {
        String Gender = childGenderCountBBU.getText().toString();
        String Umur = childMonthCountBBU.getText().toString();
        String Berat = childWeightCountBBU.getText().toString();

        Integer UmurValue = !Umur.equals("") ? Integer.parseInt(Umur) : 0;
        Double BeratValue = !Berat.equals("") ? Double.parseDouble(Berat) : 0;
        Double Result = 0.0;

        //Ganti dari sini
        if (Berat.isEmpty()) {
            fieldWeight.setError("Masukkan berat badan");
        } else if (BeratValue < 1) {
            fieldWeight.setError("Berat badan minimal 1 Kg!");
        } else if (BeratValue > 50) {
            fieldWeight.setError("Berat badan maksimal 50 Kg!");
        } else if (Umur.isEmpty()) {
            fieldAge.setError("Masukkan umur");
        } else {
            fieldWeight.setErrorEnabled(false);
            fieldAge.setErrorEnabled(false);
            buttonSaveBBU.setVisibility(View.VISIBLE);
            if (Gender.equals("Laki-laki")) {
                if (UmurValue == 0) {
                    if (BeratValue <= 3.3) {
                        Result = (BeratValue - 3.3) / (3.3 - 2.9);
                    } else {
                        Result = (BeratValue - 3.3) / (3.9 - 3.3);
                    }
                } else if (UmurValue == 1) {
                    if (BeratValue <= 4.5) {
                        Result = (BeratValue - 4.5) / (4.5 - 3.9);
                    } else {
                        Result = (BeratValue - 4.5) / (5.1 - 4.5);
                    }
                } else if (UmurValue == 2) {
                    if (BeratValue <= 5.6) {
                        Result = (BeratValue - 5.6) / (5.6 - 4.9);
                    } else {
                        Result = (BeratValue - 5.6) / (6.3 - 5.6);
                    }
                } else if (UmurValue == 3) {
                    if (BeratValue <= 6.4) {
                        Result = (BeratValue - 6.4) / (6.4 - 5.7);
                    } else {
                        Result = (BeratValue - 6.4) / (7.2 - 6.4);
                    }
                } else if (UmurValue == 4) {
                    if (BeratValue <= 7.0) {
                        Result = (BeratValue - 7.0) / (7.0 - 6.2);
                    } else {
                        Result = (BeratValue - 7.0) / (7.8 - 7.0);
                    }
                } else if (UmurValue == 5) {
                    if (BeratValue <= 7.5) {
                        Result = (BeratValue - 7.5) / (7.5 - 6.7);
                    } else {
                        Result = (BeratValue - 7.5) / (8.4 - 7.5);
                    }
                } else if (UmurValue == 6) {
                    if (BeratValue <= 7.9) {
                        Result = (BeratValue - 7.9) / (7.9 - 7.1);
                    } else {
                        Result = (BeratValue - 7.9) / (8.8 - 7.9);
                    }
                } else if (UmurValue == 7) {
                    if (BeratValue <= 8.3) {
                        Result = (BeratValue - 8.3) / (8.3 - 7.4);
                    } else {
                        Result = (BeratValue - 8.3) / (9.2 - 8.3);
                    }
                } else if (UmurValue == 8) {
                    if (BeratValue <= 8.6) {
                        Result = (BeratValue - 8.6) / (8.6 - 7.7);
                    } else {
                        Result = (BeratValue - 8.6) / (9.6 - 8.6);
                    }
                } else if (UmurValue == 9) {
                    if (BeratValue <= 8.9) {
                        Result = (BeratValue - 8.9) / (8.9 - 8.0);
                    } else {
                        Result = (BeratValue - 8.9) / (9.9 - 8.9);
                    }
                } else if (UmurValue == 10) {
                    if (BeratValue <= 9.2) {
                        Result = (BeratValue - 9.2) / (9.2 - 8.2);
                    } else {
                        Result = (BeratValue - 9.2) / (10.2 - 9.2);
                    }
                } else if (UmurValue == 11) {
                    if (BeratValue <= 9.4) {
                        Result = (BeratValue - 9.4) / (9.4 - 8.4);
                    } else {
                        Result = (BeratValue - 9.4) / (10.5 - 9.4);
                    }
                } else if (UmurValue == 12) {
                    if (BeratValue <= 9.6) {
                        Result = (BeratValue - 9.6) / (9.6 - 8.6);
                    } else {
                        Result = (BeratValue - 9.6) / (10.8 - 9.6);
                    }
                } else if (UmurValue == 13) {
                    if (BeratValue <= 9.9) {
                        Result = (BeratValue - 9.9) / (9.9 - 8.8);
                    } else {
                        Result = (BeratValue - 9.9) / (11.0 - 9.9);
                    }
                } else if (UmurValue == 14) {
                    if (BeratValue <= 10.1) {
                        Result = (BeratValue - 10.1) / (10.1 - 9.0);
                    } else {
                        Result = (BeratValue - 10.1) / (11.3 - 10.1);
                    }
                } else if (UmurValue == 15) {
                    if (BeratValue <= 10.3) {
                        Result = (BeratValue - 10.3) / (10.3 - 9.2);
                    } else {
                        Result = (BeratValue - 10.3) / (11.5 - 10.3);
                    }
                } else if (UmurValue == 16) {
                    if (BeratValue <= 10.5) {
                        Result = (BeratValue - 10.5) / (10.5 - 9.4);
                    } else {
                        Result = (BeratValue - 10.5) / (11.7 - 10.5);
                    }
                } else if (UmurValue == 17) {
                    if (BeratValue <= 10.7) {
                        Result = (BeratValue - 10.7) / (10.7 - 9.6);
                    } else {
                        Result = (BeratValue - 10.7) / (12.0 - 10.7);
                    }
                } else if (UmurValue == 18) {
                    if (BeratValue <= 10.9) {
                        Result = (BeratValue - 10.9) / (10.9 - 9.8);
                    } else {
                        Result = (BeratValue - 10.9) / (12.2 - 10.9);
                    }
                } else if (UmurValue == 19) {
                    if (BeratValue <= 11.1) {
                        Result = (BeratValue - 11.1) / (11.1 - 10.0);
                    } else {
                        Result = (BeratValue - 11.1) / (12.5 - 11.1);
                    }
                } else if (UmurValue == 20) {
                    if (BeratValue <= 11.3) {
                        Result = (BeratValue - 11.3) / (11.3 - 10.1);
                    } else {
                        Result = (BeratValue - 11.3) / (12.7 - 11.3);
                    }
                } else if (UmurValue == 21) {
                    if (BeratValue <= 11.5) {
                        Result = (BeratValue - 11.5) / (11.5 - 10.3);
                    } else {
                        Result = (BeratValue - 11.5) / (12.9 - 11.5);
                    }
                } else if (UmurValue == 22) {
                    if (BeratValue <= 11.8) {
                        Result = (BeratValue - 11.8) / (11.8 - 10.5);
                    } else {
                        Result = (BeratValue - 11.8) / (13.2 - 11.8);
                    }
                } else if (UmurValue == 23) {
                    if (BeratValue <= 12.0) {
                        Result = (BeratValue - 12.0) / (12.0 - 10.7);
                    } else {
                        Result = (BeratValue - 12.0) / (13.4 - 12.0);
                    }
                } else if (UmurValue == 24) {
                    if (BeratValue <= 12.2) {
                        Result = (BeratValue - 12.2) / (12.2 - 10.8);
                    } else {
                        Result = (BeratValue - 12.2) / (13.6 - 12.2);
                    }
                } else if (UmurValue == 25) {
                    if (BeratValue <= 12.4) {
                        Result = (BeratValue - 12.4) / (12.4 - 11.0);
                    } else {
                        Result = (BeratValue - 12.4) / (13.9 - 12.4);
                    }
                } else if (UmurValue == 26) {
                    if (BeratValue <= 12.5) {
                        Result = (BeratValue - 12.5) / (12.5 - 11.2);
                    } else {
                        Result = (BeratValue - 12.5) / (14.1 - 12.5);
                    }
                } else if (UmurValue == 27) {
                    if (BeratValue <= 12.7) {
                        Result = (BeratValue - 12.7) / (12.7 - 11.3);
                    } else {
                        Result = (BeratValue - 12.7) / (14.3 - 12.7);
                    }
                } else if (UmurValue == 28) {
                    if (BeratValue <= 12.9) {
                        Result = (BeratValue - 12.9) / (12.9 - 11.5);
                    } else {
                        Result = (BeratValue - 12.9) / (14.5 - 12.9);
                    }
                } else if (UmurValue == 29) {
                    if (BeratValue <= 13.1) {
                        Result = (BeratValue - 13.1) / (13.1 - 11.7);
                    } else {
                        Result = (BeratValue - 13.1) / (14.8 - 13.1);
                    }
                } else if (UmurValue == 30) {
                    if (BeratValue <= 13.3) {
                        Result = (BeratValue - 13.3) / (13.3 - 11.8);
                    } else {
                        Result = (BeratValue - 13.3) / (15.0 - 13.3);
                    }
                } else if (UmurValue == 31) {
                    if (BeratValue <= 13.5) {
                        Result = (BeratValue - 13.5) / (13.5 - 12.0);
                    } else {
                        Result = (BeratValue - 13.5) / (15.2 - 13.5);
                    }
                } else if (UmurValue == 32) {
                    if (BeratValue <= 13.7) {
                        Result = (BeratValue - 13.7) / (13.7 - 12.1);
                    } else {
                        Result = (BeratValue - 13.7) / (15.4 - 13.7);
                    }
                } else if (UmurValue == 33) {
                    if (BeratValue <= 13.8) {
                        Result = (BeratValue - 13.8) / (13.8 - 12.3);
                    } else {
                        Result = (BeratValue - 13.8) / (15.6 - 13.8);
                    }
                } else if (UmurValue == 34) {
                    if (BeratValue <= 14.0) {
                        Result = (BeratValue - 14.0) / (14.0 - 12.4);
                    } else {
                        Result = (BeratValue - 14.0) / (15.8 - 14.0);
                    }
                } else if (UmurValue == 35) {
                    if (BeratValue <= 14.2) {
                        Result = (BeratValue - 14.2) / (14.2 - 12.6);
                    } else {
                        Result = (BeratValue - 14.2) / (16.0 - 14.2);
                    }
                } else if (UmurValue == 36) {
                    if (BeratValue <= 14.3) {
                        Result = (BeratValue - 14.3) / (14.3 - 12.7);
                    } else {
                        Result = (BeratValue - 14.3) / (16.2 - 14.3);
                    }
                } else if (UmurValue == 37) {
                    if (BeratValue <= 14.5) {
                        Result = (BeratValue - 14.5) / (14.5 - 12.9);
                    } else {
                        Result = (BeratValue - 14.5) / (16.4 - 14.5);
                    }
                } else if (UmurValue == 38) {
                    if (BeratValue <= 14.7) {
                        Result = (BeratValue - 14.7) / (14.7 - 13.0);
                    } else {
                        Result = (BeratValue - 14.7) / (16.6 - 14.7);
                    }
                } else if (UmurValue == 39) {
                    if (BeratValue <= 14.8) {
                        Result = (BeratValue - 14.8) / (14.8 - 13.1);
                    } else {
                        Result = (BeratValue - 14.8) / (16.8 - 14.8);
                    }
                } else if (UmurValue == 40) {
                    if (BeratValue <= 15.0) {
                        Result = (BeratValue - 15.0) / (15.0 - 13.3);
                    } else {
                        Result = (BeratValue - 15.0) / (17.0 - 15.0);
                    }
                } else if (UmurValue == 41) {
                    if (BeratValue <= 15.2) {
                        Result = (BeratValue - 15.2) / (15.2 - 13.4);
                    } else {
                        Result = (BeratValue - 15.2) / (17.2 - 15.2);
                    }
                } else if (UmurValue == 42) {
                    if (BeratValue <= 15.3) {
                        Result = (BeratValue - 15.3) / (15.3 - 13.6);
                    } else {
                        Result = (BeratValue - 15.3) / (17.4 - 15.3);
                    }
                } else if (UmurValue == 43) {
                    if (BeratValue <= 15.5) {
                        Result = (BeratValue - 15.5) / (15.5 - 13.7);
                    } else {
                        Result = (BeratValue - 15.5) / (17.6 - 15.5);
                    }
                } else if (UmurValue == 44) {
                    if (BeratValue <= 15.7) {
                        Result = (BeratValue - 15.7) / (15.7 - 13.8);
                    } else {
                        Result = (BeratValue - 15.7) / (17.8 - 15.7);
                    }
                } else if (UmurValue == 45) {
                    if (BeratValue <= 15.8) {
                        Result = (BeratValue - 15.8) / (15.8 - 14.0);
                    } else {
                        Result = (BeratValue - 15.8) / (18.0 - 15.8);
                    }
                } else if (UmurValue == 46) {
                    if (BeratValue <= 16.0) {
                        Result = (BeratValue - 16.0) / (16.0 - 14.1);
                    } else {
                        Result = (BeratValue - 16.0) / (18.2 - 16.0);
                    }
                } else if (UmurValue == 47) {
                    if (BeratValue <= 16.2) {
                        Result = (BeratValue - 16.2) / (16.2 - 14.3);
                    } else {
                        Result = (BeratValue - 16.2) / (18.4 - 16.2);
                    }
                } else if (UmurValue == 48) {
                    if (BeratValue <= 16.3) {
                        Result = (BeratValue - 16.3) / (16.3 - 14.4);
                    } else {
                        Result = (BeratValue - 16.3) / (18.6 - 16.3);
                    }
                } else if (UmurValue == 49) {
                    if (BeratValue <= 16.5) {
                        Result = (BeratValue - 16.5) / (16.5 - 14.5);
                    } else {
                        Result = (BeratValue - 16.5) / (18.8 - 16.5);
                    }
                } else if (UmurValue == 50) {
                    if (BeratValue <= 16.7) {
                        Result = (BeratValue - 16.7) / (16.7 - 14.7);
                    } else {
                        Result = (BeratValue - 16.7) / (19.0 - 16.7);
                    }
                } else if (UmurValue == 51) {
                    if (BeratValue <= 16.8) {
                        Result = (BeratValue - 16.8) / (16.8 - 14.8);
                    } else {
                        Result = (BeratValue - 16.8) / (19.2 - 16.8);
                    }
                } else if (UmurValue == 52) {
                    if (BeratValue <= 17.0) {
                        Result = (BeratValue - 17.0) / (17.0 - 15.0);
                    } else {
                        Result = (BeratValue - 17.0) / (19.4 - 17.0);
                    }
                } else if (UmurValue == 53) {
                    if (BeratValue <= 17.2) {
                        Result = (BeratValue - 17.2) / (17.2 - 15.1);
                    } else {
                        Result = (BeratValue - 17.2) / (19.6 - 17.2);
                    }
                } else if (UmurValue == 54) {
                    if (BeratValue <= 17.3) {
                        Result = (BeratValue - 17.3) / (17.3 - 15.2);
                    } else {
                        Result = (BeratValue - 17.3) / (19.8 - 17.3);
                    }
                } else if (UmurValue == 55) {
                    if (BeratValue <= 17.5) {
                        Result = (BeratValue - 17.5) / (17.5 - 15.4);
                    } else {
                        Result = (BeratValue - 17.5) / (20.0 - 17.5);
                    }
                } else if (UmurValue == 56) {
                    if (BeratValue <= 17.7) {
                        Result = (BeratValue - 17.7) / (17.7 - 15.5);
                    } else {
                        Result = (BeratValue - 17.7) / (20.2 - 17.7);
                    }
                } else if (UmurValue == 57) {
                    if (BeratValue <= 17.8) {
                        Result = (BeratValue - 17.8) / (17.8 - 15.6);
                    } else {
                        Result = (BeratValue - 17.8) / (20.4 - 17.8);
                    }
                } else if (UmurValue == 58) {
                    if (BeratValue <= 18.0) {
                        Result = (BeratValue - 18.0) / (18.0 - 15.8);
                    } else {
                        Result = (BeratValue - 18.0) / (20.6 - 18.0);
                    }
                } else if (UmurValue == 59) {
                    if (BeratValue <= 18.2) {
                        Result = (BeratValue - 18.2) / (18.2 - 15.9);
                    } else {
                        Result = (BeratValue - 18.2) / (20.8 - 18.2);
                    }
                } else if (UmurValue == 60) {
                    if (BeratValue <= 18.3) {
                        Result = (BeratValue - 18.3) / (18.3 - 16.0);
                    } else {
                        Result = (BeratValue - 18.3) / (21.0 - 18.3);
                    }
                }
            } else if (Gender.equals("Perempuan")) {
                if (UmurValue == 0) {
                    if (BeratValue <= 3.2) {
                        Result = (BeratValue - 3.2) / (3.2 - 2.8);
                    } else {
                        Result = (BeratValue - 3.2) / (3.7 - 3.2);
                    }
                } else if (UmurValue == 1) {
                    if (BeratValue <= 4.2) {
                        Result = (BeratValue - 4.2) / (4.2 - 3.6);
                    } else {
                        Result = (BeratValue - 3.2) / (4.8 - 4.2);
                    }
                } else if (UmurValue == 2) {
                    if (BeratValue <= 5.1) {
                        Result = (BeratValue - 5.1) / (5.1 - 4.5);
                    } else {
                        Result = (BeratValue - 5.1) / (5.8 - 5.1);
                    }
                } else if (UmurValue == 3) {
                    if (BeratValue <= 5.8) {
                        Result = (BeratValue - 5.8) / (5.8 - 5.2);
                    } else {
                        Result = (BeratValue - 5.8) / (6.6 - 5.8);
                    }
                } else if (UmurValue == 4) {
                    if (BeratValue <= 6.4) {
                        Result = (BeratValue - 6.4) / (6.4 - 5.7);
                    } else {
                        Result = (BeratValue - 6.4) / (7.3 - 6.4);
                    }
                } else if (UmurValue == 5) {
                    if (BeratValue <= 6.9) {
                        Result = (BeratValue - 6.9) / (6.9 - 6.1);
                    } else {
                        Result = (BeratValue - 6.9) / (7.8 - 6.9);
                    }
                } else if (UmurValue == 6) {
                    if (BeratValue <= 7.3) {
                        Result = (BeratValue - 7.3) / (7.3 - 6.5);
                    } else {
                        Result = (BeratValue - 7.3) / (8.2 - 7.3);
                    }
                } else if (UmurValue == 7) {
                    if (BeratValue <= 7.6) {
                        Result = (BeratValue - 7.6) / (7.6 - 6.8);
                    } else {
                        Result = (BeratValue - 7.6) / (8.6 - 7.6);
                    }
                } else if (UmurValue == 8) {
                    if (BeratValue <= 7.9) {
                        Result = (BeratValue - 7.9) / (7.9 - 7.0);
                    } else {
                        Result = (BeratValue - 7.9) / (9.0 - 7.9);
                    }
                } else if (UmurValue == 9) {
                    if (BeratValue <= 8.2) {
                        Result = (BeratValue - 8.2) / (8.2 - 7.3);
                    } else {
                        Result = (BeratValue - 8.2) / (9.3 - 8.2);
                    }
                } else if (UmurValue == 10) {
                    if (BeratValue <= 8.5) {
                        Result = (BeratValue - 8.5) / (8.5 - 7.5);
                    } else {
                        Result = (BeratValue - 8.5) / (9.6 - 8.5);
                    }
                } else if (UmurValue == 11) {
                    if (BeratValue <= 8.7) {
                        Result = (BeratValue - 8.7) / (8.7 - 7.7);
                    } else {
                        Result = (BeratValue - 8.7) / (9.9 - 8.7);
                    }
                } else if (UmurValue == 12) {
                    if (BeratValue <= 8.9) {
                        Result = (BeratValue - 8.9) / (8.9 - 7.9);
                    } else {
                        Result = (BeratValue - 8.9) / (10.1 - 8.9);
                    }
                } else if (UmurValue == 13) {
                    if (BeratValue <= 9.2) {
                        Result = (BeratValue - 9.2) / (9.2 - 8.1);
                    } else {
                        Result = (BeratValue - 9.2) / (10.4 - 9.2);
                    }
                } else if (UmurValue == 14) {
                    if (BeratValue <= 9.4) {
                        Result = (BeratValue - 9.4) / (9.4 - 8.3);
                    } else {
                        Result = (BeratValue - 9.4) / (10.6 - 9.4);
                    }
                } else if (UmurValue == 15) {
                    if (BeratValue <= 9.6) {
                        Result = (BeratValue - 9.6) / (9.6 - 8.3);
                    } else {
                        Result = (BeratValue - 9.6) / (10.6 - 9.6);
                    }
                } else if (UmurValue == 16) {
                    if (BeratValue <= 9.8) {
                        Result = (BeratValue - 9.8) / (9.8 - 8.7);
                    } else {
                        Result = (BeratValue - 9.8) / (11.1 - 9.8);
                    }
                } else if (UmurValue == 17) {
                    if (BeratValue <= 10.0) {
                        Result = (BeratValue - 10.0) / (10.0 - 8.9);
                    } else {
                        Result = (BeratValue - 10.0) / (11.4 - 10.0);
                    }
                } else if (UmurValue == 18) {
                    if (BeratValue <= 10.2) {
                        Result = (BeratValue - 10.2) / (10.2 - 9.1);
                    } else {
                        Result = (BeratValue - 10.2) / (11.6 - 10.2);
                    }
                } else if (UmurValue == 19) {
                    if (BeratValue <= 10.4) {
                        Result = (BeratValue - 10.4) / (10.4 - 9.2);
                    } else {
                        Result = (BeratValue - 10.4) / (11.8 - 10.4);
                    }
                } else if (UmurValue == 20) {
                    if (BeratValue <= 10.6) {
                        Result = (BeratValue - 10.6) / (10.6 - 9.4);
                    } else {
                        Result = (BeratValue - 10.6) / (12.1 - 10.6);
                    }
                } else if (UmurValue == 21) {
                    if (BeratValue <= 10.9) {
                        Result = (BeratValue - 10.9) / (10.9 - 9.6);
                    } else {
                        Result = (BeratValue - 10.9) / (12.3 - 10.9);
                    }
                } else if (UmurValue == 22) {
                    if (BeratValue <= 11.1) {
                        Result = (BeratValue - 11.1) / (11.1 - 9.8);
                    } else {
                        Result = (BeratValue - 11.1) / (12.5 - 11.1);
                    }
                } else if (UmurValue == 23) {
                    if (BeratValue <= 11.3) {
                        Result = (BeratValue - 11.3) / (11.3 - 10.0);

                    } else {
                        Result = (BeratValue - 11.3) / (12.8 - 11.3);
                    }
                } else if (UmurValue == 24) {
                    if (BeratValue <= 11.5) {
                        Result = (BeratValue - 11.5) / (11.5 - 10.2);
                    } else {
                        Result = (BeratValue - 11.5) / (13.0 - 11.5);
                    }
                } else if (UmurValue == 25) {
                    if (BeratValue <= 11.7) {
                        Result = (BeratValue - 11.7) / (11.7 - 10.3);
                    } else {
                        Result = (BeratValue - 11.7) / (13.3 - 11.7);
                    }
                } else if (UmurValue == 26) {
                    if (BeratValue <= 11.9) {
                        Result = (BeratValue - 11.9) / (11.9 - 10.3);
                    } else {
                        Result = (BeratValue - 11.9) / (13.3 - 11.9);
                    }
                } else if (UmurValue == 27) {
                    if (BeratValue <= 12.1) {
                        Result = (BeratValue - 12.1) / (12.1 - 10.7);
                    } else {
                        Result = (BeratValue - 12.1) / (13.7 - 12.1);
                    }
                } else if (UmurValue == 28) {
                    if (BeratValue <= 12.3) {
                        Result = (BeratValue - 12.3) / (12.3 - 10.9);
                    } else {
                        Result = (BeratValue - 12.3) / (14.0 - 12.3);
                    }
                } else if (UmurValue == 29) {
                    if (BeratValue <= 12.5) {
                        Result = (BeratValue - 12.5) / (12.5 - 11.1);
                    } else {
                        Result = (BeratValue - 12.5) / (14.2 - 12.5);
                    }
                } else if (UmurValue == 30) {
                    if (BeratValue <= 12.7) {
                        Result = (BeratValue - 12.7) / (12.7 - 11.2);
                    } else {
                        Result = (BeratValue - 12.7) / (14.4 - 12.7);
                    }
                } else if (UmurValue == 31) {
                    if (BeratValue <= 12.9) {
                        Result = (BeratValue - 12.9) / (12.9 - 11.4);
                    } else {
                        Result = (BeratValue - 12.9) / (14.7 - 12.9);
                    }
                } else if (UmurValue == 32) {
                    if (BeratValue <= 13.1) {
                        Result = (BeratValue - 13.1) / (13.1 - 11.6);
                    } else {
                        Result = (BeratValue - 13.1) / (14.9 - 13.1);
                    }
                } else if (UmurValue == 33) {
                    if (BeratValue <= 13.3) {
                        Result = (BeratValue - 13.3) / (13.3 - 11.7);
                    } else {
                        Result = (BeratValue - 13.3) / (15.1 - 13.3);
                    }
                } else if (UmurValue == 34) {
                    if (BeratValue <= 13.5) {
                        Result = (BeratValue - 13.5) / (13.5 - 11.9);
                    } else {
                        Result = (BeratValue - 13.5) / (15.4 - 13.5);
                    }
                } else if (UmurValue == 35) {
                    if (BeratValue <= 13.7) {
                        Result = (BeratValue - 13.7) / (13.7 - 12.0);
                    } else {
                        Result = (BeratValue - 13.7) / (15.6 - 13.7);
                    }
                } else if (UmurValue == 36) {
                    if (BeratValue <= 13.9) {
                        Result = (BeratValue - 13.9) / (13.9 - 12.2);
                    } else {
                        Result = (BeratValue - 13.9) / (15.8 - 13.9);
                    }
                } else if (UmurValue == 37) {
                    if (BeratValue <= 14.0) {
                        Result = (BeratValue - 14.0) / (14.0 - 12.4);
                    } else {
                        Result = (BeratValue - 14.0) / (16.0 - 14.0);
                    }
                } else if (UmurValue == 38) {
                    if (BeratValue <= 14.2) {
                        Result = (BeratValue - 14.2) / (14.2 - 12.5);
                    } else {
                        Result = (BeratValue - 14.2) / (16.3 - 14.2);
                    }
                } else if (UmurValue == 39) {
                    if (BeratValue <= 14.4) {
                        Result = (BeratValue - 14.4) / (14.4 - 12.7);
                    } else {
                        Result = (BeratValue - 14.4) / (16.5 - 14.4);
                    }
                } else if (UmurValue == 40) {
                    if (BeratValue <= 14.6) {
                        Result = (BeratValue - 14.6) / (14.6 - 12.8);
                    } else {
                        Result = (BeratValue - 14.6) / (16.7 - 14.6);
                    }
                } else if (UmurValue == 41) {
                    if (BeratValue <= 14.8) {
                        Result = (BeratValue - 14.8) / (14.8 - 13.0);
                    } else {
                        Result = (BeratValue - 14.8) / (16.9 - 14.8);
                    }
                } else if (UmurValue == 42) {
                    if (BeratValue <= 15.0) {
                        Result = (BeratValue - 15.0) / (15.0 - 13.1);
                    } else {
                        Result = (BeratValue - 15.0) / (17.2 - 15.0);
                    }
                } else if (UmurValue == 43) {
                    if (BeratValue <= 15.2) {
                        Result = (BeratValue - 15.2) / (15.2 - 13.3);
                    } else {
                        Result = (BeratValue - 15.2) / (17.4 - 15.2);
                    }
                } else if (UmurValue == 44) {
                    if (BeratValue <= 15.3) {
                        Result = (BeratValue - 15.3) / (15.3 - 13.4);
                    } else {
                        Result = (BeratValue - 15.3) / (17.6 - 15.3);
                    }
                } else if (UmurValue == 45) {
                    if (BeratValue <= 15.5) {
                        Result = (BeratValue - 15.5) / (15.5 - 13.6);
                    } else {
                        Result = (BeratValue - 15.5) / (17.8 - 15.5);
                    }
                } else if (UmurValue == 46) {
                    if (BeratValue <= 15.7) {
                        Result = (BeratValue - 15.7) / (15.7 - 13.7);
                    } else {
                        Result = (BeratValue - 15.7) / (18.1 - 15.7);
                    }
                } else if (UmurValue == 47) {
                    if (BeratValue <= 15.9) {
                        Result = (BeratValue - 15.9) / (15.9 - 13.9);
                    } else {
                        Result = (BeratValue - 15.9) / (18.3 - 15.9);
                    }
                } else if (UmurValue == 48) {
                    if (BeratValue <= 16.1) {
                        Result = (BeratValue - 16.1) / (16.1 - 14.0);
                    } else {
                        Result = (BeratValue - 16.1) / (18.5 - 16.1);
                    }
                } else if (UmurValue == 49) {
                    if (BeratValue <= 16.3) {
                        Result = (BeratValue - 16.3) / (16.3 - 14.2);
                    } else {
                        Result = (BeratValue - 16.3) / (18.8 - 16.3);
                    }
                } else if (UmurValue == 50) {
                    if (BeratValue <= 16.4) {
                        Result = (BeratValue - 16.4) / (16.4 - 14.3);
                    } else {
                        Result = (BeratValue - 16.4) / (19.0 - 16.4);
                    }
                } else if (UmurValue == 51) {
                    if (BeratValue <= 16.6) {
                        Result = (BeratValue - 16.6) / (16.6 - 14.5);
                    } else {
                        Result = (BeratValue - 16.6) / (19.2 - 16.6);
                    }
                } else if (UmurValue == 52) {
                    if (BeratValue <= 16.8) {
                        Result = (BeratValue - 16.8) / (16.8 - 14.6);
                    } else {
                        Result = (BeratValue - 16.8) / (19.4 - 16.8);
                    }
                } else if (UmurValue == 53) {
                    if (BeratValue <= 17.0) {
                        Result = (BeratValue - 17.0) / (17.0 - 14.8);
                    } else {
                        Result = (BeratValue - 17.0) / (19.7 - 17.0);
                    }
                } else if (UmurValue == 54) {
                    if (BeratValue <= 17.2) {
                        Result = (BeratValue - 17.2) / (17.2 - 14.9);
                    } else {
                        Result = (BeratValue - 17.2) / (19.9 - 17.2);
                    }
                } else if (UmurValue == 55) {
                    if (BeratValue <= 17.3) {
                        Result = (BeratValue - 17.3) / (17.3 - 15.1);
                    } else {
                        Result = (BeratValue - 17.3) / (20.1 - 17.3);
                    }
                } else if (UmurValue == 56) {
                    if (BeratValue <= 17.5) {
                        Result = (BeratValue - 17.5) / (17.5 - 15.2);
                    } else {
                        Result = (BeratValue - 17.5) / (20.3 - 17.5);
                    }
                } else if (UmurValue == 57) {
                    if (BeratValue <= 17.7) {
                        Result = (BeratValue - 17.7) / (17.7 - 15.3);
                    } else {
                        Result = (BeratValue - 17.7) / (20.6 - 17.7);
                    }
                } else if (UmurValue == 58) {
                    if (BeratValue <= 17.9) {
                        Result = (BeratValue - 17.9) / (17.9 - 15.5);
                    } else {
                        Result = (BeratValue - 17.9) / (20.8 - 17.9);
                    }
                } else if (UmurValue == 59) {
                    if (BeratValue <= 18.0) {
                        Result = (BeratValue - 18.0) / (18.0 - 15.6);
                    } else {
                        Result = (BeratValue - 18.0) / (21.0 - 18.0);
                    }
                } else if (UmurValue == 60) {
                    if (BeratValue <= 18.2) {
                        Result = (BeratValue - 18.2) / (18.2 - 15.8);
                    } else {
                        Result = (BeratValue - 18.2) / (21.2 - 18.2);
                    }
                }
            }
            childScoreBBU.setText(String.format(Locale.ENGLISH, "%.1f", Result));
            if (Result < -3) {
                childStatusBBU.setText("Gizi Buruk");
                childStatusBBU.setTextColor(Color.parseColor("#F4511E"));
                childScoreBBU.setTextColor(Color.parseColor("#F4511E"));
            } else if (Result >= -3 && Result < -2) {
                childStatusBBU.setText("Gizi Kurang");
                childStatusBBU.setTextColor(Color.parseColor("#000000"));
                childScoreBBU.setTextColor(Color.parseColor("#000000"));
            } else if (Result >= -2 && Result <= 2) {
                childStatusBBU.setText("Gizi Baik");
                childStatusBBU.setTextColor(Color.parseColor("#000000"));
                childScoreBBU.setTextColor(Color.parseColor("#000000"));
            } else if (Result > 2) {
                childStatusBBU.setText("Gizi Lebih");
                childStatusBBU.setTextColor(Color.parseColor("#F4511E"));
                childScoreBBU.setTextColor(Color.parseColor("#F4511E"));
            }
        }
    }

    private void saveBBU() {
        String Score = String.valueOf(childScoreBBU.getText().toString());
        String Status = String.valueOf(childStatusBBU.getText().toString());
        String Berat = childWeightCountBBU.getText().toString();
        final String UmurString = childMonthCountBBU.getText().toString();
        Integer Umur = Integer.parseInt(UmurString);
        String DateSave = FormatDate;

        final String id = mDatabase.push().getKey();
        final BeratBadanUmur beratBadanUmur =
                new BeratBadanUmur(id, Score, Status, Berat, Umur, DateSave);

        showProgressDialog();
        mDatabase.orderByChild("bbuAge").equalTo(Umur)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hideProgressDialog();
                        if (!dataSnapshot.exists()) {
                            mDatabase.child(id).setValue(beratBadanUmur);
                            Snackbar.make(BBUCount.this.findViewById(android.R.id.content),
                                    "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(BBUCount.this.findViewById(android.R.id.content),
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
                            Snackbar.make(BBUCount.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        saveBBU();
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
        if (i == R.id.ButtonCountBBU) {
            countBBU();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception ignored) {

            }
        } else if (i == R.id.ButtonSaveBBU) {
            saveDialog();
        }
    }
}