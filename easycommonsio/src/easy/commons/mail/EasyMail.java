package easy.commons.mail;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import easy.commons.io.EasyConsole;
import easy.commons.io.EasyFile;
import easy.commons.io.EasyString;

public class EasyMail {
	public static long MAX_TOTAL_FILE_SIZE = 26214400L;// 25 MB
	public static int MAX_FILE_ATTACHES = 10;
	public static boolean IGNORE_FILE_NOT_FOUND_ERROR = false;

	/**
	 * Send an email
	 * 
	 * @param subject
	 * @param body
	 * @param from
	 * @param host
	 * @param username
	 * @param password
	 * @param tos
	 * @param ccs
	 * @param bccs
	 * @param attaches
	 */
	public static boolean sendMail(String subject, String body, String from, String host, int port, String username,
			String password, boolean auth, boolean tlsEnable, List<String> tos, List<String> ccs, List<String> bccs,
			List<String> attaches) {
		String vfrom = EasyString.str(from, "");
		String vsubject = EasyString.str(subject, "");
		String vbody = EasyString.str(body, "");
		String vhost = EasyString.str(host, "");
		String vusername = EasyString.str(username, "");
		String vpassword = EasyString.str(password, "");
		String vport = EasyString.str(port, "");
		String vauth = EasyString.str(auth, "false");
		String vtlsEnable = EasyString.str(tlsEnable, "false");
		boolean send = true;
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", vhost);
		properties.setProperty("mail.smtp.user", vusername);
		properties.setProperty("mail.smtp.password", vpassword);
		properties.setProperty("mail.smtp.auth", vauth);
		properties.setProperty("mail.smtp.starttls.enable", String.valueOf(vtlsEnable));
		properties.setProperty("mail.smtp.ssl.trust", vhost);
		properties.setProperty("mail.smtp.port", vport);
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EasyString.str(vusername, ""), EasyString.str(vpassword, ""));
			}
		});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(vfrom));

			// Set To: header field of the header.
			if (tos != null)
				for (String to : tos) {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				}
			if (ccs != null)
				for (String cc : ccs) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
				}
			if (bccs != null)
				for (String bcc : bccs) {
					message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
				}

			// Set Subject: header field
			message.setSubject(vsubject);

			// Now set the actual message
			Multipart multipart = new MimeMultipart();

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(vbody, "text/html");
			multipart.addBodyPart(messageBodyPart);
			if (attaches != null) {
				if (attaches.size() > MAX_FILE_ATTACHES) {
					EasyConsole.display("Max send files is " + EasyString.str(MAX_FILE_ATTACHES, "0"));
					return false;
				}
				long totalSize = EasyFile.getFilesSize(attaches.toArray(new String[attaches.size()]));
				if (totalSize > MAX_TOTAL_FILE_SIZE) {
					EasyConsole.display("Max total file size is " + EasyString.str(MAX_TOTAL_FILE_SIZE, "0") + " ("
							+ (MAX_TOTAL_FILE_SIZE / 1048576) + " MB), your total file size: " + totalSize + "("
							+ (totalSize / 1048576) + " MB)");
					return false;
				}
				DataSource source = null;
				for (String attach : attaches) {
					File f = EasyFile.makeFile(attach);
					if (f == null) {
						EasyConsole.display("File not found: " + attach);
						if (!IGNORE_FILE_NOT_FOUND_ERROR) {
							send = false;
							continue;
						}
					}
					BodyPart attachFile = new MimeBodyPart();
					source = new FileDataSource(attach);
					attachFile.setDataHandler(new DataHandler(source));
					attachFile.setFileName(f.getName());
					multipart.addBodyPart(attachFile);
				}
			}
			message.setContent(multipart);
			if (send) {
				Transport.send(message);
				return true;
			}
			return false;
		} catch (Exception e) {
			EasyConsole.display(e);
		}
		return false;
	}
}
