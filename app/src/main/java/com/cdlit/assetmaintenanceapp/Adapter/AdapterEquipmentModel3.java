package com.cdlit.assetmaintenanceapp.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogEquipmentModel3;
import com.cdlit.assetmaintenanceapp.Model.AddEquipmentModel3;
import com.cdlit.assetmaintenanceapp.R;

import java.util.ArrayList;

public class AdapterEquipmentModel3 extends RecyclerView.Adapter<AdapterEquipmentModel3.MyViewHolder> {

    private final FragmentActivity activity;
    private final DialogEquipmentModel3 context;
    public final ArrayList<AddEquipmentModel3> listEquipmentModel3;

    public AdapterEquipmentModel3(FragmentActivity activity, DialogEquipmentModel3 context, ArrayList<AddEquipmentModel3> listEquipmentModel3) {

        this.activity = activity;
        this.context = context;
        this.listEquipmentModel3 = listEquipmentModel3;

    }

    @Override
    public int getItemCount() {

        return listEquipmentModel3.size();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.equipment_model_3_listitem, parent, false);

        return new AdapterEquipmentModel3.MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvServiceName.setText(listEquipmentModel3.get(position).getServiceName() + " / " + listEquipmentModel3.get(position).getServiceFreq() + " / " + listEquipmentModel3.get(position).getServiceNo());


        //    holder.tvServiceFreq.setText(listEquipmentModel3.get(position).getServiceFreq());
        //    holder.tvSeviceNo.setText(listEquipmentModel3.get(position).getServiceNo());


        holder.tvLastServiceDate.setText(listEquipmentModel3.get(position).getLastServiceDate());
        holder.tvNextServiceDate.setText(listEquipmentModel3.get(position).getNextServiceDate());

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.deleteClick(listEquipmentModel3.get(position));

                notifyDataSetChanged();

            }
        });

    }

    public void notityAdapter(AddEquipmentModel3 addEquipmentModel3) {

        listEquipmentModel3.add(addEquipmentModel3);
        notifyDataSetChanged();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvServiceName;
        private final TextView tvLastServiceDate;
        private final TextView tvNextServiceDate;
        private final ImageView imageDelete;


        public MyViewHolder(View itemView) {

            super(itemView);

            tvServiceName = (TextView) itemView.findViewById(R.id.tv_service_name);
            tvLastServiceDate = (TextView) itemView.findViewById(R.id.tv_last_service_date);
            tvNextServiceDate = (TextView) itemView.findViewById(R.id.tv_next_service_date);
            imageDelete = (ImageView) itemView.findViewById(R.id.image_delete);

        }

    }
}
