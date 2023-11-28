package com.example.prj2be.mapper.ds;

import com.example.prj2be.domain.ds.Ds;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DsMapper {
    @Insert("""
            INSERT INTO business(name, address, phone, openHour, openMin, closeHour, closeMin, content, category, nightCare)
            VALUES (#{name}, #{address}, #{phone},
                    #{openHour}, #{openMin}, #{closeHour},
                    #{closeMin}, #{content},'drugStore', #{nightCare}
                    )
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
                content = #{content},
                nightCare = #{nightCare}
            WHERE id = #{id}
            """)
    int updateById(Ds ds);

    @Select("""
            SELECT * FROM business
            WHERE category = 'drugStore'
            """)
    List<Ds> selectByCategory( );

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
}
