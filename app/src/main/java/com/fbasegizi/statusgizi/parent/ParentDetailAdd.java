package com.fbasegizi.statusgizi.parent;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.adapter.ParentDetailList;
import com.fbasegizi.statusgizi.model.ParentDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParentDetailAdd extends BaseActivity {

    private DatabaseReference mDatabase;

    private TextView textViewName, emptyText;

    private ListView listView;
    private List<ParentDetail> parentDetails;

    private EditText editTextAge, editTextWeight, editTextHeight, editTextFamily;
    private TextInputLayout textInputLayoutAge, textInputLayoutWeight,
            textInputLayoutHeight, textInputLayoutFamily;

    private Button buttonSave;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference("ParentDetail")
                .child(getUid()).child(getIntent().getStringExtra(ParentList.CHILD_ID));

        textViewName = findViewById(R.id.textViewNameParentDetail);
        emptyText = findViewById(R.id.empty_parent_detail);
        progressBar = findViewById(R.id.ProgressParentDetail);

        listView = findViewById(R.id.listViewParentDetail);
        parentDetails = new ArrayList<>();
        listView.setEmptyView(progressBar);

        editTextAge = findViewById(R.id.AgeParentDetail);
        editTextFamily = findViewById(R.id.FamilyParentDetail);
        editTextHeight = findViewById(R.id.HeightParentDetail);
        editTextWeight = findViewById(R.id.WeightParentDetail);

        textInputLayoutAge = findViewById(R.id.AgeParentField);
        textInputLayoutFamily = findViewById(R.id.FamilyParentField);
        textInputLayoutHeight = findViewById(R.id.HeightParentField);
        textInputLayoutWeight = findViewById(R.id.WeightParentField);

        buttonSave = findViewById(R.id.ButtonSaveParent);

        textViewName.setText(getIntent().getStringExtra(ParentList.CHILD_NAME));

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDialog();
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParentDetail parentDetail = parentDetails.get(position);
                deleteDialog(parentDetail.parentId);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Data Orang Tua");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parentDetails.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ParentDetail parentDetail = postSnapshot.getValue(ParentDetail.class);
                        if (parentDetail != null) {
                            parentDetail.setParentId(postSnapshot.getKey());
                        }
                        parentDetails.add(parentDetail);
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    listView.setEmptyView(emptyText);
                }
                ParentDetailList parentAdapter =
                        new ParentDetailList(ParentDetailAdd.this, parentDetails);
                parentAdapter.notifyDataSetChanged();
                listView.setAdapter(parentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_update_detail, null);
        dialogBuilder.setView(dialogView);

        final Button buttonUpdateAgree = dialogView.findViewById(R.id.ButtonUpdateAgree);
        final Button buttonUpdateCancel = dialogView.findViewById(R.id.ButtonUpdateCancel);

        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.setCanceledOnTouchOutside(false);

        buttonUpdateAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    Snackbar mySnackbar = Snackbar.make(dialogView,
                            "Koneksi internet tidak tersedia", Snackbar.LENGTH_SHORT)
                            .setAction("COBA LAGI", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    buttonUpdateAgree.performClick();
                                }
                            });
                    mySnackbar.show();
                    return;
                }
                saveParent();
                b.dismiss();
            }
        });

        buttonUpdateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    private void deleteDialog(final String Id) {
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
                showProgressDialog();
                deleteParent(Id);
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

    private void deleteParent(String Id) {
        mDatabase.child(Id).removeValue();
        hideProgressDialog();
        Snackbar.make(this.findViewById(android.R.id.content),
                "Data berhasil di hapus", Snackbar.LENGTH_LONG).show();
    }

    private void saveParent() {
        if (!validateForm()) {
            return;
        }
        String usia = editTextAge.getText().toString();
        String tinggi = editTextHeight.getText().toString();
        String berat = editTextWeight.getText().toString();
        String anggota = editTextFamily.getText().toString();
        final String nama = getIntent().getStringExtra(ParentList.CHILD_NAME);

        Integer anggotaValue = Integer.valueOf(anggota);
        Integer usiaValue = Integer.valueOf(usia);

        final String id = mDatabase.push().getKey();
        final ParentDetail parentDetail = new ParentDetail(id, tinggi, berat, usiaValue, anggotaValue);

        showProgressDialog();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressDialog();
                if (!dataSnapshot.exists()) {
                    if (id != null) {
                        mDatabase.child(id).setValue(parentDetail);
                        Snackbar.make(ParentDetailAdd.this.findViewById(android.R.id.content),
                                "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(ParentDetailAdd.this.findViewById(android.R.id.content),
                            "Data orang tua untuk anak " + nama + " sudah ada",
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validateForm() {
        String usia = editTextAge.getText().toString();
        String tinggi = editTextHeight.getText().toString();
        String berat = editTextWeight.getText().toString();
        String anggota = editTextFamily.getText().toString();

        Integer usiaValue = !usia.equals("") ? Integer.parseInt(usia) : 0;
        Integer tinggiValue = !tinggi.equals("") ? Integer.parseInt(tinggi) : 0;
        Double beratValue = !berat.equals("") ? Double.parseDouble(berat) : 0;
        Double anggotaValue = !anggota.equals("") ? Double.parseDouble(anggota) : 0;

        if (berat.isEmpty()) {
            textInputLayoutWeight.setError("Berat Badan Tidak Boleh Kosong!");
            return false;
        } else if (beratValue == 0) {
            textInputLayoutWeight.setError("Berat Tidak Boleh Nol!");
            return false;
        } else {
            textInputLayoutWeight.setErrorEnabled(false);
        }

        if (tinggi.isEmpty()) {
            textInputLayoutHeight.setError("Tinggi Badan Tidak Boleh Kosong!");
            return false;
        } else if (tinggiValue == 0) {
            textInputLayoutHeight.setError("Tinggi Tidak Boleh Nol!");
            return false;
        } else {
            textInputLayoutHeight.setErrorEnabled(false);
        }

        if (usia.isEmpty()) {
            textInputLayoutAge.setError("Usia Tidak Boleh Kosong!");
            return false;
        } else if (usiaValue == 0) {
            textInputLayoutAge.setError("Usia Tidak Boleh Nol!");
            return false;
        } else {
            textInputLayoutAge.setErrorEnabled(false);
        }

        if (anggota.isEmpty()) {
            textInputLayoutFamily.setError("Jumlah Anggota Keluarga Tidak Boleh Kosong!");
            return false;
        } else if (anggotaValue == 0) {
            textInputLayoutFamily.setError("Jumlah Anggota Keluarga Tidak Boleh Nol!");
            return false;
        } else {
            textInputLayoutFamily.setErrorEnabled(false);
        }

        return true;
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
}
