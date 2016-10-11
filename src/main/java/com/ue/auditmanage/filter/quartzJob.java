package com.ue.auditmanage.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import util.cataUtil;

import com.ue.auditmanage.controller.service.AppServiceInter;
import com.ue.auditmanage.controller.service.AppTypeServiceInter;
import com.ue.auditmanage.controller.service.DataTypeInter;
import com.ue.auditmanage.controller.service.ServiceTypeInter;
import com.ue.auditmanage.controller.service.UserTypeServiceInter;
import com.ue.auditmanage.controller.service.YDDataInter;
import com.ue.auditmanage.controller.service.YDServiceInter;
import com.ue.auditmanage.controller.service.YDUserInter;



public class quartzJob implements Job {

	/**
	 * 转义正则特殊字符 （$()*+.[]?\^{},|）
	 * 
	 * @param keyword
	 * @return
	 */
	public static String escapeExprSpecialWord(String keyword) {

		String[] fbsArr = { "&quot;", "&amp;amp;", "&lt;", "&gt;", "&amp;",
				"&nbsp;" };
		for (String key : fbsArr) {
			if (keyword.contains(key)) {
				switch (key) {
				case "&quot;":
					keyword = keyword.replace(key, "\"");

					break;
				case "&amp;amp;":
					keyword = keyword.replace(key, "&");

					break;

				case "&lt;":
					keyword = keyword.replace(key, "<");

					break;
				case "&gt;":
					keyword = keyword.replace(key, ">");

					break;
				case "&amp;":
					keyword = keyword.replace(key, "&");

					break;
				case "&nbsp;":
					keyword = keyword.replace(key, "");

					break;
				default:
					break;
				}

			}
		}

		return keyword;
	}

	/**
	 * 定时触发
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 * @throws SchedulerException
	 */
	public void work() throws UnsupportedEncodingException, ParseException,
			SchedulerException {
		System.out.println("现在开始触发......");

	}

	// public static util.Log log;

	public static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");

	public static void main(String[] args) {

		String message = "DN:[*base64 decode of certificate failed*];URL:[http://geodata.cegn.cn:80/hitTimer?t=1471592283703]";

		String url = getUrlFromLogs(message);
		

	}

	/**
	 * 从日志中获取url
	 * 
	 * @param message
	 * @return
	 */
	private static String getUrlFromLogs(String message) {
		String temp = "";
		String url = "";
		if (message.contains("https")) {
			temp = message.substring(message.indexOf("https:") + 6);
		} else {
			temp = message.substring(message.indexOf("http:") + 6);
		}

		try {
			url = URLDecoder.decode(temp, "gb2312");
			if (url.endsWith("]")) {
				url = url.substring(0, url.indexOf("]"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return url;
	}

	/**
	 * 解析DP日志
	 * 
	 * @param fu
	 * @param sdf
	 * @param ftpservice
	 * @param ff
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	public void parseDPLog(SchedulerContext skedCtx) throws ParseException {
		UserTypeServiceInter userTypeServiceInter = (UserTypeServiceInter) skedCtx
				.get("userTypeServiceInter");
		YDUserInter ydvisiterInter = (YDUserInter) skedCtx
				.get("ydvisiterInter");
		YDDataInter ydDataInter= (YDDataInter) skedCtx.get("yddataInter");
		DataTypeInter dataTypeInter = (DataTypeInter) skedCtx.get("dataTypeInter");
		AppServiceInter appServiceInter =(AppServiceInter) skedCtx.get("appServiceInter");
		AppTypeServiceInter  appTypeServiceInter = (AppTypeServiceInter) skedCtx.get("appTypeServiceInter");
		try {
			cataUtil.catalogSynchro(ydDataInter, dataTypeInter,
					ydvisiterInter, userTypeServiceInter);//同步元数据服务器里的数据
			System.out.println("数据同步结束");
			cataUtil.appcatalogSynchro(appServiceInter, appTypeServiceInter, ydvisiterInter, userTypeServiceInter);
			System.out.println("应用同步结束");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("开始测试");
		try {
			SchedulerContext skedCtx = context.getScheduler().getContext();

			try {
				parseDPLog(skedCtx);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
