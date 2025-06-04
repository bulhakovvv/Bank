package com.example.bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load theme and language from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        String lang = prefs.getString("language", "");
        if (!lang.isEmpty()) {
            // Apply selected locale
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
        AppCompatDelegate.setDefaultNightMode(darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ensure JSON data files exist in internal storage
        copyAssetToInternal("user.json");
        copyAssetToInternal("transactions.json");
        copyAssetToInternal("credits.json");

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // If user already logged in (flag in SharedPreferences), skip login
        boolean loggedIn = prefs.getBoolean("logged_in", false);
        if (savedInstanceState == null) {
            if (loggedIn) {
                // Load data and go directly to HomeFragment
                viewModel.loadUserData();
                viewModel.loadTransactionsData();
                viewModel.loadCreditsData();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
            } else {
                // Show LoginFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new LoginFragment())
                        .commit();
            }
        }
    }

    private void copyAssetToInternal(String filename) {
        File outFile = new File(getFilesDir(), filename);
        if (outFile.exists()) return;
        try {
            InputStream is = getAssets().open(filename);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
