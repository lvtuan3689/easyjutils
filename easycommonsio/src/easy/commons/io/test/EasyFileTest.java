package easy.commons.io.test;

import easy.commons.io.EasyFile;

public class EasyFileTest {

	public static void main(String[] args) {
		String file = "F:\\Oracle";
		String zipFile = "F:\\Oracle.zip";
		EasyFile.zipDir(file, zipFile);
	}
}
