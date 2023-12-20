package com.example.prj2be.controller.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.hs.HsService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospital")
public class HsController {

    private final HsService service;


    @GetMapping("list")
    public Map<String, Object> list(
        @RequestParam(value = "c", defaultValue = "all") String category,
        @RequestParam(value = "k", defaultValue = "") String keyword) {
        return service.list(category, keyword);
    }

    @GetMapping(value = "list", params = "course")
    public Map<String, Object> list(@RequestParam(value = "course") String course) {
        return service.courseList(course);
    }

    @GetMapping("listAdmin")
    public Map<String, Object> listAdmin(
            @RequestParam(value = "p", defaultValue = "1") Integer page,
            @RequestParam(value = "l", defaultValue = "all") String list,
            @RequestParam(value = "k", defaultValue = "") String keyword) {
        return service.listAdmin(page, list, keyword);
    }

    @PostMapping("add")
    public ResponseEntity add(Hs hs,
        @RequestParam(value = "course[]", required = false) String[] course,
        @RequestParam(value = "holiday[]", required = false) String[] holidays,
        @RequestParam(value = "hsFiles[]", required = false)
        MultipartFile[] hsFile,
        @SessionAttribute(value = "login", required = false) Member login) throws IOException {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.add(hs, course, holidays, hsFile, login)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("id/{id}")
    public Hs get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PutMapping("edit")
    public ResponseEntity edit(Hs hs,
        @RequestParam(value = "holiday[]", required = false) String[] holiday,
        @RequestParam(value = "course[]", required = false) String[] course,
        @RequestParam(value = "removeFileIds[]", required = false) List<Integer> removeFileIds,
        @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] uploadFile)
        throws IOException {

        if (service.update(hs, holiday, course, removeFileIds, uploadFile)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> remove(@PathVariable Integer id,
        @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.hasAccess(id, login)) {
            if (service.remove(id)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("get")
    public Hs getId(@RequestParam("id") String memberId) {
        return service.getId(memberId);
    }


}
