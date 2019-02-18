package com.alex.wassgar.jtapi;


import javax.telephony.*;
import javax.telephony.events.*;

import com.alex.wassgar.utils.Variables;
import com.cisco.cti.util.Condition;

/**
 * Class used to monitor phones using JTAPI
 * 
 * @author Alexandre
 */
public class Monitor implements ProviderObserver
	{
	/**
	 * Variables
	 */
	private String login, delay, password, server, providerString;
	private Provider provider;
	private Condition conditionInService = new Condition ();
	
	public Monitor(String server, String delay, String login,  String password)
		{
		super();
		this.login = login;
		this.delay = delay;
		this.password = password;
		this.server = server;
		
		getProvider();
		}


	private void getProvider()
		{
		Variables.getLogger().debug("JTAPI init");
		
		try
			{
			JtapiPeer peer = JtapiPeerFactory.getJtapiPeer ( null );
			Variables.getLogger().debug("JTAPI : Got peer "+peer);
			
			providerString = server + ";login=" + login + ";passwd=" + password;
			Variables.getLogger().debug("JTAPI : providerString : "+providerString);
			//Provider provider = peer.getProvider("cti-server;login = username;passwd = pass");
			
			provider = peer.getProvider(providerString);
			Variables.getLogger().debug("JTAPI : Got provider "+provider);
			
			provider.addObserver (this);
			conditionInService.waitTrue();
			Variables.getLogger().debug("JTAPI : Now In service !");
			
			//Addresses
			Variables.getLogger().debug("JTAPI : We now get the addresses");
			/**
			 * We prefer to use addresses (lines) but it is possible
			 * to use phones instead
			 */
			Address[] addresses = provider.getAddresses();
			
			if(addresses != null)
				{
				Variables.getLogger().debug("JTAPI : Found "+addresses.length+" addresses");
			
				for(int i = 0; i< addresses.length; i++)
					{
					Variables.getLogger().debug("JTAPI : Found the following addresses : "+addresses[i]);
					
					/**
					 * We now add observers to the desired lines to see call events
					 */
					new Observer(addresses[i]);
					}
				
				
				
				}
			else
				{
				Variables.getLogger().debug("JTAPI : no address found");
				}
			
			//provider.shutdown();
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : JTAPI init Failed : "+e.getMessage(),e);
			}
		}


	@Override
	public void providerChangedEvent(ProvEv[] eventList)
		{
		if (eventList != null)
			{
			for (int i = 0; i < eventList.length; i++)
				{
				if (eventList[i] instanceof ProvInServiceEv)
					{
					conditionInService.set ();
					}
				else if (eventList[i] instanceof ProvShutdownEv)
					{
					Variables.getLogger().debug("JTAPI : The provider has been shut down successfully");
					}
				}
			}
		}
	
	
	}

/*2019*//*RATEL Alexandre 8)*/