package com.example.prj2be.mapper.hs;

import com.example.prj2be.domain.hs.Hs;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface HsMapper {

    @Select("""
        SELECT b.id,b.name,b.address,b.homePage,b.openHour,b.openMin,b.closeHour,b.closeMin,bm.lat,bm.lng,b.content,b.category,b.nightCare,b.phone
        FROM prj2.business b left join prj2.businessmap bm on b.id = bm.businessId
        WHERE b.category = #{category}
        """)
    List<Hs> selectByCategory(String category);

    @Insert("""
        INSERT INTO prj2.business(name,address,phone,openHour,openMin,closeHour,closeMin,content,category,nightCare,homePage)
        VALUES (#{name},#{address},#{phone},#{openHour},#{openMin},#{closeHour},#{closeMin},#{content},'hospital',#{nightCare},#{homePage})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Hs hs);

    @Update("""
        UPDATE prj2.business
        SET name = #{name},
        address = #{address},
        phone =#{phone},
        openHour = #{openHour},
        openMin = #{openMin},
        closeHour = #{closeHour},
        closeMin = #{closeMin},
        content = #{content},
        homePage = #{homePage},
        nightCare = #{nightCare}
        WHERE id = #{id}
        """)
    int update(Hs hs);

    @Select("""
            SELECT b.id,b.name, b.address, b.phone, b.openhour, b.openmin, b.closehour, b.closemin, b.content, b.homePage, b.category, b.nightcare
            FROM prj2.business b
            WHERE b.id = #{id}
        """)
    Hs selectById(Integer id);
}
