package shengsiyuan.capter01;

import java.sql.Connection;
import java.sql.DriverManager;

public class Test9 {
    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("");
    }
}
