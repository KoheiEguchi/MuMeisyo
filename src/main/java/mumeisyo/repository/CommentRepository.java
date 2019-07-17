package mumeisyo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mumeisyo.model.Comment;

@Transactional
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	//コメント一覧
	@Query(value = "SELECT * FROM comment WHERE place_id = :place_id ORDER BY comment_date DESC", nativeQuery = true)
	public List<Comment> getComment(long place_id);
	
	//コメント投稿
	@Modifying
	@Query(value = "INSERT INTO comment (name, user_id, place_id, comment) VALUES (:name, :user_id, :place_id, :comment)", nativeQuery = true)
	public void postComment(String name, long user_id, long place_id, String comment);
}
