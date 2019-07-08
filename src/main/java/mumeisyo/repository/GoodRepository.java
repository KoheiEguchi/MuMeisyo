package mumeisyo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mumeisyo.model.Good;

@Transactional
@Repository
public interface GoodRepository extends JpaRepository<Good, Long> {
	//高評価者一覧
	@Query(value = "SELECT * FROM good WHERE place_id = :place_id", nativeQuery = true)
	public List<Good> getGoodUsers(long place_id);
	
	//すでに高評価しているか確認
	@Query(value = "SELECT * FROM good WHERE name = :name AND place_id = :place_id", nativeQuery = true)
	public List<Good> didGoodCheck(String name, long place_id);
	
	//高評価詳細追加
	@Modifying
	@Query(value = "INSERT INTO good (name, place_id) VALUES (:name, :place_id)", nativeQuery = true)
	public void goodPlus(String name, long place_id);
}
