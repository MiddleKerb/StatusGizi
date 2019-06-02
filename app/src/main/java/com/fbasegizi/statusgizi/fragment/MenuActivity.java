package com.fbasegizi.statusgizi.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class MenuActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private DatabaseReference databaseReference;
    private TextView textViewNama, textViewEmail, textViewGreetings;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference storageReference, path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        path = storageReference.child("profiles").child(getUid());

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        textViewNama = headerView.findViewById(R.id.NamaHeader);
        textViewEmail = headerView.findViewById(R.id.EmailHeader);
        textViewGreetings = headerView.findViewById(R.id.GreetingsHeader);
        imageView = headerView.findViewById(R.id.imageNav);

        greetings();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MenuFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        if (isOnline()) {
            Snackbar.make(MenuActivity.this.findViewById(android.R.id.content),
                    "Koneksi internet tidak tersedia",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void greetings() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 6 && timeOfDay < 10) {
            textViewGreetings.setText("Selamat Pagi");
        } else if (timeOfDay >= 10 && timeOfDay < 14) {
            textViewGreetings.setText("Selamat Siang");
        } else if (timeOfDay >= 14 && timeOfDay < 18) {
            textViewGreetings.setText("Selamat Sore");
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            textViewGreetings.setText("Selamat Malam");
        } else if (timeOfDay >= 0 && timeOfDay < 6) {
            textViewGreetings.setText("Selamat Malam");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child("users").child(getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nama = dataSnapshot.child("nama").getValue(String.class);
                        textViewNama.setText(nama);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        textViewEmail.setText(email);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    if (task.getResult() != null) {
                        String download = task.getResult().toString();
                        Picasso.get().load(download).into(imageView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (fragment instanceof MenuFragment) {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Tekan sekali lagi untuk keluar",
                        Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        } else {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MenuFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MenuFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HelpFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_settings:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(this, AccountSetingsActivity.class);
                i.putExtra("RESET", textViewEmail.getText().toString());
                startActivity(i);
                break;
            case R.id.nav_logout:
                drawerLayout.closeDrawer(GravityCompat.START);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Keluar akun dari StatusGizi");
                alertDialogBuilder
                        .setMessage("Apakah Anda ingin keluar akun?")
                        .setCancelable(false)
                        .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isOnline()) {
                                    dialog.cancel();
                                    Snackbar.make(MenuActivity.this.findViewById(android.R.id.content),
                                            "Koneksi internet tidak tersedia",
                                            Snackbar.LENGTH_SHORT).show();
                                    return;
                                }
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MenuActivity.this, SignIn.class));
                                finish();
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
                        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                                .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
                    }
                });
                alertDialog.show();
                return true;
        }
        return true;
    }
}
