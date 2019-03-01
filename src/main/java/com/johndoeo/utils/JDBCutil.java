package com.johndoeo.utils;

import java.sql.*;

public class JDBCutil {
    static String username = "root";
    static String password = "123456";
    static String url = "jdbc:mysql://db:3306/spider?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true";
    static String driverClass = "com.mysql.jdbc.Driver";
    static {
        try{
            Class.forName(driverClass);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(url, username, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void close( Statement ps, Connection conn){
        close(null,ps,conn);
    }
    /**
     * 关闭链接
     * @param rs
     * @param ps
     * @param conn
     */
    public static void close(ResultSet rs, Statement ps, Connection conn){
        try {
            if(rs != null){
                rs.close();
            }
            if(ps != null){
                ps.close();
            }
            if(conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
