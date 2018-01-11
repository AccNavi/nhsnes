package kr.go.molit.nhsnes.common;

import android.util.Log;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateTimeUtil {


	public final static String DEFUALT_DATE_FORMAT1		= "yyyyMMdd";
	public final static String DEFUALT_DATE_FORMAT2		= "yyyy.MM.dd";
	public final static String DEFUALT_DATE_FORMAT3		= "yyyy-MM-dd";
	public final static String DEFUALT_DATE_FORMAT4		= "yyyy-MM-dd HH:mm:ss";
	public final static String DEFUALT_DATE_FORMAT5		= "HHmmss";
	public final static String DEFUALT_DATE_FORMAT9		= "ddHHmm";
	public final static String DEFUALT_DATE_FORMAT6		= "HH:mm:ss";
	public final static String DEFUALT_DATE_FORMAT7		= "yyyyMMddHHmmss";
	public final static String DEFUALT_DATE_FORMAT8		= "MMM dd yyyy";
	public final static String DEFUALT_DATE_FORMAT10		= "HHmm";
	public final static String DEFUALT_DATE_FORMAT11		= "yyyyMMddHHmm";
	public final static String DATE_FORMAT_PICTURE		= "MM월dd일";
	public final static String DATE_FORMAT_PICTURE_ITEM	= "MM/dd HH:mm";
	public final static String DATE_FORMAT_GAME_DATE	= "HH:mm";

	public static final int TYPE_DAYS		= 24;
	public static final int TYPE_HOURS		= 60;
	public static final int TYPE_MINUTES	= 60;
	public static final int TYPE_SECONDS	= 1000;


	/**
	* 포맷에 맞게 날짜 변환
	* @author FIESTA
	* @version 1.0.0
	* @since 오후 5:29
	**/
	public static String date(String format) {

		try {

			Date date = new Date();
			SimpleDateFormat simple_date_format = new SimpleDateFormat(format);

			return simple_date_format.format(date);

		} catch (Exception e)
		{
			return "";
		}
	}


	/**
	 * 현재 날짜 및 시간을 구한다. 사용법 : DateUtil.date(포맷을 정해준다) <br>
	 * DateUtil.date("yyyyMMddhhmmss") <br>
	 * DateUtil.date("yyyy.MM.dd hh:mm:ss") <br>
	 *
	 * @param format
	 *            출력포맷 (API에서 SimpleDateFormat 참조)
	 * @return 포맷에 맞게 현재시간을 리턴
	 */
	public static String date(Date date, String format) {

		if (date == null)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(date);

		return dateString;
	}


	/**
	* 포맷에 맞게 날짜 변환
	* @author FIESTA
	* @version 1.0.0
	* @since 오후 5:29
	**/
	public static String date(String date, String format) {

		if (date == null || date.length() != 8)
			return "";

		return date.substring(0, 4) + format + date.substring(4, 6) + format
				+ date.substring(6, 8);
	}

	/**
	 * 포맷에 맞게 날짜 변환
	 * @author FIESTA
	 * @version 1.0.0
	 * @since 오후 5:29
	 **/
	public static String dateParsing(String yyyyMMdd) {

		if (yyyyMMdd == null || yyyyMMdd.length() != 8)
			return "";

		return yyyyMMdd.substring(4, 6) + "월" + yyyyMMdd.substring(6, 8) + "일";
	}

	public static String addDotDate(String date)
	{
		if(date == null)
		{
			return "";
		}
		if(date.length() != 8)
		{
			return date;
		}

		return date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
	}

	/**
	 * 파라미터 일수에 해당하는 이전 날짜 또는 이후 날짜 정보를 리턴하는 함수
	 *
	 * @param day
	 * @return
	 */
	public static String getTargetDay(int day) {

		return getTargetDay(day, DEFUALT_DATE_FORMAT2);
	}

	public static String getTargetDay(int day, String dateFormat) {

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, day);	// 현재 날짜에서 해당일(day) 전 or 후의 날짜 가져오기

		Date currentTime=cal.getTime();

		SimpleDateFormat formatter=new SimpleDateFormat(dateFormat);

		String timeResult	= formatter.format(currentTime);

		return timeResult;

	}

	public static Date getTargetDate(int day) {

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, day);	// 현재 날짜에서 해당일(day) 전 or 후의 날짜 가져오기

		Date currentTime=cal.getTime();

		return currentTime;

	}


	public static String getTargetDate(String yyyymmdd, int elapsedDay, String format) {

		int yyyy	= 0;
		int mm		= 0;
		int dd		= 0;

		if (yyyymmdd.length() > 8)
		{
			yyyy	= Integer.parseInt(yyyymmdd.substring(0, 4));
			mm		= Integer.parseInt(yyyymmdd.substring(5, 7));
			dd		= Integer.parseInt(yyyymmdd.substring(8, 10));

		} else if (yyyymmdd.length() == 8)
		{
			yyyy	= Integer.parseInt(yyyymmdd.substring(0, 4));
			mm		= Integer.parseInt(yyyymmdd.substring(4, 6));
			dd		= Integer.parseInt(yyyymmdd.substring(6, 8));

		} else
		{
			return "";
		}

		return getTargetDate(yyyy, mm, dd, elapsedDay, format);
	}


	public static String getTargetDate(int year, int month, int day, int elapsedDay, String format) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);

		calendar.add(Calendar.DATE, elapsedDay);			// 파라미터 날짜에서 해당일(day) 전 or 후의 날짜 가져오기

		Date currentTime	= calendar.getTime();

		SimpleDateFormat formatter=new SimpleDateFormat(format);

		String timeResult	= formatter.format(currentTime);

		return timeResult;
	}


	public static Date getTargetDate(int year, int month, int day, int elapsedDay) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);

		calendar.add(Calendar.DATE, elapsedDay);			// 파라미터 날짜에서 해당일(day) 전 or 후의 날짜 가져오기

		Date currentTime	= calendar.getTime();

		return currentTime;
	}


	public static String addDateToFormatedString(String pattern, int day) {

		Calendar calendar = Calendar.getInstance(); // 현재 날짜/시간 등의 각종 정보 얻기

		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String dateString = formatter.format(calendar.getTime());
		return dateString;
	}


	public static String addDateToFormatedString(String pattern, String date, int day) {

		Calendar calendar = Calendar.getInstance(); // 현재 날짜/시간 등의 각종 정보 얻기
		calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 2);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6, 8)));
		calendar.add(Calendar.DAY_OF_MONTH, day);

		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String dateString = formatter.format(calendar.getTime());

		return dateString;
	}


	/**
	 * yyyy년 mm월의 마지막 날을 구한다. 사용방법 : DateUtil.getLastDayString("2000","08") <br>
	 * 2000년 8월의 마지막 날인 31일이 리턴 된다.
	 *
	 * @param yyyy
	 *            년도
	 * @param mm
	 *            월
	 * @return yyyy년 mm월의 마지막 날
	 */
	public static String getLastDayString(String yyyy, String mm) {

		if (yyyy == null || yyyy.length() < 4 || mm == null || "".equals(mm))
		{
			return "";

		} else
		{
			return String.valueOf(getLastDay(Integer.parseInt(yyyy), Integer.parseInt(mm)));
		}
	}


	/**
	 * yyyy년 mm월의 마지막 날을 구한다. 사용방법 : DateUtil.getLastDay("2000","08") <br>
	 * 2000년 8월의 마지막 날인 31일이 리턴 된다.
	 *
	 * @param yyyy
	 *            년도
	 * @param mm
	 *            월
	 * @return yyyy년 mm월의 마지막 날
	 */
	public static int getLastDay(String yyyy, String mm) {

		if (yyyy == null || yyyy.length() < 4 || mm == null || "".equals(mm))
		{
			return 0;

		} else
		{
			return getLastDay(Integer.parseInt(yyyy), Integer.parseInt(mm));
		}
	}


	/**
	 * yyyy년 mm월의 마지막 날을 구한다. 사용방법 : DateUtil.getLastDay("2000","08") <br>
	 * 2000년 8월의 마지막 날인 31일이 리턴 된다.
	 *
	 * @param yyyy
	 *            년도
	 * @param mm
	 *            월
	 * @return yyyy년 mm월의 마지막 날
	 */
	public static int getLastDay(int yyyy, int mm) {

		Calendar Cal = Calendar.getInstance();
		Cal.set(yyyy, mm - 1, 1);
		int Max = Cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return Max;
	}


	/**
	 * yyyy년 mm월의 마지막 날을 구한다. 사용방법 : DateUtil.getLastDay("2000","08") <br>
	 * 2000년 8월의 마지막 날인 31일이 리턴 된다.
	 *
	 * @param yyyy
	 *            년도
	 * @param mm
	 *            월
	 * @return yyyy년 mm월의 마지막 날
	 */
	public static String getLastDayString(int yyyy, int mm) {

		return String.valueOf(getLastDay(yyyy, mm));
	}


	public static String toDateTime(String strDate) {

		SimpleDateFormat formatter_one = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
		SimpleDateFormat formatter_two = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		ParsePosition pos	= new ParsePosition(0);
		Date frmTime		= formatter_one.parse(strDate, pos);

		return formatter_two.format(frmTime);
	}


	public static String getWeekString(String yyyymmdd) {

		int yyyy	= 0;
		int mm		= 0;
		int dd		= 0;

		if (yyyymmdd.length() > 8)
		{
			yyyy	= Integer.parseInt(yyyymmdd.substring(0, 4));
			mm		= Integer.parseInt(yyyymmdd.substring(5, 7));
			dd		= Integer.parseInt(yyyymmdd.substring(8, 10));

		} else if (yyyymmdd.length() == 8)
		{
			yyyy	= Integer.parseInt(yyyymmdd.substring(0, 4));
			mm		= Integer.parseInt(yyyymmdd.substring(4, 6));
			dd		= Integer.parseInt(yyyymmdd.substring(6, 8));

		} else
		{
			return "";
		}

		return getWeekString(yyyy, mm, dd);
	}


	public static String getWeekString(int year, int month, int day) {

		String week = "";

		int i = getWeek(year, month, day);

		if (i == 1)
		{
			week = "(일)";

		} else if (i == 2)
		{
			week = "(월)";

		} else if (i == 3)
		{
			week = "(화)";

		} else if (i == 4)
		{
			week = "(수)";

		} else if (i == 5)
		{
			week = "(목)";

		} else if (i == 6)
		{
			week = "(금)";

		} else if (i == 7)
		{
			week = "(토)";
		}

		return week;
	}


	public static String getWeekString(String year, String month, String day) {

		return getWeekString(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
	}


	public static String getFirstWeekString(int year, int month) {

		return getWeekString(year, month, 1);
	}


	public static String getFirstWeekString(String year, String month) {

		return getWeekString(Integer.parseInt(year), Integer.parseInt(month), 1);
	}


	public static int getWeek(String yyyymmdd) {

		int yyyy	= 0;
		int mm		= 0;
		int dd		= 0;

		if (yyyymmdd.length() > 8)
		{
			yyyy	= Integer.parseInt(yyyymmdd.substring(0, 4));
			mm		= Integer.parseInt(yyyymmdd.substring(5, 7));
			dd		= Integer.parseInt(yyyymmdd.substring(8, 10));

		} else if (yyyymmdd.length() == 8)
		{
			yyyy	= Integer.parseInt(yyyymmdd.substring(0, 4));
			mm		= Integer.parseInt(yyyymmdd.substring(4, 6));
			dd		= Integer.parseInt(yyyymmdd.substring(6, 8));

		} else
		{
			return 0;
		}

		return getWeek(yyyy, mm, dd);
	}


	public static int getWeek(int year, int month, int day) {

		Calendar calendar = Calendar.getInstance(); // 현재 날킈/시간 등의 각종 정보 얻기
		calendar.set(year, month - 1, day);

		return calendar.get(Calendar.DAY_OF_WEEK);
	}


	public static int getWeek(String year, String month, String day) {

		return getWeek(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
	}


	public static int getFirstWeek(int year, int month) {

		return getWeek(year, month, 1);
	}


	public static int getFirstWeek(String year, String month) {

		return getWeek(Integer.parseInt(year), Integer.parseInt(month), 1);
	}


	public static int getMonthWeek(int year, int month, int day) {

		Calendar calendar = Calendar.getInstance(); // 현재 날짜/시간 등의 각종 정보 얻기
		calendar.set(year, month - 1, day);

		return calendar.get(Calendar.WEEK_OF_MONTH);
	}


	public static int getMonthLastWeek(int year, int month) {

		return getMonthWeek(year, month, getLastDay(year, month));
	}


	public static int getMonthLastWeek(String yyyy, String mm) {

		if (yyyy == null || yyyy.length() < 4 || mm == null || "".equals(mm))
		{
			return 0;

		} else
		{
			return getMonthLastWeek(Integer.parseInt(yyyy), Integer.parseInt(mm));
		}
	}


	public static Date toDate() {

		return toDate(date(DEFUALT_DATE_FORMAT1), DEFUALT_DATE_FORMAT1);
	}


	public static Date toDate(String date) {

		return toDate(date, DEFUALT_DATE_FORMAT1);
	}


	public static Date toDate(String dateTime, String format) {

		Date date = null;

		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(dateTime);

		} catch (Exception e)
		{

		}

		return date;
	}


	/**
	 * 조회시작일, 조회종료일을 가지고 화면에 필요한 문자열로 합성하여 리턴하는 함수
	 *
	 * @param startDt
	 *            조회시작일 (yyyymmdd)
	 * @param endDt
	 *            조회종료일 (yyyymmdd)
	 * @return
	 */
	public static String getBetweenPeriod(String startDt, String endDt) {

		String result = "";

		if (startDt == null || endDt == null || "".equals(startDt) || "".equals(endDt))
			return result;

		String sYear = startDt.substring(0, 4);

		String sMonth = startDt.substring(4, 6);
		if (sMonth.startsWith("0"))
			sMonth = sMonth.replaceAll("0", "");

		String sDay = startDt.substring(6, 8);
		if (sDay.startsWith("0"))
			sDay = sDay.replaceAll("0", "");

		String eMonth = endDt.substring(4, 6);
		if (eMonth.startsWith("0"))
			eMonth = eMonth.replaceAll("0", "");

		String eDay = endDt.substring(6, 8);
		if (eDay.startsWith("0"))
			eDay = eDay.replaceAll("0", "");

		result = sYear + "년 " + sMonth + "월" + sDay + "일" + "~ " + eMonth + "월"
				+ eDay + "일";

		return result;

	}


	/**
	 * 문자열 날짜를 년,월,일 String[] 배열로 치환하여 리턴
	 *
	 * @param date	날짜
	 * @param delim	구분자
	 * @return
	 */
	public static String[] split(String date, String delim) {

		if(null == date || null == delim || date.length()<10)
			return null;

		String[] result = new String[3];

		String sYear	= date.substring(0, 4);
		String sMonth	= date.substring(5, 7);
		String sDay		= date.substring(8, 10);

		result[0]	= sYear;
		result[1]	= sMonth;
		result[2]	= sDay;

		return result;

	}


	public static long diffOfDate(Date begin, Date end) {

		return diffOfDate(begin, end, DEFUALT_DATE_FORMAT1);
	}


	public static long diffOfDate(Date begin, Date end, String format) {

		return diffOfDate(date(begin, format), date(end, format));
	}


	public static long diffOfDate(String begin, String end) {

		return diffOfDate(begin, end, DEFUALT_DATE_FORMAT1);
	}


	public static long diffOfDate(String begin, String end, String format) {

		SimpleDateFormat formatter = new SimpleDateFormat(format);

		long diffDays = 0;

		try {

			Date beginDate	= formatter.parse(begin);
			Date endDate	= formatter.parse(end);

			long diff	= endDate.getTime() - beginDate.getTime();
			diffDays	= diff / (24 * 60 * 60 * 1000);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return diffDays;
	}


	public static boolean isDateValidate(String date) {

		boolean isCheck = false;

		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFUALT_DATE_FORMAT1);
			simpleDateFormat.setLenient(false);
			simpleDateFormat.parse(date);
			isCheck = true;

		} catch (ParseException e)
		{
			isCheck = false;

		} catch (IllegalArgumentException e)
		{
			isCheck = false;

		} catch (Exception e)
		{
			isCheck = false;
		}

		return isCheck;
	}


	/**
	 * 조회 시작일자, 종료일자 파라미터 값을 비교 체크 하는 함수
	 *
	 * @param startDate
	 * @param endDate
	 * @param day		(0:시작일자와 종료일자 올바른지 비교, 1~N:입력된 일수의 기간을 벗어나는지 비교) 
	 * @param format	입력된 날짜 포맷 정보
	 * @return
	 */
	public static boolean checkPeriodDate(String startDate, String endDate, int day, String format) {

		boolean isValidateDate	= false;

		int days				= 0;	// 경과시간 (일)

		//String targetDate		= "2011.05.10 18:33";

		Calendar startCal	= null;
		Calendar endCal		= null;

		try {

			startCal	= toCalendar(startDate, format);
			endCal		= toCalendar(endDate, format);

		} catch (ParseException e) {

			e.printStackTrace();
		}

		long start_long	= startCal.getTimeInMillis();
		long end_long	= endCal.getTimeInMillis();


		// 조회기간 구하는 부분
		long elapsed_time	= end_long - start_long;

		days	= ( new Long((((elapsed_time/TYPE_SECONDS)/TYPE_MINUTES)/TYPE_HOURS)/TYPE_DAYS) ).intValue();

		// 시작일자 종료일자가 올바른지만 비교
		if(day==0)
		{
			// 음수 값이므로 False
			if(days<0)
				isValidateDate	= false;
			else
				isValidateDate	= true;

			// 입력받은 일자 범위에 있는지 비교
		}else
		{
			// 음수 값이므로 False
			if(days<0)
				return false;

			// 범위 안에 있으므로 True (day:제한범위값, days:입력범위값)
			if(days<=day)
				isValidateDate	= true;
			else
				isValidateDate	= false;
		}

		return isValidateDate;
	}


	/**
	 * 파라미터 일자와 현재일자를 비교하여 경과시간을 리턴하는 함수
	 *
	 * @param targetDate
	 * @param format
	 * @return
	 */
	public static String getElapsedTime(String targetDate, String format) {

		String resultTimeStr	= "";

		int seconds					= 0;	// 경과시간 (초)
		int minutes					= 0;	// 경과시간 (분)
		int hours					= 0;	// 경과시간 (시간)
		int days					= 0;	// 경과시간 (일)

		//String targetDate		= "2011.05.10 18:33";

		// 경과시간을 구할 대상 시간에 대한 설정
		Calendar targetCal	= null;

		try {

			targetCal = toCalendar(targetDate, format);

		} catch (ParseException e) {

			Log.e("DateUtil", "ParseException : " + e.getMessage());
			return targetDate;
		}

		long target_long	= targetCal.getTimeInMillis();


		// 현재 시간에 대한 설정
		long current_time	= System.currentTimeMillis();
		Date curr_date		= new Date(current_time);


		// 경과시간 구하는 부분
		long elapsed_time	= current_time - target_long;

		seconds			= ( new Long(elapsed_time/TYPE_SECONDS) ).intValue();
		minutes			= ( new Long((elapsed_time/TYPE_SECONDS)/TYPE_MINUTES) ).intValue();
		hours			= ( new Long(((elapsed_time/TYPE_SECONDS)/TYPE_MINUTES)/TYPE_HOURS) ).intValue();
		days			= ( new Long((((elapsed_time/TYPE_SECONDS)/TYPE_MINUTES)/TYPE_HOURS)/TYPE_DAYS) ).intValue();

		if(days>28)
		{
			resultTimeStr	= targetDate;

			return resultTimeStr;

		}else if(days>=1 && days<29)
		{
			resultTimeStr	= days + "일전";

			return resultTimeStr;

		}else if(hours>=1 && hours<24)
		{
			resultTimeStr	= hours + "시간전";

			return resultTimeStr;

		}else if(minutes>=1 && minutes<60)
		{
			resultTimeStr	= minutes + "분전";

			return resultTimeStr;

		}else if(seconds>=1 && seconds<60)
		{
			resultTimeStr	= seconds + "초전";

			return resultTimeStr;


			// 서버시간이 미래시간으로 넘어올 경우 (Seconds 가 마이너스로 넘어올 경우)
		}else if(seconds<0)
		{
			resultTimeStr	= "1초전";

			return resultTimeStr;


		}else
		{
			resultTimeStr	= targetDate;
		}

		return resultTimeStr;

	}

	/**
	 * format 형태의 문자열 날짜 정보를 Calendar로 변환
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Calendar toCalendar(String dateStr, String format) throws ParseException {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.parse(dateStr);
		return dateFormat.getCalendar();
	}


	/**
	 * Data 객체를 주어진 형식의 String으로 변환
	 * @param date
	 * @param format
	 * @return format 형태의 문자열
	 */
	public static String toString(Date date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String dateTime = dateFormat.format(date);
		return dateTime;
	}

}