package tw.iiijdbc.project.main;

import java.io.Serializable;

public class MaskPos implements Serializable {

	// 序列化版本
	private static final long serialVersionUID = 1L;

	
	// primary key
	private int id;
	
	private String hospitalName;
	private String hospitalAddress;
	private String hospitalPhone;
	private int maskAdult;
	private int maskChildren;
	private String dataTime;

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public String getHospitalPhone() {
		return hospitalPhone;
	}

	public void setHospitalPhone(String hospitalPhone) {
		this.hospitalPhone = hospitalPhone;
	}

	public int getMaskAdult() {
		return maskAdult;
	}

	public void setMaskAdult(int maskAdult) {
		this.maskAdult = maskAdult;
	}

	public int getMaskChildren() {
		return maskChildren;
	}

	public void setMaskChildren(int maskChildren) {
		this.maskChildren = maskChildren;
	}

	// 建構子
	public MaskPos() {
	}

	@Override
	public String toString() {
		// 使用Generate toString... code style勾選StringBuffer(chained call)產生
		StringBuilder builder = new StringBuilder();
		builder.append("ID=").append(id);
		if (hospitalName != null)
			builder.append("\n醫事機構名稱=").append(hospitalName);
		if (hospitalAddress != null)
			builder.append("\n醫事機構地址=").append(hospitalAddress);
		if (hospitalPhone != null)
			builder.append("\n機構聯絡電話=").append(hospitalPhone);
		builder.append("\n該醫院剩餘之'成人'口罩數量=").append(maskAdult);
		builder.append("\n該醫院剩餘之'孩童'口罩數量=").append(maskChildren);
		if (dataTime != null)
			builder.append("\n來源資料時間=").append(dataTime);
		builder.append("]");
		return builder.toString();
	}
	

}
