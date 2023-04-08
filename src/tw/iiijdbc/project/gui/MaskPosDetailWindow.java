package tw.iiijdbc.project.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import tw.iiijdbc.project.main.MaskPos;
import tw.iiijdbc.project.main.ShowMaskInfo;

public class MaskPosDetailWindow {

	// 來自父視窗參數
	private MaskPosMainWindow parentWin;
	private MaskPos mPos;
	private String mPosID;
	private boolean calledFromParentWin = false;
	// 原生屬性(field)
	private JFrame frame;
	private JTextField address_text;
	private JLabel address_lbl;
	private JLabel tel_lbl;
	private JTextField name_text;
	private JTextField tel_text;
	private JTextField id_text;
	private JLabel id_lbl;
	private JLabel name_lbl;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel adult_lbl;
	private JLabel child_lbl;
	private JTextField child_text;
	private JLabel recordDate_lbl;
	private JTextField recordDate_text;
	private JTextField adult_text;
	private JButton close_btn;
	private JLabel warning_lbl;
	private JButton show_btn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MaskPosDetailWindow window = new MaskPosDetailWindow();
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
	public MaskPosDetailWindow() {
		initialize();
	}

	// 建立父視窗子視窗關係之constructor
	public MaskPosDetailWindow(MaskPosMainWindow parentWin) {
		System.out.println("接收到從父視窗的呼叫");
		calledFromParentWin = true;
		this.parentWin = parentWin;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//若是從父視窗點擊"顯示詳細資訊"
		if (calledFromParentWin) {
			// 模擬main方法的開啟視窗(設為可見)
			frame.setVisible(true);
		}
		frame.setBounds(100, 100, 338, 333);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 100, 100, 100, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 21, 92, 30, 30, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"\u8A73\u7D30\u8CC7\u8A0A", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 3;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		frame.getContentPane().add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 69, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 21, 0, 0, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		name_lbl = new JLabel("名稱");
		GridBagConstraints gbc_name_lbl = new GridBagConstraints();
		gbc_name_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_name_lbl.gridx = 0;
		gbc_name_lbl.gridy = 0;
		panel_1.add(name_lbl, gbc_name_lbl);
		name_lbl.setFont(new Font("新細明體", Font.PLAIN, 15));

		name_text = new JTextField();
		GridBagConstraints gbc_name_text = new GridBagConstraints();
		gbc_name_text.fill = GridBagConstraints.HORIZONTAL;
		gbc_name_text.insets = new Insets(0, 0, 5, 0);
		gbc_name_text.gridx = 1;
		gbc_name_text.gridy = 0;
		panel_1.add(name_text, gbc_name_text);
		name_text.setColumns(10);

		address_lbl = new JLabel("地址");
		GridBagConstraints gbc_address_lbl = new GridBagConstraints();
		gbc_address_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_address_lbl.gridx = 0;
		gbc_address_lbl.gridy = 1;
		panel_1.add(address_lbl, gbc_address_lbl);
		address_lbl.setFont(new Font("新細明體", Font.PLAIN, 15));

		address_text = new JTextField();
		GridBagConstraints gbc_address_text = new GridBagConstraints();
		gbc_address_text.fill = GridBagConstraints.HORIZONTAL;
		gbc_address_text.insets = new Insets(0, 0, 5, 0);
		gbc_address_text.gridx = 1;
		gbc_address_text.gridy = 1;
		panel_1.add(address_text, gbc_address_text);
		address_text.setColumns(10);

		tel_lbl = new JLabel("電話");
		GridBagConstraints gbc_tel_lbl = new GridBagConstraints();
		gbc_tel_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_tel_lbl.gridx = 0;
		gbc_tel_lbl.gridy = 2;
		panel_1.add(tel_lbl, gbc_tel_lbl);
		tel_lbl.setFont(new Font("新細明體", Font.PLAIN, 15));

		tel_text = new JTextField();
		GridBagConstraints gbc_tel_text = new GridBagConstraints();
		gbc_tel_text.fill = GridBagConstraints.HORIZONTAL;
		gbc_tel_text.insets = new Insets(0, 0, 5, 0);
		gbc_tel_text.gridx = 1;
		gbc_tel_text.gridy = 2;
		panel_1.add(tel_text, gbc_tel_text);
		tel_text.setColumns(10);

		id_lbl = new JLabel("ID");
		GridBagConstraints gbc_id_lbl = new GridBagConstraints();
		gbc_id_lbl.insets = new Insets(0, 0, 0, 5);
		gbc_id_lbl.gridx = 0;
		gbc_id_lbl.gridy = 3;
		panel_1.add(id_lbl, gbc_id_lbl);
		id_lbl.setFont(new Font("新細明體", Font.PLAIN, 15));

		id_text = new JTextField();
		GridBagConstraints gbc_id_text = new GridBagConstraints();
		gbc_id_text.fill = GridBagConstraints.HORIZONTAL;
		gbc_id_text.gridx = 1;
		gbc_id_text.gridy = 3;
		panel_1.add(id_text, gbc_id_text);
		id_text.setColumns(10);

		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"\u53E3\u7F69\u5269\u9918\u6578\u91CF", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 3;
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 1;
		frame.getContentPane().add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 15, 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		adult_lbl = new JLabel("成人");
		adult_lbl.setFont(new Font("新細明體", Font.PLAIN, 15));
		GridBagConstraints gbc_adult_lbl = new GridBagConstraints();
		gbc_adult_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_adult_lbl.gridx = 0;
		gbc_adult_lbl.gridy = 0;
		panel_2.add(adult_lbl, gbc_adult_lbl);

		adult_text = new JTextField();
		adult_text.setColumns(10);
		GridBagConstraints gbc_adult_text = new GridBagConstraints();
		gbc_adult_text.insets = new Insets(0, 0, 5, 0);
		gbc_adult_text.fill = GridBagConstraints.HORIZONTAL;
		gbc_adult_text.gridx = 1;
		gbc_adult_text.gridy = 0;
		panel_2.add(adult_text, gbc_adult_text);

		child_lbl = new JLabel("孩童");
		child_lbl.setFont(new Font("新細明體", Font.PLAIN, 15));
		GridBagConstraints gbc_child_lbl = new GridBagConstraints();
		gbc_child_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_child_lbl.gridx = 0;
		gbc_child_lbl.gridy = 1;
		panel_2.add(child_lbl, gbc_child_lbl);

		child_text = new JTextField();
		child_text.setColumns(10);
		GridBagConstraints gbc_child_text = new GridBagConstraints();
		gbc_child_text.fill = GridBagConstraints.HORIZONTAL;
		gbc_child_text.insets = new Insets(0, 0, 5, 0);
		gbc_child_text.gridx = 1;
		gbc_child_text.gridy = 1;
		panel_2.add(child_text, gbc_child_text);

		recordDate_lbl = new JLabel("登錄日期");
		recordDate_lbl.setFont(new Font("新細明體", Font.PLAIN, 15));
		GridBagConstraints gbc_recordDate_lbl = new GridBagConstraints();
		gbc_recordDate_lbl.insets = new Insets(0, 0, 0, 5);
		gbc_recordDate_lbl.gridx = 0;
		gbc_recordDate_lbl.gridy = 2;
		panel_2.add(recordDate_lbl, gbc_recordDate_lbl);

		recordDate_text = new JTextField();
		recordDate_text.setColumns(10);
		GridBagConstraints gbc_recordDate_text = new GridBagConstraints();
		gbc_recordDate_text.fill = GridBagConstraints.HORIZONTAL;
		gbc_recordDate_text.gridx = 1;
		gbc_recordDate_text.gridy = 2;
		panel_2.add(recordDate_text, gbc_recordDate_text);

		warning_lbl = new JLabel("");
		warning_lbl.setFont(new Font("新細明體", Font.PLAIN, 15));
		warning_lbl.setForeground(Color.RED);
		GridBagConstraints gbc_warning_lbl = new GridBagConstraints();
		gbc_warning_lbl.gridwidth = 3;
		gbc_warning_lbl.insets = new Insets(0, 0, 5, 5);
		gbc_warning_lbl.gridx = 1;
		gbc_warning_lbl.gridy = 2;
		frame.getContentPane().add(warning_lbl, gbc_warning_lbl);
				
				show_btn = new JButton("顯示詳情");
				show_btn.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// 父視窗的mPos查詢結果
						mPos = parentWin.getmPos();
						// 父視窗用戶輸入的ID
						mPosID = parentWin.getIdText();
						// 有查到東西
						if (mPos != null) {
							// 若兩者不一樣 代表使用者更改了ID輸入框的值 但尚未送出查詢
							if (Integer.parseInt(mPosID) == mPos.getId()) {
								// 顯示於console
								System.out.println("-----以下為來自子視窗的訊息-----");
								ShowMaskInfo.show(mPos);
								System.out.println("-----以上為來自子視窗的訊息-----");
								// 設置視窗文字框
								int adultMask = mPos.getMaskAdult();
								int childMask = mPos.getMaskChildren();
								name_text.setText(mPos.getHospitalName());
								address_text.setText(mPos.getHospitalAddress());
								id_text.setText(mPosID);
								adult_text.setText(Integer.toString(adultMask));
								child_text.setText(Integer.toString(childMask));
								tel_text.setText(mPos.getHospitalPhone());
								recordDate_text.setText(mPos.getDataTime());
								
								if (adultMask == 0 | childMask == 0) {
									warning_lbl.setText("口罩已經賣完!");
								}else {
									warning_lbl.setText("");
								}

							} else { // 若為上述情況(未點查詢就點顯示詳細資料)則不顯示
								// 使用HTML語法的<br/>標籤來換行
								warning_lbl.setText("資料尚未更新!請先按'查詢'再點擊'顯示詳細資訊'!");
							}
							
						}else { // 沒查到東西(mPos為空)
							warning_lbl.setText(String.format("該筆ID:'%s'查無資料!", mPosID));
						}
					}
				});
				GridBagConstraints gbc_show_btn = new GridBagConstraints();
				gbc_show_btn.fill = GridBagConstraints.HORIZONTAL;
				gbc_show_btn.insets = new Insets(0, 0, 0, 5);
				gbc_show_btn.gridx = 1;
				gbc_show_btn.gridy = 3;
				frame.getContentPane().add(show_btn, gbc_show_btn);
		
				close_btn = new JButton("關閉");
				GridBagConstraints gbc_close_btn = new GridBagConstraints();
				gbc_close_btn.insets = new Insets(0, 0, 0, 5);
				gbc_close_btn.fill = GridBagConstraints.HORIZONTAL;
				gbc_close_btn.gridx = 3;
				gbc_close_btn.gridy = 3;
				frame.getContentPane().add(close_btn, gbc_close_btn);
	}

}
