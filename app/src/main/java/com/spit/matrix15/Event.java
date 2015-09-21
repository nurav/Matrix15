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

    public Event (){

    }

    public Event(String eventName, String eventId, String eventDescription, String eventCategory, String email, String contact1, String contact2, String eventHighlight1, String eventHighlight2, String eventHighlight3, String fee, String venue, String eventPoster) {
        this.eventName = eventName;
        this.eventId = eventId;
        this.eventDescription = eventDescription;
        this.eventCategory = eventCategory;
        this.email = email;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.eventHighlight1 = eventHighlight1;
        this.eventHighlight2 = eventHighlight2;
        this.eventHighlight3 = eventHighlight3;
        this.fee = fee;
        this.venue = venue;
        this.eventPoster = eventPoster;
    }
}
