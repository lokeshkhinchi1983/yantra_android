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

import com.cdlit.assetmaintenanceapp.Model.EquipmentFalulList;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentFaultLog;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

public class DialogAddFaultLogComment extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;

    private String okString;
    private EditText etAddFaultLogComment;
    private String comment;

    public static DialogAddFaultLogComment newInstance() {

        DialogAddFaultLogComment dialogAddFaultLogComment = new DialogAddFaultLogComment();
        return dialogAddFaultLogComment;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        final EquipmentFalulList.Response response = (EquipmentFalulList.Response) args.getParcelable("response");

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);


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

        // alertDialogBuilder.setTitle("" + title);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_fault_log_comment, null);

        etAddFaultLogComment = (EditText) view.findViewById(R.id.et_add_fault_log_comment);

        if (title.equalsIgnoreCase(getResources().getString(R.string.edit_fault_log_comment_dialog_title))) {

            etAddFaultLogComment.setText("" + response.getFaultLogComments().get(0).getComment());
            etAddFaultLogComment.setHint("Update Comment");
            okString = getResources().getString(R.string.update_bt_string);

        } else if (title.equalsIgnoreCase(getResources().getString(R.string.add_fault_log_comment_dialog_title))) {


            if (response.getFaultLogComments() == null) {

                comment = "";

            } else if (response.getFaultLogComments().size() == 0) {

                comment = "";

            } else {

                comment = response.getFaultLogComments().get(0).getComment();

            }

            etAddFaultLogComment.setText("" + comment);
            etAddFaultLogComment.setHint("Add Comment");
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


                if (title.equalsIgnoreCase(getResources().getString(R.string.editCategory_dialog_title))) {


                    if (etAddFaultLogComment.getText().toString().equalsIgnoreCase(comment)) {

                        //   Snackbar.make(v, getResources().getString(R.string.not_any_change), Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, getActivity().getResources().getString(R.string.not_any_change));

                    } else if (etAddFaultLogComment.getText().toString().equalsIgnoreCase("")) {

                        //    Snackbar.make(v, "Please update category", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please update category");

                    } else {

                        ((FragmentFaultLog) getTargetFragment()).editCommentPositiveClick(etAddFaultLogComment.getText().toString(), response);

                    }


                } else {


                    if (etAddFaultLogComment.getText().toString().equalsIgnoreCase(comment)) {

                        Utilities.showSnackbar(v, getActivity().getResources().getString(R.string.not_any_change));

                    } else if (etAddFaultLogComment.getText().toString().equalsIgnoreCase("")) {

                        Utilities.showSnackbar(v, "Please add comment");

                    } else {

                        ((FragmentFaultLog) getTargetFragment()).addCommentPositiveClick(etAddFaultLogComment.getText().toString(), response);

                    }


                }


            }
        });

        return dialog;
    }


}
