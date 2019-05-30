package com.cdlit.assetmaintenanceapp.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.Notification;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentNotification;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyViewHolder> {

    private final FragmentNotification context;
    private final ArrayList<Notification.Response> listNotification;
    private final ActionModeListener listener;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    private final Set<String> listPrivilege;
    private final SparseBooleanArray selectedItems;
    private final SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    public AdapterNotification(FragmentNotification context, List<Notification.Response> listNotification, ActionModeListener listener) {

        this.context = context;
        this.listNotification = (ArrayList<Notification.Response>) listNotification;
        this.listener = listener;
        listPrivilege = Utilities.getSetFromSharedPreferances(context.getActivity(), Global.KEY_PRIVILEGE_ID, null);
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Notification.Response notificationResponse = listNotification.get(position);

        holder.tvNotificationItem.setText(notificationResponse.getEquipmentname() + " ( " + notificationResponse.getChecklistname() + " )");
        holder.tvNotificationDes.setText(notificationResponse.getDescription());

        if (notificationResponse.getTasktype().equalsIgnoreCase("service")) {

            holder.tvNotificationLoggedBy.setText(notificationResponse.getEquipmentname() + " of " + notificationResponse.getLocationname() + " is due to service on " + notificationResponse.getLoggedDate());

        } else {

            holder.tvNotificationLoggedBy.setText("Checked by " + notificationResponse.getLoggedby() + " on " + notificationResponse.getLoggedDate());

        }
        //  Integer faultType = notificationResponse.getFaulttype();

        String fault = "";

        if (notificationResponse.getFaulttype() == null) {
            fault = notificationResponse.getTasktype();
        } else if (notificationResponse.getFaulttype() == 1) {
            fault = "Major fault";
        } else if (notificationResponse.getFaulttype() == 0) {
            fault = "Minor fault";
        }

        holder.tvNotificationFaulttype.setText(fault);
        // handle icon animation

        applyIconAnimation(holder, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getSelectedItemCount() >= 1) {

                } else {
                    listener.onMessageRowClicked(position);
                }
                //   listener.onMessageRowClicked(position);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (getSelectedItemCount() >= 1) {

                } else {
                    listener.onRowLongClicked(position);

                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                }

             /*   listener.onRowLongClicked(position);

                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);*/

                return true;
            }
        });
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {

        if (selectedItems.get(position, false)) {

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.item_selector));

        } else {

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        }

    }

    @Override
    public int getItemCount() {
        return listNotification.size();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;

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

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNotificationItem;
        private final TextView tvNotificationDes;
        private final TextView tvNotificationLoggedBy;
        private final TextView tvNotificationFaulttype;
        private final CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvNotificationItem = (TextView) itemView.findViewById(R.id.tv_notification_item);
            tvNotificationDes = (TextView) itemView.findViewById(R.id.tv_notification_des);
            tvNotificationLoggedBy = (TextView) itemView.findViewById(R.id.tv_notification_logged_by);
            tvNotificationFaulttype = (TextView) itemView.findViewById(R.id.tv_notification_faulttype);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

    }
}
