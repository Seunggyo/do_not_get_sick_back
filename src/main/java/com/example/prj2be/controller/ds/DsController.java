package com.example.prj2be.controller.ds;

import com.example.prj2be.domain.ds.Ds;
import com.example.prj2be.domain.ds.DsKakao;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.ds.DsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ds")
public class DsController {

    private final DsService service;

    @PostMapping("add")
    public ResponseEntity add(Ds ds,
                              @RequestParam(value = "holiday[]",required = false) String[] holidays,
                              @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files,
                              @SessionAttribute(value = "login", required = false) Member login) throws IOException {
        // 약국 정보 기입

        if ( login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!service.validate(ds)) {
            return ResponseEntity.badRequest().build();
        }

        if (service.save(ds, files, login, holidays)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }

    }
    // list?p=? 로 받아야 하기 떄문에 수정
//    TODO : 진료명 카테고리 추가 해야함
    @GetMapping("list")
    public Map<String, Object> list(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                    @RequestParam(value = "k", defaultValue = "") String keyword,
                                    @RequestParam(value = "c", defaultValue = "all") String category) {
        // dsList 와 pageInfo를 같이 넘겨야 해서 map으로 작성

        return service.list(page, keyword, category);
    }

    @GetMapping("kakao")
    public List<DsKakao> map (DsKakao dsKakao){
        return service.kakao(dsKakao);
    }

    @GetMapping("id/{id}")
    public Ds get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PutMapping("edit")
    public ResponseEntity edit(Ds ds,
                               @RequestParam(value = "updateHolidays[]", required = false) String[] holidays,
                               @RequestParam(value = "uploadFile[]", required = false) MultipartFile[] uploadFile,
                               @RequestParam(value = "deleteFileIds[]", required = false) List<Integer> deleteFileIds
                               /*@SessionAttribute(value = "login",required = false) Member login*/) throws IOException {
        // 약국 정보 수정
        // TODO : 멤버 테이블 추가 시 로그인 제약 추가
//        if (!service.hasAccess(ds.getId(), login)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
        if (service.validate(ds)) {
            if (service.update(ds, uploadFile, deleteFileIds, holidays)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {

        if (service.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

}
