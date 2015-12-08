package husc.se.dcopen.calendarsync;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.Date;

public class AddTaskDialog extends Dialog {

    private EditText txtTaskName;
    private TextView txtBeginDate;
    private TextView txtBeginTime;
    private TextView txtEndDate;
    private TextView txtEndTime;
    private EditText txtPlace;
    private EditText txtTaskContent;

    private java.util.Date date;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateTimeFormat;

    private DatabaseHelper db;

    public AddTaskDialog(Context context) {
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
        final Button btnSave = (Button)findViewById(R.id.btn_save);
        final Button btnCancel = (Button)findViewById(R.id.btn_cancel);

        db = new DatabaseHelper(getContext());

        Calendar cal = Calendar.getInstance();
        date = cal.getTime();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timeFormat = new SimpleDateFormat("hh:mm aa");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");
        txtBeginDate.setText(dateFormat.format(date));
        txtBeginTime.setText(timeFormat.format(date));
        cal.add(Calendar.HOUR_OF_DAY, 1);
        date = cal.getTime();
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
                    task.setId(id)
                            .setTaskName(taskName)
                            .setPlace(place)
                            .setTaskContent(taskContent)
                            .setType(0)
                            .setSync(0)
                            .setBeginTime(btime)
                            .setEndTime(etime);

                    long row = db.insertTask(task);

                    if (row > 0) {
                        //add event to calendar
                        new CalendarSupport(getContext()).insertToCalendar(task);
                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent("AddTask");
                        intent.putExtra("add", true);
                        getContext().sendBroadcast(intent);

                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        txtTaskName.requestFocus();
                    }
                } catch (ParseException e) {
                    Log.e("AddTaskDialog: ", e.getMessage());
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

                if(aa.compareToIgnoreCase("AM") == 0 || aa.compareToIgnoreCase("SA") == 0) {
                    if(hourOfDay == 12) hourOfDay = 0;
                } else if(aa.compareToIgnoreCase("PM") == 0 || aa.compareToIgnoreCase("CH") == 0){
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

    private DatePickerDialog.OnDateSetListener beginDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            try {
                Date date = dateFormat.parse(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                txtBeginDate.setText(dateFormat.format(date));
            } catch (ParseException e) {
                Log.e("BeginDateListener", e.getMessage());
            }
        }
    };

    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            try {
                Date date = dateFormat.parse(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                txtEndDate.setText(dateFormat.format(date));
            } catch (ParseException e) {
                Log.e("EndDateListener", e.getMessage());
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
                Log.e("BeginTimeListener", e.getMessage());
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
                Log.e("EndTimeListener", e.getMessage());
            }
        }
    };
}