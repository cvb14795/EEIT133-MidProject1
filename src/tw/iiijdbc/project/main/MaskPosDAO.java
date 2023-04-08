package tw.iiijdbc.project.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MaskPosDAO {
//		  ID INT PRIMARY　KEY,
//		  hospitalName VARCHAR(50) NOT NULL,
//		  hospitalAddress VARCHAR(255) NOT NULL, 
//		  hospitalPhone VARCHAR(20), 
//		  maskAdult INT NOT NULL, 
//		  maskChildren INT NOT NULL,
//		  dataTime datetime NOT NULL
	private Connection conn;
	// scanner不需要close 也不需要try catch 否則會無窮迴圈
	private Scanner sc = new Scanner(System.in);

	// 創連線
	public void createConn() throws ClassNotFoundException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		// dbName: MidtermProjectDB
		String urlStr = "jdbc:sqlserver://localhost:1433;databaseName=MidtermProject2DB;user=sa;password=sa123456";
		this.conn = DriverManager.getConnection(urlStr);

//		boolean status = !conn.isClosed();
//
//		if (status) {
//			System.out.println("已開啟連線!!\n");
//		}
	}

	// 關連線
	public void closeConn() throws SQLException {
		if (conn != null) {
			conn.close();
//			System.out.println("\n已關閉連線!!");
		}
	}

	// 改成用batch
	// 新增多筆資料(批次 例如讀CSV)
	public boolean addMaskPosList(List<MaskPos> mPosList) {

		String sqlStr = "INSERT INTO mask(ID, hospitalName, hospitalAddress, hospitalPhone "
				+ ", maskAdult, maskChildren, dataTime) " + "values(?,?,?,?,?,?,?)";
		PreparedStatement preState;
		try {
			preState = conn.prepareStatement(sqlStr);
			for (MaskPos m : mPosList) {
				preState.setInt(1, m.getId());
				preState.setString(2, m.getHospitalName());
				preState.setString(3, m.getHospitalAddress());
				preState.setString(4, m.getHospitalPhone());
				preState.setInt(5, m.getMaskAdult());
				preState.setInt(6, m.getMaskChildren());
				preState.setString(7, m.getDataTime());

				preState.addBatch();
			}
			preState.executeBatch();
			preState.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("發生錯誤: "+e.getMessage());
			System.out.println("將插入SQL的資料有重複 或找不到名為'mask'的Table");
			return false;
		}

		// debug用
//		System.out.println("已成功新增資料: ID"+ m.getId() +" 至資料庫!");
	}

	// 新增單筆資料
	public void addMaskPos(MaskPos m) throws SQLException {
		String sqlStr = "INSERT INTO mask(ID, hospitalName, hospitalAddress, hospitalPhone "
				+ ", maskAdult, maskChildren, dataTime) " + "values(?,?,?,?,?,?,?)";
		PreparedStatement preState;
		preState = conn.prepareStatement(sqlStr);
		preState.setInt(1, m.getId());
		preState.setString(2, m.getHospitalName());
		preState.setString(3, m.getHospitalAddress());
		preState.setString(4, m.getHospitalPhone());
		preState.setInt(5, m.getMaskAdult());
		preState.setInt(6, m.getMaskChildren());
		preState.setString(7, m.getDataTime());
		preState.execute();
		preState.close();
	}

	// 查詢資料
	public MaskPos findById(int id) throws SQLException {

		// 條件: 找指定ID
		/*
		 * MidtermProjectDB裡mask欄位名稱的ID是大寫!!!!!!!!!!!!!!!!!
		 */
		String sqlStr = "SELECT * FROM mask WHERE ID = ?";
		PreparedStatement preState = conn.prepareStatement(sqlStr);
		preState.setInt(1, id);

		// 設一個空MaskPos 以接收其查詢結果(rs)
		MaskPos m = null;
		// 注意不是execute
		ResultSet rs = preState.executeQuery();
		System.out.println("正在查詢 ID:" + id + " 的資料...");

		// 是if 不是while 因為在ID不重複的情況下其資料只有一筆
		if (rs.next()) {
			// rs.get使用欄位名稱做查詢 不是index
			m = new MaskPos();
			m.setId(rs.getInt("ID"));
			m.setHospitalName(rs.getString("hospitalName"));
			m.setHospitalAddress(rs.getString("hospitalAddress"));
			m.setHospitalPhone(rs.getString("hospitalPhone"));
			m.setMaskAdult(rs.getInt("maskAdult"));
			m.setMaskChildren(rs.getInt("maskChildren"));
			m.setDataTime(rs.getString("dataTime"));

		} else {
			System.out.println("findById查無資料 資料庫無結果!");
		}
		rs.close();
		preState.close();

		return m;

	}

	// 刪除所有資料
	public boolean deleteById(int id) throws SQLException {
		// 刪除作業是否成功
		boolean status = false;
		String sqlStr = "DELETE FROM mask WHERE ID = ?";

		PreparedStatement preState = conn.prepareStatement(sqlStr);
		preState.setInt(1, id);
		status = preState.execute();
		System.out.printf("刪除mask ID: %d的資料... %s", id, status?"成功":"失敗");
		preState.close();
		return status;

	}

	/*
	 * DEBUG用 注意以下程式用途
	 */
	// 刪除所有資料
	public boolean deleteAllData() {
		// 刪除作業是否成功
		boolean status = false;
		String sqlStr = "DELETE FROM mask";
		PreparedStatement preState = null;
		try {
			preState = conn.prepareStatement(sqlStr);
			System.out.println("\n需要清除上次測試時資料庫的資料嗎?");
			System.out.println("輸入1:是, 其他任意鍵:否");
			if (sc.hasNextInt() && sc.nextInt() == 1) {
				status = true;
				preState.execute();
				System.out.println("即將刪除mask資料庫下所有資料...");
			} else {
				// 讓游標切換到下一行 否則會讀不到input
				sc.nextLine();
				System.out.println("以上所有變更將會保存至mask資料庫內。\n注意: 若再次執行本程式前未將資料清空，有可能發生重複插入相同資料的錯誤!");
			}
			preState.close();
		} catch (Exception e) {
			System.out.println("發生錯誤: "+e.getMessage());
			status = false;
		} finally {
			if (preState != null) {
				try {
					preState.close();
				} catch (SQLException e) {
					status = false;
				}				
			}
		}

		return status;

	}
	
	public boolean createTableIfNotExist() {
		String sqlStr = "IF NOT EXISTS ( SELECT * FROM sysobjects WHERE  name = 'mask'  and xtype= 'U' )"
				+ "CREATE TABLE mask ("
				+ "ID int PRIMARY KEY,"
				+ "hospitalName nvarchar(255) NOT NULL,"
				+ "hospitalAddress nvarchar(255) NOT NULL, "
				+ "hospitalPhone varchar(50) NOT NULL,"
				+ "maskAdult int NOT NULL, "
				+ "maskChildren int NOT NULL, "
				+ "dataTime nvarchar(20) NOT NULL"
				+ ")";
		boolean status = false;
		PreparedStatement preState = null; 
		try {
			preState = conn.prepareStatement(sqlStr);
			preState.execute();
			status = true;
		} catch (Exception e) {
			System.out.println("發生錯誤: "+e.getMessage());
			status = false;
		} finally {
			if (preState != null) {
				try {
					preState.close();
				} catch (SQLException e) {
					status = false;
				}				
			}
		}
		return status;
	}
}
