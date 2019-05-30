package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterEmail;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

import java.util.ArrayList;

public class DialogAddEmail extends DialogFragment {

    private String okString;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAddEmail;
    private RecyclerView recyclerEmailId;
    private FloatingActionButton fabAddEmail;
    private ArrayList<String> listEmail;
    private AdapterEmail adapterEmail;

    public static DialogAddEmail newInstance() {

        DialogAddEmail dialogAddEmail = new DialogAddEmail();
        return dialogAddEmail;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //  return super.onCreateDialog(savedInstanceState);

        Bundle args = getArguments();
        final String title = args.getString("title");
        listEmail = args.getStringArrayList("list_email");

        if (title.equalsIgnoreCase(getResources().getString(R.string.dialog_update_email_title))) {

            okString = getResources().getString(R.string.update_bt_string);

        } else {

            okString = getResources().getString(R.string.add_bt_string);

        }

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        // alertDialogBuilder.setTitle("" + title);
        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        // customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_email, null);

        etAddEmail = (EditText) view.findViewById(R.id.et_add_email);

        fabAddEmail = (FloatingActionButton) view.findViewById(R.id.fab_add_email);

        recyclerEmailId = (RecyclerView) view.findViewById(R.id.recycler_email_id);

        adapterEmail = new AdapterEmail(this, listEmail);

        recyclerEmailId.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEmailId.setLayoutManager(mLayoutManager);

        recyclerEmailId.setAdapter(adapterEmail);

        fabAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etAddEmail.getText().toString().equals("")) {

                    //     Snackbar.make(v, "Please add email", Snackbar.LENGTH_SHORT).show();

                    Utilities.showSnackbar(v, "Please add email");

                } else if (!isValidEmail(etAddEmail.getText().toString())) {

                    //     Snackbar.make(v, "Please enter valid email id (eg: example@email.com)", Snackbar.LENGTH_SHORT).show();

                    Utilities.showSnackbar(v, "Please enter valid email id (eg: example@email.com)");

                } else {

                    String email = etAddEmail.getText().toString().trim();

                    if (listEmail.contains(email)) {

                        //   Snackbar.make(v, "This " + email + " is already exist, please add another email id", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "This " + email + " is already exist, please add another email id");

                    } else {

                        listEmail.add(email);

                        adapterEmail.notifyDataSetChanged();

                        etAddEmail.setText("");

                    }
                }

            }
        });


        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //   listEmail.clear();

            }
        });


        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;
        dialog.show();


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  if (title.equalsIgnoreCase(getResources().getString(R.string.dialog_add_email_title))) {

                    if (listEmail == null || listEmail.size() == 0) {

                        // Snackbar.make(v, "Please add any email id", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please add any email id");

                    } else {

                        ((DialogAddEquipmentModelRequired) getTargetFragment()).addEmail(adapterEmail.listEmail);

                    }

             //   } else {


               // }


            }
        });

        return dialog;

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}
