package caramel.freebusy;


import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.ServiceException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class FreeBusy  extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private static URL busyFeedUrl = null;
	private List<BusyPerson> busyTimes = new ArrayList<BusyPerson>();
	Map<String, Object> session = ActionContext.getContext().getSession(); 
	private String email;
	private String password;
	private String googleGroupURl;
	public String execute() {
		main(email, password, googleGroupURl, null, null);
		return SUCCESS;
	}
	public void main(String email, String password, String googleGroupEmailAddressUrl, String proxyHost, String proxyPort) throws NumberFormatException {	
		//Get the users data to login with
		CalendarService myService = new CalendarService("uniport-tvApp-1");
		setProxy(proxyHost,proxyPort);
		try {
			busyFeedUrl = new URL(googleGroupEmailAddressUrl);
		} catch (MalformedURLException e) {
			System.err.println("Please properly set a google group url.");
			e.printStackTrace();
		}
		
		String userName =  email;
		String userPassword = password;
		try {			
			myService.setUserCredentials(userName, userPassword);
			busyRangeQuery(myService, DateTime.parseDate(getDate()),DateTime.parseDate(getTomorrowsDate()) );
		} catch (IOException e) {
			// Communications error
			System.err.println("There was a problem communicating with the service.");
			e.printStackTrace();
		} catch (ServiceException e) {
			// Server side error
			System.err.println("The server had a problem handling your request.");
			e.printStackTrace();
		}
	}
	
	public void setProxy(String proxyHost, String proxyPort) {
		try {
		if (proxyHost != null) {
			System.setProperty("https.proxyHost", proxyHost);//"wwwcache.port.ac.uk");
			if (proxyPort != null) {

				System.setProperty("https.proxyPort",proxyPort);//"81");
			}
		}
		} catch (NullPointerException e) {
			System.err.println("Please properly set a proxy host and proxy port if you wish to use them, otherwise leave them blank.");
			e.printStackTrace();
		}
	}
	
	private void busyRangeQuery(CalendarService service, DateTime startTime, DateTime endTime) throws ServiceException,
			IOException{
		CalendarQuery myQuery = new CalendarQuery(busyFeedUrl);
		myQuery.setMinimumStartTime(startTime);
		myQuery.setMaximumStartTime(endTime);

		// Send the request and receive the response:
		CalendarEventFeed resultFeed = service.query(myQuery,CalendarEventFeed.class);
		
		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
			BusyPerson busyPerson = new BusyPerson();
			CalendarBusyEntry entry = new CalendarBusyEntry(resultFeed.getEntries().get(i));
			if (entry.getTimes().size() > 0) {
				busyPerson.setName(formatEmailName(entry.getAuthors().get(0).getName()));
				List<When> times = entry.getTimes();
				for (When time : times) {
					String startEndTime =  formatTime(time.getStartTime()) + "-" + formatTime(time.getEndTime());
					busyPerson.getTimes().add(startEndTime);
					if(getCurrentTime() >= time.getStartTime().getValue() && getCurrentTime() <= time.getEndTime().getValue()){
						busyPerson.setCurrentlyBusy(true);
					}
				}
				busyTimes.add(busyPerson);
			}			
		}
	}

	private static String formatEmailName(String authorName) {
		String[] splitName = authorName.split("\\.");// splitting on the dot
		String firstName = splitName[0];
		String surname = splitName[1].substring(0,splitName[1].indexOf('@'));// @ stuff on the end
		return capitalise(firstName) + " " + capitalise(surname);
	}

	private static String capitalise(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private static String formatTime(DateTime dateTime) {
		return dateTime.toString().substring(11, 16);
	}

	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime()).toString();
	}
	private String getTomorrowsDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,1);
		return dateFormat.format(cal.getTime()).toString();
	}
	private long getCurrentTime(){
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}
	public List<BusyPerson> getBusyTimes() {
		return busyTimes;
	}

	public void setBusyTimes(List<BusyPerson> busyTimes) {
		this.busyTimes = busyTimes;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGoogleGroupURl() {
		return googleGroupURl;
	}
	public void setGoogleGroupURl(String googleGroupURl) {
		this.googleGroupURl = googleGroupURl;
	}



}
