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

public class EditAnak extends BaseActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private TextView textView;
    private EditText dTextName, dTextDate;
    private TextInputLayout dLayoutChildDate, dLayoutChildName;
    private Spinner dSpinnerGender;
    private Button buttonUpdate, buttonDelete;
    private String id, nama, tanggal, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_anak);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        dTextName = findViewById(R.id.dialogChildName);
        dTextDate = findViewById(R.id.dialogChildDate);
        dLayoutChildDate = findViewById(R.id.dialogLayoutChildDate);
        dLayoutChildName = findViewById(R.id.dialogLayoutChildName);

        dSpinnerGender = findViewById(R.id.ChildGenderEdit);
        buttonUpdate = findViewById(R.id.buttonUpdateAnak);
        buttonDelete = findViewById(R.id.buttonDeleteAnak);
        textView = findViewById(R.id.ErorrListEditAnak);

        textView.setVisibility(View.GONE);

        id = getIntent().getStringExtra("id");
        gender = getIntent().getStringExtra("gender");
        nama = getIntent().getStringExtra("nama");
        tanggal = getIntent().getStringExtra("tanggal");

        dTextName.setText(nama);
        dTextDate.setText(tanggal);

        final List<String> genderList =
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.child_genre)));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, genderList) {
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
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dSpinnerGender.setAdapter(spinnerArrayAdapter);

        if (gender.equals("Laki-laki")) {
            dSpinnerGender.setSelection(1);
        } else if (gender.equals("Perempuan")) {
            dSpinnerGender.setSelection(2);
        }

        dTextDate.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        if (getUid().equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
            buttonUpdate.setVisibility(View.GONE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Daftar Anak");
        }
    }

    private boolean validateForm() {
        String nama = dTextName.getText().toString();
        String gender = dSpinnerGender.getSelectedItem().toString();
        String tanggal = dTextDate.getText().toString();

        if (TextUtils.isEmpty(nama)) {
            dLayoutChildName.setError("Nama Tidak Boleh Kosong!");
            return false;
        } else if (nama.length() > 100) {
            dLayoutChildName.setError("Panjang Nama Maksimal 100 Karakter !");
            return false;
        } else if (nama.length() < 1) {
            dLayoutChildName.setError("Panjang Nama Minimal 1 Karakter !");
            return false;
        } else if (!nama.matches("[a-zA-Z ]+")) {
            dLayoutChildName.setError("Nama hanya dapat diisi dengan huruf alphabet");
            return false;
        } else {
            dLayoutChildName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(tanggal)) {
            dLayoutChildDate.setError("Tanggal Tidak Boleh Kosong!");
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
                dLayoutChildDate.setError("Umur Anak Maksimal 60 Bulan!");
                return false;
            } else {
                dLayoutChildDate.setErrorEnabled(false);
            }
        }

        if (dSpinnerGender.getSelectedItem().toString().trim().equals("Pilih")) {
            textView.setText("Pilih jenis kelamin! ");
            textView.setVisibility(View.VISIBLE);
            return false;
        } else {
            textView.setText("");
            textView.setVisibility(View.GONE);
        }

        return true;
    }

    private void updateChild(final String userId, String childName, String childGender,
                             String childDate) {
        showProgressDialog();
        final Anak anak = new Anak(userId, childName, childGender, childDate);
        mDatabase.child("child").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressDialog();
                mDatabase.child("child").child(getUid()).child(userId).setValue(anak);
                Intent intent = new Intent(getBaseContext(), ListAnak.class);
                String update = "update";
                intent.putExtra("user_update", update);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressDialog();
                Snackbar.make(EditAnak.this.findViewById(android.R.id.content),
                        "Server error, coba ulangi beberapa saat lagi!",
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void deleteChild(final String id) {
        showProgressDialog();
        if (getUid().equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
            mDatabase.child("child").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    hideProgressDialog();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String requiredId = snapshot.getKey();
                        if (requiredId != null) {
                            mDatabase.child("child").child(requiredId).child(id).removeValue();
                            mDatabase.child("BeratBadanUmur").child(requiredId).child(id).removeValue();
                            mDatabase.child("TinggiBadanUmur").child(requiredId).child(id).removeValue();
                            mDatabase.child("BeratBadanTinggiBadan").child(requiredId).child(id).removeValue();
                            mDatabase.child("IndeksMassaTubuhUmur").child(requiredId).child(id).removeValue();
                            mDatabase.child("ParentDetail").child(requiredId).child(id).removeValue();
                            Intent intent = new Intent(getBaseContext(), ListAnak.class);
                            String update = "update";
                            intent.putExtra("user_delete", update);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    hideProgressDialog();
                    Snackbar.make(EditAnak.this.findViewById(android.R.id.content),
                            "Server error, coba ulangi beberapa saat lagi!",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            mDatabase.child("child").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    hideProgressDialog();
                    mDatabase.child("child").child(getUid()).child(id).removeValue();
                    mDatabase.child("BeratBadanUmur").child(getUid()).child(id).removeValue();
                    mDatabase.child("TinggiBadanUmur").child(getUid()).child(id).removeValue();
                    mDatabase.child("BeratBadanTinggiBadan").child(getUid()).child(id).removeValue();
                    mDatabase.child("IndeksMassaTubuhUmur").child(getUid()).child(id).removeValue();
                    mDatabase.child("ParentDetail").child(getUid()).child(id).removeValue();
                    Intent intent = new Intent(getBaseContext(), ListAnak.class);
                    String update = "update";
                    intent.putExtra("user_delete", update);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    hideProgressDialog();
                    Snackbar.make(EditAnak.this.findViewById(android.R.id.content),
                            "Server error, coba ulangi beberapa saat lagi!",
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void deleteDialog(final String Id) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ignored) {
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder
                .setMessage("Apakah Anda yakin ingin mengapus data ini?")
                .setCancelable(false)
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            dialog.cancel();
                            Snackbar.make(EditAnak.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        deleteChild(Id);
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

    private void updateDialog(final String Id, final String nama, final String gender, final String tanggal) {
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
                .setMessage("Apakah Anda yakin ingin mengubah data ini?")
                .setCancelable(false)
                .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            dialog.cancel();
                            Snackbar.make(EditAnak.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        updateChild(Id, nama, gender, tanggal);
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
        switch (i) {
            case R.id.dialogChildDate:
                String[] dateParts = tanggal.split("-");
                Integer mDay = Integer.valueOf(dateParts[0]);
                Integer mMonth = Integer.parseInt(dateParts[1]);
                Integer mYear = Integer.parseInt(dateParts[2]);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditAnak.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String monthString = String.valueOf(month + 1);
                                String dayString = String.valueOf(dayOfMonth);
                                if (monthString.length() == 1) {
                                    monthString = "0" + monthString;
                                }
                                if (dayString.length() == 1) {
                                    dayString = "0" + dayString;
                                }
                                dTextDate.setText(new StringBuilder().append(dayString).append("-")
                                        .append(monthString).append("-").append(year));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
            case R.id.buttonUpdateAnak:
                String nama = dTextName.getText().toString();
                String gender = dSpinnerGender.getSelectedItem().toString();
                String tanggal = dTextDate.getText().toString();
                updateDialog(id, nama.trim().replaceAll(" +", " "), gender, tanggal);
                break;
            case R.id.buttonDeleteAnak:
                deleteDialog(id);
                break;
        }
    }
}