package mumeisyo.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mumeisyo.repository.PlaceRepository;
import mumeisyo.service.Common;

@Controller
public class Top {
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	HttpSession session;
	@Autowired
	Common common;
	
	//トップ画面を表示する
	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public String topOpen(Model model) {
		//ログインしていない場合ログインページへ送る
		boolean check = common.loginCheck(model);
		if(check == false) {
			return "login";
		}else {
			//セッション読み込み
			common.sessionSet(model);
			//最新の投稿3件を取得
			common.getNewPlaceList(model);
			return "top";
		}
	}
	
	//ログアウトする
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		//セッションを廃棄
		session.invalidate();
		model.addAttribute("msg", "ログアウトしました。");
		return "login";
	}
}
