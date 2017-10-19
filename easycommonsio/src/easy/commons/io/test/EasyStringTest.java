package easy.commons.io.test;

import easy.commons.io.EasyConsole;
import easy.commons.io.EasyString;

public class EasyStringTest {

	public static void main(String[] args) {
		String name = "Tuan Le";
		EasyConsole.display(EasyString.subStr(name,-5));
	}

}