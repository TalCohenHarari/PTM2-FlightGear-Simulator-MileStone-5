package model.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


public class FileCacheManager<Problem,Solution> implements CacheManager<Problem,Solution>  {
	// Data members.
	HashMap<Problem,Solution> disc;
	Properties prop;
	
	// CTOR.
	@SuppressWarnings("unchecked")
	public FileCacheManager() {
		prop = new Properties();
		String name = "cachManager.properties";
		try {
			prop.load(new FileInputStream(name));
		} catch (FileNotFoundException e) { } 
		  catch (IOException e) { e.printStackTrace(); }
		
		this.disc = new HashMap<>();
		if(prop != null)
		{
			Enumeration<?> enumeration = prop.propertyNames();
			while(enumeration.hasMoreElements())
			{
				Problem key = (Problem)enumeration.nextElement();
				if(key != null) { this.disc.put(key,(Solution) prop.get(key)); }
			}
		}		
	}
	
	// Returns true if there is already a solution to the received problem.
	@Override
	public Boolean Check(Problem in) {
		if(disc.isEmpty()) { return false; }
		
		return disc.containsKey(in);
	}

	// Returns the solution to the received problem.
	@Override
	public Solution Extract(Problem in) { return disc.get(in); }

	// Saves the solution to the received problem.
	@Override
	public void Save(Problem in, Solution out) {
		disc.put(in, out);
		prop.putAll(this.disc);
		
		String name = "cachManager.properties";
		
		try {
			prop.store(new FileOutputStream(name), null);
		} catch (FileNotFoundException e) { e.printStackTrace(); } 
		  catch (IOException e) { e.printStackTrace(); }
	}
}