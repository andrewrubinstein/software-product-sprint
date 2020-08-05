
package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.ArrayList;

public class Day {
    boolean[] timeSlots = new boolean[24 * 2 + 1];
    
    public Day()
    {
        timeSlots[timeSlots.length - 1] = true;
    }
    //Blocks out time in schedule according to the request, and events attendees, and schedules
    public void setAvailability(Collection<Event> events, MeetingRequest request)
    {
        boolean isEventApplicable;
        for(Event event:events)
        {
            isEventApplicable = false;
            for(String attendee:request.getAttendees())
            {
                if(event.getAttendees().contains(attendee))
                {
                    isEventApplicable = true;
                    break;
                }
            }
            if(isEventApplicable)
                blockOutTimes(event.getWhen());
        }
    }
    private void blockOutTimes(TimeRange block)
    {
        int end = block.end();
        for(int i = block.start(); i < end; i += 30)
        {
            timeSlots[i / 30] = true;
        }
    }
    //using the known available timeSlots this returns a list of timeRamges that are free to fit the request
    public ArrayList<TimeRange> buildAvailabilities(ArrayList<TimeRange> available,int duration)
    {
        int start = 0,end = 0,trailer = 0;
        for(int i = 0; i < timeSlots.length; i++)
        {
            end = i*30;
            if(!timeSlots[trailer] && timeSlots[i]) 
            {
                if(end - start >=  duration)
                {
                    available.add(TimeRange.fromStartDuration(start, end - start));
                }
            }
            else if(timeSlots[trailer] && !timeSlots[i]) 
            {
                start = end;
            }
            trailer = i;
        }
        return available;
    }
}