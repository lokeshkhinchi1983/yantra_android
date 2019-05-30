package com.cdlit.assetmaintenanceapp.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Model.UserChecked;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityEquipmentChecklistDetail;

import java.util.List;
import java.util.Set;

/**
 * Created by rakesh on 16-06-2017.
 */

public class AdapterUserChecked extends RecyclerView.Adapter<AdapterUserChecked.MyViewHolder> {

    private final List<UserChecked.UserCheckedResponse> listUsers;
    private final AppCompatActivity context;
    private Set<String> listPrivilege;
    private Bitmap decodedByte;

    public AdapterUserChecked(AppCompatActivity context, List<UserChecked.UserCheckedResponse> listUsers) {
        this.listUsers = listUsers;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_checked_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final UserChecked.UserCheckedResponse response = listUsers.get(position);

        holder.tvUseItem.setText(response.getMsg());

        String bitmapstring = response.getUser_bitmap();

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            decodedByte = null;

            holder.imageUser.
                    setImageBitmap(decodedByte);

            holder.imageUser.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageUser.
                    setImageBitmap(decodedByte);
        }


        holder.imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring = listUsers.get(position).getUser_bitmap();

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

                Intent i = new Intent(context, ActivityEquipmentChecklistDetail.class);

                i.putExtra("equipmet_id", listUsers.get(position).getEquipment_id());

                i.putExtra("equipmet_type_id", listUsers.get(position).getEquipment_type_id());

                i.putExtra("time_stamp", listUsers.get(position).getCreated_time_stamp());

                i.putExtra("time_stamp", "123456");

                i.putExtra("user_checked_id", listUsers.get(position).getUser_checked_id());

                context.startActivity(i);

                context.overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public void updateImage(int loaderId, String path, String imageBitmap) {

        listUsers.get(loaderId).setUser_bitmap(imageBitmap);
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvUseItem;
        private final ImageView imageUser;

        public MyViewHolder(View view) {

            super(view);
            tvUseItem = (TextView) view.findViewById(R.id.tv_use_item);
            imageUser = (ImageView) view.findViewById(R.id.image_user);

        }


    }


}
