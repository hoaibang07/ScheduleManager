package husc.se.dcopen.calendarsync;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyncUpFragment extends Fragment {
    private CheckBox ckbSelectAll;
    private String accountName;
    private String password;
    private ListView listView;
    private List<Task> listTask;
    private ArrayList<TaskItem> listTaskItem;
    private BroadcastReceiver updateReceiver;
    private TaskArrayAdapter taskArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Settings settings = new Settings(getActivity());
        accountName = settings.getUserName();
        password = settings.getPassword();

        updateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                DatabaseHelper db = new DatabaseHelper(getActivity());

                int soNgaySyncUp = settings.getNumberDateSyncUp();

                Calendar cal = Calendar.getInstance();
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                java.util.Date date1 = cal.getTime();
                cal.add(Calendar.DAY_OF_YEAR, soNgaySyncUp);
                java.util.Date date2 = cal.getTime();

                if(soNgaySyncUp > 0) {
                    listTask = new CalendarSupport(getActivity()).getEventFromCalendar(date1, date2);
                } else {
                    listTask = new CalendarSupport(getActivity()).getEventFromCalendar(date2, date1);
                }

                listTaskItem = new ArrayList<>();
                int check;
                for(int i = 0; i < listTask.size(); i++) {
                    Task tsk = listTask.get(i);
                    check = db.checkPersonalTask(tsk);
                    if(check == 1) {
                        tsk.setAccountName(accountName);
                        listTaskItem.add(new TaskItem(tsk, false));
                    } else if(check == -1) {
                        tsk.setAccountName(accountName);
                        listTaskItem.add(new TaskItem(tsk, false));
                        db.insertTask(tsk);
                    }
                }
                taskArrayAdapter = new TaskArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listTaskItem);
                listView.setAdapter(taskArrayAdapter);
            }
        };

        getActivity().registerReceiver(updateReceiver, new IntentFilter("Setting"));
//        getActivity().registerReceiver(syncUpReceiver, new IntentFilter("SyncUp"));
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(updateReceiver);
//        getActivity().unregisterReceiver(syncUpReceiver);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_up, container, false);
        listView = (ListView)view.findViewById(R.id.list_task);
        view.findViewById(R.id.btn_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Integer, Integer>() {

                    private ProgressDialog dlg;
                    @Override
                    protected void onPreExecute() {
                        dlg = new ProgressDialog(getActivity());
                        dlg.setMessage("Đang đồng bộ dữ liệu lên...");
                        dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dlg.setIndeterminate(false);
                        dlg.setMax(100);
                        dlg.setProgress(0);
                        dlg.show();
                    }

                    @Override
                    protected Integer doInBackground(Void... params) {
                        int dem = 0;
                        DatabaseHelper db = new DatabaseHelper(getActivity());
                        for(int i = 0; i < listTaskItem.size(); i++) {
                            if(listTaskItem.get(i).isChecked()) {
                                try {
                                    Task task = listTaskItem.get(i).getTask();
                                    if(JSONParser.syncUp(accountName, password, listTaskItem.get(i).getTask())) {
                                        java.util.Date date = new java.util.Date();
                                        History history = new History();
                                        history.setNgayDongBo(date);
                                        history.setId("");
                                        history.setHisBTime(task.getBeginTime());
                                        history.setHisETime(task.getEndTime());
                                        history.setHisContent(task.getTaskContent());
                                        history.setHisName(task.getTaskName());
                                        history.setHisPlace(task.getPlace());
                                        history.setHisType(task.getType());
                                        db.insertHistory(history);
                                        task.setSync(1);
                                        db.updateTask(task);
                                        dem++;
                                    }
                                } catch (IOException e) {
                                    Log.e("IOEXCEPTION", e.getMessage());
                                } catch (JSONException e) {
                                    Log.e("JSONEXCEPTION", e.getMessage());
                                }
                            }
                            publishProgress(i*100/listTaskItem.size());
                            if(isCancelled()) break;
                        }
                        publishProgress(100);
                        return Integer.valueOf(dem);
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                        dlg.setProgress(values[0]);
                    }

                    @Override
                    protected void onPostExecute(Integer dem) {
                        dlg.dismiss();
                        Toast.makeText(getActivity(), "Đã đồng bộ được " + dem + " công việc", Toast.LENGTH_SHORT).show();
                        taskArrayAdapter.notifyDataSetChanged();
                    }
                }.execute();
            }
        });

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

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        java.util.Date date1 = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, soNgaySyncUp);
        java.util.Date date2 = cal.getTime();


        if(soNgaySyncUp > 0) {
            listTask = new CalendarSupport(getActivity()).getEventFromCalendar(date1, date2);
        } else {
            listTask = new CalendarSupport(getActivity()).getEventFromCalendar(date2, date1);
        }

        listTaskItem = new ArrayList<>();
        int check;
        for(int i = 0; i < listTask.size(); i++) {
            Task tsk = listTask.get(i);
            check = db.checkPersonalTask(tsk);
            if(check == 1) {
                tsk.setAccountName(accountName);
                listTaskItem.add(new TaskItem(tsk, false));
            } else if(check == -1) {
                tsk.setAccountName(accountName);
                listTaskItem.add(new TaskItem(tsk, false));
                db.insertTask(tsk);
            }
        }
        taskArrayAdapter = new TaskArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listTaskItem);
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
            final TaskItem taskItem = listTask.get(position);

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_task, parent, false);
            }

            final LinearLayout linear = (LinearLayout)convertView.findViewById(R.id.linear);
            final TextView txtTaskName = (TextView)convertView.findViewById(R.id.item_task_name);
            final TextView txtTaskPlace = (TextView)convertView.findViewById(R.id.item_task_place);
            final TextView txtTaskBeginTime = (TextView)convertView.findViewById(R.id.item_task_begin_time);
            final TextView txtTaskEndTime = (TextView)convertView.findViewById(R.id.item_task_end_time);
            final CheckBox ckbChecked = (CheckBox)convertView.findViewById(R.id.ckb_choose_task);

            txtTaskName.setText(taskItem.getTask().getTaskName());
            txtTaskPlace.setText(taskItem.getTask().getPlace());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            txtTaskBeginTime.setText(dateFormat.format(taskItem.getTask().getBeginTime()));

            if(txtTaskEndTime.getText() != null) {
                txtTaskEndTime.setText(dateFormat.format(taskItem.getTask().getEndTime()));
            } else {
                txtTaskEndTime.setText(dateFormat.format(taskItem.getTask().getBeginTime()));
            }

            ckbChecked.setChecked(taskItem.isChecked());

            if (taskItem.getTask().getSync() == 1) {
                linear.setBackgroundColor(new Settings(getActivity()).getSyncColor());
            } else {
                linear.setBackgroundColor(new Settings(getActivity()).getNoSyncColor());
            }

            ckbChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    taskItem.setChecked(isChecked);
                }
            });
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
