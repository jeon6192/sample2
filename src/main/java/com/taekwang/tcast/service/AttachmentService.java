package com.taekwang.tcast.service;

import com.taekwang.tcast.exception.UserException;
import com.taekwang.tcast.model.dto.AttachmentDto;
import com.taekwang.tcast.model.enums.UserError;
import com.taekwang.tcast.repository.AttachmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }


    public AttachmentDto getAttachment(Integer idx) throws UserException {
        return AttachmentDto.toDto(attachmentRepository.findById(idx)
                .orElseThrow(() -> new UserException(UserError.BAD_REQUEST)));
    }
}
