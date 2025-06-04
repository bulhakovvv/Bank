package com.example.bank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class LoginFragment extends Fragment {
    private MainViewModel viewModel;
    private EditText editUsername;
    private EditText editPassword;
    private Button buttonLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        editUsername = root.findViewById(R.id.editUsername);
        editPassword = root.findViewById(R.id.editPassword);
        buttonLogin = root.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameInput = editUsername.getText().toString().trim();
                String passwordInput = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(usernameInput) || TextUtils.isEmpty(passwordInput)) {
                    Toast.makeText(getContext(), getString(R.string.error_invalid_login), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Load user data from JSON and verify
                viewModel.loadUserData();
                User user = viewModel.getUser().getValue();
                if (user != null && usernameInput.equals(user.getUsername()) && passwordInput.equals(user.getPassword())) {
                    // Successful login
                    // Mark logged in
                    SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("logged_in", true).apply();
                    // Load other data
                    viewModel.loadTransactionsData();
                    viewModel.loadCreditsData();
                    // Navigate to HomeFragment
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                            .replace(R.id.container, new HomeFragment())
                            .commit();
                } else {
                    // Invalid credentials
                    Toast.makeText(getContext(), getString(R.string.error_invalid_login), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}
