package com.alex.wassgar.server;

import com.alex.wassgar.utils.Variables.requestType;

/**
 * Static method to build request
 * @author Alexandre
 *
 */
public class RequestBuilder
	{
	
	/**
	 * To build a popup request
	 * 
	 * Content = url
	 */
	public static Request buildPopup(String exec)
		{
		StringBuffer buf = new StringBuffer();
		buf.append(exec);
		Request r = new Request(requestType.displayPopup, buf.toString());
		
		return r;
		}
	
	/**
	 * To build a connection accepted request
	 */
	public static Request buildAcceptedConnection(String desc)
		{
		StringBuffer buf = new StringBuffer();
		buf.append(desc);
		Request r = new Request(requestType.connectionAccepted, buf.toString());
		
		return r;
		}
	
	/**
	 * To build a connection rejected request
	 */
	public static Request buildRejectedConnection(String desc)
		{
		StringBuffer buf = new StringBuffer();
		buf.append(desc);
		Request r = new Request(requestType.connectionRejected, buf.toString());
		
		return r;
		}
	
	/**
	 * To build a success request
	 */
	public static Request buildSuccess(String desc)
		{
		StringBuffer buf = new StringBuffer();
		buf.append(desc);
		Request r = new Request(requestType.success, buf.toString());
		
		return r;
		}
	
	/**
	 * To build a success request
	 */
	public static Request buildFailed(String desc)
		{
		StringBuffer buf = new StringBuffer();
		buf.append(desc);
		Request r = new Request(requestType.failed, buf.toString());
		
		return r;
		}

	}

/*2019*//*RATEL Alexandre 8)*/