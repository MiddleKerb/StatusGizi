package com.fbasegizi.statusgizi.makan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.RecyclerViewMenuMakan;
import com.fbasegizi.statusgizi.model.MenuMakan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RekomendasiMakan extends BaseActivity {

    private DatabaseReference mDatabase;

    private List<MenuMakan> menuMakans = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewMenuMakan viewMenuMakan;
    private ProgressBar progressBar;
    private TextView textView, textViewMakan;
    private Button buttonView;
    private String bid, nbid, bahan;
    private String[] bahanArray;
    private Integer bahanSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi_makan);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.RecycleRekomendasiMenu);
        progressBar = findViewById(R.id.ProgressRekomendasiMenu);
        textView = findViewById(R.id.empty_rekomendasi_menu);
        textViewMakan = findViewById(R.id.textViewRekomendasiMenu);
        buttonView = findViewById(R.id.buttonRekomendasiMenu);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        bahan = getIntent().getStringExtra("bahan");
        bahanArray = bahan.toLowerCase().split(", ");
        bahanSize = bahanArray.length;

        if (isOnline()) {
            textView.setText("Koneksi internet tidak tersedia!");
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rekomendasi Menu Makan");
        }

        loadData();
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAll();
            }
        });
    }

    private void loadData() {
        mDatabase.child("MenuMakan").orderByChild("menuJudul").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuMakans = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        MenuMakan menuMakan = postSnapshot.getValue(MenuMakan.class);
                        String bahanData = (String) postSnapshot.child("menuBahan").getValue();
                        bid = (String) postSnapshot.child("menuId").getValue();
                        for (int i = 0; i < bahanSize; i++) {
                            if (bahanData != null && bahanData.contains(bahanArray[i])) {
                                if (bid != null && menuMakan != null && !bid.equals(nbid)) {
                                    menuMakan.setMenuId(postSnapshot.getKey());
                                    menuMakans.add(menuMakan);
                                    nbid = bid;
                                }
                                progressBar.setVisibility(View.GONE);
                                textView.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                viewMenuMakan = new RecyclerViewMenuMakan(RekomendasiMakan.this, menuMakans);
                viewMenuMakan.notifyDataSetChanged();
                if (viewMenuMakan.getItemCount() == 0) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    textView.setText("Menu makan untuk data menu harian belum tersedia!");
                    textView.setVisibility(View.VISIBLE);
                }
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(viewMenuMakan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadAll() {
        textViewMakan.setText("Daftar seluruh menu makanan");
        mDatabase.child("MenuMakan").orderByChild("menuJudul").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuMakans = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        MenuMakan menuMakan = postSnapshot.getValue(MenuMakan.class);
                        if (menuMakan != null) {
                            menuMakan.setMenuId(postSnapshot.getKey());
                            menuMakans.add(menuMakan);
                        }
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                viewMenuMakan = new RecyclerViewMenuMakan(RekomendasiMakan.this, menuMakans);
                viewMenuMakan.notifyDataSetChanged();
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(viewMenuMakan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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