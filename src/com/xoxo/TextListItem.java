package com.xoxo;

public class TextListItem {
	private String pointsReason;
	private String friendName;
	private String pointsRecieved;

	public TextListItem(String friend, String reason, String points) {
		this.setPointsReason(reason);
		this.setFriendName(friend);
		this.setPointsRecieved(points);
	}


	public String getPointsRecieved() {
		return pointsRecieved;
	}

	public void setPointsRecieved(String points) {
		this.pointsRecieved = points;
	}


	public String getFriendName() {
		return friendName;
	}


	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}


	public String getPointsReason() {
		return pointsReason;
	}


	public void setPointsReason(String pointsReason) {
		this.pointsReason = pointsReason;
	}

}