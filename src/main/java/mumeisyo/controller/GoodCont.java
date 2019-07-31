package mumeisyo.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mumeisyo.model.Good;
import mumeisyo.model.Place;
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
	HttpSession session;
	@Autowired
	Common common;
	@Autowired
	PostDetail postDetail;
	
	//高評価リストを取得
	@PostMapping("/goodPosts")
	public String goodPosts(@RequestParam("good") String good, Model model) {
		List<Place> placeList = null;
		long userId = (long)session.getAttribute("userId");
		//高評価済みを選んだ場合
		if(good.equals("isGood")) {
			placeList = placeRep.getIsGood(userId);
			model.addAttribute("goodList", "あなたが高評価した投稿");
		//指定なしの場合
		}else {
			placeList = placeRep.getPlace();
			model.addAttribute("sortResult", "新しい順");
		}
		//該当しなかった場合
		if(placeList.isEmpty()) {
			model.addAttribute("noChoose", "条件に合う投稿はありません。");
		}
		model.addAttribute("placeList", placeList);

		//セッション読み込み
		common.sessionGet(model);
		return "browse";
	}
	
	//高評価を追加する
	@PostMapping("/good")
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
