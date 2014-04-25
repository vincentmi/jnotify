package jnotify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class CheckinGroup extends HashMap {	
	private static final long serialVersionUID = 1L;
	
	private String storePath = null;
	
	public void setStorePath(String path){
		storePath = path ;
	}

	public boolean store(String file){
		Iterator it = this.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		
		String[]  datelist = new String[this.size()];
		int index = 0 ;
		while(it.hasNext()){
			datelist[index] = (String) it.next();
			index++ ;
		}
		
		java.util.Arrays.sort(datelist);
		
		
		for(int i = 0 ; i < index;i++){
			Checkin ci = (Checkin)this.get(datelist[i]);
			sb.append(ci.checkin);
			sb.append(";");
			sb.append(ci.checkout);
			sb.append("\n");
		}
		sb.toString();
		File f = new File(file);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {				
				e.printStackTrace();
				return false;
			}
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(sb.toString().getBytes());
			fos.flush();
			fos.close();
			return true ;
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
			return false;
		} catch (IOException e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	public Checkin getCheckin(String date){
		return getCheckin(new Date(Date.parse(date)));
	}
	
	public Checkin getCheckin(Date date){
		String key = new SimpleDateFormat("yyyyMMdd").format(date);
		Checkin t = (Checkin) this.get(key);
		return t ;		
	}
	
	
	
	public Checkin setCheckOutTime(Date d){		
		String key = new SimpleDateFormat("yyyyMMdd").format(d);
		
		Checkin t = (Checkin) this.get(key);
		if(t == null){
			t = new Checkin();
		}
		
		t.checkout = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
		
		if(storePath != null){
			this.store(storePath);
		}
			
		return t;
	}
	
	public Checkin setCheckInTime(Date d){
		String key = new SimpleDateFormat("yyyyMMdd").format(d);		
		Checkin t = (Checkin) this.get(key);
		if(t == null){
			t = new Checkin();
			t.date = key;
			this.put(key, t);
		}
		if(t.checkin == null){
			t.checkin =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
			t.checkout = "";
			if(storePath!=null){
				this.store(storePath);
			}
		}
		return t;
	}
	
	
	public static CheckinGroup load(String file){
		CheckinGroup data = new CheckinGroup();
		try {
			File f =new File(file);
			FileInputStream fis= new FileInputStream(f);
			int len = fis.available();
			byte[] data1 = new byte[len];
			fis.read(data1);
			fis.close();
			String dataStr = new String(data1,"UTF-8");
			
			String[] lines = dataStr.split("\n");
			
			Date date ;
			
			DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int i = 0 ; i<lines.length;i++){
				if(!lines[i].equals("")){
					
					String[] part = lines[i].split(";");
					Checkin item = new Checkin();
					String key = "";
					if(part.length > 0 && part[0]!=null && !part[0].equals("")){
						
						date = fmt.parse(part[0]);
						key = new SimpleDateFormat("yyyyMMdd").format(date);
						item.date = key ;
						item.checkin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					}else{
						continue ;
					}
					
					if(part.length > 1 && part[1]!=null && !part[1].equals("")){
						date = fmt.parse(part[1]);
						item.checkout = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					}else{
						item.checkout = "";
					}
					data.put(key, item);
				}
			}
			data.setStorePath(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public String toListView(){
		
		Iterator it = this.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat f = new SimpleDateFormat("HH:mm");
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int total = 0 ;
		double totalMin = 0;
		
		String[]  datelist = new String[this.size()];
		int index = 0 ;
		while(it.hasNext()){
			datelist[index] = (String) it.next();
			index++ ;
		}
		
		java.util.Arrays.sort(datelist);
		
		
		for(int i = 0 ; i < index;i++){
			Checkin ci = (Checkin)this.get(datelist[i]);
			sb.append(ci.date);
			sb.append("     ");
			try {
				sb.append(f.format(f1.parse(ci.checkin)));
			} catch (ParseException e) {
				sb.append("-ERR-");
			}
			sb.append("  -   ");
			try {
				sb.append(f.format(f1.parse(ci.checkout)));
			} catch (ParseException e) {
				sb.append("-ERR-");
			}
			sb.append("     ");
			sb.append(ci.getElapseTime());
			sb.append("\n");
			total++ ;
			totalMin += ci.getElapse();
		}
		
		sb.append("\n");
		sb.append("Total Day : "+total);
		sb.append("\n");
		sb.append("Total Elapse : ");
		sb.append(Math.floor(totalMin/60));
		sb.append(" H ");
		sb.append(totalMin%60);
		sb.append(" M ");
		sb.append("\n");
		
		return sb.toString();
	}
	
	public String toString(){
		Iterator it = this.keySet().iterator();
		StringBuffer sb = new StringBuffer();
		while(it.hasNext()){
			Checkin ci = (Checkin)this.get(it.next());
			sb.append(ci.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
