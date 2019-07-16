package mumeisyo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mumeisyo.model.Forum;

@Transactional
@Repository
public interface ForumRepository extends JpaRepository<Forum, Long>{
	//意見投稿
	@Modifying
	@Query(value = "INSERT INTO forum (address, forum) VALUES (:address, :forum)", nativeQuery = true)
	public void postForum(String address, String forum);
	
	//意見一覧表示
	@Query(value = "SELECT * FROM forum ORDER BY forum_date DESC", nativeQuery = true)
	public List<Forum> getForum();
}
