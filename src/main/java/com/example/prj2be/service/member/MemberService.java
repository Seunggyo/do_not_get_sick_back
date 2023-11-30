package com.example.prj2be.service.member;

import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;

    private final S3Client s3;
    @Value("${image.file.prefix}")
    private String urlPrefix;
    @Value("${aws.s3.bucket.name}")
    private String bucket;

    public String getId(String id) {
        return mapper.selectIdById(id);
    }

    public String getNickName(String nickName) {
        return mapper.selectNickNameByNickName(nickName);
    }

    public String getEmail(String email) {
        return mapper.selectEmailByEmail(email);
    }

    public boolean validate(Member member, MultipartFile file) {

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
        if (member.getBirthday().isBlank()) {
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

        // 병원, 약국 계정 가입시 파일 체크
        if (member.getAuth().equals("hs") || member.getAuth().equals("ds")) {
            if (file == null) {
                return false;
            }
        }

        return true;
    }

    public boolean add(Member member,MultipartFile file) throws IOException {
        if (file != null) {
            upload(member.getId(), file);
            return mapper.insertMember(member, file.getOriginalFilename()) == 1;
        }
        return mapper.insertMember(member, "") == 1;
    }

    public void upload(String memberId, MultipartFile file) throws IOException {
        String key = "prj2/license/" + memberId + "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

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

    public boolean isAdmin(Member login) {
        if (login.getAuth() != null) {
            return login.getAuth().equals("admin");
        }
        return false;
    }

    public boolean isHs(Member login) {
        if (login.getAuth() != null) {
            return login.getAuth().equals("hs");
        }
        return false;
    }

    public boolean isDs(Member login) {
        if (login.getAuth() != null) {
            return login.getAuth().equals("ds");
        }
        return false;
    }

    public boolean hasAccess(String id, Member login) {
        if (isAdmin(login)) {
            return true;
        }
        return login.getId().equals(id);
    }

    public boolean update(Member member) {
        return mapper.update(member) == 1;
    }
}
