package com.fbasegizi.statusgizi.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;

public class HistoryChoose extends BaseActivity implements View.OnClickListener {

    private Button buttonToBBU;
    private Button buttonToTBU;
    private Button buttonToBBTB;
    private Button buttonToIMTU;

    private String id, nama, tanggal, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_choose);

        Intent intent = getIntent();

        buttonToBBU = findViewById(R.id.ButtonToBBU);
        buttonToTBU = findViewById(R.id.ButtonToTBU);
        buttonToBBTB = findViewById(R.id.ButtonToBBTB);
        buttonToIMTU = findViewById(R.id.ButtonToIMTU);

        buttonToBBU.setOnClickListener(this);
        buttonToTBU.setOnClickListener(this);
        buttonToBBTB.setOnClickListener(this);
        buttonToIMTU.setOnClickListener(this);

        id = intent.getStringExtra("id");
        nama = intent.getStringExtra("nama");
        tanggal = intent.getStringExtra("tanggal");
        gender = intent.getStringExtra("gender");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Riwayat Pengukuran");
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Intent intent = new Intent(getApplicationContext(), HistoryDetail.class);
        switch (i) {
            case R.id.ButtonToBBU:
                intent.putExtra("history", "BeratBadanUmur");
                break;
            case R.id.ButtonToTBU:
                intent.putExtra("history", "TinggiBadanUmur");
                break;
            case R.id.ButtonToBBTB:
                intent.putExtra("history", "BeratBadanTinggiBadan");
                break;
            case R.id.ButtonToIMTU:
                intent.putExtra("history", "IndeksMassaTubuhUmur");
                break;
        }
        intent.putExtra("id", id);
        intent.putExtra("nama", nama);
        intent.putExtra("tanggal", tanggal);
        intent.putExtra("gender", gender);
        startActivity(intent);
    }
}
