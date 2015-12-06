package husc.se.dcopen.calendarsync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    private ListView listView;
    private TextView tv;
    private BroadcastReceiver updateReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                java.util.Date date1 = cal.getTime();

                cal.add(Calendar.DAY_OF_MONTH, 1);
                java.util.Date date2 = cal.getTime();

                ArrayList<Task> tasks = new CalendarSupport(getActivity()).getEventFromCalendar(date1, date2);
                TaskArrayAdapter taskArrayAdapter = new TaskArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, tasks);
                listView.setAdapter(taskArrayAdapter);

                if(tasks != null && tasks.size() >0) {
                    tv.setText("Các công việc trong ngày hôm nay");
                } else {
                    tv.setText("Không có công việc nào trong ngày hôm nay");
                }
            }
        };
        getActivity().registerReceiver(updateReceiver, new IntentFilter("AddTask"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView)view.findViewById(R.id.lv_cong_viec_trong_ngay);
        tv = (TextView)view.findViewById(R.id.tv_cong_viec_trong_ngay);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.util.Date date1 = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        java.util.Date date2 = cal.getTime();

        ArrayList<Task> tasks = new CalendarSupport(getActivity()).getEventFromCalendar(date1, date2);
        TaskArrayAdapter taskArrayAdapter = new TaskArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, tasks);
        listView.setAdapter(taskArrayAdapter);

        if(tasks != null && tasks.size() >0) {
            tv.setText("Các công việc trong hôm nay");
        } else {
            tv.setText("Không có công việc nào trong hôm nay");
        }

        return view;
    }

    class TaskArrayAdapter extends ArrayAdapter<Task> {
        private List<Task> listTask;
        private LayoutInflater inflater;

        public TaskArrayAdapter(Context context, int resource, List<Task> objects) {
            super(context, resource, objects);
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.listTask = objects;
        }

        @Override
        public int getCount() {
            return listTask.size();
        }

        @Override
        public Task getItem(int position) {
            return listTask.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Task task = listTask.get(position);

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_task_date, parent, false);
            }

            final TextView tvTaskName = (TextView)convertView.findViewById(R.id.tv_task_title_date);
            final TextView tvTaskContent = (TextView)convertView.findViewById(R.id.tv_task_content_date);
            final TextView tvTaskPlace = (TextView)convertView.findViewById(R.id.tv_task_place_date);

            tvTaskName.setText(task.getTaskName() + "("+convertToStringTime(task.getBeginTime())+" - "+
                    convertToStringTime(task.getEndTime()) + ")");
            tvTaskContent.setText("Nội dung công việc: " + task.getTaskContent());
            tvTaskPlace.setText("Địa điểm: " + task.getPlace());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String message = "Tên công việc: "+task.getTaskName() + "\n" +
                            "Nội dung công việc: " + task.getTaskContent() + "\n" +
                            "Thời gian: " + convertDateToString(task.getBeginTime(), "dd/MM/yyyy hh:mm aa") + " - " +
                            convertDateToString(task.getEndTime(), "dd/MM/yyyy hh:mm aa") + "\n" +
                            "Địa điểm: " + task.getPlace();

                    createAlertDialog("Thông tin chi tiết", message, false).show();
                }
            });

            return convertView;
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(updateReceiver);
        super.onDestroy();
    }

    private AlertDialog createAlertDialog(String title, String message, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton("Đóng", null);

        return builder.create();
    }

    private String convertToStringTime(java.util.Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        return simpleDateFormat.format(date);
    }

    private String convertDateToString(java.util.Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }
}
