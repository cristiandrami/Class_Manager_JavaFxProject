package application.student;

import java.io.Serializable;

public class VotesTableModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6405345507381663513L;
	private String name, vote;

	public VotesTableModel(String name, String vote) {
		super();
		this.name = name;
		this.vote = vote;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}
	

}
