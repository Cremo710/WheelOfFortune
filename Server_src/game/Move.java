package game;

import java.io.Serializable;
import java.util.ArrayList;

public class Move implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String owner;
	private int points;
	private ArrayList<String> to;
	
	public Move(String type,String owner,int points,ArrayList<String> to){
		this.type=type;
		this.owner=owner;
		this.points=points;
		this.to=to;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public ArrayList<String> getTo() {
		return to;
	}

	public void setTo(ArrayList<String> to) {
		this.to = to;
	}

}
