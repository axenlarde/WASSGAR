package com.alex.wassgar.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.alex.wassgar.misc.User;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.requestType;

/**
 * Class used to manager request to clients
 */
public class ManageRequest
	{
	
	
	/**
	 * Used to send request to the a client
	 */
	public static synchronized void send(User user, Request r)
		{
		try
			{
			if(user.getSocket() != null && user.getSocket().isConnected())
				{
				ObjectInputStream in = new ObjectInputStream(user.getSocket().getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(user.getSocket().getOutputStream());
				
				out.writeObject((Object)r);
				out.flush();
				
				//We now wait for the response
				Object o = in.readObject();
				
				if(o instanceof Request)
					{
					Request reply = (Request)o;
					if(reply.getType().equals(requestType.success))
						{
						Variables.getLogger().debug(r.getType().name()+" success for user : "+user.getInfo());
						}
					else
						{
						Variables.getLogger().debug(r.getType().name()+" failed for user : "+user.getInfo());
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
			}
		
		}
	}

/*2019*//*RATEL Alexandre 8)*/