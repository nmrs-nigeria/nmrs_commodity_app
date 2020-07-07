/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.helper;

import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.xml.sax.SAXException;

// org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl cannot be cast to javax.xml.datatype.DatatypeFactory
/**
 * @author MORRISON.I
 */
public class RestUtils {

	//	private static final Log LOG = LogFactory.getLog(RestUtils.class);
	private static final int DATE_ONLY_TEXT_LENGTH = 10;
	private static final int DATE_TIME_TEXT_LENGTH = 16;
	private static final int DATE_TIME_SECOND_TEXT_LENGTH = 19;

	public RestUtils() {}

	public static Date parseCustomOpenhmisDateString(String dateText) {
		if (StringUtils.isEmpty(dateText)) {
			return null;
		}

		SimpleDateFormat dateFormat = null;
		if (dateText.length() == DATE_ONLY_TEXT_LENGTH) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else if (dateText.length() == DATE_TIME_TEXT_LENGTH) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		} else if (dateText.length() == DATE_TIME_SECOND_TEXT_LENGTH) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}

		Date result = null;
		if (dateFormat == null) {
			//		LOG.warn("Could not parse the date string '" + dateText + "'.");
		} else {
			try {
				result = dateFormat.parse(dateText);
			} catch (ParseException pex) {
				//		LOG.warn("Could not parse the date string '" + dateText + "'.", pex);
			}
		}

		return result;
	}

	public static String ensureDownloadFolderExist(HttpServletRequest request) {
		String folder = Paths.get(
		    new File(request.getSession().getServletContext().getRealPath(request.getContextPath())).getParentFile()
		            .toString(), "CMdownloads").toString();

		File dir = new File(folder);
		Boolean b = dir.mkdir();
		System.out.println("Creating download folder : " + folder + "was successful : " + b);
		return folder;
	}

	public static String ensureReportFolderExist(HttpServletRequest request, String reportType) {
		String downloadFolder = ensureDownloadFolderExist(request);
		//old implementation
		// String reportFolder = downloadFolder + "/" + reportType;
		String reportFolder = Paths.get(downloadFolder, reportType).toString();
		File dir = new File(reportFolder);
		dir.mkdir();
		System.out.println(reportType + " folder exist ? : " + dir.exists());

		//create today's folder
		boolean b;
		String dateString = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		//old implementation
		// String todayFolders = reportFolder + "/" + dateString;
		String todayFolders = Paths.get(reportFolder, dateString).toString();
		dir = new File(todayFolders);
		if (dir.exists()) {
			File[] previousFiles = dir.listFiles();
			assert previousFiles != null;
			for (File f : previousFiles) {
				b = f.delete();
				System.out.println("deleted previous xml successfully ? " + b);
			}
			b = dir.delete();
			System.out.println("deleted previous folder successfully ? " + b);
		}
		dir.mkdir();
		System.out.println(todayFolders + " folder exist ? " + dir.exists());

		return todayFolders;
	}

	public static String getFacilityName() {
		return Context.getAdministrationService().getGlobalProperty("Facility_Name");
	}

	public static String getFacilityType() {
		//use FAC for now
		return "FAC";
	}

	public static String getFacilityLocalId() {
		return Context.getAdministrationService().getGlobalProperty("facility_local_id");
	}

	public static String getIPShortName() {
		return Context.getAdministrationService().getGlobalProperty("partner_short_name");
	}

	public static Marshaller createMarshaller(JAXBContext jaxbContext) throws JAXBException, SAXException {
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		java.net.URL xsdFilePath = Thread.currentThread().getContextClassLoader().getResource("NDR_CM_1.0.xsd");

		assert xsdFilePath != null;

		Schema schema = sf.newSchema(xsdFilePath);

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		jaxbMarshaller.setSchema(schema);

		//Call Validator class to perform the validation
		jaxbMarshaller.setEventHandler(new Validator());
		return jaxbMarshaller;
	}

	public static XMLGregorianCalendar getXmlDate(Date date) throws DatatypeConfigurationException {

		XMLGregorianCalendar cal = null;
		String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
		if (date != null) {
			System.out.println("about to create datatypefactory");
			DatatypeFactory df = DatatypeFactory.newInstance();
			System.out.println("finished creating datatypefactory");
			cal = df
			        .newXMLGregorianCalendar(dateString);
		}
		return cal;
	}

	public static XMLGregorianCalendar getXmlDateTime(Date date) throws DatatypeConfigurationException {
		XMLGregorianCalendar cal = null;
		if (date != null) {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
			    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
		}
		return cal;
	}

	public static String zipFolder(HttpServletRequest request, String folderToZip,
	        String zipFileName, String reportType) {

		File toZIP = new File(folderToZip);
		if (!toZIP.exists() || toZIP.listFiles() == null || Objects.requireNonNull(toZIP.listFiles()).length == 0) {
			return "no new commodity record found";
		}

		ZipUtil appZip = new ZipUtil(folderToZip);

		appZip.zipIt(Paths.get(toZIP.getParent(), zipFileName).toString());

		//old implementation
		//  return request.getContextPath() + "/downloads/" + reportType + "/" + zipFileName;
		return Paths.get(request.getContextPath(), "CMdownloads", reportType, zipFileName).toString();
	}

}
