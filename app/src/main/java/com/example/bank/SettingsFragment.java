package com.example.bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class SettingsFragment extends Fragment {
    private RadioButton radioLight, radioDark, radioEnglish, radioUkrainian;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        radioLight = root.findViewById(R.id.radioLight);
        radioDark = root.findViewById(R.id.radioDark);
        radioEnglish = root.findViewById(R.id.radioEnglish);
        radioUkrainian = root.findViewById(R.id.radioUkrainian);

        // Load current preferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        String lang = prefs.getString("language", "");
        // Set initial selected options
        if (darkMode) {
            radioDark.setChecked(true);
        } else {
            radioLight.setChecked(true);
        }
        if (lang.isEmpty()) {
            // If not set, check device language
            String deviceLang = Locale.getDefault().getLanguage();
            if (deviceLang.startsWith("uk")) {
                radioUkrainian.setChecked(true);
            } else {
                radioEnglish.setChecked(true);
            }
        } else if (lang.equals("uk")) {
            radioUkrainian.setChecked(true);
        } else {
            radioEnglish.setChecked(true);
        }

        // Theme change listeners
        radioLight.setOnClickListener(v -> {
            prefs.edit().putBoolean("dark_mode", false).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            requireActivity().recreate();
        });
        radioDark.setOnClickListener(v -> {
            prefs.edit().putBoolean("dark_mode", true).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            requireActivity().recreate();
        });
        // Language change listeners
        radioEnglish.setOnClickListener(v -> {
            prefs.edit().putString("language", "en").apply();
            requireActivity().recreate();
        });
        radioUkrainian.setOnClickListener(v -> {
            prefs.edit().putString("language", "uk").apply();
            requireActivity().recreate();
        });

        return root;
    }
}
