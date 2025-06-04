package com.example.bank;

import android.content.Context;
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

public class TransferFragment extends Fragment {
    private MainViewModel viewModel;
    private EditText editRecipient;
    private EditText editAmount;
    private Button buttonSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transfer, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        editRecipient = root.findViewById(R.id.editRecipient);
        editAmount = root.findViewById(R.id.editAmount);
        buttonSend = root.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(v -> {
            String recipient = editRecipient.getText().toString().trim();
            String amountStr = editAmount.getText().toString().trim();
            if (TextUtils.isEmpty(recipient)) {
                editRecipient.setError(getString(R.string.error_no_recipient));
                return;
            }
            if (TextUtils.isEmpty(amountStr)) {
                editAmount.setError(getString(R.string.error_invalid_amount));
                return;
            }
            double amount = 0;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                editAmount.setError(getString(R.string.error_invalid_amount));
                return;
            }
            if (amount <= 0) {
                editAmount.setError(getString(R.string.error_invalid_amount));
                return;
            }
            User user = viewModel.getUser().getValue();
            if (user != null && amount > user.getBalance()) {
                editAmount.setError(getString(R.string.error_insufficient_funds));
                return;
            }
            boolean success = viewModel.makeTransfer(recipient, amount);
            if (success) {
                Toast.makeText(getContext(), getString(R.string.transfer_success), Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), getString(R.string.error_invalid_amount), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
