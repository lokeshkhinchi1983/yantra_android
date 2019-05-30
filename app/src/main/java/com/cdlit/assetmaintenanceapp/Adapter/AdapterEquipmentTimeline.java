package com.cdlit.assetmaintenanceapp.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.EquipmentTimeline;
import com.cdlit.assetmaintenanceapp.R;
import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by rakesh on 22-12-2017.
 */

public class AdapterEquipmentTimeline extends RecyclerView.Adapter<AdapterEquipmentTimeline.MyViewHolder> {

    private final FragmentActivity activity;
    private final ArrayList<EquipmentTimeline.EquipmentTimelineResponse> listEquipmentTimeline;
    private static final int VIEW_TYPE_TOP = 0;
    private static final int VIEW_TYPE_MIDDLE = 1;
    private static final int VIEW_TYPE_BOTTOM = 2;

    public AdapterEquipmentTimeline(FragmentActivity activity, ArrayList<EquipmentTimeline.EquipmentTimelineResponse> listEquipmentTimeline) {

        this.activity = activity;
        this.listEquipmentTimeline = listEquipmentTimeline;

    }

    @Override
    public AdapterEquipmentTimeline.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.equipment_timeline_list_item, parent, false);

        return new AdapterEquipmentTimeline.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AdapterEquipmentTimeline.MyViewHolder holder, int position) {

        String msg = listEquipmentTimeline.get(position).getMsg();
        String action = listEquipmentTimeline.get(position).getAction();
        Date date = listEquipmentTimeline.get(position).getDate();

        String itemAction = action.toLowerCase();

        if (itemAction.contains("edit")) {
            //   holder.imageEquipment.setImageBitmap(null);
            //    holder.imageEquipment.setImageResource(R.drawable.ic_mode_edit_black_18px);
            holder.imageEquipment.setBackgroundResource(R.drawable.ic_mode_edit_black_18px);

        } else if (itemAction.contains("delete")) {
            //  holder.imageEquipment.setImageBitmap(null);
            //     holder.imageEquipment.setImageResource(R.drawable.ic_delete_black_18px);
            holder.imageEquipment.setBackgroundResource(R.drawable.ic_delete_black_18px);

        } else if (itemAction.contains("add")) {
            // holder.imageEquipment.setImageBitmap(null);
            //    holder.imageEquipment.setImageResource(R.drawable.ic_add_24);
            holder.imageEquipment.setBackgroundResource(R.drawable.ic_add_24);

        }

        holder.tvEquipmentTimeline.setText(msg);
        holder.tvEquipmentTimelineDate.setText("" + convertDate(date));

        // Populate views...
        switch (holder.getItemViewType()) {

            case VIEW_TYPE_TOP:
                // The top of the line has to be rounded
                holder.view_image.setBackgroundResource(R.drawable.line_bg_top);
                break;
            case VIEW_TYPE_MIDDLE:
                // Only the color could be enough
                // but a drawable can be used to make the cap rounded also here
                holder.view_image.setBackgroundResource(R.drawable.line_bg_middle);
                break;
            case VIEW_TYPE_BOTTOM:
                holder.view_image.setBackgroundResource(R.drawable.line_bg_bottom);
                break;

        }

    }

    private String convertDate(Date selectedDate) {

        String oldFormat = "yyyy-MM-dd'T'HH:mm:ss";
        String newFormat = "dd-MM-yyyy";

        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

        String selectedDateString = sdf1.format(selectedDate);

        Log.d("selectedDateString", "" + selectedDateString);

        String returndate = null;
        try {
            returndate = sdf2.format(sdf1.parse(selectedDateString));
        } catch (ParseException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        Log.d("returndate", "" + returndate);

        return returndate;
    }

    @Override
    public int getItemCount() {
        return listEquipmentTimeline.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TOP;
        } else if (position == listEquipmentTimeline.size() - 1) {
            return VIEW_TYPE_BOTTOM;
        }
        return VIEW_TYPE_MIDDLE;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvEquipmentTimeline;
        private final TextView tvEquipmentTimelineDate;
        private final ImageView imageEquipment;
        private final View view_image;
        //  private final FrameLayout itemLine;

        public MyViewHolder(View view) {

            super(view);

            tvEquipmentTimeline = (TextView) view.findViewById(R.id.tv_equipment_timeline);
            tvEquipmentTimelineDate = (TextView) view.findViewById(R.id.tv_equipment_timeline_date);
            imageEquipment = (ImageView) view.findViewById(R.id.image_equipment);
            //   itemLine = (FrameLayout) view.findViewById(R.id.item_line);

            view_image = (View) view.findViewById(R.id.view);

        }
    }
}
