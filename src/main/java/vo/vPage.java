package vo;
public class vPage {
	private Integer id;
	private String registerDate;
	private String pid;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public Integer getVisitedTimes() {
		return visitedTimes;
	}
	public void setVisitedTimes(Integer visitedTimes) {
		this.visitedTimes = visitedTimes;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getBmName() {
		return bmName;
	}
	public void setBmName(String bmName) {
		this.bmName = bmName;
	}
	private Integer visitedTimes;
	private Integer visitedTotalTimes;
	public Integer getVisitedTotalTimes() {
		return visitedTotalTimes;
	}
	public void setVisitedTotalTimes(Integer visitedTotalTimes) {
		this.visitedTotalTimes = visitedTotalTimes;
	}
	private String Url;
	private String updateDate;
	private String bmName;
}
