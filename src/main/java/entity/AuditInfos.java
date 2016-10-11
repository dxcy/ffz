package entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "AuditInfos")
public class AuditInfos {
	private Integer id;
	private String USERIP;
	private String USERTYPE;
	private Date BEHAVETIME;
	private String USERID;
	private String dataType;
	private String BEHAVETYPE;
	private String URL;
	private String BUSSTYPE;
	private String USERNAME;
	private String KEYWORD;
	private String BEHAVEID;
	private String BEHAVENAME;
	private Date AuditDate;
	
	private String provider;
	@Column(name = "PROVIDER", length = 32)
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUDITDATE", length = 32)
	public Date getAuditDate() {
		return AuditDate;
	}
	public void setAuditDate(Date auditDate) {
		AuditDate = auditDate;
	}
	@Column(name = "URL", length = 512)
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	//业务类型，01图集，02 应用，03数据，04产品，05服务
	@Column(name = "BUSSTYPE", length = 8)
	public String getBUSSTYPE() {
		return BUSSTYPE;
	}
	public void setBUSSTYPE(String bUSSTYPE) {
		BUSSTYPE = bUSSTYPE;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BEHAVETIME", length = 32)
	public Date getBEHAVETIME() {
		return BEHAVETIME;
	}
	public void setBEHAVETIME(Date bEHAVETIME) {
		BEHAVETIME = bEHAVETIME;
	}
	@Column(name = "BEHAVENAME", length = 64)
	public String getBEHAVENAME() {
		return BEHAVENAME;
	}
	public void setBEHAVENAME(String bEHAVENAME) {
		BEHAVENAME = bEHAVENAME;
	}
	@Column(name = "BEHAVEID", length = 64)
	public String getBEHAVEID() {
		return BEHAVEID;
	}
	public void setBEHAVEID(String bEHAVEID) {
		BEHAVEID = bEHAVEID;
	}
	@Column(name = "KEYWORD", length = 64)
	public String getKEYWORD() {
		return KEYWORD;
	}
	public void setKEYWORD(String kEYWORD) {
		KEYWORD = kEYWORD;
	}
	@Column(name = "USERNAME", length = 64)
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	@Column(name = "UID", length = 64)
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	
	
	
	
	//操作对象类型；01下载02预览,03查询
	@Column(name = "BEHAVETYPE", length = 16)
	public String getBEHAVETYPE() {
		return BEHAVETYPE;
	}
	public void setBEHAVETYPE(String bEHAVETYPE) {
		BEHAVETYPE = bEHAVETYPE;
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
	
	@Column(name = "DATATYPE", length = 32)
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "USERIP", length = 64)
	public String getUSERIP() {
		return USERIP;
	}
	public void setUSERIP(String uSERIP) {
		USERIP = uSERIP;
	}
	@Column(name = "USERTYPE", length = 64)
	public String getUSERTYPE() {
		return USERTYPE;
	}
	public void setUSERTYPE(String uSERTYPE) {
		USERTYPE = uSERTYPE;
	}	
}
