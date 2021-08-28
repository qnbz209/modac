package com.modac.server.controller;

import com.modac.server.dto.RecordDetail;
import com.modac.server.dto.UserProfile;
import com.modac.server.service.MultipartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class MultipartController {

    private final MultipartService multipartService;

    @PostMapping("/user/{id}")
    public Mono<UserProfile> uploadProfile(@PathVariable Long id, @RequestParam MultipartFile file) {
        return multipartService.uploadProfile(id, file);
    }

    @PostMapping("/record/{id}")
    public Mono<RecordDetail> uploadRecordContent(@PathVariable Long id, @RequestParam MultipartFile file) {
        return multipartService.uploadRecordContent(id, file);
    }

    @DeleteMapping("/user/{id}")
    public Mono<UserProfile> deleteProfile(@PathVariable Long id) {
        return multipartService.deleteProfile(id);
    }

    @DeleteMapping("/record/{id}")
    public Mono<RecordDetail> deleteRecordContent(@PathVariable Long id) {
        return multipartService.deleteRecordContent(id);
    }
}
