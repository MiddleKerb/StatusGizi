package com.fbasegizi.statusgizi.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.Help;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.SignIn;
import com.fbasegizi.statusgizi.child.ListAnak;
import com.fbasegizi.statusgizi.graph.ChildGraph;
import com.fbasegizi.statusgizi.history.ChildHistory;
import com.fbasegizi.statusgizi.parent.ParentList;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends BaseActivity implements View.OnClickListener {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        CardView menu_1 = findViewById(R.id.menu_1);
        CardView menu_2 = findViewById(R.id.menu_2);
        CardView menu_3 = findViewById(R.id.menu_3);
        CardView menu_4 = findViewById(R.id.menu_4);
        CardView menu_5 = findViewById(R.id.menu_5);
        CardView menu_6 = findViewById(R.id.menu_6);

        menu_1.setOnClickListener(this);
        menu_2.setOnClickListener(this);
        menu_3.setOnClickListener(this);
        menu_4.setOnClickListener(this);
        menu_5.setOnClickListener(this);
        menu_6.setOnClickListener(this);

        if (getUid().equals("3Nxyv5oB5aVijKiO0bA0oZjEejg2")) {
            menu_5.setVisibility(View.VISIBLE);
        } else {
            menu_5.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dots, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SignIn.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tekan sekali lagi untuk keluar",
                    Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.menu_1:
                i = new Intent(this, ListAnak.class);
                startActivity(i);
                break;
            case R.id.menu_2:
                i = new Intent(this, GiziCount.class);
                startActivity(i);
                break;
            case R.id.menu_3:
                i = new Intent(this, ChildHistory.class);
                startActivity(i);
                break;
            case R.id.menu_4:
                i = new Intent(this, ChildGraph.class);
                startActivity(i);
                break;
            case R.id.menu_5:
                i = new Intent(this, ParentList.class);
                startActivity(i);
                break;
            case R.id.menu_6:
                i = new Intent(this, Help.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
