package lk.ci.int_cn_system.Utils;

import java.util.ArrayList;
import java.util.List;

public class VehiNoPatternValidator {

    public static String generate(String pro,String let,String no){


        if(pro.isEmpty()){

            if(let.length()==1){

                return (let+"   "+no).toUpperCase();

            }

            if(let.length()==2){

                return (let+"  "+no).toUpperCase();

            }

            if(let.length()==3){

                return (let+" "+no).toUpperCase();

            }
        }else{

            if(let.length()==2){

                return (pro+" "+let+"  "+no).toUpperCase();

            }

            if(let.length()==3){

                return (pro+" "+let+" "+no).toUpperCase();

            }
        }


        return "";
    }
    
    public static List<String> divide(String vehiNo) {
    	List<String> out=new ArrayList<String>();
    	
    	 for(String s:vehiNo.split(" ")) {
    		if(!s.trim().isEmpty()) {
    			out.add(s);
    		}
    	 }
    	 
    	 if(out.size()==2) {
    		 out.add(0, null);
    	 }
    	
    	return out;
    }

}