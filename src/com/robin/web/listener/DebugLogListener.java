package com.robin.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.robin.utils.LogUtils;

/**
 * Application Lifecycle Listener implementation class DebugLogListener
 *
 */
public class DebugLogListener implements ServletContextListener {


    public void contextInitialized(ServletContextEvent arg0)  { 
    	LogUtils.setLevel(LogUtils.INFO);
    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }
	
}
