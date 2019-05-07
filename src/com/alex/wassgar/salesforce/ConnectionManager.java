package com.alex.wassgar.salesforce;

import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;

/**
 * Class used to manage salesforce connection
 * It guaranty to be always connected
 * 
 * @author Alexandre
 */
public class ConnectionManager extends Thread
	{
	/**
	 * Variables
	 */
	private boolean run = true;
	
	public ConnectionManager()
		{
		super();
		start();
		}

	
	public void run()
		{
		Variables.getLogger().info("Salesforce connection manager started !");
		
		while(run)
			{
			try
				{
				if(Variables.getSFConnection() == null)
					{
					Variables.getLogger().info("Salesforce connection innactive, trying to connect");
					
					SalesForceManager.connection(UsefulMethod.getTargetOption("sfusername"),
						UsefulMethod.getTargetOption("sfpassword"),
						UsefulMethod.getTargetOption("sfsecuritytoken"));
					}
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR : While managing salesforce connection : "+e.getMessage(),e);
				}
			
			try
				{
				this.sleep(Integer.parseInt(UsefulMethod.getTargetOption("retryinterval"))*1000);
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR : While sleeping : "+e.getMessage(),e);
				}
			}
		Variables.getLogger().info("Salesforce connection manager stopped !");
		}
	
	public void tchao()
		{
		this.run = false;
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/