package provaURL;

import java.util.ArrayList;
import java.util.List;

public class Technique {
	private int ID;
	private String name;
	private List<String> platforms;
	private List<String> tactics;
	private List<String> permissions;	
	private List<String> Effpermissions;


	public Technique (int ID, String name) {
		this.ID=ID;
		this.name=name;
		platforms = new ArrayList<String>();
		tactics = new ArrayList<String>();
		permissions = new ArrayList<String>();
		Effpermissions = new ArrayList<String>();

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
	
	public String getTactic(int index) {
		if (index > platforms.size())
			return " ";
		return tactics.get(index);
	}
	public List<String> getTactic() {
		return tactics;
	}
	public void addTactic(String newTactic) {
		tactics.add(newTactic);
	}
	
	public String getPermission(int index) {
		if (index > permissions.size())
			return " ";
		return permissions.get(index);
	}
	public List<String> getPermission() {
		return permissions;
	}
	public void addPermission(String newPermission) {
		permissions.add(newPermission);
	}
	public String getEffPermission(int index) {
		if (index > Effpermissions.size())
			return " ";
		return Effpermissions.get(index);
	}
	public List<String> getEffPermission() {
		return Effpermissions;
	}
	public void addEffPermission(String newEffPermission) {
		Effpermissions.add(newEffPermission);
	}
}

