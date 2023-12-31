package com.sulsul.api.exception.comment;

import com.sulsul.api.exception.AccessNotAllowedException;

public class NotAllowedCommentDeleteException extends AccessNotAllowedException {
    public NotAllowedCommentDeleteException() {
        super("COMMENT_05", "다른 유저가 작성한 댓글을 삭제할 수 없습니다.");
    }
}