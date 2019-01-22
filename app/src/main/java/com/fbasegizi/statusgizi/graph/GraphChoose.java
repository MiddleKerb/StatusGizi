package com.fbasegizi.statusgizi.graph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;

public class GraphChoose extends BaseActivity implements View.OnClickListener {
    private Button graphToBBU;
    private Button graphToTBU;
    private Button graphToBBTB;
    private Button graphToIMTU;

    private String id, nama, tanggal, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_choose);

        Intent intent = getIntent();

        graphToBBU = findViewById(R.id.GraphToBBU);
        graphToTBU = findViewById(R.id.GraphToTBU);
        graphToBBTB = findViewById(R.id.GraphToBBTB);
        graphToIMTU = findViewById(R.id.GraphToIMTU);

        graphToBBU.setOnClickListener(this);
        graphToTBU.setOnClickListener(this);
        graphToBBTB.setOnClickListener(this);
        graphToIMTU.setOnClickListener(this);

        id = intent.getStringExtra("id");
        nama = intent.getStringExtra("nama");
        tanggal = intent.getStringExtra("tanggal");
        gender = intent.getStringExtra("gender");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Grafik Pertumbuhan");
        }
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
        if (id.equals("ALL")) {
            Intent intent = new Intent(getApplicationContext(), PieGraphChild.class);
            switch (i) {
                case R.id.GraphToBBU:
                    intent.putExtra("history", "BeratBadanUmur");
                    break;
                case R.id.GraphToTBU:
                    intent.putExtra("history", "TinggiBadanUmur");
                    break;
                case R.id.GraphToBBTB:
                    intent.putExtra("history", "BeratBadanTinggiBadan");
                    break;
                case R.id.GraphToIMTU:
                    intent.putExtra("history", "IndeksMassaTubuhUmur");
                    break;
            }
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), LineGraphChild.class);
            switch (i) {
                case R.id.GraphToBBU:
                    intent.putExtra("history", "BeratBadanUmur");
                    break;
                case R.id.GraphToTBU:
                    intent.putExtra("history", "TinggiBadanUmur");
                    break;
                case R.id.GraphToBBTB:
                    intent.putExtra("history", "BeratBadanTinggiBadan");
                    break;
                case R.id.GraphToIMTU:
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
}
