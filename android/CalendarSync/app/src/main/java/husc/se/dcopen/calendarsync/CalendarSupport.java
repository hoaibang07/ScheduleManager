package husc.se.dcopen.calendarsync;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.TimeZone;

public class CalendarSupport extends ContextWrapper{

    public static final String[] FIELDS = {
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND };

    public CalendarSupport(Context base) {
        super(base);
    }

    public ArrayList<Task> getEventFromCalendar(java.util.Date btime, java.util.Date etime) {
        ArrayList<Task> listTask = new ArrayList<>();
        Uri uri = getCalendarUriEvent();
        Cursor cursor = getContentResolver().query(uri, FIELDS, null, null, null);

        while(cursor.moveToNext()) {
            java.util.Date dtstart = new java.util.Date(Long.parseLong(cursor.getString(4)));
            java.util.Date dtend;
            if(cursor.getString(5) != null) {
                dtend = new java.util.Date(Long.parseLong(cursor.getString(5)));
            } else {
                dtend = dtstart;
            }
            if(!(dtend.getTime() < btime.getTime() || dtstart.getTime() > etime.getTime())) {
                String id = cursor.getString(0);

                String title = cursor.getString(1);
                String descrip = cursor.getString(2);
                String location = cursor.getString(3);

                Task task = new Task();
                task.setId(id);
                task.setTaskName(title);
                task.setTaskContent(descrip);
                task.setPlace(location);
                task.setBeginTime(dtstart);
                task.setEndTime(dtend);

                listTask.add(task);
            }
        }
        return listTask;
    }

    public void insertToCalendar(Task task) {
        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 1);
        event.put(CalendarContract.Events._ID, Long.parseLong(task.getId()));
        event.put(CalendarContract.Events.TITLE, task.getTaskName());
        event.put(CalendarContract.Events.DESCRIPTION, task.getTaskContent());
        event.put(CalendarContract.Events.EVENT_LOCATION, task.getPlace());
        event.put(CalendarContract.Events.DTSTART, task.getBeginTime().getTime());
        event.put(CalendarContract.Events.DTEND, task.getEndTime().getTime());
        event.put(CalendarContract.Events.ALL_DAY, 0);
        event.put(CalendarContract.Events.HAS_ALARM, 0);
        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

        Uri baseUri = getCalendarUriEvent();
        getContentResolver().insert(baseUri, event);
    }

    public long editToCalendar(Task task) {
        final ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, 1);
        event.put(CalendarContract.Events.TITLE, task.getTaskName());
        event.put(CalendarContract.Events.DESCRIPTION, task.getTaskContent());
        event.put(CalendarContract.Events.EVENT_LOCATION, task.getPlace());
        event.put(CalendarContract.Events.DTSTART, task.getBeginTime().getTime());
        event.put(CalendarContract.Events.DTEND, task.getEndTime().getTime());
        event.put(CalendarContract.Events.ALL_DAY, 0);
        event.put(CalendarContract.Events.HAS_ALARM, 0);
        String timeZone = TimeZone.getDefault().getID();
        event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

        Uri baseUri = getCalendarUriEvent();
        Uri updateUri = ContentUris.withAppendedId(baseUri, Long.parseLong(task.getId()));
        return getContentResolver().update(updateUri, event, null, null);
    }

    public long deleteTaskInCalendar(long taskID) {
        Uri eventUri = ContentUris.withAppendedId(getCalendarUriEvent(), taskID);
        return getContentResolver().delete(eventUri, null, null);
    }

    private Uri getCalendarUriEvent() {
        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            eventUri = Uri.parse("content://calendar/events");
        } else {
            eventUri = Uri.parse("content://com.android.calendar/events");
        }
        return eventUri;
    }
}
