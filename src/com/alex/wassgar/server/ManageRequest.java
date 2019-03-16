package com.alex.wassgar.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
	public static synchronized void send(User user, Request r)
		{
		try
			{
			if(user.getSocket() != null)
				{
				Variables.getLogger().debug("Trying to send "+r.getType().name()+" to "+user.getInfo());
				
				ObjectOutputStream out = new ObjectOutputStream(user.getSocket().getOutputStream());
				
				out.writeObject((Object)r);
				out.flush();
				
				//We now wait for the response
				ObjectInputStream in = new ObjectInputStream(user.getSocket().getInputStream());
				Object o = in.readObject();
				
				if(o instanceof Request)
					{
					Request reply = (Request)o;
					if(reply.getType().equals(requestType.success))
						{
						Variables.getLogger().debug(r.getType().name()+" sent with success to "+user.getInfo());
						}
					else
						{
						Variables.getLogger().debug(r.getType().name()+" sending failed for user "+user.getInfo());
						}
					}
				else
					{
					throw new Exception("Bad reply for user : "+user.getInfo());
					}
				}
			else
				{
				throw new Exception("Socket not found for user : "+user.getInfo());
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While sending a request : "+e.getMessage(),e);
			try
				{
				user.getSocket().close();
				user.setSocket(null);
				}
			catch (Exception exc)
				{
				Variables.getLogger().error("ERROR : While closing the socket : "+e.getMessage(),e);
				}
			}
		
		}
	}

/*2019*//*RATEL Alexandre 8)*/