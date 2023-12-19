package com.example.prj2be.service.ds;

import com.example.prj2be.domain.business.BusinessHoliday;
import com.example.prj2be.domain.ds.Ds;
import com.example.prj2be.domain.ds.DsKakao;
import com.example.prj2be.domain.ds.DsPicture;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.business.BusinessLikeMapper;
import com.example.prj2be.mapper.business.BusinessPictureMapper;
import com.example.prj2be.mapper.ds.DsCommentMapper;
import com.example.prj2be.mapper.ds.DsMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
@RequiredArgsConstructor
public class DsService {

    private final DsMapper mapper;
    private final BusinessPictureMapper businessFileMapper;
    private final DsCommentMapper dsCommentMapper;
    private final BusinessLikeMapper businessLikeMapper;
    private final S3Client s3;

    @Value("${image.file.prefix}")
    private String urlPrefix;
    @Value("${aws.s3.bucket.name}")
    private String bucket;


    public boolean validate(Ds ds) {
        // 약국 기입시 필수적으로 적어야 하는 목록

        if (ds == null) {
            return false;
        }
        if (ds.getName().isBlank()) {
            return false;
        }
        if (ds.getAddress().isBlank()) {
            return false;
        }
        if (ds.getPhone().isBlank()) {
            return false;
        }

        return true;
    }

    public boolean save(Ds ds, MultipartFile[] files, Member login, String[] holidays)
        throws IOException {
        // 올바르게 접근한 사용자가 정보 저장 시 db로 정보 보내는 코드

        ds.setMemberId(login.getId());

        int cnt = mapper.insert(ds);

        if (holidays != null) {
            for (String holiday : holidays) {
                mapper.insertHoliday(ds.getId(), holiday);
            }
        }

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                businessFileMapper.insert(ds.getId(), files[i].getOriginalFilename());

                upload(ds.getId(), files[i]);
            }
        }

        return cnt == 1;
    }

    private void upload(Integer dsId, MultipartFile file) throws IOException {
//         aws 저장 코드
        String key = "prj2/Ds/" + dsId + "/" + file.getOriginalFilename();
        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build();

        s3.putObject(objectRequest,
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

    }

    public boolean update(Ds ds, MultipartFile[] uploadFile, List<Integer> deleteFileIds,
        String[] holidays) throws IOException {
        // 유저가 정보 수정 할려 할 떄 보내는 코드

        // 파일 삭제
        if (deleteFileIds != null) {
            for (Integer id : deleteFileIds) {
                DsPicture picture = businessFileMapper.selectById(id);
                String key = "prj2/Ds/" + ds.getId() + "/" + picture.getName();
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
                s3.deleteObject(objectRequest);

                // db에서 삭제
                businessFileMapper.deleteById(id);
            }
        }

        // 파일 업데이트
        if (uploadFile != null) {
            // 미리 작성한 upload를 사용하여 aws 저장
            for (MultipartFile file : uploadFile) {
                upload(ds.getId(), file);
                // db에 추가
                businessFileMapper.insert(ds.getId(), file.getOriginalFilename());
            }
        }

//        System.out.println("Arrays.toString(holidays) = " + Arrays.toString(holidays));
        //    업데이트를 하는것이 아니라 기존 데이터를 삭제한 후 다시 삽입 하는 식으로 코드 구성

        mapper.deleteHolidayByDsId(ds.getId());
        if (holidays != null) {
            for (String holiday : holidays) {
                mapper.insertHoliday(ds.getId(), holiday);
//                mapper.updateByHoliday(ds.getId(), holiday);
            }
        }

        return mapper.updateById(ds) == 1;
    }

    public Map<String, Object> list(Integer page, String keyword) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        // 현재 페이지
        int from = (page - 1) * 10;
        // 총 게시글이 몇개 인지, 어떤 키워드로 검색할 껏인지 등등 총 게시물에서 하는 키워드
        int countAll = mapper.countAll("%" + keyword + "%");

        int lastPageNumber = (countAll - 1) / 10 + 1;
        int startPageNumber = ((page - 1) / 10 * 10) + 1;
        int endPageNumber = startPageNumber + 9;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);

        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);

        // view안에 있는 사진리스트를 리스트 안에 사진 받아 오는 방법
        List<Ds> dsList = mapper.selectAllByCategory(from, "%" + keyword + "%");

        for (Ds ds : dsList) {
            List<DsPicture> dsPictures = businessFileMapper.selectNamesByDsId(ds.getId());

            for (DsPicture dsPicture : dsPictures) {
                String url = urlPrefix + "prj2/Ds/" + ds.getId() + "/" + dsPicture.getName();
                dsPicture.setUrl(url);
            }

            ds.setFiles(dsPictures);
        }

        map.put("dsList", dsList);
        map.put("pageInfo", pageInfo);

        return map;
    }

    public Map<String, Object> listMap(String keyword) {
        Map<String, Object> map = new HashMap<>();

        int countAll = mapper.countAll("%" + keyword + "%");

        // view안에 있는 사진리스트를 리스트 안에 사진 받아 오는 방법
        List<Ds> dsList = mapper.selectAllByCategoryMap("%" + keyword + "%");

        for (Ds ds : dsList) {
            List<DsPicture> dsPictures = businessFileMapper.selectNamesByDsId(ds.getId());

            for (DsPicture dsPicture : dsPictures) {
                String url = urlPrefix + "prj2/Ds/" + ds.getId() + "/" + dsPicture.getName();
                dsPicture.setUrl(url);
            }

            ds.setFiles(dsPictures);
        }

        map.put("dsList", dsList);

        return map;
    }

    public Ds get(Integer id) {
        Ds ds = mapper.selectById(id);

        List<DsPicture> dsPictures = businessFileMapper.selectNamesByDsId(id);

        for (DsPicture dsPicture : dsPictures) {
            String url = urlPrefix + "prj2/Ds/" + id + "/" + dsPicture.getName();
            dsPicture.setUrl(url);
        }

        List<BusinessHoliday> businessHolidays = mapper.selectHolidayById(id);

        ds.setFiles(dsPictures);
        ds.setHolidays(businessHolidays);

        return ds;
    }


    public boolean delete(Integer id) {

        // 휴무일 삭제
        mapper.deleteHolidayByDsId(id);

        // 코멘트 삭제
        dsCommentMapper.deleteById(id);

        // 좋아요 삭제
        businessLikeMapper.deleteById(id);

        // 파일 레코드 삭제
        businessFileMapper.deleteByDsId(id);

        return mapper.deleteById(id) == 1;
    }

    public List<DsKakao> kakao(DsKakao dsKakao) {
        return mapper.selectAllByKakao(dsKakao);
    }

    public Ds getName(String name) {
        return get(mapper.selectByName(name).getId());
    }

    public boolean hasAccess(Integer id, Member login) {
        if (login == null) {
            return false;
        }

        if (login.getAuth() != null) {
            return login.getAuth().equals("admin") == true;
        }

        Ds ds = mapper.selectById(id);

        return ds.getMemberId().equals(login.getId());
    }

    public Ds idGet(String memberId) {
        Integer id = mapper.idGet(memberId);
        return mapper.selectById(id);
    }

//    public List<Ds> getListByCK(String keyword, String category) {
//        return mapper.getListByCK("%" + keyword + "%", category);
//    }
}
