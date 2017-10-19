package easy.commons.io;

public class EasyConsole {
	/**
	 * System print
	 * @param object
	 */
	public static void display(Object object) {
		System.out.print(object);
	}
	
	/**
	 * System print
	 * @param object
	 */
	public static void display(Object object,boolean newLine) {
		if(newLine) {
			System.out.println(object);
		}else {
			System.out.print(object);
		}
	}
}
