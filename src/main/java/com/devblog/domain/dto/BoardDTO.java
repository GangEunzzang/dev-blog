package com.devblog.domain.dto;

import com.devblog.domain.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardDTO {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String title;
        private String content;
        private String writer;

        public Board toEntity() {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .writer(writer)
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private int views;
        private List<CommentDTO.Response> comments;

        public Response(final Board board) {
            id = board.getId();
            title = board.getTitle();
            content = board.getContent();
            writer = board.getWriter();
            views = board.getViews();
            comments = board.getComments().stream().map(CommentDTO.Response::new).collect(Collectors.toList());

        }
    }
}
