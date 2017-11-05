package provaURL;

import java.util.ArrayList;

public class Example {
	private String ID;
	private String name;
	private String type;
	public Example (String ID, String name,String type) {
		this.ID=ID;
		this.name=name;
		this.type=type;
	}
	public String getID() {
		return ID;
	}
	
	public void setID(String iDExample) {
		ID = iDExample;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String newName) {
		name=newName;
	}
	public void setType(String newType) {
		type=newType;
	}
	public String getType() {
		return type;
	}	
}
