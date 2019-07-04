package mumeisyo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mumeisyo.repository.UserRepository;
import mumeisyo.service.Common;

@Controller
public class UserDelete {
	@Autowired
	UserRepository userRep;
	@Autowired
	HttpSession session;
	@Autowired
	Common common;
	
	//退会ページを表示
	@RequestMapping(value = "/userDelete", method = RequestMethod.GET)
	public String userDeleteOpen(Model model) {
		//ログインしていない場合ログインページへ送る
		boolean check = common.loginCheck(model);
		if(check == false) {
			return "login";
		}else {
			return "userDelete";
		}
	}
	
	//退会
	@RequestMapping(value = "/deleteAction", method = RequestMethod.GET)
	public String userDelete(Model model) {
		String name = (String)session.getAttribute("name");
		userRep.userDelete(name);
		session.invalidate();
		model.addAttribute("msg", "退会しました。");
		return "login";
	}
}
