package com.cdlit.assetmaintenanceapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogEquipmentChecklistImage;
import com.cdlit.assetmaintenanceapp.Dialog.DialogFullImage;
import com.cdlit.assetmaintenanceapp.Model.UserEquipmentChecklist;
import com.cdlit.assetmaintenanceapp.R;
import com.tooltip.Tooltip;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

/**
 * Created by rakesh on 08-09-2017.
 */

public class AdapterUserEquipmentChecklist extends RecyclerView.Adapter<AdapterUserEquipmentChecklist.MyViewHolder> {

    private final String timeStamp;
    private AppCompatActivity activity;
    public ArrayList<UserEquipmentChecklist.Response> listChecklist;
    private DialogEquipmentChecklistImage dialogEquipmentChecklistImage;
    public MyViewHolder holder;

    public AdapterUserEquipmentChecklist(AppCompatActivity activity, ArrayList<UserEquipmentChecklist.Response> listChecklist, String timeStamp) {

        this.activity = activity;
        this.listChecklist = listChecklist;
        this.timeStamp = timeStamp;

        Log.e("timeStamp", "" + timeStamp);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_equipment_checklist_list_item, parent, false);

        return new AdapterUserEquipmentChecklist.MyViewHolder(itemView, new MyCustomEditTextListener());

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        this.holder = holder;

        holder.tvEquipmentDes.setVisibility(View.GONE);

