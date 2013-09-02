package caramel.freebusy;

import java.util.ArrayList;
import java.util.List;

public class BusyPerson {
	private String name;
	private List<String> times = new ArrayList<String>();
	private boolean currentlyBusy;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getTimes() {
		return times;
	}
	public void setTimes(List<String> times) {
		this.times = times;
	}
	public boolean isCurrentlyBusy() {
		return currentlyBusy;
	}
	public void setCurrentlyBusy(boolean currentlyBusy) {
		this.currentlyBusy = currentlyBusy;
	}
	
}
