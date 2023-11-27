package com.example.prj2be.mapper;

import com.example.prj2be.domain.Ds;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
                content = #{content}
            """)
    int updateById(Ds ds);

    @Select("""
            SELECT * FROM business
            WHERE category = 'drugStore'
            """)
    List<Ds> selectByCategory( );

    @Select("""
            SELECT id, name, address, phone, openHour, openMin, closeHour, closeMin, content, category, nightCare
            FROM business
            WHERE id = #{id};
            """)
    Ds selectById(Integer id);
}
