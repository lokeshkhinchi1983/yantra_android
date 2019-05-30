package com.cdlit.assetmaintenanceapp.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.EquipmentChecklistResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipmentCheckList;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rakesh on 16-06-2017.
 */

public class AdaperEquipmentChecklist extends RecyclerView.Adapter<AdaperEquipmentChecklist.MyViewHolder> {

    private final List<EquipmentChecklistResponse> listChecklist;
    private final FragmentEquipmentCheckList context;
    private final ActionModeListener listener;
    private final SparseBooleanArray selectedItems;
    private final SparseBooleanArray animationItemsIndex;
    private Set<String> listPrivilege;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    private boolean reverseAllAnimations = false;

    public AdaperEquipmentChecklist(FragmentEquipmentCheckList context, List<EquipmentChecklistResponse> listChecklist, ActionModeListener listener) {
        this.listChecklist = listChecklist;
        this.context = context;
        this.listener = listener;
        listPrivilege = Utilities.getSetFromSharedPreferances(context.getActivity(), Global.KEY_PRIVILEGE_ID, null);
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.equipment_checklist_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final EquipmentChecklistResponse equipmentChecklistResponse = listChecklist.get(position);

      /*  if (!listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_WRITE)) {

            //    popup.getMenu().getItem(0).setVisible(false);
            holder.imageEdit.setVisibility(View.GONE);

        }

        if (!listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE)) {

            //   popup.getMenu().getItem(1).setVisible(false);
            holder.imageDelete.setVisibility(View.GONE);

        }
*/



       /* if (position == 0) {

            holder.tvChecklistItem.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.imageOption.setVisibility(View.GONE);

        } else {


        }*/

        //   if (equipmentChecklistResponse.getCheckListName().equalsIgnoreCase("other comment")) {

        if (false) {

            holder.tvChecklistItem.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvChecklistItem.setText(equipmentChecklistResponse.getCheckListName());
            holder.tvChecklistDes.setText("");
            //  holder.imageOption.setVisibility(View.GONE);
            //  holder.imageEdit.setVisibility(View.GONE);
            //    holder.imageDelete.setVisibility(View.GONE);

        } else {

            holder.tvChecklistItem.setTextColor(context.getResources().getColor(R.color.text_color_primary_dark));
            holder.tvChecklistItem.setText(equipmentChecklistResponse.getCheckListName());
            holder.tvChecklistDes.setText(equipmentChecklistResponse.getDescription());
            holder.tvChecklistFreq.setText(equipmentChecklistResponse.getFrequency());
            // holder.imageOption.setVisibility(View.VISIBLE);
            //   holder.imageEdit.setVisibility(View.VISIBLE);
            //    holder.imageDelete.setVisibility(View.VISIBLE);

        }

        //    holder.tvChecklistItem.setText(equipmentChecklistResponse.getCheckListName());
        //    holder.tvChecklistDes.setText(equipmentChecklistResponse.getDescription());

        Log.e("name", "" + equipmentChecklistResponse.getCheckListName());
        Log.e("des", "" + equipmentChecklistResponse.getDescription());

      /*  holder.imageOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenu(holder.imageOption, equipmentChecklistResponse);

            }
        });
*/
       /* holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((FragmentEquipmentCheckList) context).editClick(equipmentChecklistResponse);
            }
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((FragmentEquipmentCheckList) context).deleteClick(equipmentChecklistResponse);

            }
        });*/
        // handle icon animation
        applyIconAnimation(holder, position);

      /*  holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("onIconClicked", "" + position);

                listener.onIconClicked(position);

            }
        });
*/

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
        listChecklist.remove(position);
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

    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {

          /*  holder.iconFront.setVisibility(View.GONE);

            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);*/

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.item_selector));
            //  holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_green));

           /* if (currentSelectedIndex == position) {
                FlipAnimator.flipView(context.getContext(), holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }*/

        } else {

         /*   holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
                holder.iconFront.setAlpha(1);*/


            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
/*
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(context.getContext(), holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }*/

        }


    }

    @Override
    public int getItemCount() {
        return listChecklist.size();
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, EquipmentChecklistResponse equipmentChecklistResponse) {

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

        if (!listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_WRITE)) {

            popup.getMenu().getItem(0).setVisible(false);
        }

        if (!listPrivilege.contains(Utilities.EQUIPMENT_CHECKLIST_MASTER_DELETE)) {

            popup.getMenu().getItem(1).setVisible(false);
        }

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(equipmentChecklistResponse));
        popup.show();
    }

    public void restoreItem(EquipmentChecklistResponse item, int position) {

        listChecklist.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
        // notifyItemChanged(position);


    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private final EquipmentChecklistResponse equipmentChecklistResponse;

        public MyMenuItemClickListener(EquipmentChecklistResponse equipmentChecklistResponse) {
            this.equipmentChecklistResponse = equipmentChecklistResponse;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:

                    ((FragmentEquipmentCheckList) context).editClick(equipmentChecklistResponse);

                    return true;

                case R.id.action_delete:

                    ((FragmentEquipmentCheckList) context).deleteClick(equipmentChecklistResponse, null);

                    return true;
                default:
            }
            return false;
        }
    }

    public void removeItem(int position) {
        listChecklist.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView tvChecklistItem, tvChecklistDes;
        private final CardView cardView;
        private final TextView tvChecklistFreq;
        //  private final ImageView imageOption;
        //  private final ImageView imageEdit;
        //   private final ImageView imageDelete;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            tvChecklistItem = (TextView) view.findViewById(R.id.tv_checklist_item);
            tvChecklistDes = (TextView) view.findViewById(R.id.tv_checklist_des);
            //  imageOption = (ImageView) view.findViewById(R.id.image_option);
            //   imageEdit = (ImageView) view.findViewById(R.id.image_edit);
            //   imageDelete = (ImageView) view.findViewById(R.id.image_delete);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            cardView = (CardView) view.findViewById(R.id.card_view);
            tvChecklistFreq = (TextView) view.findViewById(R.id.tv_checklist_freq);

            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {

            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;

        }
    }

    public interface AdapterEquipmentChecklistListener {

        /*void onIconClicked(int position);*/
 /*
        void onIconImportantClicked(int position);*/

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }


}
