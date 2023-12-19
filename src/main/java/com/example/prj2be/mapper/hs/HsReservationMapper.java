package com.example.prj2be.mapper.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.domain.hs.HsReservation;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface HsReservationMapper {

    @Select("""
        SELECT id,openHour,openMin,closeHour,closeMin,restHour,restMin,restCloseHour,restCloseMin
        FROM prj2.business
        WHERE business.id = #{id}
        """)
    Hs selectByBusinessId(Integer id);

    @Insert("""
        INSERT INTO prj2.businessreservation(businessId, memberId, reservationDate, reservationHour, reservationMin, comment,isReservationCheck)
        VALUES (#{businessId},#{memberId},#{reservationDate},#{reservationHour},#{reservationMin},#{comment},false)
        """)
    int insert(HsReservation reservation);

    @Delete("""
        DELETE FROM prj2.businessreservation
        WHERE businessId = #{id}
        """)
    void deleteByBusinessId(Integer id);

    @Select("""
        SELECT *
        FROM prj2.businessreservation
        WHERE id = #{id}
        """)
    HsReservation selectById(Integer id);

    @Delete("""
        DELETE FROM prj2.businessreservation
        WHERE id = #{id}
        """)
    int remove(Integer id);

    @Select("""
        SELECT r.memberId, r.reservationDate, r.reservationHour, r.reservationMin,r.id,b.name,r.comment,r.isReservationCheck,m.nickName
        FROM prj2.businessreservation r JOIN prj2.business b on b.id = r.businessId JOIN prj2.member m on m.id = r.memberId
        WHERE r.memberId =#{memberId} AND r.isReservationCheck = false
        ORDER BY r.reservationDate
        """)
    List<HsReservation> selectByMemberId(String memberId);

    @Select("""
        SELECT r.memberId, r.reservationDate, r.reservationHour, r.reservationMin, r.id,b.name, r.comment, r.isReservationCheck
        FROM prj2.businessreservation r JOIN prj2.business b on b.id = r.businessId
        WHERE r.memberId = #{memberId} AND r.isReservationCheck = true
        ORDER BY r.reservationDate
        """)
    List<HsReservation> selectByCheckMemberId(String memberId);

    @Select("""
        SELECT m.nickName, r.reservationDate, r.reservationHour, r.reservationMin, r.id, r.comment, r.isReservationCheck
        FROM prj2.businessreservation r JOIN prj2.member m on m.id = r.memberId
        WHERE r.businessId = #{id}
        ORDER BY r.reservationDate
        """)
    List<HsReservation> selectByBusinessMemberId(Integer id);


    @Select("""
        SELECT id, name
        FROM prj2.business
        WHERE memberId = #{memberId}
        AND category = 'hospital'
        """)
    List<Hs> selectByMemberIdBList(String memberId);

    @Update("""
        UPDATE prj2.businessreservation
        SET isReservationCheck = true
        WHERE id = #{reservationId}
        """)
    Integer updateByReservationId(Integer reservationId);

    @Select("""
        SELECT m.nickName, r.reservationDate, r.reservationHour, r.reservationMin, r.id, r.comment, r.isReservationCheck
        From businessreservation r JOIN prj2.member m on m.id = r.memberId
        WHERE businessId = #{businessId} AND isReservationCheck = TRUE
        ORDER BY r.reservationDate
        """)
    List<HsReservation> bIdCheckGet(Integer businessId);

    @Select("""
        SELECT r.reservationDate, COUNT(*) people
        FROM businessreservation r
        WHERE businessId = #{businessId}
        GROUP BY r.reservationDate
        ORDER BY r.reservationDate
        """)
    List<HsReservation> dayCheckFromBid(Integer businessId);

    @Select("""
        <script>
        SELECT r.id, r.businessId, r.memberId, r.reservationDate, r.reservationHour, r.reservationMin, r.isReservationCheck, m.nickName, m.phone
        FROM businessreservation r JOIN member m on m.id = r.memberId
        WHERE
        <choose>
        <when test="start != null and end != null">
        <![CDATA[
        r.reservationDate >= #{start}
        AND r.reservationDate < #{end}
        AND r.businessId = #{businessId}
        ]]>
        </when>
        <otherwise>
        r.businessId = #{businessId}
        </otherwise>
        </choose>
        </script>
        """)
    List<HsReservation> monthCheck(String businessId, String start, String end);
}
