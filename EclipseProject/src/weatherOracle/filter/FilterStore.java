package weatherOracle.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import weatherOracle.control.MainControl;

import android.util.Pair;

public abstract class FilterStore {
	private static int versionNumber = 0;
	private static byte[] data;
	
	public synchronized static void setFilters(List<Filter> filters) {

		ByteArrayOutputStream fbos = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(fbos);
			out.writeObject(filters);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		data=fbos.toByteArray();
		modified();
		versionNumber++;
		writeOut();
	}
	
	// save data to file
	private synchronized static void writeOut(){
		//TODO
	}
	
	// read data from file, if there is a file
	private synchronized static void readIn(){
		//TODO
	}
	
	public synchronized static Pair<List<Filter>,Integer> getFilters(){
		if (data==null){ // no data set or read in yet
			readIn();
			if (data==null){ // nothing to read/read failed
				return new Pair<List<Filter>,Integer>(new ArrayList<Filter>(),versionNumber);
			}
		}
		
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(data));
			List<Filter> copy=(List<Filter>)in.readObject();
			return new Pair<List<Filter>,Integer>(copy,versionNumber);
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assert(false);
		return null;
	}
	
	private static void modified(){
		MainControl.startUpdate();
	}
}
