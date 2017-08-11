package com.chinamobo.ue.utils;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 版本: [1.0]
 * 功能说明: GenericDateFormat
 *
 * 作者: wjx
 * 创建时间: 2016年2月26日 上午11:10:48
 */
public class GenericDateFormat extends DateFormat {
	private static final long serialVersionUID = -535311322420097236L;
	
	private static final String dateTimeFormatPattern ="yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(dateTimeFormatPattern);
	private static final String dateTimeFormatRegex = "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}";
	
	private static final String dateFormatPattern = "yyyy-MM-dd";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
	private static final String dateFormatRegex = "\\d{4}-\\d{1,2}-\\d{1,2}";
	
	public static final GenericDateFormat instance = new GenericDateFormat();
	
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		if(date != null){
			return dateTimeFormat.format(date, toAppendTo, fieldPosition);
		}
		return null;
	}

	public Date parse(String source, ParsePosition pos) {
		if(source.matches(dateTimeFormatRegex)){
			return dateTimeFormat.parse(source, pos);
		}
		if(source.matches(dateFormatRegex)){
			return dateFormat.parse(source, pos);
		}
		throw new RuntimeException(new ParseException(String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)",
			source, dateTimeFormatPattern + "," + dateFormatPattern), pos.getErrorIndex()));
	}
	
	public GenericDateFormat clone() {
		return new GenericDateFormat();
	}

}

