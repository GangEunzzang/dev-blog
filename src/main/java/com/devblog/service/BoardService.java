package com.devblog.service;

import com.devblog.domain.dto.BoardDTO;
import com.devblog.domain.entity.Board;
import com.devblog.domain.repository.BoardQueryRepository;
import com.devblog.domain.repository.BoardRepository;
import com.devblog.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.devblog.exception.ErrorCode.*;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;
    private static final String BOARD_COOKIE_NAME = "board_cookie";

    public Page<BoardDTO.Response> findAll(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 12;

        return boardQueryRepository.findByPage(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
    }
    public Long save(BoardDTO.Request request) {
        Board board = boardRepository.save(request.toEntity());
        return board.getId();
    }

    public BoardDTO.Response findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        return new BoardDTO.Response(board);
    }

    public void update(Long id, BoardDTO.Request request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        board.update(request.getTitle(), request.getContent());
    }

    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        boardRepository.delete(board);
    }


    public void incrementView(Long id, HttpServletRequest request, HttpServletResponse response) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        Cookie[] cookies = request.getCookies();
        if (cookies == null || Arrays.stream(cookies).noneMatch(cookie -> cookie.getName().equals(BOARD_COOKIE_NAME + id))) {
            board.incrementViewCount();
            Cookie newCookie = createCookieForForNotOverlap(id);
            response.addCookie(newCookie);
        }
    }

    private Cookie createCookieForForNotOverlap(Long id) {
        Cookie cookie = new Cookie(BOARD_COOKIE_NAME + id, String.valueOf(id));
        cookie.setComment("duplicate views prevention");
        cookie.setMaxAge(getRemainSecondForTomorrow()); // 쿠키 유지기간 하루
        cookie.setHttpOnly(true);                       // 서버에서만 조작 가능
        return cookie;
    }

    private int getRemainSecondForTomorrow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
        return (int) now.until(tomorrow, ChronoUnit.SECONDS);
    }

}
