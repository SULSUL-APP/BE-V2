package com.sulsul.api.comment.dto.response;

import com.sulsul.api.comment.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentGroupResponse {

    @Schema(description = "댓글 리스트")
    private final List<CommentResponse> comments = new ArrayList<>();

    public CommentGroupResponse(List<Comment> comments) {
        comments.stream()
                .map(CommentResponse::new)
                .forEach(comment -> this.comments.add(comment));
    }
}