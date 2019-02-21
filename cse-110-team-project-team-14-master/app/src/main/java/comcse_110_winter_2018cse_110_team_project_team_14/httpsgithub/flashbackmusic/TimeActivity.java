package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;


import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeActivity extends AppCompatActivity {

    Button d_pick;
    Button t_pick;
    Calendar dateTime;
    TextView time;
    int hour, minute;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_activity);

        d_pick = (Button) findViewById(R.id.datePick);
        t_pick = (Button) findViewById(R.id.timePick);

        time = (TextView) findViewById(R.id.time);

        dateTime = Calendar.getInstance();

        hour = dateTime.get(Calendar.HOUR_OF_DAY);
        minute = dateTime.get(Calendar.MINUTE);

        year = dateTime.get(Calendar.YEAR);
        month = dateTime.get(Calendar.MONTH);
        day = dateTime.get(Calendar.DAY_OF_MONTH);

//        month = month+1;
//
//        text.setText(hour + ":" + minute);
//        text2.setText(month + "/" + day + "/" + year);


        d_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TimeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        setDay(dayOfMonth);
                        setMonth(month);
                        setYear(year);
                        updateDateTime();
                        updateTimeTracker();
                        displayTime();
                    }
                }, year, month, day);
                dateTime.set(Calendar.MINUTE, minute);
                datePickerDialog.show();
            }
        });

        t_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setHour(hourOfDay);
                        setMinute(minute);
                        updateDateTime();
                        updateTimeTracker();
                        displayTime();
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        Button normal = (Button) findViewById(R.id.normal);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeTracker.turnOnNormal();
                dateTime.setTimeInMillis(TimeTracker.getTime());
                displayTime();
            }
        });
    }

    private void updateDateTime() {
        dateTime.set(Calendar.MINUTE, minute);
        dateTime.set(Calendar.HOUR_OF_DAY, hour);
        dateTime.set(Calendar.DAY_OF_MONTH, day);
        dateTime.set(Calendar.MONTH, month);
        dateTime.set(Calendar.YEAR, year);
    }

    private void setMinute(int minute) {
        this.minute = minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    private void updateTimeTracker() {
        TimeTracker.turnOnMock();
        TimeTracker.setMockedTime(dateTime.getTimeInMillis());
    }

    private void displayTime() {
        SimpleDateFormat timeFormat = new
                SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
        time.setVisibility(View.VISIBLE);
        time.setText(timeFormat.format(dateTime.getTime()));
    }
}
