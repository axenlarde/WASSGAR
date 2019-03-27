package com.alex.wassgar.server;

import java.io.IOException;
import java.net.Socket;

import com.alex.wassgar.misc.User;
import com.alex.wassgar.server.Request.requestType;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;

/**
 * Class used to listen for new client request
 */
public class Listener extends Thread
	{
	/**
	 * Variables
	 */
	private Socket myS;
	
	public Listener(Socket socket)
		{
		super();
		this.myS = socket;
		start();
		}

	public void run()
		{
		try
			{
			Variables.getLogger().debug("Managing the new connection");
			
			String IP = myS.getInetAddress().getHostAddress();
			int port = myS.getPort();
			Variables.getLogger().debug("Connexion request from : "+IP+":"+port);
			
			ClientConnection connection = new ClientConnection(myS);
			
			//We receive the request;
			Object o = connection.getIn().readObject();
			
			//We check if the request has the correct format
			if(o instanceof Request)
				{
				Request r = (Request)o;
				
				if(r.getType().equals(requestType.connectionRequest))
					{
					String[] content = r.getContent().split(",");
					String extension = content[0];
					
					Variables.getLogger().debug("Extension received : "+extension);
					
					//We add this socket to the corresponding user
					boolean found = false;
					for(User u : Variables.getUserList())
						{
						if(u.getExtension().equals(extension))
							{
							//We check for an existing socket
							if(u.getConnection() != null)
								{
								Variables.getLogger().debug("An existing socket were found for user "+u.getInfo()+" so we close it first");
								u.getConnection().close();
								if(u.getClientListener() != null)u.getClientListener().tchao();
								}
							//We pass the socket to the user for further usage
							u.setConnection(connection);
							Variables.getLogger().debug("Socket associated to user : "+u.getInfo());
							
							//We create a client listener for further request
							u.setClientListener(new ClientListener(u));
							
							//We send a successful response
							Request reply = RequestBuilder.buildAcceptedConnection("You are currently connected for user "+u.getInfo());
							connection.getOut().writeObject(reply);
							connection.getOut().flush();
							
							//We send the option status
							Request optionUpdate = RequestBuilder.buildOptionUpdate(UsefulMethod.encodeOptionList(u));
							connection.getOut().writeObject(optionUpdate);
							connection.getOut().flush();
							
							found = true;
							break;
							}
						}
					
					//We send the response
					if(!found)
						{
						//We send a rejected response
						Request reply = RequestBuilder.buildRejectedConnection("No valid user were found for the given data");
						connection.getOut().writeObject(reply);
						connection.getOut().flush();
						
						//We drop the socket
						Variables.getLogger().debug("No user found for extension "+extension+" dropping socket !");
						dropS();
						}
					}
				}
			else
				{
				Variables.getLogger().debug("Incorrect request from : "+IP+":"+port+" dropping socket !");
				dropS();
				}
			}
		catch(Exception e)
			{
			Variables.getLogger().error("ERROR : Client request treatment failed : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Used to drop the socket
	 * @throws IOException 
	 */
	private void dropS() throws Exception
		{
		myS.close();
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/