package com.sulsul.api.exception.file;

import com.sulsul.api.exception.ResourceNotFoundException;

import java.util.Map;

public class FileNotFoundException extends ResourceNotFoundException {
    public FileNotFoundException() {
        super("FILE_05", "파일을 찾을 수 없습니다.");
    }

    public FileNotFoundException(String filePath) {
        super("FILE_05", "파일을 찾을 수 없습니다.", Map.of("filePath", filePath));
    }
}