package com.taekwang.tcast.controller;

import com.taekwang.tcast.exception.UserException;
import com.taekwang.tcast.model.dto.AttachmentDto;
import com.taekwang.tcast.service.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class AttachmentController {
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping("/attachment/{idx}")
    public ResponseEntity<AttachmentDto> getAttachment(@PathVariable Integer idx) throws UserException {
        return ResponseEntity.ok(attachmentService.getAttachment(idx));
    }
}
