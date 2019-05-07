package com.alex.wassgar.server;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.alex.wassgar.misc.ManageUserFile;
import com.alex.wassgar.misc.User;
import com.alex.wassgar.server.Request.requestType;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.xMLGear;

/**
 * Class used to watch for socket status
 * 
 * @author Alexandre
 */
public class ClientListener extends Thread
	{
	/**
	 * Variables
	 */
	private boolean run = true;
	private User u;
	
	public ClientListener(User u)
		{
		super();
		this.u = u;
		start();
		}
	
	public void run()
		{
		Variables.getLogger().debug("Client listener started for user "+u.getInfo()+" !");
		while(run)
			{
			try
				{
				if(u.getConnection() != null)
					{
					Object o = u.getConnection().getIn().readObject();
					if(o instanceof Request)
						{
						Request reply = (Request)o;
						
						if(reply.getType().equals(requestType.updateOption))
							{
							Variables.getLogger().debug(u.getInfo()+" "+u.getExtension()+" update user request received : \r\n"+reply.getContent());
							
							ArrayList<String> listParams = new ArrayList<String>();
							listParams.add("options");
							ArrayList<String[][]> parsed = xMLGear.getResultListTab(reply.getContent(), listParams);
							
							for(String[] tabs : parsed.get(0))
								{
								if(tabs[0].equals("incomingcallpopup"))u.setIncomingCallPopup(Boolean.parseBoolean(tabs[1]));
								else if(tabs[0].equals("reverselookup"))u.setReverseLookup(Boolean.parseBoolean(tabs[1]));
								else if(tabs[0].equals("emailreminder"))u.setEmailReminder(Boolean.parseBoolean(tabs[1]));
								}
							
							ManageUserFile.updateUser(u.getID(),
									null,
									null,
									null,
									u.isIncomingCallPopup(),
									u.isReverseLookup(),
									u.isEmailReminder());
							}
						else if(reply.getType().equals(requestType.status))
							{
							Variables.getLogger().debug(u.getInfo()+" "+u.getExtension()+" status request received");
							}
						else
							{
							Variables.getLogger().debug("Warning : "+u.getInfo()+" "+u.getExtension()+" received request type is not managed : "+reply.getType().name());
							}
						}
					else
						{
						throw new Exception("Unkown request received");
						}
					}
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ClientListener : Error while receiving a request : "+e.getMessage(),e);
				Variables.getLogger().debug("ClientListener : "+u.getInfo()+" "+u.getExtension()+" closing socket");
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
				break;//we exit the client listener
				}
			}
		Variables.getLogger().debug("Client listener stopped for user "+u.getInfo()+" !");
		}
	
	/**
	 * To stop it
	 */
	public void tchao()
		{
		run = false;
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/