package com.example.bank;

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

public class UserProfileFragment extends Fragment {
    private MainViewModel viewModel;
    private EditText editName, editPhone, editAddress;
    private Button buttonSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        editName = root.findViewById(R.id.editName);
        editPhone = root.findViewById(R.id.editPhone);
        editAddress = root.findViewById(R.id.editAddress);
        buttonSave = root.findViewById(R.id.buttonSave);

        // Pre-fill fields with current user data
        User user = viewModel.getUser().getValue();
        if (user != null) {
            editName.setText(user.getName());
            editPhone.setText(user.getPhone());
            editAddress.setText(user.getAddress());
        }

        buttonSave.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newPhone = editPhone.getText().toString().trim();
            String newAddress = editAddress.getText().toString().trim();
            if (TextUtils.isEmpty(newName)) {
                editName.setError(getString(R.string.error_no_name));
                return;
            }
            if (TextUtils.isEmpty(newPhone)) {
                editPhone.setError(getString(R.string.error_no_phone));
                return;
            }
            // Update profile via ViewModel
            viewModel.updateUserProfile(newName, newPhone, newAddress);
            Toast.makeText(getContext(), getString(R.string.profile_saved), Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return root;
    }
}
