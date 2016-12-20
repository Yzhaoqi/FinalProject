package com.group.android.finalproject.player.util;

/**
 * Created by YZQ on 2016/12/20.
 */

public class RecordItem {
//    map.put("title", cursor.getString(cursor.getColumnIndex("title")));
//    map.put("date", cursor.getString(cursor.getColumnIndex("date")));
//    map.put("feel", cursor.getString(cursor.getColumnIndex("feel")));
//    map.put("place", cursor.getString(cursor.getColumnIndex("place")));
//    map.put("remark", cursor.getString(cursor.getColumnIndex("remark")));
//    map.put("storeUrl", cursor.getString(cursor.getColumnIndex("storeUrl")));
    private String title, date, feel, place, remark, storeUrl;

    public RecordItem(String title, String date, String feel, String place, String remark, String storeUrl) {
        this.title = title;
        this.date = date;
        this.feel = feel;
        this.place = place;
        this.remark = remark;
        this.storeUrl = storeUrl;
    }

    public String getDate() {
        return date;
    }

    public String getFeel() {
        return feel;
    }

    public String getPlace() {
        return place;
    }

    public String getRemark() {
        return remark;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public String getTitle() {
        return title;
    }
}
