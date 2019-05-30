package com.cdlit.assetmaintenanceapp.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogAddEquipmentModelNotRequired;
import com.cdlit.assetmaintenanceapp.Dialog.DialogAddEquipmentType;
import com.cdlit.assetmaintenanceapp.Dialog.DialogEquipmentChecklistImage;
import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Dialog.DialogRepairLog;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityFaultLogDetail;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakesh on 06-07-2017.
 */

public class AdapterMultiImages extends RecyclerView.Adapter<AdapterMultiImages.MyViewHolder> {

    private final Activity context;
    private final List<Bitmap> listImages;
    private final String timeStamp;
    private DialogRepairLog dialogRepairLog;
    private DialogEquipmentChecklistImage dialogEquipmentChecklistImage;
    private DialogAddEquipmentType dialogAddEquipmentType = null;
    private DialogAddEquipmentModelNotRequired dialogAddEquipmentModelNotRequired = null;
    private Bitmap bitmap;

    public AdapterMultiImages(Activity context, DialogAddEquipmentType dialogAddEquipmentType, ArrayList<Bitmap> listImages) {

        this.listImages = listImages;
        this.context = context;
        this.dialogAddEquipmentType = dialogAddEquipmentType;

        timeStamp = null;

    }

    public AdapterMultiImages(Activity context, ArrayList<Bitmap> listImages) {

        this.listImages = listImages;
        this.context = context;

        timeStamp = null;

    }

    public AdapterMultiImages(Activity context, DialogAddEquipmentModelNotRequired dialogAddEquipmentModelNotRequired, ArrayList<Bitmap> listImages) {

        this.listImages = listImages;
        this.context = context;
        this.dialogAddEquipmentModelNotRequired = dialogAddEquipmentModelNotRequired;

        timeStamp = null;

    }

    public AdapterMultiImages(Activity context, DialogRepairLog dialogRepairLog, ArrayList<Bitmap> listImages) {

        this.listImages = listImages;
        this.context = context;
        this.dialogRepairLog = dialogRepairLog;
        // todayDate = true;
        timeStamp = null;

    }

    public AdapterMultiImages(Activity context, DialogEquipmentChecklistImage dialogEquipmentChecklistImage, ArrayList<Bitmap> listImages, String timeStamp) {

        this.listImages = listImages;
        this.context = context;
        this.dialogEquipmentChecklistImage = dialogEquipmentChecklistImage;
        this.timeStamp = timeStamp;

    }

    @Override
    public AdapterMultiImages.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.multi_images_item, parent, false);

        return new MyViewHolder(itemView);

    }

    public void updateImage(int position, String path, String imageBitmap) {

        Log.e("position", "" + position);
        Log.e("imageBitmap", "" + imageBitmap);

        byte[] decodedString = Base64.decode(imageBitmap, Base64.DEFAULT);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        listImages.set(position, decodedByte);

        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(final AdapterMultiImages.MyViewHolder holder, final int position) {

        bitmap = listImages.get(position);

        holder.imageItem.
                setImageBitmap(bitmap);

        if (timeStamp == null || timeStamp.equalsIgnoreCase("")) {
            holder.imageRemove.setEnabled(true);
        } else {
            holder.imageRemove.setEnabled(false);
        }

        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = listImages.get(position);

                String encodedString;

                if (bitmap == null) {
                    encodedString = null;
                } else {
                    encodedString = Utilities.getEncodedString(bitmap);
                }


                if (encodedString != null && !encodedString.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullimage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", encodedString);

                    dialogFullimage.setArguments(bundle);

                    if (dialogAddEquipmentType != null && dialogAddEquipmentType instanceof DialogAddEquipmentType) {

                        dialogFullimage.show(dialogAddEquipmentType.getFragmentManager(), "dialog");
                    }

                    if (dialogAddEquipmentModelNotRequired != null && dialogAddEquipmentModelNotRequired instanceof DialogAddEquipmentModelNotRequired) {

                        dialogFullimage.show(dialogAddEquipmentModelNotRequired.getFragmentManager(), "dialog");
                    }

                    if (dialogEquipmentChecklistImage != null && dialogEquipmentChecklistImage instanceof DialogEquipmentChecklistImage) {

                        dialogFullimage.show(dialogEquipmentChecklistImage.getFragmentManager(), "dialog");
                    }

                    if (dialogRepairLog != null && dialogRepairLog instanceof DialogRepairLog) {

                        dialogFullimage.show(dialogRepairLog.getFragmentManager(), "dialog");

                    }

                    if (dialogRepairLog != null && dialogRepairLog instanceof DialogRepairLog) {

                        dialogFullimage.show(dialogRepairLog.getFragmentManager(), "dialog");

                    }

                    if (context != null && context instanceof ActivityFaultLogDetail) {

                        dialogFullimage.show(((ActivityFaultLogDetail) context).getSupportFragmentManager(), "dialog");

                    }

                }

            }
        });

        holder.imageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (context instanceof ActivityFaultLogDetail) {

                } else {
                    listImages.remove(position);
                    notifyDataSetChanged();
                }


                if (dialogAddEquipmentType != null && dialogAddEquipmentType instanceof DialogAddEquipmentType) {

                    dialogAddEquipmentType.imageChanges = true;
                }

                if (dialogAddEquipmentModelNotRequired != null && dialogAddEquipmentModelNotRequired instanceof DialogAddEquipmentModelNotRequired) {

                    dialogAddEquipmentModelNotRequired.imageChanges = true;
                }

                if (dialogEquipmentChecklistImage != null && dialogEquipmentChecklistImage instanceof DialogEquipmentChecklistImage) {

                    dialogEquipmentChecklistImage.imageChanges = true;
                }

                if (dialogRepairLog != null && dialogRepairLog instanceof DialogRepairLog) {

                    dialogRepairLog.imageChanges = true;
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return listImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageItem, imageRemove;

        public MyViewHolder(View view) {
            super(view);

            imageItem = (ImageView) view.findViewById(R.id.image_item);
            imageRemove = (ImageView) view.findViewById(R.id.image_remove);
        }

    }

}
