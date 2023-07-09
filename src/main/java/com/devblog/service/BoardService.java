package com.devblog.service;

import com.devblog.domain.dto.BoardDTO;
import com.devblog.domain.entity.Board;
import com.devblog.domain.repository.BoardRepository;
import com.devblog.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.devblog.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Long save(BoardDTO.Request boardDTO) {
        Board board = boardRepository.save(boardDTO.toEntity());
        return board.getId();
    }

    public BoardDTO.Response findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(PAGE_NOT_FOUND));
        return new BoardDTO.Response(board);
    }

    public void update(Long id, BoardDTO.Request dto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(PAGE_NOT_FOUND));
        board.update(dto.getTitle(), dto.getContent());
    }

    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(PAGE_NOT_FOUND));
        boardRepository.delete(board);
    }

}
