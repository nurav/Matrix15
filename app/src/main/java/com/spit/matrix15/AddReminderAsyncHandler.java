package com.spit.matrix15;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

public class AddReminderAsyncHandler extends AsyncQueryHandler {

    ContentResolver contentResolver;

    public AddReminderAsyncHandler(ContentResolver cr) {
        super(cr);
        contentResolver = cr;
    }

    public long inertValues(Uri eventsUri, ContentValues values) {
        Uri uri = contentResolver.insert(eventsUri, values);
        return Long.parseLong(uri.getLastPathSegment());
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);
        extractEventID(uri);
    }

    public long extractEventID(Uri uri) {
        return Long.parseLong(uri.getLastPathSegment());
    }
}