package com.devblog.controller;

import com.devblog.domain.dto.BoardDTO;
import com.devblog.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping({"board", "", "/", "list"})
    public String board(Model model, @PageableDefault(page = 1) Pageable pageable, Principal principal) {

        Page<BoardDTO.Response> boardList = boardService.findAll(pageable);

        int blockLimit = 3;
        int startPage = ((int) Math.ceil((double) pageable.getPageNumber() / blockLimit) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), boardList.getTotalPages());

        model.addAttribute("dtoList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board";
    }

    @GetMapping("register")
    public void register(Principal principal) {
        System.out.println("이름" + principal);
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
    public String register(BoardDTO.Request request) {
        Long id = boardService.save(request);
        return "redirect:/read/" + id;
    }


    @PostMapping("modify/{id}")
    public String modify(@PathVariable Long id, BoardDTO.Request request) {
        boardService.update(id, request);
        return "redirect:/read/" + id;
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }

    @PostMapping("comment/register")
    public void commentRegister(Long id) {
    }

}

