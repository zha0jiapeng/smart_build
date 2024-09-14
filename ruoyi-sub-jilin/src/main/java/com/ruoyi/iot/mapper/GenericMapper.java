package com.ruoyi.iot.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericMapper {
    @Delete("DELETE FROM ${table} WHERE DATE(creation_date) < DATE_SUB(CURDATE(), INTERVAL #{intervalMonths} MONTH)")
    int deleteOldRecords(@Param("table") String table, @Param("intervalMonths") int intervalMonths);
}

