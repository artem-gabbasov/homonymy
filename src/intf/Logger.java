package intf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class Logger {

	FileOutputStream fos = null;
	
	public Logger(String fileName, boolean full)
	{
		if (full)
		{
			createFile(fileName);
		}
		else
		{
			Calendar calendar = Calendar.getInstance();
			
			createFile(fileName + "-" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.SECOND) + ".log");		
		}
	}
	
	private void createFile(String fileName)
	{
		File outputFile = new File(fileName);
		
		try {
			if (outputFile.createNewFile())
			{
				try {
					fos = new FileOutputStream(outputFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				System.err.println("File " + fileName + "already exists!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void log(String string)
	{
		if (fos != null)
		{
			try {
				fos.write((string + "\n").getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void close()
	{
		if (fos != null)
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
