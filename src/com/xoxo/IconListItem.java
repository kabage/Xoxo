package com.xoxo;

public class IconListItem {
	private String name;
	private String description;
	private int redValue, greenValue, blueValue;

	public IconListItem(String i, String d, int colorRed, int colorGreen,
			int colorBlue) {
		this.description = d;
		this.name = i;
		this.setRedValue(colorRed);
		this.setGreenValue(colorGreen);
		this.setBlueValue(colorBlue);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRedValue() {
		return redValue;
	}

	public void setRedValue(int redValue) {
		this.redValue = redValue;
	}

	public int getGreenValue() {
		return greenValue;
	}

	public void setGreenValue(int greenValue) {
		this.greenValue = greenValue;
	}

	public int getBlueValue() {
		return blueValue;
	}

	public void setBlueValue(int blueValue) {
		this.blueValue = blueValue;
	}

}