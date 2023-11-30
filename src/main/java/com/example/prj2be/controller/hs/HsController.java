package com.example.prj2be.controller.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.service.hs.HsService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospital")
public class HsController {

    private final HsService service;


    @GetMapping("list")
    public List<Hs> list(@RequestParam(value = "category", required = false) String category) {
        return service.list(category);
    }

    @PostMapping("add")
    public ResponseEntity add(Hs hs,
        @RequestParam(value = "hsFiles[]", required = false)
        MultipartFile[] hsFile) throws IOException {
        System.out.println("hs = " + hs);
        if (service.add(hs, hsFile)) {
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
        @RequestParam(value = "removeFileIds[]", required = false) List<Integer> removeFileIds,
        @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] uploadFile)
        throws IOException {
        System.out.println("hs = " + hs);
        if (service.update(hs, removeFileIds, uploadFile)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity remove(@PathVariable Integer id) {
        if (service.remove(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }


}
