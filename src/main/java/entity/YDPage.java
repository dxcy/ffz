package entity;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "YDPage")
public class YDPage {
	private Integer id;
	private Date registerDate;
	private Integer visitedTimes;
	private String Url;
	private Date updateDate;
	private BModule bmodule;
	private String pid;
	@Column(name = "PID", length = 128)
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registerDate", length = 32)
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Integer getVisitedTimes() {
		return visitedTimes;
	}
	public void setVisitedTimes(Integer visitedTimes) {
		this.visitedTimes = visitedTimes;
	}
	@Column(name = "URL", length = 512)
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateDate", length = 32)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@ManyToOne(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "BID" , updatable = true)
	public BModule getBmodule() {
		return bmodule;
	}
	public void setBmodule(BModule bmodule) {
		this.bmodule = bmodule;
	}
}
