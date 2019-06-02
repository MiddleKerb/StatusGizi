package com.fbasegizi.statusgizi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fbasegizi.statusgizi.fragment.MenuActivity;
import com.fbasegizi.statusgizi.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNameField;
    private Button mSignUpButton;
    private TextView mSignInLink;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutName;
    private CheckBox checkBox;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        mNameField = findViewById(R.id.field_name_signUp);
        mEmailField = findViewById(R.id.field_email_signUp);
        mPasswordField = findViewById(R.id.field_password_signUp);
        mSignUpButton = findViewById(R.id.button_sign_up);
        mSignInLink = findViewById(R.id.link_signin);
        textInputLayoutEmail = findViewById(R.id.textLayoutEmailUp);
        textInputLayoutPassword = findViewById(R.id.textLayoutPasswordUp);
        textInputLayoutName = findViewById(R.id.textLayoutNameUp);

        // Click listeners
        mSignUpButton.setOnClickListener(this);
        mSignInLink.setOnClickListener(this);
        mSignInLink.setPaintFlags(mSignInLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
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

    private void signUp() {
        Log.d(TAG, "signUp");
        showProgressDialog();
        final String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        } else {
                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.ScrollSignUp),
                                    "Akun " + email + " sudah terdaftar", Snackbar.LENGTH_SHORT)
                                    .setAction("MASUK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(SignUp.this, SignIn.class));
                                            finish();
                                        }
                                    });
                            mySnackbar.show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        String name = mNameField.getText().toString().trim().replaceAll(" +", " ");

        // Write new user
        writeNewUser(user.getUid(), name, username, user.getEmail());

        // Go to MenuActivity
        startActivity(new Intent(SignUp.this, MenuActivity.class));
        finish();
    }

    // Simpen data Email dan Nama ke dalam DB
    private void writeNewUser(String userId, String name, String username, String email) {
        User user = new User(name, username, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        String name = mNameField.getText().toString();

        if (email.isEmpty()) {
            textInputLayoutEmail.setError("Email tidak boleh kosong");
            result = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayoutEmail.setError("Format email salah");
            result = false;
        } else {
            textInputLayoutEmail.setErrorEnabled(false);
        }

        if (password.isEmpty()) {
            textInputLayoutPassword.setError("Password tidak boleh kosong");
            result = false;
        } else if (password.length() < 8 || password.length() > 30) {
            textInputLayoutPassword.setError("Panjang password minimal 8 karakter dan" +
                    " maksimal 30 karakter");
            result = false;
        } else {
            textInputLayoutPassword.setErrorEnabled(false);
        }

        if (name.isEmpty()) {
            textInputLayoutName.setError("Nama tidak boleh kosong");
            result = false;
        } else if (name.length() < 2) {
            textInputLayoutName.setError("Panjang nama minimal 2 karakter !");
            result = false;
        } else if (name.length() > 100) {
            textInputLayoutName.setError("Panjang nama maksimal 100 karakter !");
            result = false;
        } else if (!name.matches("[a-zA-Z ]+")) {
            textInputLayoutName.setError("Nama hanya dapat diisi dengan huruf alphabet");
            result = false;
        } else {
            textInputLayoutName.setErrorEnabled(false);
        }

        return result;
    }

    private void SaveDialog() {
        if (!validateForm()) {
            return;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Peringatan");
        alertDialogBuilder
                .setMessage("Apakah data yang diisi sudah benar?")
                .setCancelable(false)
                .setPositiveButton("Daftar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            dialog.cancel();
                            Snackbar.make(SignUp.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        signUp();
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
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
            }
        });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_sign_up) {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception ignored) {
            }
            SaveDialog();
        } else if (i == R.id.link_signin) {
            startActivity(new Intent(SignUp.this, SignIn.class));
            finish();
        }
    }
}
