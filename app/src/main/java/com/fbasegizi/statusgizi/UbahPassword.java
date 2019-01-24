package com.fbasegizi.statusgizi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.fbasegizi.statusgizi.fragment.AccountSetingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UbahPassword extends BaseActivity implements View.OnClickListener {

    private EditText editText;
    private TextInputLayout textInputLayout;
    private Button button;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        editText = findViewById(R.id.email_change_pass);
        textInputLayout = findViewById(R.id.textLayoutChangePass);
        button = findViewById(R.id.btn_reset_password);

        button.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Ubah Password");
        }

        Intent intent = getIntent();
        editText.setText(intent.getStringExtra(SignIn.RESET));
        sign = intent.getStringExtra("reset_pass");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child("users").child(getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String email = dataSnapshot.child("email").getValue(String.class);
                            editText.setText(email);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Snackbar.make(UbahPassword.this.findViewById(android.R.id.content),
                                    "Server error, coba ulangi beberapa saat lagi!",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void ResetPassword() {
        String email = editText.getText().toString();
        showProgressDialog();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideProgressDialog();
                    if (sign.equals("sign_in")) {
                        Intent intent = new Intent(getBaseContext(), AccountSetingsActivity.class);
                        intent.putExtra("password_change", "in_update");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else if (sign.equals("sign_out")) {
                        Intent intent = new Intent(getBaseContext(), SignIn.class);
                        intent.putExtra("password_change", "out_update");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    hideProgressDialog();
                    Snackbar.make(findViewById(R.id.layoutChangePass),
                            "Coba kembali dalam beberapa saat",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ResetDialog() {
        String email = editText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textInputLayout.setError("Alamat Email tidak boleh kosong!");
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayout.setError("Format email salah");
            return;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Ubah kata sandi");
        alertDialogBuilder
                .setMessage("Apakah Anda ingin mengubah kata sandi?")
                .setCancelable(false)
                .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            dialog.cancel();
                            Snackbar.make(UbahPassword.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        ResetPassword();
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
        if (i == R.id.btn_reset_password) {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception ignored) {
            }
            ResetDialog();
        }
    }
}
