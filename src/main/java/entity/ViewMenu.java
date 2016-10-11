package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ViewMmenu")
public class ViewMenu {
	@Column(name = "STATE", length = 32)
	public String getState() {
		return state;
	}
	public ViewMenu() {
		super();
	}
	public void setState(String state) {
		this.state = state;
	}

	private Integer id;
	private String iconCls;
	@Column(name = "ICONCLS", length = 32)
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	private Integer pid;//父节点,pid为空时为过根节点
	private String text;//菜单名称
	private String url;//点击菜单访问的url
	private String seq;//排序
	private String state;
	private int checked;
	
	@Column(name = "CHECKED")
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
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
	@Column(name = "PID", length = 32)
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	@Column(name = "TEXT", length = 32)
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Column(name = "URL", length = 32)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "SEQ", length = 32)
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	
	
	
}
