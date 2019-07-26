package com.fbasegizi.statusgizi.fragment.Help;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;

public class Referensi extends BaseActivity implements View.OnClickListener {

    private Button buttonHitung;
    private Button buttonAngka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referensi);

        buttonHitung = findViewById(R.id.buttonRefHitung);
        buttonAngka = findViewById(R.id.buttonRefKalori);

        buttonHitung.setOnClickListener(this);
        buttonAngka.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Referensi");
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
        Intent i;
        switch (v.getId()) {
            case R.id.buttonRefHitung:
                i = new Intent(this, RefWebView.class);
                i.putExtra("url", "https://drive.google.com/file/d/11Zm3w3828ZczjhiQCC6JUWdvVbn1gb_1/preview");
                startActivity(i);
                break;
            case R.id.buttonRefKalori:
                i = new Intent(this, RefWebView.class);
                i.putExtra("url", "https://drive.google.com/file/d/1gRFbxcZRELEY_aTIj1hKLRJQWzaX7vSm/preview");
                startActivity(i);
                break;
        }
    }
}
