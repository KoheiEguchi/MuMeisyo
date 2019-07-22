package mumeisyo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.model.Forum;
import mumeisyo.repository.ForumRepository;
import mumeisyo.service.Common;

@Controller
public class ForumCont {
	@Autowired
	ForumRepository forumRep;
	@Autowired
	Common common;
	
	//意見投稿ページを表示する
	@GetMapping("/forum")
	public String forumOpen(Model model) {
		//ログインしていない場合ログインページへ送る
		boolean check = common.loginCheck(model);
		if(check == false) {
			return "login";
		}else {
			return "forum";
		}
	}
	
	//投稿意見一覧を表示する
	@GetMapping("forumCheck")
	public String forumCheck(Model model) {
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
				List<Forum> forumList = forumRep.getForum();
				model.addAttribute("forumList", forumList);
				
				model.addAttribute("admin", "admin");
				return "forum";
			}
		}
	}
	
	//意見を投稿する
	@PostMapping("/forumPost")
	public String forumPost(@RequestParam("address")String address, @RequestParam("forum")String forum, Model model) {
		//本文が空欄の場合
		if(forum == "") {
			model.addAttribute("msg", "本文を入力してください。");
			return "forum";
		//本文が入力されている場合
		}else {
			//意見投稿
			forumRep.postForum(address, forum);
			model.addAttribute("msg", "ご意見ありがとうございました。");
			return "forum";
		}
	}
}
