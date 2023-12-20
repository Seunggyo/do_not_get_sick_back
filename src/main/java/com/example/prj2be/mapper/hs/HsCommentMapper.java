package com.example.prj2be.mapper.hs;

import com.example.prj2be.domain.hs.HsComment;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface HsCommentMapper {

    @Insert("""
        INSERT INTO prj2.businesscomment(memberId,businessId,comment) 
        VALUES (#{memberId},#{businessId},#{comment})
        """)
    int insert(HsComment comment);

    @Select("""
        SELECT bc.id,
        bc.businessId,
        bc.comment,
        bc.inserted,
        bc.memberId,
        m.nickName memberNickName
        FROM prj2.businesscomment bc JOIN prj2.member m on bc.memberId = m.id
        WHERE businessId = #{businessId}
        """)
    List<HsComment> selectByBusinessId(Integer businessId);

    @Select("""
        SELECT *
        FROM businesscomment
        WHERE id = #{id}
        """)
    HsComment selectById(Integer id);

    @Update("""
        UPDATE prj2.businesscomment
        SET comment = #{comment}
        WHERE id = #{id}
        """)
    boolean update(HsComment comment);

    @Delete("""
        DELETE FROM businesscomment
        WHERE id = #{id}
        """)
    int deleteById(Integer id);

    @Delete("""
        DELETE FROM businesscomment
        WHERE businessId = #{id}
        """)
    int deleteByBusinessId(Integer id);
}
