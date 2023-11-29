package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.DrugFile.DrugFile;
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
    List<DrugFile> selectNamesByDrugId(Integer id);
}
