package vo;

public class Module {
	private Integer id;
	private String name;
	private String registerDate;
	private String updateDate;
	private Integer visitedTotalTimes;
	public Integer getVisitedTotalTimes() {
		return visitedTotalTimes;
	}
	public void setVisitedTotalTimes(Integer visitedTotalTimes) {
		this.visitedTotalTimes = visitedTotalTimes;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	
}
