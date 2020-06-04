package com.gsafety.pythonrestructure.Utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PageHelp {


    public static Page newPages(List list , int pageIndex, int pageSize){ //拿到当前的总条数和每页展示条数
        Page page = new Page();
        page.setTotalCount(list.size());
        page.setPageSize(pageSize);
        Object collect = list.stream().skip(pageSize * (pageIndex - 1))
                .limit(pageSize).collect(Collectors.toList());
        List list1 = StringCommentUtils.get(List.class, collect);
        int pageNo = (list.size() + page.getPageSize() - 1) / page.getPageSize(); //总页数
        page.setPageCount(pageNo);
        page.setPageIndex(pageIndex);
        page.setContent(list1);
        return  page;
    }

    public static Map<String,Object> newPagesMap(List list , int pageIndex, int pageSize){ //拿到当前的总条数和每页展示条数
        Page page = new Page();
        Map<String,Object> map = new HashMap<>();
        Object collect = list.stream().skip(pageSize * (pageIndex - 1))
                .limit(pageSize).collect(Collectors.toList());
        List list1 = StringCommentUtils.get(List.class, collect);
        int pageNo = (list.size() + pageSize - 1) / pageSize; //总页数

        map.put("page_index",pageIndex);
        map.put("page_size",pageSize);
        map.put("page_count",pageNo);
        map.put("total_count",list.size());
        map.put("content",list1);
        return  map;
    }
}
