package com.example.bank;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.List;

public class CreditsFragment extends Fragment {
    private MainViewModel viewModel;
    private ListView listCredits;
    private CreditAdapter creditAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_credits, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        listCredits = root.findViewById(R.id.listCredits);

        // Load credits list from ViewModel
        List<Credit> credits = viewModel.getCredits().getValue();
        if (credits != null) {
            creditAdapter = new CreditAdapter(getContext(), credits);
            listCredits.setAdapter(creditAdapter);
        }
        return root;
    }
}
