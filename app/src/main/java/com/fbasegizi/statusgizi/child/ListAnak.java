package com.fbasegizi.statusgizi.child;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.RecyclerViewDaftarAnak;
import com.fbasegizi.statusgizi.model.Anak;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListAnak extends BaseActivity {
    private DatabaseReference mDatabase;

    private List<Anak> anaks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewDaftarAnak viewDaftarAnak;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView emptyText;
    private FloatingActionButton floatAddAnak;
    private Button button;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_anak);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        emptyText = findViewById(R.id.empty_list_anak);
        progressBar = findViewById(R.id.ProgressListAnak);
        coordinatorLayout = findViewById(R.id.clayout);
        button = findViewById(R.id.buttonListAnak);

        recyclerView = findViewById(R.id.RecycleListAnak);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        viewDaftarAnak = new RecyclerViewDaftarAnak(this, anaks);

        swipeRefreshLayout = findViewById(R.id.swipeListAnak);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        floatAddAnak = findViewById(R.id.FloatAddChild);
        floatAddAnak.hide();
        floatAddAnak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListAnak.this, DaftarAnak.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DaftarAnak.class);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Daftar Anak");
        }

        if (isOnline()) {
            emptyText.setText("Koneksi internet tidak tersedia!");
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            floatAddAnak.hide();
            return;
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        message();
        loadData();
    }

    private void message() {
        Intent intent = getIntent();
        intent.getExtras();

        if (intent.hasExtra("user_delete")) {
            Snackbar.make(coordinatorLayout, "Data anak berhasil di hapus",
                    Snackbar.LENGTH_LONG).show();
        } else if (intent.hasExtra("user_update")) {
            Snackbar.make(coordinatorLayout, "Data anak berhasil di ubah",
                    Snackbar.LENGTH_LONG).show();
        } else if (intent.hasExtra("user_add")) {
            Snackbar.make(coordinatorLayout, "Data anak berhasil di tambahkan",
                    Snackbar.LENGTH_LONG).show();
        }
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
        if (isOnline()) {
            Snackbar.make(coordinatorLayout, "Koneksi internet tidak tersedia!",
                    Snackbar.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        if (getUid().equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
            mDatabase.child("child").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    anaks = new ArrayList<>();
                    state = recyclerView.getLayoutManager().onSaveInstanceState();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                                Anak anak = childSnapshot.getValue(Anak.class);
                                if (anak != null) {
                                    anak.setChildId(childSnapshot.getKey());
                                }
                                anaks.add(anak);
                                progressBar.setVisibility(View.GONE);
                                emptyText.setVisibility(View.GONE);
                                button.setVisibility(View.GONE);
                                floatAddAnak.show();
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        emptyText.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);
                        floatAddAnak.hide();
                    }
                    viewDaftarAnak = new RecyclerViewDaftarAnak(ListAnak.this, anaks);
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
        } else {
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
                            floatAddAnak.show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        emptyText.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);
                        floatAddAnak.hide();
                    }
                    viewDaftarAnak = new RecyclerViewDaftarAnak(ListAnak.this, anaks);
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