package com.cdlit.assetmaintenanceapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Model.RepairLog;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentRepairLog;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.FlipAnimator;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by rakesh on 12-12-2017.
 */

public class AdaperRepairLog extends RecyclerView.Adapter<AdaperRepairLog.MyViewHolder> {

    private final FragmentRepairLog fragmentRepairLog;
    private final List<RepairLog.RepairlogResponse> listRepairLog;
    private final Set<String> listPrivilege;
    private final SparseBooleanArray selectedItems;
    private final SparseBooleanArray animationItemsIndex;
    private final ActionModeListener listener;
    private Bitmap decodedByte;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    private boolean reverseAllAnimations = false;

    public AdaperRepairLog(FragmentRepairLog fragmentRepairLog, List<RepairLog.RepairlogResponse> listRepairLog, ActionModeListener listener) {

        this.fragmentRepairLog = fragmentRepairLog;
        this.listRepairLog = listRepairLog;
        listPrivilege = Utilities.getSetFromSharedPreferances(fragmentRepairLog.getActivity(), Global.KEY_PRIVILEGE_ID, null);
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repairlog_list_item, parent, false);

        return new AdaperRepairLog.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final RepairLog.RepairlogResponse repairLogResponse = listRepairLog.get(position);

      /*  if (!listPrivilege.contains(Utilities.REPAIRLOG_WRITE)) {

            holder.imageEdit.setVisibility(View.GONE);
        }

        if (!listPrivilege.contains(Utilities.REPAIRLOG_DELETE)) {

            holder.imageDelete.setVisibility(View.GONE);

        }*/

        holder.tvRepairlogItem.setText(repairLogResponse.getModelName() + " ( " + repairLogResponse.getLocation() + " ) ");

        String date = "";
        if (repairLogResponse.getRepairLogDate() != null) {
            date = convertDate(repairLogResponse.getRepairLogDate());
        } else {
            date = "";
        }

        holder.tvCheckedBy.setText("Entered by " + repairLogResponse.getFirstName() + " " + repairLogResponse.getLastName() + " on " + date);

        String bitmapstring;

        if (repairLogResponse.getModel_bitmap() == null || repairLogResponse.getModel_bitmap().size() == 0) {
            bitmapstring = null;
        } else {
            bitmapstring = repairLogResponse.getModel_bitmap().get(0);
        }

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            decodedByte = null;

            holder.imageRepairLog.
                    setImageBitmap(decodedByte);

            holder.imageRepairLog.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageRepairLog.
                    setImageBitmap(decodedByte);

        }

        holder.imageRepairLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring;

                if (repairLogResponse.getModel_bitmap() == null || repairLogResponse.getModel_bitmap().size() == 0) {
                    bitmapstring = null;
                } else {
                    bitmapstring = repairLogResponse.getModel_bitmap().get(0);
                }

                //  String bitmapstring = listRepairLog.get(position).getModel_bitmap().get(0);

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(fragmentRepairLog.getFragmentManager(), "dialog");

                }

            }
        });


        /*holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((FragmentRepairLog) fragmentRepairLog).editClick(repairLogResponse);

            }
        });*/

       /* holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((FragmentRepairLog) fragmentRepairLog).deleteClick(repairLogResponse);

            }
        });*/

        // handle icon animation
        applyIconAnimation(holder, position);

        // apply click events
        applyClickEvents(holder, position);
    }

    private String convertDate(String repairLogDate) {

        Date convertedDate = null;

        String newFormat = "dd-MM-yyyy";
        String oldFormat = "yyyy-MM-dd";

        SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);

        String date = null;
        try {
            date = sdf2.format(sdf1.parse(repairLogDate));
        } catch (ParseException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        try {
            convertedDate = (Date) sdf2.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return date;

    }

    private void applyClickEvents(MyViewHolder holder, final int position) {

        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("onIconClicked", "" + position);

                listener.onIconClicked(position);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onMessageRowClicked(position);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                listener.onRowLongClicked(position);

                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                return true;
            }
        });
    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void clearSelections() {

        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();

    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public void toggleSelection(int pos) {

        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }

        notifyItemChanged(pos);

    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    public void removeData(int position) {
        listRepairLog.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    @Override
    public int getItemCount() {
        return listRepairLog.size();
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {

        if (selectedItems.get(position, false)) {

            holder.iconFront.setVisibility(View.GONE);

            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);

            holder.cardView.setCardBackgroundColor(fragmentRepairLog.getResources().getColor(R.color.item_selector));
            //  holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_green));

            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(fragmentRepairLog.getContext(), holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }

        } else {

            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);

            holder.iconFront.setAlpha(1);

            holder.cardView.setCardBackgroundColor(fragmentRepairLog.getResources().getColor(R.color.white));

            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(fragmentRepairLog.getContext(), holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }

        }

    }

    public void updateImage(int position, String path, String imageBitmap) {

        ArrayList<String> listBitmap = new ArrayList<>();
        listBitmap.add(imageBitmap);

        listRepairLog.get(position).setModel_bitmap(listBitmap);

        notifyDataSetChanged();

    }

    public void removeItem(int position) {
        listRepairLog.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(RepairLog.RepairlogResponse item, int position) {

        listRepairLog.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
        // notifyItemChanged(position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView tvRepairlogItem;
        private final ImageView imageRepairLog;
        private final TextView tvCheckedBy;
        //   private final ImageView imageEdit;
        // private final ImageView imageDelete;
        public RelativeLayout viewBackground, viewForeground;
        public RelativeLayout iconContainer, iconBack, iconFront;
        private final CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRepairlogItem = (TextView) itemView.findViewById(R.id.tv_repairlog_item);
            imageRepairLog = (ImageView) itemView.findViewById(R.id.image_repair_log);
            //  imageEdit = (ImageView) itemView.findViewById(R.id.image_edit);
            //    imageDelete = (ImageView) itemView.findViewById(R.id.image_delete);
            viewBackground = (RelativeLayout) itemView.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);
            iconBack = (RelativeLayout) itemView.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) itemView.findViewById(R.id.icon_front);
            iconContainer = (RelativeLayout) itemView.findViewById(R.id.icon_container);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            tvCheckedBy = (TextView) itemView.findViewById(R.id.tv_checked_by);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(View view) {

            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;

        }
    }

    public interface AdapterRepairLogListener {

        void onIconClicked(int position);
 /*
        void onIconImportantClicked(int position);*/

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }
}
