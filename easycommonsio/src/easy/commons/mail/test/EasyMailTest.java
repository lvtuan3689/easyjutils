package easy.commons.mail.test;

import java.util.ArrayList;

import easy.commons.mail.EasyMail;

public class EasyMailTest {

	public static void main(String[] args) {
		EasyMail.sendMail("This is testing email", "This is testing email", "tuanlv@etcom.com.vn", "mail.etcom.com.vn",
				"465", "tuanlv@etcom.com.vn", "Lkj8A321", true, true, new ArrayList<String>() {
					{
						add("lvtuan3689@gmail.com");
					}
				}, null, null, null);
	}

}
