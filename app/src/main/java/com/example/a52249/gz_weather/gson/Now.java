package com.example.a52249.gz_weather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 52249 on 2019/6/15.
 * 返回的json对象解析：天气的温度，天气状况实体类
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
