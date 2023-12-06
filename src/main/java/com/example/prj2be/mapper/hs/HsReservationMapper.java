package com.example.prj2be.mapper.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.domain.hs.HsReservation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HsReservationMapper {

    @Select("""
        SELECT id,openHour,openMin,closeHour,closeMin,restHour,restMin,restCloseHour,restCloseMin
        FROM prj2.business
        WHERE business.id = #{id}
        """)
    Hs selectByBusinessId(Integer id);

    @Insert("""
        INSERT INTO prj2.businessreservation(businessId, memberId, reservationDate, reservationHour, reservationMin, comment)
        VALUES (#{businessId},#{memberId},#{reservationDate},#{reservationHour},#{reservationMin},#{comment})
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
}
