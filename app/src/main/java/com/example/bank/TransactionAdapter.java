package com.example.bank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends ArrayAdapter<Transaction> {
    public TransactionAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_transaction, parent, false);
        }
        TextView infoText = convertView.findViewById(R.id.textTransactionInfo);
        TextView amountText = convertView.findViewById(R.id.textTransactionAmount);
        if (transaction != null) {
            String info = transaction.getDate() + " - " + transaction.getDescription();
            infoText.setText(info);
            amountText.setText(String.format(Locale.getDefault(), "%.2f", transaction.getAmount()));
        }
        return convertView;
    }
}
