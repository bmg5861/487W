package Application;

import java.sql.Timestamp;

public class Access {
	
	private String id = new String();
	private String name = new String();
	private String type = new String();
	private String swipeType = new String();
	private Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
	
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
	public String getSwipeType() {
		return swipeType;
	}
	public void setSwipeType(String swipeType) {
		this.swipeType = swipeType;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	
	
	
	
}