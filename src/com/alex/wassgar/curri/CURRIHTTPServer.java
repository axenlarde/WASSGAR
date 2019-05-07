package com.alex.wassgar.curri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.alex.wassgar.misc.User;
import com.alex.wassgar.salesforce.SFObject;
import com.alex.wassgar.salesforce.SalesForceManager;
import com.alex.wassgar.utils.ExtensionManipulation;
import com.alex.wassgar.utils.UsefulMethod;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callType;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

/**
 * CURRI HTTP Server
 * @author Alexandre
 */
public class CURRIHTTPServer implements HttpHandler
	{
	/***
	 * Variables
	 */
	
	/***
	 * Constructor
	 */
	public CURRIHTTPServer()
		{
		try
			{
			HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(UsefulMethod.getTargetOption("curriserverport"))), 0);
			HttpContext context = server.createContext("/ECCP", this);
			server.start();
			Variables.getLogger().debug("CURRI Server started !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While listening for new CURRI request : "+e.getMessage(),e);
			} 
		}

	@Override
	public void handle(HttpExchange exc) throws IOException
		{
		Variables.getLogger().debug("CURRI Server : Request received");
		
		try
			{
			if(exc.getRequestMethod().equals("HEAD"))
				{
				Variables.getLogger().debug("CURRI Server : "+exc.getRequestMethod()+" Method received");
				
				Headers hs = exc.getResponseHeaders();
				hs.add("Connection", "Keep-Alive");
				hs.add("Keep-Alive", "timeout = 20000 max = 100");
				exc.sendResponseHeaders(200, 0);
				}
			else if(exc.getRequestMethod().equals("POST"))
				{
				Variables.getLogger().debug("CURRI Server : "+exc.getRequestMethod()+" Method received");
				
				String content = getContent(exc);
				Variables.getLogger().debug("CURRI request content : ");
				Variables.getLogger().debug(content);
				
				//We now need to parse the xml content of the request
				CURRIRequest cr = ManageCURRI.parseCURRI(content);
				
				//Then we send the reply
				CURRIRequest reply = buildReply(cr.getCalledNumber(), cr.getCallingNumber());
				
				OutputStream os = exc.getResponseBody();
				
				if(reply != null)
					{
					reply = CURRIBuilder.buildAcceptAndDisplayAN(reply.getCallingNumber(), reply.getCalledNumber(), reply.getAlertingName(), reply.getType());
					}
				else
					{
					//No alerting name to display so we just accept the call
					reply = CURRIBuilder.buildAccept();
					}
				
				Variables.getLogger().debug("CURRI Server : Sending response : ");
				Variables.getLogger().debug(reply.getContent());
				
				exc.sendResponseHeaders(200, reply.getContent().getBytes().length);
				os.write(reply.getContent().getBytes());
				os.flush();
				os.close();
				
				Variables.getLogger().debug("CURRI Server : Response sent");
				}
			else
				{
				Variables.getLogger().debug("Unkown method : "+exc.getRequestMethod());
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While treating a CURRI request : "+e.getMessage(),e);
			}
		}
	
	
	private String getContent(HttpExchange exc) throws Exception
		{
		BufferedReader in = new BufferedReader(new InputStreamReader(exc.getRequestBody()));
		StringBuffer buf = new StringBuffer();
		
		while(true)
			{
			String s = in.readLine();
			if(s != null)
				{
				buf.append(s);
				buf.append("\r\n");
				}
			else
				{
				break;
				}
			}
		
		return buf.toString();
		}
	
	private CURRIRequest buildReply(String extension, String callingNumber) throws Exception
		{
		//We first look for the user
		for(User u : Variables.getUserList())
			{
			if(u.getExtension().equals(extension))
				{
				if(u.isReverseLookup())
					{
					//We then ask salesforce if the callingnumber is known
					SFObject sfo = SalesForceManager.lookForExtension(u.getSalesforceID(), callingNumber);
					if(sfo != null)
						{
						return new CURRIRequest(callingNumber, extension, UsefulMethod.getAlertingNameFromSFObject(sfo), null, callType.incoming);
						}
					else
						{
						Variables.getLogger().debug("The callingNumber was not found in salesforce for user : "+u.getInfo());
						return null;
						}
					}
				else
					{
					Variables.getLogger().debug("We do not proceed because reverselookup is not activated for this user : "+u.getInfo());
					return null;
					}
				}
			}
		
		Variables.getLogger().debug("No user found for the following extension : "+extension);
		return null;
		}

	}

/*2019*//*RATEL Alexandre 8)*/