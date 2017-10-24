package easy.commons.io.test;

import easy.commons.io.EasyFile;

public class EasyFileTest {

	public static void main(String[] args) {
		String file = "F:/test.dat";
		String content = "Testing to write file";
		EasyFile.writeToFile(file, content);
	}

}
