package com.example.a52249.gz_weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 52249 on 2019/6/15.
 *
 * json实体类：天气介绍，天气建议，天气建议
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
