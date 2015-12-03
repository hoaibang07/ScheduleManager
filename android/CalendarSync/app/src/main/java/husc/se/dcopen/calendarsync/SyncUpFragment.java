package husc.se.dcopen.calendarsync;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyncUpFragment extends Fragment {
    private CheckBox ckbSelectAll;
    private String accountName;
    private ListView listView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_up, container, false);
        listView = (ListView)view.findViewById(R.id.list_task);

        ckbSelectAll = (CheckBox)view.findViewById(R.id.ckb_select_all);
        ckbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    TaskArrayAdapter taskArr = (TaskArrayAdapter)listView.getAdapter();
                    int count = taskArr.getCount();
                    for(int i = 0; i < count; i++) {
                        taskArr.getItem(i).setChecked(true);
                    }
                    taskArr.notifyDataSetChanged();
                } else {
                    TaskArrayAdapter taskArr = (TaskArrayAdapter)listView.getAdapter();
                    int count = taskArr.getCount();
                    for(int i = 0; i < count; i++) {
                        taskArr.getItem(i).setChecked(false);
                    }
                    taskArr.notifyDataSetChanged();
                }
            }
        });


        DatabaseHelper db = new DatabaseHelper(getActivity());
        Settings settings = new Settings(getActivity());
        int soNgaySyncUp = settings.getNumberDateSyncUp();
        accountName = settings.getUserName();

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        java.util.Date date1 = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, soNgaySyncUp);
        java.util.Date date2 = cal.getTime();

        List<Task> listTask;
        if(soNgaySyncUp > 0) {
            listTask = new CalendarSupport(getActivity()).getEventFromCalendar(date1, date2);
        } else {
            listTask = new CalendarSupport(getActivity()).getEventFromCalendar(date2, date1);
        }

        ArrayList<TaskItem> listTaskItem = new ArrayList<>();
        int check;
        for(int i = 0; i < listTask.size(); i++) {
            Task tsk = listTask.get(i);
            check = db.checkPersonalTask(tsk);
            Log.e(tsk.getTaskName(), check + "");
            if(check == 1) {
                tsk.setAccountName(accountName);
                listTaskItem.add(new TaskItem(tsk, false));
            } else if(check == -1) {
                tsk.setAccountName(accountName);
                listTaskItem.add(new TaskItem(tsk, false));
                db.insertTask(tsk);
            }
        }
        TaskArrayAdapter taskArrayAdapter = new TaskArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listTaskItem);
        listView.setAdapter(taskArrayAdapter);

        return view;
    }

    class TaskArrayAdapter extends ArrayAdapter<TaskItem> {
        private List<TaskItem> listTask;
        private LayoutInflater inflater;

        public TaskArrayAdapter(Context context, int resource, List<TaskItem> objects) {
            super(context, resource, objects);
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.listTask = objects;
        }

        @Override
        public int getCount() {
            return listTask.size();
        }

        @Override
        public TaskItem getItem(int position) {
            return listTask.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskItem taskItem = listTask.get(position);

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_task, parent, false);
            }

            final TextView txtTaskName = (TextView)convertView.findViewById(R.id.item_task_name);
            final TextView txtTaskPlace = (TextView)convertView.findViewById(R.id.item_task_place);
            final TextView txtTaskBeginTime = (TextView)convertView.findViewById(R.id.item_task_begin_time);
            final TextView txtTaskEndTime = (TextView)convertView.findViewById(R.id.item_task_end_time);
            final CheckBox ckbChecked = (CheckBox)convertView.findViewById(R.id.ckb_choose_task);
            final TableLayout table = (TableLayout)convertView.findViewById(R.id.table);

            txtTaskName.setText(taskItem.getTask().getTaskName());
            txtTaskPlace.setText(taskItem.getTask().getPlace());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            txtTaskBeginTime.setText(dateFormat.format(taskItem.getTask().getBeginTime()));
//            txtTaskEndTime.setText(dateFormat.format(taskItem.getTask().getEndTime()));
            ckbChecked.setChecked(taskItem.isChecked());

            if(taskItem.getTask().getSync() == 1) {
                table.setBackgroundColor(new Settings(getActivity()).getSyncColor());
            } else {
                table.setBackgroundColor(new Settings(getActivity()).getNoSyncColor());
            }
            return convertView;
        }
    }

    class TaskItem {
        private Task task;
        private boolean checked;

        public TaskItem() {}

        public TaskItem(Task task, boolean checked) {
            this.task = task;
            this.checked = checked;
        }

        public Task getTask() {
            return task;
        }

        public TaskItem setTask(Task task) {
            this.task = task;
            return this;
        }

        public boolean isChecked() {
            return checked;
        }

        public TaskItem setChecked(boolean checked) {
            this.checked = checked;
            return this;
        }
    }
}
