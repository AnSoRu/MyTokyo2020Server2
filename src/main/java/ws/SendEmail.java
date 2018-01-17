package ws;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendEmail {
	final String miCorreo = "dam2017g3@gmail.com";
	final String miContrasenia = "damg3damg3";
	final String servidorSMTP = "smtp.gmail.com";
	final String puertoEnvio = "465";
	String mailReceptor = null;
	String asunto = null;
	String cuerpo = null;
	
	
	
	public void SendMail(String mailReceptor, String path, String asunto, String cuerpo) {
        this.mailReceptor = mailReceptor;
        this.asunto = asunto;
        this.cuerpo = cuerpo;

        Properties props = new Properties();
        props.put("mail.smtp.user", miCorreo);
        props.put("mail.smtp.host", servidorSMTP);
        props.put("mail.smtp.port", puertoEnvio);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", puertoEnvio);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        @SuppressWarnings("unused")
		SecurityManager security = System.getSecurityManager();

        try {
            Authenticator auth = new autentificadorSMTP();
            Session session = Session.getInstance(props, auth);
            // session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            msg.setSubject(asunto);
            msg.setFrom(new InternetAddress(miCorreo));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    mailReceptor));
            
            MimeBodyPart part1 = new MimeBodyPart();
            part1.setText(cuerpo);
            
            MimeBodyPart part2 = new MimeBodyPart();
            
            //part2.attachFile(new File("D:\\apache-tomcat-8.0.38\\webapps\\MyTokyo2020Server2\\WEB-INF\\resources\\images.pdf"));
            part2.attachFile(new File(path,"images.pdf"));
            
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(part1);
            mp.addBodyPart(part2);
            
            msg.setContent(mp);
            msg.setSentDate(new Date());
            
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }

    }

    private class autentificadorSMTP extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(miCorreo, miContrasenia);
        }
    }
	
	
}
