package com.fbasegizi.statusgizi.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.RecyclerViewHistoryBBTB;
import com.fbasegizi.statusgizi.adapter.RecyclerViewHistoryBBU;
import com.fbasegizi.statusgizi.adapter.RecyclerViewHistoryIMTU;
import com.fbasegizi.statusgizi.adapter.RecyclerViewHistoryTBU;
import com.fbasegizi.statusgizi.count.AnakCount;
import com.fbasegizi.statusgizi.model.BeratBadanTinggiBadan;
import com.fbasegizi.statusgizi.model.BeratBadanUmur;
import com.fbasegizi.statusgizi.model.IndeksMassaTubuhUmur;
import com.fbasegizi.statusgizi.model.TinggiBadanUmur;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryDetail extends BaseActivity {

    private DatabaseReference mDatabase;

    private TextView historyDetailName;
    private TextView historyDetailDOB;
    private TextView historyDetailGender;
    private TextView emptyText;
    private Button button;
    private ProgressBar progressBar;

    private String id, history;
    private RecyclerView recyclerView;
    private RecyclerViewHistoryBBU viewHistoryBBU;
    private RecyclerViewHistoryBBTB viewHistoryBBTB;
    private RecyclerViewHistoryTBU viewHistoryTBU;
    private RecyclerViewHistoryIMTU viewHistoryIMTU;

    private List<BeratBadanUmur> beratBadanUmurs = new ArrayList<>();
    private List<BeratBadanTinggiBadan> beratBadanTinggiBadans = new ArrayList<>();
    private List<TinggiBadanUmur> tinggiBadanUmurs = new ArrayList<>();
    private List<IndeksMassaTubuhUmur> indeksMassaTubuhUmurs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressBar = findViewById(R.id.ProgressHistoryDetail);

        historyDetailName = findViewById(R.id.HistoryDetailName);
        historyDetailDOB = findViewById(R.id.HistoryDetailDOB);
        historyDetailGender = findViewById(R.id.HistoryDetailGender);
        button = findViewById(R.id.buttonHistoryDetail);
        emptyText = findViewById(R.id.empty_history_detail);

        recyclerView = findViewById(R.id.RecycleListHistory);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnakCount.class);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        history = intent.getStringExtra("history");
        id = intent.getStringExtra("id");
        historyDetailName.setText(intent.getStringExtra("nama"));
        historyDetailGender.setText(intent.getStringExtra("gender"));
        historyDetailDOB.setText(intent.getStringExtra("tanggal"));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if ("BeratBadanUmur".equals(history)) {
                actionBar.setTitle("Berat Badan Umur");
                viewHistoryBBU = new RecyclerViewHistoryBBU(this, beratBadanUmurs);
                loadBBU();
            } else if ("TinggiBadanUmur".equals(history)) {
                actionBar.setTitle("Tinggi Badan Umur");
                viewHistoryTBU = new RecyclerViewHistoryTBU(this, tinggiBadanUmurs);
                loadTBU();
            } else if ("BeratBadanTinggiBadan".equals(history)) {
                actionBar.setTitle("Berat Badan Tinggi Badan");
                viewHistoryBBTB = new RecyclerViewHistoryBBTB(this, beratBadanTinggiBadans);
                loadBBTB();
            } else if ("IndeksMassaTubuhUmur".equals(history)) {
                actionBar.setTitle("Indeks Massa Tubuh");
                viewHistoryIMTU = new RecyclerViewHistoryIMTU(this, indeksMassaTubuhUmurs);
                loadIMTU();
            }
        }

        if (isOnline()) {
            emptyText.setText("Koneksi internet tidak tersedia!");
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void loadBBU() {
        mDatabase.child("BeratBadanUmur").child(getUid()).child(id)
                .orderByChild("bbuAge").startAt(0).endAt(60)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        beratBadanUmurs = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                BeratBadanUmur beratBadanUmur = postSnapshot.getValue(BeratBadanUmur.class);
                                if (beratBadanUmur != null) {
                                    beratBadanUmur.setBbuId(postSnapshot.getKey());
                                }
                                beratBadanUmurs.add(beratBadanUmur);
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
                        viewHistoryBBU = new RecyclerViewHistoryBBU(HistoryDetail.this, beratBadanUmurs);
                        viewHistoryBBU.notifyDataSetChanged();
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(viewHistoryBBU);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadTBU() {
        mDatabase.child("TinggiBadanUmur").child(getUid()).child(id)
                .orderByChild("tbuAge").startAt(0).endAt(60)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tinggiBadanUmurs = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                TinggiBadanUmur tinggiBadanUmur = postSnapshot.getValue(TinggiBadanUmur.class);
                                if (tinggiBadanUmur != null) {
                                    tinggiBadanUmur.setTbuId(postSnapshot.getKey());
                                }
                                tinggiBadanUmurs.add(tinggiBadanUmur);
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
                        viewHistoryTBU = new RecyclerViewHistoryTBU(HistoryDetail.this, tinggiBadanUmurs);
                        viewHistoryTBU.notifyDataSetChanged();
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(viewHistoryTBU);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadBBTB() {
        mDatabase.child("BeratBadanTinggiBadan").child(getUid()).child(id)
                .orderByChild("bbtbAge").startAt(0).endAt(60)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        beratBadanTinggiBadans = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                BeratBadanTinggiBadan beratBadanTinggiBadan = postSnapshot.getValue(BeratBadanTinggiBadan.class);
                                if (beratBadanTinggiBadan != null) {
                                    beratBadanTinggiBadan.setBbtbId(postSnapshot.getKey());
                                }
                                beratBadanTinggiBadans.add(beratBadanTinggiBadan);
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
                        viewHistoryBBTB = new RecyclerViewHistoryBBTB(HistoryDetail.this, beratBadanTinggiBadans);
                        viewHistoryBBTB.notifyDataSetChanged();
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(viewHistoryBBTB);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadIMTU() {
        mDatabase.child("IndeksMassaTubuhUmur").child(getUid()).child(id)
                .orderByChild("imtuAge").startAt(0).endAt(60)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        indeksMassaTubuhUmurs = new ArrayList<>();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                IndeksMassaTubuhUmur indeksMassaTubuhUmur = postSnapshot.getValue(IndeksMassaTubuhUmur.class);
                                if (indeksMassaTubuhUmur != null) {
                                    indeksMassaTubuhUmur.setImtuId(postSnapshot.getKey());
                                }
                                indeksMassaTubuhUmurs.add(indeksMassaTubuhUmur);
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
                        viewHistoryIMTU = new RecyclerViewHistoryIMTU(HistoryDetail.this, indeksMassaTubuhUmurs);
                        viewHistoryIMTU.notifyDataSetChanged();
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(viewHistoryIMTU);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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