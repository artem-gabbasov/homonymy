package intf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {

	public byte[] readFile(String fileName)
	{
		try
		{
			File inputFile = new File(fileName);
			FileInputStream fis = new FileInputStream(inputFile);

			byte[] bytes = new byte[(int)inputFile.length()];
            fis.read(bytes);

            fis.close();
            
            return bytes;
		} catch (FileNotFoundException e)
		{
			System.err.println("File '" + fileName + "' not found");
		} catch (IOException e) {
            System.err.println(e.getMessage());
        }
		
		return null;
	}
}
