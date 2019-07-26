package com.fbasegizi.statusgizi.fragment.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.UbahPassword;
import com.google.android.material.snackbar.Snackbar;

public class AccountSettings extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Pengaturan");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AccountSettingsFragment())
                .commit();

        Intent intent = getIntent();
        intent.getExtras();
        if (intent.hasExtra("profile_change")) {
            Snackbar.make(AccountSettings.this.findViewById(android.R.id.content),
                    "Sukses mengubah profil",
                    Snackbar.LENGTH_LONG).show();
        } else if (intent.hasExtra("password_change")) {
            Snackbar.make(AccountSettings.this.findViewById(android.R.id.content),
                    "Silahkan cek Email Anda! Untuk instruksi selanjutnya!",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    public static class AccountSettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.pref_account);

            Preference preferenceProfil = findPreference(getString(R.string.key_profil));
            Preference preferenceReset = findPreference(getString(R.string.key_password));

            preferenceProfil.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {
                    Intent intent = new Intent(getActivity(), UbahProfil.class);
                    startActivity(intent);
                    return true;
                }
            });

            preferenceReset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), UbahPassword.class);
                    intent.putExtra("reset_pass", "sign_in");
                    startActivity(intent);
                    return true;
                }
            });
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
}
