package jnotify;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class JNotify {
	
	public static TrayIcon trayIcon ;
	public static Image icon ;
	public static Image iconSafe;
	public static PopupMenu popup;
	public static MenuItem itemElapse;
	public static String dataPath;
	
	public static Checkin today ;
	public static CheckinGroup month ;
	public static TimeUpdate tu ;
	
	 public static void print(Object o) {  
	        System.out.println(o);  
	    }  
	 
	public static void main(String[] args) {        
		String path = System.getProperty("java.class.path");
		int firstIndex = path.lastIndexOf(System.getProperty("path.separator"))+1;
		int lastIndex = path.lastIndexOf(File.separator) +1;
		path = path.substring(firstIndex, lastIndex);
				
       dataPath = path+File.separator+"data";
       
      // String path = System.class.getProtectionDomain().getCodeSource().getLocation().getPath();
       // print(path);
       
		if (SystemTray.isSupported()) {
			
			SystemTray tray = SystemTray.getSystemTray();
			
			icon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icon.png"));
			iconSafe = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("icon_safe.png"));
			
			popup = new PopupMenu();
			
			loadCurrentFile();
			
			MenuItem itemHistory = new MenuItem("History");
			MenuItem itemQuit = new MenuItem("Quit");
			itemQuit.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							JNotify.tu.exit();
							System.exit(0);
						}
			});
			
			itemHistory.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog(null,month.toListView());
				}
				
			});
			
			
			itemElapse = new MenuItem("Elapse     "+today.getCheckinTime());
			
			popup.add("CheckinMgr 1.0");
			popup.addSeparator();
			popup.add("Sign in at   "+today.getCheckinTime());
			popup.add(itemElapse);
			popup.addSeparator();
			popup.add(itemHistory);
			popup.addSeparator();
			popup.add(itemQuit);
			
			trayIcon = new TrayIcon(icon, "Checkin "+today.getCheckinTime()+" Elapse "+today.getElapseTime(), popup);
			tu = new TimeUpdate();
			new Thread(tu).start();
			Runtime.getRuntime().addShutdownHook(new ExitThread());
			
			//System.out.println(month);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				JOptionPane.showMessageDialog(null,"Fail to add trayicon£º " + e);
			}
		} else {
			JOptionPane.showMessageDialog(null,"tray icon fail!");
		}
	}
	
	
	
	public static void loadCurrentFile(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		File d = new File(dataPath);
		if(!d.exists() || !d.isDirectory()){
			d.mkdir();
		}
		String file = dataPath +File.separator+sdf.format(new Date())+".log";
		File f = new File(file);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Fail to create data folder("+f.getAbsolutePath()+").");
			}
		}
		month = CheckinGroup.load(file);
		month.setCheckInTime(new Date());
		today = month.getCheckin(new Date());		
	}
	
	
	
	public static String readSystemStartTime() throws IOException,InterruptedException {
		Process process = Runtime.getRuntime().exec("cmd /c net statistics workstation");
		String startUpTime = "";
		BufferedReader bufferedReader = new BufferedReader(
		        new InputStreamReader(process.getInputStream()));
		int i = 0;
		String timeWith = "";
		while ((timeWith = bufferedReader.readLine()) != null) {
		    if (i == 3) {
		        System.out.println(timeWith);
		        startUpTime = timeWith;
		    }
		    i++;
		}
		process.waitFor();
		return startUpTime;
	}
	
	
}
