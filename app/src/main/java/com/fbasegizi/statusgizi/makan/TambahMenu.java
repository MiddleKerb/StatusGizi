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
import com.fbasegizi.statusgizi.model.MenuMakan;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TambahMenu extends BaseActivity {

    private TextInputLayout textInputLayoutNama, textInputLayoutBahan, textInputLayoutCara, textInputLayoutUmur;
    private EditText editTextNama, editTextBahan, editTextCara, editTextUmur;
    private Button button;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        textInputLayoutNama = findViewById(R.id.FieldNamaMenu);
        textInputLayoutBahan = findViewById(R.id.FieldMenuBahan);
        textInputLayoutCara = findViewById(R.id.FieldMenuBuat);
        textInputLayoutUmur = findViewById(R.id.FieldMenuUmur);
        editTextNama = findViewById(R.id.EditNamaMenu);
        editTextBahan = findViewById(R.id.EditMenuBahan);
        editTextCara = findViewById(R.id.EditMenuBuat);
        editTextUmur = findViewById(R.id.EditMenuUmur);
        button = findViewById(R.id.ButtonAddMenu);

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
            actionBar.setTitle("Rekomendasi Menu Makan");
        }
    }

    private void saveMenu() {
        final String id = mDatabase.push().getKey();
        String judul = editTextNama.getText().toString().toLowerCase();
        String bahan = editTextBahan.getText().toString().toLowerCase();
        String proses = editTextCara.getText().toString();
        String umur = editTextUmur.getText().toString();

        final MenuMakan menuMakan = new MenuMakan(id, judul, bahan, proses, umur);
        mDatabase.child("MenuMakan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressDialog();
                if (id != null) {
                    mDatabase.child("MenuMakan").child(id).setValue(menuMakan);
                }
                Snackbar.make(TambahMenu.this.findViewById(android.R.id.content),
                        "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                editTextNama.setText("");
                editTextBahan.setText("");
                editTextCara.setText("");
                editTextUmur.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        showProgressDialog();
    }

    private void saveDialog() {
        String Nama = editTextNama.getText().toString();
        String Bahan = editTextBahan.getText().toString();
        String Cara = editTextCara.getText().toString();

        if (TextUtils.isEmpty(Nama)) {
            textInputLayoutNama.setError("Kosong");
        } else if (TextUtils.isEmpty(Bahan)) {
            textInputLayoutBahan.setError("Kosong");
        } else if (TextUtils.isEmpty(Cara)) {
            textInputLayoutCara.setError("Kosong");
        } else {
            textInputLayoutNama.setErrorEnabled(false);
            textInputLayoutBahan.setErrorEnabled(false);
            textInputLayoutCara.setErrorEnabled(false);

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
                                Snackbar.make(TambahMenu.this.findViewById(android.R.id.content),
                                        "Koneksi internet tidak tersedia",
                                        Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                            saveMenu();
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