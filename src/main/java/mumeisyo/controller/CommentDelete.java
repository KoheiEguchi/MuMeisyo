package mumeisyo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import mumeisyo.model.Comment;
import mumeisyo.repository.CommentRepository;
import mumeisyo.repository.PlaceRepository;
import mumeisyo.service.Common;

@Controller
public class CommentDelete {
	@Autowired
	CommentRepository commentRep;
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	Common common;
	@Autowired
	HttpSession session;
	@Autowired
	PostDetail postDetail;
	
	//コメント削除ページを表示
	@GetMapping("/commentDelete")
	public String commentDeleteOpen(long commentId, Model model) {
		//ログインしていない場合ログインページへ送る
		boolean loginCheck = common.loginCheck(model);
		if(loginCheck == false) {
			return "login";
		}else {
			//管理人以外トップページへ送る
			boolean adminCheck = common.adminCheck(model);
			if(adminCheck == false) {
				return "top";
			}else {
				//削除するコメントを取得
				List<Comment> deleteComment = commentRep.getDeleteComment(commentId);
				model.addAttribute("deleteComment", deleteComment);
				return "commentDelete";
			}
		}
	}
	
	//コメントを削除
	@PostMapping("/commentDeleteOk")
	public String commentDelete(long commentId, long placeId, Model model) {
		//コメント削除
		commentRep.deleteComment(commentId);
		model.addAttribute("msg", "コメントを削除しました。");
		//投稿者かの判別用に自分のIDを取得
		long userId = (long)session.getAttribute("userId");
		
		postDetail.postDetailOpen(placeId, userId, model);
		return "postDetail";
	}
}
