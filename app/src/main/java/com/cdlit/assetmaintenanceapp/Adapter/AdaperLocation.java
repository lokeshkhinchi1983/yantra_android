package com.cdlit.assetmaintenanceapp.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.LocationResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentLocation;
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

public class AdaperLocation extends RecyclerView.Adapter<AdaperLocation.MyViewHolder> {

    private final List<LocationResponse> listLocation;
    private final FragmentLocation context;
    private final ActionModeListener listener;
    private final SparseBooleanArray selectedItems;
    private final SparseBooleanArray animationItemsIndex;
    private Set<String> listPrivilege;
    private boolean reverseAllAnimations = false;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    public AdaperLocation(FragmentLocation context, List<LocationResponse> listLocation, ActionModeListener listener) {

        this.listLocation = listLocation;
        this.context = context;
        listPrivilege = Utilities.getSetFromSharedPreferances(context.getActivity(), Global.KEY_PRIVILEGE_ID, null);
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final LocationResponse locationResponse = listLocation.get(position);

       /* if (!listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {

            // popup.getMenu().getItem(0).setVisible(false);

            holder.imageEdit.setVisibility(View.GONE);
        }

        if (!listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {

            //  popup.getMenu().getItem(1).setVisible(false);

            holder.imageDelete.setVisibility(View.GONE);
        }*/

        holder.location.setText(locationResponse.getLocationName());


      /*  holder.imageOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenu(holder.imageOption, locationResponse);


            }
        });*/

       /* holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentLocation) context).editClick(locationResponse);

            }
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FragmentLocation) context).deleteClick(locationResponse);

            }
        });*/
// handle icon animation
        applyIconAnimation(holder, position);


        // apply click events
        applyClickEvents(holder, position);
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {


       /* holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("onIconClicked", "" + position);

                listener.onIconClicked(position);

            }
        });*/


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

    private void applyIconAnimation(AdaperLocation.MyViewHolder holder, int position) {

        if (selectedItems.get(position, false)) {

          /*  holder.iconFront.setVisibility(View.GONE);

            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);*/

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.item_selector));
            //  holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_green));

         /*   if (currentSelectedIndex == position) {
                FlipAnimator.flipView(context.getContext(), holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }*/

        } else {

            /*holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);

            holder.iconFront.setAlpha(1);*/

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

           /* if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(context.getContext(), holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
*/
        }

    }

    @Override
    public int getItemCount() {
        return listLocation.size();
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, LocationResponse locationResponse) {
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

        if (!listPrivilege.contains(Utilities.LOCATION_MASTER_WRITE)) {

            popup.getMenu().getItem(0).setVisible(false);
        }

        if (!listPrivilege.contains(Utilities.LOCATION_MASTER_DELETE)) {

            popup.getMenu().getItem(1).setVisible(false);
        }


        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(locationResponse));

        popup.show();
    }

    public void removeItem(int position) {
        listLocation.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(LocationResponse deletedItem, int deletedIndex) {

        listLocation.add(deletedIndex, deletedItem);
        // notify item added by position
        notifyItemInserted(deletedIndex);

    }

    public void clearSelections() {

        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();

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
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private final LocationResponse locationResponse;

        public MyMenuItemClickListener(LocationResponse locationResponse) {
            this.locationResponse = locationResponse;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:

                    ((FragmentLocation) context).editClick(locationResponse);

                    return true;

                case R.id.action_delete:

                    ((FragmentLocation) context).deleteClick(locationResponse, null);

                    return true;
                default:
            }
            return false;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView location;
        //   private final ImageView imageOption;
        //  private final ImageView imageEdit;
        //  private final ImageView imageDelete;
        private final CardView cardView;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            location = (TextView) view.findViewById(R.id.tv_location);
            //   imageOption = (ImageView) view.findViewById(R.id.image_option);
            // imageEdit = (ImageView) view.findViewById(R.id.image_edit);
            // imageDelete = (ImageView) view.findViewById(R.id.image_delete);
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

    public interface AdapterLocationListener {

        void onIconClicked(int position);
 /*
        void onIconImportantClicked(int position);*/

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);

    }

}
