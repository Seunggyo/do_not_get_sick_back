package com.example.prj2be.drugStore.mapper;

import com.example.prj2be.drugStore.domain.Ds;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DsMapper {
    @Insert("""
            INSERT INTO business
            VALUES (#{name}, #{address}, #{phone}, #{openHour}, #{openMin}, #{closeHour}, #{closeMin}, #{content})
            """)
    int insertById(Ds ds);

    @Update("""
            UPDATE business
            SET name = #{name},
                address = #{address},
                phone = #{phone},
                openHour = #{openHour},
                openMin = #{openMin},
                closeHour = #{closeHour},
                closeMin = #{closeMin},
                content = #{content}
            """)
    int updateById(Ds ds);
}
