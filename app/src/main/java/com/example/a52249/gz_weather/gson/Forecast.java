package com.example.a52249.gz_weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 52249 on 2019/6/15.
 * 每天的天气，包括：时间，天气状况，最高温度，最低温度
 */

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }

}
