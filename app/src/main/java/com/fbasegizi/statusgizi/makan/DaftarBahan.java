package com.fbasegizi.statusgizi.makan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.RecyclerViewDaftarBahan;
import com.fbasegizi.statusgizi.model.Bahan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DaftarBahan extends BaseActivity {

    private Button buttonDaftar;
    private ProgressBar progressBar;
    private TextView textView;
    private EditText editText;

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private RecyclerViewDaftarBahan viewDaftarBahan;
    private List<Bahan> bahans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_bahan);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        buttonDaftar = findViewById(R.id.ButtonDaftarBahan);
        recyclerView = findViewById(R.id.RecycleListDaftarBahan);
        progressBar = findViewById(R.id.ProgressListDaftarBahan);
        textView = findViewById(R.id.empty_list_bahan);
        editText = findViewById(R.id.EditDaftarBahan);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        searchBahan("");

        buttonDaftar.setVisibility(View.GONE);
        if (getUid().equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
            buttonDaftar.setVisibility(View.VISIBLE);
        }

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TambahBahan.class);
                startActivity(intent);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    searchBahan("");
                } else {
                    searchBahan(s.toString().toLowerCase());
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Daftar Bahan");
        }

        if (isOnline()) {
            textView.setText("Koneksi internet tidak tersedia!");
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void searchBahan(final String s) {
        mDatabase.child("Bahan").orderByChild("bahanNama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bahans = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    bahans.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Bahan bahan = postSnapshot.getValue(Bahan.class);
                        String nama = (String) postSnapshot.child("bahanNama").getValue();
                        if (nama != null && nama.contains(s)) {
                            if (bahan != null) {
                                bahan.setBahanId(postSnapshot.getKey());
                            }
                            bahans.add(bahan);
                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                viewDaftarBahan = new RecyclerViewDaftarBahan(DaftarBahan.this, bahans);
                viewDaftarBahan.notifyDataSetChanged();
                if (viewDaftarBahan.getItemCount() == 0) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(viewDaftarBahan);
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