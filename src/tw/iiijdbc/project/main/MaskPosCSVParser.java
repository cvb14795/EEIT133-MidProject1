package tw.iiijdbc.project.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class MaskPosCSVParser {
	/**
	 * 健保特約機構口罩剩餘數量明細清單（來源：政府資料開放平台） 
	 */
	public static final String ONLINE_MASK_DATA_CSV_URL = "https://data.nhi.gov.tw/Datasets/Download.ashx?rid=A21030000I-D50001-001&l=https://data.nhi.gov.tw/resource/mask/maskdata.csv";
	
	private static final MaskPosCSVParser INSTANCE = new MaskPosCSVParser();
	private static final String PROPERTIES_FILENAME = "config.properties";
	
	private boolean usingLocalFile = false;
	private Path localCSVFilePath;
	private Properties properties;
	

	public static MaskPosCSVParser getInstance() {
		return INSTANCE;
	}
	
	public MaskPosCSVParser() {
		// config預設存放在當前專案目錄下
		this.properties = new Properties();
		Path propertiesFilePath = reslovePathFromWorkingDir(PROPERTIES_FILENAME);
		try {
			properties.load(Files.newInputStream(propertiesFilePath));
		} catch (IOException e) {
			System.err.printf("\n於路徑:'%s'下讀取config檔案時發生錯誤! %s\n", propertiesFilePath, e.getMessage());
			return;
		}
		
		try {
			this.usingLocalFile = Boolean.parseBoolean(this.properties.getProperty("using-local-csv-file"));
			this.localCSVFilePath = reslovePathFromWorkingDir(this.properties.getProperty("local-csv-file-path")).normalize();
			if (!Files.exists(this.localCSVFilePath)) {
				throw new Exception(String.format("於路徑:'%s'下不存在欲讀取之csv檔案!", this.localCSVFilePath));
			}
		} catch (Exception e) {
			System.out.printf("\nconfig檔設定讀取失敗! 請檢查參數設定:%s是否有誤!\n 詳細訊息: '%s'\n", this.properties.stringPropertyNames(), e);
			System.out.println("將不使用讀取本機CSV檔案功能...");
			this.usingLocalFile = false;
			return;
		}
	}
	
	public boolean isUsingLocalFile() {
		return usingLocalFile;
	}

	public void setUsingLocalFile(boolean usingLocalFile) {
		this.usingLocalFile = usingLocalFile;
	}

	private Path reslovePathFromWorkingDir(String subDirPath) {
		return Paths.get(System.getProperty("user.dir"), subDirPath);
	}
	
	/**
	 * 讀取政府開放平台「健保特約機構口罩剩餘數量明細清單」之CSV並解析<br>
	 * 依照設定檔 ({@value #PROPERTIES_FILENAME}) 參數決定抓政府開放平台資料<br>
	 * 或直接讀取本機檔案(路徑同樣於設定檔內指定)
	 * @return 
	 */
	public List<MaskPos> getmPosList() {
		URL url = null;
		try {
			if (usingLocalFile) {
				url = this.localCSVFilePath.toUri().toURL();
			} else {
				url = new URL(MaskPosCSVParser.ONLINE_MASK_DATA_CSV_URL);
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return Collections.emptyList();
		}
		System.out.printf("資料來源路徑(%s): '%s'", 
				usingLocalFile? "本機" : "政府開放平台",
				url);
		// 將有千分位數或其特定格式string轉其他type(例如int)
		NumberFormat numTmp = new DecimalFormat();
		// 將CSV中時間的字串格式轉為date 供JDBC送進SQL
		// CSV時間格式: 2021/06/30 14:32:25
		// HH=24小時制 hh:12小時制
		SimpleDateFormat simDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		// 資料筆數ID
		int maskPosID = 1;

		// 將每筆資料分開成list
		List<MaskPos> mPosList = new ArrayList<>();

		// 記得留意有沒有BOM 一般的編輯器(如EXCEL)及JAVA IO時不吃BOM
		try (InputStream is = url.openStream();
		//資料來源:健保特約機構口罩剩餘數量明細清單
		//資料網址:"https://data.gov.tw/dataset/116285"
				InputStreamReader in = new InputStreamReader(is, "UTF-8");
				CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader())) {
			
			System.out.println("前三筆資料預覽:");
			for (CSVRecord record : parser) {
				// 回傳"口罩"物件
				MaskPos mPos = new MaskPos();
				
				// 醫院資訊
				// 不採用"醫事機構代碼" 而使用目前資料筆數為ID
				mPos.setId(maskPosID);
				mPos.setHospitalName(record.get("醫事機構名稱"));
				mPos.setHospitalAddress(record.get("醫事機構地址"));
				mPos.setHospitalPhone(record.get("醫事機構電話"));

				// 口罩數量
				mPos.setMaskAdult(numTmp.parse(record.get("成人口罩剩餘數")).intValue());
				mPos.setMaskChildren(numTmp.parse(record.get("兒童口罩剩餘數")).intValue());
				// 最後更新日期
				Date dataDate = simDateFormat.parse(record.get("來源資料時間"));
				mPos.setDataTime(simDateFormat.format(dataDate));
				
				//顯示資訊(top 3)
				if (maskPosID <= 3) {
					ShowMaskInfo.show(mPos);					
				}
				
				mPosList.add(mPos);
				// debug用
//				if (mPosList.size() > 0) {					
//					System.out.println("已從CSV檔加入 ID=" + mPosList.get(maskPosID-1).getId() + " 之mPos成員!");
//				}
				//此行必須要在該block最後 否則get(maskPosID-1)會outOfBound
				maskPosID++;
			}
			System.out.println("\n總共： 加入" + mPosList.size() + "筆 mPos成員");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mPosList;

	}

	
	
}
