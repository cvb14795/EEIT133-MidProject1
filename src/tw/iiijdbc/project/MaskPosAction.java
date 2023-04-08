package tw.iiijdbc.project;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import tw.iiijdbc.project.main.MaskPos;
import tw.iiijdbc.project.main.MaskPosCSVParser;
import tw.iiijdbc.project.main.MaskPosDAO;
import tw.iiijdbc.project.main.MaskPosDAOFactory;
import tw.iiijdbc.project.main.ShowMaskInfo;

public class MaskPosAction {

	// 總共新增幾筆資料
	private static int insertedDataCount = 0;
	// 不要用/maps/place 因為缺少經緯度不能使用這個 用maps/search才可以直接用地址搜尋
	private static final StringBuilder gMapURLPrefix = new StringBuilder().append("https://www.google.com.tw/maps/search/");
	// 共用SC
	private static Scanner sc = new Scanner(System.in);
	private static final MaskPosDAO mDAO = MaskPosDAOFactory.getMaskPosDAO();
	// 程式提供的4種功能(4組鍵對)
	// 1.新增, 2.查詢, 3.修改, 4.刪除
	private static final Map<Integer, String> progFuncMap = new HashMap<Integer, String>();
	private static String errMsg;
	private static MaskPos mResult = null;

	static {
		progFuncMap.put(-1, "結束程式");
		progFuncMap.put(0, "讀取CSV");
		progFuncMap.put(1, "新增");
		progFuncMap.put(2, "查詢");
		progFuncMap.put(3, "修改");
		progFuncMap.put(4, "刪除");
		progFuncMap.put(5, "開啟查詢視窗UI介面");

	}
	
	public static void main(String[] args) {

		// 拆成幾個不同方法
		try { 
			int userChoice = -2;
			boolean isFirstRun = true;
			/* ========== 開啟連線 ========== */
			mDAO.createConn();
			do {
				// 第一次執行不檢查輸入
				if (isFirstRun) {
					mDAO.createTableIfNotExist();
					isFirstRun = false;
				}else {
					// 檢查前次輸入是否正確
					userChoice = checkUserInput(userChoice);
				}
				// 進入功能
				entryMainFunc(userChoice, isFirstRun);
			}while (userChoice != -1);
			mDAO.closeConn();
			/* ========== 關閉連線 ========== */

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("===== Something Wrong!! =====");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("===== 驅動程式載入錯誤！請檢查主程式內是否有包含對應資料庫之驅動程式！ =====");
			e.printStackTrace();
		} finally {
			System.out.println("===== 本程式執行完畢 =====");
		}

	}
	private static int checkUserInput(int userChoice) {
		boolean isVaild = false;
		final String INVAILD_INPUT = "-2";
		
		while (!isVaild) {
			if (INVAILD_INPUT.equals(progFuncMap.getOrDefault(userChoice, INVAILD_INPUT))) {
				errMsg = " 無此功能 輸入不正確! (請勿輸入非整數字元)!";
				userChoice = getUserInputInt(getWelcomeMsg(), errMsg);
				continue;
			}
			isVaild = true;
		}
		return userChoice;
	}
	
