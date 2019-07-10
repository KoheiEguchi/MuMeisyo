package mumeisyo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.model.Place;
import mumeisyo.model.Good;
import mumeisyo.repository.GoodRepository;
import mumeisyo.repository.PlaceRepository;
import mumeisyo.service.Common;

@Controller
public class PlaceDetail {
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	GoodRepository goodRep;
	@Autowired
	Common common;
	
	//指定された投稿の詳細を表示
	@RequestMapping(value = "/placeDetail", method = RequestMethod.GET)
	public String placeDetailOpen(@RequestParam("id")long id, @RequestParam("userId")long userId, Model model) {
		//投稿内容
		List<Place> placeDetail = placeRep.getPlaceDetail(id);
		model.addAttribute("placeDetail", placeDetail);
		//高評価者一覧
		List<Good> goodUsers = goodRep.getGoodUsers(id);
		model.addAttribute("goodUsers", goodUsers);
		//自分の投稿か確認
		long myPostCheck = placeRep.myPostCheck(id);
		//自分の投稿だった場合
		if(myPostCheck == userId) {
			model.addAttribute("myPost", "myPost");
		//他人の投稿だった場合
		}else {
			//すでに高評価しているか確認
			List<Good> didGoodCheck = goodRep.didGoodCheck(userId, id);
			if(didGoodCheck.isEmpty()) {
				model.addAttribute("noGood", "noGood");
				common.sessionSet(model);
			}
		}
		return "placeDetail";
	}
}
