package tw.iiijdbc.project.main;

import java.util.StringJoiner;

public class ShowMaskInfo {
	
//	private static int maskID;
//	private static String hospitalName;
//	private static String hospitalAddress;
//	private static String hospitalPhone;
//	private static int maskAdult;
//	private static int maskChildren;
//	private static String dataTime;
//	
	
	public static void show(MaskPos mPos) {
		if (mPos != null) {
			StringJoiner sj = new StringJoiner(System.lineSeparator());
			sj.add("====================")
			.add("ID: " + mPos.getId())
			.add("名稱: " + mPos.getHospitalName())
			.add("地址: " + mPos.getHospitalAddress())
			.add("電話: " + mPos.getHospitalPhone())
			.add("成人口罩剩餘數量: " + mPos.getMaskAdult())
			.add("兒童口罩剩餘數量: " + mPos.getMaskChildren())
			.add("登錄日期: " + mPos.getDataTime())
			.add("====================");
			System.out.println(sj.toString());
		}else {
			System.out.println("無此筆MaskPos資料!");
		}
	}


//	public showMaskInfo(int maskID) {
//		this.maskID = maskID;
//	}
}
