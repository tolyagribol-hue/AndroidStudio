package com.example.currnecyconeverterbyserega.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.currnecyconeverterbyserega.R;
import com.example.currnecyconeverterbyserega.ui.model.Currency;

import java.util.List;

public class CurrencySpinnerAdapter extends ArrayAdapter<Currency> {

    public CurrencySpinnerAdapter(@NonNull Context context, @NonNull List<Currency> currencies) {
        super(context, 0, currencies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    private View createViewFromResource(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_currency, parent, false);
        }

        Currency currency = getItem(position);
        TextView tvFlag = convertView.findViewById(R.id.tv_flag);
        TextView tvCode = convertView.findViewById(R.id.tv_code);
        TextView tvName = convertView.findViewById(R.id.tv_name);

        if (currency != null) {
            tvFlag.setText(currency.getFlagEmoji());
            tvCode.setText(currency.getCode());
            tvName.setText(currency.getName());
        }

        return convertView;
    }
}
