package easy.commons.io.test;

import easy.commons.io.EasyBarcode;

public class EasyBarcodeTest {

	public static void main(String[] args) {
		EasyBarcode.getInstance().buildFromXML(
				"E:\\Projects\\OpenSource\\easyjutils\\easycommonsio\\resources\\barcode\\Codabar.xml", "Tuan Le",
				EasyBarcode.OutputType.SVG, "F:\\barcode.svg", null);
	}
}
