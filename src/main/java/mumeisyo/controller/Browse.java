package mumeisyo.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	@GetMapping("/browse")
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
			common.sessionGet(model);
			return "browse";
		}
	}
	
	//投稿を並べ替え
	@PostMapping("/browseSort")
	public String browseSort(@RequestParam("sort") String sort, Model model) throws SQLException {
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
		common.sessionGet(model);
		return "browse";
	}
	
	//投稿を絞り込み
	@PostMapping("/browseChoose")
	public String browseChoose(@RequestParam("name") String name, @RequestParam("pic") String pic, Model model) {
		List<Place> placeList = null;
		//写真ありの場合
		if(pic.equals("isPic")) {
			placeList = placeRep.chooseIsPic(name);
		//写真なしの場合
		}else if(pic.equals("noPic")){
			placeList = placeRep.chooseNoPic(name);
		//写真指定なしの場合
		}else {
			placeList = placeRep.chooseBothPic(name);
		}
		//該当しなかった場合
		if(placeList.isEmpty()) {
			model.addAttribute("noChoose", "条件に合う投稿はありません。");
		}
		model.addAttribute("placeList", placeList);
		model.addAttribute("sortResult", "新しい順");

		//セッション読み込み
		common.sessionGet(model);
		return "browse";
	}
	
	//特定の語句で検索する
	@PostMapping("/wordSearch")
	public String wordSearch(@RequestParam("word") String word, Model model) {
		List<Place> placeList = placeRep.wordSearch(word);
		model.addAttribute("placeList", placeList);
		//検索語句の表示
		if(!(word.equals(""))){
			model.addAttribute("sortResult", "「" + word + "」での検索結果");
		}else {
			model.addAttribute("sortResult", "新しい順");
		}
		
		//該当しなかった場合
		if(placeList.isEmpty()) {
			model.addAttribute("noChoose", "条件に合う投稿はありません。");
		}
		
		//セッション読み込み
		common.sessionGet(model);
		return "browse";
	}
	
}
