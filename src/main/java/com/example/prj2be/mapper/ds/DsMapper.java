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
                nightCare = #{nightCare},
                holiday = #{holiday}
            WHERE id = #{id}
            """)
    int updateById(Ds ds);

    @Select("""
            <script>
            SELECT b.id,
                   b.name,
                   b.phone,
                   b.address,
                   b.category,
                   COUNT(DISTINCT bl.id) `likeCount`,
                   COUNT(DISTINCT bc.id) `commentCount`
            FROM business b
                LEFT JOIN businesslike bl
                    ON b.id = bl.businessId
                LEFT JOIN businesscomment bc
                    ON b.id = bc.businessId
            WHERE 
                <trim prefixOverrides="OR">
                    <if test="category == 'all' or category == 'name'">
                        OR name LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'category'">
                        OR category LIKE #{keyword}
                    </if>
                </trim>
            GROUP BY b.id
            LIMIT #{from}, 10
            </script>
            """)
    List<Ds> selectAllByCategory(Integer from, String keyword, String category);

    // 데이터 값이 없어서 임시로 사용
//    @Select("""
//            <script>
//            SELECT b.id,
//                   b.name,
//                   b.phone,
//                   b.address,
//                   b.category
//            FROM business b
//            WHERE
//                <trim prefixOverrides="OR">
//                    <if test="category == 'all' or category == 'name'">
//                        OR name LIKE #{keyword}
//                    </if>
//                    <if test="category == 'all' or category == 'category'">
//                        OR category LIKE #{keyword}
//                    </if>
//                </trim>
//            GROUP BY b.id
//            LIMIT #{from}, 10
//            </script>
//            """)
//    List<Ds> selectAllByCategory(Integer from, String keyword, String category);

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
}
