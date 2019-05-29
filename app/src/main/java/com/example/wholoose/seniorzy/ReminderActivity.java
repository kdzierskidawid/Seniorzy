package com.example.wholoose.seniorzy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;

    Switch simpleSwitch1;
    Switch simpleSwitch2;
    Switch simpleSwitch3;
    Switch simpleSwitch4;

    EditText eText1;
    EditText eText2;
    EditText eText3;
    EditText eText4;
    private static int mTextViewCurr=0;
    private static int cancelAlarm=0;
    protected static String content="default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        mTextView1= findViewById(R.id.textView1);
        mTextView2= findViewById(R.id.textView2);
        mTextView3= findViewById(R.id.textView3);
        mTextView4= findViewById(R.id.textView4);

        eText1=(EditText)findViewById(R.id.editText1);
        eText2=(EditText)findViewById(R.id.editText2);
        eText3=(EditText)findViewById(R.id.editText3);
        eText4=(EditText)findViewById(R.id.editText4);


        // initiate a Switch
        simpleSwitch1= (Switch) findViewById(R.id.switch1_timepicker);
        simpleSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mTextViewCurr=1;
                    content=eText1.getText().toString();
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
//                    timePicker.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialogInterface) {
//                            simpleSwitch1.setChecked(false);
//                        }
//                    });
                }
                else
                {
                    cancelAlarm=1;
                    cancelAlarm();
                }
            }
        });
        //switch 2
        simpleSwitch2= (Switch) findViewById(R.id.switch2_timepicker);
        simpleSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mTextViewCurr=2;
                    content=eText2.getText().toString();
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
                else
                {
                    cancelAlarm=2;
                    cancelAlarm();
                }
            }
        });
        //switch 3
        simpleSwitch3= (Switch) findViewById(R.id.switch3_timepicker);
        simpleSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mTextViewCurr=3;
                    content=eText3.getText().toString();
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
                else
                {
                    cancelAlarm=3;
                    cancelAlarm();
                }
            }
        });
        //switch 4
        simpleSwitch4= (Switch) findViewById(R.id.switch4_timepicker);
        simpleSwitch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    mTextViewCurr=4;
                    content=eText4.getText().toString();
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
                else
                {
                    cancelAlarm=4;
                    cancelAlarm();
                }
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        updateTimeText(c);
        startAlarm(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Time: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        if(mTextViewCurr==1)
            mTextView1.setText(timeText);
        else if(mTextViewCurr==2)
            mTextView2.setText(timeText);
        else if(mTextViewCurr==3)
            mTextView3.setText(timeText);
        else if(mTextViewCurr==4)
            mTextView4.setText(timeText);

    }

    private void startAlarm(Calendar c) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("name",content);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mTextViewCurr, intent, PendingIntent.FLAG_ONE_SHOT);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, mTextViewCurr, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

        if(cancelAlarm==1)
            mTextView1.setText("No alarm");
        else if(cancelAlarm==2)
            mTextView2.setText("No alarm");
        else if(cancelAlarm==3)
            mTextView3.setText("No alarm");
        else if(cancelAlarm==4)
            mTextView4.setText("No alarm");
    }
}