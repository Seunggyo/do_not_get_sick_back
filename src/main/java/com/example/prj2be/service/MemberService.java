package com.example.prj2be.service;

import com.example.prj2be.domain.Member;
import com.example.prj2be.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;

    public String getId(String id) {
        return mapper.selectIdById(id);
    }

    public String getNickName(String nickName) {
        return mapper.selectNickNameByNickName(nickName);
    }

    public String getEmail(String email) {
        return mapper.selectEmailByEmail(email);
    }

    public boolean validate(Member member) {

        if (member == null) {
            return false;
        }
        if (member.getId().isBlank()) {
            return false;
        }
        if (member.getPassword().isBlank()) {
            return false;
        }
        if (member.getNickName().isBlank()) {
            return false;
        }
        if (member.getPhone().isBlank()) {
            return false;
        }
        if (member.getEmail().isBlank()) {
            return false;
        }
        if (member.getAddress().isBlank()) {
            return false;
        }
        if (member.getAuth().isBlank()) {
            return false;
        }
        return true;
    }

    public boolean add(Member member) {
        return mapper.insertMember(member) == 1;
    }

    public List<Member> selectAll() {
        return mapper.selectAll();
    }

    public Member selectById(String id) {
        return mapper.selectById(id);
    }

    public boolean login(Member member, WebRequest request) {
        Member dbMember = mapper.selectById(member.getId());

        if (member != null) {
            if (dbMember.getPassword().equals(member.getPassword())) {
                String auth = mapper.selectAuthById(member.getId());
                dbMember.setAuth(auth);
                dbMember.setPassword("");
                request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
                return true;
            }
        }

        return false;
    }
}
