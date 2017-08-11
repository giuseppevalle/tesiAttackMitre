package provaURL;

import java.util.ArrayList;
import java.util.List;

public class Technique {
	private int ID;
	private String name;
	private List<String> platforms;
	public Technique (int ID, String name) {
		this.ID=ID;
		this.name=name;
		platforms = new ArrayList<String>();
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int newID) {
		ID = newID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name=newName;
	}
	
	public String getPlatform(int index) {
		if (index > platforms.size())
			return " ";
		return platforms.get(index);
	}
	
	public List<String> getPlatforms() {
		return platforms;
	}
	
	public void addPlatform(String newPlatform) {
		platforms.add(newPlatform);
	}
}

