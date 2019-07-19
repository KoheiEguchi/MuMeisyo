package mumeisyo.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
			List<Place> placeList = placeRep.getPlaceNew();
			model.addAttribute("placeList", placeList);
			model.addAttribute("sortResult", "新しい順");
			
			//セッション読み込み
			common.sessionSet(model);
			return "browse";
		}
	}
	
	//投稿を並べ替え
	@RequestMapping(value = "/browseSort", method = RequestMethod.POST)
	public String browseSort(@RequestParam("sort")String sort, Model model) throws SQLException {
		List<Place> placeList = null;
		switch(sort) {
		case "new":
			placeList = placeRep.getPlaceNew();
			model.addAttribute("sortResult", "新しい順");
			break;
		case "old":
			placeList = placeRep.getPlaceOld();
			model.addAttribute("sortResult", "古い順");
			break;
		case "good":
			placeList = placeRep.getPlaceGood();
			model.addAttribute("sortResult", "評価の多い順");
			break;
		case "bad":
			placeList = placeRep.getPlaceBad();
			model.addAttribute("sortResult", "評価の少ない順");
			break;
		}
		model.addAttribute("placeList", placeList);
		
		//セッション読み込み
		common.sessionSet(model);
		return "browse";
	}
}
