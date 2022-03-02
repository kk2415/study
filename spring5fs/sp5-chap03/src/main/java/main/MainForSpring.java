package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.AppCtx;

public class MainForSpring {
	private static ApplicationContext ctx;
	
	public static void main(String[] agrs) {
		ctx = new AnnotationConfigApplicationContext(AppCtx.class);
		
		
	}
}
