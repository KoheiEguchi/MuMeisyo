package mumeisyo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.model.Place;
import mumeisyo.model.Comment;
import mumeisyo.model.Good;
import mumeisyo.repository.CommentRepository;
import mumeisyo.repository.GoodRepository;
import mumeisyo.repository.PlaceRepository;
import mumeisyo.service.Common;

@Controller
public class PostDetail {
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	GoodRepository goodRep;
	@Autowired
	CommentRepository commentRep;
	@Autowired
	Common common;
	@Autowired
	HttpSession session;
	
	//指定された投稿の詳細を表示する
	@RequestMapping(value = "/postDetail", method = RequestMethod.GET)
	public String postDetailOpen(@RequestParam("id")long id, @RequestParam("userId")long userId, Model model) {
		//投稿内容
		List<Place> postDetail = placeRep.getPostDetail(id);
		model.addAttribute("postDetail", postDetail);
		//高評価者一覧
		List<Good> goodUsers = goodRep.getGoodUsers(id);
		model.addAttribute("goodUsers", goodUsers);
		//コメント一覧
		List<Comment> commentList = commentRep.getComment(id);
		model.addAttribute("commentList", commentList);
		//自分の投稿か確認
		long myPostCheck = placeRep.myPostCheck(id);
		//自分の投稿だった場合
		if(myPostCheck == userId) {
			model.addAttribute("myPost", "myPost");
		//他人の投稿だった場合
		}else {
			//すでに高評価しているか確認
			List<Good> didGoodCheck = goodRep.didGoodCheck(userId, id);
			if(didGoodCheck.isEmpty()) {
				model.addAttribute("noGood", "noGood");
			}
		}
		common.sessionSet(model);
		return "postDetail";
	}
	
	//コメントを投稿する
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public String comment(@RequestParam("placeId") long placeId, @RequestParam("comment") String comment, Model model) {
		long userId = (long)session.getAttribute("userId");
		
		//コメントが書かれていない場合
		if(comment.equals("")) {
			model.addAttribute("msg", "コメントが空欄です。");
		//コメントが書かれていた場合
		}else {
			String name = (String)session.getAttribute("name");
			
			//コメント投稿
			commentRep.postComment(name, userId, placeId, comment);
			model.addAttribute("msg", "コメントを受け付けました。");
		}
		postDetailOpen(placeId, userId, model);
		return "postDetail";
	}
}
