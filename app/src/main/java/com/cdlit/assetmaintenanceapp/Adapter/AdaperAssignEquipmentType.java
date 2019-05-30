package com.cdlit.assetmaintenanceapp.Adapter;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Model.AssignEquipmentToUser;
import com.cdlit.assetmaintenanceapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakesh on 16-06-2017.
 */

public class AdaperAssignEquipmentType extends RecyclerView.Adapter<AdaperAssignEquipmentType.MyViewHolder> {

    public final List<AssignEquipmentToUser.Response> listEquipmentType;
    private final FragmentActivity context;
    public final ArrayList<Integer> listAssignedIds;
    public final ArrayList<Integer> listAssignedEquipmentTypeIds;
    //  public ArrayList<Integer> listCheckedItemsId;
    private Bitmap decodedByte;

    public AdaperAssignEquipmentType(FragmentActivity context, List<AssignEquipmentToUser.Response> listEquipmentType, ArrayList<Integer> listAssignedEquipmentTypeIds) {

        this.listEquipmentType = listEquipmentType;
        this.context = context;
        //   listCheckedItemsId = new ArrayList<Integer>();
        this.listAssignedEquipmentTypeIds = listAssignedEquipmentTypeIds;
        this.listAssignedIds = new ArrayList<>();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assign_equipment_type_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final AssignEquipmentToUser.Response equipmentTypeResponse = listEquipmentType.get(position);

        holder.tvEquipmentType.setText(equipmentTypeResponse.getEquipmentName());

        String bitmapstring;

        if (equipmentTypeResponse.getEquipmentImages() == null || equipmentTypeResponse.getEquipmentImages().size() == 0) {
            bitmapstring = null;
        } else {
            bitmapstring = equipmentTypeResponse.getEquipmentImages().get(0).getBitmapstring();
        }

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            decodedByte = null;

            holder.imageEquipmentType.
                    setImageBitmap(decodedByte);

            holder.imageEquipmentType.setBackgroundResource(R.drawable.ic_no_img);


        } else {

            getImageBitmap(bitmapstring);

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageEquipmentType.
                    setImageBitmap(decodedByte);

        }


        if (listAssignedEquipmentTypeIds.contains(equipmentTypeResponse)) {

            holder.cbAssignToUser.setChecked(true);

        } else {

            holder.cbAssignToUser.setChecked(false);

        }


        holder.imageEquipmentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring;

                if (equipmentTypeResponse.getEquipmentImages() == null || equipmentTypeResponse.getEquipmentImages().size()==0) {
                    bitmapstring = null;
                } else {
                    bitmapstring = equipmentTypeResponse.getEquipmentImages().get(0).getBitmapstring();
                }

                //   String bitmapstring = listEquipmentType.get(position).getEquipmentTypeImages().get(0).getBitmapstring();

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(context.getSupportFragmentManager(), "dialog");

                }

            }
        });


        holder.cbAssignToUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    Integer equipmentTypeId = listEquipmentType.get(position).getId();

                    Log.e("add getId", "" + equipmentTypeId);

                    listAssignedEquipmentTypeIds.add(equipmentTypeId);

                    listAssignedIds.add(equipmentTypeId);

                } else {

                    Integer equipmentTypeId = listEquipmentType.get(position).getId();

                    Log.e("remove getId", "" + equipmentTypeId);

                    int position = 0;

                    for (int i = 0; i < listAssignedEquipmentTypeIds.size(); i++) {
                        if (listAssignedEquipmentTypeIds.get(i) == equipmentTypeId) {
                            position = i;
                        }
                    }

                    listAssignedEquipmentTypeIds.remove(position);

                    for (int i = 0; i < listAssignedIds.size(); i++) {
                        if (listAssignedIds.get(i) == equipmentTypeId) {
                            position = i;
                        }
                    }

                    listAssignedIds.remove(position);

                }
            }
        });


    }

    private void getImageBitmap(String bitmapstring) {


    }

    @Override
    public int getItemCount() {
        return listEquipmentType.size();
    }

    public void updateImage(int position, String path, String imageBitmap) {

        listEquipmentType.get(position).getEquipmentImages().get(0).setBitmapstring(imageBitmap);

        notifyDataSetChanged();

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvEquipmentType;
        private final CheckBox cbAssignToUser;
        private final ImageView imageEquipmentType;

        public MyViewHolder(View view) {

            super(view);

            tvEquipmentType = (TextView) view.findViewById(R.id.tv_equipment_type);
            cbAssignToUser = (CheckBox) view.findViewById(R.id.cb_assign_to_user);
            imageEquipmentType = (ImageView) view.findViewById(R.id.image_equipment_type);

        }

    }


}
