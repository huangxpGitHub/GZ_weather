package com.example.a52249.gz_weather.db;


import org.litepal.crud.DataSupport;

/**
 * Created by 52249 on 2019/6/14.
 */

public class Province extends DataSupport {

    private int id;
    private String provinceName;//省的名字
    private int provinceCode;//省的代号


    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
