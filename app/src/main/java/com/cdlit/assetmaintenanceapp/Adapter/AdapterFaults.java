package com.cdlit.assetmaintenanceapp.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.FaultLog;
import com.cdlit.assetmaintenanceapp.R;

import java.util.ArrayList;

public class AdapterFaults extends RecyclerView.Adapter<AdapterFaults.MyViewHolder> {

    private final FragmentActivity activity;
    private final ArrayList<FaultLog.Response> listFaultLog;
    public ArrayList<Integer> listFaultLogId1;
    public final ArrayList<Integer> listFaultLogId;

    public AdapterFaults(FragmentActivity activity, ArrayList<FaultLog.Response> listFaultLog, ArrayList<Integer> listFaultLogId) {

        if (listFaultLogId != null && listFaultLogId.size() != 0) {
            Log.e("listFaultLogId", listFaultLogId.toString());
        }

        this.activity = activity;
        this.listFaultLog = listFaultLog;

        if (listFaultLogId != null && listFaultLogId.size() != 0) {
            this.listFaultLogId1 = new ArrayList<>(listFaultLogId);
        }

        this.listFaultLogId = new ArrayList<>();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fault_task_log_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final FaultLog.Response equipmentTypeResponse = listFaultLog.get(position);

        holder.tvFaultType.setText(equipmentTypeResponse.getCheckListName() + " (" + equipmentTypeResponse.getUserFirstName() + " " + equipmentTypeResponse.getUserLastName() + ")");

        holder.tvFaultDes.setText(equipmentTypeResponse.getUserComment());

        //   holder.tvFaultType.setText(equipmentTypeResponse.getRepairLog());

        Integer faultId = listFaultLog.get(position).getFaultLogId();

        Log.e("faultId", "" + faultId);

        if (listFaultLogId1 != null && listFaultLogId1.size() != 0) {

            if (listFaultLogId1.contains(faultId)) {
                Log.e("faultId", "contains " + faultId);
                holder.cbFaultType.setChecked(true);
            } else {
                Log.e("faultId", "not contains" + faultId);
                holder.cbFaultType.setChecked(false);
            }

        }


        holder.cbFaultType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    Integer faultLogId = listFaultLog.get(position).getFaultLogId();

                    Log.e("add faultLogId", "" + faultLogId);

                    listFaultLogId.add(faultLogId);

                } else {

                    Integer faultLogId = listFaultLog.get(position).getFaultLogId();

                    Log.e("add faultLogId", "" + faultLogId);

                    int position = 0;

                    for (int i = 0; i < listFaultLogId.size(); i++) {
                        if (listFaultLogId.get(i) == faultLogId) {
                            position = i;
                        }
                    }

                    listFaultLogId.remove(position);

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return listFaultLog.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvFaultType;
        private final CheckBox cbFaultType;
        private final TextView tvFaultDes;
        //  private final ImageView imageEquipmentType;

        public MyViewHolder(View view) {

            super(view);

            tvFaultType = (TextView) view.findViewById(R.id.tv_fault_type);
            tvFaultDes = (TextView) view.findViewById(R.id.tv_fault_des);
            cbFaultType = (CheckBox) view.findViewById(R.id.cb_fault_type);

        }

    }


}
