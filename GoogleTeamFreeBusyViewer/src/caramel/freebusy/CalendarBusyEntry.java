package caramel.freebusy;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.Person;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;

public class CalendarBusyEntry extends CalendarEventEntry {

	private static final String WHEN_TAG = "gd:when";
	private static final String BUSY_TAG = "gCal:busy";
	private static final String XML_FOOT = "</feed>";
	private static final String XML_HEAD = "<?xml version='1.0' encoding='UTF-8'?><feed xmlns='http://www.w3.org/2005/Atom' xmlns:openSearch='http://a9.com/-/spec/opensearchrss/1.0/' xmlns:gCal='http://schemas.google.com/gCal/2005' xmlns:gd='http://schemas.google.com/g/2005'>";

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'H:m:s.S");
	private CalendarEventEntry cee;
	
	public CalendarBusyEntry(CalendarEventEntry cee){
		this.cee= cee;
	}
	
	public List<When> getTimes(){
		try {
			List<When> times = new ArrayList<When>();
	        Document doc = getBusyXmlDoc();
	    	times = getBusyWhenList(doc);
			return times;
		} catch (ParserConfigurationException e) {
			return null;
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	private List<When> getBusyWhenList(Document doc)
			throws ParseException {
		List<When> times = new ArrayList<When>();
		NodeList nBusy = doc.getElementsByTagName(BUSY_TAG);
		for(int x=0; x<nBusy.getLength(); x++){
			Element busy = (Element)nBusy.item(x);
			times.addAll(getWhenList(busy));
		}
		return times;
	}

	private List<When> getWhenList(Element busy)
			throws ParseException {
		List<When> times = new ArrayList<When>();
		NodeList nWhen = busy.getElementsByTagName(WHEN_TAG);
		for(int w=0; w<nWhen.getLength(); w++){
			Node nValue = (Node) nWhen.item(w);
			if(nValue!=null){
				When when = makeWhen(nValue);
		    	times.add(when);
			}
		}
		return times;
	}

	private Document getBusyXmlDoc() throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		String xml = cee.getXmlBlob().getBlob().toString();
		Document doc = builder.parse(new InputSource(new StringReader(XML_HEAD+xml+XML_FOOT)));
		return doc;
	}
	private When makeWhen(Node nValue) throws ParseException {
		String startTime = nValue.getAttributes().getNamedItem("startTime").getNodeValue();
		String endTime = nValue.getAttributes().getNamedItem("endTime").getNodeValue();
		When when = new When();
		when.setStartTime(new DateTime(df.parse(startTime)));
		when.setEndTime(new DateTime(df.parse(endTime)));
		return when;
	}
	public java.util.List<Person> getAuthors(){
		return cee.getAuthors();
	}
}
