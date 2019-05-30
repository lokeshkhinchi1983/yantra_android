package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterFaults;
import com.cdlit.assetmaintenanceapp.Model.FaultLog;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

import java.util.ArrayList;

public class DialogFaultLog extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;

    private ArrayList<FaultLog.Response> listFaultLog;
    private RecyclerView recyclerEquipmentType;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private String okString;
    private AdapterFaults adapterFaultLog;
    private ArrayList<Integer> listFaultLogId;

    public static DialogFaultLog newInstance() {

        DialogFaultLog dialogFaultTaskLog = new DialogFaultLog();
        return dialogFaultTaskLog;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        listFaultLog = args.getParcelableArrayList("list_fault_log");
        listFaultLogId = args.getIntegerArrayList("list_fault_log_id");
        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        // customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_assign_equipment_user, null);

        recyclerEquipmentType = (RecyclerView) view.findViewById(R.id.recycler_equipment_type);

        emptyView = (ViewStub) view.findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

      //  listFaultLogId = new ArrayList<>();

        adapterFaultLog = new AdapterFaults(getActivity(), listFaultLog, listFaultLogId);

        recyclerEquipmentType.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEquipmentType.setLayoutManager(mLayoutManager);

        recyclerEquipmentType.setAdapter(adapterFaultLog);

        checkEmptyView(adapterFaultLog);

        // loadImages();

        if (title.equalsIgnoreCase(getResources().getString(R.string.add_fault_log_dialog_title))) {

            okString = getResources().getString(R.string.add_bt_string);

        } else {

            okString = getResources().getString(R.string.update_bt_string);

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

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (title.equalsIgnoreCase(getResources().getString(R.string.add_fault_log_dialog_title))) {

                    if (adapterFaultLog.listFaultLogId == null || adapterFaultLog.listFaultLogId.size() == 0) {

                        // Snackbar.make(v, "Please add any email id", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please select any fault log");

                    } else {

                        //   adapterFaultLog.listFaultLogId.toString()

                        Log.e("adapterFaultLog.listFaultLogId.toString()", "" + adapterFaultLog.listFaultLogId.toString());

                        ((DialogRepairLog) getTargetFragment()).assignFaultLog(adapterFaultLog.listFaultLogId);

                        dialog.dismiss();

                    }


                } else {


                }


            }
        });

        return dialog;
    }

    private void checkEmptyView(AdapterFaults adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            tvEmptyItem.setText("No Fault Log Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }


    }

}
