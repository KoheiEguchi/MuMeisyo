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
	@Query(value = "SELECT * FROM place ORDER BY post_date DESC LIMIT 3", nativeQuery = true)
	public List<Place> newPlace();
	
	//投稿内容を全て取得
	@Query(value = "SELECT * FROM place", nativeQuery = true)
	public List<Place> getPlace();
	
	//一覧を古い順に並べ替え
	@Query(value = "SELECT * FROM place ORDER BY post_date", nativeQuery = true)
	public List<Place> getPlaceOld();
	//一覧を新しい順に並べ替え
	@Query(value = "SELECT * FROM place ORDER BY post_date DESC", nativeQuery = true)
	public List<Place> getPlaceNew();
	//一覧を評価の多い順に並べ替え
	@Query(value = "SELECT * FROM place ORDER BY good DESC", nativeQuery = true)
	public List<Place> getPlaceGood();
	//一覧を評価の少ない順に並べ替え
	@Query(value = "SELECT * FROM place ORDER BY good", nativeQuery = true)
	public List<Place> getPlaceBad();
	
	//一覧を指定名つき、写真ありで絞り込み
	@Query(value = "SELECT * FROM place WHERE name LIKE %:name% AND pic IS NOT NULL ORDER BY post_date DESC", nativeQuery = true)
	public List<Place> chooseIsPic(String name);
	//一覧を指定名つき、写真なしで絞り込み
	@Query(value = "SELECT * FROM place WHERE name LIKE %:name% AND pic IS NULL ORDER BY post_date DESC", nativeQuery = true)
	public List<Place> chooseNoPic(String name);
	//一覧を指定名つき、写真指定なしで絞り込み
	@Query(value = "SELECT * FROM place WHERE name LIKE %:name% ORDER BY post_date DESC", nativeQuery = true)
	public List<Place> chooseBothPic(String name);
	
	//高評価済みを指定
	@Query(value = "SELECT * FROM place JOIN good ON place.id = good.place_id WHERE good.user_id = :user_id AND place.id IN "
			+ "(SELECT good.place_id FROM good WHERE good.user_id = :user_id)", nativeQuery = true)
	public List<Place> getIsGood(long user_id);
	
	//特定語句検索
	@Query(value = "SELECT * FROM place WHERE name LIKE %:word% OR text LIKE %:word% ORDER BY post_date DESC", nativeQuery = true)
	public List<Place> wordSearch(String word);
	
	//指定された投稿の詳細を取得
	@Query(value = "SELECT * FROM place WHERE id = :id", nativeQuery = true)
	public List<Place> getPostDetail(long id);
	
	//自分の投稿か確認
	@Query(value = "SELECT user_id FROM place WHERE id = :id", nativeQuery = true)
	public int myPostCheck(long id);
	
	//ユーザーの投稿履歴取得
	@Query(value = "SELECT * FROM place WHERE user_id = :user_id ORDER BY post_date DESC", nativeQuery = true)
	public List<Place> userPlace(long user_id);
	
	//新規投稿
	@Modifying
	@Query(value = "INSERT INTO place (name, user_id, pic, text) VALUES (:name, :user_id, :pic_name, :text)", nativeQuery = true)
	public void newPost(String name, long user_id, String pic_name,  String text);
	
	//高評価数追加
	@Modifying
	@Query(value = "UPDATE place SET good = good + 1 WHERE id = :id", nativeQuery = true)
	public void goodNumPlus(long id);
	
	//投稿削除
	@Modifying
	@Query(value = "DELETE FROM place WHERE id = :id", nativeQuery = true)
	public void postDelete(long id);
}
