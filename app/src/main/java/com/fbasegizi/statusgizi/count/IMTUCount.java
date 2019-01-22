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
import com.fbasegizi.statusgizi.model.IndeksMassaTubuhUmur;
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

public class IMTUCount extends BaseActivity implements View.OnClickListener {

    private TextInputLayout fieldWeight;
    private TextInputLayout fieldHeight;
    private TextInputLayout fieldAge;

    private TextView textViewNameCountIMTU;
    private TextView childScoreIMTU;
    private TextView childStatusIMTU;

    private EditText childDateCountIMTU;
    private EditText childGenderCountIMTU;
    private EditText childMonthCountIMTU;
    private EditText childWeightCountIMTU;
    private EditText childHeightCountIMTU;

    private Button buttonCountIMTU;
    private Button buttonSaveIMTU;

    private String Tanggal, FormatDate;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_imtu);

        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference("IndeksMassaTubuhUmur")
                .child(getUid()).child(intent.getStringExtra("id"));

        textViewNameCountIMTU = findViewById(R.id.textViewNameCountIMTU);
        childDateCountIMTU = findViewById(R.id.ChildDateCountIMTU);
        childGenderCountIMTU = findViewById(R.id.ChildGenderCountIMTU);
        childMonthCountIMTU = findViewById(R.id.ChildMonthCountIMTU);
        childWeightCountIMTU = findViewById(R.id.ChildWeightCountIMTU);
        childHeightCountIMTU = findViewById(R.id.ChildHeightCountIMTU);
        buttonCountIMTU = findViewById(R.id.ButtonCountIMTU);
        childScoreIMTU = findViewById(R.id.ChildScoreIMTU);
        childStatusIMTU = findViewById(R.id.ChildStatusIMTU);
        buttonSaveIMTU = findViewById(R.id.ButtonSaveIMTU);
        fieldWeight = findViewById(R.id.WeightFieldIMTU);
        fieldHeight = findViewById(R.id.HeightFieldIMTU);
        fieldAge = findViewById(R.id.AgeFieldIMTU);

        buttonCountIMTU.setOnClickListener(this);
        buttonSaveIMTU.setOnClickListener(this);

        buttonSaveIMTU.setVisibility(View.GONE);

        textViewNameCountIMTU.setText(intent.getStringExtra("nama"));
        childGenderCountIMTU.setText(intent.getStringExtra("gender"));
        Tanggal = intent.getStringExtra("tanggal");
        childDateCountIMTU.setText(Tanggal);

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
        childMonthCountIMTU.setText(String.valueOf(months));
        //Get current date
        Date c = Calendar.getInstance().getTime();
        FormatDate = sdf.format(c);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Indeks Massa Tubuh");
        }
    }

    private void countIMTU() {
        String Gender = childGenderCountIMTU.getText().toString();
        String Tinggi = childHeightCountIMTU.getText().toString();
        String Berat = childWeightCountIMTU.getText().toString();
        String Umur = childMonthCountIMTU.getText().toString();

        Double BeratValue = !Berat.equals("") ? Double.parseDouble(Berat) : 0;
        Integer UmurValue = !Umur.equals("") ? Integer.parseInt(Umur) : 0;
        Double TinggiValue = !Tinggi.equals("") ? (Double.parseDouble(Tinggi) / 100) : 0;

        Double IMT = BeratValue / (Math.pow(TinggiValue, 2));
        Double Result = 0.0;

        if (Berat.isEmpty()) {
            fieldWeight.setError("Masukkan Berat Badan");
        } else if (BeratValue < 1) {
            fieldWeight.setError("Berat badan minimal 1 Kg!");
        } else if (BeratValue > 50) {
            fieldWeight.setError("Berat badan maksimal 50 Kg!");
        } else if (Tinggi.isEmpty()) {
            fieldHeight.setError("Masukkan Tinggi Badan");
        } else if (TinggiValue < 0.25) {
            fieldHeight.setError("Tinggi badan minimal 25 Cm!");
        } else if (TinggiValue > 1.5) {
            fieldHeight.setError("Tinggi badan maksimal 150 Cm!");
        } else if (Umur.isEmpty()) {
            fieldAge.setError("Masukkan umur");
        } else {
            fieldWeight.setErrorEnabled(false);
            fieldHeight.setErrorEnabled(false);
            fieldAge.setErrorEnabled(false);
            buttonSaveIMTU.setVisibility(View.VISIBLE);
            if (Gender.equals("Laki-laki")) {
                if (UmurValue == 0) {
                    if (IMT <= 13.4) {
                        Result = (IMT - 13.4) / (13.4 - 12.2);
                    } else {
                        Result = (IMT - 13.4) / (14.8 - 13.4);
                    }
                } else if (UmurValue == 1) {
                    if (IMT <= 14.9) {
                        Result = (IMT - 14.9) / (14.9 - 13.6);
                    } else {
                        Result = (IMT - 14.9) / (16.3 - 14.9);
                    }
                } else if (UmurValue == 2) {
                    if (IMT <= 16.3) {
                        Result = (IMT - 16.3) / (16.3 - 15.0);
                    } else {
                        Result = (IMT - 16.3) / (17.8 - 16.3);
                    }
                } else if (UmurValue == 3) {
                    if (IMT <= 16.9) {
                        Result = (IMT - 16.9) / (16.9 - 15.5);
                    } else {
                        Result = (IMT - 16.9) / (18.4 - 16.9);
                    }
                } else if (UmurValue == 4) {
                    if (IMT <= 17.2) {
                        Result = (IMT - 17.2) / (17.2 - 15.8);
                    } else {
                        Result = (IMT - 17.2) / (18.7 - 17.2);
                    }
                } else if (UmurValue == 5) {
                    if (IMT <= 17.3) {
                        Result = (IMT - 17.3) / (17.3 - 15.9);
                    } else {
                        Result = (IMT - 17.3) / (18.8 - 17.3);
                    }
                } else if (UmurValue == 6) {
                    if (IMT <= 17.3) {
                        Result = (IMT - 17.3) / (17.3 - 16.0);
                    } else {
                        Result = (IMT - 17.3) / (18.8 - 17.3);
                    }
                } else if (UmurValue == 7) {
                    if (IMT <= 17.3) {
                        Result = (IMT - 17.3) / (17.3 - 16.0);
                    } else {
                        Result = (IMT - 17.3) / (18.8 - 17.3);
                    }
                } else if (UmurValue == 8) {
                    if (IMT <= 17.3) {
                        Result = (IMT - 17.3) / (17.3 - 15.9);
                    } else {
                        Result = (IMT - 17.3) / (18.7 - 17.3);
                    }
                } else if (UmurValue == 9) {
                    if (IMT <= 17.2) {
                        Result = (IMT - 17.2) / (17.2 - 15.8);
                    } else {
                        Result = (IMT - 17.2) / (18.6 - 17.2);
                    }
                } else if (UmurValue == 10) {
                    if (IMT <= 17.0) {
                        Result = (IMT - 17.0) / (17.0 - 15.7);
                    } else {
                        Result = (IMT - 17.0) / (18.5 - 17.0);
                    }
                } else if (UmurValue == 11) {
                    if (IMT <= 16.9) {
                        Result = (IMT - 16.9) / (16.9 - 15.6);
                    } else {
                        Result = (IMT - 16.9) / (18.4 - 16.9);
                    }
                } else if (UmurValue == 12) {
                    if (IMT <= 16.8) {
                        Result = (IMT - 16.8) / (16.8 - 15.5);
                    } else {
                        Result = (IMT - 16.8) / (18.2 - 16.8);
                    }
                } else if (UmurValue == 13) {
                    if (IMT <= 16.7) {
                        Result = (IMT - 16.7) / (16.7 - 15.4);
                    } else {
                        Result = (IMT - 16.7) / (18.1 - 16.7);
                    }
                } else if (UmurValue == 14) {
                    if (IMT <= 16.6) {
                        Result = (IMT - 16.6) / (16.6 - 15.3);
                    } else {
                        Result = (IMT - 16.6) / (18.0 - 16.6);
                    }
                } else if (UmurValue == 15) {
                    if (IMT <= 16.4) {
                        Result = (IMT - 16.4) / (16.4 - 15.2);
                    } else {
                        Result = (IMT - 16.4) / (17.8 - 16.4);
                    }
                } else if (UmurValue == 16) {
                    if (IMT <= 16.3) {
                        Result = (IMT - 16.3) / (16.3 - 15.1);
                    } else {
                        Result = (IMT - 16.3) / (17.7 - 16.3);
                    }
                } else if (UmurValue == 17) {
                    if (IMT <= 16.2) {
                        Result = (IMT - 16.2) / (16.2 - 15.0);
                    } else {
                        Result = (IMT - 16.2) / (17.6 - 16.2);
                    }
                } else if (UmurValue == 18) {
                    if (IMT <= 16.1) {
                        Result = (IMT - 16.1) / (16.1 - 14.9);
                    } else {
                        Result = (IMT - 16.1) / (17.5 - 16.1);
                    }
                } else if (UmurValue == 19) {
                    if (IMT <= 16.1) {
                        Result = (IMT - 16.1) / (16.1 - 14.9);
                    } else {
                        Result = (IMT - 16.1) / (17.4 - 16.1);
                    }
                } else if (UmurValue == 20) {
                    if (IMT <= 16.0) {
                        Result = (IMT - 16.0) / (16.0 - 14.8);
                    } else {
                        Result = (IMT - 16.0) / (17.3 - 16.0);
                    }
                } else if (UmurValue == 21) {
                    if (IMT <= 15.9) {
                        Result = (IMT - 15.9) / (15.9 - 14.7);
                    } else {
                        Result = (IMT - 15.9) / (17.2 - 15.9);
                    }
                } else if (UmurValue == 22) {
                    if (IMT <= 15.8) {
                        Result = (IMT - 15.8) / (15.8 - 14.7);
                    } else {
                        Result = (IMT - 15.8) / (17.2 - 15.8);
                    }
                } else if (UmurValue == 23) {
                    if (IMT <= 15.8) {
                        Result = (IMT - 15.8) / (15.8 - 14.6);
                    } else {
                        Result = (IMT - 15.8) / (17.1 - 15.8);
                    }
                } else if (UmurValue == 24) {
                    if (IMT <= 15.7) {
                        Result = (IMT - 15.7) / (15.7 - 14.6);
                    } else {
                        Result = (IMT - 15.7) / (17.0 - 15.7);
                    }
                } else if (UmurValue == 25) {
                    if (IMT <= 16.0) {
                        Result = (IMT - 16.0) / (16.0 - 14.8);
                    } else {
                        Result = (IMT - 16.0) / (17.3 - 16.0);
                    }
                } else if (UmurValue == 26) {
                    if (IMT <= 15.9) {
                        Result = (IMT - 15.9) / (15.9 - 14.8);
                    } else {
                        Result = (IMT - 15.9) / (17.3 - 15.9);
                    }
                } else if (UmurValue == 27) {
                    if (IMT <= 15.9) {
                        Result = (IMT - 15.9) / (15.9 - 14.7);
                    } else {
                        Result = (IMT - 15.9) / (17.2 - 15.9);
                    }
                } else if (UmurValue == 28) {
                    if (IMT <= 15.9) {
                        Result = (IMT - 15.9) / (15.9 - 14.7);
                    } else {
                        Result = (IMT - 15.9) / (17.2 - 15.9);
                    }
                } else if (UmurValue == 29) {
                    if (IMT <= 15.8) {
                        Result = (IMT - 15.8) / (15.8 - 14.7);
                    } else {
                        Result = (IMT - 15.8) / (17.1 - 15.8);
                    }
                } else if (UmurValue == 30) {
                    if (IMT <= 15.8) {
                        Result = (IMT - 15.8) / (15.8 - 14.6);
                    } else {
                        Result = (IMT - 15.8) / (17.1 - 15.8);
                    }
                } else if (UmurValue == 31) {
                    if (IMT <= 15.8) {
                        Result = (IMT - 15.8) / (15.8 - 14.6);
                    } else {
                        Result = (IMT - 15.8) / (17.1 - 15.8);
                    }
                } else if (UmurValue == 32) {
                    if (IMT <= 15.7) {
                        Result = (IMT - 15.7) / (15.7 - 14.6);
                    } else {
                        Result = (IMT - 15.7) / (17.0 - 15.7);
                    }
                } else if (UmurValue == 33) {
                    if (IMT <= 15.7) {
                        Result = (IMT - 15.7) / (15.7 - 14.5);
                    } else {
                        Result = (IMT - 15.7) / (17.0 - 15.7);
                    }
                } else if (UmurValue == 34) {
                    if (IMT <= 15.7) {
                        Result = (IMT - 15.7) / (15.7 - 14.5);
                    } else {
                        Result = (IMT - 15.7) / (17.0 - 15.7);
                    }
                } else if (UmurValue == 35) {
                    if (IMT <= 15.6) {
                        Result = (IMT - 15.6) / (15.6 - 14.5);
                    } else {
                        Result = (IMT - 15.6) / (16.9 - 15.6);
                    }
                } else if (UmurValue == 36) {
                    if (IMT <= 15.6) {
                        Result = (IMT - 15.6) / (15.6 - 14.4);
                    } else {
                        Result = (IMT - 15.6) / (16.9 - 15.6);
                    }
                } else if (UmurValue == 37) {
                    if (IMT <= 15.6) {
                        Result = (IMT - 15.6) / (15.6 - 14.4);
                    } else {
                        Result = (IMT - 15.6) / (16.9 - 15.6);
                    }
                } else if (UmurValue == 38) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.4);
                    } else {
                        Result = (IMT - 15.5) / (16.8 - 15.5);
                    }
                } else if (UmurValue == 39) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.3);
                    } else {
                        Result = (IMT - 15.5) / (16.8 - 15.5);
                    }
                } else if (UmurValue == 40) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.3);
                    } else {
                        Result = (IMT - 15.5) / (16.8 - 15.5);
                    }
                } else if (UmurValue == 41) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.3);
                    } else {
                        Result = (IMT - 15.5) / (16.8 - 15.5);
                    }
                } else if (UmurValue == 42) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.3);
                    } else {
                        Result = (IMT - 15.4) / (16.8 - 15.5);
                    }
                } else if (UmurValue == 43) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.7 - 15.4);
                    }
                } else if (UmurValue == 44) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.7 - 15.4);
                    }
                } else if (UmurValue == 45) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.7 - 15.4);
                    }
                } else if (UmurValue == 46) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.7 - 15.4);
                    }
                } else if (UmurValue == 47) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.2);
                    } else {
                        Result = (IMT - 15.3) / (16.7 - 15.3);
                    }
                } else if (UmurValue == 48) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.7 - 15.3);
                    }
                } else if (UmurValue == 49) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.7 - 15.3);
                    }
                } else if (UmurValue == 50) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.7 - 15.3);
                    }
                } else if (UmurValue == 51) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.6 - 15.3);
                    }
                } else if (UmurValue == 52) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.6 - 15.3);
                    }
                } else if (UmurValue == 53) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.6 - 15.3);
                    }
                } else if (UmurValue == 54) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.0);
                    } else {
                        Result = (IMT - 15.3) / (16.6 - 15.3);
                    }
                } else if (UmurValue == 55) {
                    if (IMT <= 15.2) {
                        Result = (IMT - 15.2) / (15.2 - 14.0);
                    } else {
                        Result = (IMT - 15.2) / (16.6 - 15.2);
                    }
                } else if (UmurValue == 56) {
                    if (IMT <= 15.2) {
                        Result = (IMT - 15.2) / (15.2 - 14.0);
                    } else {
                        Result = (IMT - 15.2) / (16.6 - 15.2);
                    }
                } else if (UmurValue == 57) {
                    if (IMT <= 15.2) {
                        Result = (IMT - 15.2) / (15.2 - 14.0);
                    } else {
                        Result = (IMT - 15.2) / (16.6 - 15.2);
                    }
                } else if (UmurValue == 58) {
                    if (IMT <= 15.2) {
                        Result = (IMT - 15.2) / (15.2 - 14.0);
                    } else {
                        Result = (IMT - 15.2) / (16.6 - 15.2);
                    }
                } else if (UmurValue == 59) {
                    if (IMT <= 15.2) {
                        Result = (IMT - 15.2) / (15.2 - 14.0);
                    } else {
                        Result = (IMT - 15.2) / (16.6 - 15.2);
                    }
                } else if (UmurValue == 60) {
                    if (IMT <= 15.2) {
                        Result = (IMT - 15.2) / (15.2 - 14.0);
                    } else {
                        Result = (IMT - 15.2) / (16.6 - 15.2);
                    }
                }
            } else if (Gender.equals("Perempuan")) {
                if (UmurValue <= 1) {
                    if (IMT <= 14.6) {
                        Result = (IMT - 14.6) / (14.6 - 13.2);
                    } else {
                        Result = (IMT - 14.6) / (14.6 - 16.0);
                    }
                } else if (UmurValue == 2) {
                    if (IMT <= 15.8) {
                        Result = (IMT - 15.8) / (15.8 - 14.3);
                    } else {
                        Result = (IMT - 15.8) / (17.3 - 15.8);
                    }
                } else if (UmurValue == 3) {
                    if (IMT <= 16.4) {
                        Result = (IMT - 16.4) / (16.4 - 14.9);
                    } else {
                        Result = (IMT - 16.4) / (17.9 - 16.4);
                    }
                } else if (UmurValue == 4) {
                    if (IMT <= 16.7) {
                        Result = (IMT - 16.7) / (16.7 - 15.2);
                    } else {
                        Result = (IMT - 16.7) / (18.3 - 16.7);
                    }
                } else if (UmurValue == 5) {
                    if (IMT <= 16.8) {
                        Result = (IMT - 16.8) / (16.8 - 15.4);
                    } else {
                        Result = (IMT - 16.8) / (18.4 - 16.8);
                    }
                } else if (UmurValue == 6) {
                    if (IMT <= 16.9) {
                        Result = (IMT - 16.9) / (16.9 - 15.5);
                    } else {
                        Result = (IMT - 16.9) / (18.5 - 16.9);
                    }
                } else if (UmurValue == 7) {
                    if (IMT <= 16.9) {
                        Result = (IMT - 16.9) / (16.9 - 15.5);
                    } else {
                        Result = (IMT - 16.9) / (18.5 - 16.9);
                    }
                } else if (UmurValue == 8) {
                    if (IMT <= 16.8) {
                        Result = (IMT - 16.8) / (16.8 - 15.4);
                    } else {
                        Result = (IMT - 16.8) / (18.4 - 16.8);
                    }
                } else if (UmurValue == 9) {
                    if (IMT <= 16.7) {
                        Result = (IMT - 16.7) / (16.7 - 15.3);
                    } else {
                        Result = (IMT - 16.7) / (18.3 - 16.7);
                    }
                } else if (UmurValue == 10) {
                    if (IMT <= 16.6) {
                        Result = (IMT - 16.6) / (16.6 - 15.2);
                    } else {
                        Result = (IMT - 16.6) / (18.2 - 16.6);
                    }
                } else if (UmurValue == 11) {
                    if (IMT <= 16.5) {
                        Result = (IMT - 16.5) / (16.5 - 15.1);
                    } else {
                        Result = (IMT - 16.5) / (18.0 - 16.5);
                    }
                } else if (UmurValue == 12) {
                    if (IMT <= 16.4) {
                        Result = (IMT - 16.4) / (16.4 - 15.0);
                    } else {
                        Result = (IMT - 16.4) / (17.9 - 16.4);
                    }
                } else if (UmurValue == 13) {
                    if (IMT <= 16.2) {
                        Result = (IMT - 16.2) / (16.2 - 14.9);
                    } else {
                        Result = (IMT - 16.2) / (17.7 - 16.2);
                    }
                } else if (UmurValue == 14) {
                    if (IMT <= 16.1) {
                        Result = (IMT - 16.1) / (16.1 - 14.8);
                    } else {
                        Result = (IMT - 16.1) / (17.6 - 16.1);
                    }
                } else if (UmurValue == 15) {
                    if (IMT <= 16.0) {
                        Result = (IMT - 16.0) / (16.0 - 14.7);
                    } else {
                        Result = (IMT - 16.0) / (17.5 - 16.0);
                    }
                } else if (UmurValue == 16) {
                    if (IMT <= 15.9) {
                        Result = (IMT - 15.9) / (15.9 - 14.6);
                    } else {
                        Result = (IMT - 15.9) / (17.4 - 15.9);
                    }
                } else if (UmurValue == 17) {
                    if (IMT <= 15.8) {
                        Result = (IMT - 15.8) / (15.8 - 14.5);
                    } else {
                        Result = (IMT - 15.8) / (17.3 - 15.8);
                    }
                } else if (UmurValue == 18) {
                    if (IMT <= 15.7) {
                        Result = (IMT - 15.7) / (15.7 - 14.4);
                    } else {
                        Result = (IMT - 15.7) / (17.2 - 15.7);
                    }
                } else if (UmurValue == 19) {
                    if (IMT <= 15.7) {
                        Result = (IMT - 15.7) / (15.7 - 14.4);
                    } else {
                        Result = (IMT - 15.7) / (17.1 - 15.7);
                    }
                } else if (UmurValue == 20) {
                    if (IMT <= 15.6) {
                        Result = (IMT - 15.6) / (15.6 - 14.3);
                    } else {
                        Result = (IMT - 15.6) / (17.0 - 15.6);
                    }
                } else if (UmurValue == 21) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.3);
                    } else {
                        Result = (IMT - 15.5) / (17.0 - 15.5);
                    }
                } else if (UmurValue == 22) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.2);
                    } else {
                        Result = (IMT - 15.5) / (16.9 - 15.5);
                    }
                } else if (UmurValue == 23) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.9 - 15.4);
                    }
                } else if (UmurValue == 24) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.8 - 15.4);
                    }
                } else if (UmurValue == 25) {
                    if (IMT <= 15.7) {
                        Result = (IMT - 15.7) / (15.7 - 14.4);
                    } else {
                        Result = (IMT - 15.7) / (17.1 - 15.7);
                    }
                } else if (UmurValue == 26) {
                    if (IMT <= 15.6) {
                        Result = (IMT - 15.6) / (15.6 - 14.4);
                    } else {
                        Result = (IMT - 15.6) / (17.0 - 15.6);
                    }
                } else if (UmurValue == 27) {
                    if (IMT <= 15.6) {
                        Result = (IMT - 15.6) / (15.6 - 14.4);
                    } else {
                        Result = (IMT - 15.6) / (17.0 - 15.6);
                    }
                } else if (UmurValue == 28) {
                    if (IMT <= 15.6) {
                        Result = (IMT - 15.6) / (15.6 - 14.3);
                    } else {
                        Result = (IMT - 15.6) / (17.0 - 15.6);
                    }
                } else if (UmurValue == 29) {
                    if (IMT <= 15.6) {
                        Result = (IMT - 15.6) / (15.6 - 14.3);
                    } else {
                        Result = (IMT - 15.6) / (17.0 - 15.6);
                    }
                } else if (UmurValue == 30) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.3);
                    } else {
                        Result = (IMT - 15.5) / (16.9 - 15.5);
                    }
                } else if (UmurValue == 31) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.3);
                    } else {
                        Result = (IMT - 15.5) / (16.9 - 15.5);
                    }
                } else if (UmurValue == 32) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.3);
                    } else {
                        Result = (IMT - 15.5) / (16.9 - 15.5);
                    }
                } else if (UmurValue == 33) {
                    if (IMT <= 15.5) {
                        Result = (IMT - 15.5) / (15.5 - 14.3);
                    } else {
                        Result = (IMT - 15.5) / (16.9 - 15.5);
                    }
                } else if (UmurValue == 34) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.8 - 15.4);
                    }
                } else if (UmurValue == 35) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.8 - 15.4);
                    }
                } else if (UmurValue == 36) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.2);
                    } else {
                        Result = (IMT - 15.4) / (16.8 - 15.4);
                    }
                } else if (UmurValue == 37) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.1);
                    } else {
                        Result = (IMT - 15.4) / (16.8 - 15.4);
                    }
                } else if (UmurValue == 38) {
                    if (IMT <= 15.4) {
                        Result = (IMT - 15.4) / (15.4 - 14.1);
                    } else {
                        Result = (IMT - 15.4) / (16.8 - 15.4);
                    }
                } else if (UmurValue == 39) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 40) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 41) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.1);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 42) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.0);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 43) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.0);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 44) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.0);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 45) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.0);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 46) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.0);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 47) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.0);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 48) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 14.0);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 49) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 50) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 51) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 52) {
                    if (IMT <= 15.2) {
                        Result = (IMT - 15.2) / (15.2 - 13.9);
                    } else {
                        Result = (IMT - 15.2) / (16.8 - 15.2);
                    }
                } else if (UmurValue == 53) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 54) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 55) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 56) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.8 - 15.3);
                    }
                } else if (UmurValue == 57) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.9 - 15.3);
                    }
                } else if (UmurValue == 58) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.9 - 15.3);
                    }
                } else if (UmurValue == 59) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.9 - 15.3);
                    }
                } else if (UmurValue == 60) {
                    if (IMT <= 15.3) {
                        Result = (IMT - 15.3) / (15.3 - 13.9);
                    } else {
                        Result = (IMT - 15.3) / (16.9 - 15.3);
                    }
                }
            }
            childScoreIMTU.setText(String.format(Locale.ENGLISH, "%.1f", Result));
            if (Result < -3) {
                childStatusIMTU.setText("Sangat Kurus");
                childStatusIMTU.setTextColor(Color.parseColor("#F4511E"));
                childScoreIMTU.setTextColor(Color.parseColor("#F4511E"));
            } else if (Result >= -3 && Result < -2) {
                childStatusIMTU.setText("Kurus");
                childStatusIMTU.setTextColor(Color.parseColor("#000000"));
                childScoreIMTU.setTextColor(Color.parseColor("#000000"));
            } else if (Result >= -2 && Result <= 2) {
                childStatusIMTU.setText("Normal");
                childStatusIMTU.setTextColor(Color.parseColor("#000000"));
                childScoreIMTU.setTextColor(Color.parseColor("#000000"));
            } else if (Result > 2) {
                childStatusIMTU.setText("Gemuk");
                childStatusIMTU.setTextColor(Color.parseColor("#F4511E"));
                childScoreIMTU.setTextColor(Color.parseColor("#F4511E"));
            }
        }
    }

    private void saveIMTU() {
        String Score = String.valueOf(childScoreIMTU.getText().toString());
        String Status = String.valueOf(childStatusIMTU.getText().toString());
        String Berat = childWeightCountIMTU.getText().toString();
        String Tinggi = childHeightCountIMTU.getText().toString();
        final String UmurString = childMonthCountIMTU.getText().toString();
        Integer Umur = Integer.parseInt(UmurString);
        String DateSave = FormatDate;

        final String id = mDatabase.push().getKey();
        final IndeksMassaTubuhUmur indeksMassaTubuhUmur = new
                IndeksMassaTubuhUmur(id, Score, Status, Berat, Tinggi, Umur, DateSave);

        showProgressDialog();
        mDatabase.orderByChild("imtuAge").equalTo(Umur)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hideProgressDialog();
                        if (!dataSnapshot.exists()) {
                            mDatabase.child(id).setValue(indeksMassaTubuhUmur);
                            Snackbar.make(IMTUCount.this.findViewById(android.R.id.content),
                                    "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(IMTUCount.this.findViewById(android.R.id.content),
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
                            Snackbar.make(IMTUCount.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        saveIMTU();
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
        if (i == R.id.ButtonCountIMTU) {
            countIMTU();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

            } catch (Exception ignored) {

            }
        } else if (i == R.id.ButtonSaveIMTU) {
            saveDialog();
        }
    }
}
