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
import java.util.List;

/**
 * Created by rakesh on 30-08-2017.
 */

public class AdaperEquipmentTypeList extends RecyclerView.Adapter<AdaperEquipmentTypeList.MyViewHolder> {

    //private final ArrayList<AssignEquipment.AssignEquipmentResponse> listEquipmentTypeList;
    private final FragmentActivity context;
    private Integer equipmentTypeId;
    public DialogSelectEquipmentModel dialogSelectEquipmentModel;
    private List<AssignEquipment.AssignEquipments> listEquipments;
    private Bitmap decodedByte;
    private ArrayList<AssignEquipment.AssignEquipments> listEquipmentList = null;

    public AdaperEquipmentTypeList(FragmentActivity context, ArrayList<AssignEquipment.AssignEquipments> listEquipmentList) {

        this.listEquipmentList = listEquipmentList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_equipment_type_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        AssignEquipment.AssignEquipments assignEquipmentResponse = listEquipmentList.get(position);

        //    listEquipments = listEquipmentTypeList.get(position).getEquipments();

        holder.tvEquipmentType.setText(assignEquipmentResponse.getModel_no());

        holder.tvEquipmentTypeDes.setText(assignEquipmentResponse.getDescription());

        holder.tvLastCheckedUser.setText(assignEquipmentResponse.getLast_checked_user());


        //  if (assignEquipmentResponse.getLast_checked_user() != null  && !assignEquipmentResponse.getLast_checked_user().equalsIgnoreCase("")) {


        //  }

        String bitmapstring;

        if (assignEquipmentResponse.getImage_bitmap() == null || assignEquipmentResponse.getImage_bitmap().size() == 0) {

            bitmapstring = null;

        } else {

            bitmapstring = assignEquipmentResponse.getImage_bitmap().get(0);

        }

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            decodedByte = null;

            holder.imageEquipmentType.
                    setImageBitmap(decodedByte);

            holder.imageEquipmentType.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageEquipmentType.
                    setImageBitmap(decodedByte);

        }


        holder.imageEquipmentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring;

                if (listEquipmentList.get(position).getImage_bitmap() == null || listEquipmentList.get(position).getImage_bitmap().size() == 0) {

                    bitmapstring = null;

                } else {

                    bitmapstring = listEquipmentList.get(position).getImage_bitmap().get(0);

                }

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(context.getSupportFragmentManager(), "dialog");

                }

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  listEquipments = listEquipmentTypeList.get(position).getEquipments();
                dialogSelectEquipmentModel = DialogSelectEquipmentModel.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString("title", context.getString(R.string.select_equipment_model));
                bundle.putParcelableArrayList("list_equipments", (ArrayList<? extends Parcelable>) listEquipments);
                //  bundle.putParcelableArrayList("list_assigned_equipment_type", listAssignEquipment);

                dialogSelectEquipmentModel.setArguments(bundle);
                //  dialogSelectEquipmentModel.setTargetFragment(FragmentEquipmentToUser.this, 0);
                dialogSelectEquipmentModel.show(context.getSupportFragmentManager(), "dialog");
*/

                //  dialogAssignEquipmetUser.dismiss();
                Intent i = new Intent(context, ActivityEquipmentChecklist.class);

                Log.e("equipmet_id", "" + listEquipmentList.get(position).getEquipment_id());

                Log.e("equipmet_type_id", "" + listEquipmentList.get(position).getEquipment_type_id());

                i.putExtra("equipmet_id", listEquipmentList.get(position).getEquipment_id());

                i.putExtra("equipmet_type_id", listEquipmentList.get(position).getEquipment_type_id());

                i.putExtra("equipmet_model", listEquipmentList.get(position).getModel_no());

                i.putExtra("equipmet_model_des", listEquipmentList.get(position).getDescription());

                context.startActivity(i);

                //push from bottom to top
                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                //slide from right to left
                context.overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                //  activity.overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);

            }
        });


    }


    @Override
    public int getItemCount() {

        return listEquipmentList.size();

    }

    public void updateImage(int loaderId, String path, String imageBitmap) {

        ArrayList<String> listBitmap = new ArrayList<>();
        listBitmap.add(imageBitmap);

        listEquipmentList.get(loaderId).setImage_bitmap(listBitmap);
        notifyDataSetChanged();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvEquipmentType;
        private final ImageView imageEquipmentType;
        private final TextView tvEquipmentTypeDes;
        private final TextView tvLastCheckedUser;

        public MyViewHolder(View view) {
            super(view);

            tvEquipmentType = (TextView) view.findViewById(R.id.tv_equipment_type);
            imageEquipmentType = (ImageView) view.findViewById(R.id.image_equipment_type);
            tvEquipmentTypeDes = (TextView) view.findViewById(R.id.tv_equipment_type_des);
            tvLastCheckedUser = (TextView) view.findViewById(R.id.tv_last_checked_user);

        }

    }


}


