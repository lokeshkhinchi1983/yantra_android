package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentLocation;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogAddLocation extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAddLocation;
    private String okString;
    public View view;

    public static DialogAddLocation newInstance() {

        DialogAddLocation dialogAddLocation = new DialogAddLocation();
        return dialogAddLocation;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        final LocationResponse locationResponse = (LocationResponse) args.getParcelable("location_response");

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        //   alertDialogBuilder.setTitle("" + title);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        //customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));
        //getResources().getDimension(R.dimen.text_size_medium)
        alertDialogBuilder.setCustomTitle(customTitle);

         view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_location, null);

        etAddLocation = (EditText) view.findViewById(R.id.et_add_location);

        if (title.equalsIgnoreCase(getResources().getString(R.string.editLocation_dialog_title))) {

            //    etAddLocation.setHint("Update location");
            etAddLocation.setText("" + locationResponse.getLocationName());
            okString = getResources().getString(R.string.update_bt_string);

        } else {

            //   etAddLocation.setHint("Add location");
            okString = getResources().getString(R.string.add_bt_string);
        }

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;
        //  dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();



        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.editLocation_dialog_title))) {

                    if (etAddLocation.getText().toString().equalsIgnoreCase(locationResponse.getLocationName())) {

                        Utilities.showSnackbar(v, getResources().getString(R.string.not_any_change));

                    } else if (etAddLocation.getText().toString().equalsIgnoreCase("")) {


                        Utilities.showSnackbar(v, "Please update location");

                    } else {

                        ((FragmentLocation) getTargetFragment()).updateLocationPositiveClick(etAddLocation.getText().toString(), locationResponse);

                    }

                } else {

                    if (etAddLocation.getText().toString().equalsIgnoreCase("")) {

                        //Snackbar.make(v, "Please add location", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add location");
                    } else {

                        ((FragmentLocation) getTargetFragment()).addLocationPositiveClick(etAddLocation.getText().toString());

                    }

                }


            }
        });

        return dialog;
    }


}
