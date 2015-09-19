package com.spit.matrix15;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kshitij on 9/19/2015.
 */
public class EventsDataSource {
    private SQLiteDatabase database;
    private EventSQLiteHelper dbHelper;

    private String[] allColumns = { EventSQLiteHelper.COLUMN_contact1,
            EventSQLiteHelper.COLUMN_contact2, EventSQLiteHelper.COLUMN_category, EventSQLiteHelper.COLUMN_id, EventSQLiteHelper.COLUMN_desc, EventSQLiteHelper.COLUMN_email,
            EventSQLiteHelper.COLUMN_eventhightlight1, EventSQLiteHelper.COLUMN_eventhightlight2, EventSQLiteHelper.COLUMN_eventhightlight3, EventSQLiteHelper.COLUMN_fee,
            EventSQLiteHelper.COLUMN_postername, EventSQLiteHelper.COLUMN_venue, EventSQLiteHelper.COLUMN_name };

    public EventsDataSource(Context context, int version) {
        dbHelper = new EventSQLiteHelper(context, version);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertListIntoSQLite(ArrayList<HashMap<String, String>> eventList) {
        for (HashMap<String, String> map : eventList) {
            ContentValues contentValues = new ContentValues();
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                String key = mapEntry.getKey();
                String value = mapEntry.getValue();
                contentValues.put(key, value);
            }
            long insertId = database.insert(EventSQLiteHelper.TABLE_Events, null,
                    contentValues);
        }
    }

    private EventData cursorToExpenseData(Cursor cursor) {
        EventData eventData = new EventData();
        eventData.setId(String.valueOf(cursor.getLong(0)));
        eventData.setName(cursor.getString(1));
//        expenseData.setAmount(cursor.getFloat(2));
//        expenseData.setCategory(cursor.getString(3));
//        expenseData.setNote(cursor.getString(4));
        return eventData;
    }

}
