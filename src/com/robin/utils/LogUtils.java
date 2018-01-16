package com.robin.utils;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

public class LogUtils {
	// 1.Debug Log显示等级
	// 2.Debug Log等级
	// 3.文件名/类名/函数名/行号
	
	public static final int DEBUG = 0;
	public static final int INFO = 1;
	public static final int WARN = 2;
	public static final int ERROR = 3;
	public static final int NONE = -1;
	
	private static int level = 0;
	
	
	public static int setLevel(int level) {
		if(level <=3 && level >= -1)
		{
			LogUtils.level = level;
		}
		else
		{
			System.out.println("[log]set debug level out of range!-1~3!Input is "+level);
			System.out.println("[log]-1:show nothing");
			System.out.println("[log] 0:show debug/info/warning/error log");
			System.out.println("[log] 1:show info/warning/error log");
			System.out.println("[log] 2:show warning/error log");
			System.out.println("[log] 3:show error log");
		}
		return LogUtils.level;
	}
	
	public static int getLevel() {
		System.out.println("[log]Debug level is "+LogUtils.level);
		System.out.println("[log]-1:show nothing");
		System.out.println("[log] 0:show debug/info/warning/error log");
		System.out.println("[log] 1:show info/warning/error log");
		System.out.println("[log] 2:show warning/error log");
		System.out.println("[log] 3:show error log");
		return LogUtils.level;
	}
	
	public static void debug(String str) {
		log(0,"[Debug]",str);
	}
	public static void info(String str) {
		log(1,"[Info]",str);
	}
	public static void warning(String str) {
		log(2,"[Warning]",str);
	}
	public static void error(String str) {
		log(3,"[Error]",str);
	}
	private static void log(int level,String prefix,String str) {
		if(level >= LogUtils.level)
		{
			if(level >= LogUtils.WARN)
			{
				System.err.println(prefix+"["+fileName()+"]"+"["+ClassName()+"]"+"["+methodName()+"]"+"["+LineNum()+"]"+str);
			}
			else {
				System.out.println(prefix+"["+fileName()+"]"+"["+ClassName()+"]"+"["+methodName()+"]"+"["+LineNum()+"]"+str);
			}
		}
	}
	
	public static String fileName() {
		return Thread.currentThread().getStackTrace()[4].getFileName();
	}
	public static String methodName() {
		return Thread.currentThread().getStackTrace()[4].getMethodName();
	}
	public static String ClassName() {
		return Thread.currentThread().getStackTrace()[4].getClassName();
	}
	public static int LineNum() {
		return Thread.currentThread().getStackTrace()[4].getLineNumber();
	}
	
	

}
