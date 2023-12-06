package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.DrugFile.DrugFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("""
            INSERT INTO drugFile (drugId, name)
            VALUES (#{drugId}, #{name})
            """)
    int insert(Integer drugId, String name);

    @Select("""
            SELECT id, name
            FROM drugFile
            WHERE drugId = #{drigId}
            """)
    List<DrugFile> selectNamesByDrugId(Integer drugId);

    @Delete("""
            DELETE FROM drugFile
            WHERE drugId = #{drugId}
            """)
    int deleteByBoardId(Integer drugId);
    
    @Select("""
            SELECT *
            FROM drugFile
            WHERE id = #{id}
            """)
    DrugFile selectById(Integer id);

    @Delete("""
            DELETE FROM drugFile
            WHERE id = #{id}
            """)
    void deleteById(Integer id);
}
