package com.sulsul.api.exception.comment;

import com.sulsul.api.exception.ResourceNotFoundException;

import java.util.Map;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException(long commentId) {
        super("COMMENT_03", "해당 댓글을 찾을 수 없습니다.", Map.of("commentId", String.valueOf(commentId)));
    }
}