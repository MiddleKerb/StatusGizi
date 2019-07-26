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
import com.fbasegizi.statusgizi.adapter.RecyclerViewRekomendasiAnak;
import com.fbasegizi.statusgizi.child.ListAnak;
import com.fbasegizi.statusgizi.model.Anak;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RekomendasiAnak extends BaseActivity {

    private DatabaseReference mDatabase;

    private List<Anak> anaks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewRekomendasiAnak viewRekomendasiAnak;
    private ProgressBar progressBar;
    private TextView emptyText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi_anak);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emptyText = findViewById(R.id.empty_rekomendasi_anak);
        progressBar = findViewById(R.id.ProgressRekomendasiAnak);
        button = findViewById(R.id.buttonRekomendasiAnak);
        recyclerView = findViewById(R.id.RecycleRekomendasiAnak);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        viewRekomendasiAnak = new RecyclerViewRekomendasiAnak(this, anaks);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListAnak.class);
                startActivity(intent);
                finish();
            }
        });

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

        loadData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rekomendasi Menu Makan");
        }
    }

    private void loadData() {
        mDatabase.child("child").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                anaks = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Anak anak = postSnapshot.getValue(Anak.class);
                        if (anak != null) {
                            anak.setChildId(postSnapshot.getKey());
                        }
                        anaks.add(anak);
                        progressBar.setVisibility(View.INVISIBLE);
                        emptyText.setVisibility(View.INVISIBLE);
                        button.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    emptyText.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
                viewRekomendasiAnak = new RecyclerViewRekomendasiAnak(RekomendasiAnak.this, anaks);
                viewRekomendasiAnak.notifyDataSetChanged();
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(viewRekomendasiAnak);
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
