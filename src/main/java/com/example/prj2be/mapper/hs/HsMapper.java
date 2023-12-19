package com.example.prj2be.mapper.hs;

import com.example.prj2be.domain.business.BusinessHoliday;
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
            SELECT b.id,b.name,b.address,b.oldAddress,b.homePage,b.openHour,b.openMin,b.closeHour,b.closeMin,
                    b.restHour,b.restMin,b.restCloseHour,b.restCloseMin,
                   b.content,b.category,b.nightCare,b.phone,
                    COUNT(DISTINCT b2.id) countLike, mc.medicalCourseCategory, bh.holiday
                FROM prj2.business b
                    left join prj2.businesslike b2
                        on b.id = b2.businessId
                    left join prj2.medicalcourse mc
                        on b.id = mc.medicalCourseId
                    left join prj2.businessholiday bh
                        on b.id = bh.businessId
            WHERE b.category = 'hospital'
                AND (b.name LIKE #{keyword} OR b.oldAddress LIKE #{keyword} OR mc.medicalCourseCategory LIKE #{keyword})
            GROUP BY b.id
            ORDER BY count(distinct b2.id) DESC
            """)
    List<Hs> selectByKeyword(String category, String keyword);

    @Select("""
            SELECT b.id,b.name,b.address,b.oldAddress,b.homePage,b.openHour,b.openMin,b.closeHour,b.closeMin,
                    b.restHour,b.restMin,b.restCloseHour,b.restCloseMin,
                   b.content,b.category,b.nightCare,b.phone,
                    COUNT(DISTINCT b2.id) countLike, mc.medicalCourseCategory, bh.holiday
            FROM prj2.business b
                left join prj2.businesslike b2
                    on b.id = b2.businessId
                left join prj2.medicalcourse mc
                    on b.id = mc.medicalCourseId
                left join prj2.businessholiday bh
                    on b.id = bh.businessId
            WHERE b.category = 'hospital'
                AND mc.medicalCourseCategory = #{course}
            GROUP BY b.id
            ORDER BY count(distinct b2.id) DESC
            """)
    List<Hs> selectByCategory(String course);

    @Insert("""
        INSERT INTO prj2.business(name,memberId,address,oldAddress,phone,openHour,openMin,restHour,restMin,restCloseHour,restCloseMin,closeHour,closeMin,info,content,category,nightCare,homePage)
        VALUES (#{name},#{memberId},#{address},#{oldAddress},#{phone},#{openHour},#{openMin},#{restHour},#{restMin},#{restCloseHour},#{restCloseMin},#{closeHour},#{closeMin},#{info},#{content},'hospital',#{nightCare},#{homePage})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Hs hs);

    @Update("""
        UPDATE prj2.business
        SET name = #{name},
        address = #{address},
        oldAddress = #{oldAddress},
        phone =#{phone},
        openHour = #{openHour},
        openMin = #{openMin},
        restHour = #{restHour},
        restMin = #{restMin},
        restCloseHour = #{restCloseHour},
        restCloseMin = #{restCloseMin},
        closeHour = #{closeHour},
        closeMin = #{closeMin},
        content = #{content},
        info = #{info},
        homePage = #{homePage},
        nightCare = #{nightCare}
        WHERE id = #{id}
        """)
    int update(Hs hs);

    @Select("""
            SELECT *
            FROM prj2.business 
            WHERE id = #{id}
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
        SELECT id,medicalCourseId,medicalCourseCategory
        FROM prj2.medicalcourse
        WHERE medicalCourseId =#{id}
        """)
    List<HsCourse> courseSelectByBusinessId(Integer id);

    @Delete("""
        DELETE FROM prj2.medicalcourse
        WHERE prj2.medicalcourse.medicalCourseId = #{id}
        """)
    void courseDeleteByBusinessId(Integer id);

    @Delete("""
        DELETE FROM businessholiday
        WHERE businessId = #{id}
        """)
    void holidayDeleteByBusinessId(Integer id);

    @Insert("""
        INSERT INTO businessholiday(businessId, holiday) 
        VALUES (#{id},#{holiday})
        """)
    void insertHoliday(Integer id, String holiday);

    @Select("""
        SELECT *
        FROM prj2.businessholiday
        WHERE businessId = #{id}
        """)
    List<BusinessHoliday> holidaySelectByBusinessId(Integer id);

    @Select("""
        SELECT *
        FROM business
        WHERE memberId = #{memberId}
        """)
    Hs selectBymemberId(String memberId);

    @Select("""
        SELECT id
        FROM business
        WHERE memberId = #{memberId}
        """)
    Integer selectIdByMemberId(String memberId);

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM business b
                JOIN prj2.medicalcourse m on b.id = m.medicalCourseId
                <where prefixOverrides="OR">
                    <if test="list == 'all' or list == 'name'">
                        OR b.name LIKE #{keyword}
                    </if>
                    <if test="list == 'all' or list == 'medicalCourseCategory'">
                        OR m.medicalCourseCategory LIKE #{keyword}
                    </if>
                    <if test="list == 'all' or list == 'address'">
                        OR b.address LIKE #{keyword}
                    </if>
                </where>
            </script>
            """)
    int countAll(String list, String keyword);

    @Select("""
            <script>
            SELECT 
                   b.id,
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
                   m.medicalCourseCategory,
                   COUNT(DISTINCT bl.id) `likeCount`,
                   COUNT(DISTINCT bc.id) `commentCount`
             FROM business b
                LEFT JOIN businesslike bl
                    ON b.id = bl.businessId
                LEFT JOIN businesscomment bc
                    ON b.id = bc.businessId
                LEFT JOIN businessholiday bh
                    ON b.id = bh.businessId
                LEFT JOIN medicalcourse m
                    ON b.id = m.medicalCourseId
            WHERE
                b.category = 'hospital' 
                <trim prefixOverrides="OR" prefix="AND (" suffix=")" >
                    <if test="list == 'all' or list == 'name'">
                        OR name LIKE #{keyword}
                    </if>
                    <if test="list == 'all' or list == 'medicalCourseCategory'">
                        OR medicalCourseCategory LIKE #{keyword}
                    </if>
                    <if test="list == 'all' or list == 'address'">
                        OR address LIKE #{keyword}
                    </if>
                </trim>
            GROUP BY b.id
            LIMIT #{from}, 10
            </script>
            """)
    List<Hs> selectByPagingById(int from, String list, String keyword);

    @Select("""
            SELECT * FROM medicalCourse
            WHERE id = #{medicalCourseId}
            """)
    List<HsCourse> courseSelectByCategory(String category);
}
