package com.fbasegizi.statusgizi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        int SPLASH_DISPLAY_LENGTH = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startActivityIntent = new Intent(MainActivity.this, SignIn.class);
                startActivity(startActivityIntent);
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
