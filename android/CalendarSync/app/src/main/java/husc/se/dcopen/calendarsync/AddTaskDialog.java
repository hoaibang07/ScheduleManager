package husc.se.dcopen.calendarsync;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AddTaskDialog extends Dialog {

    private EditText txtTaskName;
    private TextView txtBeginDate;
    private TextView txtBeginTime;
    private TextView txtEndDate;
    private TextView txtEndTime;
    private EditText txtPlace;
    private EditText txtTaskContent;
    private Button btnSave;
    private Button btnCancel;

    private java.util.Date date;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateTimeFormat;
    private String m_accountName;
    private DatabaseHelper db;

    public AddTaskDialog(Context context, String accountName) {
        super(context);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        setContentView(R.layout.dialog_add_task);

        txtTaskName = (EditText)findViewById(R.id.txt_task_name);
        txtBeginDate = (TextView)findViewById(R.id.txt_begin_date);
        txtBeginTime = (TextView)findViewById(R.id.txt_begin_time);
        txtEndDate = (TextView)findViewById(R.id.txt_end_day);
        txtEndTime = (TextView)findViewById(R.id.txt_end_time);
        txtPlace = (EditText)findViewById(R.id.txt_place);
        txtTaskContent = (EditText)findViewById(R.id.txt_task_content);
        btnSave = (Button)findViewById(R.id.btn_save);
        btnCancel = (Button)findViewById(R.id.btn_cancel);

        m_accountName = accountName;
        db = new DatabaseHelper(getContext());

        date = new java.util.Date();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("hh:mm aa");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");
        txtBeginDate.setText(dateFormat.format(date));
        txtBeginTime.setText(timeFormat.format(date));
        txtEndDate.setText(dateFormat.format(date));
        txtEndTime.setText(timeFormat.format(date));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String id = new java.util.Date().getTime() + "";
                    String taskName = txtTaskName.getText().toString();
                    String place = txtPlace.getText().toString();
                    String taskContent = txtTaskContent.getText().toString();
                    java.util.Date btime = dateTimeFormat.parse(txtBeginDate.getText().toString() +
                            " - " + txtBeginTime.getText().toString());
                    java.util.Date etime = dateTimeFormat.parse(txtEndDate.getText().toString() +
                            " - " + txtEndTime.getText().toString());

                    Task task = new Task();
                    task.setId(new java.util.Date().getTime() + "")
                            .setTaskName(txtTaskName.getText().toString())
                            .setPlace(txtPlace.getText().toString())
                            .setTaskContent(txtTaskContent.getText().toString())
                            .setType(0)
                            .setAccountName(m_accountName)
                            .setSync(0);
                    task.setBeginTime(new java.sql.Date(btime.getTime()))
                            .setEndTime(new java.sql.Date(etime.getTime()));

                    long row = db.insertTask(task);

                    if (row > 0) {
                        //add event to calendar
                        insertToCalendar(id, taskName, taskContent, place, btime, etime);
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                        txtTaskName.requestFocus();
                    }
                } catch (ParseException e) {
                    Log.e("Add task: ", e.getMessage());
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txtBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strBeginDate = txtBeginDate.getText().toString();
                int day = Integer.parseInt(strBeginDate.substring(0, 2));
                int month = Integer.parseInt(strBeginDate.substring(3, 5));
                int year = Integer.parseInt(strBeginDate.substring(6, 10));

                Toast.makeText(getContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        beginDateSetListener, year, month-1, day);
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.show();
            }
        });

        txtBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beginTime = txtBeginTime.getText().toString();
                int hourOfDay = Integer.parseInt(beginTime.substring(0, 2));
                int minute = Integer.parseInt(beginTime.substring(3, 5));
                String aa = beginTime.substring(6);

                if(aa.compareToIgnoreCase("AM") == 0) {
                    if(hourOfDay == 12) hourOfDay = 0;
                } else if(aa.compareToIgnoreCase("PM") == 0){
                    if(hourOfDay != 12) hourOfDay += 12;
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        beginTimeSetListener, hourOfDay, minute, false);
                timePickerDialog.show();
            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEndDate = txtEndDate.getText().toString();
                int day = Integer.parseInt(strEndDate.substring(0, 2));
                int month = Integer.parseInt(strEndDate.substring(3, 5));
                int year = Integer.parseInt(strEndDate.substring(6, 10));

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        endDateSetListener, year, month-1, day);
                datePickerDialog.getDatePicker().setCalendarViewShown(false);
                datePickerDialog.show();
            }
        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEndTime = txtEndTime.getText().toString();
                int hourOfDay = Integer.parseInt(strEndTime.substring(0, 2));
                int minute = Integer.parseInt(strEndTime.substring(3, 5));
                String aa = strEndTime.substring(6);

                if(aa.compareToIgnoreCase("AM") == 0) {
                    if(hourOfDay == 12) hourOfDay = 0;
                } else if(aa.compareToIgnoreCase("PM") == 0){
                    if(hourOfDay != 12) hourOfDay += 12;
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        endTimeSetListener, hourOfDay, minute, false);
                timePickerDialog.show();
            }
        });
    }

    public void insertToCalendar(String id, String taskName, String taskContent, String place,
                                 java.util.Date btime, java.util.Date etime) {
        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 1);
        event.put(CalendarContract.Events._ID, Long.parseLong(id));
        event.put(CalendarContract.Events.TITLE, taskName);
        event.put(CalendarContract.Events.DESCRIPTION, taskContent);
        event.put(CalendarContract.Events.EVENT_LOCATION, place);
        event.put(CalendarContract.Events.DTSTART, btime.getTime());
        event.put(CalendarContract.Events.DTEND, etime.getTime());
        event.put(CalendarContract.Events.ALL_DAY, 0);
        event.put(CalendarContract.Events.HAS_ALARM, 1);
        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

        Uri baseUri;
        if (Build.VERSION.SDK_INT >= 8) {
            baseUri = Uri.parse("content://com.android.calendar/events");
        } else {
            baseUri = Uri.parse("content://calendar/events");
        }
        getContext().getContentResolver().insert(baseUri, event);
    }

    private DatePickerDialog.OnDateSetListener beginDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = timeFormat.parse(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                txtBeginDate.setText(timeFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = timeFormat.parse(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                txtEndDate.setText(timeFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener beginTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SimpleDateFormat timeFormat1 = new SimpleDateFormat("HH:mm");
            SimpleDateFormat timeFormat2 = new SimpleDateFormat("hh:mm aa");

            try {
                java.util.Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                txtBeginTime.setText(timeFormat2.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SimpleDateFormat timeFormat1 = new SimpleDateFormat("HH:mm");
            SimpleDateFormat timeFormat2 = new SimpleDateFormat("hh:mm aa");

            try {
                java.util.Date date = timeFormat1.parse(hourOfDay + ":" + minute);
                txtEndTime.setText(timeFormat2.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
}