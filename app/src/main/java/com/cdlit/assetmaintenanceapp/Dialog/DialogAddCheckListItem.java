package com.cdlit.assetmaintenanceapp.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.EquipmentChecklistResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentEquipmentCheckList;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

import java.util.ArrayList;

/**
 * Created by rakesh on 13-06-2017.
 */

public class DialogAddCheckListItem extends DialogFragment {

    private AlertDialog.Builder alertDialogBuilder;
    private EditText etAddChecklistDec;
    private EditText etAddChecklistName;
    private EquipmentChecklistResponse response;
    private String okString;
    private Spinner checklistFreq;
    private ArrayList<String> listchecklistFreq;
    private ArrayAdapter<String> checklistAdapter;
    private TextView tvWeekDay;
    private LinearLayout llWeekDay;
    private Spinner spinnerWeekDay;
    private TextView tvMonthDay;
    private TextView tvOtherDay;
    private EditText etMonthDay;
    private ArrayList<String> listDayFreq;
    private ArrayAdapter<String> checkDayAdapter;
    private Spinner spinnerChecklistType;
    private ArrayList<String> listchecklistType;
    private ArrayAdapter<String> checklistTypeAdapter;

    public static DialogAddCheckListItem newInstance() {

        DialogAddCheckListItem dialogAddCheckListItem = new DialogAddCheckListItem();
        return dialogAddCheckListItem;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        final String title = args.getString("title");
        response = (EquipmentChecklistResponse) args.getParcelable("response");

        alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        //  alertDialogBuilder.setTitle("" + title);

        TextView customTitle = new TextView(getActivity());
        // You Can Customise your Title here
        customTitle.setText(title);
        //  customTitle.setAllCaps(true);
        customTitle.setPadding(16, 16, 16, 16);
        customTitle.setGravity(Gravity.CENTER);
        customTitle.setTextColor(getResources().getColor(R.color.text_color_primary_dark));
        customTitle.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        alertDialogBuilder.setCustomTitle(customTitle);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_checklist_item, null);

        etAddChecklistName = (EditText) view.findViewById(R.id.et_add_checklist_name);

        etAddChecklistDec = (EditText) view.findViewById(R.id.et_add_checklist_dec);

        checklistFreq = (Spinner) view.findViewById(R.id.checklist_freq);

        spinnerChecklistType = (Spinner) view.findViewById(R.id.spinner_checklist_type);

        tvWeekDay = (TextView) view.findViewById(R.id.tv_week_day);
        llWeekDay = (LinearLayout) view.findViewById(R.id.ll_week_day);
        spinnerWeekDay = (Spinner) view.findViewById(R.id.spinner_week_day);
        tvMonthDay = (TextView) view.findViewById(R.id.tv_month_day);
        etMonthDay = (EditText) view.findViewById(R.id.et_month_day);
        tvOtherDay = (TextView) view.findViewById(R.id.tv_other_day);

        listchecklistType = new ArrayList<String>();
        listchecklistType.add("Select Type");
        listchecklistType.add("Yes / No");
        listchecklistType.add("Input");


        checklistTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listchecklistType);
        checklistTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChecklistType.setAdapter(checklistTypeAdapter);


        listchecklistFreq = new ArrayList<String>();
        listchecklistFreq.add("Select Frequency");

        listchecklistFreq.add("Daily");
        listchecklistFreq.add("Weekly");
        listchecklistFreq.add("Monthly");
        listchecklistFreq.add("Quarterly");
        listchecklistFreq.add("Half Yearly");
        listchecklistFreq.add("Yearly");

        checklistAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listchecklistFreq);
        checklistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistFreq.setAdapter(checklistAdapter);


        listDayFreq = new ArrayList<String>();
        listDayFreq.add("Select Day");

        listDayFreq.add("Monday");
        listDayFreq.add("Tuesday");
        listDayFreq.add("Wednesday");
        listDayFreq.add("Thursday");
        listDayFreq.add("Friday");
        //  listDayFreq.add("Saturday");
        //  listDayFreq.add("Sunday");

        checkDayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listDayFreq);
        checkDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeekDay.setAdapter(checkDayAdapter);


        checklistFreq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    tvWeekDay.setVisibility(View.GONE);
                    llWeekDay.setVisibility(View.GONE);
                    spinnerWeekDay.setVisibility(View.GONE);
                    tvMonthDay.setVisibility(View.GONE);
                    etMonthDay.setVisibility(View.GONE);
                    tvOtherDay.setVisibility(View.GONE);

                } else if (position == 1) {

                    tvWeekDay.setVisibility(View.GONE);
                    llWeekDay.setVisibility(View.GONE);
                    spinnerWeekDay.setVisibility(View.GONE);
                    tvMonthDay.setVisibility(View.GONE);
                    etMonthDay.setVisibility(View.GONE);
                    tvOtherDay.setVisibility(View.GONE);

                } else if (position == 2) {

                    spinnerWeekDay.setVisibility(View.VISIBLE);
                    tvWeekDay.setVisibility(View.VISIBLE);
                    llWeekDay.setVisibility(View.VISIBLE);

                    tvMonthDay.setVisibility(View.GONE);
                    etMonthDay.setVisibility(View.GONE);
                    tvOtherDay.setVisibility(View.GONE);

                } else if (position == 3) {

                    spinnerWeekDay.setVisibility(View.GONE);
                    tvWeekDay.setVisibility(View.GONE);
                    llWeekDay.setVisibility(View.GONE);

                    tvMonthDay.setVisibility(View.VISIBLE);
                    etMonthDay.setVisibility(View.VISIBLE);
                    tvOtherDay.setVisibility(View.GONE);

                } else if (position == 4 || position == 5 || position == 6) {

                    spinnerWeekDay.setVisibility(View.GONE);
                    tvWeekDay.setVisibility(View.GONE);
                    llWeekDay.setVisibility(View.GONE);

                    tvMonthDay.setVisibility(View.GONE);
                    etMonthDay.setVisibility(View.GONE);
                    tvOtherDay.setVisibility(View.VISIBLE);

                    tvOtherDay.setText("Due day will be first working day after the due date. Due date will be calculated on the basis of last checked date.");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (title.equalsIgnoreCase(getResources().getString(R.string.update_checklist_item_dialog_title))) {

            okString = getResources().getString(R.string.update_bt_string);
            //  etAddChecklistName.setHint("Update checklist name");
            //   etAddChecklistDec.setHint("Update checklist description");
            etAddChecklistName.setText(response.getCheckListName());
            etAddChecklistDec.setText(response.getDescription());

            String freq = response.getFrequency();

            int pos = 0;

            for (int i = 0; i < listchecklistFreq.size(); i++) {

                if (freq.equalsIgnoreCase(listchecklistFreq.get(i))) {
                    pos = i;
                }

            }

            checklistFreq.setSelection(pos, true);

            String freqDay = response.getFrequencyDay();

            if (pos == 2) {

                int position = 0;

                for (int i = 0; i < listDayFreq.size(); i++) {

                    if (freqDay.equalsIgnoreCase(listDayFreq.get(i))) {
                        position = i;
                    }

                }

                spinnerWeekDay.setSelection(position);

            } else if (pos == 3) {

                etMonthDay.setText(freqDay);

            }

            if (response.getChecklistType() == 0) {
                spinnerChecklistType.setSelection(1, true);
            } else {
                spinnerChecklistType.setSelection(2, true);
            }

        } else {

            okString = getResources().getString(R.string.add_bt_string);

        }

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton(okString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.bt_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom_to_top;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (title.equalsIgnoreCase(getResources().getString(R.string.update_checklist_item_dialog_title))) {

                    String freqDay = "";

                    if (checklistFreq.getSelectedItemPosition() == 1) {
                        freqDay = "";
                    } else if (checklistFreq.getSelectedItemPosition() == 2) {
                        freqDay = spinnerWeekDay.getSelectedItem().toString();
                    } else if (checklistFreq.getSelectedItemPosition() == 3) {
                        freqDay = etMonthDay.getText().toString();
                    } else if (checklistFreq.getSelectedItemPosition() == 4 || checklistFreq.getSelectedItemPosition() == 5 || checklistFreq.getSelectedItemPosition() == 6) {
                        freqDay = "";
                    }

                    int checklistType = 0;

                    if (spinnerChecklistType.getSelectedItemPosition() == 1) {

                        checklistType = 0;

                    } else if (spinnerChecklistType.getSelectedItemPosition() == 2) {

                        checklistType = 1;
                    }


                    if (etAddChecklistName.getText().toString().equalsIgnoreCase(response.getCheckListName()) &&
                            etAddChecklistDec.getText().toString().equalsIgnoreCase(response.getDescription()) &&
                            checklistFreq.getSelectedItem().toString().equalsIgnoreCase(response.getFrequency()) && response.getFrequencyDay().equalsIgnoreCase(freqDay) && response.getChecklistType() == checklistType) {

                        Utilities.showSnackbar(v, getActivity().getResources().getString(R.string.not_any_change));

                    } else if (etAddChecklistName.getText().toString().equalsIgnoreCase("")) {


                        Utilities.showSnackbar(v, "Please update checklist name");

                    } else if (etAddChecklistDec.getText().toString().equalsIgnoreCase("")) {


                        Utilities.showSnackbar(v, "Please update checklist description");

                    }


                    /*else if (checklistFreq.getSelectedItem().toString().equalsIgnoreCase("select frequency")) {

                        Snackbar.make(v, "Please update checklist frequency", Snackbar.LENGTH_LONG).show();

                    }*/


                    else if (checklistFreq.getSelectedItemPosition() == 0) {

                        //       Snackbar.make(v, "Please add checklist frequency", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add checklist frequency");

                    } else if (spinnerChecklistType.getSelectedItemPosition() == 0) {

                        //Snackbar.make(v, "Please add checklist frequency", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select checklist type");

                    } else if (checklistFreq.getSelectedItemPosition() == 1) {

                        ((FragmentEquipmentCheckList) getTargetFragment()).updateChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), response, checklistFreq.getSelectedItem().toString(), freqDay, spinnerChecklistType.getSelectedItemPosition());

                    } else if (checklistFreq.getSelectedItemPosition() == 2) {

                        if (spinnerWeekDay.getSelectedItem().toString().equalsIgnoreCase("select day")) {

                            //Snackbar.make(v, "Please select week day", Snackbar.LENGTH_LONG).show();
                            Utilities.showSnackbar(v, "Please select week day");

                        } else {

                            freqDay = spinnerWeekDay.getSelectedItem().toString();

                            ((FragmentEquipmentCheckList) getTargetFragment()).updateChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), response, checklistFreq.getSelectedItem().toString(), freqDay, spinnerChecklistType.getSelectedItemPosition());

                        }

                    } else if (checklistFreq.getSelectedItemPosition() == 3) {

                        if (etMonthDay.getText().toString().equalsIgnoreCase("")) {

                            //  Snackbar.make(v, "Please add month days", Snackbar.LENGTH_LONG).show();
                            Utilities.showSnackbar(v, "Please add month days");

                        } else if (Integer.parseInt(etMonthDay.getText().toString()) < 1 || Integer.parseInt(etMonthDay.getText().toString()) > 31) {

                            // Snackbar.make(v, "Please add valid month days between 1 to 31", Snackbar.LENGTH_LONG).show();

                            Utilities.showSnackbar(v, "Please add valid month days between 1 to 31");

                        } else {

                            freqDay = etMonthDay.getText().toString();
                            ((FragmentEquipmentCheckList) getTargetFragment()).updateChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), response, checklistFreq.getSelectedItem().toString(), freqDay, spinnerChecklistType.getSelectedItemPosition());

                        }

                    } else if (checklistFreq.getSelectedItemPosition() == 4 || checklistFreq.getSelectedItemPosition() == 5 || checklistFreq.getSelectedItemPosition() == 6) {

                        ((FragmentEquipmentCheckList) getTargetFragment()).updateChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), response, checklistFreq.getSelectedItem().toString(), freqDay, spinnerChecklistType.getSelectedItemPosition());

                    }







                    /*else {

                        ((FragmentEquipmentCheckList) getTargetFragment()).updateChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), response, checklistFreq.getSelectedItem().toString());

                    }*/

                } else {

                    String freqDay = "";

                    if (etAddChecklistName.getText().toString().equalsIgnoreCase("")) {

                        //      Snackbar.make(v, "Please add checklist name", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add checklist name");

                    } else if (etAddChecklistDec.getText().toString().equalsIgnoreCase("")) {

                        //Snackbar.make(v, "Please add checklist description", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add checklist description");

                    } else if (checklistFreq.getSelectedItemPosition() == 0) {

                        //Snackbar.make(v, "Please add checklist frequency", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please add checklist frequency");

                    } else if (spinnerChecklistType.getSelectedItemPosition() == 0) {

                        //Snackbar.make(v, "Please add checklist frequency", Snackbar.LENGTH_LONG).show();

                        Utilities.showSnackbar(v, "Please select checklist type");

                    } else if (checklistFreq.getSelectedItemPosition() == 1) {

                        ((FragmentEquipmentCheckList) getTargetFragment()).addChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), checklistFreq.getSelectedItem().toString(), freqDay, spinnerChecklistType.getSelectedItemPosition());

                    } else if (checklistFreq.getSelectedItemPosition() == 2) {

                        if (spinnerWeekDay.getSelectedItem().toString().equalsIgnoreCase("select day")) {

                            //  Snackbar.make(v, "Please select week day", Snackbar.LENGTH_LONG).show();/

                            Utilities.showSnackbar(v, "Please select week day");

                        } else {

                            freqDay = spinnerWeekDay.getSelectedItem().toString();

                            ((FragmentEquipmentCheckList) getTargetFragment()).addChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), checklistFreq.getSelectedItem().toString(), freqDay, spinnerChecklistType.getSelectedItemPosition());

                        }

                    } else if (checklistFreq.getSelectedItemPosition() == 3) {

                        if (etMonthDay.getText().toString().equalsIgnoreCase("")) {

//                            Snackbar.make(v, "Please add month days", Snackbar.LENGTH_LONG).show();
                            Utilities.showSnackbar(v, "Please add month days");

                        } else if (Integer.parseInt(etMonthDay.getText().toString()) < 1 || Integer.parseInt(etMonthDay.getText().toString()) > 31) {

                            // Snackbar.make(v, "Please add valid month days between 1 to 31", Snackbar.LENGTH_LONG).show();
                            Utilities.showSnackbar(v, "Please add valid month days between 1 to 31");

                        } else {

                            freqDay = etMonthDay.getText().toString();
                            ((FragmentEquipmentCheckList) getTargetFragment()).addChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), checklistFreq.getSelectedItem().toString(), freqDay, spinnerChecklistType.getSelectedItemPosition());

                        }

                    } else if (checklistFreq.getSelectedItemPosition() == 4 || checklistFreq.getSelectedItemPosition() == 5 || checklistFreq.getSelectedItemPosition() == 6) {

                        ((FragmentEquipmentCheckList) getTargetFragment()).addChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), checklistFreq.getSelectedItem().toString(), freqDay, spinnerChecklistType.getSelectedItemPosition());

                    }


                    /*else {

                        ((FragmentEquipmentCheckList) getTargetFragment()).addChecklistItemPositiveClick(etAddChecklistName.getText().toString(), etAddChecklistDec.getText().toString(), checklistFreq.getSelectedItem().toString(), freqDay);

                    }*/

                }


            }
        });

        return dialog;
    }
}
