package com.example.prj2be.controller.ds;

import com.example.prj2be.domain.Ds;
import com.example.prj2be.service.DsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ds")
public class DsController {

    private final DsService service;

    @PostMapping("add")
    public ResponseEntity add ( Ds ds
                               /*,@SessionAttribute(value = "login", required = false) Member login*/) {
        // 약국 정보 기입
        // TODO : 멤버 테이블 추가 시 로그인 제약 추가

//        if ( login != null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
        System.out.println(ds);
        if ( !service.validate(ds)) {
            return  ResponseEntity.badRequest().build();
        }

        if ( service.save(ds)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("list")
    public List<Ds> list (){
        return service.list();
    }

    @GetMapping("id/{id}")
    public Ds get(@PathVariable Integer id){
        return service.get(id);
    }

    @PutMapping("edit")
    public ResponseEntity edit(Ds ds/*,
                               @SessionAttribute(value = "login",required = false) Member login*/){
        // 약국 정보 수정
        // TODO : 멤버 테이블 추가 시 로그인 제약 추가
//        if (!service.hasAccess(ds.getId(), login)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }

        if (service.validate(ds)) {
            if (service.update(ds)){
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
           return ResponseEntity.badRequest().build();
        }
    }

//    public ResponseEntity<List<Ds>> asdf() {
//        return ResponseEntity.ok(service.getDs())
//    }
}
