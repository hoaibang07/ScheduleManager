package husc.se.dcopen.calendarsync;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private LinearLayout navMenu;
//    private MenuItem mniAdd;
//    private MenuItem mniSyncUp;
    private SyncUpFragment syncUpFragment;

    private String m_accountName;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFunctionMenu();
        actionBar.setTitle("Calendar Sync");
        actionBar.setSubtitle("Trang chủ");

        Bundle bundle = getIntent().getBundleExtra("Account");
        m_accountName = bundle.getString("UserName");
//        boolean b = deleteDatabase(DatabaseHelper.DATABASE_NAME);
//        if(b) Log.e("Main Activity", "Da xoa data base");

        db = new DatabaseHelper(this);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_holder, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setFunctionMenu() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navMenu = (LinearLayout)findViewById(R.id.nav_menu);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        mniAdd = menu.findItem(R.id.action_add);
//        mniSyncUp = menu.findItem(R.id.action_sync_up);
//        mniAdd.setVisible(true);
//        mniSyncUp.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(navMenu)) {
                drawerLayout.closeDrawer(navMenu);
            } else {
                drawerLayout.openDrawer(navMenu);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void btnHomeOnClick(View view) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_holder, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        actionBar.setSubtitle("Trang chủ");
        drawerLayout.closeDrawer(navMenu);
    }

    public void btnSyncUpOnClick(View v) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(syncUpFragment != null) {
            syncUpFragment.onDestroy();
        }
        syncUpFragment = new SyncUpFragment();
        fragmentTransaction.replace(R.id.fragment_holder, syncUpFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        actionBar.setSubtitle("Chọn sự kiện cần đồng bộ lên");
        drawerLayout.closeDrawer(navMenu);
    }

    public void btnSyncDownOnClick(View v) {
        new SyncDownTask().execute(m_accountName);
        drawerLayout.closeDrawer(navMenu);
    }

    public void btnSyncHistoryOnClick(View v) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SyncHistoryFragment fragment = new SyncHistoryFragment();
        fragmentTransaction.replace(R.id.fragment_holder, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        actionBar.setSubtitle("Lịch sử đồng bộ");
        drawerLayout.closeDrawer(navMenu);
    }

    public void btnShowCalendarOnClick(View v) {
        long startMillis = new java.util.Date().getTime();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);
        drawerLayout.closeDrawer(navMenu);
    }

    public void btnSettingOnClick(View v) {
        SettingDialog settingDialog = new SettingDialog();
        FragmentManager fm = getSupportFragmentManager();
        settingDialog.show(fm, "Cài đặt");
        drawerLayout.closeDrawer(navMenu);
    }

    public void btnAboutOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin");

        builder.setMessage("- Tác giả: Nguyễn Dũng và cộng sự\n- Năm hoàn thành: 2015\n- Liên hệ: nguyendung622@gmail.com");
        builder.setCancelable(false);
        builder.setPositiveButton("Đóng", null);
        builder.create().show();
        drawerLayout.closeDrawer(navMenu);
    }

    public void btnLogoutOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Không", null);
        builder.create().show();
        drawerLayout.closeDrawer(navMenu);
    }

    @Override
    public void onBackPressed() {
        AlertDialog exitDialog = createAlertDialog();
        exitDialog.show();
    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thoát");
        builder.setMessage("Bạn có thực sự muốn thoát?");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        builder.setNegativeButton("Không", null);

        return builder.create();
    }

    private class SyncDownTask extends AsyncTask<String, Integer, ArrayList<History>> {
        private ProgressDialog dlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dlg = new ProgressDialog(MainActivity.this);
            dlg.setMessage("Đang đồng bộ dữ liệu xuống...");
            dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dlg.setIndeterminate(false);
            dlg.setMax(100);
            dlg.setProgress(0);
            dlg.show();
        }

        @Override
        protected ArrayList<History> doInBackground(String... params) { //params : userName
            String userName = params[0];
            ArrayList<Task> listTask = null;
            ArrayList<History> listHistory = new ArrayList<History>();

            try {
                listTask = JSONParser.syncDown(userName, new Settings(MainActivity.this).getNumberDateSyncDown());
                publishProgress(30);
                CalendarSupport calendarSupport = new CalendarSupport(MainActivity.this);
                for(int i = 0; i < listTask.size(); i++) {
                    Task task = listTask.get(i);
                    if(db.checkExistTask(task.getId())) {
                        db.updateTask(task);
                        calendarSupport.editToCalendar(task);
                    } else {
                        long row = db.insertTask(task);
                        if(row > 0) {
                            calendarSupport.insertToCalendar(task);
                        }
                    }

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
                    listHistory.add(history);
                    db.insertHistory(history);

                    if((i* 100/listTask.size() + 30) <= 100) {
                        publishProgress(i* 100/listTask.size() + 30);
                    } else {
                        publishProgress(100);
                    }
                    if(isCancelled()) break;
                }
                publishProgress(100);
                return listHistory;
            } catch (IOException e) {
                Log.e("IOException ", e.getMessage());
            } catch (JSONException e) {
                Log.e("JSONException ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dlg.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<History> listHistory) {
            dlg.dismiss();
            Toast.makeText(MainActivity.this, "Đã đồng bộ xong", Toast.LENGTH_SHORT).show();

            ListSend.listHistories = listHistory;

            android.app.FragmentManager fragmentManager = getFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            NewHisFragment fragment = new NewHisFragment();

            fragmentTransaction.replace(R.id.fragment_holder, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}

