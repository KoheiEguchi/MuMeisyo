package mumeisyo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.model.User;
import mumeisyo.repository.UserRepository;

@Controller
public class UserCreate {
	@Autowired
	UserRepository userRep;
	
	//登録ページを表示する
	@RequestMapping(value = "/userCreate", method = RequestMethod.GET)
	public String userCreateOpen() {
		return "userCreate";
	}
	
	//ユーザー新規登録
	@RequestMapping(value = "/userCreate", method = RequestMethod.POST)
	public String userCreate(@RequestParam("name")String name, @RequestParam("pass1")String pass1, @RequestParam("pass2")String pass2, Model model) {
		String password = "";
		//空欄がある場合
		if(name == "" || pass1 == "" || pass2 == "") {
			model.addAttribute("msg", "全て入力してください。");
			return "userCreate";
		//パスワードが違う場合
		}else if(!(pass1.equals(pass2))) {
			model.addAttribute("msg", "パスワードは同じものを入力してください。");
			return "userCreate";
		//パスワードが同じ場合
		}else {
			password = pass1;
			List<User> sameCheck = userRep.userCheck(name);
			//名前被りがない場合
			if(CollectionUtils.isEmpty(sameCheck)) {
				userRep.userCreate(name, password);
				model.addAttribute("msg", "登録しました。");
				return "login";
			//被っている場合
			}else {
				model.addAttribute("msg", "その名前はすでに使われています。");
				return "userCreate";
			}
		}
	}
	
}
