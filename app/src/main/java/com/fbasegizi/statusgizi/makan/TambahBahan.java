package com.fbasegizi.statusgizi.makan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.Bahan;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TambahBahan extends BaseActivity {

    private TextInputLayout textInputLayoutNama, textInputLayoutKalori, textInputLayoutKarbo,
            textInputLayoutLemak, textInputLayoutProtein;
    private EditText editTextNama, editTextKalori, editTextKarbo, editTextLemak, editTextProtein;
    private Button button;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_bahan);

        mDatabase = FirebaseDatabase.getInstance().getReference("Bahan");

        textInputLayoutNama = findViewById(R.id.FieldNamaBahan);
        textInputLayoutKalori = findViewById(R.id.FieldKaloriBahan);
        textInputLayoutKarbo = findViewById(R.id.FieldKarboBahan);
        textInputLayoutLemak = findViewById(R.id.FieldLemakBahan);
        textInputLayoutProtein = findViewById(R.id.FieldProteinBahan);

        editTextNama = findViewById(R.id.EditNamaBahan);
        editTextKalori = findViewById(R.id.EditKaloriBahan);
        editTextKarbo = findViewById(R.id.EditKarboBahan);
        editTextLemak = findViewById(R.id.EditLemakBahan);
        editTextProtein = findViewById(R.id.EditProteinBahan);

        button = findViewById(R.id.ButtonAddBahan);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {

                }
                saveDialog();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Daftar Bahan");
        }
    }

    private void saveBahan() {
        final String Nama = editTextNama.getText().toString().trim().replaceAll(" +", " ").toLowerCase();
        String Kalori = String.valueOf(editTextKalori.getText().toString());
        String Karbo = String.valueOf(editTextKarbo.getText().toString());
        String Lemak = String.valueOf(editTextLemak.getText().toString());
        String Protein = String.valueOf(editTextProtein.getText().toString());

        final String id = mDatabase.push().getKey();
        final Bahan bahan = new Bahan(id, Nama, Kalori, Karbo, Lemak, Protein);
        showProgressDialog();
        mDatabase.orderByChild("bahanNama").equalTo(Nama)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hideProgressDialog();
                        if (!dataSnapshot.exists()) {
                            if (id != null) {
                                mDatabase.child(id).setValue(bahan);
                                Snackbar.make(TambahBahan.this.findViewById(android.R.id.content),
                                        "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Snackbar.make(TambahBahan.this.findViewById(android.R.id.content),
                                    "Data " + Nama + " sudah tersimpan",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void saveDialog() {
        String Nama = editTextNama.getText().toString();
        String Kalori = String.valueOf(editTextKalori.getText().toString());
        String Karbo = String.valueOf(editTextKarbo.getText().toString());
        String Lemak = String.valueOf(editTextLemak.getText().toString());
        String Protein = String.valueOf(editTextProtein.getText().toString());

        if (TextUtils.isEmpty(Nama)) {
            textInputLayoutNama.setError("Kosong");
        } else if (TextUtils.isEmpty(Kalori)) {
            textInputLayoutKalori.setError("Kosong");
        } else if (TextUtils.isEmpty(Karbo)) {
            textInputLayoutKarbo.setError("Kosong");
        } else if (TextUtils.isEmpty(Lemak)) {
            textInputLayoutLemak.setError("Kosong");
        } else if (TextUtils.isEmpty(Protein)) {
            textInputLayoutProtein.setError("Kosong");
        } else {
            textInputLayoutNama.setErrorEnabled(false);
            textInputLayoutKalori.setErrorEnabled(false);
            textInputLayoutKarbo.setErrorEnabled(false);
            textInputLayoutLemak.setErrorEnabled(false);
            textInputLayoutProtein.setErrorEnabled(false);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Peringatan");
            alertDialogBuilder
                    .setMessage("Apakah data yang diisi sudah benar?")
                    .setCancelable(false)
                    .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isOnline()) {
                                dialog.cancel();
                                Snackbar.make(TambahBahan.this.findViewById(android.R.id.content),
                                        "Koneksi internet tidak tersedia",
                                        Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                            saveBahan();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
                }
            });
            alertDialog.show();
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
