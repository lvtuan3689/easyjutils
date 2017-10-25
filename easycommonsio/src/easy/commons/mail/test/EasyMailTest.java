package easy.commons.mail.test;

import java.util.ArrayList;

import easy.commons.io.EasyConsole;
import easy.commons.mail.EasyMail;

public class EasyMailTest {

	public static void main(String[] args) {
		if (EasyMail.sendMail("This is testing email", "This is testing email of TuanLe", "tuanlv@etcom.com.vn",
				"mail.etcom.com.vn", 587, "tuanlv@etcom.com.vn", "Lkj8A321", true, true, new ArrayList<String>() {
					{
						add("lvtuan3689@gmail.com");
					}
				}, new ArrayList<String>() {
					{
						add("tuanle2221@gmail.com");
					}
				}, null, new ArrayList<String>() {
					{
						add("E:\\Projects\\Big5\\AutoDeploymentManager\\toolkit\\jazn-data.xml");
					}
				}))
			EasyConsole.display("Success");
		else
			EasyConsole.display("Failed");
	}
}
