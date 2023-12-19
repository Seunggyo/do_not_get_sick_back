package com.example.prj2be.mapper.ds;

import com.example.prj2be.domain.business.BusinessHoliday;
import com.example.prj2be.domain.ds.Ds;
import com.example.prj2be.domain.ds.DsKakao;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DsMapper {

    @Insert("""
        INSERT INTO
        business(name, address, phone, openHour, openMin, closeHour,
                closeMin, content, category, nightCare, restHour, restMin,
                restCloseHour, restCloseMin, info, memberId, oldAddress)
        VALUES (#{name}, #{address}, #{phone},
                #{openHour}, #{openMin}, #{closeHour},
                #{closeMin}, #{content},'drugStore', #{nightCare},
                #{restHour}, #{restMin}, #{restCloseHour}, #{restCloseMin},
                #{info}, #{memberId}, #{oldAddress} )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Ds ds);

    @Update("""
        UPDATE business
        SET name = #{name},
            address = #{address},
            oldAddress = #{oldAddress},
            phone = #{phone},
            openHour = #{openHour},
            openMin = #{openMin},
            closeHour = #{closeHour},
            closeMin = #{closeMin},
            restHour = #{restHour},
            restMin = #{restMin},
            restCloseHour = #{restCloseHour},
            restCloseMin = #{restCloseMin},
            info = #{info},
            content = #{content},
            nightCare = #{nightCare}
            
        WHERE id = #{id}
        """)
    int updateById(Ds ds);

    //    TODO : 검색 기능 추가해야 함
    @Select("""
        SELECT b.id,
               b.name,
               b.phone,
               b.address,
               b.oldAddress,
               b.category,
               b.openHour,
               b.openMin,
               b.closeHour,
               b.closeMin,
               b.restHour,
               b.restMin,
               b.restCloseHour,
               b.restCloseMin,
               b.memberId,
               bh.holiday,
               COUNT(DISTINCT bl.id) `likeCount`,
               COUNT(DISTINCT bc.id) `commentCount`
        FROM business b
            LEFT JOIN businesslike bl
                ON b.id = bl.businessId
            LEFT JOIN businesscomment bc
                ON b.id = bc.businessId
            LEFT JOIN businessholiday bh
                ON b.id = bh.businessId
        WHERE b.category='drugStore'
            AND (b.name LIKE #{keyword} OR b.oldAddress LIKE #{keyword})
        GROUP BY b.id
        LIMIT #{from}, 15
        """)
    List<Ds> selectAllByCategory(Integer from, String keyword);

    @Select("""
        SELECT b.id,
               b.name,
               b.phone,
               b.address,
               b.oldAddress,
               b.category,
               b.openHour,
               b.openMin,
               b.closeHour,
               b.closeMin,
               b.restHour,
               b.restMin,
               b.restCloseHour,
               b.restCloseMin,
               b.memberId,
               bh.holiday,
               COUNT(DISTINCT bl.id) `likeCount`,
               COUNT(DISTINCT bc.id) `commentCount`
        FROM business b
            LEFT JOIN businesslike bl
                ON b.id = bl.businessId
            LEFT JOIN businesscomment bc
                ON b.id = bc.businessId
            LEFT JOIN businessholiday bh
                ON b.id = bh.businessId
        WHERE b.category='drugStore'
            AND (b.name LIKE #{keyword} OR b.oldAddress LIKE #{keyword})
        GROUP BY b.id
        ORDER BY count(distinct bl.id) DESC
        """)
    List<Ds> selectAllByCategoryMap(String keyword);

    @Select("""
        SELECT b.id,
               b.name,
               b.phone,
               b.address,
               b.oldAddress,
               b.category,
               b.openHour,
               b.openMin,
               b.closeHour,
               b.closeMin,
               b.restHour,
               b.restMin,
               b.restCloseHour,
               b.restCloseMin,
               b.nightCare,
               b.memberId,
               b.content,
               b.info
        FROM business b
        WHERE b.id = #{id};
        """)
    Ds selectById(Integer id);

    @Delete("""
        DELETE FROM business
        WHERE id = #{id}
        """)
    int deleteById(Integer id);

    @Select("""
        SELECT COUNT(*) FROM business b
        WHERE b.category = 'drugStore'
        AND b.name LIKE #{keyword}
        """)
    int countAll(String keyword);

    @Insert("""
        INSERT INTO businessholiday (businessId, holiday)
        VALUES (#{id}, #{holiday})
        """)
    void insertHoliday(Integer id, String holiday);

    @Select("""
        SELECT id, holiday FROM businessholiday
        WHERE businessId = #{id}
        ORDER BY holiday
        """)
    List<BusinessHoliday> selectHolidayById(Integer id);

    @Delete("""
        DELETE FROM businessHoliday
        WHERE businessId = #{id}
        """)
    void deleteHolidayByDsId(Integer id);

    @Select("""
        SELECT name, address, phone
        FROM business;
        """)
    List<DsKakao> selectAllByKakao(DsKakao dsKakao);

    @Select("""
        SELECT * FROM business
        WHERE name = #{name}
        """)
    Ds selectByName(String name);

    @Select("""
                SELECT b.id,
                       b.name,
                       b.phone,
                       b.address,
                       b.oldAddress,
                       b.category,
                       b.openHour,
                       b.openMin,
                       b.closeHour,
                       b.closeMin,
                       b.restHour,
                       b.restMin,
                       b.restCloseHour,
                       b.restCloseMin,
                       bh.holiday,
                       b.memberId,
                       COUNT(DISTINCT bl.id) `likeCount`,
                       COUNT(DISTINCT bc.id) `commentCount`
                FROM business b
                         LEFT JOIN businesslike bl
                                   ON b.id = bl.businessId
                         LEFT JOIN businesscomment bc
                                   ON b.id = bc.businessId
                         LEFT JOIN businessholiday bh
                                   ON b.id = bh.businessId
                WHERE b.category = 'drugStore' AND name LIKE #{keyword}
                GROUP BY b.id
                LIMIT 0, 10
        """)
    List<Ds> getListByCK(String keyword);

    @Select("""
        SELECT id
        FROM business
        WHERE memberId = #{memberId}
        """)
    Integer idGet(String memberId);
}
