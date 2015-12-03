package husc.se.dcopen.calendarsync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "MyPlan.db";
    public static final int DATA_VERSION = 1;

    //table Task
    public static final String TABLE_TASK = "Task";
    public static final String KEY_TASK_ID = "TaskID";
    public static final String KEY_TASK_TYPE = "TaskType";
    public static final String KEY_TASK_SYNC = "TaskSync";
    public static final String CREATE_TABLE_TASK = "CREATE TABLE IF NOT EXISTS " + TABLE_TASK + " ("+
            KEY_TASK_ID +" TEXT PRIMARY KEY, "+
            KEY_TASK_TYPE + " INTEGER, "+
            KEY_TASK_SYNC + " INTEGER)";

    //table History
    public static final String TABLE_HISTORY = "History";
    public static final String KEY_HIS_ID = "HistoryID";
    public static final String KEY_HIS_NGAY_DONG_BO = "NgayDongBo";
    public static final String KEY_HIS_NAME = "HisName";
    public static final String KEY_HIS_CONTENT = "HisContent";
    public static final String KEY_HIS_TYPE = "HisType";
    public static final String CREATE_TABLE_HISTORY = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + " (" +
            KEY_HIS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_HIS_NGAY_DONG_BO + " TEXT, " +
            KEY_HIS_NAME + " TEXT, " +
            KEY_HIS_CONTENT + " TEXT, " +
            KEY_HIS_TYPE + " INTEGER)";

    public static final String GET_HISTORY_SYNC_UP = "select "+KEY_HIS_NGAY_DONG_BO+", "+KEY_HIS_NAME+", " + KEY_HIS_CONTENT + " " +
            "from " + TABLE_HISTORY + " " +
            "where " + KEY_HIS_TYPE +"  = '0' " +
            "order by " + KEY_HIS_ID + " desc";
    public static final String GET_HISTORY_SYNC_DOWN = "select " +
            KEY_HIS_NGAY_DONG_BO + ", " +
            KEY_HIS_NAME + ", " +
            KEY_HIS_CONTENT + " " +
            "from " + TABLE_HISTORY + " " +
            "where " + KEY_HIS_TYPE + " = '1' " +
            "order by " + KEY_HIS_ID + " desc";

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

    /**
     *
     * @param task
     * @return 1 nếu như là lịch cá nhân, 0 nếu không phải, -1 nếu là lịch cá nhân chưa có trong csdl
     */
    public int checkPersonalTask(Task task) {
        String sql = "select " +KEY_TASK_TYPE+ ", " +KEY_TASK_SYNC+" from " +TABLE_TASK+ " where " +KEY_TASK_ID+ " = '" +task.getId() + "'";
        Cursor cursor = getAll(sql);
        if(cursor.getCount() == 1) {
            cursor.moveToNext();
            task.setType(cursor.getInt(cursor.getColumnIndex(KEY_TASK_TYPE)))
                    .setSync(cursor.getInt(cursor.getColumnIndex(KEY_TASK_SYNC)));
            close();

            if(task.getType() == 0) return 1;
            else return 0;
        } else {
            close();
            task.setType(0); //lich ca nhan
            task.setSync(0); //chua dong bo
            return -1;
        }
    }

    //task db method
    public boolean checkExistTask(String taskID) {
        Cursor cursor = getAll("select TaskID from Task where TaskID = '" +taskID+"'");
        return (cursor.getCount() == 1);
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
        close();
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
                .setType(cursor.getInt(cursor.getColumnIndex(KEY_TASK_TYPE)))
                .setSync(cursor.getInt(cursor.getColumnIndex(KEY_TASK_SYNC)));
        return task;
    }

    private ContentValues taskToValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, task.getId());
        values.put(KEY_TASK_TYPE, task.getType());
        values.put(KEY_TASK_SYNC, task.getSync());

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

    public long insertHistory(java.util.Date ngayDongBo, String taskName, String taskContent, int taskType) {
        ContentValues values = new ContentValues();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm aa");
        values.put(KEY_HIS_NGAY_DONG_BO, dateTimeFormat.format(ngayDongBo));
        values.put(KEY_HIS_NAME, taskName);
        values.put(KEY_HIS_CONTENT, taskContent);
        values.put(KEY_HIS_TYPE, taskType);
        return insert(TABLE_HISTORY, values);
    }

    public boolean deleteHistory(String where) {
        return delete(TABLE_HISTORY, where);
    }

    private String cursorToHistory(Cursor cursor) {
        return String.format("%-25s%s - %s",
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2));
    }
}
