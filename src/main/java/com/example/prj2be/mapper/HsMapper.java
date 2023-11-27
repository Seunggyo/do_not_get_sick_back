package com.example.prj2be.mapper;

import com.example.prj2be.domain.Hs;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HsMapper {

    @Select("""
        SELECT b.id,b.name,b.address,b.homePage,b.openHour,b.openMin,b.closeHour,b.closeMin,bm.lat,bm.lng,b.content,b.category,b.nightCare,b.phone
        FROM prj2.business b left join prj2.businessmap bm on b.id = bm.businessId
        WHERE b.category = #{category}
        """)
    List<Hs> selectByCategory(String category);

    @Insert("""
        INSERT INTO prj2.business(name,address,phone,openHour,openMin,closeHour,closeMin,content,category,nightCare)
        VALUES (#{name},#{address},#{phone},#{openHour},#{openMin},#{closeHour},#{closeMin},#{content},'hospital',#{nightCare})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Hs hs);
}
