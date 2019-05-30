package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityNavigationDrawerAdmin;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogLogout extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvDeleteMsg;

    public static DialogLogout newInstance() {

        DialogLogout dialogLogout = new DialogLogout();
        return dialogLogout;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String title = args.getString("title");
        String msg = args.getString("msg");

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

      /*  alertDialogBuilder.setTitle("" + title);

        alertDialogBuilder.setMessage("" + msg);*/


        // alertDialogBuilder.setTitle("" + title);
        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
     //   customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_delete, null);
        tvDeleteMsg = (TextView) view.findViewById(R.id.tv_delete_msg);
        alertDialogBuilder.setView(view);

        tvDeleteMsg.setText("" + msg);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.bt_positive_logout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((ActivityNavigationDrawerAdmin) getActivity()).logoutPositiveClick();

            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_fad_in_out;
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //  dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        return dialog;
    }
}
