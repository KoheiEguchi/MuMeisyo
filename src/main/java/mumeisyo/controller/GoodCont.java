package mumeisyo.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.model.Good;
import mumeisyo.repository.GoodRepository;
import mumeisyo.repository.PlaceRepository;
import mumeisyo.service.Common;

@Controller
public class GoodCont {
	@Autowired
	GoodRepository goodRep;
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	Common common;
	@Autowired
	PostDetail postDetail;
	
	//高評価を追加する
	@RequestMapping(value = "/good", method = RequestMethod.POST)
	public String goodPlus(@RequestParam("id")long placeId, @RequestParam("name")String name, @RequestParam("userId")long userId, Model model) 
			throws SQLException {
		//すでに高評価しているか確認
		List<Good> didGoodCheck = goodRep.didGoodCheck(userId, placeId);
		//高評価していない場合
		if(didGoodCheck.isEmpty()) {
			//高評価詳細追加
			goodRep.goodPlus(userId, name, placeId);
			//高評価数追加
			placeRep.goodNumPlus(placeId);
			model.addAttribute("msg", "高評価しました。");
		//高評価している場合
		}else {
			model.addAttribute("msg", "すでに高評価しています。");
		}
		
		//投稿詳細に戻る
		postDetail.postDetailOpen(placeId, userId, model);
		return "postDetail";
	}
}
