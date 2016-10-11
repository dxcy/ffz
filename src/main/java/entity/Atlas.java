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
@Table(name = "ATLAS")
public class Atlas {
	private Integer id;
	private String aid;
	private Integer visitedTimes;
	private Integer downloadTime;
	private String name;
	private atlasType aType;
	private Date registerDate;
	private Date updateDate;
	private YDUser provider;
	private String aUrl;
	private Integer searchedTime;
	@Column(name = "SEARCHEDTIME" )
	public Integer getSearchedTime() {
		return searchedTime;
	}

	public void setSearchedTime(Integer searchedTime) {
		this.searchedTime = searchedTime;
	}
	@Column(name = "AURL", length = 512)
	public String getaUrl() {
		return aUrl;
	}
	public void setaUrl(String aUrl) {
		this.aUrl = aUrl;
	}
	@ManyToOne(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "ATLASProvider_ID", updatable = true)
	public YDUser getProvider() {
		return provider;
	}
	public void setProvider(YDUser provider) {
		this.provider = provider;
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
	@Column(name = "ATLASID")
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
	@Column(name = "DOWNLOADTIME" )
	public Integer getDownloadTime() {
		return downloadTime;
	}
	public void setDownloadTime(Integer downloadTime) {
		this.downloadTime = downloadTime;
	}
	@Column(name = "ATLASNAME" )
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "ATLAS_ID", updatable = true)
	public atlasType getaType() {
		return aType;
	}
	public void setaType(atlasType aType) {
		this.aType = aType;
	}
	
	
}
