package easy.commons.io.test;

import easy.commons.io.EasyConsole;
import easy.commons.io.EasyString;

public class EasyStringTest {

	public static void main(String[] args) {
		String initVector = "aLnhG76Y$&OueacF";
		String key = "okjp)ihnaVbvauq7";
		String name = "Lê Văn Tuấn";
		String encrypted = EasyString.getEncrypted(initVector, key, name);
		EasyConsole.display("Encrypted: " + encrypted);
		String decrypted = EasyString.getDecrypted(initVector, key, encrypted);
		EasyConsole.display("Decrypted: " + decrypted);
	}
}