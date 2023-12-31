package com.sulsul.api.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
public class FileUploadRequest {
    private MultipartFile file;
    private String detail;
}