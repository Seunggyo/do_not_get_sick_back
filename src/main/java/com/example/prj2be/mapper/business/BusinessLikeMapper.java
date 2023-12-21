package com.example.prj2be.mapper.business;

import com.example.prj2be.domain.ds.DsLike;
import com.example.prj2be.domain.member.Member;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface BusinessLikeMapper {
    @Delete("""
            DELETE FROM businesslike
            WHERE memberId = #{memberId}
            AND businessId = #{businessId}
            """)
    int delete(DsLike like);

    @Delete("""
            DELETE FROM businesslike
            WHERE businessId = #{id}
         
            """)
    int deleteById(Integer id);

    @Insert("""
            INSERT INTO businesslike (memberId, businessId)
            VALUES (#{memberId}, #{businessId})
            """)
    int insert(DsLike like);

    @Select("""
            SELECT COUNT(id)
            FROM businesslike
            WHERE businessId = #{businessId}
            """)
    int countByMemberId(Integer businessId);

    @Select("""
            SELECT * FROM businesslike
            WHERE businessId = #{businessId}
              AND memberId = #{memberId}
            """)
    DsLike selectByDsIdAndMemberId(Integer businessId, String memberId);

    @Select("""
            SELECT * FROM businesslike
            WHERE memberId = #{memberId}
            """)
    Map<String, Object> selectByName(String name);

    @Select("""
        select id from business
        where name = #{name}
""")
    Integer getIdByName(String name);

    @Delete("""
        delete from businessLike
        where memberId = #{memberID}
""")
    void deleteByMemberId(String memberId);
}
