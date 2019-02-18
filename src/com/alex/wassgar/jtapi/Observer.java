package com.alex.wassgar.jtapi;

import javax.telephony.events.*;
import javax.telephony.*;

import com.alex.wassgar.utils.Variables;
import com.cisco.jtapi.extensions.CiscoAddressObserver;

/**
 * Class used to monitor one line
 * 
 * @author Alexandre
 *
 */
public class Observer implements CiscoAddressObserver, CallObserver
	{
	/**
	 * Variables
	 */
	private Address line;

	/**
	 * Constructor
	 */
	public Observer(Address line)
		{
		super();
		this.line = line;
		
		try
			{
			line.addObserver(this);
			line.addCallObserver(this);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : JTAPI : Adding observer to line : "+line.getName()+" : "+e.getMessage(),e);
			}
		}





	@Override
	public void addressChangedEvent(AddrEv[] events)
		{
		if(events != null)
			{
			for (int i = 0; i < events.length; i++)
				{
				if (events[i] instanceof CallEv)
					{
					Variables.getLogger().debug("JTAPI : Call event on line : "+line.getName());
					}
				
				Variables.getLogger().debug("#Line "+line.getName()+" event : "+events[i].getClass().getName());
				}
			}
		}





	@Override
	public void callChangedEvent(CallEv[] events)
		{
		if(events != null)
			{
			for (int i = 0; i < events.length; i++)
				{
				Variables.getLogger().debug("Line "+line.getName()+" event : "+events[i].getClass().getName());
				}
			}
		}

	}

/*2019*//*RATEL Alexandre 8)*/