package com.fbasegizi.statusgizi.count;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;

public class GiziCount extends BaseActivity implements View.OnClickListener {

    private String id, nama, tanggal, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gizi_count);

        CardView menu_1_1 = findViewById(R.id.menu_1_1);
        CardView menu_1_2 = findViewById(R.id.menu_1_2);
        CardView menu_1_3 = findViewById(R.id.menu_1_3);
        CardView menu_1_4 = findViewById(R.id.menu_1_4);

        menu_1_1.setOnClickListener(this);
        menu_1_2.setOnClickListener(this);
        menu_1_3.setOnClickListener(this);
        menu_1_4.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Hitung Status Gizi");
        }

        id = getIntent().getStringExtra("id");
        gender = getIntent().getStringExtra("gender");
        nama = getIntent().getStringExtra("nama");
        tanggal = getIntent().getStringExtra("tanggal");
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
        Intent i;
        switch (v.getId()) {
            case R.id.menu_1_1:
                i = new Intent(this, BBUCount.class);
                i.putExtra("id", id);
                i.putExtra("nama", nama);
                i.putExtra("tanggal", tanggal);
                i.putExtra("gender", gender);
                startActivity(i);
                break;
            case R.id.menu_1_2:
                i = new Intent(this, TBUCount.class);
                i.putExtra("id", id);
                i.putExtra("nama", nama);
                i.putExtra("tanggal", tanggal);
                i.putExtra("gender", gender);
                startActivity(i);
                break;
            case R.id.menu_1_3:
                i = new Intent(this, BBTBCount.class);
                i.putExtra("id", id);
                i.putExtra("nama", nama);
                i.putExtra("tanggal", tanggal);
                i.putExtra("gender", gender);
                startActivity(i);
                break;
            case R.id.menu_1_4:
                i = new Intent(this, IMTUCount.class);
                i.putExtra("id", id);
                i.putExtra("nama", nama);
                i.putExtra("tanggal", tanggal);
                i.putExtra("gender", gender);
                startActivity(i);
                break;
        }
    }
}