package thogakade.Shared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DateHandler {
    private static DateHandler instance;

    public static DateHandler getSharedInstance() {
	if (instance == null) {
	    instance = new DateHandler();
	}
	return instance;
    }

    public Date getCurrentDate() {
	return new Date(System.currentTimeMillis());
    }
    
    /**
     * Do not change the default value null for date Used in API promotion info
     * date parsing
     * 
     * @param dateString
     * @param formatType
     * @return
     */
    public Date getFormatedDateDateForType(String dateString, String formatType) {
	Date date = null;
	if (dateString != null && !dateString.isEmpty()) {
	    SimpleDateFormat format = new SimpleDateFormat(formatType);
	    try {
		date = format.parse(dateString);
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	}
	return date;
    }

    public String getFormatedDateForFormat(Date date, String format) {
	String returnstr = "";
	if (date != null) {
	    try {
		returnstr = new SimpleDateFormat(format).format(date);
	    } catch (Exception e) {
		returnstr = "";
	    }
	}
	return returnstr;
    }

    public Long getDaysBetweenDates(String startdate, String enddate) {
	Long dayscount = null;
	SimpleDateFormat format = new SimpleDateFormat(Shared.DB_TRANSACTION_DATE_FORMAT);
	try {
	    Date date1 = format.parse(startdate);
	    Date date2 = format.parse(enddate);
	    long diff = date2.getTime() - date1.getTime();
	    System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
	    dayscount = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return dayscount;
    }

    public Long getTimeBetweenDatesByMinutes(String startdate, String enddate) {
	Long dayscount = null;
	SimpleDateFormat format = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
	try {
	    Date date1 = format.parse(startdate);
	    Date date2 = format.parse(enddate);
	    long difference = date1.getTime() - date2.getTime();
	    System.out.println("difference" + difference);
	    System.out.println("Miniutes: " + TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));
	    dayscount = TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return dayscount;
    }

    public Long getTimeBetweenDatesBySeconds(String startdate, String enddate) {
	Long dayscount = null;
	SimpleDateFormat format = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
	try {
	    Date date1 = format.parse(startdate);
	    Date date2 = format.parse(enddate);
	    long difference = date1.getTime() - date2.getTime();
	    System.out.println("difference" + difference);
	    System.out.println("Miniutes: " + TimeUnit.SECONDS.convert(difference, TimeUnit.MILLISECONDS));
	    dayscount = TimeUnit.SECONDS.convert(difference, TimeUnit.MILLISECONDS);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return dayscount;
    }

    public String getFormatedDate(Date date) {
	String returnstr = "";
	if (date != null) {
	    try {
		returnstr = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT).format(date);
	    } catch (Exception e) {
		returnstr = "";
	    }
	}
	return returnstr;
    }

    public String getFormatedDateDateOnly(Date date) {
	String returnstr = "";
	if (date != null) {
	    try {
		returnstr = new SimpleDateFormat(Shared.DB_TRANSACTION_DATE_FORMAT).format(date);
	    } catch (Exception e) {
		returnstr = "";
	    }
	}
	return returnstr;
    }

    public Date getDefaultExpDate() {
	Calendar cal = Calendar.getInstance();
	cal.set(2099, 12, 31);
	Date exdate = cal.getTime();
	return exdate;
    }

    public String getFormatedDateWithDefault(Date date, String defaultValue) {
	String retString = defaultValue;
	if (date != null) {
	    SimpleDateFormat format = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
	    retString = format.format(date);
	}
	return retString;
    }

    public String getFormatedDate(Date date, boolean timestamp) {
	SimpleDateFormat format_date = new SimpleDateFormat(Shared.DF_BUSINESS_DATE_FORMAT);
	SimpleDateFormat format_tomestamp = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
	SimpleDateFormat selectformat = format_date;
	if (timestamp) {
	    selectformat = format_tomestamp;
	}
	if (date != null) {
	    return selectformat.format(date);

	} else {
	    return selectformat.format(new Date(System.currentTimeMillis()));
	}
    }

    public String getFormatedDateOnly(Date date) {
	SimpleDateFormat format_date = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
	if (date != null) {
	    return format_date.format(date);

	} else {
	    return format_date.format(new Date(System.currentTimeMillis()));
	}
    }

    public Date getFormatedDate(String datestr) {
	Date date = new Date(System.currentTimeMillis());
	if (date != null) {
	    SimpleDateFormat format = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
	    try {
		date = format.parse(datestr);
	    } catch (ParseException e) {
		e.printStackTrace();
		date = new Date(System.currentTimeMillis());
	    }
	}
	return date;
    }

    public Date getDateForFormat(String dateString, String formatString) {
	Date date = null;
	if (dateString != null) {
	    SimpleDateFormat format = new SimpleDateFormat(formatString);
	    try {
		date = format.parse(dateString);
	    } catch (ParseException e) {
		e.printStackTrace();
		date = null;
	    }
	}
	return date;
    }

    public Date getFormatedDateDateOnly(String datestr) {
	Date date = new Date(System.currentTimeMillis());
	if (date != null) {
	    SimpleDateFormat format = new SimpleDateFormat(Shared.DB_TRANSACTION_DATE_FORMAT);
	    try {
		date = format.parse(datestr);
	    } catch (ParseException e) {
		e.printStackTrace();
		date = new Date(System.currentTimeMillis());
	    }
	}
	return date;
    }

    public String convertDateFromat(String value, String currentFormat, String newFormat) {
	String outValue = null;
	try {
	    SimpleDateFormat format1 = new SimpleDateFormat(currentFormat);
	    SimpleDateFormat format2 = new SimpleDateFormat(newFormat);
	    Date date = format1.parse(value);
	    outValue = format2.format(date);
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return outValue;
    }

    public String convertDateFromat(Date date, String format) {
	String outValue = null;
	try {
	    SimpleDateFormat sFormat = new SimpleDateFormat(format);
	    outValue = sFormat.format(date);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return outValue;
    }

    public boolean isToday(long timestamp) {
	Calendar now = Calendar.getInstance();
	Calendar timeToCheck = Calendar.getInstance();
	timeToCheck.setTimeInMillis(timestamp);
	return (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
		&& now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR));
    }

    public boolean isYesterday(long timestamp) {
	Calendar yesterday = Calendar.getInstance();
	yesterday.add(Calendar.DATE, -1);
	Calendar timeToCheck = Calendar.getInstance();
	timeToCheck.setTimeInMillis(timestamp);
	return (yesterday.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
		&& yesterday.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR));
    }

    public int daysBetween(Date startDate) {
	Calendar day1 = Calendar.getInstance();
	day1.setTime(startDate);
	Calendar day2 = Calendar.getInstance();
	Calendar dayOne = (Calendar) day1.clone();
	Calendar dayTwo = (Calendar) day2.clone();
	int difference = 0;
	if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
	    difference = dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR);
	} else {
	    boolean swapped = false;
	    if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
		// swap them
		Calendar temp = dayOne;
		dayOne = dayTwo;
		dayTwo = temp;
		swapped = true;
	    }
	    int extraDays = 0;

	    int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

	    while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
		dayOne.add(Calendar.YEAR, -1);
		// getActualMaximum() important for leap years
		extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
	    }

	    difference = extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
	    if (swapped) {
		difference = difference * -1;
	    }
	}
	System.out.println(startDate + " DIFF:" + difference);
	return difference;
    }

    public long daysBetween1(Date startDate) {
	Calendar date = Calendar.getInstance();
	date.setTime(startDate);
	Calendar endDate = Calendar.getInstance();
	long daysBetween = 0;
	if (date.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)
		&& date.get(Calendar.DAY_OF_YEAR) == endDate.get(Calendar.DAY_OF_YEAR)) {
	    daysBetween = 0;
	    System.out.println(startDate + " DIFF:" + daysBetween);
	    return daysBetween;
	}
	while (date.before(endDate)) {
	    date.add(Calendar.DAY_OF_MONTH, 1);
	    daysBetween++;
	}
	System.out.println(startDate + " DIFF:" + daysBetween);
	return daysBetween;
    }

    /**
     * Get a diff between two dates
     * 
     * @param date1
     *            the oldest date
     * @param timeUnit
     *            the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public long getDateDiff(Date date1, TimeUnit timeUnit) {
	Date date2 = getCurrentDate();
	return getDateDiff(date1, date2, timeUnit);
    }

    /**
     * Get a diff between two dates
     * 
     * @param date1
     *            the oldest date
     * @param date2
     *            the newest date
     * @param timeUnit
     *            the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	long diffInMillies = date2.getTime() - date1.getTime();
	return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public Date getMaxDate(Date a, Date b) {
	return a == null ? b : (b == null ? a : (a.after(b) ? a : b));
    }

    public Date getMinDate(Date a, Date b) {
	return a == null ? b : (b == null ? a : (a.before(b) ? a : b));
    }

    public boolean isFirstDateTimeOld(Date dt_1, Date dt_2) {
	boolean firstDateOld = false;
	if (dt_1.compareTo(dt_2) > 0) {
	    // System.out.println("Date 1 occurs after Date 2");
	} // compareTo method returns the value greater than 0 if this Date is
	  // after the Date argument.
	else if (dt_1.compareTo(dt_2) < 0) {
	    firstDateOld = true;
	    // System.out.println("Date 1 occurs before Date 2");
	} // compareTo method returns the value less than 0 if this Date is
	  // before the Date argument;
	else if (dt_1.compareTo(dt_2) == 0) {
	    System.out.println("Both are same dates");
	} // compareTo method returns the value 0 if the argument Date is equal
	  // to the second Date;
	else {
	    System.out.println("You seem to be a time traveller !!");
	}
	return firstDateOld;
    }

    public boolean isGivenTimePassedByNow(Date givenTime) {
	boolean givenTimePassed = false;
	Date nowTime = getSharedInstance().getCurrentDate();
	Calendar givenTimeCal = Calendar.getInstance();
	givenTimeCal.setTime(givenTime);
	Calendar nowTimeCal = Calendar.getInstance();
	nowTimeCal.setTime(nowTime);

	if (givenTimeCal.get(Calendar.HOUR_OF_DAY) < nowTimeCal.get(Calendar.HOUR_OF_DAY)) {
	    givenTimePassed = true;
	    // System.out.println("givenTime: "+givenTime +
	    // " nowTime:"+nowTime+" GIVEN HOUR "
	    // +givenTimeCal.get(Calendar.HOUR)+
	    // " HOUR IS PASSED"+" NOW HOUR "+nowTimeCal.get(Calendar.HOUR) );
	    // System.out.println("$$$$$$$$$$$$$givenTimePassed:
	    // "+givenTimePassed+"\n");
	    return givenTimePassed;
	}
	if (givenTimeCal.get(Calendar.HOUR_OF_DAY) == nowTimeCal.get(Calendar.HOUR_OF_DAY)
		&& givenTimeCal.get(Calendar.MINUTE) <= nowTimeCal.get(Calendar.MINUTE)) {
	    givenTimePassed = true;
	    // System.out.println("givenTime: "+givenTime + " nowTime:"+nowTime+
	    // " MINUTE IS PASSED or EQUAL");
	    // System.out.println("$$$$$$$$$$$$$givenTimePassed:
	    // "+givenTimePassed+"\n");

	    return givenTimePassed;
	} else {
	    // System.out.println("givenTime: "+givenTime + " nowTime:"+nowTime+
	    // " NOTHING PASSED");
	}
	// System.out.println("############givenTimePassed:
	// "+givenTimePassed+"\n");
	return givenTimePassed;
    }

    public boolean isGivenDatePassedByNow(Date givenTime) {
	boolean givenTimePassed = false;
	Date nowTime = getSharedInstance().getCurrentDate();
	Calendar givenTimeCal = Calendar.getInstance();
	givenTimeCal.setTime(givenTime);
	Calendar nowTimeCal = Calendar.getInstance();
	nowTimeCal.setTime(nowTime);
	// 2015-05-17 //2015 > 2016
	if (givenTimeCal.get(Calendar.YEAR) < nowTimeCal.get(Calendar.YEAR)) {
	    givenTimePassed = true;
	    System.out.println(" GIVEN TIME" + givenTime + " NOW " + nowTimeCal + " YEAR PASSED");
	} // 10 11
	if (givenTimeCal.get(Calendar.YEAR) == nowTimeCal.get(Calendar.YEAR)
		&& givenTimeCal.get(Calendar.DAY_OF_YEAR) < nowTimeCal.get(Calendar.DAY_OF_YEAR)) {
	    givenTimePassed = true;
	    System.out.println(" GIVEN TIME" + givenTime + " NOW " + nowTimeCal + " DAY PASSED");
	}

	return givenTimePassed;
    }

    public Date addMonthsToNow(int noOfMonths) {
	Date date = null;
	try {
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.MONTH, noOfMonths);
	    date = cal.getTime();
	} catch (Exception e) {

	}
	return date;
    }

    public Date adjustToMonthEnd(Date date) {

	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	    date = cal.getTime();
	} catch (Exception e) {

	}
	return date;
    }

    public Date adjustToMonthStart(Date date) {

	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
	    date = cal.getTime();
	} catch (Exception e) {

	}
	return date;
    }

    public Date addDaysToGivenDate(int days, Date date) {

	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DATE, days);
	    date = cal.getTime();
	} catch (Exception e) {

	}
	return date;
    }

    public Date addMonthsToGivenDate(int noOfMonths, Date date) {

	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.MONTH, noOfMonths);
	    date = cal.getTime();
	} catch (Exception e) {

	}
	return date;
    }

    public Date addSecondsToGivenDate(int seconds, Date date) {

	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.SECOND, seconds);
	    date = cal.getTime();
	} catch (Exception e) {

	}
	return date;
    }

    public int getDayOfWeek(Date date) {
	int day = 0;
	try {
	    Calendar now = Calendar.getInstance();
	    now.setTime(date);
	    day = now.get(Calendar.DAY_OF_WEEK);
	} catch (Exception ex) {

	}
	return day;
    }

    public Date adjustToDateEnd(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.HOUR_OF_DAY, 23);
	calendar.set(Calendar.MINUTE, 59);
	calendar.set(Calendar.SECOND, 59);
	calendar.set(Calendar.MILLISECOND, 999);
	return calendar.getTime();
    }

    public Date adjustToDateStart(Date date) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	return calendar.getTime();
    }

    public static void main(String[] args) {
	DateHandler datehandler = DateHandler.getSharedInstance();
	int factor = -1;
	Calendar yesterday = Calendar.getInstance();
	/*
	 * SimpleDateFormat myFormat = new
	 * SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT); String
	 * inputString1 = "2015-12-31 10:01:39"; try { Date date1 =
	 * myFormat.parse(inputString1); yesterday.setTime(date1); } catch
	 * (ParseException e) { e.printStackTrace(); }
	 */
	Date nowDate = DateHandler.getSharedInstance().getCurrentDate();
	Date startDate = DateHandler.getSharedInstance().addDaysToGivenDate(-30, nowDate);
	// Date startDate =
	// DateHandler.getSharedInstance().adjustToMonthStart(nowDate);
	Date endDate = DateHandler.getSharedInstance().adjustToMonthEnd(nowDate);
	System.out.println(startDate);
	System.out.println(endDate);
	datehandler.daysBetween(yesterday.getTime());
	yesterday.add(Calendar.DATE, 0);
	datehandler.daysBetween(yesterday.getTime());
	yesterday.add(Calendar.DATE, 1 * factor);
	datehandler.daysBetween(yesterday.getTime());
	yesterday.add(Calendar.DATE, 1 * factor);
	datehandler.daysBetween(yesterday.getTime());
	yesterday.add(Calendar.DATE, 1 * factor);
	datehandler.daysBetween(yesterday.getTime());
	yesterday.add(Calendar.DATE, 1 * factor);
	datehandler.daysBetween(yesterday.getTime());
	yesterday = Calendar.getInstance();
	yesterday.add(Calendar.HOUR_OF_DAY, 2 * factor);
	datehandler.daysBetween(yesterday.getTime());
	yesterday = Calendar.getInstance();
	yesterday.add(Calendar.MINUTE, 2 * factor);
	datehandler.daysBetween(yesterday.getTime());
	yesterday = Calendar.getInstance();
	yesterday.add(Calendar.SECOND, 2 * factor);
	datehandler.daysBetween(yesterday.getTime());

	yesterday.add(Calendar.YEAR, 0);
	datehandler.daysBetween(yesterday.getTime());
	yesterday.add(Calendar.YEAR, 1 * factor);
	datehandler.daysBetween(yesterday.getTime());
	yesterday.add(Calendar.YEAR, 1 * factor);
	datehandler.daysBetween(yesterday.getTime());

	/**
	 * Thu Dec 08 12:33:21 IST 2016 DIFF:0 Thu Dec 08 12:33:21 IST 2016
	 * DIFF:0 Fri Dec 09 12:33:21 IST 2016 DIFF:1 Sat Dec 10 12:33:21 IST
	 * 2016 DIFF:2 Sun Dec 11 12:33:21 IST 2016 DIFF:3 Mon Dec 12 12:33:21
	 * IST 2016 DIFF:4 Thu Dec 08 14:33:21 IST 2016 DIFF:0 Thu Dec 08
	 * 12:35:21 IST 2016 DIFF:0 Thu Dec 08 12:33:23 IST 2016 DIFF:0 Thu Dec
	 * 08 12:33:23 IST 2016 DIFF:0 Fri Dec 08 12:33:23 IST 2017 DIFF:365 Sat
	 * Dec 08 12:33:23 IST 2018 DIFF:730
	 * 
	 * Thu Dec 08 12:33:51 IST 2016 DIFF:0 Thu Dec 08 12:33:51 IST 2016
	 * DIFF:0 Wed Dec 07 12:33:51 IST 2016 DIFF:-1 Tue Dec 06 12:33:51 IST
	 * 2016 DIFF:-2 Mon Dec 05 12:33:51 IST 2016 DIFF:-3 Sun Dec 04 12:33:51
	 * IST 2016 DIFF:-4 Thu Dec 08 10:33:51 IST 2016 DIFF:0 Thu Dec 08
	 * 12:31:51 IST 2016 DIFF:0 Thu Dec 08 12:33:49 IST 2016 DIFF:0 Thu Dec
	 * 08 12:33:49 IST 2016 DIFF:0 Tue Dec 08 12:33:49 IST 2015 DIFF:-366
	 * Mon Dec 08 12:33:49 IST 2014 DIFF:-731
	 */

    }

    public ArrayList<Date> getDatesBetweenDateRange(Date startDate, Date endDate) {
	ArrayList<Date> dates = new ArrayList<Date>();
	// Date startDate;

	try {
	    // startDate = (Date) formatter.parse(str_date);
	    // Date endDate = (Date) formatter.parse(end_date);
	    long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
	    long endTime = endDate.getTime(); // create your endtime here,
	    // possibly using Calendar or Date
	    long curTime = startDate.getTime();
	    while (curTime <= endTime) {
		dates.add(new Date(curTime));
		curTime += interval;
	    }
	} catch (Exception ex) {

	}
	return dates;
    }

    public String getTimeStampString() {
	Date date = getCurrentDate();
	SimpleDateFormat format_date = new SimpleDateFormat(Shared.DF_TRANSACTION_SEQUENCE_FORMAT);
	if (date != null) {
	    return format_date.format(date);

	} else {
	    return format_date.format(new Date(System.currentTimeMillis()));
	}
    }

    public String getTodayString() {
	Date date = getCurrentDate();
	SimpleDateFormat format_date = new SimpleDateFormat(Shared.DF_BUSINESS_DATE_FORMAT);
	if (date != null) {
	    return format_date.format(date);

	} else {
	    return format_date.format(new Date(System.currentTimeMillis()));
	}
    }

    public String getTimeStampString(Date date) {
	SimpleDateFormat format_date = new SimpleDateFormat(Shared.DF_TRANSACTION_SEQUENCE_FORMAT);
	if (date != null) {
	    return format_date.format(date);

	} else {
	    return format_date.format(new Date(System.currentTimeMillis()));
	}
    }

    public String getTimeStampString(Date date, String format) {
	SimpleDateFormat format_date = new SimpleDateFormat(format);
	if (date != null) {
	    return format_date.format(date);

	} else {
	    return format_date.format(new Date(System.currentTimeMillis()));
	}
    }

    public String getTimeStampString(int offset) {
	Calendar now = Calendar.getInstance();
	now.add(Calendar.DATE, offset);
	Date date = now.getTime();
	SimpleDateFormat format_date = new SimpleDateFormat(Shared.DF_TRANSACTION_SEQUENCE_FORMAT);
	if (date != null) {
	    return format_date.format(date);

	} else {
	    return format_date.format(new Date(System.currentTimeMillis()));
	}
    }

    public Date generateExpirationDate(Date startDate, int days) {
	Date expirydate = null;
	SimpleDateFormat format = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.DATE, cal.get(Calendar.DATE) + days);
	startDate = new Date(cal.getTimeInMillis());
	try {
	    expirydate = format.parse(format.format(startDate));
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	}
	return expirydate;
    }

    private Date previosday(int previousday) {
	final Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, previousday);
	return cal.getTime();
    }

    public String getPreviosdayDateString(int previousday, String format) {
	SimpleDateFormat sdf = new SimpleDateFormat(format);
	return sdf.format(previosday(previousday));
    }

    public Date addMinutesToGivenDate(int minutes, Date date) {

	try {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.MINUTE, minutes);
	    date = cal.getTime();
	} catch (Exception e) {

	}
	return date;
    }

    public Date parseDateWithGivenForamt(String dateStr, String format) {
	try {
	    SimpleDateFormat format_date = new SimpleDateFormat(format);
	    return format_date.parse(dateStr);
	} catch (Exception e) {

	    return null;
	}

    }

    private static boolean isLeapbyYear(Date currentdate) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(currentdate);
	int year = calendar.get(Calendar.YEAR);
	return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0));
    }

    private static boolean isLeapbyYear(int year) {
	return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0));
    }

    public String getFirstDateOfMonth(Date date) {
	SimpleDateFormat format = new SimpleDateFormat(Shared.DB_TRANSACTION_DATE_FORMAT);
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
	return format.format(cal.getTime());
    }

    public static String getLastYearLastMonthDate(Date currentdate) throws ParseException {
	SimpleDateFormat format = new SimpleDateFormat(Shared.DB_TRANSACTION_DATE_FORMAT);

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(currentdate);
	int y = calendar.get(Calendar.YEAR);
	int m = calendar.get(Calendar.MONTH);
	int d = calendar.get(Calendar.DAY_OF_MONTH);
	Calendar c = Calendar.getInstance();
	if (y > 0 && m >= 0 && d > 0) {
	    c.set((y - 1), (m), d, 0, 0);
	}
	Date todate = c.getTime();
	// System.out.println("Previous Month Date" + format.format(todate));
	if (todate != null) {
	    return (format.format(todate));
	}
	return null;
    }

    public String getLastMonthLastDate(Date currentdate) throws ParseException {
	SimpleDateFormat format = new SimpleDateFormat(Shared.DB_TRANSACTION_DATE_FORMAT);
	int year = 0;
	int month = 0;
	int date = 0;

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(currentdate);
	int y = calendar.get(Calendar.YEAR);
	int m = calendar.get(Calendar.MONTH);
	int d = calendar.get(Calendar.DAY_OF_MONTH);

	if (m == 0) {
	    month = 12;
	    year = y - 1;
	} else {
	    year = y;
	    month = m;
	}

	// February month with leap years
	if (m == 2) {
	    if (isLeapbyYear(currentdate)) {
		if (d <= 29) {
		    date = d;
		} else {
		    date = 29;
		}
	    } else {
		if (d <= 28) {
		    date = d;
		} else {
		    date = 28;
		}
	    }
	} else if (m == 4 || m == 6 || m == 9 || m == 11) {
	    // Months of April, June,Sept and Nov
	    if (d <= 30) {
		date = d;
	    } else {
		date = 30;
	    }
	} else {
	    date = d;
	}
	Calendar c = Calendar.getInstance();
	if (year > 0 && month > 0 && date > 0) {
	    c.set(year, (month - 1), date, 0, 0);
	}
	Date todate = c.getTime();
	// System.out.println("Previous Month Date" + format.format(todate));
	if (todate != null) {
	    return (format.format(todate));
	}
	return null;
    }

    public String getLastYearThisMonthDate(Date currentdate) throws ParseException {
	SimpleDateFormat format = new SimpleDateFormat(Shared.DB_TRANSACTION_DATE_FORMAT);
	int year = 0;
	int month = 0;
	int date = 0;

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(currentdate);
	int y = calendar.get(Calendar.YEAR);
	int m = calendar.get(Calendar.MONTH);
	int d = calendar.get(Calendar.DAY_OF_MONTH);

	if (m == 0) {
	    month = m;
	    year = y - 1;
	} else {
	    year = y - 1;
	    month = m;
	}

	// February month with leap years
	if (m == 1) {
	    if (isLeapbyYear(year)) {
		if (d <= 29) {
		    date = d;
		} else {
		    date = 29;
		}
	    } else {
		if (d <= 28) {
		    date = d;
		} else {
		    date = 28;
		}
	    }
	} else if (m == 4 || m == 6 || m == 9 || m == 11) {
	    // Months of April, June,Sept and Nov
	    if (d <= 30) {
		date = d;
	    } else {
		date = 30;
	    }
	} else {
	    date = d;
	}
	Calendar c = Calendar.getInstance();
	if (year > 0 && month >= 0 && date > 0) {
	    c.set(year, (month), date, 0, 0);
	}
	Date todate = c.getTime();
	// System.out.println("Previous Month Date" + format.format(todate));
	if (todate != null) {
	    return (format.format(todate));
	}
	return null;
    }

    public ArrayList<String> getDatesBetweenDateRange(Date startDate, Date endDate, SimpleDateFormat format) {
	ArrayList<String> dates = new ArrayList<String>();
	// Date startDate;
	try {
	    // startDate = (Date) formatter.parse(str_date);
	    // Date endDate = (Date) formatter.parse(end_date);
	    long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
	    long endTime = endDate.getTime(); // create your endtime here,
	    // possibly using Calendar or Date
	    long curTime = startDate.getTime();
	    while (curTime <= endTime) {
		dates.add(format.format(new Date(curTime)));
		curTime += interval;
	    }
	} catch (Exception ex) {

	}
	return dates;
    }

}
