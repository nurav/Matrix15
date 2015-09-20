package com.spit.matrix15;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kshitij on 9/19/2015.
 */
public class EventSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_Events = "Events";
    public static final String COLUMN_id = "event_id";
    public static final String COLUMN_name = "event_name";
    public static final String COLUMN_desc = "event_desciption";
    public static final String COLUMN_email = "email";
    public static final String COLUMN_contact1 = "contact1";
    public static final String COLUMN_contact2 = "contact2";
    public static final String COLUMN_fee = "fee";
    public static final String COLUMN_venue = "venue";
    public static final String COLUMN_eventhightlight1 = "eventhighlight1";
    public static final String COLUMN_eventhightlight2 = "eventhighlight2";
    public static final String COLUMN_eventhightlight3 = "eventhighlight3";
    public static final String COLUMN_category = "category";
    public static final String COLUMN_postername = "eventposter";

    private static final String DATABASE_NAME = "Events.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "+ TABLE_Events + "(" + COLUMN_id + " INTEGER PRIMARY KEY, "
            + COLUMN_name + " TEXT NOT NULL," + COLUMN_desc + " TEXT, " + COLUMN_email +
            " TEXT, " + COLUMN_contact1 + " TEXT, " + COLUMN_contact2 + " TEXT, " + COLUMN_fee + " TEXT, " +
            COLUMN_venue + " TEXT, " + COLUMN_eventhightlight1 + " TEXT, " + COLUMN_eventhightlight2 + " TEXT, " +
            COLUMN_eventhightlight3 + " TEXT, " + COLUMN_category + " TEXT, " + COLUMN_postername + " TEXT"
            + ");";

    public EventSQLiteHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(EventSQLiteHelper.class.getName(),
                "Upgrading database from version " + i + " to "
                        + i1 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Events);
        onCreate(sqLiteDatabase);
    }
}
