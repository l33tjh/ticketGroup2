package com.group.ticket.service;

import com.group.ticket.DAO.BoardDAO;
import com.group.ticket.DAO.ResearchDAO;
import com.group.ticket.domain.entity.Board;
import com.group.ticket.domain.entity.research;
import com.group.ticket.domain.repository.BoardRepository;
import com.group.ticket.domain.repository.ResearchRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {
    private BoardRepository boardRepository;
    private ResearchRepository researchRepository;

    public BoardService(BoardRepository boardRepository, ResearchRepository researchRepository) {
        this.boardRepository = boardRepository;
        this.researchRepository = researchRepository;
    }

    @Transactional
    public Long savePost(BoardDAO boardDAO) {
        return boardRepository.save(boardDAO.toEntity()).getId();
    }
    @Transactional
    public List<BoardDAO> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDAO> boardDAOList = new ArrayList<>();

        for (Board board : boardList) {
            BoardDAO boardDAO = BoardDAO.builder()
                    .id(board.getId())
                    .author(board.getAuthor())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .build();
            boardDAOList.add(boardDAO);
        }
        return boardDAOList;
    }
    @Transactional
    public List<ResearchDAO> searchResearchByKeyword(String keyword) {
        List<research> researchList = researchRepository.findByKeywordContaining(keyword);
        return mapResearchEntitiesToDAOs(researchList);
    }
    public List<ResearchDAO> searchResearchByImagePath(String keyword) {
        List<research> researchResults = researchRepository.findBySpathContaining(keyword);
        return researchResults.stream()
                .map(ResearchDAO::new)
                .collect(Collectors.toList());
    }
    private List<ResearchDAO> mapResearchEntitiesToDAOs(List<research> researchList) {
        return researchList.stream()
                .map(research -> ResearchDAO.builder()
                        .sid(research.getSid())
                        .stype(research.getStype())
                        .sname(research.getSname())
                        .sperformer(research.getSperformer())
                        .sexplain(research.getSexplain())
                        .splace(research.getSplace())
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional
    public BoardDAO getPost(Long id) {
        Board board = boardRepository.findById(id).get();

        BoardDAO boardDAO = BoardDAO.builder()
                .id(board.getId())
                .author(board.getAuthor())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .build();
        return boardDAO;
    }
    @Transactional
    public ResearchDAO getResearch(Long id) {
        research research = researchRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 ID의 공연이 없습니다."));
        return new ResearchDAO(research);
    }
}
