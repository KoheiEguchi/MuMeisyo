package mumeisyo.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="mumeisyo")
public class Place {
	//自分の投稿かの確認用
	public Place(String name) {
		this.name = name;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@NotNull
	@Column(name="name")
	private String name;
	
	@NotNull
	@Column(name="user_id")
	private long userId;
	
	@Column(name="pic")
	private String pic;
	
	@NotNull
	@Column(name="text")
	private String text;
	
	@Column(name="post_date")
	private Timestamp postDate;
	
	@Column(name="good")
	private int good;
}
