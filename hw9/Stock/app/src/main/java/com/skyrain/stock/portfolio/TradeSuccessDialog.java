package com.skyrain.stock.portfolio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.skyrain.stock.R;

public class TradeSuccessDialog extends AppCompatDialogFragment {
    private Integer shares;
    private String ticker;
    private String status;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getShares() {
        return shares;
    }

    public void setShares(Integer shares) {
        this.shares = shares;
    }

    public TradeSuccessDialog(Integer shares, String ticker,String status) {
        this.shares = shares;
        this.ticker = ticker;
        this.status=status;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View inflate = layoutInflater.inflate(R.layout.trade_success_dialog,null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView statusView = dialog.findViewById(R.id.trade_success_status);
        TextView sharesView = dialog.findViewById(R.id.trade_success_shares);
        TextView tickerView = dialog.findViewById(R.id.trade_success_ticker);
        Button done=dialog.findViewById(R.id.trade_suceess_done);

        statusView.setText(getStatus()+" ");
        sharesView.setText(getShares().toString());
        tickerView.setText(getTicker());

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        return dialog;
    }
}
