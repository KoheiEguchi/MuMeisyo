package mumeisyo.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import mumeisyo.model.Place;
import mumeisyo.model.User;
import mumeisyo.repository.PlaceRepository;
import mumeisyo.repository.UserRepository;

@Service
public class Common {
	@Autowired
	UserRepository userRep;
	@Autowired
	PlaceRepository placeRep;
	@Autowired
	HttpSession session;
	
	//ログインしていない場合ログインページへ送る
	public boolean loginCheck(Model model) {
		boolean check = false;
		if(session.getAttribute("name") == null) {
			model.addAttribute("msg", "ログインしていません。");
			check = false;
		}else {
			//セッション読み込み
			sessionGet(model);
			check = true;
		}
		return check;
	}
	
	//管理人以外トップページへ送る
	public boolean adminCheck(Model model) {
		boolean check = false;
		long userId = (long)session.getAttribute("userId");
		if(userId != 1) {
			model.addAttribute("msg", "そのページは管理人以外閲覧できません。");
			check = false;
		}else {
			//セッション読み込み
			sessionGet(model);
			check = true;
		}
		return check;
	}
	
	//セッション読み込み
	public void sessionGet(Model model) {
		//セッションのID読み込み
		long userId = (long)session.getAttribute("userId");
		model.addAttribute("userId", userId);
		//セッションの名前読み込み
		String name = (String)session.getAttribute("name");
		model.addAttribute("name", name);
	}
	
	//最新の投稿3件を取得
	public void getNewPlaceList(Model model) {
		List<Place> newPlaceList = placeRep.newPlace();
		model.addAttribute("newPlaceList", newPlaceList);
	}
	
	//画像を保存する
	public String picSave(MultipartFile pic) {
		//画像投稿がなかったら名前はnullになる
		String picName = null;
		//画像が指定された場合
		if(!(pic.isEmpty())) {
			//画像保存フォルダが存在しない場合
			Path path = Paths.get("src/main/resources/static/pic/post");
			if(!(Files.exists(path))) {
				try {
					//保存フォルダを作成
					Files.createDirectory(path);
				}catch(NoSuchFileException ne) {
					ne.printStackTrace();
				}catch(IOException ie) {
					ie.printStackTrace();
				}
			//保存フォルダが存在するならそのまま
			}
			
			//拡張子取得のためドットの位置を取得
			int dot = pic.getOriginalFilename().lastIndexOf(".");
			String extention = "";
			if(dot > 0) {
				//拡張子取得
				extention = pic.getOriginalFilename().substring(dot).toLowerCase();
			}
			//現在日時からファイル名生成
			String fileName = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
			//ファイル名と拡張子を組み合わせる
			picName = "pic/post/" + fileName + extention;
			//保存場所を指定する
			Path uploadFile = Paths.get("src/main/resources/static/" + picName);
			//画像を指定された場所に保存
			try(OutputStream os = Files.newOutputStream(uploadFile, StandardOpenOption.CREATE)) {
				byte[] bytes = pic.getBytes();
				os.write(bytes);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		return picName;
	}
	
	//ユーザー情報と投稿履歴を取得する
	public void getUserDetail(long userId, Model model) {
		//ユーザー情報を取得する
		List<User> userData = userRep.getUserData(userId);
		model.addAttribute("userData", userData);
		//ユーザーの投稿履歴を取得する
		List<Place> placeList = placeRep.userPlace(userId);
		model.addAttribute("placeList", placeList);
	}
	
}
