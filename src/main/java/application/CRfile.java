package application;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;

public class CRfile {
	public static int[] read(String location) throws InvalidDescriptionException,InvalidValueException {
		int counter = 0;  //reads a description and return the values if they are valid
		String[] data = new String[5];
		int res[] = {0,0,0,0};
		try {
			File file = new File(location);
			Scanner Reader = new Scanner(file);
			
			
			while (Reader.hasNextLine()) {
				data[counter] = Reader.nextLine();
				counter++;
				if (counter == 5)
					break;
				}
			Reader.close();
		} catch(FileNotFoundException e) {
			Popup.message("This scenario id is not valid");
			return res;
		}
		
		if(counter!=4) {
			throw new InvalidDescriptionException();
		}
		try {
			for(int i = 0; i < 4; i++) {
				res[i] = Integer.parseInt(data[i]);
			}
		} catch(NumberFormatException e) {
			throw new InvalidValueException("Some values were not numbers");
		}
		if (res[0] == 1) {
			if(res[1] > 8 && res[1] < 12 && res[2] > 119 && res[2] < 181 && res[3] == 0) {
				Popup.message("Scenario was loaded");
				return res;
			}
			else {
				throw new InvalidValueException();
			}
			
		}
		else if(res[0] == 2) {
			if(res[1] > 34 && res[1] < 46 && res[2] > 239 && res[2] < 361 && res[3] == 1) {
				Popup.message("Scenario was loaded");
				return res;
			}
			else {
				throw new InvalidValueException();
			}
		}
		else {
			throw new InvalidValueException();
		}
	}
	
	public static void create(String location, int[] values) {//creates description with the values given
		try { 
			File file = new File(location);
			if (file.createNewFile()) {
				FileWriter w = new FileWriter(location);
				int size = values.length;
				for(int i = 0; i < size; i++) {
					String temp = Integer.toString(values[i]);
					temp += '\n';
					w.write(temp);
				}
				w.close();
				Popup.message("scenario was created");
			} else {
				Popup.message("this scenario id already exists \n");
			}
	} catch(IOException e){
		Popup.message("An error has occured, the file was not created");
	}
	}
	
}