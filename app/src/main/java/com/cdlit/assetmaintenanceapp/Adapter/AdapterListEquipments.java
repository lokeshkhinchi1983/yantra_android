package com.cdlit.assetmaintenanceapp.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Model.EquipmentList;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityEquipmentChecklist;

import java.util.ArrayList;

/**
 * Created by rakesh on 04-09-2017.
 */

public class AdapterListEquipments extends RecyclerView.Adapter<AdapterListEquipments.MyViewHolder> {

    private final AppCompatActivity activity;
    private final ArrayList<EquipmentList.Response> listEquipments;
    private Bitmap decodedByte;

    public AdapterListEquipments(AppCompatActivity activity, ArrayList<EquipmentList.Response> listEquipments) {

        this.activity = activity;
        this.listEquipments = listEquipments;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.equipments_list_item, parent, false);

        return new AdapterListEquipments.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final EquipmentList.Response response = listEquipments.get(position);

        holder.tvEquipmentItem.setText(response.getModel_no());

        holder.tvEquipmentDes.setText(response.getDescription());


        String msg = response.getMessage();

        if (msg != null && !msg.equalsIgnoreCase("")) {

            msg = msg.split("\\.")[0];

        }

        //  holder.tvCheckedUser.setText(msg);

        String bitmapstring = response.getImage_bitmap().get(0);

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            decodedByte = null;

            holder.imageEquipment.
                    setImageBitmap(decodedByte);

            holder.imageEquipment.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageEquipment.
                    setImageBitmap(decodedByte);

        }

        holder.imageEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring;
                bitmapstring = response.getImage_bitmap().get(0);

                //  String bitmapstring = listEquipmentType.get(position).getEquipmentTypeImages().get(0).getBitmapstring();

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(activity.getSupportFragmentManager(), "dialog");

                }


            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, ActivityEquipmentChecklist.class);

                Log.e("equipmet_id", "" + listEquipments.get(position).getEquipment_id());

                Log.e("equipmet_type_id", "" + listEquipments.get(position).getEquipment_type_id());

                i.putExtra("equipmet_id", listEquipments.get(position).getEquipment_id());

                i.putExtra("equipmet_type_id", listEquipments.get(position).getEquipment_type_id());

                i.putExtra("equipmet_model", response.getModel_no());

                i.putExtra("equipmet_model_des", response.getDescription());


                activity.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listEquipments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvEquipmentItem;
        private final ImageView imageEquipment;
        //  private final TextView tvCheckedUser;
        private final TextView tvEquipmentDes;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvEquipmentItem = (TextView) itemView.findViewById(R.id.tv_equipment_item);
            imageEquipment = (ImageView) itemView.findViewById(R.id.image_equipment);
            // tvCheckedUser = (TextView) itemView.findViewById(R.id.tv_checked_user);
            tvEquipmentDes = (TextView) itemView.findViewById(R.id.tv_equipment_des);
        }

    }

}
