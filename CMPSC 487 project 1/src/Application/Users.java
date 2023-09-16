package Application;

public class Users {
	
	private String id = new String();
	private String name = new String();
	private String type = new String();
	private String status = new String();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String isStatus(boolean s) {
		if(s) {
			return "Access";
		} else {
			return "Suspended";
		}
	}
	
	
	
}
