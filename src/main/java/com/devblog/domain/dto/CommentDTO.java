package com.devblog.domain.dto;

import com.devblog.domain.entity.Comment;
import com.devblog.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long id;
        private User user;
        private String comment;

        public Comment toEntity() {
            return Comment.builder()
                    .comment(comment)
                    .user(user)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String comment;
        private String nickName;
        private Long boardId;
        public Response(final Comment comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();
            this.nickName = comment.getUser().getName();
            this.boardId = comment.getBoard().getId();
        }
    }
}
