package com.example.prj2be.mapper.member;

import com.example.prj2be.domain.member.Member;
import org.apache.ibatis.annotations.*;

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
        id, password, nickName, birthday, phone,
        email, address, auth, fileName, profile)
        values (
        #{member.id}, #{member.password}, #{member.nickName}, #{member.birthday},
        #{member.phone}, #{member.email}, #{member.address},
        #{member.auth}, #{fileName}, #{profile})
""")
    int insertMember(Member member, String fileName, String profile);

    @Select("""
        select * from member
        where id like #{keyword}
        order by inserted
        limit #{from}, 10
""")
    List<Member> selectAll(Integer from, String keyword);

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
        birthday=#{birthday}, address=#{address}
        where id = #{id}
""")
    int update(Member member);

    @Select("""
        select id from member
        where email = #{email}
""")
    String findIdByEmail(String email);

    @Insert("""
        insert into member (
        id, password, nickName, birthday, phone,
        email, address, auth, fileName, profile)
        values (
        #{id}, #{password}, #{nickName}, #{birthday},
        #{phone}, #{email}, #{address},
        #{auth}, #{fileName}, #{profile})
""")
    int acceptMember(Member member);


    @Select("""
        select count(*) from member
        where id like #{id}
""")
    int countAll(String id);

    @Delete("""
       SELECT *
       FROM member
       WHERE id = #{id}
       """)
    void deleteById(Integer id);


    @Select("""
       SELECT profile
       FROM member
       WHERE id = #{id}
       """)
    String getFileNameById(Integer id);

    void insert(String id, String originalFilename);
}
