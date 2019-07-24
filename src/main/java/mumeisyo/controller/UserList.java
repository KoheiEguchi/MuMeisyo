package mumeisyo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mumeisyo.model.User;
import mumeisyo.repository.UserRepository;
import mumeisyo.service.Common;

@Controller
public class UserList {
	@Autowired
	UserRepository userRep;
	@Autowired
	Common common;
	
	//ユーザー一覧ページを開く
	@GetMapping("/userList")
	public String userListOpen(Model model) {
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
				//ユーザー一覧取得
				List<User> userList = userRep.getUserList();
				model.addAttribute("userList", userList);
				return "userList";
			}
		}
	}
}
