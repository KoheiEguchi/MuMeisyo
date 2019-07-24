package mumeisyo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.model.User;
import mumeisyo.repository.UserRepository;
import mumeisyo.service.Common;

@Controller
public class UserBAN {
	@Autowired
	UserRepository userRep;
	@Autowired
	Common common;
	@Autowired
	UserList userList;
	
	@GetMapping("/userBAN")
	public String userBANOpen(@RequestParam("id") long id, Model model) {
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
				//退会させるユーザーの情報取得
				List<User> banUser = userRep.getUserData(id);
				model.addAttribute("banUser", banUser);
				return "userBAN";
			}
		}
	}
	
	//ユーザーを退会させる
	@PostMapping("/userBANAction")
	public String userBAN(@RequestParam("id") long id, @RequestParam("name") String name, Model model) {
		//ユーザー削除
		userRep.userDelete(id, name);
		
		model.addAttribute("msg", "ユーザーを削除しました。");
		userList.userListOpen(model);
		return "userList";
	}
}
