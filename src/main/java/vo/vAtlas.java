package vo;
public class vAtlas {
	
	private String aurl;
	private String registerDate;
	private String updateDate;
	private String atlasType;
	private Integer Id;
	private String aid;
	private String name;
	private Integer searchedTime;

	public Integer getSearchedTime() {
		return searchedTime;
	}

	public void setSearchedTime(Integer searchedTime) {
		this.searchedTime = searchedTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private Integer visiteTotalTime;
	private Integer downloadTime;
	public Integer getDownloadTime() {
		return downloadTime;
	}
	public void setDownloadTime(Integer downloadTime) {
		this.downloadTime = downloadTime;
	}
	private String isNew;
	private String provider;
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	
	public String getAurl() {
		return aurl;
	}
	public void setAurl(String aurl) {
		this.aurl = aurl;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
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
	
	
	
	public String getAtlasType() {
		return atlasType;
	}
	public void setAtlasType(String atlasType) {
		this.atlasType = atlasType;
	}
	public Integer getVisiteTotalTime() {
		return visiteTotalTime;
	}
	public void setVisiteTotalTime(Integer visiteTotalTime) {
		this.visiteTotalTime = visiteTotalTime;
	}
}
