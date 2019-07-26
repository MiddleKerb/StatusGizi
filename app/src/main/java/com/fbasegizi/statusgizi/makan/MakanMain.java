package com.fbasegizi.statusgizi.makan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;

public class MakanMain extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makan_main);

        CardView menu_1 = findViewById(R.id.makan_main_1);
        CardView menu_2 = findViewById(R.id.makan_main_2);
        CardView menu_3 = findViewById(R.id.makan_main_3);
        Button button = findViewById(R.id.buttonRekomendasiMenu);

        menu_1.setOnClickListener(this);
        menu_2.setOnClickListener(this);
        menu_3.setOnClickListener(this);
        button.setOnClickListener(this);

        button.setVisibility(View.GONE);
        if (getUid().equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
            button.setVisibility(View.VISIBLE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Angka Kecukupan Gizi");
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.makan_main_1:
                i = new Intent(this, DaftarBahan.class);
                startActivity(i);
                break;
            case R.id.makan_main_2:
                i = new Intent(this, MenuHarianAnak.class);
                startActivity(i);
                break;
            case R.id.makan_main_3:
                i = new Intent(this, RekomendasiAnak.class);
                startActivity(i);
                break;
            case R.id.buttonRekomendasiMenu:
                i = new Intent(this, TambahMenu.class);
                startActivity(i);
                break;
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