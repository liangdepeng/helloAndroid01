package com.example.zhengjun.helloandroid.utils;

import java.util.Date;

public class TimeDiffer {
	public static String caculatedDate(long createtime){
		String difftime="";
		Date create = new Date(createtime*1000);
		Date current = new Date(System.currentTimeMillis());
		long diff=current.getTime()-create.getTime();
		long days = diff/(1000*60*60*24);
		long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
		long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
		long seconds = diff/1000;
		long month = days/30;
		 if(month>3){
			difftime ="很久以前";
		}else if(month >1){
			difftime =month+"个月前";
		}else if(days >1){
			difftime =days +"天前";
		}else if(hours >1){
			difftime = hours+"个小时前";
		}else if(minutes>1){
			difftime=minutes + "分钟前";	
		}else if(seconds>1){
			difftime =seconds+"秒前";
			}
		return difftime;
	}
}
