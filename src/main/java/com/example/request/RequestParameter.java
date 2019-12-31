package com.example.request;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestParameter{
    public static Map getParameter(HttpServletRequest request, Map<String,String> parameter) {
        Map map = new HashMap();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String s = null;
            while ((s=br.readLine())!=null){
                sb.append(s);
            }
            if (sb!=null) {
                JSONObject jsonObject = JSONObject.parseObject(sb.toString());
                for (String str:parameter.keySet()
                     ) {
                    map.put(str,jsonObject.getString(str));
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return map;
    }
}
