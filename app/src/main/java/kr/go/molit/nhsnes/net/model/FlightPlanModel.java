package kr.go.molit.nhsnes.net.model;

import android.util.Log;

import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import kr.go.molit.nhsnes.model.FlightRouteModel;
import kr.go.molit.nhsnes.net.realm.FlightPlanInfo;

/**
 * Created by user on 2017-08-17.
 */

public class FlightPlanModel {
    private String result_code;
    private String result_msg;
    private FlightPlanInfo FplDetail;
    private ArrayList<FlightRouteModel> route;
    private ArrayList<FlightPlanInfo> list;

    public Object invoke(Object obj, String methodName, Object[] param) {
        Method[] methods = obj.getClass().getMethods();

        for(int i=0; i<methods.length; i++) {
            if(methods[i].getName().equals(methodName)) {
                Log.d("JeLib",""+methods[i].getName());
                try {
                    if (methods[i].getReturnType().getName().equals("void")) {
                        methods[i].invoke(obj, param);
                    } else {
                        return methods[i].invoke(obj, param);
                    }
                } catch(Exception lae) {

                }
            }
        }
        return null;
    }

    public ArrayList<FlightRouteModel> getRoute() {
        if(route!=null && route.getClass().equals(String.class)){
            return null;
        }
        /*
        ArrayList<FlightRouteModel> arr = new ArrayList<FlightRouteModel>();
        for(Object o : (ArrayList)route){
            //FlightRouteModel model = new FlightRouteModel();
            LinkedTreeMap tree = (LinkedTreeMap)o;
            Set s = tree.keySet();
            Iterator i = s.iterator();
            //FlightRouteModel model = new FlightRouteModel();
            Class cls = null;
            FlightRouteModel model = null;
            try {
                cls = Class.forName("kr.go.molit.nhsnes.model.FlightRouteModel");
                model = (FlightRouteModel)cls.newInstance();
            } catch (Exception e) {
                Log.d("JeLib","e:"+e.getMessage());
            }

            while(i.hasNext()){
                String key = (String)i.next();
                Object val = tree.get(key);
                Log.d("JeLib","val::::::::"+val);
                String func = String.format("set%s%s",key.substring(0,1).toUpperCase(),key.substring(1,key.length()));

                try {
                    Method mth  = cls.getMethod(func,new Class[]{String.class});
                    mth.invoke(model,new Object[]{val.toString()});

                } catch (Exception e) {
                    Log.d("JeLib","e::::::::"+e.getMessage());
                }
                Log.d("JeLib","func:"+func+" :::"+model.getStepNum());
                Log.d("JeLib","func:"+func+" :::"+model.getAreaId());
                Log.d("JeLib","func:"+func+" :::"+model.getAreaNm());
                Log.d("JeLib","func:"+func+" :::"+model.getElev());
                Log.d("JeLib","func:"+func+" :::"+model.getHeading());
                Log.d("JeLib","func:"+func+" :::"+model.getLat());
                Log.d("JeLib","func:"+func+" :::"+model.getLon());

            }
        }*/
        return route;
    }

    public void setRoute(ArrayList<FlightRouteModel> route) {
        this.route = route;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public ArrayList<FlightPlanInfo> getList() {
        if(list!=null && list.getClass().equals(String.class)){
            return null;
        }
        return list;
    }

    public void setList(ArrayList<FlightPlanInfo> list) {
        this.list = list;
    }

    public FlightPlanInfo getFplDetail() {
        return FplDetail;
    }

    public void setFplDetail(FlightPlanInfo fplDetail) {
        FplDetail = fplDetail;
    }
    
}
