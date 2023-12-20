package com.example.prj2be.service.member;

import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.member.MemberJoinMapper;
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
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

   private final MemberMapper mapper;
   private final MemberJoinMapper memberJoinMapper;

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
      if (member.getAuth().equals("user")) {
         if (member.getBirthday().isBlank()) {
            return false;
         }
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

   public boolean add(Member member, MultipartFile file, MultipartFile profile) throws IOException {

      if (member.getAuth().equals("user") || member.getAuth().equals("admin")) {
         if (profile != null) {
            uploadProfile(member.getId(), profile);
            return mapper.insertMember(member, "", profile.getOriginalFilename()) == 1;
         } else {
            return mapper.insertMember(member, "", "") == 1;
         }
      }
      if (member.getAuth().equals("hs") || member.getAuth().equals("ds")) {
         if (file != null) {
            upload(member.getId(), file);
            return memberJoinMapper.insertMember(member, file.getOriginalFilename(), profile.getOriginalFilename()) == 1;
         }
      }

      return false;
   }

   private void uploadProfile(String memberId, MultipartFile profile) throws IOException {
      String key = "prj2/profile/" + memberId + "/" + profile.getOriginalFilename();

      PutObjectRequest objectRequest = PutObjectRequest.builder()
         .bucket(bucket)
         .key(key)
         .acl(ObjectCannedACL.PUBLIC_READ)
         .build();

      s3.putObject(objectRequest,
         RequestBody.fromInputStream(profile.getInputStream(), profile.getSize()));
   }

   public void upload(String memberId, MultipartFile file) throws IOException {
      String key = "prj2/license/" + memberId + "/" + file.getOriginalFilename();

      PutObjectRequest objectRequest = PutObjectRequest.builder()
         .bucket(bucket)
         .key(key)
         .acl(ObjectCannedACL.PUBLIC_READ)
         .build();

      s3.putObject(objectRequest,
         RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

   }

   public Map<String, Object> selectAll(String keyword, Integer page) {
      Map<String, Object> map = new HashMap<>();
      Map<String, Object> pageInfo = new HashMap<>();

      int countAll = mapper.countAll("%" + keyword + "%");
      int lastPageNumber = (countAll - 1) / 10 + 1;
      int startPageNumber = (page - 1) / 10 * 10 + 1;
      int endPageNumber = startPageNumber + 9;
      endPageNumber = Math.min(endPageNumber, lastPageNumber);
      int prevPageNumber = startPageNumber - 10;
      int nextPageNumber = endPageNumber + 1;

      pageInfo.put("currentPageNumber", page);
      pageInfo.put("startPageNumber", startPageNumber);
      pageInfo.put("endPageNumber", endPageNumber);
      if (prevPageNumber > 0) {
         pageInfo.put("prevPageNumber", prevPageNumber);
      }
      if (nextPageNumber <= lastPageNumber) {
         pageInfo.put("nextPageNumber", nextPageNumber);
      }

      int from = (page - 1) * 10;
      map.put("pageInfo", pageInfo);
      map.put("memberList", mapper.selectAll(from, "%" + keyword + "%"));
      return map;
   }

   public Map<String, Object> selectJoinAll(String keyword, Integer page) {
      Map<String, Object> map = new HashMap<>();
      Map<String, Object> pageInfo = new HashMap<>();

      int countAll = memberJoinMapper.countAll("%" + keyword + "%");
      int lastPageNumber = (countAll - 1) / 10 + 1;
      int startPageNumber = (page - 1) / 10 * 10 + 1;
      int endPageNumber = startPageNumber + 9;
      endPageNumber = Math.min(endPageNumber, lastPageNumber);
      int prevPageNumber = startPageNumber - 10;
      int nextPageNumber = endPageNumber + 1;

      pageInfo.put("currentPageNumber", page);
      pageInfo.put("startPageNumber", startPageNumber);
      pageInfo.put("endPageNumber", endPageNumber);
      if (prevPageNumber > 0) {
         pageInfo.put("prevPageNumber", prevPageNumber);
      }
      if (nextPageNumber <= lastPageNumber) {
         pageInfo.put("nextPageNumber", nextPageNumber);
      }

      int from = (page - 1) * 10;
      map.put("pageInfo", pageInfo);
      map.put("memberList", memberJoinMapper.selectAll(from, "%" + keyword + "%"));
      return map;
   }

   public Member selectById(String id) {
      Member member = mapper.selectById(id);

      String profileUrl = urlPrefix + "prj2/profile/" + member.getId() + "/" + member.getProfile();
      member.setProfile(profileUrl);
      return member;
   }

   public boolean login(Member member, WebRequest request) {
      Member dbMember = mapper.selectById(member.getId());

      if (member != null) {
         if (dbMember.getPassword().equals(member.getPassword())) {
            String auth = mapper.selectAuthById(member.getId());
            dbMember.setAuth(auth);
            dbMember.setPassword("");

            if (dbMember.getProfile() != null && !dbMember.getProfile().isBlank()) {
               String profileUrl = urlPrefix + "prj2/profile/" + dbMember.getId() + "/" + dbMember.getProfile();
               dbMember.setProfile(profileUrl);
            }

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

   public boolean update(Member member, List<Integer> removeFileIds, MultipartFile profile,
      MultipartFile file) throws IOException{
      // TODO: file에 대한 코드가 이미 없음...
      // 아마 병원멤버와 약국 멤버에 관한 코드인 것 같은데...
      // 일단 profile이 동작하도록 코드를 작성할 테니
      // 병원멤버와 약국멤버에 대한 코드를 이후에 수정해야할 것 같아요...



      if (removeFileIds != null) {
         for (Integer id : removeFileIds) {

            // s3 에서 지우기
            String key = "prj2/profile/" + member.getId() + "/" + profile.getOriginalFilename();
            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
               .bucket(bucket)
               .key(key)
               .build();
            s3.deleteObject(objectRequest);

            mapper.deleteById(id);
         }
      }

      // 프로필 파일 삭제하고 추가하기
      if (profile != null ) {

         Member member1 = mapper.selectById(member.getId());
         if (member1.getProfile() != null && !member1.getProfile().isBlank()) {

            // 프로필 파일 삭제하기
            String deleteKey = "prj2/profile/" + member.getId() + "/" + profile.getOriginalFilename();
            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
               .bucket(bucket)
               .key(deleteKey)
               .build();
            s3.deleteObject(objectRequest);
         }


         // 프로필 파일 추가하기
         // s3에 올리기
         String key = "prj2/profile/" + member.getId() + "/" + profile.getOriginalFilename();

         PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build();

         s3.putObject(objectRequest,
            RequestBody.fromInputStream(profile.getInputStream(), profile.getSize()));

         // db에 파일 추가하기
         mapper.updateProfile(member.getId(), profile.getOriginalFilename());
      }

      return mapper.update(member) == 1;
   }

   public String findIdByEmail(String email) {
      return mapper.findIdByEmail(email);
   }

   public boolean accept(Member member) {
      memberJoinMapper.deleteById(member.getId());
      return mapper.acceptMember(member) == 1;
   }

   public boolean cancel(Member member) {
      return memberJoinMapper.deleteById(member.getId()) == 1;
   }


   public void loginUpdate(Member member, WebRequest request) {
      Member dbMember = mapper.selectById(member.getId());

      if (member != null) {
         String auth = mapper.selectAuthById(member.getId());
         dbMember.setAuth(auth);
         dbMember.setPassword("");

         if (dbMember.getProfile() != null && !dbMember.getProfile().isBlank()) {
            String profileUrl = urlPrefix + "prj2/profile/" + dbMember.getId() + "/" + dbMember.getProfile();
            dbMember.setProfile(profileUrl);
         }

         request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
      }

   }
}
