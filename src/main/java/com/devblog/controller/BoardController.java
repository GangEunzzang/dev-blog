package com.devblog.controller;

import com.devblog.domain.dto.BoardDTO;
import com.devblog.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping({"board", "", "/"})
    public String board(Model model) {
        List<BoardDTO.Response> boardList = boardService.findAll();
        model.addAttribute("dtoList", boardList);
        return "board";
    }

    @GetMapping("register")
    public void register() {
    }

    @GetMapping("modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        BoardDTO.Response dto = boardService.findById(id);
        model.addAttribute("dto", dto);
        return "modify";
    }

    @GetMapping("read/{id}")
    public String read(@PathVariable Long id, Model model, HttpServletRequest request, HttpServletResponse response) {
        boardService.incrementView(id, request, response);
        BoardDTO.Response dto = boardService.findById(id);
        model.addAttribute("dto", dto);
        return "read";
    }

    @PostMapping("register")
    public void register(BoardDTO.Request requestDTO) {
        boardService.save(requestDTO);
    }


    @PutMapping("modify/{id}")
    public void modify(@PathVariable Long id, BoardDTO.Request request) {
        boardService.update(id, request);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }


}

