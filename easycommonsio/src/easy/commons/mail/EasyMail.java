package easy.commons.mail;

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
import easy.commons.io.EasyString;

public class EasyMail {

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
	public static void sendMail(String subject, String body, String from, String host, String port, String username,
			String password, boolean auth, boolean tlsEnable, List<String> tos, List<String> ccs, List<String> bccs,
			List<String> attaches) {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", EasyString.str(host, ""));
		properties.setProperty("mail.smtp.user", EasyString.str(username, ""));
		properties.setProperty("mail.smtp.password", EasyString.str(password, ""));
		properties.setProperty("mail.smtp.auth", String.valueOf(auth));
		properties.setProperty("mail.smtp.starttls.enable", String.valueOf(tlsEnable));
		properties.setProperty("mail.smtp.port", port);
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EasyString.str(username, ""), EasyString.str(password, ""));
			}
		});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

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
			message.setSubject(subject);

			// Now set the actual message

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			if (attaches != null) {
				for (String attach : attaches) {
					DataSource source = new FileDataSource(attach);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(attach);
					multipart.addBodyPart(messageBodyPart);
				}
			}
			message.setContent(multipart);
			// Send message
			Transport.send(message);
		} catch (Exception e) {
			EasyConsole.display(e);
		}
	}
}
