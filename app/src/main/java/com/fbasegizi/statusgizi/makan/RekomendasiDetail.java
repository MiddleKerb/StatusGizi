package com.fbasegizi.statusgizi.makan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;

public class RekomendasiDetail extends BaseActivity {

    private String judul, bahan, proses;
    private String[] bahanArray, prosesArray;
    private TextView textViewJudul, tableDash, tableisi, tableSpace;
    private TableLayout tableLayoutBahan, tableLayoutProses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekomendasi_detail);

        judul = getIntent().getStringExtra("Judul");
        bahan = getIntent().getStringExtra("Bahan");
        proses = getIntent().getStringExtra("Proses");

        textViewJudul = findViewById(R.id.RekomendasiDetailJudul);
        tableLayoutBahan = findViewById(R.id.TableLayoutDetailBahan);
        tableLayoutProses = findViewById(R.id.TableLayoutDetailProses);

        bahanArray = bahan.split(", ");
        prosesArray = proses.split("; ");

        textViewJudul.setText(judul);

        for (int i = 0; i < bahanArray.length; i++) {
            String s = bahanArray[i] + ".";

            TableRow tr = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            tableDash = new TextView(this);
            tableDash.setText("-");
            tableDash.setTextColor(Color.BLACK);
            tableDash.setTextAppearance(android.R.style.TextAppearance_Medium);
            tr.addView(tableDash);

            tableSpace = new TextView(this);
            tableSpace.setText(" ");
            tableSpace.setTextColor(Color.BLACK);
            tableSpace.setTextAppearance(android.R.style.TextAppearance_Medium);
            tr.addView(tableSpace);

            tableisi = new TextView(this);
            tableisi.setText(s);
            tableisi.setTextColor(Color.BLACK);
            tableisi.setGravity(Gravity.START);
            tableisi.setLayoutParams(params);
            tableisi.setTextAppearance(android.R.style.TextAppearance_Medium);
            tr.addView(tableisi);

            tableLayoutBahan.addView(tr);
        }

        for (int i = 0; i < prosesArray.length; i++) {
            String s = prosesArray[i] + ".";
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            tableDash = new TextView(this);
            tableDash.setText(String.valueOf(i + 1));
            tableDash.setTextColor(Color.BLACK);
            tableDash.setTextAppearance(android.R.style.TextAppearance_Medium);
            tr.addView(tableDash);

            tableSpace = new TextView(this);
            tableSpace.setText(". ");
            tableSpace.setTextColor(Color.BLACK);
            tableSpace.setTextAppearance(android.R.style.TextAppearance_Medium);
            tr.addView(tableSpace);

            tableisi = new TextView(this);
            tableisi.setText(s);
            tableisi.setTextColor(Color.BLACK);
            tableisi.setGravity(Gravity.START);
            tableisi.setLayoutParams(params);
            tableisi.setTextAppearance(android.R.style.TextAppearance_Medium);
            tr.addView(tableisi);

            tableLayoutProses.addView(tr);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rekomendasi Menu Makan");
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
