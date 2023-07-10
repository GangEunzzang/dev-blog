package com.devblog.service;

import com.devblog.domain.dto.BoardDTO;
import com.devblog.domain.entity.Board;
import com.devblog.domain.repository.BoardRepository;
import com.devblog.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.devblog.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private static final String BOARD_COOKIE_NAME = "board_cookie";

    public List<BoardDTO.Response> findAll() {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream().map(BoardDTO.Response::new).collect(Collectors.toList());
    }
    public Long save(BoardDTO.Request boardDTO) {
        Board board = boardRepository.save(boardDTO.toEntity());
        return board.getId();
    }

    public BoardDTO.Response findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        board.incrementViewCount();

        return new BoardDTO.Response(board);
    }

    public void update(Long id, BoardDTO.Request dto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        board.update(dto.getTitle(), dto.getContent());
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
            boardRepository.save(board);
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
