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
@Table(name = "APP")
public class App {
	private Integer id;
	private String aid;
	private Integer visitedTimes;
	private String name;
	private AppType aType;
	private Date registerDate;
	private Date updateDate;
	private String url;
	private YDUser provider;
	private Integer searchedTime;
	@Column(name = "SEARCHEDTIME" )
	public Integer getSearchedTime() {
		return searchedTime;
	}

	public void setSearchedTime(Integer searchedTime) {
		this.searchedTime = searchedTime;
	}
	@ManyToOne(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "APPProvider_ID", updatable = true)
	public YDUser getProvider() {
		return provider;
	}
	public void setProvider(YDUser provider) {
		this.provider = provider;
	}
	@Column(name = "URL", length = 512)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	@Column(name = "APPID")
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	@Column(name = "VISITEDTIME" )
	public Integer getVisitedTimes() {
		return visitedTimes;
	}
	public void setVisitedTimes(Integer visitedTimes) {
		this.visitedTimes = visitedTimes;
	}
	@Column(name = "NAME" )
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "APP_ID", updatable = true)
	public AppType getaType() {
		return aType;
	}
	public void setaType(AppType aType) {
		this.aType = aType;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registerDate", length = 32)
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateDate", length = 32)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