	private static int entryMainFunc(int userChoice, boolean isFirstRun) throws SQLException{
		if (!isFirstRun) {
			errMsg = " 無此功能 輸入不正確! (請勿輸入非整數字元)!";
			userChoice = getUserInputInt(getWelcomeMsg(), errMsg);			
		}
		System.out.println("執行功能: " + progFuncMap.get(userChoice));
		switch (userChoice) {
		// 讀取CSV
		case 0:
			// 從JDBC撈資料 獲取插入資料筆數
			insertedDataCount = createSqlDataFromCSV(mDAO);
			System.out.println("\n讀取結果：");
			if (insertedDataCount != 0) {
				System.out.println("共加入 " + insertedDataCount + " 筆 資料");
			} else {
				System.out.println("無任何資料被新增至資料庫！");
			}
			// 換行
			System.out.println();
			break;
		// 新增
		case 1:
			MaskPos mPos = new MaskPos();
			SimpleDateFormat simDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// 醫院資訊
			// 不採用"醫事機構代碼" 而使用目前資料筆數為ID
			errMsg = "請輸入正確的輸入值(請勿輸入非整數字元)!";
			String errMsgStr = "請輸入正確的輸入值!";
			int id = getUserInputInt("請輸入ID: ", errMsg);
			
			MaskPos oldMaskPos = mDAO.findById(id);
			while (oldMaskPos != null) {
				System.out.println("已有此ID資料!\n");
				System.out.println("====================");
				System.out.println(oldMaskPos.toString());
				System.out.println("====================");
				id = getUserInputInt("已有此ID資料, 請重新輸入ID: ", errMsg);
				if (id < 0) {					
					id = getUserInputInt("ID請輸入非負整數(不可小於0)!, 請重新輸入ID: ", errMsg);
				}
				oldMaskPos = mDAO.findById(id);
			}
			
			mPos.setId(id);
			mPos.setHospitalName(getUserInput("請輸入醫事機構名稱: ", errMsgStr));
			mPos.setHospitalAddress(getUserInput("請輸入醫事機構地址: ", errMsgStr));
			mPos.setHospitalPhone(getUserInput("請輸入該機構聯絡電話: ", errMsgStr));

			// 口罩數量
			mPos.setMaskAdult(getUserInputInt("請輸入該醫院剩餘之'成人'口罩數量: ", errMsg));
			mPos.setMaskChildren(getUserInputInt("請輸入該醫院剩餘之'孩童'口罩數量:", errMsg));
			// 最後更新日期(現在)
			Date dataDate = new Date(System.currentTimeMillis());
			mPos.setDataTime(simDateFormat.format(dataDate));
			mDAO.addMaskPos(mPos);
			break;
		// 查詢
		case 2:
			// 查詢使用者指定之輸入之搜尋結果
			mResult = findSqlDataByUserInput(mDAO);
			// 在瀏覽器顯示google map位置資訊
			showMapInfo(mResult);
			break;
		// 修改
		case 3:
			System.out.println("抱歉!本功能尚在開發中 (˘•ω•˘)...");
			break;
		// 刪除
		case 4:
			int delId = getUserInputInt("請輸入要刪除的ID", "刪除失敗!");
			mDAO.deleteById(delId);
			break;
		case 5:
			try {
				Runtime.getRuntime().exec("cmd.exe /C start java -jar MidTopicProject_SwingGUI.jar", null, Paths.get(System.getProperty("user.dir")).toFile());
			} catch (IOException e) {
			}
		case -1:
			break;
		}
		return userChoice;
	}
	
	private static int getUserInputInt(String hintMsg, String errMsg) {
		boolean isInputVaild = false;
		int intInput = 0;

		do {
			System.out.println(hintMsg);

			if (sc.hasNextInt()) {
				intInput = sc.nextInt();
				isInputVaild = true;
				break;
			} else {
//				System.out.println("test");
				// 讓游標切換到下一行 否則會讀不到input
				// 跑完這裡後會直接回到迴圈開頭
				sc.nextLine();
				System.out.println(errMsg);
			}
		} while (!isInputVaild);

		return intInput;
	}

	private static String getUserInput(String hintMsg, String errMsg) {
		String userInput = "";

		System.out.println(hintMsg);

		if (sc.hasNext()) {
			userInput = sc.next();
		} else {
			// 讓游標切換到下一行 否則會讀不到input
			// 跑完這裡後會直接回到迴圈開頭
			sc.nextLine();
			System.out.println(errMsg);
		}
		return userInput;
	}

