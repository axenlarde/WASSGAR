package com.alex.wassgar.action;

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
			System.out.println("User list size : "+Variables.getUserList().size());
			System.out.println("##"+Variables.getUserList().get(0).getFirstName());
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		
		
		/**
		 * We start the monitor thread (JTAPI)
		 */
		
		
		/**
		 * We start the SalesForce connection
		 */
		
		
		/**
		 * We now wait for events ;)
		 */
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
