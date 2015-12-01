package husc.se.dcopen.calendarsync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "MyPlan.db";
    public static final int DATA_VERSION = 1;

    //table Task
    public static final String TABLE_TASK = "Task";
    public static final String KEY_TASK_ID = "TaskID";
    public static final String KEY_TASK_NAME = "TaskName";
    public static final String KEY_BEGIN_TIME = "BeginTime";
    public static final String KEY_END_TIME = "EndTime";
    public static final String KEY_PLACE = "Place";
    public static final String KEY_TASK_CONTENT = "TaskContent";
    public static final String KEY_TYPE = "Type";
    public static final String KEY_ACCOUNT_NAME = "AccountName";
    public static final String KEY_SYNC = "Sync";
    public static final String CREATE_TABLE_TASK = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + " ("+
            KEY_TASK_ID +" TEXT PRIMARY KEY, "+
            KEY_TASK_NAME + " TEXT, "+
            KEY_BEGIN_TIME + " TEXT, "+
            KEY_END_TIME + " TEXT, "+
            KEY_PLACE + " TEXT, " +
            KEY_TASK_CONTENT + " TEXT, "+
            KEY_TYPE + " INTEGER, "+
            KEY_ACCOUNT_NAME + " TEXT, "+
            KEY_SYNC + " INTEGER)";

    //table History
    public static final String TABLE_HISTORY = "History";
    public static final String KEY_HISTORY_ID = "HistoryID";
    public static final String KEY_HISTORY_NGAY_DONG_BO = "NgayDongBo";
    public static final String FK_TASK_ID = "TaskID";
    public static final String CREATE_TABLE_HISTORY = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " (" +
            KEY_HISTORY_ID + " INTEGER AUTOINCREMENT, " +
            KEY_HISTORY_NGAY_DONG_BO + " TEXT, " +
            FK_TASK_ID + " TEXT)";

    public static final String GET_HISTORY_SYNC_UP = "select "+KEY_HISTORY_NGAY_DONG_BO+", "+KEY_TASK_NAME+" " +
            "from Task inner join History on Task.TaskID = History.TaskID " +
            "where Type = '0' " +
            "order by HistoryID desc";
    public static final String GET_HISTORY_SYNC_DOWN = "select "+KEY_HISTORY_NGAY_DONG_BO+", "+KEY_TASK_NAME+" " +
            "from Task inner join History on Task.TaskID = History.TaskID " +
            "where Type = '1' " +
            "order by HistoryID desc";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_TASK);
            db.execSQL(CREATE_TABLE_HISTORY);
        } catch (SQLException ex) {
            Log.e("#Database", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void open() {
        try {
            db = getWritableDatabase();
        } catch (Exception ex) {
            Log.e("#Open database", ex.getMessage());
        }
    }

    public void close() {
        if(db != null && db.isOpen()) {
            try {
                db.close();
            } catch(SQLException ex) {
                Log.e("#Close database", ex.getMessage());
            }
        }
    }

    //common method
    private Cursor getAll(String sql) {
        open();
        return db.rawQuery(sql, null);
    }

    private long insert(String table, ContentValues values) {
        open();
        long index = db.insert(table, null, values);
        close();
        return index;
    }

    private boolean update(String table, ContentValues values, String where) {
        open();
        long index = db.update(table, values, where, null);
        close();
        return index > 0;
    }

    private boolean delete(String table, String where) {
        open();
        long index = db.delete(table, where, null);
        close();
        return index > 0;
    }

    //task db method
    public boolean checkExistTask(String taskID) {
        Cursor cursor = getAll("select TaskID from Task where TaskID = '" +taskID+"'");
        if(cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Task> getListTask(String sql) {
        ArrayList<Task> list = new ArrayList<>();
        Cursor cursor = getAll(sql);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                list.add(cursorToTask(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public long insertTask(Task task) {
        return insert(TABLE_TASK, taskToValues(task));
    }

    public boolean updateTask(Task task) {
        return update(TABLE_TASK, taskToValues(task), KEY_TASK_ID + " = " + task.getId());
    }

    public boolean deleteTask(String where) {
        return delete(TABLE_TASK, where);
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getString(cursor.getColumnIndex(KEY_TASK_ID)))
                .setTaskName(cursor.getString(cursor.getColumnIndex(KEY_TASK_NAME)))
                .setPlace(cursor.getString(cursor.getColumnIndex(KEY_PLACE)))
                .setTaskContent(cursor.getString(cursor.getColumnIndex(KEY_TASK_CONTENT)))
                .setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)))
                .setAccountName(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NAME)))
                .setSync(cursor.getInt(cursor.getColumnIndex(KEY_SYNC)));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");
        String strBeginTime = cursor.getString(cursor.getColumnIndex(KEY_BEGIN_TIME));
        String strEndTime = cursor.getString(cursor.getColumnIndex(KEY_END_TIME));
        try {
            java.util.Date utilBeginTime = dateFormat.parse(strBeginTime);
            java.util.Date utilEndTime = dateFormat.parse(strEndTime);

            task.setBeginTime(new java.sql.Date(utilBeginTime.getTime()))
                    .setEndTime(new java.sql.Date(utilEndTime.getTime()));
        } catch (ParseException e) {
            Log.e("#Date parse", e.getMessage());
        }
        return task;
    }

    private ContentValues taskToValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, task.getId());
        values.put(KEY_TASK_NAME, task.getTaskName());
        values.put(KEY_PLACE, task.getPlace());
        values.put(KEY_TASK_CONTENT, task.getTaskContent());
        values.put(KEY_TYPE, task.getType());
        values.put(KEY_ACCOUNT_NAME, task.getAccountName());
        values.put(KEY_SYNC, task.getSync());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");
        String strBeginTime = dateFormat.format(task.getBeginTime());
        String strEndTime = dateFormat.format(task.getEndTime());
        values.put(KEY_BEGIN_TIME, strBeginTime);
        values.put(KEY_END_TIME, strEndTime);

        return values;
    }

    //history db method
    public ArrayList<String> getHistorys(String sql) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getAll(sql);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                list.add(cursorToHistory(cursor));
            }
            cursor.close();
        }
        return list;
    }

    public long insertHistory(java.util.Date ngayDongBo, String taskName) {
        ContentValues values = new ContentValues();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");
        values.put(KEY_HISTORY_NGAY_DONG_BO, dateTimeFormat.format(ngayDongBo));
        values.put(KEY_TASK_NAME, taskName);
        return insert(TABLE_HISTORY, values);
    }

    public boolean deleteHistory(String where) {
        return delete(TABLE_HISTORY, where);
    }

    private String cursorToHistory(Cursor cursor) {
        String str = String.format("%-25s%s",
                cursor.getString(cursor.getColumnIndex(KEY_HISTORY_NGAY_DONG_BO)),
                cursor.getString(cursor.getColumnIndex(KEY_TASK_NAME)));
        return str;
    }
}
