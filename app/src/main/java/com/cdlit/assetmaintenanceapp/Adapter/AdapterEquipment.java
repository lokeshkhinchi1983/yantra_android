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
import com.cdlit.assetmaintenanceapp.Model.EquipmentResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipment;
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

public class AdapterEquipment extends RecyclerView.Adapter<AdapterEquipment.MyViewHolder> {

    private final List<EquipmentResponse> listEquipment;
    private final FragmentEquipment context;
    private final SparseBooleanArray selectedItems;
    private final SparseBooleanArray animationItemsIndex;
    private final ActionModeListener listener;
    private Set<String> listPrivilege;
    private Bitmap decodedByte;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    private boolean reverseAllAnimations = false;

    public AdapterEquipment(FragmentEquipment context, List<EquipmentResponse> listEquipment, ActionModeListener listener) {

        this.listEquipment = listEquipment;
        this.context = context;
        this.listener = listener;
        listPrivilege = Utilities.getSetFromSharedPreferances(context.getActivity(), Global.KEY_PRIVILEGE_ID, null);
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.equipment_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public int getItemCount() {
        return listEquipment.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //   this.holder = holder;
        EquipmentResponse equipmentResponse = listEquipment.get(position);



        holder.tvEquipmentItem.setText(equipmentResponse.getLocation().getLocationName() + " / " +
                equipmentResponse.getEquipmentType().getEquipmentTypeName() + " / " + equipmentResponse.getModelNo());

        holder.tvEquipmentDes.setText(equipmentResponse.getDescription());

        String bitmapstring;

        if (equipmentResponse.getEquipmentImages() == null || equipmentResponse.getEquipmentImages().size() == 0) {
            bitmapstring = null;
        } else {
            bitmapstring = equipmentResponse.getEquipmentImages().get(0).getBitmapstring();
        }

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            decodedByte = null;

            holder.imageEquipment.
                    setImageBitmap(decodedByte);

            holder.imageEquipment.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageEquipment.
                    setImageBitmap(decodedByte);

        }



        // handle icon animation
        applyIconAnimation(holder, position);

        // apply click events
        applyClickEvents(holder, position);
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


      /*  View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        };*/

      /*  if (getSelectedItemCount() > 0) {
            Log.e("acitonmode not null", "" + position);

            holder.imageEquipment.setClickable(false);
            holder.imageEquipment.setEnabled(false);

        } else {
            Log.e("acitonmode null", "" + position);

            holder.imageEquipment.setClickable(true);
            holder.imageEquipment.setEnabled(true);

        }*/


        holder.imageEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring;

                if (listEquipment.get(position).getEquipmentImages() == null || listEquipment.get(position).getEquipmentImages().size() == 0) {
                    bitmapstring = null;
                } else {
                    bitmapstring = listEquipment.get(position).getEquipmentImages().get(0).getBitmapstring();
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

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void removeItem(int position) {
        listEquipment.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(EquipmentResponse item, int position) {
        listEquipment.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void updateImage(int position, String path, String imageBitmap) {

        listEquipment.get(position).getEquipmentImages().get(0).setBitmapstring(imageBitmap);
        notifyDataSetChanged();

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
        listEquipment.remove(position);
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

    public class MyViewHolder extends RecyclerView.ViewHolder /*implements View.OnLongClickListener*/ {
        private final TextView tvEquipmentItem, tvEquipmentDes;
        // private final ImageView imageOption;
        public final ImageView imageEquipment;
        private final CardView cardView;
        //  private final ImageView imageEdit;
        //  private final ImageView imageDelete;
        // private final ImageView imageTimeline;
        public RelativeLayout viewBackground, viewForeground;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View view) {

            super(view);
            tvEquipmentItem = (TextView) view.findViewById(R.id.tv_equipment_item);
            tvEquipmentDes = (TextView) view.findViewById(R.id.tv_equipment_des);
            // imageOption = (ImageView) view.findViewById(R.id.image_option);
            imageEquipment = (ImageView) view.findViewById(R.id.image_equipment);
            //   imageEdit = (ImageView) view.findViewById(R.id.image_edit);
            //  imageDelete = (ImageView) view.findViewById(R.id.image_delete);
            //  imageTimeline = (ImageView) view.findViewById(R.id.image_timeline);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            cardView = (CardView) view.findViewById(R.id.card_view);


        }


    }


}
