package com.example.prj2be.mapper.hs;

import com.example.prj2be.domain.hs.Hs;
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
}
