package com.cdlit.assetmaintenanceapp.Adapter;

import android.content.Intent;
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
import com.cdlit.assetmaintenanceapp.Model.EquipmentFalulList;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityFaultLogDetail;
import com.cdlit.assetmaintenanceapp.Ui.FragmentFaultLog;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.FlipAnimator;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdaperFaultLog extends RecyclerView.Adapter<AdaperFaultLog.MyViewHolder> {

    private ArrayList<EquipmentFalulList.Response> listFaults;
    private Set<String> listPrivilege;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private FragmentFaultLog fragmentFaultLog;
    private ActionModeListener listener = null;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    private boolean reverseAllAnimations = false;
    private Bitmap decodedByte;

    public AdaperFaultLog(FragmentFaultLog fragmentFaultLog, ArrayList<EquipmentFalulList.Response> listFaults, ActionModeListener listener) {

        this.listFaults = listFaults;
        this.fragmentFaultLog = fragmentFaultLog;
        listPrivilege = Utilities.getSetFromSharedPreferances(fragmentFaultLog.getActivity(), Global.KEY_PRIVILEGE_ID, null);
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.faultlog_list_item, parent, false);

        return new AdaperFaultLog.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        EquipmentFalulList.Response response = listFaults.get(position);

        holder.tvFaultlogItem.setText(response.getEquipment().getModelNo());

        holder.tvFaultlogRemarks.setText(response.getUserComment());

        holder.tvFaultlogCheckedBy.setText("Checked by " + response.getUserFirstName() + " on " + response.getLoggedDate());

        String bitmapstring;

        if (response.getImagePath() == null || response.getImagePath().size() == 0) {
            bitmapstring = null;
        } else {
            bitmapstring = response.getBitmapstring();
        }

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            decodedByte = null;

            holder.imageFaultType.
                    setImageBitmap(decodedByte);

            holder.imageFaultType.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageFaultType.
                    setImageBitmap(decodedByte);

        }

        if (response.getFaultType() == 0) {
            holder.tvFaultlogType.setText("Minor fault");
        } else if (response.getFaultType() == 1) {
            holder.tvFaultlogType.setText("Major fault");
        } else {
            holder.tvFaultlogType.setText("");
        }

        // handle icon animation
        applyIconAnimation(holder, position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("setOnClickListener", "" + position);

                if (selectedItems.size() == 0) {

                    Intent intent = new Intent(fragmentFaultLog.getActivity(), ActivityFaultLogDetail.class);

                    EquipmentFalulList.Response faultDetail = listFaults.get(position);

                    intent.putExtra("equipment_name", faultDetail.getEquipment().getModelNo());
                    intent.putExtra("comment", faultDetail.getUserComment());
                    intent.putExtra("fault_type", faultDetail.getFaultType());
                    intent.putExtra("user_name", faultDetail.getUserFirstName());
                    intent.putExtra("logged_date", faultDetail.getLoggedDate());

                    List<String> image_path = faultDetail.getImagePath();

                    intent.putStringArrayListExtra("image_path", (ArrayList<String>) image_path);
                    //    EquipmentFalulList.Response cfgOptions = intent.getParcelableExtra("cfgOptions");

                 /*   Bundle b = new Bundle();
                    b.putParcelable("fault_detail", faultDetail);

                    i.putExtra("bundle", b);*/

                    fragmentFaultLog.getActivity().startActivity(intent);

                    // push from bottom to top
                    // overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    // slide from right to left
                    // fragmentFaultLog.getActivity().overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);

                } else {

                    listener.onMessageRowClicked(position);

                }

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

        holder.imageFaultType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring;

                if (listFaults.get(position).getImagePath() == null || listFaults.get(position).getImagePath().size() == 0) {
                    bitmapstring = null;
                } else {
                    bitmapstring = listFaults.get(position).getBitmapstring();
                }

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(fragmentFaultLog.getFragmentManager(), "dialog");

                }

            }
        });


    }

    private void applyIconAnimation(MyViewHolder holder, int position) {

        if (selectedItems.get(position, false)) {

            holder.iconFront.setVisibility(View.GONE);

            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);

            holder.cardView.setCardBackgroundColor(fragmentFaultLog.getResources().getColor(R.color.item_selector));
            //  holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_green));

            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(fragmentFaultLog.getContext(), holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }

        } else {

            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);

            holder.iconFront.setAlpha(1);

            holder.cardView.setCardBackgroundColor(fragmentFaultLog.getResources().getColor(R.color.white));

            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(fragmentFaultLog.getContext(), holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }

        }


    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }


    @Override
    public int getItemCount() {
        return listFaults.size();
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

    public void updateImage(int loaderId, String path, String imageBitmap) {

        Log.e("adapter position---", "" + loaderId);

        //    bitmapstring = equipmentTypeResponse.getEquipmentTypeImages().get(0).getBitmapstring();

        listFaults.get(loaderId).setBitmapstring(imageBitmap);

        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView tvFaultlogItem;
        private final TextView tvFaultlogRemarks;
        private final CardView cardView;
        private final TextView tvFaultlogCheckedBy;
        private final TextView tvFaultlogType;
        private final ImageView imageFaultType;
        public RelativeLayout viewBackground, viewForeground;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View itemView) {

            super(itemView);

            tvFaultlogItem = (TextView) itemView.findViewById(R.id.tv_faultlog_item);
            tvFaultlogRemarks = (TextView) itemView.findViewById(R.id.tv_faultlog_remarks);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            imageFaultType = (ImageView) itemView.findViewById(R.id.image_fault_type);
            tvFaultlogCheckedBy = (TextView) itemView.findViewById(R.id.tv_faultlog_checked_by);
            tvFaultlogType = (TextView) itemView.findViewById(R.id.tv_faultlog_type);

            viewBackground = (RelativeLayout) itemView.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);
            iconBack = (RelativeLayout) itemView.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) itemView.findViewById(R.id.icon_front);
            iconContainer = (RelativeLayout) itemView.findViewById(R.id.icon_container);

            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {

            listener.onRowLongClicked(getAdapterPosition());

            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

            return true;

        }

    }
}
