package lk.ci.int_cn_system.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5 {
	private static MessageDigest md;
    
    
 public static String cryptWithMD5(String pass){
   try {
       md = MessageDigest.getInstance("MD5");
       byte[] passBytes = pass.getBytes();
       md.reset();
       byte[] digested = md.digest(passBytes);
       StringBuffer sb = new StringBuffer();
       for(int i=0;i<digested.length;i++){
           sb.append(Integer.toHexString(0xff & digested[i]));
       }
       return sb.toString();
   } catch (NoSuchAlgorithmException ex) {
     ///  Logger.getLogger(CryptWithMD5.class.getName()).log(Level.SEVERE, null, ex);
     ex.printStackTrace();
   }
       return null;


  }
    
    public static void main(String args[]) {
    	
    	System.out.println(cryptWithMD5("74012601"));
    }
}
