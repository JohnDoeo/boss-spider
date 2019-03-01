package com.johndoeo.utils;

import java.io.BufferedReader;
import java.io.FileReader;

public class JSONUtil {
    public static String readFile(String file){
        BufferedReader br = null;
        try{
             br = new BufferedReader(new FileReader(file));// 读取NAMEID对应值
            String s = null;
            StringBuilder sb = new StringBuilder();
            while ((s = br.readLine()) != null){
                sb.append(s);
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(br != null){
                try{
                    br.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
