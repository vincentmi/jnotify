package jnotify;

import java.util.Date;

public class ExitThread extends Thread {
	public void run(){
		
		Date chOut = new Date();
		//System.out.println("Exit ...."+chOut);
		JNotify.month.setCheckOutTime(chOut);
	}
}
