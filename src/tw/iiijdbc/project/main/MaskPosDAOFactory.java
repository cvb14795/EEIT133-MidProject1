package tw.iiijdbc.project.main;

public class MaskPosDAOFactory {

	private static final MaskPosDAO MASK_POS_DAO = createMaskPosDAO();
	
	private static MaskPosDAO createMaskPosDAO() {
		MaskPosDAO mDAO = new MaskPosDAO();
		return mDAO;
		
	}
	
	//設為public 以供Action使用
	//若為static則只有該Factory類別以及其子package能用 會呼叫不到
	public static MaskPosDAO getMaskPosDAO() {
		return MASK_POS_DAO;
	}

}
