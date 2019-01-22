package com.fbasegizi.statusgizi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.fbasegizi.statusgizi.BaseActivity;
import com.fbasegizi.statusgizi.R;
import com.fbasegizi.statusgizi.UbahPassword;

public class AccountSetingsActivity extends BaseActivity {

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
    }

    public static class AccountSettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.pref_account);

            Preference preferenceLogOut = findPreference(getString(R.string.key_name));
            Preference preferenceReset = findPreference(getString(R.string.key_password));

            preferenceLogOut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {

                    return true;
                }
            });

            preferenceReset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), UbahPassword.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
}
