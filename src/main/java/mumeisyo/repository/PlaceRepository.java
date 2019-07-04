package mumeisyo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mumeisyo.model.Place;

@Transactional
@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
	//最新の投稿3件を取得
	@Query(value = "SELECT * FROM place ORDER BY post_date DESC LIMIT 4", nativeQuery = true)
	public List<Place> newPlace();
	
	//投稿内容を全て取得
	@Query(value = "SELECT * FROM place", nativeQuery = true)
	public List<Place> getPlace();
	
	//ユーザーの投稿履歴取得
	@Query(value = "SELECT * FROM place WHERE name = :name", nativeQuery = true)
	public List<Place> userPlace(String name);
	
	//新規投稿
	@Modifying
	@Query(value = "INSERT INTO place (name, pic, text) VALUES (:name, :pic_name, :text)", nativeQuery = true)
	public void newPost(String name, String pic_name,  String text);
	
	//高評価追加
	@Modifying
	@Query(value = "UPDATE place SET good = good + 1 WHERE id = :id", nativeQuery = true)
	public void goodPlus(long id);
}
