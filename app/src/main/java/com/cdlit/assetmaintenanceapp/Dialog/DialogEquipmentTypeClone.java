package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipment;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogEquipmentTypeClone extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAddCategory;
    private String okString;
    private TextView tvEquipmentType;
    private EditText etEquipmentType;
    private Spinner spinnerLocation;

    public static DialogEquipmentTypeClone newInstance() {

        DialogEquipmentTypeClone dialogAddTypeClone = new DialogEquipmentTypeClone();

        return dialogAddTypeClone;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        final EquipmentResponse equipmentTypeResponse = (EquipmentResponse) args.getParcelable("equipment_type_response");

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        //  alertDialogBuilder.setTitle("" + title);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        //  customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_equipment_type_clone, null);

        tvEquipmentType = (TextView) view.findViewById(R.id.tv_equipment_type);

        etEquipmentType = (EditText) view.findViewById(R.id.et_equipment_type);

        tvEquipmentType.setText("" + equipmentTypeResponse.getModelNo());

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.add_bt_string), new DialogInterface.OnClickListener() {
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
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etEquipmentType.getText().toString().equalsIgnoreCase("")) {

                    // Snackbar.make(v, "Please add new equipment clone", Snackbar.LENGTH_LONG).show();

                    Utilities.showSnackbar(v, "Please add new equipment clone");

                } else {

                    ((FragmentEquipment) getTargetFragment()).addEquipmentTypeClone(etEquipmentType.getText().toString(), equipmentTypeResponse);

                }

            }
        });

        return dialog;
    }


}
