package com.alex.wassgar.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.alex.wassgar.misc.User;
import com.alex.wassgar.server.Request.requestType;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;

/**
 * Class used to watch for socket status
 * 
 * @author Alexandre
 */
public class Watchman extends Thread
	{
	/**
	 * Variables
	 */
	private boolean run = true;
	
	public Watchman()
		{
		super();
		
		start();
		}
	
	public void run()
		{
		Variables.getLogger().debug("Watchman started !");
		while(run)
			{
			try
				{
				for(User u : Variables.getUserList())
					{
					try
						{
						if(u.getConnection() != null)
							{
							Request r = RequestBuilder.buildStatus();
							u.getConnection().getOut().writeObject(r);
							u.getConnection().getOut().flush();
							
							//If the writing succeed, the connection is still up, otherwise an exception would have been raised
							Variables.getLogger().debug("Watchman : "+u.getInfo()+" "+u.getExtension()+" client is UP");
							}
						}
					catch (Exception e)
						{
						Variables.getLogger().error("Watchman : Error while sending a request : "+e.getMessage(),e);
						Variables.getLogger().debug("Watchman : "+u.getInfo()+" "+u.getExtension()+" closing socket");
						try
							{
							u.getConnection().close();
							u.setConnection(null);
							}
						catch (Exception exc)
							{
							Variables.getLogger().error("Watchman : Unable to close socket : "+e.getMessage(),e);
							u.setConnection(null);
							}
						}
					}
				
				this.sleep(Integer.parseInt(UsefulMethod.getTargetOption("retryinterval"))*1000);
				}
			catch (Exception e)
				{
				Variables.getLogger().error("Watchman : While checking socket status : "+e.getMessage(),e);
				}
			}
		Variables.getLogger().debug("Watchman stopped !");
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/