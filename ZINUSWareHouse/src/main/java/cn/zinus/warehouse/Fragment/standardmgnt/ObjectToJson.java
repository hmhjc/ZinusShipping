package cn.zinus.warehouse.Fragment.standardmgnt;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Developer:Spring
 * DataTime :2017/8/22 17:58
 * Main Change:
 */

public class ObjectToJson {
    public  static String ObjListToJson(ArrayList<?> list){
        StringBuilder build=new StringBuilder();
        //迭代器
        Iterator<?> iterator=list.iterator();
        // build.append("[");
        String separate="";
        while(iterator.hasNext()){
            build.append(separate);
            //object转json字符串
            String str=ObjToJson(iterator.next());
            build.append(str);
            separate=",";
        }
        //build.append("]");
        return build.toString();
    }

    @SuppressWarnings("unchecked")
    public  static String ObjToJson(Object obj){
        StringBuilder build=new StringBuilder();
        build.append("{");
        @SuppressWarnings("rawtypes")
        Class cla=null;
        try {
            //反射加载类
            cla=Class.forName(obj.getClass().getName());
        } catch (ClassNotFoundException e) {
            System.out.println(obj.getClass().toString().concat(" 未找到这个类"));
            e.printStackTrace();
            return null;
        }

        StringBuffer methodname=new StringBuffer();
        //获取java类的变量
        Field[] fields=cla.getDeclaredFields();
        String separate="";
        for(Field temp:fields){
            build.append(separate);
            build.append("\"");
            build.append(temp.getName());
            build.append("\":");

            methodname.append("get");
            methodname.append(temp.getName().substring(0,1).toUpperCase());
            methodname.append(temp.getName().substring(1));

            build.append("\"");
            Method method=null;
            try {
                //获取java的get方法
                method=cla.getMethod(methodname.toString());
            } catch (NoSuchMethodException | SecurityException e) {
                methodname.setLength(0);
                e.printStackTrace();
            }

            try {
                //执行get方法，获取变量参数的直。
                build.append(method.invoke(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            build.append("\"");
            methodname.setLength(0);
            separate=",";
        }

        build.append("}");
        return build.toString();
    }
}
