package mumeisyo.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.repository.GoodRepository;
import mumeisyo.repository.PlaceRepository;
import mumeisyo.service.Common;

@Controller
public class Good {
	@Autowired
	GoodRepository goodRep;
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	Common common;
	
	//高評価を追加する
	@RequestMapping(value = "/good", method = RequestMethod.GET)
	public String goodPlus(@RequestParam("id")long placeId, @RequestParam("name")String name, Model model) throws SQLException {
		//高評価詳細追加
		goodRep.goodPlus(name, placeId);
		//高評価数追加
		placeRep.goodNumPlus(placeId);
		model.addAttribute("msg", "高評価しました。");
		//最新の投稿3件を取得
		common.getNewPlaceList(model);
		return "top";
	}
}
