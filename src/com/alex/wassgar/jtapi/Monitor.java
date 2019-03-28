package com.alex.wassgar.jtapi;


import javax.telephony.*;
import javax.telephony.events.*;

import com.alex.wassgar.misc.User;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;
import com.cisco.cti.util.Condition;
import com.cisco.jtapi.CiscoAddrCreatedEvImpl;
import com.cisco.jtapi.CiscoAddrRemovedEvImpl;

/**
 * Class used to monitor phones using JTAPI
 * 
 * @author Alexandre
 */
public class Monitor extends Thread implements ProviderObserver
	{
	/**
	 * Variables
	 */
	private String login, delay, password, server, providerString;
	private Provider provider;
	private Condition conditionInService = new Condition ();
	private Address[] addresses;
	private boolean run = true;
	
	public Monitor(String server, String delay, String login,  String password)
		{
		super();
		this.login = login;
		this.delay = delay;
		this.password = password;
		this.server = server;
		
		start();
		}

	public void run()
		{
		getJTAPIProvider();//JTAPI
		}
	
	
	private void getJTAPIProvider()
		{
		Variables.getLogger().debug("JTAPI init");
		Variables.getLogger().info("JTAPI connection manager started !");
		
		while(run)
			{
			try
				{
				if(provider == null)
					{
					Variables.getLogger().info("JTAPI connection innactive, trying to connect");
					
					//We get a JTAPI Provider
					JtapiPeer peer = JtapiPeerFactory.getJtapiPeer ( null );
					Variables.getLogger().debug("JTAPI : Got peer "+peer);
					
					providerString = server + ";login=" + login + ";passwd=" + password;
					Variables.getLogger().debug("JTAPI : providerString : "+providerString);
					
					provider = peer.getProvider(providerString);
					Variables.getLogger().debug("JTAPI : Got provider "+provider);
					
					provider.addObserver(this);
					conditionInService.waitTrue();
					Variables.getLogger().debug("JTAPI : Now In service !");
					
					updateAddresseList();//We get the line a first time
					addMonitoring();
					}
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR : JTAPI init Failed : "+e.getMessage(),e);
				}
			try
				{
				this.sleep(Integer.parseInt(UsefulMethod.getTargetOption("retryinterval")));
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR : While sleeping : "+e.getMessage(),e);
				}
			}
		Variables.getLogger().info("JTAPI connection manager stopped !");
		}
	
	
	/**
	 * Method used to manage monitored line
	 */
	private void addMonitoring()
		{
		try
			{
			for(int i = 0; i<addresses.length; i++)
				{
				//We check if the address is part of the monitored users
				boolean userFound = false;
				for(User myUser : Variables.getUserList())
					{
					if(myUser.getExtension().equals(addresses[i].getName()))
						{
						userFound = true;
						/**
						 * We do not start a new observer if the address is already monitored
						 * It is a design choice and therefore can change in future releases
						 */
						boolean present = false;
						for(Observer myO : Variables.getObserverList())
							{
							if(myO.getLine().getName().equals(addresses[i].getName()))
								{
								Variables.getLogger().debug("JTAPI : The following line is already monitored so we do not start a new monitor : "+addresses[i].getName());
								present = true;
								break;
								}
							}
						
						if(!present)
							{
							Variables.getObserverList().add(new Observer(addresses[i], myUser));
							Variables.getLogger().debug("JTAPI : The following user is now monitored : "+myUser.getFirstName()+" "+myUser.getLastName()+" "+addresses[i].getName());
							break;
							}
						}
					}
				if(!userFound)
					{
					Variables.getLogger().debug("JTAPI : No user found for the following address : "+addresses[i]);
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : JTAPI address monitoring failed : "+e.getMessage(),e);
			provider = null;
			}
		}
	
	/**
	 * Method used to delete stale monitoring
	 */
	private void deleteMonitoring()
		{
		try
			{
			boolean found = false;
			for(Observer o : Variables.getObserverList())
				{
				for(Address myA : addresses)
					{
					if(o.getLine().getName().equals(myA.getName()))
						{
						found = true;
						break;
						}
					}
				if(!found)
					{
					Variables.getLogger().debug("JTAPI : The following observer has not been found in the address list so we delete it : "+o.getLine().getName());
					o.prepareToClose();
					Variables.getObserverList().remove(o);
					deleteMonitoring();//We call the method again in case more entry need to be removed
					break;
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : JTAPI address monitoring deletion failed : "+e.getMessage(),e);
			}
		}
	
	/**
	 * Method used to get the provider addresses
	 * @throws Exception 
	 */
	private void updateAddresseList() throws Exception
		{
		addresses = provider.getAddresses();
		
		if(addresses != null)
			{
			Variables.getLogger().debug("JTAPI : Found "+addresses.length+" addresses");
		
			for(int i = 0; i< addresses.length; i++)
				{
				Variables.getLogger().debug("JTAPI : Found the following addresses : "+addresses[i]);
				}
			}
		else
			{
			Variables.getLogger().debug("JTAPI : no address found");
			addresses = new Address[0];//To create an empty array rather than a null value
			}
		}
	
	/**
	 * To update the monitored lines
	 */
	public synchronized void updateMonitoring()
		{
		try
			{
			updateAddresseList();
			addMonitoring();
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Failed to update the observer list : "+e.getMessage(),e);
			}
		}
	
	/**
	 * To delete a user monitoring
	 */
	public synchronized boolean deleteUserMonitoring(User u)
		{
		for(Observer o : Variables.getObserverList())
			{
			if(o.getUser().getID().equals(u.getID()))
				{
				o.prepareToClose();
				Variables.getObserverList().remove(o);
				Variables.getLogger().debug("Observer removed for user : "+u.getInfo());
				return true;
				}
			}
		
		Variables.getLogger().error("ERROR : No observer to delete for user : "+u.getInfo());
		return false;
		}
	
	/**
	 * Method used to stop the monitoring
	 */
	public void shutdown() throws Exception
		{
		provider.shutdown();
		
		//Add more if needed
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
				else if (eventList[i] instanceof CiscoAddrCreatedEvImpl)
					{
					Variables.getLogger().debug("JTAPI : A new line has been associated to the JTAPI user. We need to restart the monitoring");
					updateMonitoring();
					}
				else if (eventList[i] instanceof CiscoAddrRemovedEvImpl)
					{
					Variables.getLogger().debug("JTAPI : A line has been removed from the JTAPI user. We need to restart the monitoring");
					try
						{
						updateAddresseList();
						Variables.getLogger().debug("JTAPI : We delete stale observer due to line removed from the monitoring user");
						deleteMonitoring();
						Variables.getLogger().debug("JTAPI : Finish cleaning");
						}
					catch (Exception e)
						{
						Variables.getLogger().error("ERROR : Failed to delete entries in the observer list : "+e.getMessage(),e);
						}
					}
				}
			}
		}
	
	public void tchao()
		{
		run = false;
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/