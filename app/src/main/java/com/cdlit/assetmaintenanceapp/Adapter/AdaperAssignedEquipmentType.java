package com.cdlit.assetmaintenanceapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Model.AssignedEquipment;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipmentToUser;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.FlipAnimator;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rakesh on 16-06-2017.
 */

public class AdaperAssignedEquipmentType extends RecyclerView.Adapter<AdaperAssignedEquipmentType.MyViewHolder> {

    private final List<AssignedEquipment.Response> listEquipmentType;
    private final FragmentEquipmentToUser context;
    private final ActionModeListener listener;
    private final SparseBooleanArray selectedItems;
    private final SparseBooleanArray animationItemsIndex;
    private Set<String> listPrivilege;
    private Bitmap decodedByte;
    private boolean reverseAllAnimations = false;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    public AdaperAssignedEquipmentType(FragmentEquipmentToUser context, List<AssignedEquipment.Response> listEquipmentType, ActionModeListener listener) {

        this.listEquipmentType = listEquipmentType;
        this.context = context;
        this.listener = listener;
        listPrivilege = Utilities.getSetFromSharedPreferances(context.getActivity(), Global.KEY_PRIVILEGE_ID, null);
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assign_equipment_type_item_to_user, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final AssignedEquipment.Response equipmentTypeResponse = listEquipmentType.get(position);

    /*    if (!listPrivilege.contains(Utilities.ASSIGN_TO_USER_DELETE)) {

            holder.imageDelete.setVisibility(View.GONE);
        }
*/

        holder.tvEquipmentType.setText(equipmentTypeResponse.getEquipment_name());

        holder.tvEquipmentTypeDes.setText(equipmentTypeResponse.getEquipment_description());

        String bitmapstring;

        if (equipmentTypeResponse.getImage_bitmap() == null) {

            bitmapstring = null;

        } else {

            bitmapstring = equipmentTypeResponse.getImage_bitmap();

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

                if (equipmentTypeResponse.getImage_bitmap() == null) {

                    bitmapstring = null;

                } else {

                    bitmapstring = equipmentTypeResponse.getImage_bitmap();

                }

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(context.getFragmentManager(), "dialog");

                }

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

        applyIconAnimation(holder, position);
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {

        if (selectedItems.get(position, false)) {

            holder.iconFront.setVisibility(View.GONE);

            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.item_selector));
            //  holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_green));

            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(context.getContext(), holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }

        } else {

            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);

            holder.iconFront.setAlpha(1);

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(context.getContext(), holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }

        }

    }


    public void removeItem(int position) {

        listEquipmentType.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);

    }


    public void restoreItem(AssignedEquipment.Response deletedItem, int deletedIndex) {

        listEquipmentType.add(deletedIndex, deletedItem);
        // notify item added by position
        notifyItemInserted(deletedIndex);

    }

    public void clearSelections() {

        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();

    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
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


    /**
     * Showing popup menu when tapping on 3 dots
     */
/*
    private void showPopupMenu(View view, AssignEquipment.AssignEquipmentResponse equipmentResponse) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context.getActivity(), view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_option, popup.getMenu());

        listPrivilege = Utilities.getSetFromSharedPreferances(context.getActivity(), Global.KEY_PRIVILEGE_ID, null);

        popup.getMenu().getItem(0).setVisible(false);

      */
/*  if (!listPrivilege.contains(Utilities.ASSIGN_TO_USER_WRITE)) {

            popup.getMenu().getItem(0).setVisible(false);
        }*//*


        if (!listPrivilege.contains(Utilities.ASSIGN_TO_USER_DELETE)) {

            popup.getMenu().getItem(1).setVisible(false);

        }

        popup.setOnMenuItemClickListener(new AdaperAssignedEquipmentType.MyMenuItemClickListener(equipmentResponse));
        popup.show();
    }
*/
    @Override
    public int getItemCount() {
        return listEquipmentType.size();
    }

    public void updateImage(int position, String path, String imageBitmap) {

      /*  ArrayList<String> listImages = new ArrayList<>();
        listImages.add(imageBitmap);*/

        listEquipmentType.get(position).setImage_bitmap(imageBitmap);

        notifyDataSetChanged();

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView tvEquipmentType;
        private final ImageView imageEquipmentType;
        private final TextView tvEquipmentTypeDes;
        private final ImageView imageOption;
        //  private final ImageView imageEdit;
        // private final ImageView imageDelete;
        public RelativeLayout viewBackground, viewForeground;
        private final CardView cardView;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View view) {
            super(view);

            tvEquipmentType = (TextView) view.findViewById(R.id.tv_equipment_type);
            tvEquipmentTypeDes = (TextView) view.findViewById(R.id.tv_equipment_type_des);
            imageOption = (ImageView) view.findViewById(R.id.image_option);
            imageEquipmentType = (ImageView) view.findViewById(R.id.image_equipment_type);

            //  imageEdit = (ImageView) view.findViewById(R.id.image_edit);
            //  imageDelete = (ImageView) view.findViewById(R.id.image_delete);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);

            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            cardView = (CardView) view.findViewById(R.id.card_view);

            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {

            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;

        }
    }

    public interface AdapterAssignedEquipmentTypeUserListener {

        void onIconClicked(int position);
 /*
        void onIconImportantClicked(int position);*/

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }

    /**
     * Click listener for popup menu items
     */
    /*class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private final AssignEquipment.AssignEquipmentResponse equipmentResponse;

        public MyMenuItemClickListener(AssignEquipment.AssignEquipmentResponse equipmentResponse) {
            this.equipmentResponse = equipmentResponse;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:

                    ((FragmentEquipmentToUser) context).editClick(equipmentResponse);

                    return true;

                case R.id.action_delete:

                    ((FragmentEquipmentToUser) context).deleteClick(equipmentResponse, null);

                    return true;
                default:
            }
            return false;
        }

    }
*/
}
