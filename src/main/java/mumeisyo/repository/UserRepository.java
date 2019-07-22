package mumeisyo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mumeisyo.model.User;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	//ログイン認証、情報変更前に本人確認
	@Query(value = "SELECT * FROM user WHERE name = :name AND password = :password", nativeQuery = true)
	public List<User> login(String name, String password);
	
	//IDをセッション登録用に取得
	@Query(value = "SELECT id FROM user WHERE name = :name AND password = :password", nativeQuery = true)
	public long getId(String name, String password);
	
	//ユーザー名被り確認
	@Query(value = "SELECT * FROM user WHERE name = :name", nativeQuery = true)
	public List<User> userCheck(String name);
	
	//ユーザー新規登録
	@Modifying
	@Query(value = "INSERT INTO user (name, password) VALUES (:name, :password)", nativeQuery = true)
	public void userCreate(String name, String password);
	
	//一言更新
	@Modifying
	@Query(value = "UPDATE user SET greet = :greet WHERE id = :id", nativeQuery = true)
	public void greet(String greet, long id);
	
	//ユーザー情報取得
	@Query(value = "SELECT * FROM user WHERE id = :id", nativeQuery = true)
	public List<User> getUserData(long id);
	
	//ユーザー情報変更
	@Modifying
	@Query(value = "UPDATE user SET name = :name, password = :password, update_date = now() WHERE name = :session_name", nativeQuery = true)
	public void userUpdate(String name, String password, String session_name);
	
	//退会
	@Modifying
	@Query(value = "DELETE FROM user WHERE user_id = :user_id AND name = :name", nativeQuery = true)
	public void userDelete(long user_id, String name);
}