	private static String getWelcomeMsg() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n==========口罩地圖查詢系統==========\n");
		sb.append(getAllFuncDesc());
		sb.append("\n等待使用者輸入中..");
		return sb.toString();
	}
	
	private static String getAllFuncDesc() {
		return progFuncMap.entrySet().stream()
				.map(e->String.format("%s.%s", e.getKey(), e.getValue()))
				.collect(Collectors.joining(", "));
	}

	private static void runExplorer(String url) {
		// 打開edge(預設虛擬機沒chrome的情況 且edge為win10內建不怕找不到)
		try {
			Runtime.getRuntime().exec(String.format("cmd.exe /C start microsoft-edge:%s ", url));
		} catch (IOException e) {
			System.out.println("===== 打開edge瀏覽器時發生錯誤! =====");
			System.out.println("可能是您已將edge瀏覽器移除，請安裝edge瀏覽器後再重試\n或者通知程式開發者以獲取進一步的解決辦法");
		}
	}

	private static int createSqlDataFromCSV(MaskPosDAO mDAO) throws SQLException {
		// 獲取maskPos成員		
		mDAO.deleteAllData();
		List<MaskPos> mPosList = MaskPosCSVParser.getInstance().getmPosList();
		mDAO.addMaskPosList(mPosList);
		return mPosList.size();
	}

	private static MaskPos findSqlDataByUserInput(MaskPosDAO mDAO) throws SQLException {
		// 接收查詢ID的mPos回傳結果
		MaskPos mResult = null;
		int id = getUserInputInt("\n請輸入要查詢的ID:", "請輸入正確的ID格式(僅能輸入非負整數且不可為0)!");
		/* =====舊方法===== */
//		// 預設回傳第一筆資料
//		int id = 0;
//		boolean checkOK = false;
//		
//		do {
//			System.out.println("\n請輸入要查詢的ID:");
//			// id若=0則不給查詢 因為資料庫的ID從1開始
//			if (sc.hasNextInt()) {
//				id = sc.nextInt();
//				checkOK = true;
//			}else {
//				// 讓游標切換到下一行 否則會讀不到input
//				// 跑完這裡後會直接回到迴圈開頭
//				sc.nextLine();
//				System.out.println("請輸入正確的ID格式(僅能輸入非負整數且不可為0)!");					
//			}
//		}while (!checkOK);
		/* =====舊方法===== */

		// 印出查詢到的資料之所有欄位資訊
		mResult = mDAO.findById(id);
		System.out.println("\n查詢完畢，結果為:");
		ShowMaskInfo.show(mResult);

		return mResult;
	}

	private static void showMapInfo(MaskPos mResult) {
		System.out.println("\n正在打開google map以顯示所查詢醫院之位置資訊...");
		if (mResult != null) {
			// 將中文的地址及名稱使用UTF8編碼為URL格式 再轉回字串
			String queryMapURL = getEncodeURLString(mResult.getHospitalName());
			// 呼叫edge瀏覽器將查詢結果用網頁顯示
			// 使用StringBuilder串接診所名稱 至google map顯示
			String gMapURL = gMapURLPrefix.append(queryMapURL).toString();
			runExplorer(gMapURL);
		} else {
			System.out.println("由於該筆資料查無結果，故無法顯示地圖位置資訊!");
		}
	}

	private static String getEncodeURLString(String unencodeURLstr) {
		String encodeURLStr = "";
		// 指定URLEncoder的編碼形式
		String stdCharSet = StandardCharsets.UTF_8.toString();
		try {
			System.out.println("將資料庫讀出之字串重新編碼為URL格式...");
			System.out.println("設定編碼格式為: " + stdCharSet);
			encodeURLStr = URLEncoder.encode(unencodeURLstr, stdCharSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("於轉換URL網址時發生錯誤 無法打開網址!");
		} finally {
			System.out.printf("轉換前網址: '%s/%s'%n", gMapURLPrefix, unencodeURLstr);
			System.out.printf("轉換後網址: '%s/%s'%n", gMapURLPrefix, encodeURLStr);
		}
		return encodeURLStr;
	}
}
