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
@Table(name = "PRODUCTION")
public class Production {
	private Integer id;
	private String proid;
	private Integer visitedTimes;
	private Integer downloadTime;
	private String name;
	private ProductionType pType;
	private Date registerDate;
	private Date updateDate;
	private YDUser provider;
	private String url;
	@Column(name = "URL", length = 512)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@ManyToOne(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "ProProvider_ID", updatable = true)
	public YDUser getProvider() {
		return provider;
	}
	public void setProvider(YDUser provider) {
		this.provider = provider;
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
	
	@Column(name = "PROID")
	public String getProid() {
		return proid;
	}
	public void setProid(String proid) {
		this.proid = proid;
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
	@Column(name = "NAME" )
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(cascade={CascadeType.ALL}) 
	@JoinColumn(name = "PRODUCTION_ID", updatable = true)
	public ProductionType getpType() {
		return pType;
	}
	public void setpType(ProductionType pType) {
		this.pType = pType;
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
