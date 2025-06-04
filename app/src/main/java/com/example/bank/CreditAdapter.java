package com.example.bank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class CreditAdapter extends ArrayAdapter<Credit> {
    public CreditAdapter(Context context, List<Credit> credits) {
        super(context, 0, credits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Credit credit = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_credit, parent, false);
        }
        TextView nameText = convertView.findViewById(R.id.textCreditName);
        TextView infoText = convertView.findViewById(R.id.textCreditInfo);
        TextView scheduleText = convertView.findViewById(R.id.textCreditSchedule);
        if (credit != null) {
            nameText.setText(credit.getName());
            // Initial and remaining amounts
            String initialStr = String.format(Locale.getDefault(), getContext().getString(R.string.initial_amount), credit.getInitialAmount());
            String remainingStr = String.format(Locale.getDefault(), getContext().getString(R.string.remaining_amount), credit.getRemainingAmount());
            infoText.setText(initialStr + "\n" + remainingStr);
            // Build schedule string
            StringBuilder scheduleBuilder = new StringBuilder();
            scheduleBuilder.append(getContext().getString(R.string.payment_schedule)).append("\n");
            List<Payment> payments = credit.getSchedule();
            for (int i = 0; i < payments.size(); i++) {
                Payment p = payments.get(i);
                scheduleBuilder.append("- ").append(p.getDate()).append(": ");
                scheduleBuilder.append(String.format(Locale.getDefault(), "%.2f", p.getAmount()));
                if (i < payments.size() - 1) scheduleBuilder.append("\n");
            }
            scheduleText.setText(scheduleBuilder.toString());
        }
        return convertView;
    }
}
