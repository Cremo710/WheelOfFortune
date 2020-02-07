package game;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * la classe Md5Hash è usata per generare un hash md5 delle password per non salvare in chiaro i dati sensibili nel database
 * @author Andrea Peluso
 * @version 1.0
 */
public class Md5Hash {

	/**
	 * metodo usato per generare un hash md5 a partire da una stringa
	 * @param pswd stringa su cui effettuare l'hashing
	 * @return stringa sulla quale è stato fatto l'hashing
	 */
	public static String Hash(String pswd){
		String passwordToHash = pswd;
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			//Add password bytes to digest
			md.update(passwordToHash.getBytes());
			//Get the hash's bytes
			byte[] bytes = md.digest();
			//This bytes[] has bytes in decimal format;
			//Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hex format
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}
}
