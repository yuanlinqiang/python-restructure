package com.gsafety.pythonrestructure.Utils;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    public  int  pageIndex;
    public  int  pageSize;
    public  int  totalCount;
    public  int  pageCount;
    public  List<T>  content;

}
