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
public class UserDetail {
	@Autowired
	UserRepository userRep;
	@Autowired
	HttpSession session;
	@Autowired
	Common common;
	
	//自分の情報ページを表示
	@RequestMapping(value = "/userDetail", method = RequestMethod.GET)
	public String userDetailOpen(Model model) {
		//ログインしていない場合ログインページへ送る
		boolean check = common.loginCheck(model);
		if(check == false) {
			return "login";
		}else {
			String name = (String)session.getAttribute("name");
			//ユーザー情報と投稿履歴を取得する
			common.getUserDetail(name, model);
			//情報変更欄表示用
			model.addAttribute("myData", "myData");
			return "userDetail";
		}
	}
	
	//他ユーザーの情報ページを表示
	@RequestMapping(value = "/otherUserDetail", method = RequestMethod.POST)
	public String otherUserDetail(@RequestParam("name")String name, Model model) {
		String sessionName = (String)session.getAttribute("name");
		//自分の情報ページへ行く場合
		if(name.equals(sessionName)) {
			userDetailOpen(model);
		//他ユーザーの場合
		}else {
			//ユーザー情報と投稿履歴を取得する
			common.getUserDetail(name, model);
		}
		return "userDetail";
	}
	
	//情報変更前に本人確認
	@RequestMapping(value = "/userDetailCheck", method = RequestMethod.POST)
	public String userDetailCheck(@RequestParam("password")String password, Model model) {
		//空欄がある場合
		if(password == "") {
			model.addAttribute("msg", "入力してください。");
		//入力されている場合
		}else {
			String name = (String)session.getAttribute("name");
			List<User> checkName = userRep.login(name, password);
			//パスワードとユーザー名が適合する場合
			if(!(checkName.isEmpty())) {
				model.addAttribute("newUserDetail", "newUserDetail");
			//適合しない場合
			}else {
				model.addAttribute("msg", "パスワードが違います。");
			}
			checkName.clear();
		}
		userDetailOpen(model);
		return "userDetail";
	}
	
	//ユーザー情報変更
	@RequestMapping(value = "/newUserDetail", method = RequestMethod.POST)
	public String newUserDetail(@RequestParam("name")String name, @RequestParam("pass1")String pass1, @RequestParam("pass2")String pass2, Model model) {
		String password = "";
		//空欄がある場合
		if(name == "" || pass1 == "" || pass2 == "") {
			model.addAttribute("msg", "全て入力してください。");
		//パスワードが違う場合
		}else if(!(pass1.equals(pass2))) {
			model.addAttribute("msg", "パスワードは同じものを入力してください。");
		//パスワードが同じ場合
		}else {
			password = pass1;
			List<User> sameCheck = userRep.userCheck(name);
			//名前被りがない場合
			if(CollectionUtils.isEmpty(sameCheck)) {
				String sessionName = (String)session.getAttribute("name");
				userRep.userUpdate(name, password, sessionName);
				model.addAttribute("msg", "変更しました。");
				
				//セッションの名前を更新
				session.setAttribute("name", name);
			//被っている場合
			}else {
				model.addAttribute("msg", "その名前はすでに使われています。");
			}
		}
		userDetailOpen(model);
		return "userDetail";
	}
}
