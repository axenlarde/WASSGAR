package com.alex.wassgar.server;

import com.alex.wassgar.misc.User;
import com.alex.wassgar.server.Request.requestType;
import com.alex.wassgar.utils.Variables;

/**
 * Class used to manager request to clients
 */
public class ManageRequest
	{
	
	
	/**
	 * Used to send request to a client
	 */
	public static synchronized void send(User u, Request r)
		{
		try
			{
			if(u.getConnection() != null)
				{
				Variables.getLogger().debug("Sending "+r.getType().name()+" to "+u.getInfo()+" : "+r.getContent());
				
				u.getConnection().getOut().writeObject(r);
				u.getConnection().getOut().flush();
				
				Variables.getLogger().debug("Popup Sent for "+u.getInfo());
				}
			else
				{
				throw new Exception("Socket not found for user : "+u.getInfo());
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While sending a request : "+e.getMessage(),e);
			try
				{
				u.getConnection().close();
				u.setConnection(null);
				}
			catch (Exception exc)
				{
				Variables.getLogger().error("ERROR : While closing the socket : "+e.getMessage(),e);
				u.setConnection(null);
				}
			}
		
		}
	}

/*2019*//*RATEL Alexandre 8)*/