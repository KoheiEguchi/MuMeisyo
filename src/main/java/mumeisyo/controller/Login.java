package mumeisyo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.model.User;
import mumeisyo.repository.UserRepository;
import mumeisyo.service.Common;

@Controller
public class Login {
	@Autowired
	UserRepository userRep;
	@Autowired
	HttpSession session;
	@Autowired
	Common common;
	@Autowired
	Top top;
	
	//ログインページを表示する
	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
	public String loginOpen() {
		return "login";
	}
	
	//ログインを確認する
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("name")String name, @RequestParam("password")String password, Model model) {
		//空欄がある場合
		if(name == "" || password == "") {
			model.addAttribute("msg", "全て入力してください。");
			return "login";
		//全て入力されている場合
		}else {
			List<User> userList = userRep.login(name, password);
			//入力されたユーザーが存在する場合
			if(!(CollectionUtils.isEmpty(userList))) {
				model.addAttribute("userList", userList);
				
				//セッションにIDを保存
				long userId = userRep.getId(name, password);
				session.setAttribute("userId", userId);
				//セッションに名前を保存
				session.setAttribute("name", name);
				
				//トップに進む
				top.topOpen(model);
				return "top";
			//存在しない場合
			}else {
				model.addAttribute("msg", "正しく入力してください。");
				return "login";
			}
		}
	}
}
