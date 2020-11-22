package com.edescamp.go4lunch.service.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHoursAPIDetails {
    @SerializedName("open_now")
    private Boolean open_now;

//    @SerializedName("periods")
//    private List<PeriodAPIDetails> periods;

    @SerializedName("weekday_text")
    private List<String> weekday_text;


}
