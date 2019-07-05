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

@Controller
public class PlaceDetail {
	@Autowired
	PlaceRepository placeRep;
	
	//指定された投稿の詳細を表示
	@RequestMapping(value = "/placeDetail", method = RequestMethod.GET)
	public String placeDetailOpen(@RequestParam("id")long id, Model model) {
		List<Place> placeDetail = placeRep.getPlaceDetail(id);
		model.addAttribute("placeDetail", placeDetail);
		return "placeDetail";
	}
}
