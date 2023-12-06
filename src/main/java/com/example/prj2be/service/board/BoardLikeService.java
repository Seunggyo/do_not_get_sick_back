package com.example.prj2be.service.board;

import com.example.prj2be.domain.board.BoardLike;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.board.BoardLikeMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

   private final BoardLikeMapper likeMapper;

   public Map<String, Object> update(BoardLike like, Member login) {

      like.setMemberId(login.getId());

      int count = 0;
      if (likeMapper.delete(like) == 0) {
         count = likeMapper.insert(like);
      }

      int countLike = likeMapper.countByBoardId(like.getBoardId());
      // 현재 게시물의 좋아요가 몇갠지

      return Map.of("like", count == 1,
         "countLike", countLike);
      // like좋아요 개수를 리턴..

      // 처음 좋아요 누를 때 : insert
      // 다시 누르면 : delete
   }

   public Map<String, Object> get(Integer boardId, Member login) {

      int countLike = likeMapper.countByBoardId(boardId);

      BoardLike like = null;

      if (login != null) {
         like = likeMapper.selectByBoardIdAndMemberId(boardId, login.getId());

      }
      return Map.of("like", like != null, "countLike", countLike);
   }
}
