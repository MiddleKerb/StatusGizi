package com.fbasegizi.statusgizi.makan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.model.Bahan;
import com.fbasegizi.statusgizi.model.Rekomendasi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuHarian extends BaseActivity implements View.OnClickListener {

    private TextView textViewTanggal, textViewKalori, textViewTotal, textViewWarn, textViewSama;
    private Button buttonPilih, buttonSimpan;
    private Spinner spinner;
    private TableLayout tableLayout;

    private DatabaseReference mDatabase;
    private ArrayList<Bahan> bahans = new ArrayList<>();
    private ArrayList<String> listname = new ArrayList<>();

    private String[] stringArray;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserBahan = new ArrayList<>();

    private Integer kalor, totalKal;
    private String id, nama, tanggal, gender, item, dataBahan, combine, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_harian);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        textViewTanggal = findViewById(R.id.TextMenuHarianTanggal);
        textViewTotal = findViewById(R.id.TextMenuHarianTotal);
        textViewKalori = findViewById(R.id.TextMenuHarianKebutuhan);
        textViewWarn = findViewById(R.id.TextMenuHarianWarn);
        textViewSama = findViewById(R.id.TextMenuHarianSama);
        buttonPilih = findViewById(R.id.ButtonMenuPilih);
        buttonSimpan = findViewById(R.id.ButtonMenuSimpan);
        spinner = findViewById(R.id.SpinnerMenu);
        tableLayout = findViewById(R.id.TableSimpan);

        id = getIntent().getStringExtra("id");
        gender = getIntent().getStringExtra("gender");
        nama = getIntent().getStringExtra("nama");
        tanggal = getIntent().getStringExtra("tanggal");

        textViewWarn.setVisibility(View.GONE);

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
        if (months <= 6) {
            totalKal = 550;
        } else if (months <= 11) {
            totalKal = 725;
        } else if (months <= 36) {
            totalKal = 1125;
        } else if (months <= 72) {
            totalKal = 1600;
        }
        textViewKalori.setText(String.format("Kebutuhan Kalori Anak : %s Kkal", String.valueOf(totalKal)));

        isiData();

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        date = formatter.format(today);
        textViewTanggal.setText(date);

        buttonSimpan.setVisibility(View.GONE);
        tableLayout.setVisibility(View.GONE);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.waktu_menu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 6 && timeOfDay < 10) {
            spinner.setSelection(adapter.getPosition("Pagi"));
        } else if (timeOfDay >= 10 && timeOfDay < 14) {
            spinner.setSelection(adapter.getPosition("Siang"));
        } else if (timeOfDay >= 14 && timeOfDay < 18) {
            spinner.setSelection(adapter.getPosition("Sore"));
        } else {
            spinner.setSelection(adapter.getPosition("Sore"));
        }

        buttonPilih.setOnClickListener(this);
        buttonSimpan.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Menu Makan Harian");
        }
    }

    private void isiData() {
        mDatabase.child("Bahan").orderByChild("bahanNama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bahan bahan = snapshot.getValue(Bahan.class);
                    if (bahan != null) {
                        listname.add(capitalize(bahan.bahanNama));
                    }
                    bahans.add(bahan);
                }
                stringArray = listname.toArray(new String[0]);
                checkedItems = new boolean[stringArray.length];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    private void saveMenu() {
        combine = date + "_" + spinner.getSelectedItem();
        final String waktu = String.valueOf(spinner.getSelectedItem());
        final String uid = mDatabase.push().getKey();
        final Rekomendasi rekomendasi = new Rekomendasi(uid, dataBahan, date, waktu, combine);

        showProgressDialog();
        mDatabase.child("Rekomendasi").child(getUid()).child(id).orderByChild("rekomendasiKode").equalTo(combine)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hideProgressDialog();
                        if (!dataSnapshot.exists()) {
                            if (uid != null) {
                                mDatabase.child("Rekomendasi").child(getUid()).child(id).child(uid).setValue(rekomendasi);
                            }
                            Snackbar.make(MenuHarian.this.findViewById(android.R.id.content),
                                    "Data Berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(MenuHarian.this.findViewById(android.R.id.content),
                                    "Data hari ini untuk waktu " + waktu + " hari sudah tersimpan",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void saveDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder
                .setMessage(String.format("Apakah anda ingin menyimpan menu untuk %s hari?", spinner.getSelectedItem()))
                .setCancelable(false)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            dialog.cancel();
                            Snackbar.make(MenuHarian.this.findViewById(android.R.id.content),
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ButtonMenuPilih) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MenuHarian.this);
            alertDialogBuilder.setTitle("Pilih Bahan")
                    .setCancelable(false)
                    .setMultiChoiceItems(stringArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                                mUserBahan.add(which);
                            } else {
                                mUserBahan.remove((Integer.valueOf(which)));
                            }
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            item = "- ";
                            dataBahan = "";
                            kalor = 0;
                            if (!mUserBahan.isEmpty()) {
                                for (int i = 0; i < mUserBahan.size(); i++) {
                                    item = item + stringArray[mUserBahan.get(i)];
                                    dataBahan = dataBahan + stringArray[mUserBahan.get(i)];
                                    kalor = kalor + Integer.valueOf(bahans.get(mUserBahan.get(i)).getBahanKalori());
                                    if (i != mUserBahan.size() - 1) {
                                        item = item + " \n- ";
                                        dataBahan = dataBahan + ", ";
                                    }
                                }
                            } else {
                                item = "Bahan kosong, pilih bahan terlebih dahulu!";
                                tableLayout.setVisibility(View.GONE);
                                buttonSimpan.setVisibility(View.GONE);
                            }
                            textViewSama.setText(item);
                            textViewTotal.setText(String.format("%s Kkal", String.valueOf(kalor)));
                            if (kalor > totalKal) {
                                textViewWarn.setVisibility(View.VISIBLE);
                            } else {
                                textViewWarn.setVisibility(View.GONE);
                            }
                            tableLayout.setVisibility(View.VISIBLE);
                            buttonSimpan.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < checkedItems.length; i++) {
                                checkedItems[i] = false;
                                mUserBahan.clear();
                                textViewWarn.setVisibility(View.GONE);
                                textViewSama.setText("Bahan kosong, pilih bahan terlebih dahulu!");
                                textViewTotal.setText("0 Kkal");
                            }
                            tableLayout.setVisibility(View.GONE);
                            buttonSimpan.setVisibility(View.GONE);
                        }
                    });
            AlertDialog mDialog = alertDialogBuilder.create();
            mDialog.show();
        } else if (i == R.id.ButtonMenuSimpan) {
            saveDialog();
        }
    }
}
