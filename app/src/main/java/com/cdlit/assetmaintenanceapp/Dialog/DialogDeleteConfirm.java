package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.AddEquipmentModel3;
import com.cdlit.assetmaintenanceapp.Model.AssignedEquipment;
import com.cdlit.assetmaintenanceapp.Model.CategoryResponse;
import com.cdlit.assetmaintenanceapp.Model.EquipmentChecklistResponse;
import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.Model.EquipmentTypeResponse;
import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.Model.RepairLog;
import com.cdlit.assetmaintenanceapp.Model.UserResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentCategory;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipment;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipmentCheckList;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipmentToUser;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipmentType;
import com.cdlit.assetmaintenanceapp.Ui.FragmentLocation;
import com.cdlit.assetmaintenanceapp.Ui.FragmentManageUser;
import com.cdlit.assetmaintenanceapp.Ui.FragmentRepairLog;

/**
 * Created by rakesh on 19-06-2017.
 */

public class DialogDeleteConfirm extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvDeleteMsg;
    private String item;

    public static DialogDeleteConfirm newInstance() {

        DialogDeleteConfirm dialogDeleteConfirm = new DialogDeleteConfirm();
        return dialogDeleteConfirm;

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final Parcelable response = args.getParcelable("response");
        item = args.getString("item", "");
        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        String title = getResources().getString(R.string.delete_dialog_title);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here

        if (getTargetRequestCode() == 0) {

            customTitle.setText(title + " location");

        } else if (getTargetRequestCode() == 1) {

            customTitle.setText(title + " category");

        } else if (getTargetRequestCode() == 2) {

            customTitle.setText(title + " asset category");

        } else if (getTargetRequestCode() == 3) {

            customTitle.setText(title + " checklist");

        } else if (getTargetRequestCode() == 4) {

            customTitle.setText(title + " asset");

        } else if (getTargetRequestCode() == 5) {

            customTitle.setText(title + " asset");

        } else if (getTargetRequestCode() == 6) {

            customTitle.setText(title + " personnel");

        } else if (getTargetRequestCode() == 7) {

            customTitle.setText(title + " maintenance log");

        }
        else if (getTargetRequestCode() == 8) {

            customTitle.setText(title + " asset service");

        }

     //   customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));
        alertDialogBuilder.setCustomTitle(customTitle);

        //  alertDialogBuilder.setTitle("" + title);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_delete, null);
        tvDeleteMsg = (TextView) view.findViewById(R.id.tv_delete_msg);
        alertDialogBuilder.setView(view);


        tvDeleteMsg.setText("" + getResources().getString(R.string.delete_dislog_msg) + " " + item + " ?");
        //  alertDialogBuilder.setMessage("" + getResources().getString(R.string.delete_dislog_msg));

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.bt_delete_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (getTargetRequestCode() == 0) {

                    if (response == null) {

                        ((FragmentLocation) getTargetFragment()).multiDeletePositiveClick();
                    } else {

                        ((FragmentLocation) getTargetFragment()).deletePositiveClick((LocationResponse) response);

                    }

                } else if (getTargetRequestCode() == 1) {

                    ((FragmentCategory) getTargetFragment()).deletePositiveClick((CategoryResponse) response);

                } else if (getTargetRequestCode() == 2) {


                    if (response == null) {

                        //  ((FragmentLocation) getTargetFragment()).multiDeletePositiveClick();

                        ((FragmentEquipmentType) getTargetFragment()).multiDeletePositiveClick();


                    } else {

                        ((FragmentEquipmentType) getTargetFragment()).deletePositiveClick((EquipmentTypeResponse) response);

                    }


                } else if (getTargetRequestCode() == 3) {


                    if (response == null) {

                        ((FragmentEquipmentCheckList) getTargetFragment()).multiDeletePositiveClick();


                    } else {
                        ((FragmentEquipmentCheckList) getTargetFragment()).deletePositiveClick((EquipmentChecklistResponse) response);

                    }


                } else if (getTargetRequestCode() == 4) {

                    if (response == null) {

                        ((FragmentEquipment) getTargetFragment()).multiDeletePositiveClick();
                    } else {

                        ((FragmentEquipment) getTargetFragment()).deletePositiveClick((EquipmentResponse) response);

                    }

                } else if (getTargetRequestCode() == 5) {

                    if (response == null) {

                        ((FragmentEquipmentToUser) getTargetFragment()).multiDeletePositiveClick();

                    } else {

                        ((FragmentEquipmentToUser) getTargetFragment()).deletePositiveClick((AssignedEquipment.Response) response);

                    }

                } else if (getTargetRequestCode() == 6) {

                    if (response == null) {
                        ((FragmentManageUser) getTargetFragment()).multiDeletePositiveClick();
                    } else {
                        ((FragmentManageUser) getTargetFragment()).deletePositiveClick((UserResponse) response);
                    }

                } else if (getTargetRequestCode() == 7) {

                    if (response == null) {
                        ((FragmentRepairLog) getTargetFragment()).multiDeletePositiveClick();
                    } else {
                        ((FragmentRepairLog) getTargetFragment()).deletePositiveClick((RepairLog.RepairlogResponse) response);
                    }

                } else if (getTargetRequestCode() == 8) {

                    ((DialogEquipmentModel3) getTargetFragment()).deletePositiveClick((AddEquipmentModel3) response);

                }
            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (getTargetRequestCode() == 0) {
                    if (response != null) {
                        ((FragmentLocation) getTargetFragment()).deleteNegativeClick((LocationResponse) response);
                    }
                }

                if (getTargetRequestCode() == 2) {

                    if (response != null) {
                        ((FragmentEquipmentType) getTargetFragment()).deleteNegativeClick((EquipmentTypeResponse) response);
                    }
                }

                if (getTargetRequestCode() == 3) {

                    if (response != null) {
                        ((FragmentEquipmentCheckList) getTargetFragment()).deleteNegativeClick((EquipmentChecklistResponse) response);
                    }
                }

                if (getTargetRequestCode() == 4) {

                    if (response != null) {
                        ((FragmentEquipment) getTargetFragment()).deleteNegativeClick((EquipmentResponse) response);
                    }

                }
                if (getTargetRequestCode() == 5) {

                    if (response != null) {
                        ((FragmentEquipmentToUser) getTargetFragment()).deleteNegativeClick((AssignedEquipment.Response) response);
                    }

                }

                if (getTargetRequestCode() == 6) {

                    if (response != null) {
                        ((FragmentManageUser) getTargetFragment()).deleteNegativeClick((UserResponse) response);
                    }

                }

                if (getTargetRequestCode() == 7) {

                    if (response != null) {
                        ((FragmentRepairLog) getTargetFragment()).deleteNegativeClick((RepairLog.RepairlogResponse) response);
                    }

                }

                if (getTargetRequestCode() == 8) {

                   /* if (response != null) {
                        ((FragmentRepairLog) getTargetFragment()).deleteNegativeClick((RepairLog.RepairlogResponse) response);
                    }*/

                   dialog.dismiss();

                }

            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_fad_in_out;

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return dialog;

    }
}
