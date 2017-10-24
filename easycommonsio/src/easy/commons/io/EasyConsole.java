package easy.commons.io;

public class EasyConsole {
	/**
	 * System print
	 * @param object
	 */
	public static void display(Object object) {
		System.out.println(object);
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
	
	/**
	 * Write exception to log file or screen
	 * @param e
	 */
	public static void display(Exception e) {
		e.printStackTrace();
	}
	
	/**
	 * Write exception to log file or screen
	 * @param e
	 */
	public static void display(String message,Exception e) {
		display(message);
		e.printStackTrace();
	}
}
