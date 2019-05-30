package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.cdlit.assetmaintenanceapp.Model.CategoryResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentCategory;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogAddCategory extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAddCategory;
    private String okString;

    public static DialogAddCategory newInstance() {

        DialogAddCategory dialogAddCategory = new DialogAddCategory();
        return dialogAddCategory;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        final String title = args.getString("title");

        final CategoryResponse categoryResponse = (CategoryResponse) args.getParcelable("category_response");

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        alertDialogBuilder.setTitle("" + title);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_category, null);

        etAddCategory = (EditText) view.findViewById(R.id.et_add_category);

        if (title.equalsIgnoreCase(getResources().getString(R.string.editCategory_dialog_title))) {

            etAddCategory.setText("" + categoryResponse.getCategoryName());
            etAddCategory.setHint("Update Category");
            okString = getResources().getString(R.string.update_bt_string);

        } else {

            etAddCategory.setHint("Add Category");
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
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.editCategory_dialog_title))) {

                    if (etAddCategory.getText().toString().equalsIgnoreCase(categoryResponse.getCategoryName())) {

                        //   Snackbar.make(v, getResources().getString(R.string.not_any_change), Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, getActivity().getResources().getString(R.string.not_any_change));

                    } else if (etAddCategory.getText().toString().equalsIgnoreCase("")) {

                        //    Snackbar.make(v, "Please update category", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please update category");

                    } else {

                        ((FragmentCategory) getTargetFragment()).editCategoryPositiveClick(etAddCategory.getText().toString(), categoryResponse);

                    }

                } else {


                    if (etAddCategory.getText().toString().equalsIgnoreCase("")) {

                        //    Snackbar.make(v, "Please add category", Snackbar.LENGTH_LONG).show();
                        Utilities.showSnackbar(v, "Please add category");
                    } else {

                        ((FragmentCategory) getTargetFragment()).addCategoryPositiveClick(etAddCategory.getText().toString());

                    }

                }


            }
        });

        return dialog;
    }


}
