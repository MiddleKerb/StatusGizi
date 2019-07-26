package com.fbasegizi.statusgizi.makan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.RecyclerViewRekomendasiHarian;
import com.fbasegizi.statusgizi.model.Rekomendasi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RekomendasiHarian extends BaseActivity {

    private DatabaseReference mDatabase;

    private List<Rekomendasi> rekomendasis = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewRekomendasiHarian viewRekomendasiHarian;
    private ProgressBar progressBar;
    private TextView emptyText;
    private Button button;
    private String id, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_harian_rekomendasi);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emptyText = findViewById(R.id.empty_rekomendasi_harian);
        progressBar = findViewById(R.id.ProgressRekomendasiHarian);
        button = findViewById(R.id.buttonRekomendasiHarian);
        recyclerView = findViewById(R.id.RecycleRekomendasiHarian);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuHarianAnak.class);
                startActivity(intent);
                finish();
            }
        });

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        date = formatter.format(today);

        id = getIntent().getStringExtra("id");

        if (isOnline()) {
            emptyText.setText("Koneksi internet tidak tersedia!");
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rekomendasi Menu Makan");
        }

        loadData();
    }

    private void loadData() {
        mDatabase.child("Rekomendasi").child(getUid()).child(id).orderByChild("rekomendasiWaktu")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        rekomendasis = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Rekomendasi rekomendasi = postSnapshot.getValue(Rekomendasi.class);
                                String tanggal = (String) postSnapshot.child("rekomendasiTanggal").getValue();
                                if (date.equals(tanggal)) {
                                    if (rekomendasi != null) {
                                        rekomendasi.setRekomendasiId(postSnapshot.getKey());
                                    }
                                    rekomendasis.add(rekomendasi);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    emptyText.setVisibility(View.INVISIBLE);
                                    button.setVisibility(View.INVISIBLE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    recyclerView.setVisibility(View.INVISIBLE);
                                    emptyText.setVisibility(View.VISIBLE);
                                    button.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            emptyText.setVisibility(View.VISIBLE);
                            button.setVisibility(View.VISIBLE);
                        }
                        viewRekomendasiHarian = new RecyclerViewRekomendasiHarian(RekomendasiHarian.this, rekomendasis);
                        viewRekomendasiHarian.notifyDataSetChanged();
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(viewRekomendasiHarian);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Server error, coba ulangi beberapa saat lagi!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
