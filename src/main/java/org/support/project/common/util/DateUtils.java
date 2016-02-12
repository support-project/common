package org.support.project.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;


/**
 * 日付操作のユーティリティクラス
 * 
 * @author Koda
 * 
 */
public class DateUtils {
	/** 通信で利用する日付のフォーマット文字列 */
	public static final String TRANSFER_DATETIME_FORMAT = "yyyyMMddHHmmssSSS";
	/** 通信で利用する日付のフォーマット */
	public static final DateFormat TRANSFER_DATETIME = new SimpleDateFormat(TRANSFER_DATETIME_FORMAT);
	/** DateFormat */
	public static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG);

	/** 日付のみを表示するフォーマット(ロケールで切り替えない際に利用) */
	public static final DateFormat DAY_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
	
	public static final DateFormat SHORT_DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");

	/** 通信で利用する日付のフォーマット文字列 */
	public static final String SECOND_FORMAT_STR = "yyyyMMddHHmmss";
	/** 通信で利用する日付のフォーマット */
	public static final DateFormat SECOND_FORMAT = new SimpleDateFormat(SECOND_FORMAT_STR);
	
	/** ログ */
	private static Log LOG = LogFactory.getLog(DateUtils.class);

	/**
	 * 通信に使用する日付フォーマットによる日付文字列の取得
	 * 
	 * @param d
	 * @return
	 */
	public static String formatTransferDateTime(Date d) {
		return TRANSFER_DATETIME.format(d);
	}

	/**
	 * 通信に使用する日付フォーマットによる日付文字列の取得
	 * 
	 * @param d
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTransferDateTime(String d) throws ParseException {
		return TRANSFER_DATETIME.parse(d);
	}

	/**
	 * 2つの日付の差を求めます。 日付文字列 strDate1 - strDate2 が何日かを返します。
	 * 
	 * @param strDate1
	 *            日付文字列 yyyy/MM/dd
	 * @param strDate2
	 *            日付文字列 yyyy/MM/dd
	 * @return 2つの日付の差
	 * @throws ParseException
	 *             日付フォーマットが不正な場合
	 */
	public static int differenceDays(String strDate1, String strDate2) throws ParseException {
		Date date1 = DateFormat.getDateInstance().parse(strDate1);
		Date date2 = DateFormat.getDateInstance().parse(strDate2);
		return differenceDays(date1, date2);
	}

	/**
	 * 2つの日付の差を求めます。 java.util.Date 型の日付 date1 - date2 が何日かを返します。
	 * 
	 * 計算方法は以下となります。 1.最初に2つの日付を long 値に変換します。 　※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの 　経過ミリ秒数となります。 2.次にその差を求めます。 3.上記の計算で出た数量を 1 日の時間で割ることで
	 * 　日付の差を求めることができます。 　※1 日 ( 24 時間) は、86,400,000 ミリ秒です。
	 * 
	 * @param date1
	 *            日付 java.util.Date
	 * @param date2
	 *            日付 java.util.Date
	 * @return 2つの日付の差
	 */
	public static int differenceDays(Date date1, Date date2) {
		if (LOG.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("********* 日付の差分計算のインプット ********");
			builder.append("\n\t入力１ : " + TRANSFER_DATETIME.format(date1));
			builder.append("\n\t入力２ : " + TRANSFER_DATETIME.format(date2));
			LOG.debug(builder.toString());
		}
		
		date1 = getRoundsDate(date1);
		date2 = getRoundsDate(date2);
		
		long datetime1 = date1.getTime();
		long datetime2 = date2.getTime();
		
		long one_date_time = 1000 * 60 * 60 * 24;
		long diff = datetime1 - datetime2;
		long diffDays = diff / one_date_time;
		
		if (LOG.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("********* 日付の差分計算 ********");
			builder.append("\n\t計算値１ : " + TRANSFER_DATETIME.format(date1));
			builder.append("\n\t計算値２ : " + TRANSFER_DATETIME.format(date2));
			builder.append("\n\t差分   : " + diff);
			builder.append("\n\t一日のミリ秒   : " + one_date_time);
			builder.append("\n\t日付の差分   : " + diffDays);
			LOG.debug(builder.toString());
		}
		
		return (int) diffDays;
	}
	
	/**
	 * Dateは、日付(yyyy/MM/dd)以外に、時刻(hh:mm:ss.SSS)を持っている。
	 * 日付の情報のみを使いたい場合、この時刻の情報が邪魔になることがある。
	 * そこで、時刻の情報を固定値をセットしたDateを取得する。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getRoundsDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(
				calendar.get(Calendar.YEAR), 
				calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DATE),
				12, 0, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
//		calendar1.set(Calendar.SECOND, 0);
//		calendar1.set(Calendar.MINUTE, 0);
//		calendar1.set(Calendar.HOUR, 12); 
//		long datetime1 = calendar1.getTimeInMillis();
		
		if (LOG.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("********* 日付の丸めの処理 ********");
			builder.append("\n\t入力 : " + TRANSFER_DATETIME.format(date));
			builder.append("\n\t出力 : " + TRANSFER_DATETIME.format(calendar1.getTime()));
			LOG.debug(builder.toString());
		}
		
		return calendar1.getTime();
	}

}
