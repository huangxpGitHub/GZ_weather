package com.example.a52249.gz_weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 52249 on 2019/6/15.
 * json对象解析出来：城市的名称，，编号，天气更新时间
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
