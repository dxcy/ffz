package vo;

public class vMonitoring {
	private Integer id;
public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
private String url;
private String statut;
private Integer code;
private String name;
private String registerDate;
private String imgSrc;
public String getImgSrc() {
	return imgSrc;
}
public void setImgSrc(String imgSrc) {
	this.imgSrc = imgSrc;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getRegisterDate() {
	return registerDate;
}
public void setRegisterDate(String registerDate) {
	this.registerDate = registerDate;
}
public String getUpdateDate() {
	return updateDate;
}
public void setUpdateDate(String updateDate) {
	this.updateDate = updateDate;
}
private String updateDate;
public Integer getCode() {
	return code;
}
public void setCode(Integer code) {
	this.code = code;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getStatut() {
	return statut;
}
public void setStatut(String statut) {
	this.statut = statut;
}

}
