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
				model.addAttribute("sortResult", "登録が古い順");
				return "userList";
			}
		}
	}
	
	//ユーザー一覧を並べ替え
	@PostMapping("/userSort")
	public String userSort(@RequestParam("sort") String sort, Model model) {
		List<User> userList = null;
		if(sort.equals("new")) {
			userList = userRep.getUserNew();
			model.addAttribute("sortResult", "登録が新しい順");
		}else {
			userList = userRep.getUserOld();
			model.addAttribute("sortResult", "登録が古い順");
		}
		model.addAttribute("userList", userList);
		return "userList";
	}
	
	//ユーザー一覧を絞り込み
	@PostMapping("/userChoose")
	public String userChoose(@RequestParam("name") String name, Model model) {
		List<User> userList = userRep.chooseUser(name);
		//該当しなかった場合
		if(userList.isEmpty()) {
			model.addAttribute("noChoose", "条件に合うユーザーはいません。");
		}
		model.addAttribute("userList", userList);
		model.addAttribute("sortResult", "登録が古い順");
		return "userList";
	}
}
