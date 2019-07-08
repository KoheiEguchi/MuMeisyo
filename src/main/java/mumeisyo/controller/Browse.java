package mumeisyo.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mumeisyo.model.Place;
import mumeisyo.repository.PlaceRepository;
import mumeisyo.service.Common;

@Controller
public class Browse {
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	Common common;
	
	//投稿紹介ページを表示する
	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public String browseOpen(Model model) throws SQLException {
		//ログインしていない場合ログインページへ送る
		boolean check = common.loginCheck(model);
		if(check == false) {
			return "login";
		}else {
			List<Place> placeList = placeRep.getPlace();
			model.addAttribute("placeList", placeList);
			return "browse";
		}
	}
}
