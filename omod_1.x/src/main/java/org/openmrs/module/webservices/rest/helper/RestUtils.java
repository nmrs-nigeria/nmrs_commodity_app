/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.openhmis.commons.api.Utility;

/**
 * @author MORRISON.I
 */
public class RestUtils {

	private static final Log LOG = LogFactory.getLog(Utility.class);
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
			LOG.warn("Could not parse the date string '" + dateText + "'.");
		} else {
			try {
				result = dateFormat.parse(dateText);
			} catch (ParseException pex) {
				LOG.warn("Could not parse the date string '" + dateText + "'.", pex);
			}
		}

		return result;
	}

}
