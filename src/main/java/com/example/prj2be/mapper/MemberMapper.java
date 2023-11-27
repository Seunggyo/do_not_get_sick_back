package com.example.prj2be.mapper;

import com.example.prj2be.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MemberMapper {

    @Select("""
        select id from member
        where id = #{id}
""")
    String selectIdById(String id);

    @Select("""
        select nickName from member
        where nickName = #{nickName}
""")
    String selectNickNameByNickName(String nickName);

    @Select("""
        select email from member
        where email = #{email}
""")
    String selectEmailByEmail(String email);

    @Insert("""
        insert into member (
        id, password, nickName, phone,
        email, address, auth)
        values (
        #{id}, #{password}, #{nickName}, #{phone},
        #{email}, #{address}, #{auth})
""")
    int insertMember(Member member);

    @Select("""
        select * from member
        order by inserted
""")
    List<Member> selectAll();

    @Select("""
        select * from member
        where id = #{id}
""")
    Member selectById(String id);

    @Select(("""
        select auth from member
        where id = #{id}
"""))
    String selectAuthById(String id);

    @Update("""
        update member
        set nickName=#{nickName}, phone=#{phone},
        email=#{email}, address=#{address}
        where id = #{id}
""")
    int update(Member member);
}
