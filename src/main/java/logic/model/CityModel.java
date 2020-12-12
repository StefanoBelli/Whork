package logic.model;

public final class CityModel {
	private String cap;
	private String name;
	private ProvinceModel province;

	public ProvinceModel getProvince() {
		return province;
	}

	public void setProvince(ProvinceModel province) {
		this.province = province;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
