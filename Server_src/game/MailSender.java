package game;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

/**
 * la classe MailSender è usata per inviare email usando i server mail di google
 * @author Andrea Peluso
 * @version 1.0
 */
public class MailSender extends Thread{
	private String usr,pwd,to,subject,body;
	
	/**
	 * costruttore della classe
	 * @param usr indica l'email del mittente
	 * @param pwd indica la password del mittente
	 * @param to indica il destinatario
	 * @param subject indica il soggetto dell'email
	 * @param body indica il corpo dell'email
	 */
	public MailSender(String usr, String pwd, String to, String subject, String body){
		this.usr=usr;
		this.pwd=pwd;
		this.to=to;
		this.subject=subject;
		this.body=body;
		start();
	}
	
	public void run(){
		try {
			send_email(usr, pwd, to, subject, body);
		} catch (MessagingException e) {
			JOptionPane.showMessageDialog(null, "Errore invio email all'utente: " +to+ " , messaggio: "+body);
		}
	}

	/**
	 * metodo usato per l'invio della mail
	 * @param usr indica l'email del mittente
	 * @param pwd indica la password del mittente
	 * @param to indica il destinatario
	 * @param subject indica il soggetto dell'email
	 * @param body indica il corpo dell'email
	 * @throws SendFailedException per la gestione delle eccezioni
	 * @throws MessagingException per la gestione delle eccezioni
	 */
	public void send_email(String usr, String pwd, String to, String subject, String body) throws SendFailedException, MessagingException{
		String password=pwd;
		String username=usr;
		String host = "smtp.gmail.com";
		String from=username;
		Properties props = System.getProperties();
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.host",host);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port",465);
		Session session = Session.getInstance(props);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
		msg.setSubject(subject);
		msg.setText(body);

		Transport.send(msg,username,password);
	}



}
