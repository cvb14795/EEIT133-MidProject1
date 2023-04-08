# EEIT133-MidProject1

(備份) 資策會Java班EEIT133期中專題1－資料庫CRUD

# 主題
口罩地圖展示及剩餘數量查詢  

串接政府資料開放平台(data.gov.tw)之[健保特約機構口罩剩餘數量明細清單](https://data.gov.tw/en/datasets/116285)

亦可讀取自定義資料之CSV檔 

**(但資料欄位格式需與上述之開放平台資料格式相符 目前尚不支援自定義欄位格式)**

# 說明
`config.properties`為讀取本機CSV資料之設定檔 
範例:
* `using-local-csv-file=false`
* `local-csv-file-path=maskData_NoBOM.csv`

其中`local-csv-file-path`為該專案資料夾的相對路徑
若using-local-csv-file設定為false則不讀取本機檔案，僅使用政府開放資料平台之資料

# Usage

**因本專案為資料庫CRUD練習 故需使用到MS SQL Server 預設連localhost:1433 其他連線字串參數因僅為個人demo用途 故沒有開放於設定檔內修改 需自行於程式內更改**
** 資料表名稱(Table Name)固定為'mask' 於首次執行程式時若無該資料表會自動建立**

## 主程式
點擊`期中專題1_口罩地圖展示及剩餘數量查詢(Console).bat` 

或直接於CMD執行`java -jar MidTopicProject.jar`

![](https://i.imgur.com/0snjA3D.gif)


## UI視窗介面查詢

點擊`期中專題1_口罩剩餘數量查詢(UI介面).bat`

或直接於CMD執行`java -jar MidTopicProject_SwingGUI.jar`

![](https://i.imgur.com/oJ6xMTh.gif)

# 相關技術
JDBC for MS SQL Server 

Java Swing GUI

JDK 8

相關套件依賴請參考`lib`資料夾內各jar檔
