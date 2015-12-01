package husc.se.dcopen.calendarsync;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.TimeZone;

public class CalendarSupport extends ContextWrapper{

    public CalendarSupport(Context base) {
        super(base);
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

        Uri baseUri = getCalendarUriBase();
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

        Uri baseUri = getCalendarUriBase();
        Uri updateUri = ContentUris.withAppendedId(baseUri, Long.parseLong(task.getId()));
        return getContentResolver().update(updateUri, event, null, null);
    }

    public long deleteTaskInCalendar(long taskID) {
        Uri eventUri = ContentUris.withAppendedId(getCalendarUriBase(), taskID);
        return getContentResolver().delete(eventUri, null, null);
    }

    private Uri getCalendarUriBase() {
        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            eventUri = Uri.parse("content://calendar/events");
        } else {
            eventUri = Uri.parse("content://com.android.calendar/events");
        }
        return eventUri;
    }
}
