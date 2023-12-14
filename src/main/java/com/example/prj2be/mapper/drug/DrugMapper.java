package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Drug;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DrugMapper {

    @Select("""
            select id, name, function func, content, price, inserted, shipping from drug
            where function=#{function}
                """)
    List<Drug> selectByFunction(String function);

    @Insert("""
            INSERT INTO drug (name, function, content, price, shipping)
            VALUES (#{name}, #{func},#{content},#{price}, #{shipping})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Drug drug);


    @Select("""
            <script>
            select DISTINCT d.id, d.name, d.function func, d.content, d.price, d.inserted, d.shipping
            FROM drug d
                JOIN drugFile f
                    ON d.id = f.drugId
                LEFT JOIN drugLike l
                    ON l.drugId = d.id
            WHERE 
            <trim prefixOverrides="OR">
                    <if test="category == 'all' or category == 'name'">
                        OR d.name LIKE #{keyword}
                    </if>
                    <if test="category == 'all' or category == 'function'">
                        OR d.function LIKE #{keyword}
                    </if>
                </trim>
            GROUP BY d.id
            ORDER BY COUNT(distinct l.memberId) DESC 
            LIMIT #{from}, 6
            </script>
            """)
    List<Drug> selectDrugList(Integer from, String keyword, String category);

    @Select("""
            select d.id, d.name, d.function func, d.content, d.price, d.inserted, d.shipping
            FROM drug d
            WHERE d.id = #{id}
            """)
    Drug selectById(Integer id);

    @Delete("""
            DELETE FROM drug
            WHERE id = #{id}
            """)
    int deleteById(Integer id);

    @Update("""
            UPDATE drug
            SET 
            name = #{name},
            function = #{func},
            content = #{content},
            price = #{price},
            shipping = #{shipping}
            WHERE id = #{id}
            """)
    int update(Drug drug);

    @Select("""
            select DISTINCT d.id, d.name, d.function func, d.content, d.price, d.inserted, d.shipping
            FROM drug d
            JOIN drugFile f
            ON d.id = f.drugId
            where function = #{func}
            ORDER BY d.id DESC 
            LIMIT #{from}, 6
            """)
    List<Drug> selectDrugListByFunc(int from, String func);

    @Select("""
    <script>
    SELECT COUNT(*) FROM drug d
    WHERE 
        <trim prefixOverrides="OR">
            <if test="category == 'all' or category == 'name'">
                OR d.name LIKE #{keyword}
            </if>
            <if test="category == 'all' or category == 'function'">
                OR d.function LIKE #{keyword}
            </if>
        </trim>
    </script>
    """)

    int countAll(String keyword, String category);
}
