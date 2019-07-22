package mumeisyo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	//ユーザーの情報ページを表示
	@GetMapping("/userDetail")
	public String userDetailOpen(@RequestParam("userId")long userId, Model model) {
		//ログインしていない場合ログインページへ送る
		boolean check = common.loginCheck(model);
		if(check == false) {
			return "login";
		}else {
			//ユーザー情報と投稿履歴を取得する
			common.getUserDetail(userId, model);
			//セッション読み込み
			common.sessionGet(model);
			
			//自分のページの場合
			long myId = (long)session.getAttribute("userId");
			if(userId == myId) {
				model.addAttribute("myData", "myData");
			}
		}
		return "userDetail";
	}
	
	//一言を更新する
	@PostMapping("/greet")
	public String greet(@RequestParam("greet") String greet, Model model) {
		long userId = (long)session.getAttribute("userId");
		//一言更新
		userRep.greet(greet, userId);
		
		model.addAttribute("msg", "一言を更新しました。");
		userDetailOpen(userId, model);
		return "userDetail";
	}
	
	//情報変更前に本人確認
	@PostMapping("/userDetailCheck")
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
		long userId = (long)session.getAttribute("userId");
		userDetailOpen(userId, model);
		return "userDetail";
	}
	
	//ユーザー情報変更
	@PostMapping("/newUserDetail")
	public String newUserDetail(@RequestParam("name")String name, @RequestParam("pass1")String pass1, @RequestParam("pass2")String pass2, Model model) {
		String password = "";
		//空欄がある場合
		if(name == "" || pass1 == "" || pass2 == "") {
			model.addAttribute("msg", "全て入力してください。");
			model.addAttribute("newUserDetail", "newUserDetail");
		//パスワードが違う場合
		}else if(!(pass1.equals(pass2))) {
			model.addAttribute("msg", "パスワードは同じものを入力してください。");
			model.addAttribute("newUserDetail", "newUserDetail");
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
		long userId = (long)session.getAttribute("userId");
		userDetailOpen(userId, model);
		return "userDetail";
	}
}
