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

//アクセサを内蔵
@Data
//コンストラクタを内蔵
@AllArgsConstructor
@NoArgsConstructor

@Table(name="mumeisyo")
public class User {	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@NotNull
	@Column(name="name")
	private String name;
	
	@NotNull
	@Column(name="password")
	private String password;
	
	@Column(name="createDate")
	private Timestamp createDate;
	
	@Column(name="updateDate")
	private Timestamp updateDate;
}
