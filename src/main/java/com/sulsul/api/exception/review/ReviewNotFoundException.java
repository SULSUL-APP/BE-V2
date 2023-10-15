package com.sulsul.api.exception.review;

import com.sulsul.api.exception.ResourceNotFoundException;

import java.util.Map;

public class ReviewNotFoundException extends ResourceNotFoundException {
    public ReviewNotFoundException(long essayId) {
        super("REVIEW_02", "첨삭에 작성된 리뷰를 찾을 수 없습니다.", Map.of("essayId", String.valueOf(essayId)));
    }
}