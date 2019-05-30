package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.R;

public class DialogFullImage extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private ImageView imageFull;
    private String encodedString;
    private Bitmap bitmap;
    private View view;

    public static DialogFullImage newInstance() {

        DialogFullImage dialogFullImage = new DialogFullImage();
        return dialogFullImage;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String title = args.getString("title");
        encodedString = args.getString("bitmapstring");

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
      //  customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        view = getActivity().getLayoutInflater().inflate(R.layout.dialog_full_image, null);
        imageFull = (ImageView) view.findViewById(R.id.image_full);
        alertDialogBuilder.setView(view);

        if (encodedString == null || encodedString.equalsIgnoreCase("")) {

            encodedString = "";

            bitmap = null;

            //imageUser.setImageResource(R.mipmap.ic_default_rounded_img);
            imageFull.setImageResource(R.drawable.ic_no_img);

        } else {

            byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);

            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imageFull.setImageBitmap(bitmap);

        }


        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok_bt_string), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });





   /*
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
*/

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_fad_in_out;
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //  dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

        return dialog;
    }



}
