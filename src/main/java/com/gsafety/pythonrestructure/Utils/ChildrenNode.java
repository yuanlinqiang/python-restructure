package com.gsafety.pythonrestructure.Utils;

import java.util.List;
import java.util.Map;

public class ChildrenNode {


    /**
     * 获取子级ids，含自己
     * @param id 父节点
     * @param TaxBureauList 组织单位列表
     * @return
     */
    public static String  getChildIds(String id, List<Map<String, Object>> TaxBureauList) {
        StringBuilder childIds = new StringBuilder();
        childIds.append(id + ",");
        getChildIds(id, childIds, TaxBureauList);
        return childIds.toString().substring(0, childIds.length()-1);
    }

    private  static  void  getChildIds(String id, StringBuilder childIds, List<Map<String, Object>> TaxBureauList) {
        for (Map<String, Object> bureau : TaxBureauList) {
            //过滤父节点为空的数据
            if (bureau.get("parentId") == null ){
                continue;
            }
            // 判断是否存在子节点
            if (id.equals(bureau.get("parentId").toString())) {
                childIds.append(bureau.get("id").toString()+",");
                // 递归遍历下一级
                getChildIds(bureau.get("id").toString(), childIds, TaxBureauList);
            }
        }
        return;
    }
}
