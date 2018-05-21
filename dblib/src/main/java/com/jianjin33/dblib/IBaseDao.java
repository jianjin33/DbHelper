package com.jianjin33.dblib;

import java.util.List;

/**
 * Created by Administrator on 2018/5/20.
 * 数据库增删改查
 */
public interface IBaseDao<T> {

    Long insert(T entity);

    int update(T entity,T where);

    int delete(T where);

    List<T> query(T where);

    List<T> query(T where,String orderBy, Integer startIndex,Integer limit);
}
