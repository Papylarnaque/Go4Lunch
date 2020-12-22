package com.edescamp.go4lunch.model.details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHoursAPIDetails {
    @SerializedName("open_now")
    private Boolean open_now;

//    @SerializedName("periods")
//    private List<PeriodAPIDetails> periods;

    @SerializedName("weekday_text")
    private List<String> weekday_text;

    public Boolean getOpen_now() {
        return open_now;
    }

    public void setOpen_now(Boolean open_now) {
        this.open_now = open_now;
    }

    public List<String> getWeekday_text() {
        return weekday_text;
    }

    public void setWeekday_text(List<String> weekday_text) {
        this.weekday_text = weekday_text;
    }
}
