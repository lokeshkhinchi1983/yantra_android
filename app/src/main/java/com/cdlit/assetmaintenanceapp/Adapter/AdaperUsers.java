package com.cdlit.assetmaintenanceapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Model.UserResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentManageUser;
import com.cdlit.assetmaintenanceapp.Utils.ActionModeListener;
import com.cdlit.assetmaintenanceapp.Utils.FlipAnimator;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;
import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rakesh on 04-07-2017.
 */

public class AdaperUsers extends RecyclerView.Adapter<AdaperUsers.MyViewHolder> {

    private final List<UserResponse> listUsers;
    private final FragmentManageUser context;
    private final ActionModeListener listener;
    private final SparseBooleanArray selectedItems;
    private final SparseBooleanArray animationItemsIndex;
    private Set<String> listPrivilege;
    private Bitmap decodedByte;
    private String bitmapstring;
    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    private boolean reverseAllAnimations = false;
    //   private MyViewHolder holder;
    private DrawableRequestBuilder<String> img;


    public AdaperUsers(FragmentManageUser context, List<UserResponse> listUsers, ActionModeListener listener) {

        this.listUsers = listUsers;
        this.context = context;
        this.listener = listener;
        listPrivilege = Utilities.getSetFromSharedPreferances(context.getActivity(), Global.KEY_PRIVILEGE_ID, null);
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public int getItemCount() {

        return listUsers.size();

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //   this.holder = holder;

        final UserResponse userResponse = listUsers.get(position);

        holder.tvUserFirstLastName.setText(userResponse.getFirstName() + " " + userResponse.getLastName());

        holder.tvUserType.setText(userResponse.getUserType() + " ( " + userResponse.getLocation().getLocationName() + " )");

        bitmapstring = userResponse.getImage_bitmap();


        /// rakesh glide

/*
        String imagePath = userResponse.getImage_path();

        if (imagePath == null || imagePath.equalsIgnoreCase("")) {

            // make sure Glide doesn't load anything into this view until told otherwise
         //   Glide.clear(holder.imageUser);
            holder.imageUser.setBackgroundResource(R.drawable.ic_no_img);

         *//*   //Loading image from url into imageView
            Target<GlideDrawable> imag = Glide.with(context)
                    .load(imagePath).placeholder(R.drawable.ic_no_img).into(holder.imageUser);*//*

        } else {
            //Loading image from url into imageView
            Target<GlideDrawable> imag = Glide.with(context)
                    .load(imagePath).placeholder(R.drawable.ic_no_img).into(holder.imageUser);

        }*/


        /// rakesh glide




      /*  if (imagePath == null || imagePath.equalsIgnoreCase("")) {
            holder.imageUser.setBackgroundResource(R.drawable.ic_no_img);
           // Glide.with(context).load(R.drawable.ic_no_img).into(holder.imageUser);
           // Glide.with(context).load(imagePath).error(android.R.drawable.ic_lock_lock).into(holder.imageUser);
        }

        else {
            Glide.with(context).load(imagePath).into(holder.imageUser);
        }
*/

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


      /*  //Loading image from url into imageView
        Target<GlideDrawable> imag = Glide.with(context)
                .load(Global.TEST_BASE_URL + "image/get/equipments/etype_20180628_06-30-00MRxAE").placeholder(R.drawable.ic_no_img).thumbnail(0.2f)
                .into(holder.imageUser);
*/

        //   http://192.168.1.23:8080/api/image/get/equipments/etype_20180628_06-30-00MRxAE

        /*if (img == null) {

            img.placeholder(R.drawable.ic_no_img).into(holder.imageUser);

        } else {

            img.into(holder.imageUser);

        }*/


        holder.imageUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String bitmapstring = listUsers.get(position).getImage_bitmap();

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(context.getFragmentManager(), "dialog");

                }

            }
        });

        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("onIconClicked", "" + position);

                Log.e("getFirstName", "" + listUsers.get(position).getFirstName());

                listener.onIconClicked(position);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("setOnClickListener", "" + position);

                Log.e("getFirstName", "" + listUsers.get(position).getFirstName());

                listener.onMessageRowClicked(position);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Log.e("setOnLongClickListener", "" + position);

                Log.e("getFirstName", "" + listUsers.get(position).getFirstName());

                listener.onRowLongClicked(position);

                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                return true;
            }
        });

        // handle icon animation
        applyIconAnimation(holder, position);

    }

    public void updateImage(int position, String path, String response) {
        Log.e("adapter position---", "" + position);
        listUsers.get(position).setImage_bitmap(response);
        notifyDataSetChanged();
    }


    public void updateUserImage(int position, String path) {

     /*   //Loading image from url into imageView
        DrawableRequestBuilder<String> imag = Glide.with(context)
                .load(path).placeholder(R.drawable.ic_no_img).thumbnail(0.2f);*/

        //  listUsers.get(position).setImag(imag);

        listUsers.get(position).setImage_path(path);

        //   notifyDataSetChanged();

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

    public List<Integer> getSelectedItems() {

        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;

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


    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, UserResponse userResponse) {
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

        if (!listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {

            popup.getMenu().getItem(0).setVisible(false);

        }

        if (listPrivilege.contains(Utilities.USER_MASTER_WRITE)) {

            popup.getMenu().getItem(3).setVisible(true);

        } else {

            popup.getMenu().getItem(3).setVisible(false);

        }

        if (!listPrivilege.contains(Utilities.USER_MASTER_DELETE)) {

            popup.getMenu().getItem(1).setVisible(false);

        }


        //  popup.setOnMenuItemClickListener(new MyMenuItemClickListener(userResponse));
        popup.show();
    }


    public void removeItem(int position) {

        listUsers.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);

    }

    public void restoreItem(UserResponse item, int position) {

        listUsers.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
        // notifyItemChanged(position);

    }



    /* */

    /**
     * Click listener for popup menu items
     *//*
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private final UserResponse userResponse;

        public MyMenuItemClickListener(UserResponse userResponse) {
            this.userResponse = userResponse;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:

                    ((FragmentManageUser) context).editClick(userResponse);

                    return true;

                case R.id.action_delete:

                    ((FragmentManageUser) context).deleteClick(userResponse, null);

                    return true;

                case R.id.action_update_user_privilege:

                    ((FragmentManageUser) context).updatePrivilegeClick(userResponse);

                    return true;

                default:
            }
            return false;
        }
    }
*/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final TextView tvUserFirstLastName;
        //  private final ImageView imageOption;

        //private final ImageView imageEdit;
        // private final ImageView imageDelete;
        //   private final ImageView imageEditPrivilege;
        private final ImageView imageUser;
        private final TextView tvUserType;
        private final CardView cardView;
        public RelativeLayout viewBackground, viewForeground;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View view) {

            super(view);

            // imageOption = (ImageView) view.findViewById(R.id.image_option);
            imageUser = (ImageView) view.findViewById(R.id.image_user);
            tvUserFirstLastName = (TextView) view.findViewById(R.id.tv_user_first_last_name);
            //   tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            //  imageEdit = (ImageView) view.findViewById(R.id.image_edit);
            //imageDelete = (ImageView) view.findViewById(R.id.image_delete);
            //  imageEditPrivilege = (ImageView) view.findViewById(R.id.image_edit_privilege);

            tvUserType = (TextView) view.findViewById(R.id.tv_user_type);
            viewBackground = (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout) view.findViewById(R.id.view_foreground);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
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

   /* public interface AdapterUserListener {

        void onIconClicked(int position);
 *//*
        void onIconImportantClicked(int position);*//*

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }
*/

}
