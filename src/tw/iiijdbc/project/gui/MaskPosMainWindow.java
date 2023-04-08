package tw.iiijdbc.project.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import tw.iiijdbc.project.main.MaskPos;
import tw.iiijdbc.project.main.MaskPosDAO;
import tw.iiijdbc.project.main.MaskPosDAOFactory;
import tw.iiijdbc.project.main.ShowMaskInfo;

public class MaskPosMainWindow {

	// 接收findById回傳
	private MaskPos mPos = null;
	// 父視窗(獲取此視窗直接用this 不可new 否則會生出另一個同樣視窗)
 	private MaskPosMainWindow thisWin = this;
 	// 子視窗
 	private MaskPosDetailWindow childWin = null;
	//原生屬性(field)
	private JFrame frame;
	private JPanel panel;
	private JTextField id_text;
	private JTextField name_text;
	private JButton query_btn;
	private JButton detail_btn;
	
	public MaskPos getmPos() {
		return mPos;
	}
	
	public String getIdText() {
		return id_text.getText();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MaskPosMainWindow window = new MaskPosMainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MaskPosMainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 250);
		// 設定視窗標題列的關閉按鈕結束程式執行
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		
		panel = new JPanel();
		frame.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JLabel id_lbl = new JLabel("ID");
		sl_panel.putConstraint(SpringLayout.NORTH, id_lbl, 23, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, id_lbl, 32, SpringLayout.WEST, panel);
		id_lbl.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		panel.add(id_lbl);
		
		id_text = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, id_text, 0, SpringLayout.NORTH, id_lbl);
		sl_panel.putConstraint(SpringLayout.WEST, id_text, 54, SpringLayout.EAST, id_lbl);
		sl_panel.putConstraint(SpringLayout.SOUTH, id_text, 0, SpringLayout.SOUTH, id_lbl);
		sl_panel.putConstraint(SpringLayout.EAST, id_text, -26, SpringLayout.EAST, panel);
		id_text.setColumns(10);
		panel.add(id_text);
		
		JLabel name_lbl = new JLabel("名稱");
		sl_panel.putConstraint(SpringLayout.NORTH, name_lbl, 17, SpringLayout.SOUTH, id_lbl);
		sl_panel.putConstraint(SpringLayout.WEST, name_lbl, 0, SpringLayout.WEST, id_lbl);
		name_lbl.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		panel.add(name_lbl);
		
		name_text = new JTextField();
		sl_panel.putConstraint(SpringLayout.EAST, name_text, 0, SpringLayout.EAST, id_text);
		sl_panel.putConstraint(SpringLayout.NORTH, name_text, 0, SpringLayout.NORTH, name_lbl);
		sl_panel.putConstraint(SpringLayout.WEST, name_text, 35, SpringLayout.EAST, name_lbl);
		sl_panel.putConstraint(SpringLayout.SOUTH, name_text, 0, SpringLayout.SOUTH, name_lbl);
		name_text.setColumns(10);
		panel.add(name_text);
		
		JLabel warning_lbl = new JLabel("");
		warning_lbl.setForeground(Color.RED);
		sl_panel.putConstraint(SpringLayout.WEST, warning_lbl, 0, SpringLayout.WEST, id_lbl);
		sl_panel.putConstraint(SpringLayout.EAST, warning_lbl, 0, SpringLayout.EAST, id_text);
		warning_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		warning_lbl.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		sl_panel.putConstraint(SpringLayout.NORTH, warning_lbl, 6, SpringLayout.SOUTH, name_text);
		panel.add(warning_lbl);
		
		query_btn = new JButton("查詢");
		sl_panel.putConstraint(SpringLayout.SOUTH, warning_lbl, -6, SpringLayout.NORTH, query_btn);
		sl_panel.putConstraint(SpringLayout.NORTH, query_btn, -55, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, query_btn, 61, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, query_btn, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, query_btn, -248, SpringLayout.EAST, panel);
		query_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					int userInput = Integer.parseInt(id_text.getText());  
					System.out.println("使用者輸入ID: " + userInput);
					MaskPosDAO mDAO = MaskPosDAOFactory.getMaskPosDAO();
					mDAO.createConn();
					mPos = mDAO.findById(userInput);
					//
					mDAO.createConn();
					System.out.println("傳回名稱: "+mPos.getHospitalName());
					
					System.out.println("\n使用者點擊的詳細資訊: ");
					ShowMaskInfo.show(mPos);
					name_text.setText(mPos.getHospitalName());
					
					
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					warning_lbl.setText("請輸入正確的ID值(必須為非負整數)!");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					// 使用HTML語法的<br/>標籤來換行
					warning_lbl.setText("<html>ID查詢失敗!<br/> 請點擊「顯示詳細資訊」以查看查詢結果!</html>");
				} catch (NullPointerException e1) {
					// TODO: handle exception
					warning_lbl.setText("輸入之ID為空 或該ID於資料庫無對應之資料!");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println("===== 驅動程式載入錯誤！請檢查主程式內是否有包含對應資料庫之驅動程式！ =====");
					warning_lbl.setText("載入資料庫驅動程式時發生錯誤 請檢查SQL服務是否為已開啟狀態!");
				}
			}
		});
		panel.add(query_btn);
		
		detail_btn = new JButton("顯示詳細資訊");
		sl_panel.putConstraint(SpringLayout.NORTH, detail_btn, 0, SpringLayout.NORTH, query_btn);
		sl_panel.putConstraint(SpringLayout.WEST, detail_btn, -172, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, detail_btn, -53, SpringLayout.EAST, panel);
		detail_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (childWin == null) {
					// 父視窗作為參數傳入 並且打開子視窗
				 	childWin = new MaskPosDetailWindow(thisWin);
				}
			}
		});
		sl_panel.putConstraint(SpringLayout.SOUTH, detail_btn, 0, SpringLayout.SOUTH, query_btn);
		panel.add(detail_btn);
		
		
	}
}
