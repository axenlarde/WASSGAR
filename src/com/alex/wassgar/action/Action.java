package com.alex.wassgar.action;

import com.alex.wassgar.jtapi.Monitor;
import com.alex.wassgar.salesforce.SalesForceManager;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;

/**
 * Class used to launch the main jobs
 * 
 * @author Alexandre
 */
public class Action
	{
	/**
	 * Variables
	 */
	
	public Action()
		{
		/**
		 * We read the current user file
		 */
		try
			{
			Variables.getLogger().info("User list size : "+Variables.getUserList().size());//This trigger the file reading
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while reading the user list : "+e.getMessage(), e);
			}
		
		/**
		 * We start the SalesForce connection
		 */
		try
			{
			SalesForceManager.newConnection();
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR setting up the salesforce thread : "+e.getMessage(), e);
			}
		
		/**
		 * We start the monitor thread
		 */
		try
			{
			/***
			 * Maybe rewrite the following as a static "MonitoringManager"
			 */
			new Monitor(UsefulMethod.getTargetOption("ctihost"),
					UsefulMethod.getTargetOption("ctidelay"),
					UsefulMethod.getTargetOption("ctiusername"),
					UsefulMethod.getTargetOption("ctipassword"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR setting up the monitor thread : "+e.getMessage(), e);
			}
		
		/**
		 * We now wait for events ;)
		 */
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
