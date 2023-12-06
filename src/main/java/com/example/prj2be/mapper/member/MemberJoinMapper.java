package com.example.prj2be.mapper.member;

import com.example.prj2be.domain.member.Member;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberJoinMapper {

    @Insert("""
        insert into memberJoin (
        id, password, nickName, phone,
        email, address, auth, fileName)
        values (
        #{member.id}, #{member.password}, #{member.nickName},
        #{member.phone}, #{member.email}, #{member.address},
        #{member.auth}, #{fileName})
""")
    int insertMember(Member member, String fileName);

    @Select("""
        select * from memberJoin
        where id like #{keyword}
        order by inserted
        limit #{from}, 10
""")
    List<Member> selectAll(Integer from, String keyword);

    @Delete("""
        delete from memberJoin
        where id = #{id}
""")
    int deleteById(String id);

    @Select("""
        select count(*) from memberJoin
        where id like #{keword}
""")
    int countAll(String keyword);
}
