package com.alex.wassgar.server;

import java.net.ServerSocket;
import java.net.Socket;

import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;

/****
 * Class used to manager listener
 */
public class ListenerManager extends Thread
	{
	/**
	 * Variables
	 */
	private ServerSocket ss;
	private boolean run = true;
	
	public ListenerManager()
		{
		super();
		start();
		}
	
	public void run()
		{
		try
			{
			Variables.getLogger().info("Listener manager started !");
			
			ss = new ServerSocket(Integer.parseInt(UsefulMethod.getTargetOption("listenerport")));
			
			while(run)
				{
				Variables.getLogger().debug("Listener : Waiting for a new connection");
				
				//Connection accepted
				Socket newRequest = ss.accept();
				Variables.getLogger().info("Listener : New connection attempt from : "+newRequest.getInetAddress().getHostAddress());
				
				new Listener(newRequest);
				}
			Variables.getLogger().info("Listener manager stopped !");
			}
		catch(Exception e)
			{
			Variables.getLogger().error("ERROR : While listening for new request : "+e.getMessage(),e);
			}
		}
	
	/**
	 * To stop the thread
	 */
	public void tchao()
		{
		run = false;
		}

	}

/*2019*//*RATEL Alexandre 8)*/