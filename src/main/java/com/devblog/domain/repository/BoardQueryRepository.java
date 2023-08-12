package com.devblog.domain.repository;

import com.devblog.domain.dto.BoardDTO;
import com.devblog.domain.entity.QBoard;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.devblog.domain.entity.QBoard.board;

@RequiredArgsConstructor
@Repository
public class BoardQueryRepository {

    private final JPAQueryFactory queryFactory;


    public Page<BoardDTO.Response> findByPage(Pageable pageable) {

        List<BoardDTO.Response> result = queryFactory.
                select(Projections.fields(BoardDTO.Response.class,
                        board.id, board.title,
                        board.content, board.writer,
                        board.views))
                .from(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(board.count())
                .from(board)
                .fetchOne();

        long totalCount = (count != null) ? count : 0;

        return new PageImpl<>(result, pageable, totalCount);
    }
}
