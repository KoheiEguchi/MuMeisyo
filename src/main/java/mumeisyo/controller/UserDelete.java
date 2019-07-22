package mumeisyo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
	@GetMapping("/userDelete")
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
	@PostMapping("/userDeleteAction")
	public String userDelete(Model model) {
		long userId = (long)session.getAttribute("userId");
		String name = (String)session.getAttribute("name");
		userRep.userDelete(userId, name);
		session.invalidate();
		model.addAttribute("msg", "退会しました。");
		return "login";
	}
}
