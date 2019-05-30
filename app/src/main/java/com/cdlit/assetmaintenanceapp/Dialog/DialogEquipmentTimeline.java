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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Adapter.AdapterEquipmentTimeline;
import com.cdlit.assetmaintenanceapp.Model.EquipmentTimeline;
import com.cdlit.assetmaintenanceapp.R;

import java.util.ArrayList;

/**
 * Created by rakesh on 22-12-2017.
 */

public class DialogEquipmentTimeline extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAddLocation;
    private String okString;
    private ArrayList<EquipmentTimeline.EquipmentTimelineResponse> listEquipmentTimeline;
    private RecyclerView recyclerEquipmentTimeline;
    private ViewStub emptyView;
    private ImageView imageEmptyItem;
    private TextView tvEmptyItem;
    private AdapterEquipmentTimeline adapterEquipmentTimeline;

    public static DialogEquipmentTimeline newInstance() {

        DialogEquipmentTimeline dialogEquipmentTimeline = new DialogEquipmentTimeline();
        return dialogEquipmentTimeline;

    }

    @Override
    public void onStart() {

        super.onStart();

    }

    private void checkEmptyView(RecyclerView.Adapter adapter) {

        if (adapter.getItemCount() == 0) {

            Log.e("adapter", "null");

            emptyView.setVisibility(View.VISIBLE);

            tvEmptyItem.setText("No Equipment Timeline Available");

        } else {

            Log.e("adapter", " not null");

            emptyView.setVisibility(View.GONE);

        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        final String title = args.getString("title");

        listEquipmentTimeline = args.getParcelableArrayList("list_equipment_timeline");

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
     //   customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_equipment_timeline, null);

        recyclerEquipmentTimeline = (RecyclerView) view.findViewById(R.id.recycler_equipment_timeline);

        emptyView = (ViewStub) view.findViewById(R.id.empty_view);

        View emptyLayout = emptyView.inflate();

        imageEmptyItem = (ImageView) emptyLayout.findViewById(R.id.image_empty_item);

        tvEmptyItem = (TextView) emptyLayout.findViewById(R.id.tv_empty_item);

        adapterEquipmentTimeline = new AdapterEquipmentTimeline(getActivity(), listEquipmentTimeline);

        recyclerEquipmentTimeline.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerEquipmentTimeline.setLayoutManager(mLayoutManager);

        recyclerEquipmentTimeline.setAdapter(adapterEquipmentTimeline);

        checkEmptyView(adapterEquipmentTimeline);

        okString = getResources().getString(R.string.ok_bt_string);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        return dialog;
    }


}
