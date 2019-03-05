package com.crawler.util;

import com.crawler.entity.BaseEntity;
import org.joda.time.DateTime;

import java.util.Date;

public class EntityUtil {

    public static <T extends BaseEntity> void init(T t){
        BaseEntity baseEntity = (BaseEntity) t;
        try {
            t.getClass().getMethod("setId", String.class).invoke(t, IdGeneratorUtil.id());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date time = DateTime.now().toDate();
        baseEntity.setCreateTime(time);
        baseEntity.setModifyTime(time);
        baseEntity.setStatus(0);
    }
}
