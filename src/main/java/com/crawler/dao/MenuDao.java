package com.crawler.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuDao {

    void update(@Param("id") String id, @Param("column") String column, @Param("value") Object value);
}
