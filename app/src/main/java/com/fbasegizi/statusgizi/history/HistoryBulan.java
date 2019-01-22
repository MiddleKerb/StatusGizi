package com.fbasegizi.statusgizi.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.count.AnakCount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryBulan extends BaseActivity {
    private String id, nama, tanggal, gender, history, BulanStart, BulanEnd;
    private Integer StartValue, EndValue;

    private TextInputLayout layoutBulanStart;
    private TextInputLayout layoutBulanEnd;
    private Button buttonBulan, buttonAll, buttonEmpty;
    private EditText textBulanStart;
    private EditText textBulanEnd;
    private TextView textView1, textView2, textViewEmpty;
    private ProgressBar progressBar;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_bulan);

        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonBulan = findViewById(R.id.ButtonBulan);
        buttonAll = findViewById(R.id.ButtonAll);
        buttonEmpty = findViewById(R.id.buttonHistoryBulan);
        textBulanStart = findViewById(R.id.TextHistoryBulanStart);
        textBulanEnd = findViewById(R.id.TextHistoryBulanEnd);
        layoutBulanStart = findViewById(R.id.LayoutHistoryBulanStart);
        layoutBulanEnd = findViewById(R.id.LayoutHistoryBulanEnd);
        textView1 = findViewById(R.id.JudulHistoryBulan);
        textView2 = findViewById(R.id.SubJudulHistoryBulan);
        textViewEmpty = findViewById(R.id.empty_history_bulan);
        progressBar = findViewById(R.id.ProgressHistoryBulan);

        id = intent.getStringExtra("id");
        nama = intent.getStringExtra("nama");
        tanggal = intent.getStringExtra("tanggal");
        gender = intent.getStringExtra("gender");
        history = intent.getStringExtra("history");

        textView1.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);
        buttonAll.setVisibility(View.GONE);
        buttonBulan.setVisibility(View.GONE);
        layoutBulanStart.setVisibility(View.GONE);
        layoutBulanEnd.setVisibility(View.GONE);

        buttonEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnakCount.class);
                startActivity(intent);
                finish();
            }
        });

        buttonBulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateBulan()) {
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), HistoryDetail.class);
                intent.putExtra("BulanStart", BulanStart);
                intent.putExtra("BulanEnd", BulanEnd);
                intent.putExtra("id", id);
                intent.putExtra("nama", nama);
                intent.putExtra("tanggal", tanggal);
                intent.putExtra("gender", gender);
                intent.putExtra("history", history);
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {

                }
                startActivity(intent);
            }
        });

        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryBulan.this, HistoryDetail.class);
                intent.putExtra("BulanStart", "0");
                intent.putExtra("BulanEnd", "60");
                intent.putExtra("id", id);
                intent.putExtra("nama", nama);
                intent.putExtra("tanggal", tanggal);
                intent.putExtra("gender", gender);
                intent.putExtra("history", history);
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {

                }
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Riwayat Pengukuran");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.child(history).child(getUid()).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    textViewEmpty.setVisibility(View.VISIBLE);
                    buttonEmpty.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    textView1.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    buttonAll.setVisibility(View.VISIBLE);
                    buttonBulan.setVisibility(View.VISIBLE);
                    layoutBulanStart.setVisibility(View.VISIBLE);
                    layoutBulanEnd.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Server error, coba ulangi beberapa saat lagi!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateBulan() {
        boolean result = true;
        BulanStart = textBulanStart.getText().toString();
        BulanEnd = textBulanEnd.getText().toString();
        StartValue = !BulanStart.equals("") ? Integer.parseInt(BulanStart) : 0;
        EndValue = !BulanEnd.equals("") ? Integer.parseInt(BulanEnd) : 0;

        if (BulanStart.isEmpty()) {
            layoutBulanStart.setError("Harap masukkan bulan awal perhitungan");
            result = false;
        } else {
            layoutBulanStart.setErrorEnabled(false);
        }

        if (BulanEnd.isEmpty()) {
            layoutBulanEnd.setError("Harap masukkan bulan akhir perhitungan");
            result = false;
        } else {
            layoutBulanStart.setErrorEnabled(false);
        }

        if (StartValue > EndValue) {
            layoutBulanStart.setError("Bulan awal tidak boleh lebih besar dari bulan akhir");
            return false;
        } else {
            layoutBulanStart.setErrorEnabled(false);
        }

        if (StartValue < 0) {
            layoutBulanStart.setError("Bulan tidak dapat dibawah 0");
            return false;
        } else {
            layoutBulanStart.setErrorEnabled(false);
        }

        if (EndValue < 0) {
            layoutBulanEnd.setError("Bulan tidak dapat dibawah 0");
            return false;
        } else if (EndValue > 60) {
            layoutBulanEnd.setError("Bulan tidak dapat diatas 60");
            return false;
        } else {
            layoutBulanStart.setErrorEnabled(false);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}