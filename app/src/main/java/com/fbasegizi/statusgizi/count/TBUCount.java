package com.fbasegizi.statusgizi.count;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.TinggiBadanUmur;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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

public class TBUCount extends BaseActivity implements View.OnClickListener {

    private TextInputLayout fieldHeight;
    private TextInputLayout fieldAge;

    private TextView textViewNameCountTBU;
    private TextView childScoreTBU;
    private TextView childStatusTBU;

    private EditText childDateCountTBU;
    private EditText childGenderCountTBU;
    private EditText childMonthCountTBU;
    private EditText childHeightCountTBU;

    private Button buttonCountTBU;
    private Button buttonSaveTBU;

    private String Tanggal, FormatDate;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_tbu);

        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference("TinggiBadanUmur")
                .child(getUid()).child(intent.getStringExtra("id"));

        textViewNameCountTBU = findViewById(R.id.textViewNameCountTBU);
        childDateCountTBU = findViewById(R.id.ChildDateCountTBU);
        childGenderCountTBU = findViewById(R.id.ChildGenderCountTBU);
        childMonthCountTBU = findViewById(R.id.ChildMonthCountTBU);
        childHeightCountTBU = findViewById(R.id.ChildHeightCountTBU);
        buttonCountTBU = findViewById(R.id.ButtonCountTBU);
        childScoreTBU = findViewById(R.id.ChildScoreTBU);
        childStatusTBU = findViewById(R.id.ChildStatusTBU);
        buttonSaveTBU = findViewById(R.id.ButtonSaveTBU);
        fieldHeight = findViewById(R.id.HeightFieldTBU);
        fieldAge = findViewById(R.id.AgeFieldTBU);

        buttonCountTBU.setOnClickListener(this);
        buttonSaveTBU.setOnClickListener(this);

        buttonSaveTBU.setVisibility(View.GONE);

        textViewNameCountTBU.setText(intent.getStringExtra("nama"));
        childGenderCountTBU.setText(intent.getStringExtra("gender"));
        Tanggal = intent.getStringExtra("tanggal");
        childDateCountTBU.setText(Tanggal);

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
        childMonthCountTBU.setText(String.valueOf(months));
        //Get current date
        Date c = Calendar.getInstance().getTime();
        FormatDate = sdf.format(c);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Tinggi Badan Umur");
        }
    }

    private void countTBU() {
        String Gender = childGenderCountTBU.getText().toString();
        String Umur = childMonthCountTBU.getText().toString();
        String Tinggi = childHeightCountTBU.getText().toString();

        Double TinggiValue = !Tinggi.equals("") ? Double.parseDouble(Tinggi) : 0;
        Integer UmurValue = !Umur.equals("") ? Integer.parseInt(Umur) : 0;
        Double Result = 0.0;

        //Ganti dari sini
        if (Tinggi.isEmpty()) {
            fieldHeight.setError("Masukkan Tinggi Badan");
        } else if (TinggiValue < 25) {
            fieldHeight.setError("Tinggi badan minimal 25 Cm!");
        } else if (TinggiValue > 150) {
            fieldHeight.setError("Tinggi badan maksimal 150 Cm!");
        } else if (Umur.isEmpty()) {
            fieldAge.setError("Masukkan umur");
        } else {
            fieldHeight.setErrorEnabled(false);
            fieldAge.setErrorEnabled(false);
            buttonSaveTBU.setVisibility(View.VISIBLE);
            if (Gender.equals("Laki-laki")) {
                if (UmurValue == 0) {
                    if (TinggiValue <= 49.9) {
                        Result = (TinggiValue - 49.9) / (49.9 - 48.0);
                    } else {
                        Result = (TinggiValue - 49.9) / (51.8 - 49.9);
                    }
                } else if (UmurValue == 1) {
                    if (TinggiValue <= 54.7) {
                        Result = (TinggiValue - 54.7) / (54.7 - 52.8);
                    } else {
                        Result = (TinggiValue - 54.7) / (56.7 - 54.7);
                    }
                } else if (UmurValue == 2) {
                    if (TinggiValue <= 58.4) {
                        Result = (TinggiValue - 58.4) / (58.4 - 56.4);
                    } else {
                        Result = (TinggiValue - 58.4) / (60.4 - 58.4);
                    }
                } else if (UmurValue == 3) {
                    if (TinggiValue <= 61.4) {
                        Result = (TinggiValue - 61.4) / (61.4 - 59.4);
                    } else {
                        Result = (TinggiValue - 61.4) / (63.5 - 61.4);
                    }
                } else if (UmurValue == 4) {
                    if (TinggiValue <= 63.9) {
                        Result = (TinggiValue - 63.9) / (63.9 - 61.8);
                    } else {
                        Result = (TinggiValue - 63.9) / (66.0 - 63.9);
                    }
                } else if (UmurValue == 5) {
                    if (TinggiValue <= 65.9) {
                        Result = (TinggiValue - 65.9) / (65.9 - 63.8);
                    } else {
                        Result = (TinggiValue - 65.9) / (68.0 - 65.9);
                    }
                } else if (UmurValue == 6) {
                    if (TinggiValue <= 67.6) {
                        Result = (TinggiValue - 67.6) / (67.6 - 65.5);
                    } else {
                        Result = (TinggiValue - 67.6) / (69.8 - 67.6);
                    }
                } else if (UmurValue == 7) {
                    if (TinggiValue <= 69.2) {
                        Result = (TinggiValue - 69.2) / (69.2 - 67.0);
                    } else {
                        Result = (TinggiValue - 69.2) / (71.3 - 69.2);
                    }
                } else if (UmurValue == 8) {
                    if (TinggiValue <= 70.6) {
                        Result = (TinggiValue - 70.6) / (70.6 - 68.4);
                    } else {
                        Result = (TinggiValue - 70.6) / (72.8 - 70.6);
                    }
                } else if (UmurValue == 9) {
                    if (TinggiValue <= 72.0) {
                        Result = (TinggiValue - 72.0) / (72.0 - 69.7);
                    } else {
                        Result = (TinggiValue - 72.0) / (74.2 - 72.0);
                    }
                } else if (UmurValue == 10) {
                    if (TinggiValue <= 73.3) {
                        Result = (TinggiValue - 73.3) / (73.3 - 71.0);
                    } else {
                        Result = (TinggiValue - 73.3) / (75.6 - 73.3);
                    }
                } else if (UmurValue == 11) {
                    if (TinggiValue <= 74.5) {
                        Result = (TinggiValue - 74.5) / (74.5 - 72.2);
                    } else {
                        Result = (TinggiValue - 74.5) / (76.9 - 74.5);
                    }
                } else if (UmurValue == 12) {
                    if (TinggiValue <= 75.7) {
                        Result = (TinggiValue - 75.7) / (75.7 - 73.4);
                    } else {
                        Result = (TinggiValue - 75.7) / (78.1 - 75.7);
                    }
                } else if (UmurValue == 13) {
                    if (TinggiValue <= 76.9) {
                        Result = (TinggiValue - 76.9) / (76.9 - 74.5);
                    } else {
                        Result = (TinggiValue - 76.9) / (79.3 - 76.9);
                    }
                } else if (UmurValue == 14) {
                    if (TinggiValue <= 78.0) {
                        Result = (TinggiValue - 78.0) / (78.0 - 75.6);
                    } else {
                        Result = (TinggiValue - 78.0) / (80.5 - 78.0);
                    }
                } else if (UmurValue == 15) {
                    if (TinggiValue <= 79.1) {
                        Result = (TinggiValue - 79.1) / (79.1 - 76.6);
                    } else {
                        Result = (TinggiValue - 79.1) / (81.7 - 79.1);
                    }
                } else if (UmurValue == 16) {
                    if (TinggiValue <= 80.2) {
                        Result = (TinggiValue - 80.2) / (80.2 - 77.6);
                    } else {
                        Result = (TinggiValue - 80.2) / (82.8 - 80.2);
                    }
                } else if (UmurValue == 17) {
                    if (TinggiValue <= 81.2) {
                        Result = (TinggiValue - 81.2) / (81.2 - 78.6);
                    } else {
                        Result = (TinggiValue - 81.2) / (83.9 - 81.2);
                    }
                } else if (UmurValue == 18) {
                    if (TinggiValue <= 82.3) {
                        Result = (TinggiValue - 82.3) / (82.3 - 79.6);
                    } else {
                        Result = (TinggiValue - 82.3) / (85.0 - 82.3);
                    }
                } else if (UmurValue == 19) {
                    if (TinggiValue <= 83.2) {
                        Result = (TinggiValue - 83.2) / (83.2 - 80.5);
                    } else {
                        Result = (TinggiValue - 83.2) / (86.0 - 83.2);
                    }
                } else if (UmurValue == 20) {
                    if (TinggiValue <= 84.2) {
                        Result = (TinggiValue - 84.2) / (84.2 - 81.4);
                    } else {
                        Result = (TinggiValue - 84.2) / (87.0 - 84.2);
                    }
                } else if (UmurValue == 21) {
                    if (TinggiValue <= 85.1) {
                        Result = (TinggiValue - 85.1) / (85.1 - 82.3);
                    } else {
                        Result = (TinggiValue - 85.1) / (88.0 - 85.1);
                    }
                } else if (UmurValue == 22) {
                    if (TinggiValue <= 86.0) {
                        Result = (TinggiValue - 86.0) / (86.0 - 83.1);
                    } else {
                        Result = (TinggiValue - 86.0) / (89.0 - 86.0);
                    }
                } else if (UmurValue == 23) {
                    if (TinggiValue <= 86.9) {
                        Result = (TinggiValue - 86.9) / (86.9 - 83.9);
                    } else {
                        Result = (TinggiValue - 86.9) / (89.9 - 86.9);
                    }
                    //umur 24 diukur dengan kondisi anak terlentang
                } else if (UmurValue == 24) {
                    if (TinggiValue <= 87.8) {
                        Result = (TinggiValue - 87.8) / (87.8 - 84.8);
                    } else {
                        Result = (TinggiValue - 87.8) / (90.9 - 87.8);
                    }
                } else if (UmurValue == 25) {
                    if (TinggiValue <= 88.0) {
                        Result = (TinggiValue - 88.0) / (88.0 - 84.9);
                    } else {
                        Result = (TinggiValue - 88.0) / (91.1 - 88.0);
                    }
                } else if (UmurValue == 26) {
                    if (TinggiValue <= 88.8) {
                        Result = (TinggiValue - 88.8) / (88.8 - 85.6);
                    } else {
                        Result = (TinggiValue - 88.8) / (92.0 - 88.8);
                    }
                } else if (UmurValue == 27) {
                    if (TinggiValue <= 89.6) {
                        Result = (TinggiValue - 89.6) / (89.6 - 86.4);
                    } else {
                        Result = (TinggiValue - 89.6) / (92.9 - 89.6);
                    }
                } else if (UmurValue == 28) {
                    if (TinggiValue <= 90.4) {
                        Result = (TinggiValue - 90.4) / (90.4 - 87.1);
                    } else {
                        Result = (TinggiValue - 90.4) / (93.7 - 90.4);
                    }
                } else if (UmurValue == 29) {
                    if (TinggiValue <= 91.2) {
                        Result = (TinggiValue - 91.2) / (91.2 - 87.8);
                    } else {
                        Result = (TinggiValue - 91.2) / (94.5 - 91.2);
                    }
                } else if (UmurValue == 30) {
                    if (TinggiValue <= 91.9) {
                        Result = (TinggiValue - 91.9) / (91.9 - 88.5);
                    } else {
                        Result = (TinggiValue - 91.9) / (95.3 - 91.9);
                    }
                } else if (UmurValue == 31) {
                    if (TinggiValue <= 92.7) {
                        Result = (TinggiValue - 92.7) / (92.7 - 89.2);
                    } else {
                        Result = (TinggiValue - 92.7) / (96.1 - 92.7);
                    }
                } else if (UmurValue == 32) {
                    if (TinggiValue <= 93.4) {
                        Result = (TinggiValue - 93.4) / (93.4 - 89.9);
                    } else {
                        Result = (TinggiValue - 93.4) / (96.9 - 93.4);
                    }
                } else if (UmurValue == 33) {
                    if (TinggiValue <= 94.1) {
                        Result = (TinggiValue - 94.1) / (94.1 - 90.5);
                    } else {
                        Result = (TinggiValue - 94.1) / (97.6 - 94.1);
                    }
                } else if (UmurValue == 34) {
                    if (TinggiValue <= 94.8) {
                        Result = (TinggiValue - 94.8) / (94.8 - 91.1);
                    } else {
                        Result = (TinggiValue - 94.8) / (98.4 - 94.8);
                    }
                } else if (UmurValue == 35) {
                    if (TinggiValue <= 95.4) {
                        Result = (TinggiValue - 95.4) / (95.4 - 91.8);
                    } else {
                        Result = (TinggiValue - 95.4) / (99.1 - 95.4);
                    }
                } else if (UmurValue == 36) {
                    if (TinggiValue <= 96.1) {
                        Result = (TinggiValue - 96.1) / (96.1 - 92.4);
                    } else {
                        Result = (TinggiValue - 96.1) / (99.8 - 96.1);
                    }
                } else if (UmurValue == 37) {
                    if (TinggiValue <= 96.7) {
                        Result = (TinggiValue - 96.7) / (96.7 - 93.0);
                    } else {
                        Result = (TinggiValue - 96.7) / (100.5 - 96.7);
                    }
                } else if (UmurValue == 38) {
                    if (TinggiValue <= 97.4) {
                        Result = (TinggiValue - 97.4) / (97.4 - 93.6);
                    } else {
                        Result = (TinggiValue - 97.4) / (101.2 - 97.4);
                    }
                } else if (UmurValue == 39) {
                    if (TinggiValue <= 98.0) {
                        Result = (TinggiValue - 98.0) / (98.0 - 94.2);
                    } else {
                        Result = (TinggiValue - 98.0) / (101.8 - 98.0);
                    }
                } else if (UmurValue == 40) {
                    if (TinggiValue <= 98.6) {
                        Result = (TinggiValue - 98.6) / (98.6 - 94.7);
                    } else {
                        Result = (TinggiValue - 98.6) / (102.5 - 98.6);
                    }
                } else if (UmurValue == 41) {
                    if (TinggiValue <= 99.2) {
                        Result = (TinggiValue - 99.2) / (99.2 - 95.3);
                    } else {
                        Result = (TinggiValue - 99.2) / (103.2 - 99.2);
                    }
                } else if (UmurValue == 42) {
                    if (TinggiValue <= 99.9) {
                        Result = (TinggiValue - 99.9) / (99.9 - 95.9);
                    } else {
                        Result = (TinggiValue - 99.9) / (103.8 - 99.9);
                    }
                } else if (UmurValue == 43) {
                    if (TinggiValue <= 100.4) {
                        Result = (TinggiValue - 100.4) / (100.4 - 96.4);
                    } else {
                        Result = (TinggiValue - 100.4) / (104.5 - 100.4);
                    }
                } else if (UmurValue == 44) {
                    if (TinggiValue <= 101.0) {
                        Result = (TinggiValue - 101.0) / (101.0 - 97.0);
                    } else {
                        Result = (TinggiValue - 101.0) / (105.1 - 101.0);
                    }
                } else if (UmurValue == 45) {
                    if (TinggiValue <= 101.6) {
                        Result = (TinggiValue - 101.6) / (101.6 - 97.5);
                    } else {
                        Result = (TinggiValue - 101.6) / (105.7 - 101.6);
                    }
                } else if (UmurValue == 46) {
                    if (TinggiValue <= 102.2) {
                        Result = (TinggiValue - 102.2) / (102.2 - 98.1);
                    } else {
                        Result = (TinggiValue - 102.2) / (106.3 - 102.2);
                    }
                } else if (UmurValue == 47) {
                    if (TinggiValue <= 102.8) {
                        Result = (TinggiValue - 102.8) / (102.8 - 98.6);
                    } else {
                        Result = (TinggiValue - 102.8) / (106.9 - 102.8);
                    }
                } else if (UmurValue == 48) {
                    if (TinggiValue <= 103.3) {
                        Result = (TinggiValue - 103.3) / (103.3 - 99.1);
                    } else {
                        Result = (TinggiValue - 103.3) / (107.5 - 103.3);
                    }
                } else if (UmurValue == 49) {
                    if (TinggiValue <= 103.9) {
                        Result = (TinggiValue - 103.9) / (103.9 - 99.7);
                    } else {
                        Result = (TinggiValue - 103.9) / (108.1 - 103.9);
                    }
                } else if (UmurValue == 50) {
                    if (TinggiValue <= 104.4) {
                        Result = (TinggiValue - 104.4) / (104.4 - 100.2);
                    } else {
                        Result = (TinggiValue - 104.4) / (108.7 - 104.4);
                    }
                } else if (UmurValue == 51) {
                    if (TinggiValue <= 105.0) {
                        Result = (TinggiValue - 105.0) / (105.0 - 100.7);
                    } else {
                        Result = (TinggiValue - 105.0) / (109.3 - 105.0);
                    }
                } else if (UmurValue == 52) {
                    if (TinggiValue <= 105.6) {
                        Result = (TinggiValue - 105.6) / (105.6 - 101.2);
                    } else {
                        Result = (TinggiValue - 105.6) / (109.9 - 105.6);
                    }
                } else if (UmurValue == 53) {
                    if (TinggiValue <= 106.1) {
                        Result = (TinggiValue - 106.1) / (106.1 - 101.7);
                    } else {
                        Result = (TinggiValue - 106.1) / (110.5 - 106.1);
                    }
                } else if (UmurValue == 54) {
                    if (TinggiValue <= 106.7) {
                        Result = (TinggiValue - 106.7) / (106.7 - 102.3);
                    } else {
                        Result = (TinggiValue - 106.7) / (111.1 - 106.7);
                    }
                } else if (UmurValue == 55) {
                    if (TinggiValue <= 107.2) {
                        Result = (TinggiValue - 107.2) / (107.2 - 102.8);
                    } else {
                        Result = (TinggiValue - 107.2) / (111.7 - 107.2);
                    }
                } else if (UmurValue == 56) {
                    if (TinggiValue <= 107.8) {
                        Result = (TinggiValue - 107.8) / (107.8 - 103.3);
                    } else {
                        Result = (TinggiValue - 107.8) / (112.3 - 107.8);
                    }
                } else if (UmurValue == 57) {
                    if (TinggiValue <= 108.3) {
                        Result = (TinggiValue - 108.3) / (108.3 - 103.8);
                    } else {
                        Result = (TinggiValue - 108.3) / (112.8 - 108.3);
                    }
                } else if (UmurValue == 58) {
                    if (TinggiValue <= 108.9) {
                        Result = (TinggiValue - 108.9) / (108.9 - 104.3);
                    } else {
                        Result = (TinggiValue - 108.9) / (113.4 - 108.9);
                    }
                } else if (UmurValue == 59) {
                    if (TinggiValue <= 109.4) {
                        Result = (TinggiValue - 109.4) / (109.4 - 104.8);
                    } else {
                        Result = (TinggiValue - 109.4) / (114.0 - 109.4);
                    }
                } else if (UmurValue == 60) {
                    if (TinggiValue <= 110.0) {
                        Result = (TinggiValue - 110.0) / (110.0 - 105.3);
                    } else {
                        Result = (TinggiValue - 110.0) / (114.6 - 110.0);
                    }
                }
            } else if (Gender.equals("Perempuan")) {
                if (UmurValue == 0) {
                    if (TinggiValue <= 49.1) {
                        Result = (TinggiValue - 49.1) / (49.1 - 47.3);
                    } else {
                        Result = (TinggiValue - 49.1) / (51.0 - 49.1);
                    }
                } else if (UmurValue == 1) {
                    if (TinggiValue <= 53.7) {
                        Result = (TinggiValue - 53.7) / (53.7 - 51.7);
                    } else {
                        Result = (TinggiValue - 53.7) / (55.6 - 53.7);
                    }
                } else if (UmurValue == 2) {
                    if (TinggiValue <= 57.1) {
                        Result = (TinggiValue - 57.1) / (57.1 - 55.0);
                    } else {
                        Result = (TinggiValue - 57.1) / (59.1 - 57.1);
                    }
                } else if (UmurValue == 3) {
                    if (TinggiValue <= 59.8) {
                        Result = (TinggiValue - 59.8) / (59.8 - 57.7);
                    } else {
                        Result = (TinggiValue - 59.8) / (61.9 - 59.8);
                    }
                } else if (UmurValue == 4) {
                    if (TinggiValue <= 62.1) {
                        Result = (TinggiValue - 62.1) / (62.1 - 59.9);
                    } else {
                        Result = (TinggiValue - 62.1) / (64.3 - 62.1);
                    }
                } else if (UmurValue == 5) {
                    if (TinggiValue <= 64.0) {
                        Result = (TinggiValue - 64.0) / (64.0 - 61.8);
                    } else {
                        Result = (TinggiValue - 64.0) / (66.2 - 64.0);
                    }
                } else if (UmurValue == 6) {
                    if (TinggiValue <= 65.7) {
                        Result = (TinggiValue - 65.7) / (65.7 - 68.0);
                    } else {
                        Result = (TinggiValue - 65.7) / (63.5 - 65.7);
                    }
                } else if (UmurValue == 7) {
                    if (TinggiValue <= 67.3) {
                        Result = (TinggiValue - 67.3) / (67.3 - 65.0);
                    } else {
                        Result = (TinggiValue - 67.3) / (69.6 - 67.3);
                    }
                } else if (UmurValue == 8) {
                    if (TinggiValue <= 68.7) {
                        Result = (TinggiValue - 68.7) / (68.7 - 66.4);
                    } else {
                        Result = (TinggiValue - 68.7) / (71.1 - 68.7);
                    }
                } else if (UmurValue == 9) {
                    if (TinggiValue <= 70.1) {
                        Result = (TinggiValue - 70.1) / (70.1 - 67.7);
                    } else {
                        Result = (TinggiValue - 70.1) / (72.6 - 70.1);
                    }
                } else if (UmurValue == 10) {
                    if (TinggiValue <= 71.5) {
                        Result = (TinggiValue - 71.5) / (71.5 - 69.0);
                    } else {
                        Result = (TinggiValue - 71.5) / (73.9 - 71.5);
                    }
                } else if (UmurValue == 11) {
                    if (TinggiValue <= 72.8) {
                        Result = (TinggiValue - 72.8) / (72.8 - 70.3);
                    } else {
                        Result = (TinggiValue - 72.8) / (75.3 - 72.8);
                    }
                } else if (UmurValue == 12) {
                    if (TinggiValue <= 74.0) {
                        Result = (TinggiValue - 74.0) / (74.0 - 71.4);
                    } else {
                        Result = (TinggiValue - 74.0) / (76.6 - 74.0);
                    }
                } else if (UmurValue == 13) {
                    if (TinggiValue <= 75.2) {
                        Result = (TinggiValue - 75.2) / (75.2 - 72.6);
                    } else {
                        Result = (TinggiValue - 75.2) / (77.8 - 75.2);
                    }
                } else if (UmurValue == 14) {
                    if (TinggiValue <= 76.4) {
                        Result = (TinggiValue - 76.4) / (76.4 - 73.7);
                    } else {
                        Result = (TinggiValue - 76.4) / (79.1 - 76.4);
                    }
                } else if (UmurValue == 15) {
                    if (TinggiValue <= 77.5) {
                        Result = (TinggiValue - 77.5) / (77.5 - 74.8);
                    } else {
                        Result = (TinggiValue - 77.5) / (80.2 - 77.5);
                    }
                } else if (UmurValue == 16) {
                    if (TinggiValue <= 78.6) {
                        Result = (TinggiValue - 78.6) / (78.6 - 75.8);
                    } else {
                        Result = (TinggiValue - 78.6) / (81.4 - 78.6);
                    }
                } else if (UmurValue == 17) {
                    if (TinggiValue <= 79.7) {
                        Result = (TinggiValue - 79.7) / (79.7 - 76.8);
                    } else {
                        Result = (TinggiValue - 79.7) / (82.5 - 79.7);
                    }
                } else if (UmurValue == 18) {
                    if (TinggiValue <= 80.7) {
                        Result = (TinggiValue - 80.7) / (80.7 - 77.8);
                    } else {
                        Result = (TinggiValue - 80.7) / (83.6 - 80.7);
                    }
                } else if (UmurValue == 19) {
                    if (TinggiValue <= 81.7) {
                        Result = (TinggiValue - 81.7) / (81.7 - 78.8);
                    } else {
                        Result = (TinggiValue - 81.7) / (84.7 - 81.7);
                    }
                } else if (UmurValue == 20) {
                    if (TinggiValue <= 82.7) {
                        Result = (TinggiValue - 82.7) / (82.7 - 79.7);
                    } else {
                        Result = (TinggiValue - 82.7) / (85.7 - 82.7);
                    }
                } else if (UmurValue == 21) {
                    if (TinggiValue <= 83.7) {
                        Result = (TinggiValue - 83.7) / (83.7 - 80.6);
                    } else {
                        Result = (TinggiValue - 83.7) / (86.7 - 83.7);
                    }
                } else if (UmurValue == 22) {
                    if (TinggiValue <= 84.6) {
                        Result = (TinggiValue - 84.6) / (84.6 - 81.5);
                    } else {
                        Result = (TinggiValue - 84.6) / (87.7 - 84.6);
                    }
                } else if (UmurValue == 23) {
                    if (TinggiValue <= 85.5) {
                        Result = (TinggiValue - 85.5) / (85.5 - 82.3);
                    } else {
                        Result = (TinggiValue - 85.5) / (88.7 - 85.5);
                    }
                } else if (UmurValue == 24) {
                    if (TinggiValue <= 86.4) {
                        Result = (TinggiValue - 86.4) / (86.4 - 83.2);
                    } else {
                        Result = (TinggiValue - 86.4) / (89.6 - 86.4);
                    }
                } else if (UmurValue == 25) {
                    if (TinggiValue <= 86.6) {
                        Result = (TinggiValue - 86.6) / (86.6 - 83.3);
                    } else {
                        Result = (TinggiValue - 86.6) / (89.9 - 86.6);
                    }
                } else if (UmurValue == 26) {
                    if (TinggiValue <= 87.4) {
                        Result = (TinggiValue - 87.4) / (87.4 - 84.1);
                    } else {
                        Result = (TinggiValue - 87.4) / (90.8 - 87.4);
                    }
                } else if (UmurValue == 27) {
                    if (TinggiValue <= 88.3) {
                        Result = (TinggiValue - 88.3) / (88.3 - 84.9);
                    } else {
                        Result = (TinggiValue - 88.3) / (91.7 - 88.3);
                    }
                } else if (UmurValue == 28) {
                    if (TinggiValue <= 89.1) {
                        Result = (TinggiValue - 89.1) / (89.1 - 85.7);
                    } else {
                        Result = (TinggiValue - 89.1) / (92.5 - 89.1);
                    }
                } else if (UmurValue == 29) {
                    if (TinggiValue <= 89.9) {
                        Result = (TinggiValue - 89.9) / (89.9 - 93.4);
                    } else {
                        Result = (TinggiValue - 89.9) / (86.4 - 89.9);
                    }
                } else if (UmurValue == 30) {
                    if (TinggiValue <= 90.7) {
                        Result = (TinggiValue - 90.7) / (90.7 - 87.1);
                    } else {
                        Result = (TinggiValue - 90.7) / (94.2 - 90.7);
                    }
                } else if (UmurValue == 31) {
                    if (TinggiValue <= 91.4) {
                        Result = (TinggiValue - 91.4) / (91.4 - 87.9);
                    } else {
                        Result = (TinggiValue - 91.4) / (95.0 - 91.4);
                    }
                } else if (UmurValue == 32) {
                    if (TinggiValue <= 92.2) {
                        Result = (TinggiValue - 92.2) / (92.2 - 88.6);
                    } else {
                        Result = (TinggiValue - 92.2) / (95.8 - 92.2);
                    }
                } else if (UmurValue == 33) {
                    if (TinggiValue <= 92.9) {
                        Result = (TinggiValue - 92.9) / (92.9 - 89.3);
                    } else {
                        Result = (TinggiValue - 92.9) / (96.6 - 92.9);
                    }
                } else if (UmurValue == 34) {
                    if (TinggiValue <= 93.6) {
                        Result = (TinggiValue - 93.6) / (93.6 - 89.9);
                    } else {
                        Result = (TinggiValue - 93.6) / (97.4 - 93.6);
                    }
                } else if (UmurValue == 35) {
                    if (TinggiValue <= 94.4) {
                        Result = (TinggiValue - 94.4) / (94.4 - 90.6);
                    } else {
                        Result = (TinggiValue - 94.4) / (98.1 - 94.4);
                    }
                } else if (UmurValue == 36) {
                    if (TinggiValue <= 95.1) {
                        Result = (TinggiValue - 95.1) / (95.1 - 91.2);
                    } else {
                        Result = (TinggiValue - 95.1) / (98.9 - 95.1);
                    }
                } else if (UmurValue == 37) {
                    if (TinggiValue <= 95.7) {
                        Result = (TinggiValue - 95.7) / (95.7 - 91.9);
                    } else {
                        Result = (TinggiValue - 95.7) / (99.6 - 95.7);
                    }
                } else if (UmurValue == 38) {
                    if (TinggiValue <= 96.4) {
                        Result = (TinggiValue - 96.4) / (96.4 - 90.6);
                    } else {
                        Result = (TinggiValue - 96.4) / (100.3 - 96.4);
                    }
                } else if (UmurValue == 39) {
                    if (TinggiValue <= 97.1) {
                        Result = (TinggiValue - 97.1) / (97.1 - 93.1);
                    } else {
                        Result = (TinggiValue - 97.1) / (101.0 - 97.1);
                    }
                } else if (UmurValue == 40) {
                    if (TinggiValue <= 97.7) {
                        Result = (TinggiValue - 97.7) / (97.7 - 93.8);
                    } else {
                        Result = (TinggiValue - 97.7) / (101.7 - 97.7);
                    }
                } else if (UmurValue == 41) {
                    if (TinggiValue <= 98.4) {
                        Result = (TinggiValue - 98.4) / (98.4 - 94.4);
                    } else {
                        Result = (TinggiValue - 98.4) / (102.4 - 98.4);
                    }
                } else if (UmurValue == 42) {
                    if (TinggiValue <= 99.0) {
                        Result = (TinggiValue - 99.0) / (99.0 - 95.0);
                    } else {
                        Result = (TinggiValue - 99.0) / (103.1 - 99.0);
                    }
                } else if (UmurValue == 43) {
                    if (TinggiValue <= 99.7) {
                        Result = (TinggiValue - 99.7) / (99.7 - 95.6);
                    } else {
                        Result = (TinggiValue - 99.7) / (103.8 - 99.7);
                    }
                } else if (UmurValue == 44) {
                    if (TinggiValue <= 100.3) {
                        Result = (TinggiValue - 100.3) / (100.3 - 96.2);
                    } else {
                        Result = (TinggiValue - 100.3) / (104.5 - 100.3);
                    }
                } else if (UmurValue == 45) {
                    if (TinggiValue <= 100.9) {
                        Result = (TinggiValue - 100.9) / (100.9 - 96.7);
                    } else {
                        Result = (TinggiValue - 100.9) / (105.1 - 100.9);
                    }
                } else if (UmurValue == 46) {
                    if (TinggiValue <= 101.5) {
                        Result = (TinggiValue - 101.5) / (101.5 - 97.3);
                    } else {
                        Result = (TinggiValue - 101.5) / (105.8 - 101.5);
                    }
                } else if (UmurValue == 47) {
                    if (TinggiValue <= 102.1) {
                        Result = (TinggiValue - 102.1) / (102.1 - 97.9);
                    } else {
                        Result = (TinggiValue - 102.1) / (107.0 - 102.1);
                    }
                } else if (UmurValue == 48) {
                    if (TinggiValue <= 102.7) {
                        Result = (TinggiValue - 102.7) / (102.7 - 98.4);
                    } else {
                        Result = (TinggiValue - 102.7) / (107.0 - 102.7);
                    }
                } else if (UmurValue == 49) {
                    if (TinggiValue <= 103.3) {
                        Result = (TinggiValue - 103.3) / (103.3 - 99.0);
                    } else {
                        Result = (TinggiValue - 103.3) / (107.7 - 103.3);
                    }
                } else if (UmurValue == 50) {
                    if (TinggiValue <= 103.9) {
                        Result = (TinggiValue - 103.9) / (103.9 - 99.5);
                    } else {
                        Result = (TinggiValue - 103.9) / (108.3 - 103.9);
                    }
                } else if (UmurValue == 51) {
                    if (TinggiValue <= 104.5) {
                        Result = (TinggiValue - 104.5) / (104.5 - 100.1);
                    } else {
                        Result = (TinggiValue - 104.5) / (108.9 - 104.5);
                    }
                } else if (UmurValue == 52) {
                    if (TinggiValue <= 105.0) {
                        Result = (TinggiValue - 105.0) / (105.0 - 100.6);
                    } else {
                        Result = (TinggiValue - 105.0) / (109.5 - 105.0);
                    }
                } else if (UmurValue == 53) {
                    if (TinggiValue <= 105.6) {
                        Result = (TinggiValue - 105.6) / (105.6 - 101.1);
                    } else {
                        Result = (TinggiValue - 105.6) / (110.1 - 105.6);
                    }
                } else if (UmurValue == 54) {
                    if (TinggiValue <= 106.2) {
                        Result = (TinggiValue - 106.2) / (106.2 - 101.6);
                    } else {
                        Result = (TinggiValue - 106.2) / (110.7 - 106.2);
                    }
                } else if (UmurValue == 55) {
                    if (TinggiValue <= 106.7) {
                        Result = (TinggiValue - 106.7) / (106.7 - 102.2);
                    } else {
                        Result = (TinggiValue - 106.7) / (111.3 - 106.7);
                    }
                } else if (UmurValue == 56) {
                    if (TinggiValue <= 107.3) {
                        Result = (TinggiValue - 107.3) / (107.3 - 102.7);
                    } else {
                        Result = (TinggiValue - 107.3) / (111.9 - 107.3);
                    }
                } else if (UmurValue == 57) {
                    if (TinggiValue <= 107.8) {
                        Result = (TinggiValue - 107.8) / (107.8 - 103.2);
                    } else {
                        Result = (TinggiValue - 107.8) / (112.5 - 107.8);
                    }
                } else if (UmurValue == 58) {
                    if (TinggiValue <= 108.4) {
                        Result = (TinggiValue - 108.4) / (108.4 - 103.7);
                    } else {
                        Result = (TinggiValue - 108.4) / (113.0 - 108.4);
                    }
                } else if (UmurValue == 59) {
                    if (TinggiValue <= 108.9) {
                        Result = (TinggiValue - 108.9) / (108.9 - 104.2);
                    } else {
                        Result = (TinggiValue - 108.9) / (113.6 - 108.9);
                    }
                } else if (UmurValue == 60) {
                    if (TinggiValue <= 109.4) {
                        Result = (TinggiValue - 109.4) / (109.4 - 104.7);
                    } else {
                        Result = (TinggiValue - 109.4) / (114.2 - 109.4);
                    }
                }
            }
            childScoreTBU.setText(String.format(Locale.ENGLISH, "%.1f", Result));
            if (Result < -3) {
                childStatusTBU.setText("Sangat Pendek");
                childStatusTBU.setTextColor(Color.parseColor("#F4511E"));
                childScoreTBU.setTextColor(Color.parseColor("#F4511E"));
            } else if (Result >= -3 && Result < -2) {
                childStatusTBU.setText("Pendek");
                childStatusTBU.setTextColor(Color.parseColor("#000000"));
                childScoreTBU.setTextColor(Color.parseColor("#000000"));
            } else if (Result >= -2 && Result <= 2) {
                childStatusTBU.setText("Normal");
                childStatusTBU.setTextColor(Color.parseColor("#000000"));
                childScoreTBU.setTextColor(Color.parseColor("#000000"));
            } else if (Result > 2) {
                childStatusTBU.setText("Tinggi");
                childStatusTBU.setTextColor(Color.parseColor("#F4511E"));
                childScoreTBU.setTextColor(Color.parseColor("#F4511E"));
            }
        }
    }

    private void saveTBU() {
        String Score = String.valueOf(childScoreTBU.getText().toString());
        String Status = String.valueOf(childStatusTBU.getText().toString());
        String Tinggi = childHeightCountTBU.getText().toString();
        final String UmurString = childMonthCountTBU.getText().toString();
        Integer Umur = Integer.parseInt(UmurString);
        String DateSave = FormatDate;

        final String id = mDatabase.push().getKey();
        final TinggiBadanUmur tinggiBadanUmur =
                new TinggiBadanUmur(id, Score, Status, Tinggi, Umur, DateSave);

        showProgressDialog();
        mDatabase.orderByChild("tbuAge").equalTo(Umur)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hideProgressDialog();
                        if (!dataSnapshot.exists()) {
                            if (id != null) {
                                mDatabase.child(id).setValue(tinggiBadanUmur);
                            }
                            Snackbar.make(TBUCount.this.findViewById(android.R.id.content),
                                    "Data Berhasil Disimpan", Snackbar.LENGTH_LONG)
                                    .setAction("BATAL", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDatabase.child(id).removeValue();
                                        }
                                    }).show();
                        } else {
                            Snackbar.make(TBUCount.this.findViewById(android.R.id.content),
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
                            Snackbar.make(TBUCount.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        saveTBU();
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
        if (i == R.id.ButtonCountTBU) {
            countTBU();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception ignored) {

            }
        } else if (i == R.id.ButtonSaveTBU) {
            saveDialog();
        }
    }
}
