package com.example.a52249.gz_weather;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a52249.gz_weather.db.City;
import com.example.a52249.gz_weather.db.County;
import com.example.a52249.gz_weather.db.Province;
import com.example.a52249.gz_weather.util.HttpUtil;
import com.example.a52249.gz_weather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 52249 on 2019/6/15.
 */

public class Choose_AreaFragment extends Fragment {
    public static final int LEVEL_PROVINCES =0 ;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY =2;
    private AlertDialog alertDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> datalist = new ArrayList<>();

    private List<Province> provinceList;//省列表
    private List<City> cityList;//市列表
    private List<County> countyList;//县列表

    private Province selectedProvince;//选中的省
    private City selectedCity;//选中的市
    private County selectedCounty;//选中的县

    private int currentLevel;//选中的级别


    /*

    获取控件实例
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText =view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        return view;
    }

    /*
    为控件设置点击事件
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                if(currentLevel == LEVEL_PROVINCES){
                    selectedProvince=provinceList.get(i);
                    queryCities();
                }else if(currentLevel ==LEVEL_CITY){
                    selectedCity = cityList.get(i);
                    queryCounties();
                }else if(currentLevel ==LEVEL_COUNTY){
                    String weatherId = countyList.get(i).getWeatherId();
                    Intent intent =new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel ==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(listView.GONE);//设置按钮不可见且不占据空间
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            datalist.clear();
            for(Province province:provinceList){
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCES;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);//设置按钮可见
        cityList = DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            datalist.clear();
            for(City city:cityList){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }

    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            datalist.clear();
            for(County county:countyList){
                datalist.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }

    }

    private void queryFromServer(String address,final String type){
        showalertDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {//向服务器发送请求，响应的数据回调到onResponse（）中
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closealterDialog();
                        Toast.makeText(getContext(),"加载失败...",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);//解析和处理服务器返回的数据，并存储到数据库
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {//实现从子线程切换到主线程
                        @Override
                        public void run() {
                            closealterDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }
        });


    }
    private void showalertDialog(){
        if(alertDialog == null){
            alertDialog.setMessage("正在加载...");
            alertDialog.setCancelable(false);
        }
        alertDialog.show();

    }
    private void closealterDialog(){
        if(alertDialog!=null){
            alertDialog.dismiss();
        }
    }


}
