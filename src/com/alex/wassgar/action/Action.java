package com.alex.wassgar.action;

import com.alex.wassgar.jtapi.Monitor;
import com.alex.wassgar.salesforce.SFObject;
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
			SalesForceManager.connection(UsefulMethod.getTargetOption("sfusername"),
					UsefulMethod.getTargetOption("sfpassword"),
					UsefulMethod.getTargetOption("sfsecuritytoken"));
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
			/*new Monitor(UsefulMethod.getTargetOption("ctihost"),
					UsefulMethod.getTargetOption("ctidelay"),
					UsefulMethod.getTargetOption("ctiusername"),
					UsefulMethod.getTargetOption("ctipassword"));*/
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR setting up the monitor thread : "+e.getMessage(), e);
			}
		
		/**
		 * We now wait for events ;)
		 */
		//Temp
		try
			{
			SFObject sfo = SalesForceManager.lookForExtension("0051i0000011rvcAAA", "0712983467");
			Variables.getLogger().debug("Found : "+sfo.getType()+" - "+sfo.getID()+" - "+sfo.getInfo());
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : "+e.getMessage(),e);
			}
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
