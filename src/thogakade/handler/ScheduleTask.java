package thogakade.handler;

import java.util.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleTask {

	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static Timer timer = new Timer();

	private static class MyTimeTask extends TimerTask {
		public void run() {
                    try {
                        System.out.println("Running Task");
                        Downloader downloader = new Downloader();
                        downloader.downloadFile();
                        System.out.println("Current Time: " + df.format(new Date()));
                        timer.cancel();
                    } catch (IOException ex) {
                        Logger.getLogger(ScheduleTask.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ScheduleTask.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ScheduleTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
	}

	public static void main(String[] args) throws ParseException {

		System.out.println("Current Time: " + df.format(new Date()));

		// Date and time at which you want to execute
		Date date = df.parse("2021-06-10 16:07:00");

		timer.schedule(new MyTimeTask(), date);
	}
}
