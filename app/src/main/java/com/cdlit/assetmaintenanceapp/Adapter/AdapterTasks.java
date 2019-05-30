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

import com.cdlit.assetmaintenanceapp.Model.TaskLog;
import com.cdlit.assetmaintenanceapp.R;

import java.util.ArrayList;

public class AdapterTasks extends RecyclerView.Adapter<AdapterTasks.MyViewHolder> {

    private final FragmentActivity activity;
    private final ArrayList<TaskLog.Response> listTaskLog;
    public final ArrayList<Integer> listTaskLogId;


    public AdapterTasks(FragmentActivity activity, ArrayList<TaskLog.Response> listTaskLog) {
        this.activity = activity;
        this.listTaskLog = listTaskLog;
        listTaskLogId = new ArrayList<Integer>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_log_list_item, parent, false);

        return new AdapterTasks.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final AdapterTasks.MyViewHolder holder, final int position) {

        final TaskLog.Response equipmentTypeResponse = listTaskLog.get(position);

        holder.tvTaskDes.setText(equipmentTypeResponse.getDescription());
        //   holder.tvFaultType.setText(equipmentTypeResponse.getRepairLog());


      /*  if (listAssignedEquipmentTypeIds.contains(equipmentTypeResponse)) {

            holder.cbFaultType.setChecked(true);

        } else {

            holder.cbFaultType.setChecked(false);

        }
*/


        holder.cbTaskType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    Integer taskLogId = listTaskLog.get(position).getId();

                    Log.e("add taskLogId", "" + taskLogId);

                    listTaskLogId.add(taskLogId);


                } else {

                    Integer faultLogId = listTaskLog.get(position).getId();

                    Log.e("add faultLogId", "" + faultLogId);

                    int position = 0;

                    for (int i = 0; i < listTaskLog.size(); i++) {
                        if (listTaskLogId.get(i) == faultLogId) {

                            position = i;
                        }
                    }

                    listTaskLogId.remove(position);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return listTaskLog.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox cbTaskType;
        private final TextView tvTaskDes;
        //  private final ImageView imageEquipmentType;

        public MyViewHolder(View view) {
            super(view);

            tvTaskDes = (TextView) view.findViewById(R.id.tv_task_des);
            cbTaskType = (CheckBox) view.findViewById(R.id.cb_task_type);
            //    imageEquipmentType = (ImageView) view.findViewById(R.id.image_equipment_type);

        }

    }


}
