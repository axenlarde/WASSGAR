package com.alex.wassgar.jtapi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.telephony.Address;

import com.alex.wassgar.misc.User;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callStatus;
import com.alex.wassgar.utils.Variables.callType;
import com.cisco.jtapi.extensions.CiscoPartyInfo;

/**
 * Class used to manage a call
 * 
 * @author Alexandre
 */
public class Call
	{
	/**
	 * Variables
	 */
	private String callID;
	private CiscoPartyInfo calledParty;
	private CiscoPartyInfo callingParty;
	private long startTime;
	private String formatStartTime;
	private long endTime;
	private String formatEndTime;
	private long duration;
	private String formatDuration;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat timeFormat;
	private callStatus status;
	private callType type;
	private User user;
	private Address line;
	
	/**
	 * Constructor
	 */
	public Call(User user, Address line, String callID, CiscoPartyInfo calledParty, CiscoPartyInfo callingParty, callType type)
		{
		super();
		this.user = user;
		this.line = line;
		this.callID = callID;
		this.calledParty = calledParty;
		this.callingParty = callingParty;
		this.type = type;
		
		timeFormat = new SimpleDateFormat("mm:ss:SSS");
		dateFormat = new SimpleDateFormat("dd/MM/YYYY - mm:ss:SSS");
		//dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));//Check if needed
		
		//We consider that the following can be initialize with the call object
		status = callStatus.inProgress;
		startTime = System.currentTimeMillis();
		endTime = 0;
		}
	
	
	/**
	 * To end the call calculation
	 */
	public void callEnds()
		{
		endTime = System.currentTimeMillis();
		status = callStatus.ended;
		Variables.getCallLogger().info(user.getFirstName()+","+
				user.getLastName()+","+
				line.getName()+","+
				getFormatStartTime()+","+
				getFormatEndTime()+","+
				getFormatDuration()+","+
				calledParty.getAddress().getName()+","+
				callingParty.getAddress().getName());
		}
	
	/**
	 * Return the line extension followed by the callID
	 * Used to compare this call to another one
	 * Indeed, the callID is the same for the calling and the called party but we sometime need 
	 * to count calls for both party
	 */
	public String getInfo()
		{
		return line.getName()+callID;
		}
	
	
	public String getCallID()
		{
		return callID;
		}
	public CiscoPartyInfo getCalledParty()
		{
		return calledParty;
		}
	public CiscoPartyInfo getCallingParty()
		{
		return callingParty;
		}
	public long getStartTime()
		{
		return startTime;
		}
	public long getEndTime()
		{
		return endTime;
		}
	public long getDuration()
		{
		duration = System.currentTimeMillis() - startTime;
		return duration;
		}
	public callStatus getStatus()
		{
		return status;
		}
	public void setStatus(callStatus status)
		{
		this.status = status;
		}
	public String getFormatStartTime()
		{
		if(formatStartTime == null)
			{
			Date myDate = new Date();
			myDate.setTime(getStartTime());
			formatStartTime = dateFormat.format(myDate);
			}
			
		return formatStartTime;
		}
	public String getFormatEndTime()
		{
		if(formatEndTime == null)
			{
			Date myDate = new Date();
			myDate.setTime(getEndTime());
			formatEndTime = dateFormat.format(myDate);
			}
			
		return formatEndTime;
		}
	public String getFormatDuration()
		{
		if(formatDuration == null)
			{
			Date myDate = new Date();
			myDate.setTime(getDuration());
			formatDuration = timeFormat.format(myDate);
			}
		
		return formatDuration;
		}
	public callType getType()
		{
		return type;
		}
	public User getUser()
		{
		return user;
		}
	public Address getLine()
		{
		return line;
		}
	}

/*2019*//*RATEL Alexandre 8)*/