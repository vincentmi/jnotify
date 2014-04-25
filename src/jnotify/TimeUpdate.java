package jnotify;

public class TimeUpdate implements Runnable {
	public boolean flag = true ;
	
	public void exit(){
		flag = false ;
	}
	public void run() {
		String str,slap;
		while(flag){
			str = "Checkin "+JNotify.today.getCheckinTime()+" Elapse "+JNotify.today.getElapseTime();
			JNotify.trayIcon.setToolTip(str);
			JNotify.itemElapse.setLabel("Elapse     "+JNotify.today.getElapseTime());
			//System.out.println(str);
			if(JNotify.today.getElapse() > Checkin.WORK_MIN){
				JNotify.trayIcon.setImage(JNotify.iconSafe);
			}else{
				JNotify.trayIcon.setImage(JNotify.icon);
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
