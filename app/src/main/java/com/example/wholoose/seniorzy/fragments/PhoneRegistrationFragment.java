package com.example.wholoose.seniorzy.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.wholoose.seniorzy.R;
import com.example.wholoose.seniorzy.RegisterActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PhoneRegistrationFragment extends Fragment {

    EditText firstname_edittext, lastname_edittext, phone_edittext, edittext;
    Button register_btn;
    RadioGroup rdb_senior_carer;
    RadioButton rdb_role;
    final Calendar myCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_register_phone,container,false);
        setup(view);
        setupDatePicker(view);

        register_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerByPhoneNumber(view);
            }
        });

        return view;
    }

    public void registerByPhoneNumber(View view){
        final String firstname_string = firstname_edittext.getText().toString();
        final String lastname_string = lastname_edittext.getText().toString();
        final String email_string = phone_edittext.getText().toString();

        int selectedId = rdb_senior_carer.getCheckedRadioButtonId();
        rdb_role = (RadioButton) view.findViewById(selectedId);
        final String role_string = rdb_role.getText().toString();

        if(TextUtils.isEmpty(firstname_string)){
            Toast.makeText(((RegisterActivity)getActivity()), "Please enter Firstname", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(lastname_string)){
            Toast.makeText(((RegisterActivity)getActivity()), "Please enter Lastname", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email_string)){
            Toast.makeText(((RegisterActivity)getActivity()), "Please enter correct phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        ((RegisterActivity)getActivity()).createUserWithPhoneNumber(email_string, firstname_string, lastname_string, role_string, myCalendar);
    }

    public void setup(View view){
        firstname_edittext = (EditText) view.findViewById(R.id.input_firstname);
        lastname_edittext = (EditText) view.findViewById(R.id.input_lastname);
        phone_edittext = (EditText) view.findViewById(R.id.input_phone);
        phone_edittext.setHint("+"+getCountryDialCode(view));
        register_btn = (Button) view.findViewById(R.id.b_register);
        rdb_senior_carer = (RadioGroup) view.findViewById(R.id.rdb_senior_carer);
    }

    private void setupDatePicker(View view) {
        edittext= (EditText) view.findViewById(R.id.input_callendar);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(((RegisterActivity)getActivity()), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    public static String getCountryDialCode(View view){
        String contryId = null;
        String contryDialCode = null;

        TelephonyManager telephonyMngr = (TelephonyManager) view.getContext().getSystemService(Context.TELEPHONY_SERVICE);

        contryId = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrContryCode=view.getContext().getResources().getStringArray(R.array.DialingCountryCode);
        for(int i=0; i<arrContryCode.length; i++){
            String[] arrDial = arrContryCode[i].split(",");
            if(arrDial[1].trim().equals(contryId.trim())){
                contryDialCode = arrDial[0];
                break;
            }
        }
        return contryDialCode;
    }
}
