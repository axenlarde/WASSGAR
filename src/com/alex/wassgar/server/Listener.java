package com.alex.wassgar.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.alex.wassgar.misc.User;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.requestType;

/**
 * Class used to listen for new client request
 */
public class Listener extends Thread
	{
	/**
	 * Variables
	 */
	private Socket myS;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public Listener(Socket socket)
		{
		super();
		this.myS = socket;
		}

	public void run()
		{
		try
			{
			//Init buffers
			in = new ObjectInputStream(myS.getInputStream());
			out = new ObjectOutputStream(myS.getOutputStream());
			String IP = myS.getInetAddress().getHostAddress();
			int port = myS.getPort();
			
			//We receive the request;
			Object o = in.readObject();
			
			//We check if the request has the correct format
			if(o instanceof Request)
				{
				Request r = (Request)in.readObject();
				
				if(r.getType().equals(requestType.connexionRequest))
					{
					String[] content = r.getContent().split(",");
					String extension = content[0];
					
					Variables.getLogger().debug("Connexion request from : "+IP+":"+port);
					Variables.getLogger().debug("Extension received : "+extension);
					
					//We add this socket to the corresponding user
					boolean found = false;
					for(User u : Variables.getUserList())
						{
						if(u.getExtension().equals(extension))
							{
							//We check for an existing socket
							if((u.getSocket() != null) && (u.getSocket().isConnected()))
								{
								Variables.getLogger().debug("An existing socket were found for user "+u.getInfo()+" so we close it first");
								u.getSocket().close();
								}
							//We pass the socket to the user for further usage
							u.setSocket(myS);
							Variables.getLogger().debug("Socket associated to user : "+u.getInfo());
							
							//We send a successful response
							Request reply = RequestBuilder.buildAcceptedConnection("You are currently connected for user "+u.getInfo());
							out.writeObject((Object) reply);
							out.flush();
							
							found = true;
							break;
							}
						}
					
					//We send the response
					if(!found)
						{
						//We send a rejected response
						Request reply = RequestBuilder.buildRejectedConnection("No valid user were found for the given data");
						out.writeObject((Object) reply);
						out.flush();
						
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
	private void dropS() throws IOException
		{
		this.myS.close();
		}
	}

/*2019*//*RATEL Alexandre 8)*/