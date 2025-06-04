package com.example.bank;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    private MainViewModel viewModel;
    private TextView textBalance;
    private ListView listRecentTransactions;
    private TransactionAdapter transactionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        textBalance = root.findViewById(R.id.textBalance);
        listRecentTransactions = root.findViewById(R.id.listRecentTransactions);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                textBalance.setText(getString(R.string.balance, user.getBalance()));
            }
        });
        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactions -> {
            if (transactions != null) {
                List<Transaction> displayList = transactions;
                if (transactions.size() > 5) {
                    displayList = new ArrayList<>(transactions.subList(0, 5));
                }
                if (transactionAdapter == null) {
                    transactionAdapter = new TransactionAdapter(getContext(), displayList);
                    listRecentTransactions.setAdapter(transactionAdapter);
                } else {
                    transactionAdapter.clear();
                    transactionAdapter.addAll(displayList);
                    transactionAdapter.notifyDataSetChanged();
                }
            }
        });

        return root;
    }
}
