package jnotify;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Checkin {
	public static long  WORK_MIN = 405 ;
	public String date = null;
	public String checkin = null;
	public String checkout = null;
	public String toString(){
		return date+";CHECK_IN="+checkin+";CHECK_OUT="+checkout ;
	}
	
	
	public String getCheckinTime(){
		if(checkin != null){
			return checkin.substring(11);
		}else{
			return "NA";
		}
	}
	
	public String getElapseTime(){
		double elapse = getElapse();
		System.out.println(elapse);
		double hour = Math.floor(elapse/60);
		double min = elapse%60;
		StringBuilder sb = new StringBuilder();
		if(hour > 0){
			sb.append(hour);
			sb.append(" H ");
		}
		
		if(min > 0){
			sb.append(min);
			sb.append(" M ");
		}
		
		return sb.length() == 0? "NA" : sb.toString();
		
	}
	
	public double getElapse(){
		String nowStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String cCheckOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		System.out.print("nowStr=");
		System.out.println(nowStr);
		
		System.out.print("cCheckOut=");
		System.out.println(cCheckOut);
		
		System.out.print("this.date=");
		System.out.println(date);		
		
		if(!nowStr.equals(this.date)){
			cCheckOut = checkout ;
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		try {
			
			Calendar iCal = Calendar.getInstance();
			iCal.setTime(df.parse(checkin));
			Calendar oCal = Calendar.getInstance();
			oCal.setTime(df.parse(cCheckOut));
			//System.out.println(iCal.toString());
			//System.out.println(oCal.toString());
			
			double in = iCal.get(Calendar.HOUR_OF_DAY) * 60 + iCal.get(Calendar.MINUTE);
			double out = oCal.get(Calendar.HOUR_OF_DAY) * 60 + oCal.get(Calendar.MINUTE);
			
			if(out < in){
				return  0 ;
			}
			double diff = 0;
			System.out.println(in);
			System.out.println(out);
			
						
			int t08_30 = 510 ;			
			int t12_00 = 720 ;
			int t13_30 = 810 ;
			int t15_00 = 900 ;
			int t15_15 = 915 ;
			
			
			
			diff = out - in ;
			
			if(in < t08_30 ){
				if(out < t12_00){
					diff = diff  -  (t08_30 - in);
				}else if(out < t13_30){
					diff = diff  -  (t08_30 - in) - (out - t12_00); 
				}else if(out < t15_00){
					diff = diff  -  (t08_30 - in) - 90; 
				}else if(out < t15_15){
					diff = diff  -  (t08_30 - in) - 90 - (out - t15_00); 
				}else{
					diff = diff  -  (t08_30 - in) - 90 - 15 ;
				}
			}else if(in < t12_00 ){
				if(out < t12_00){
					
				}else if(out < t13_30){
					diff = diff  -  (out - t12_00); 
				}else if(out < t15_00){
					diff = diff  -  90; 
				}else if(out < t15_15){
					diff = diff  -  90 - (out - t15_00); 
				}else{
					diff = diff  -  90 - 15 ;
				}
			}else if(in < t13_30 ){
				if(out < t13_30){
					diff = 0; 
				}else if(out < t15_00){
					diff = diff  -  (t12_00 - in); 
				}else if(out < t15_15){
					diff = diff  -  (t12_00 - in) - (out - t15_00); 
				}else{
					diff = diff  -  (t12_00 - in) - 15 ;
				}
			}else if(in < t15_00 ){
				if(out < t15_00){
					
				}else if(out < t15_15){
					diff = diff  - (out - t15_00); 
				}else{
					diff = diff  - 15 ;
				}
			}else if(in < t15_15 ){
				if(out < t15_15){
					diff = 0; 
				}else{
					diff = diff  - (t15_15 - in) ;
				}
			}
			
						
			return diff;
			
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
