package com.fbasegizi.statusgizi;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";
    public static final String RESET = "ResetPassword";

    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSignInButton;
    private TextView mSignUpLink;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private CheckBox checkBox;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = findViewById(R.id.field_email_signIn);
        mPasswordField = findViewById(R.id.field_password_signIn);
        mSignInButton = findViewById(R.id.button_sign_in);
        mSignUpLink = findViewById(R.id.link_signup);
        textInputLayoutEmail = findViewById(R.id.textLayoutEmailIn);
        textInputLayoutPassword = findViewById(R.id.textLayoutPasswordIn);

        mSignUpLink.setPaintFlags(mSignUpLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // Click listeners
        mSignInButton.setOnClickListener(this);
        mSignUpLink.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Intent intent = getIntent();
        intent.getExtras();
        if (intent.hasExtra("password_change")) {
            Snackbar.make(SignIn.this.findViewById(android.R.id.content),
                    "Silahkan cek Email Anda! Untuk instruksi selanjutnya!",
                    Snackbar.LENGTH_LONG).show();
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

    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }
        if (isOnline()) {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.ScrollSignIn),
                    "Koneksi internet tidak tersedia", Snackbar.LENGTH_LONG)
                    .setAction("COBA LAGI", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signIn();
                        }
                    });
            mySnackbar.show();
            return;
        }

        showProgressDialog();
        final String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.ScrollSignIn),
                                        "Alamat email/password salah", Snackbar.LENGTH_LONG)
                                        .setAction("UBAH PASSWORD", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getBaseContext(), UbahPassword.class);
                                                intent.putExtra(RESET, email);
                                                intent.putExtra("reset_pass", "sign_out");
                                                startActivity(intent);
                                            }
                                        });
                                mySnackbar.show();
                                textInputLayoutPassword.setError("Password salah");
                                mPasswordField.requestFocus();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.ScrollSignIn),
                                        "Akun " + email + " belum terdaftar", Snackbar.LENGTH_LONG)
                                        .setAction("DAFTAR", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(SignIn.this, SignUp.class));
                                                finish();
                                            }
                                        });
                                mySnackbar.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.ScrollSignIn),
                                        "Terlalu banyak kesalahan pada input password," +
                                                " coba kembali dalam beberapa saat.",
                                        Snackbar.LENGTH_LONG);
                                mySnackbar.show();
                            }
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        // Go to MenuActivity
        startActivity(new Intent(SignIn.this, MenuActivity.class));
        finish();
    }

    private boolean validateForm() {
        boolean result = true;
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

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
        } else if (password.length() < 5 || password.length() > 30) {
            textInputLayoutPassword.setError("Panjang password minimal 5 karakter dan maksimal 30" +
                    " karakter");
            result = false;
        } else {
            textInputLayoutPassword.setErrorEnabled(false);
        }

        return result;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_sign_in) {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception ignored) {
            }
            signIn();
        } else if (i == R.id.link_signup) {
            startActivity(new Intent(SignIn.this, SignUp.class));
            finish();
        }
    }
}
