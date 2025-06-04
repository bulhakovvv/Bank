package com.example.bank;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    private MainViewModel viewModel;
    private TextView textWelcome;
    private Button buttonAccount, buttonTransactions, buttonTransfer, buttonCredits, buttonSettings, buttonProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        textWelcome = root.findViewById(R.id.textWelcome);
        buttonAccount = root.findViewById(R.id.buttonAccount);
        buttonTransactions = root.findViewById(R.id.buttonTransactions);
        buttonTransfer = root.findViewById(R.id.buttonTransfer);
        buttonCredits = root.findViewById(R.id.buttonCredits);
        buttonSettings = root.findViewById(R.id.buttonSettings);
        buttonProfile = root.findViewById(R.id.buttonProfile);

        // Set welcome text with user name
        User user = viewModel.getUser().getValue();
        if (user != null) {
            textWelcome.setText(getString(R.string.welcome, user.getName()));
        }

        // Navigation button listeners
        buttonAccount.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, new AccountFragment())
                    .addToBackStack(null)
                    .commit();
        });
        buttonTransactions.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, new TransactionsFragment())
                    .addToBackStack(null)
                    .commit();
        });
        buttonTransfer.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, new TransferFragment())
                    .addToBackStack(null)
                    .commit();
        });
        buttonCredits.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, new CreditsFragment())
                    .addToBackStack(null)
                    .commit();
        });
        buttonSettings.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        });
        buttonProfile.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, new UserProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return root;
    }
}
