package com.example.bank;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment {
    private MainViewModel viewModel;
    private EditText editFilterDate;
    private Button buttonFilter;
    private ListView listTransactions;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> allTransactions = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transactions, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        editFilterDate = root.findViewById(R.id.editFilterDate);
        buttonFilter = root.findViewById(R.id.buttonFilter);
        listTransactions = root.findViewById(R.id.listTransactions);

        // Get all transactions from ViewModel
        List<Transaction> transactions = viewModel.getTransactions().getValue();
        if (transactions != null) {
            allTransactions = transactions;
        }
        // Set initial full list in adapter
        transactionAdapter = new TransactionAdapter(getContext(), new ArrayList<>(allTransactions));
        listTransactions.setAdapter(transactionAdapter);

        buttonFilter.setOnClickListener(v -> {
            String filterText = editFilterDate.getText().toString().trim();
            List<Transaction> filtered = new ArrayList<>();
            if (TextUtils.isEmpty(filterText)) {
                // Show all transactions if filter is empty
                filtered.addAll(allTransactions);
            } else {
                for (Transaction t : allTransactions) {
                    if (t.getDate().contains(filterText)) {
                        filtered.add(t);
                    }
                }
            }
            if (filtered.isEmpty()) {
                Toast.makeText(getContext(), getString(R.string.no_transactions), Toast.LENGTH_SHORT).show();
            }
            transactionAdapter.clear();
            transactionAdapter.addAll(filtered);
            transactionAdapter.notifyDataSetChanged();
        });

        return root;
    }
}
