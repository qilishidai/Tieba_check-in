package tb;
import tb.AutoSign;

public class main {
	public static void main(String[] args) {

		AutoSign 签到 = new AutoSign();
		//控制台传入参数，args介绍如下
		//第一个cookie，第二个为SendKey。
		//第一个为cookie ，String 企业ID，企业应用secret,企业应用的id		
		
		
		签到.mainHandler(args);

	
	}
}
