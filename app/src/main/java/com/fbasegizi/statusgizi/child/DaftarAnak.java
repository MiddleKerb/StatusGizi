package com.fbasegizi.statusgizi.child;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.Anak;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaftarAnak extends BaseActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;

    private TextView textView;
    private EditText mChildName;
    private EditText mChildDate;
    private Spinner mChildGender;
    private Button ButtonChildSign;
    private TextInputLayout tLayoutChildName;
    private TextInputLayout tLayoutChildDate;

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_anak);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mChildName = findViewById(R.id.ChildName);
        mChildDate = findViewById(R.id.ChildDate);
        mChildGender = findViewById(R.id.ChildGender);

        tLayoutChildName = findViewById(R.id.textLayoutChildName);
        tLayoutChildDate = findViewById(R.id.textLayoutChildDate);

        textView = findViewById(R.id.ErorrListDaftarAnak);
        ButtonChildSign = findViewById(R.id.button_sign_child);

        textView.setVisibility(View.GONE);

        mChildDate.setOnClickListener(this);
        ButtonChildSign.setOnClickListener(this);

        final List<String> genderList =
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.child_genre)));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.custom_spinner, genderList) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner);
        mChildGender.setAdapter(spinnerArrayAdapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Daftar Anak");
        }
    }

    private boolean validateForm() {
        String nama = mChildName.getText().toString();
        String tanggal = mChildDate.getText().toString();

        if (TextUtils.isEmpty(nama)) {
            tLayoutChildName.setError("Nama tidak boleh kosong!");
            return false;
        } else if (nama.length() > 100) {
            tLayoutChildName.setError("Panjang nama maksimal 100 karakter !");
            return false;
        } else if (nama.length() < 1) {
            tLayoutChildName.setError("Panjang nama minimal 1 karakter !");
            return false;
        } else if (!nama.matches("[a-zA-Z ]+")) {
            tLayoutChildName.setError("Nama hanya dapat diisi dengan huruf alphabet");
            return false;
        } else {
            tLayoutChildName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(tanggal)) {
            tLayoutChildDate.setError("Tanggal tidak boleh kosong!");
            return false;
        } else {
            //GetMonth
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date birthdate = null;
            try {
                birthdate = sdf.parse(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long time = currentDate.getTime().getTime() / 1000 - birthdate.getTime() / 1000;
            int years = Math.round(time) / 31536000;
            int months = years * 12 + (Math.round(time - years * 31536000) / 2628000);
            //EndGetMonth

            if (months > 60) {
                tLayoutChildDate.setError("Umur anak maksimal 60 bulan!");
                return false;
            } else {
                tLayoutChildDate.setErrorEnabled(false);
            }
        }

        if (mChildGender.getSelectedItem().toString().trim().equals("Pilih")) {
            textView.setText("Pilih jenis kelamin! ");
            textView.setVisibility(View.VISIBLE);
            return false;
        } else {
            textView.setText("");
            textView.setVisibility(View.GONE);
        }

        return true;
    }

    private void saveDialog() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ignored) {
        }
        if (!validateForm()) {
            return;
        }

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
                            Snackbar.make(DaftarAnak.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        submitChild();
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

    private void submitChild() {
        final String nama = mChildName.getText().toString();
        final String gender = mChildGender.getSelectedItem().toString();
        final String tanggal = mChildDate.getText().toString();
        final String id = mDatabase.child("child").push().getKey();
        final String userId = getUid();

        final Anak anak = new Anak(id, nama.replaceFirst("\\s++$", ""), gender, tanggal);

        showProgressDialog();
        mDatabase.child("child").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressDialog();
                if (id != null) {
                    mDatabase.child("child").child(userId).child(id).setValue(anak);
                    Intent intent = new Intent(getBaseContext(), ListAnak.class);
                    String update = "update";
                    intent.putExtra("user_add", update);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
                Snackbar.make(DaftarAnak.this.findViewById(android.R.id.content),
                        "Server error, coba ulangi beberapa saat lagi!",
                        Snackbar.LENGTH_LONG).show();
            }
        });
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ChildDate) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mChildDate.setText(new StringBuilder().append(dayOfMonth).append("-")
                                    .append(month + 1).append("-").append(year));
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        } else if (i == R.id.button_sign_child) {
            saveDialog();
        }
    }
}
