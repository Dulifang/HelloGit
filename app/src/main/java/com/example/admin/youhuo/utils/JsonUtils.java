package com.example.admin.youhuo.utils;

/**
 * Created by admin on 2016/11/23.
 */

import android.content.Context;

import com.example.admin.youhuo.info.AdverInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理整个项目中所有的Json解析
 */
public class JsonUtils {
    private Context context;
    private String jsonStr;
    private int index;

    public JsonUtils(Context context, String jsonStr, int index) {
        this.context = context;
        this.jsonStr = jsonStr;
        this.index = index;
    }

    private List getAdvers(String jsonStr) {
        List<AdverInfo> list = new ArrayList<AdverInfo>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONObject json1 = json.getJSONObject("result");
            JSONArray array = json1.getJSONArray("data");
            for (int i = 6; i < 11; i++) {
                JSONObject jo = array.getJSONObject(i);
                AdverInfo ad = new AdverInfo();
                JSONArray arry2 = jo.getJSONArray("albums");
                String imgUrl = arry2.getString(0);
                ad.setImgpath(imgUrl);
                list.add(ad);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
