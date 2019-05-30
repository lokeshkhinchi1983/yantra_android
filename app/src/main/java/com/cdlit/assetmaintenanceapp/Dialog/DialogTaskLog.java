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

import com.cdlit.assetmaintenanceapp.Adapter.AdapterTasks;
import com.cdlit.assetmaintenanceapp.Model.TaskLog;
import com.cdlit.assetmaintenanceapp.R;

import java.util.ArrayList;

public class DialogTaskLog extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private ArrayList<TaskLog.Response> listTaskLog;
    private RecyclerView recyclerEquipmentType;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private AdapterTasks adapterTaskLog;
    private String okString;

    public static DialogTaskLog newInstance() {

        DialogTaskLog dialogTaskLog = new DialogTaskLog();
        return dialogTaskLog;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        listTaskLog = args.getParcelableArrayList("list_task_log");

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

        adapterTaskLog = new AdapterTasks(getActivity(), listTaskLog);

        recyclerEquipmentType.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEquipmentType.setLayoutManager(mLayoutManager);

        recyclerEquipmentType.setAdapter(adapterTaskLog);

        checkEmptyView(adapterTaskLog);

        // loadImages();

        if (title.equalsIgnoreCase(getResources().getString(R.string.add_task_log_dialog_title))) {

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

                if (title.equalsIgnoreCase(getResources().getString(R.string.add_task_log_dialog_title))) {

                    ((DialogRepairLog) getTargetFragment()).assignTaskLog(adapterTaskLog.listTaskLogId);

                } else {


                }

              /*  if (title.equalsIgnoreCase(getResources().getString(R.string.assign_equipment_type))) {

                    if (adapterEquipmentType.getItemCount() == 0) {

                        Utilities.showSnackbar(v, "No asset available to assign");

                    } else if (adapterEquipmentType.listAssignedIds.size() == 0) {

                        //    Snackbar.make(v, "Please select any Asset Category", Snackbar.LENGTH_SHORT).show();

                        Utilities.showSnackbar(v, "Please select any Asset Category");

                    } else {

                        ((FragmentEquipmentToUser) getTargetFragment()).assignEquipmentToUser(adapterEquipmentType.listAssignedEquipmentTypeIds);

                    }

                } else {


                }
*/


            }
        });

        return dialog;
    }

    private void checkEmptyView(AdapterTasks adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            tvEmptyItem.setText("No Task Log Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }


}
