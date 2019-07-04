package mumeisyo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mumeisyo.repository.PlaceRepository;
import mumeisyo.service.Common;

@Controller
public class Post {
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	HttpSession session;
	@Autowired
	Common common;
		
	//投稿ページを表示する
	@RequestMapping(value = "/post", method = RequestMethod.GET)
	public String postOpen(Model model) {
		//ログインしていない場合ログインページへ送る
		boolean check = common.loginCheck(model);
		if(check == false) {
			return "login";
		}else {
			return "post";
		}
	}
	
	//投稿する
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String post(@RequestParam("pic")MultipartFile pic, @RequestParam("text")String text, Model model) {
		//説明が空欄の場合
		if(text.equals("")) {
			model.addAttribute("msg", "説明を入力してください。");
			return "post";
		//空欄でない場合
		}else {
			//画像を保存する
			String picName = common.picSave(pic);
			
			String name = (String)session.getAttribute("name");
			placeRep.newPost(name, picName, text);
			model.addAttribute("msg", "投稿を受けつけました。");
			return "top";
		}
	}
	
}
