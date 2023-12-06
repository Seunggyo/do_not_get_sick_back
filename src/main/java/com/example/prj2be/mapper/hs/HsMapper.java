package com.example.prj2be.mapper.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.domain.hs.HsCourse;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface HsMapper {

    @Select("""
        SELECT b.id,b.name,b.address,b.homePage,b.openHour,b.openMin,b.closeHour,b.closeMin,
               bm.lat,bm.lng,b.content,b.category,b.nightCare,b.phone, COUNT(DISTINCT b2.id) countLike
        FROM prj2.business b left join prj2.businessmap bm on b.id = bm.businessId
        left join prj2.businesslike b2 on b.id = b2.businessId
        WHERE b.category = #{category}
        GROUP BY
            b.id,
            b.name,
            b.address,
            b.homePage,
            b.openHour,
            b.openMin,
            b.closeHour,
            b.closeMin,
            bm.lat,
            bm.lng,
            b.content,
            b.category,
            b.nightCare,
            b.phone;
        """)
    List<Hs> selectByCategory(String category);

    @Insert("""
        INSERT INTO prj2.business(name,memberId,address,phone,openHour,openMin,restHour,restMin,closeHour,closeMin,content,category,nightCare,homePage)
        VALUES (#{name},#{memberId},#{address},#{phone},#{openHour},#{openMin},#{restHour},#{restMin},#{closeHour},#{closeMin},#{content},'hospital',#{nightCare},#{homePage})
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
            SELECT b.id,b.memberId,b.name, b.address, b.phone, b.openhour, b.openmin, b.closehour, b.closemin, b.content, b.homePage, b.category, b.nightcare
            FROM prj2.business b
            WHERE b.id = #{id}
        """)
    Hs selectById(Integer id);

    @Delete("""
        DELETE FROM prj2.business
        WHERE id = #{id}
        """)
    int deleteById(Integer id);


    @Insert("""
        INSERT INTO prj2.medicalcourse(medicalCourseId, medicalCourseCategory) 
        VALUES (#{id},#{medicalCourse})
        """)
    void insertCourse(Integer id, String medicalCourse);

    @Select("""
        SELECT id,medicalCourseCategory
        FROM prj2.medicalcourse
        WHERE medicalCourseId =#{id}
        """)
    List<HsCourse> courseSelectByBuisnessId(Integer id);
}
