package com.gsafety.pythonrestructure.config.oauth2.neo4j;

import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neo4jConfig {

    public static List getNeo4jData(String neo4jSql) throws Exception{
        try (Connection con = DriverManager.getConnection("jdbc:neo4j:http://10.3.10.205/7474", "neo4j", "neo4j123")){
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(neo4jSql);
                List populate = populate(rs);
                return populate;
            }
        }
    }

    public static List populate(ResultSet rs ) throws SQLException{
        Gson gson = new Gson();
        //结果集的元素对象
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        //返回结果的列表集合
        List list = new ArrayList();
        //业务对象的属性数组
        while (rs.next()) {
            Map<String,Object> rowData = new HashMap<String,Object>();
            for (int i = 1; i <= colCount; i++) {
                rowData.put(rsmd.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }


}
