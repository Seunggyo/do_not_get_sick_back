package com.example.prj2be.mapper.ds;

import com.example.prj2be.domain.business.BusinessHoliday;
import com.example.prj2be.domain.ds.Ds;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DsMapper {
    @Insert("""
            INSERT INTO
            business(name, address, phone, openHour, openMin, closeHour,
                    closeMin, content, category, nightCare, restHour, restMin,
                    restCloseHour, restCloseMin, info)
            VALUES (#{name}, #{address}, #{phone},
                    #{openHour}, #{openMin}, #{closeHour},
                    #{closeMin}, #{content},'drugStore', #{nightCare},
                    #{restHour}, #{restMin}, #{restCloseHour}, #{restCloseMin},
                    #{info} )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Ds ds);

    @Update("""
            UPDATE business
            SET name = #{name},
                address = #{address},
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
                   COUNT(DISTINCT bl.id) `likeCount`,
                   COUNT(DISTINCT bc.id) `commentCount`
            FROM business b
                LEFT JOIN businesslike bl
                    ON b.id = bl.businessId
                LEFT JOIN businesscomment bc
                    ON b.id = bc.businessId
                LEFT JOIN businessholiday bh
                    ON b.id = bh.businessId
            WHERE b.category = 'drugStore'

            GROUP BY b.id
            LIMIT #{from}, 10
            """)
    List<Ds> selectAllByCategory(Integer from, String keyword, String category);

    @Select("""
            SELECT *
            FROM business
            WHERE id = #{id};
            """)
    Ds selectById(Integer id);

    @Delete("""
            DELETE FROM business
            WHERE id = #{id}
            """)
    int deleteById(Integer id);

    @Select("""
            <script>
            SELECT COUNT(*) FROM business
            WHERE
                <trim prefixOverrides="OR">
                    <if test="category == 'all' or category == 'name'">
                        OR name LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'category'">
                        OR category LIKE #{keyword}
                    </if>
                </trim>
            </script>
            """)
    int countAll(String keyword, String category);

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
//    업데이트를 하는것이 아니라 기존 데이터를 삭제한 후 다시 삽입 하는 식으로 코드 구성
//    @Update("""
//            UPDATE businessholiday
//            SET id = #{id},
//                holiday = #{holiday}
//            WHERE businessId = #{businessId}
//            """)
//    void updateByHoliday(Integer id, String holiday);

    @Delete("""
        DELETE FROM businessHoliday
        WHERE businessId = #{id}
        """)
    void deleteHolidayByDsId(Integer id);
}
