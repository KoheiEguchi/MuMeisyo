package mumeisyo.controller;

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
public class PostDelete {
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	Common common;
	
	//投稿削除ページを開く
	@RequestMapping(value = "postDelete", method = RequestMethod.GET)
	public String postDeleteOpen(@RequestParam("placeId") long placeId, Model model){
		//ログインしていない場合ログインページへ送る
		boolean loginCheck = common.loginCheck(model);
		if(loginCheck == false) {
			return "login";
		}else {
			//管理人以外トップページへ送る
			boolean adminCheck = common.adminCheck(model);
			if(adminCheck == false) {
				return "top";
			}else {
				//指定された投稿の詳細を取得
				List<Place> deletePost = placeRep.getPostDetail(placeId);
				model.addAttribute("deletePost", deletePost);
				return "postDelete";
			}
		}
	}
	
	//投稿を削除する
	@RequestMapping(value = "deletePostOk", method = RequestMethod.GET)
	public String postDelete(@RequestParam("placeId") long placeId, Model model) {
		//ログインしていない場合ログインページへ送る
		boolean loginCheck = common.loginCheck(model);
		if(loginCheck == false) {
			return "login";
		}else {
			//管理人以外トップページへ送る
			boolean adminCheck = common.adminCheck(model);
			if(adminCheck == false) {
				return "top";
			}else {
				//投稿削除
				placeRep.postDelete(placeId);
				model.addAttribute("msg", "削除しました。");
				
				//セッション読み込み
				common.sessionSet(model);
				//最新の投稿3件を取得
				common.getNewPlaceList(model);
				return "top";
			}
		}
	}
}