        if (timeStamp == null || timeStamp.equalsIgnoreCase("")) {

            holder.tvEquipmentNo.setEnabled(true);
            holder.tvEquipment.setEnabled(true);
            //    holder.tvEquipmentDes.setEnabled(true);
            holder.etComment.setEnabled(true);
            //    holder.chEquipmentChecklistNotOk.setEnabled(true);
            //    holder.rgOkNOtOk.setEnabled(true);
            holder.rbNotOk.setEnabled(true);
            holder.rbOk.setEnabled(true);

            holder.rbMinor.setEnabled(true);
            holder.rbMajor.setEnabled(true);

            holder.llComments.setVisibility(VISIBLE);
            //   holder.tvEquipmentReadMore.setVisibility(View.GONE);

        } else {

            holder.tvEquipmentNo.setEnabled(false);
            holder.tvEquipment.setEnabled(false);
            //   holder.tvEquipmentDes.setEnabled(false);
            holder.etComment.setEnabled(false);
            //    holder.chEquipmentChecklistNotOk.setEnabled(false);
            //    holder.rgOkNOtOk.setEnabled(false);
            holder.rbNotOk.setEnabled(false);
            holder.rbOk.setEnabled(false);

            holder.rbMinor.setEnabled(false);
            holder.rbMajor.setEnabled(false);

            holder.llComments.setVisibility(View.GONE);
            //    holder.tvEquipmentReadMore.setVisibility(View.VISIBLE);

        }

        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mDataset to update
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());


        holder.tvEquipmentNo.setText("( " + (position + 1) + " ) ");
        holder.rbOk.setVisibility(VISIBLE);
        holder.rbNotOk.setVisibility(VISIBLE);
        holder.rbMinor.setVisibility(VISIBLE);
        holder.rbMajor.setVisibility(VISIBLE);

        holder.tvEquipment.setTextColor(activity.getResources().getColor(R.color.text_color_primary_dark));

        holder.tvEquipment.setText(listChecklist.get(position).getCheckListName() + " ( " + listChecklist.get(position).getCheckListDescription() + " )");

        holder.tvEquipmentReadMore.setVisibility(VISIBLE);

        holder.itemView.setVisibility(View.VISIBLE);

        String bitmapstring;

        if (listChecklist.get(position).getImages() == null || listChecklist.get(position).getImages().size() == 0) {
            bitmapstring = null;
        } else {
            bitmapstring = listChecklist.get(position).getImages().get(0);
        }

        if (bitmapstring == null || bitmapstring.equalsIgnoreCase("")) {

            holder.imageEquipment.
                    setImageBitmap(null);

            holder.imageEquipment.setBackgroundResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(bitmapstring, Base64.DEFAULT);

            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.imageEquipment.
                    setImageBitmap(decodedByte);

        }


        holder.etComment.setText(listChecklist.get(position).getComment());


        // checklistType 0 for comment type and checklistType 1 for miles/hours type..
        if (listChecklist.get(position).getChecklistType() == 0) {

            holder.rbNotOk.setVisibility(VISIBLE);
            holder.rbOk.setVisibility(VISIBLE);

            holder.imageEquipment.setVisibility(VISIBLE);
            holder.fabAddEquipment.setVisibility(VISIBLE);

            holder.etComment.setHint("Enter Comments");

            holder.etComment.setInputType(InputType.TYPE_CLASS_TEXT);

            // for manager adding comments or not..

            if (listChecklist.get(position).getManagerComment() == null && listChecklist.get(position).getManagerName() == null) {

                holder.tvManagerComment.setVisibility(View.GONE);

            } else {

                holder.tvManagerComment.setVisibility(VISIBLE);

                holder.tvManagerComment.setText(listChecklist.get(position).getManagerName() + " : " + listChecklist.get(position).getManagerComment() + " ( " + listChecklist.get(position).getModifiedDate() + " )");

            }

        } else {

            holder.rbNotOk.setVisibility(View.GONE);
            holder.rbOk.setVisibility(View.GONE);

            holder.rbMajor.setVisibility(View.GONE);
            holder.rbMinor.setVisibility(View.GONE);

            holder.imageEquipment.setVisibility(View.GONE);
            holder.fabAddEquipment.setVisibility(View.GONE);

            holder.etComment.setHint("Enter miles / hours");

            holder.etComment.setInputType(InputType.TYPE_CLASS_NUMBER);

            // for manager adding comments or not..

            if (listChecklist.get(position).getManagerComment() == null && listChecklist.get(position).getManagerName() == null) {

                holder.tvManagerComment.setVisibility(View.GONE);

            } else {

                holder.tvManagerComment.setVisibility(VISIBLE);

                holder.tvManagerComment.setText(listChecklist.get(position).getManagerName() + " : " + listChecklist.get(position).getManagerComment() + " ( " + listChecklist.get(position).getModifiedDate() + " )");

            }

        }


        holder.rbOk.setTag(holder.getAdapterPosition());

        holder.rbNotOk.setTag(holder.getAdapterPosition());

        holder.rbMinor.setTag(holder.getAdapterPosition());

        holder.rbMajor.setTag(holder.getAdapterPosition());


        Log.e("setTag", "Position: " + position + " - Answer: " + listChecklist.get(position).getBooleanAnswer());


        if (listChecklist.get(position).getBooleanAnswer() == 1) {

            holder.rbOk.setChecked(true);
            holder.etComment.setEnabled(false);
            holder.etComment.setVisibility(View.GONE);
            holder.fabAddEquipment.setVisibility(View.GONE);
            holder.llComments.setVisibility(View.GONE);

        } else {

            holder.rbNotOk.setChecked(true);

            if (timeStamp == null || timeStamp.equalsIgnoreCase("")) {
                holder.etComment.setEnabled(true);
                holder.etComment.setVisibility(VISIBLE);
                holder.fabAddEquipment.setVisibility(VISIBLE);
                holder.llComments.setVisibility(VISIBLE);
            } else {
                holder.etComment.setEnabled(false);
                holder.etComment.setVisibility(VISIBLE);
                holder.fabAddEquipment.setVisibility(VISIBLE);
                holder.llComments.setVisibility(VISIBLE);
            }

            if (listChecklist.get(position).getFaultType() == 0) {

                holder.rbMinor.setChecked(true);

            } else {

                holder.rbMajor.setChecked(true);

            }

        }

        int tvEquipmentLength = holder.tvEquipment.getText().toString().length();

        Log.e("tvEquipment length", "" + tvEquipmentLength);

        if (tvEquipmentLength >= 40) {

            holder.tvEquipmentReadMore.setVisibility(VISIBLE);
            Log.e("tvEquipment length", "" + holder.tvEquipment.getText().toString() + "Visible");

        } else {

            holder.tvEquipmentReadMore.setVisibility(View.GONE);
            Log.e("tvEquipment length", "" + holder.tvEquipment.getText().toString() + "Gone");

        }


      /*  // for manager adding comments or not..

        if (listChecklist.get(position).getManagerComment() == null && listChecklist.get(position).getManagerName() == null) {

            holder.tvManagerComment.setVisibility(View.GONE);

            holder.etComment.setVisibility(VISIBLE);

            holder.rbNotOk.setVisibility(VISIBLE);
            holder.rbOk.setVisibility(VISIBLE);

            holder.rbMajor.setVisibility(VISIBLE);
            holder.rbMinor.setVisibility(VISIBLE);

            holder.imageEquipment.setVisibility(VISIBLE);
            holder.fabAddEquipment.setVisibility(VISIBLE);

        } else {

            holder.tvManagerComment.setVisibility(VISIBLE);

            holder.tvManagerComment.setText(listChecklist.get(position).getManagerName() + " : " + listChecklist.get(position).getManagerComment() + " ( " + listChecklist.get(position).getModifiedDate() + " )");

            holder.etComment.setVisibility(View.GONE);

            holder.rbNotOk.setVisibility(View.GONE);
            holder.rbOk.setVisibility(View.GONE);

            holder.rbMajor.setVisibility(View.GONE);
            holder.rbMinor.setVisibility(View.GONE);

            holder.imageEquipment.setVisibility(View.GONE);
            holder.fabAddEquipment.setVisibility(View.GONE);

        }*/


        holder.rbOk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                RadioButton rb = (RadioButton) buttonView;

                Integer pos = (Integer) rb.getTag();

                int answer;

                if (rb.isChecked()) {

                    answer = 1;
                    holder.etComment.setEnabled(false);
                    holder.etComment.setVisibility(View.GONE);

                    holder.fabAddEquipment.setVisibility(View.GONE);
                    holder.imageEquipment.setVisibility(View.GONE);
                    holder.llComments.setVisibility(View.GONE);

                } else {

                    answer = 0;
                    holder.etComment.setEnabled(true);
                    holder.etComment.setVisibility(VISIBLE);

                    holder.fabAddEquipment.setVisibility(VISIBLE);
                    holder.imageEquipment.setVisibility(VISIBLE);
                    holder.llComments.setVisibility(VISIBLE);

                }

                Log.e("Pos - Answer: ", "Pos : " + pos + "Ans : " + answer);
                listChecklist.get(pos).setBooleanAnswer(answer);

            }
        });


        holder.rbNotOk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                RadioButton rb = (RadioButton) buttonView;
                Integer pos = (Integer) rb.getTag();

                int answer;

                if (rb.isChecked()) {

                    answer = 0;
                    holder.etComment.setEnabled(true);
                    holder.etComment.setVisibility(VISIBLE);

                    holder.fabAddEquipment.setVisibility(VISIBLE);
                    holder.imageEquipment.setVisibility(VISIBLE);
                    holder.llComments.setVisibility(VISIBLE);

                } else {

                    answer = 1;
                    holder.etComment.setEnabled(false);
                    holder.etComment.setVisibility(View.GONE);

                    holder.fabAddEquipment.setVisibility(View.GONE);
                    holder.imageEquipment.setVisibility(View.GONE);
                    holder.llComments.setVisibility(View.GONE);

                }

                Log.e("Pos - Answer: ", "Pos : " + pos + "Ans : " + answer);
                listChecklist.get(pos).setBooleanAnswer(answer);

            }
        });


        holder.rbMinor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                RadioButton rb = (RadioButton) buttonView;

                Integer pos = (Integer) rb.getTag();

                int answer;

                if (rb.isChecked()) {
                    answer = 0;

                } else {
                    answer = 1;

                }

                Log.e("Pos - Answer: ", "Pos : " + pos + "Ans : " + answer);
                listChecklist.get(pos).setFaultType(answer);

            }
        });


        holder.rbMajor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                RadioButton rb = (RadioButton) buttonView;

                Integer pos = (Integer) rb.getTag();

                int answer;

                if (rb.isChecked()) {

                    answer = 1;

                } else {

                    answer = 0;

                }

                Log.e("Pos - Answer: ", "Pos : " + pos + "Ans : " + answer);
                listChecklist.get(pos).setFaultType(answer);

            }
        });



        holder.tvEquipmentReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Read More:", listChecklist.get(position).getCheckListName() + " / " + listChecklist.get(position).getCheckListDescription());

                String toolTipText = listChecklist.get(position).getCheckListDescription();

                showTooltip(v, toolTipText);

            }
        });


        holder.imageEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bitmapstring;

                if (listChecklist.get(position).getImages() == null || listChecklist.get(position).getImages().size() == 0) {
                    bitmapstring = null;
                } else {
                    bitmapstring = listChecklist.get(position).getImages().get(0);
                }

                if (bitmapstring != null && !bitmapstring.equalsIgnoreCase("")) {

                    DialogFullImage dialogFullmage = DialogFullImage.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString("bitmapstring", bitmapstring);

                    dialogFullmage.setArguments(bundle);
                    dialogFullmage.show(activity.getSupportFragmentManager(), "dialog");

                }

            }
        });


        holder.fabAddEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogEquipmentChecklistImage = DialogEquipmentChecklistImage.newInstance();

                Bundle bundle = new Bundle();

                bundle.putString("title", activity.getString(R.string.add_equipment_checklist_title));

                bundle.putString("time_stamp", timeStamp);

                bundle.putParcelable("user_checklist_response", listChecklist.get(position));

                bundle.putInt("position", position);

                dialogEquipmentChecklistImage.setArguments(bundle);

                dialogEquipmentChecklistImage.show(activity.getSupportFragmentManager(), "show");

            }
        });

    }

    private void showTooltip(View v, String toolTipText) {

        Tooltip toolTip = new Tooltip.Builder(v).
                setText("" + toolTipText).
                setTextColor(Color.WHITE).
                setGravity(Gravity.BOTTOM).
                setCornerRadius(8f).setCancelable(true).
                setDismissOnClick(true).
                setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary)).show();

    }

    @Override
    public int getItemCount() {
        return listChecklist.size();
    }

    public void updateImage(int loaderId, String path, String imageBitmap) {

        ArrayList<String> listBitmap = new ArrayList<>();
        listBitmap.add(imageBitmap);

        listChecklist.get(loaderId).setImages(listBitmap);

        notifyDataSetChanged();

    }


    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            listChecklist.get(position).setComment(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvEquipment;
        private final TextView tvEquipmentDes;
        public final EditText etComment;
        private final FloatingActionButton fabAddEquipment;
        private final TextView tvEquipmentNo;
        private final MyCustomEditTextListener myCustomEditTextListener;
        //   private final CheckBox chEquipmentChecklistNotOk;
        //  private final TextView tvNoOfImages;
        //  private final RadioGroup rgOkNOtOk;
        private final RadioButton rbOk;
        private final RadioButton rbNotOk;
        private final ImageView imageEquipment;
        private final LinearLayout llComments;
        private final TextView tvEquipmentReadMore;
        private final RadioButton rbMinor, rbMajor;
        private final TextView tvManagerComment;

        public MyViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {

            super(itemView);

            tvEquipmentReadMore = (TextView) itemView.findViewById(R.id.tv_equipment_read_more);

            tvEquipmentNo = (TextView) itemView.findViewById(R.id.tv_equipment_no);
            tvEquipment = (TextView) itemView.findViewById(R.id.tv_equipment);
            tvEquipmentDes = (TextView) itemView.findViewById(R.id.tv_equipment_des);
            etComment = (EditText) itemView.findViewById(R.id.et_comment);
            //    tvNoOfImages = (TextView) itemView.findViewById(R.id.tv_no_of_images);

            //  rgOkNOtOk = (RadioGroup) itemView.findViewById(R.id.rg_ok_notok);
            rbOk = (RadioButton) itemView.findViewById(R.id.rb_ok);
            rbNotOk = (RadioButton) itemView.findViewById(R.id.rb_not_ok);

            imageEquipment = (ImageView) itemView.findViewById(R.id.image_equipment);
            llComments = (LinearLayout) itemView.findViewById(R.id.ll_comments);

            this.myCustomEditTextListener = myCustomEditTextListener;

            etComment.addTextChangedListener(myCustomEditTextListener);

            fabAddEquipment = (FloatingActionButton) itemView.findViewById(R.id.fab_add_equipment);
            //   chEquipmentChecklistNotOk = (CheckBox) itemView.findViewById(R.id.ch_equipment_checklist_notok);

            rbMinor = (RadioButton) itemView.findViewById(R.id.rb_minor);
            rbMajor = (RadioButton) itemView.findViewById(R.id.rb_major);

            tvManagerComment = (TextView) itemView.findViewById(R.id.tv_manager_comment);

        }

    }
}
