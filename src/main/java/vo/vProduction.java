package vo;
public class vProduction {
	private Integer id;
	private String proid;
	private Integer visitedTimes;
	private Integer downloadTime;
	private String name;
	private String pType;
	private String registerDate;
	private String updateDate;
	private String provider;
	private String url;
	private Integer searchedTime;

	public Integer getSearchedTime() {
		return searchedTime;
	}

	public void setSearchedTime(Integer searchedTime) {
		this.searchedTime = searchedTime;
	}
	public String getpType() {
		return pType;
	}
	public void setpType(String pType) {
		this.pType = pType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProid() {
		return proid;
	}
	public void setProid(String proid) {
		this.proid = proid;
	}
	public Integer getVisitedTimes() {
		return visitedTimes;
	}
	public void setVisitedTimes(Integer visitedTimes) {
		this.visitedTimes = visitedTimes;
	}
	public Integer getDownloadTime() {
		return downloadTime;
	}
	public void setDownloadTime(Integer downloadTime) {
		this.downloadTime = downloadTime;
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
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
}
