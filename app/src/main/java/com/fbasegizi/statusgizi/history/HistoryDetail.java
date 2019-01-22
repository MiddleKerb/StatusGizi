package com.fbasegizi.statusgizi.history;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.HistoryListBBTB;
import com.fbasegizi.statusgizi.adapter.HistoryListBBU;
import com.fbasegizi.statusgizi.adapter.HistoryListIMTU;
import com.fbasegizi.statusgizi.adapter.HistoryListTBU;
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
    private TextView historyDetailName;
    private TextView historyDetailDOB;
    private TextView historyDetailGender;
    private TextView emptyText;

    private String id, history;

    private Integer BulanStart, BulanEnd;

    private DatabaseReference mDatabase;

    private ProgressBar progressBar;

    private ListView historyDetailList;

    private List<BeratBadanUmur> beratBadanUmurs;
    private List<TinggiBadanUmur> tinggiBadanUmurs;
    private List<BeratBadanTinggiBadan> beratBadanTinggiBadans;
    private List<IndeksMassaTubuhUmur> indeksMassaTubuhUmurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressBar = findViewById(R.id.ProgressHistoryDetail);

        historyDetailList = findViewById(R.id.HistoryDetailList);
        beratBadanUmurs = new ArrayList<>();
        tinggiBadanUmurs = new ArrayList<>();
        beratBadanTinggiBadans = new ArrayList<>();
        indeksMassaTubuhUmurs = new ArrayList<>();

        historyDetailList.setEmptyView(progressBar);

        historyDetailName = findViewById(R.id.HistoryDetailName);
        historyDetailDOB = findViewById(R.id.HistoryDetailDOB);
        historyDetailGender = findViewById(R.id.HistoryDetailGender);
        emptyText = findViewById(R.id.empty_history_detail);

        history = intent.getStringExtra("history");
        id = intent.getStringExtra("id");
        BulanStart = Integer.parseInt(intent.getStringExtra("BulanStart"));
        BulanEnd = Integer.parseInt(intent.getStringExtra("BulanEnd"));

        historyDetailName.setText(intent.getStringExtra("nama"));
        historyDetailGender.setText(intent.getStringExtra("gender"));
        historyDetailDOB.setText(intent.getStringExtra("tanggal"));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if ("BeratBadanUmur".equals(history)) {
                actionBar.setTitle("Berat Badan Umur");
            } else if ("TinggiBadanUmur".equals(history)) {
                actionBar.setTitle("Tinggi Badan Umur");
            } else if ("BeratBadanTinggiBadan".equals(history)) {
                actionBar.setTitle("Berat Badan Tinggi Badan");
            } else if ("IndeksMassaTubuhUmur".equals(history)) {
                actionBar.setTitle("Indeks Massa Tubuh");
            }
        }

        historyDetailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (history) {
                    case "BeratBadanUmur":
                        BeratBadanUmur beratBadanUmur = beratBadanUmurs.get(position);
                        deleteDialog(beratBadanUmur.bbuId);
                        break;
                    case "TinggiBadanUmur":
                        TinggiBadanUmur tinggiBadanUmur = tinggiBadanUmurs.get(position);
                        deleteDialog(tinggiBadanUmur.tbuId);
                        break;
                    case "BeratBadanTinggiBadan":
                        BeratBadanTinggiBadan beratBadanTinggiBadan = beratBadanTinggiBadans.get(position);
                        deleteDialog(beratBadanTinggiBadan.bbtbId);
                        break;
                    case "IndeksMassaTubuhUmur":
                        IndeksMassaTubuhUmur indeksMassaTubuhUmur = indeksMassaTubuhUmurs.get(position);
                        deleteDialog(indeksMassaTubuhUmur.imtuId);
                        break;
                }
            }
        });
    }

    private void deleteDialog(final String detailId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_delete_detail, null);
        dialogBuilder.setView(dialogView);

        final Button buttonDelete = dialogView.findViewById(R.id.ButtonDeleteDetail);
        final Button buttonCancel = dialogView.findViewById(R.id.buttonDeleteDetailCancel);

        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.setCanceledOnTouchOutside(false);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    Snackbar mySnackbar = Snackbar.make(dialogView,
                            "Koneksi internet tidak tersedia", Snackbar.LENGTH_SHORT)
                            .setAction("COBA LAGI", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    buttonDelete.performClick();
                                }
                            });
                    mySnackbar.show();
                    return;
                }
                deleteChild(detailId);
                b.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    private void deleteChild(String detailId) {
        showProgressDialog();
        mDatabase.child(history).child(getUid()).child(id).child(detailId).removeValue();
        hideProgressDialog();
        Snackbar.make(this.findViewById(android.R.id.content),
                "Data berhasil di hapus", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (history) {
            case "BeratBadanUmur":
                mDatabase.child(history).child(getUid()).child(id)
                        .orderByChild("bbuAge").startAt(BulanStart).endAt(BulanEnd)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                beratBadanUmurs.clear();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        BeratBadanUmur beratBadanUmur = postSnapshot.getValue(BeratBadanUmur.class);
                                        if (beratBadanUmur != null) {
                                            beratBadanUmur.setBbuId(postSnapshot.getKey());
                                        }
                                        beratBadanUmurs.add(beratBadanUmur);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    historyDetailList.setEmptyView(emptyText);
                                }
                                HistoryListBBU listBBUAdapter = new HistoryListBBU(HistoryDetail.this,
                                        beratBadanUmurs);
                                listBBUAdapter.notifyDataSetChanged();
                                historyDetailList.setAdapter(listBBUAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                break;
            case "TinggiBadanUmur":
                mDatabase.child(history).child(getUid()).child(id)
                        .orderByChild("tbuAge").startAt(BulanStart).endAt(BulanEnd)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                tinggiBadanUmurs.clear();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        TinggiBadanUmur tinggiBadanUmur = postSnapshot.getValue(TinggiBadanUmur.class);
                                        if (tinggiBadanUmur != null) {
                                            tinggiBadanUmur.setTbuId(postSnapshot.getKey());
                                        }
                                        tinggiBadanUmurs.add(tinggiBadanUmur);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    historyDetailList.setEmptyView(emptyText);
                                }
                                HistoryListTBU listTBUAdapter = new HistoryListTBU(HistoryDetail.this,
                                        tinggiBadanUmurs);
                                listTBUAdapter.notifyDataSetChanged();
                                historyDetailList.setAdapter(listTBUAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                break;
            case "BeratBadanTinggiBadan":
                mDatabase.child(history).child(getUid()).child(id)
                        .orderByChild("bbtbAge").startAt(BulanStart).endAt(BulanEnd)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                beratBadanTinggiBadans.clear();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        BeratBadanTinggiBadan beratBadanTinggiBadan = postSnapshot.getValue(BeratBadanTinggiBadan.class);
                                        if (beratBadanTinggiBadan != null) {
                                            beratBadanTinggiBadan.setBbtbId(postSnapshot.getKey());
                                        }
                                        beratBadanTinggiBadans.add(beratBadanTinggiBadan);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    historyDetailList.setEmptyView(emptyText);
                                }
                                HistoryListBBTB listBBTBAdapter = new HistoryListBBTB(HistoryDetail.this,
                                        beratBadanTinggiBadans);
                                listBBTBAdapter.notifyDataSetChanged();
                                historyDetailList.setAdapter(listBBTBAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                break;
            case "IndeksMassaTubuhUmur":
                mDatabase.child(history).child(getUid()).child(id)
                        .orderByChild("imtuAge").startAt(BulanStart).endAt(BulanEnd)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                indeksMassaTubuhUmurs.clear();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        IndeksMassaTubuhUmur indeksMassaTubuhUmur = postSnapshot.getValue(IndeksMassaTubuhUmur.class);
                                        if (indeksMassaTubuhUmur != null) {
                                            indeksMassaTubuhUmur.setImtuId(postSnapshot.getKey());
                                        }
                                        indeksMassaTubuhUmurs.add(indeksMassaTubuhUmur);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    historyDetailList.setEmptyView(emptyText);
                                }
                                HistoryListIMTU listIMTUAdapter = new HistoryListIMTU(HistoryDetail.this,
                                        indeksMassaTubuhUmurs);
                                listIMTUAdapter.notifyDataSetChanged();
                                historyDetailList.setAdapter(listIMTUAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                break;
        }
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