package com.cdlit.assetmaintenanceapp.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Dialog.DialogSelectEquipmentModel;
import com.cdlit.assetmaintenanceapp.Model.AssignEquipment;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityEquipmentChecklist;

import java.util.ArrayList;

/**
 * Created by rakesh on 10-11-2017.
 */

public class AdapterSelectEquipmentModel extends RecyclerView.Adapter<AdapterSelectEquipmentModel.MyViewHolder> {

    private final FragmentActivity activity;
    private final ArrayList<AssignEquipment.AssignEquipments> listEquipments;

    private final DialogSelectEquipmentModel dialogAssignEquipmetUser;
    private Bitmap decodedByte;

    public AdapterSelectEquipmentModel(FragmentActivity activity, ArrayList<AssignEquipment.AssignEquipments> listEquipments, DialogSelectEquipmentModel dialogAssignEquipmetUser) {

        this.activity = activity;
        this.listEquipments = listEquipments;

        this.dialogAssignEquipmetUser = dialogAssignEquipmetUser;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_equipment_model_list_item, parent, false);

        return new AdapterSelectEquipmentModel.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvEquipmentModel.setText(listEquipments.get(position).getModel_no());
        holder.tvEquipmentDes.setText(listEquipments.get(position).getDescription());

        String bitmapstring;

        if (listEquipments.get(position).getImage_bitmap() == null || listEquipments.get(position).getImage_bitmap().size() == 0) {
            bitmapstring = null;
        } else {
            bitmapstring = listEquipments.get(position).getImage_bitmap().get(0);
        }

        //    String bitmapstring = listEquipments.get(position).getImage_bitmap().get(0);

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            decodedByte = null;

            holder.imageEquipmentModel.
                    setImageBitmap(decodedByte);

            holder.imageEquipmentModel.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageEquipmentModel.
                    setImageBitmap(decodedByte);

        }


        holder.imageEquipmentModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring;

                if (listEquipments.get(position).getImage_bitmap() == null || listEquipments.get(position).getImage_bitmap().size() == 0) {
                    bitmapstring = null;
                } else {
                    bitmapstring = listEquipments.get(position).getImage_bitmap().get(0);
                }

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(dialogAssignEquipmetUser.getFragmentManager(), "dialog");

                }

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAssignEquipmetUser.dismiss();

                Intent i = new Intent(activity, ActivityEquipmentChecklist.class);

                Log.e("equipmet_id", "" + listEquipments.get(position).getEquipment_id());

                Log.e("equipmet_type_id", "" + listEquipments.get(position).getEquipment_type_id());

                i.putExtra("equipmet_id", listEquipments.get(position).getEquipment_id());

                i.putExtra("equipmet_type_id", listEquipments.get(position).getEquipment_type_id());

                i.putExtra("equipmet_model", listEquipments.get(position).getModel_no());

                i.putExtra("equipmet_model_des", listEquipments.get(position).getDescription());

                activity.startActivity(i);

                //push from bottom to top
                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                //slide from right to left
                activity.overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                //  activity.overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);


            }
        });
    }

    @Override
    public int getItemCount() {
        return listEquipments.size();
    }

    public void updateImage(int loaderId, String path, String imageBitmap) {

        ArrayList<String> listimages = new ArrayList<>();
        listimages.add(imageBitmap);

        listEquipments.get(loaderId).setImage_bitmap(listimages);

        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageEquipmentModel;
        private final TextView tvEquipmentModel, tvEquipmentDes;

        public MyViewHolder(View itemView) {

            super(itemView);

            imageEquipmentModel = (ImageView) itemView.findViewById(R.id.image_equipment_model);
            tvEquipmentModel = (TextView) itemView.findViewById(R.id.tv_equipment_model);
            tvEquipmentDes = (TextView) itemView.findViewById(R.id.tv_equipment_des);


        }
    }
}
