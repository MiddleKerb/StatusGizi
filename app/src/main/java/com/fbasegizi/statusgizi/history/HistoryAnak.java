package com.fbasegizi.statusgizi.history;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.RecyclerViewRiwayatAnak;
import com.fbasegizi.statusgizi.child.ListAnak;
import com.fbasegizi.statusgizi.model.Anak;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryAnak extends BaseActivity {

    private DatabaseReference mDatabase;

    private List<Anak> anaks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewRiwayatAnak viewDaftarAnak;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Parcelable state;
    private ProgressBar progressBar;
    private TextView emptyText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_anak);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emptyText = findViewById(R.id.empty_history_anak);
        progressBar = findViewById(R.id.ProgressHistoryAnak);
        button = findViewById(R.id.buttonHistoryAnak);
        recyclerView = findViewById(R.id.RecycleHistoryAnak);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        viewDaftarAnak = new RecyclerViewRiwayatAnak(this, anaks);

        swipeRefreshLayout = findViewById(R.id.swipeHistoryAnak);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListAnak.class);
                startActivity(intent);
                finish();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Riwayat Pengukuran");
        }

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
    }

    private void refreshItems() {
        loadData();
        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadData() {
        mDatabase.child("child").child(getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                anaks = new ArrayList<>();
                state = recyclerView.getLayoutManager().onSaveInstanceState();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Anak anak = postSnapshot.getValue(Anak.class);
                        if (anak != null) {
                            anak.setChildId(postSnapshot.getKey());
                        }
                        anaks.add(anak);
                        progressBar.setVisibility(View.GONE);
                        emptyText.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
                viewDaftarAnak = new RecyclerViewRiwayatAnak(HistoryAnak.this, anaks);
                viewDaftarAnak.notifyDataSetChanged();
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(viewDaftarAnak);
                recyclerView.getLayoutManager().onRestoreInstanceState(state);
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