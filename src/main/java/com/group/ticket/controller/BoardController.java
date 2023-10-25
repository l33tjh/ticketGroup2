package com.group.ticket.controller;

import com.group.ticket.DAO.BoardDAO;
import com.group.ticket.DAO.ResearchDAO;
import com.group.ticket.domain.entity.Board;
import com.group.ticket.domain.entity.research;
import com.group.ticket.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BoardController {
    private BoardService boardService;

    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }
    @GetMapping("/")
    public String main(){
        return "/main.html";
    }
    @GetMapping("/list")
    public String list(Model model){
        List<BoardDAO> boradDAOList = boardService.getBoardList();
        model.addAttribute("postList", boradDAOList);
        return "/list.html";
    }
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<ResearchDAO> rsearchResults = boardService.searchResearchByKeyword(keyword);
        model.addAttribute("postList", rsearchResults);
        return "/search-results.html"; // 검색 결과를 나타낼 HTML 페이지의 이름
}
    @GetMapping("/post")
    public String post() {
        return "/post.html";
    }
    @PostMapping("/post")
    public String write(BoardDAO boardDAO) {
        boardService.savePost(boardDAO);
        return "redirect:/list";
    }
    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDAO boardDAO = boardService.getPost(id);
        model.addAttribute("post", boardDAO);
        return "/detail.html";
    }
}
