package com.example.prj2be.service.hs;

import com.example.prj2be.domain.business.BusinessHoliday;
import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.domain.hs.HsCourse;
import com.example.prj2be.domain.hs.HsFile;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.hs.HsCommentMapper;
import com.example.prj2be.mapper.hs.HsFileMapper;
import com.example.prj2be.mapper.hs.HsLikeMapper;
import com.example.prj2be.mapper.hs.HsMapper;
import com.example.prj2be.mapper.hs.HsReservationMapper;
import java.io.IOException;
import java.util.List;
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
public class HsService {

    private final HsMapper mapper;
    private final HsFileMapper fileMapper;
    private final HsLikeMapper likeMapper;
    private final HsCommentMapper commentMapper;
    private final HsReservationMapper reservationMapper;
    private final S3Client s3;
    @Value("${aws.s3.bucket.name}")
    private String bucket;
    @Value("${image.file.prefix}")
    private String urlPrefix;

    private void upload(Integer businessId, MultipartFile file) throws IOException {
        String key = "prj2/hospital/" + businessId + "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build();
        s3.putObject(objectRequest,
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

    }

    public List<Hs> list(String category) {
        return mapper.selectByCategory(category);
    }

    public boolean add(Hs hs, String[] course, String[] holidays, MultipartFile[] hsFile,
        Member login)
        throws IOException {
        hs.setMemberId(login.getId());
        int cnt = mapper.insert(hs);

        if (course != null) {
            for (String hsCourse : course) {
                mapper.insertCourse(hs.getId(), hsCourse);
            }
        }
        
        if (holidays != null) {
            for (String holiday : holidays) {
                mapper.insertHoliday(hs.getId(), holiday);
            }
        }

        if (hsFile != null) {
            for (int i = 0; i < hsFile.length; i++) {
                fileMapper.insert(hs.getId(), hsFile[i].getOriginalFilename());

                upload(hs.getId(), hsFile[i]);

            }
        }
        return cnt == 1;
    }

    public boolean update(Hs hs, String[] holidays, String[] courses, List<Integer> removeFileIds,
        MultipartFile[] uploadFiles)
        throws IOException {
        int cnt = mapper.update(hs);
        mapper.holidayDeleteByBusinessId(hs.getId());
        if (holidays != null) {
            for (String holiday : holidays) {
                mapper.insertHoliday(hs.getId(), holiday);
            }
        }
        mapper.courseDeleteByBusinessId(hs.getId());
        if (courses != null) {
            for (String course : courses) {
                mapper.insertCourse(hs.getId(), course);
            }
        }

        if (removeFileIds != null) {
            for (Integer id : removeFileIds) {
                HsFile file = fileMapper.selectById(id);
                String key = "prj2/hospital/" + hs.getId() + "/" + file.getId();

                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
                s3.deleteObject(objectRequest);

                fileMapper.deleteById(id);
            }
        }

        if (uploadFiles != null) {
            for (MultipartFile file : uploadFiles) {
                upload(hs.getId(), file);

                fileMapper.insert(hs.getId(), file.getOriginalFilename());
            }
        }
        return cnt == 1;
    }

    public Hs get(Integer id) {
        Hs hs = mapper.selectById(id);
        List<HsFile> hsFiles = fileMapper.selectByHsId(id);
        for (HsFile hsFile : hsFiles) {
            String url = urlPrefix + "prj2/hospital/" + id + "/" + hsFile.getName();
            hsFile.setUrl(url);
        }
        hs.setFiles(hsFiles);
        List<HsCourse> hsCourses = mapper.courseSelectByBusinessId(id);
        List<BusinessHoliday> businessHolidays = mapper.holidaySelectByBusinessId(id);
        hs.setMedicalCourse(hsCourses);
        hs.setHolidays(businessHolidays);
        return hs;
    }

    public boolean remove(Integer id) {
        deleteFile(id);

        mapper.holidayDeleteByBusinessId(id);

        mapper.courseDeleteByBusinessId(id);

        commentMapper.deleteByBusinessId(id);

        likeMapper.deleteByBusinessId(id);

        reservationMapper.deleteByBusinessId(id);

        return mapper.deleteById(id) == 1;
    }

    private void deleteFile(Integer id) {
        List<HsFile> hsFiles = fileMapper.selectByHsId(id);

        for (HsFile file : hsFiles) {
            String key = "prj2/hospital/" + id + "/" + file.getName();
            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
            s3.deleteObject(objectRequest);
        }

        fileMapper.deleteByHsId(id);
    }


    public boolean hasAccess(Integer id, Member login) {
        Hs hospital = mapper.selectById(id);
        if (login == null) {
            return false;
        }
        if (login.isAdmin()) {
            return true;
        }
        return hospital.getMemberId().equals(login.getId());
    }
}
