package com.evs.foodexp.models;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    public String lat;
    public String longs;
    public String bookingId;
    public String adress;
    public String driverId;

    public Comment() {

    }

    public Comment(String lat, String longs, String bookingId, String adress, String driverId) {
        this.lat = lat;
        this.longs = longs;
        this.bookingId = bookingId;
        this.adress = adress;
        this.driverId = driverId;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lat", lat);
        result.put("longs", longs);
        result.put("bookingId", bookingId);
        result.put("adress", adress);
        result.put("driverId", driverId);

        return result;
    }
}
