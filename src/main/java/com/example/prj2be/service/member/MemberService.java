package com.example.prj2be.service.member;

import com.example.prj2be.domain.board.Board;
import com.example.prj2be.domain.board.BoardComment;
import com.example.prj2be.domain.cs_qa.CustomerQA;
import com.example.prj2be.domain.cs_qa.CustomerService;
import com.example.prj2be.domain.drug.DrugComment;
import com.example.prj2be.domain.ds.Ds;
import com.example.prj2be.domain.ds.DsComment;
import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.board.BoardCommentMapper;
import com.example.prj2be.mapper.board.BoardFileMapper;
import com.example.prj2be.mapper.board.BoardLikeMapper;
import com.example.prj2be.mapper.board.BoardMapper;
import com.example.prj2be.mapper.business.BusinessLikeMapper;
import com.example.prj2be.mapper.cs_qa.CSMapper;
import com.example.prj2be.mapper.cs_qa.QAMapper;
import com.example.prj2be.mapper.drug.CartMapper;
import com.example.prj2be.mapper.drug.DrugCommentMapper;
import com.example.prj2be.mapper.drug.DrugLikeMapper;
import com.example.prj2be.mapper.ds.DsCommentMapper;
import com.example.prj2be.mapper.ds.DsMapper;
import com.example.prj2be.mapper.hs.HsMapper;
import com.example.prj2be.mapper.member.MemberJoinMapper;
import com.example.prj2be.mapper.member.MemberMapper;
import com.example.prj2be.mapper.order.OrderListMapper;
import com.example.prj2be.mapper.order.OrderWaitMapper;
import com.example.prj2be.mapper.order.OrdersMapper;
import com.example.prj2be.mapper.payment.PaymentMapper;
import com.example.prj2be.service.cs_qa.CSService;
import com.example.prj2be.service.cs_qa.QAService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class MemberService {

    private final MemberMapper mapper;
    private final MemberJoinMapper memberJoinMapper;
    private final BoardMapper boardMapper;
    private final BoardCommentMapper boardCommentMapper;
    private final DsCommentMapper dsCommentMapper;
    private final DrugCommentMapper drugCommentMapper;

    private final BoardLikeMapper boardLikeMapper;
    private final BusinessLikeMapper businessLikeMapper;
    private final DrugLikeMapper drugLikeMapper;


    // 작성 글 삭제 보드 약국 병원 드러그 qa cs
    private final DsMapper dsMapper;
    private final HsMapper hsMapper;
    private final QAMapper qaMapper;
    private final CSMapper csMapper;

    private final QAService qaService;
    private final CSService csService;

    private final BoardFileMapper boardFileMapper;

    // buy, drugCart, orderList, orders, orderWait, payment
    private final CartMapper cartMapper;
    private final OrderListMapper orderListMapper;
    private final OrdersMapper ordersMapper;
    private final OrderWaitMapper orderWaitMapper;
    private final PaymentMapper paymentMapper;


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

    public boolean add(Member member, MultipartFile file, MultipartFile profile)
        throws IOException {

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
                String profileFilename = (profile != null) ? profile.getOriginalFilename()
                    : "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
                return
                    memberJoinMapper.insertMember(member, file.getOriginalFilename(),
                        profileFilename)
                        == 1;
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

    public Map<String, Object> selectById(String id) {
        Map<String, Object> map = new HashMap<>();
        Member member = mapper.selectById(id);

        String profileUrl =
            urlPrefix + "prj2/profile/" + member.getId() + "/" + member.getProfile();
        member.setProfile(profileUrl);
        map.put("member", member);
        map.put("boardList", boardMapper.selectByMemberId(id));
        map.put("commentList", boardCommentMapper.selectByMemberId(id));
        return map;
    }

    public Member selectById1(String id) {
        Member member = mapper.selectById(id);

        String profileUrl =
            urlPrefix + "prj2/profile/" + member.getId() + "/" + member.getProfile();
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
                    String profileUrl = urlPrefix + "prj2/profile/" + dbMember.getId() + "/"
                        + dbMember.getProfile();
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
        MultipartFile file) throws IOException {

        if (removeFileIds != null) {
            for (Integer id : removeFileIds) {

                // s3 에서 지우기
                String key = "prj2/profile/" + member.getId() + "/" + profile.getOriginalFilename();
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
                s3.deleteObject(objectRequest);

//            mapper.deleteById(id);
            }
        }

        // 프로필 파일 삭제하고 추가하기
        if (profile != null) {

            Member member1 = mapper.selectById(member.getId());
            if (member1.getProfile() != null && !member1.getProfile().isBlank()) {

                // 프로필 파일 삭제하기
                String deleteKey =
                    "prj2/profile/" + member.getId() + "/" + profile.getOriginalFilename();
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
                String profileUrl =
                    urlPrefix + "prj2/profile/" + dbMember.getId() + "/" + dbMember.getProfile();
                dbMember.setProfile(profileUrl);
            }

            request.setAttribute("login", dbMember, RequestAttributes.SCOPE_SESSION);
        }

    }

    public boolean remove(String id) {
        //보드 비즈니스, 드러그 코멘트 삭제
        List<BoardComment> boardCommentList = boardCommentMapper.selectByMemberId(id);
        for (BoardComment comment : boardCommentList) {
            boardCommentMapper.deleteById(comment.getId());
        }
        List<DsComment> dsCommentList = dsCommentMapper.selectByMemberId(id);
        for (DsComment comment : dsCommentList) {
            dsCommentMapper.deleteById(comment.getId());
        }
        List<DrugComment> drugCommentList = drugCommentMapper.selectByMemberId(id);
        for (DrugComment comment : drugCommentList) {
            drugCommentMapper.deleteById(comment.getId());
        }

        //좋아요 삭제
        boardLikeMapper.deleteByMemberId(id);
        businessLikeMapper.deleteByMemberId(id);
        drugLikeMapper.deleteByMemberId(id);

        //qa 삭제

        // 작성 글 삭제 보드 약국 병원 드러그 qa cs // drug는 일부러 삭제안함 (위험)
        List<Board> boardList = boardMapper.selectByMemberId(id);
        for (Board board : boardList) {
            boardFileMapper.deleteByFileId(board.getId());
            boardMapper.deleteById(board.getId());
        }
        Ds ds = dsMapper.selectByName(id);
        if (ds != null) {
            dsMapper.deleteHolidayByDsId(ds.getId());
            dsMapper.deleteById(ds.getId());
        }
        Hs hs = hsMapper.selectBymemberId(id);
        if (hs != null) {
            hsMapper.holidayDeleteByBusinessId(hs.getId());
            hsMapper.deleteById(hs.getId());
        }

        List<CustomerQA> qaList = qaMapper.selectByMemberId(id);
        for (CustomerQA qa : qaList) {
            qaService.remove(qa.getId());
        }
        List<CustomerService> csList = csMapper.selectByMemberId(id);
        for (CustomerService cs : csList) {
            csService.remove(cs.getId());
        }

        // buy, drugCart, orderList, orders, orderWait, payment
        cartMapper.deleteByMemberId(id);
        cartMapper.deleteBuyByMemberId(id);
        orderListMapper.deleteByMemberId(id);
        ordersMapper.deleteByMemberId(id);
        orderWaitMapper.deleteByMemberId(id);
        paymentMapper.deleteByMemberId(id);

        //탈퇴
        Member member = mapper.selectById(id);
        String delProfile = "prj2/profile" + id + "/" + member.getProfile();
        String delFile = "prj2/license" + id + "/" + member.getFileName();
        List<String> delList = List.of(delProfile, delFile);

        for (String key : delList) {

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
            s3.deleteObject(deleteObjectRequest);
        }
        return mapper.deleteById(id) == 1;


    }

    public boolean isAccess(String id, Member login) {
        if (login == null) {
            return false;
        }
        if (login.getAuth().equals("admin")) {
            return true;
        }
        return login.getId().equals(id);
    }

//   private final CartMapper cartMapper;
//   private final OrderListMapper orderListMapper;
//   private final OrdersMapper ordersMapper;
//   private final OrderWaitMapper orderWaitMapper;


}
