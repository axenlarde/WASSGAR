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
	
	
	public String getCallID()
		{
		return callID;
		}
	public void setCallID(String callID)
		{
		this.callID = callID;
		}
	public CiscoPartyInfo getCalledParty()
		{
		return calledParty;
		}
	public void setCalledParty(CiscoPartyInfo calledParty)
		{
		this.calledParty = calledParty;
		}
	public CiscoPartyInfo getCallingParty()
		{
		return callingParty;
		}
	public void setCallingParty(CiscoPartyInfo callingParty)
		{
		this.callingParty = callingParty;
		}
	public long getStartTime()
		{
		return startTime;
		}
	public void setStartTime(long startTime)
		{
		this.startTime = startTime;
		}
	public long getEndTime()
		{
		return endTime;
		}
	public void setEndTime(long endTime)
		{
		this.endTime = endTime;
		}
	public long getDuration()
		{
		duration = System.currentTimeMillis() - startTime;
		return duration;
		}
	public void setDuration(long duration)
		{
		this.duration = duration;
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
		Date myDate = new Date();
		myDate.setTime(getStartTime());
		setFormatStartTime(dateFormat.format(myDate));
		
		return formatStartTime;
		}
	public void setFormatStartTime(String formatStartTime)
		{
		this.formatStartTime = formatStartTime;
		}
	public String getFormatEndTime()
		{
		Date myDate = new Date();
		myDate.setTime(getEndTime());
		setFormatEndTime(dateFormat.format(myDate));
		
		return formatEndTime;
		}
	public void setFormatEndTime(String formatEndTime)
		{
		this.formatEndTime = formatEndTime;
		}
	public String getFormatDuration()
		{
		Date myDate = new Date();
		myDate.setTime(getDuration());
		setFormatDuration(timeFormat.format(myDate));
		
		return formatDuration;
		}
	public void setFormatDuration(String formatDuration)
		{
		this.formatDuration = formatDuration;
		}
	public callType getType()
		{
		return type;
		}
	public void setType(callType type)
		{
		this.type = type;
		}
	public User getUser()
		{
		return user;
		}
	public void setUser(User user)
		{
		this.user = user;
		}
	public Address getLine()
		{
		return line;
		}
	public void setLine(Address line)
		{
		this.line = line;
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/