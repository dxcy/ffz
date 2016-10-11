package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
//一般使用javax.persisitence
@Table(name = "YDQUARTZ")
public class YDQuartz {
	private Integer id;
	private Integer state;
	@Id
	@GenericGenerator(name = "generator", strategy = "increment")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", length = 11)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "YDSTATE", length = 128)
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Column(name = "YDNAME", length = 128)
	public String getName() {
		return name;
	}
	
	private String group;
	@Column(name = "YDGROUP", length = 128)
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "YDDESCRIPTION", length = 128)
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	@Column(name = "CRONEXPRESSION", length = 128)
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	private String name;
	private String discription;
	private String cronExpression;
}
