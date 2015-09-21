package com.spit.matrix15;

import com.orm.SugarRecord;

/**
 * Created by The Joshis on 9/20/2015.
 */
public class Event extends SugarRecord<Event> {
    public String eventName;
    public String eventId;
    public String eventDescription;
    public String eventCategory;
    public String email;
    public String contact1;
    public String contact2;
    public String eventHighlight1;
    public String eventHighlight2;
    public String eventHighlight3;
    public String fee;
    public String venue;
    public String eventPoster;

    public Event () {

    }
}
