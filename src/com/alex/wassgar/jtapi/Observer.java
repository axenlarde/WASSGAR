package com.alex.wassgar.jtapi;

import javax.telephony.events.*;
import javax.telephony.media.MediaCallObserver;
import javax.telephony.media.events.MediaTermConnDtmfEv;
import javax.telephony.*;
import javax.telephony.callcontrol.CallControlCallObserver;
import javax.telephony.callcontrol.events.CallCtlConnAlertingEv;
import javax.telephony.callcontrol.events.CallCtlConnEstablishedEv;
import javax.telephony.callcontrol.events.CallCtlConnOfferedEv;
import javax.telephony.callcontrol.events.CallCtlEv;

import com.alex.wassgar.misc.ManageUser;
import com.alex.wassgar.misc.User;
import com.alex.wassgar.utils.Variables;
import com.alex.wassgar.utils.Variables.callType;
import com.cisco.jtapi.JTAPIDecoder;
import com.cisco.jtapi.extensions.CiscoAddressObserver;
import com.cisco.jtapi.extensions.CiscoCall;
import com.cisco.jtapi.extensions.CiscoCallChangedEv;
import com.cisco.jtapi.extensions.CiscoCallCtlConnOfferedEv;
import com.cisco.jtapi.extensions.CiscoCallEv;
import com.cisco.jtapi.extensions.CiscoConnection;
import com.cisco.jtapi.extensions.CiscoPartyInfo;
import com.cisco.jtapi.extensions.CiscoUrlInfo;


/**
 * Class used to monitor one line
 * 
 * @author Alexandre
 *
 */
public class Observer implements CallControlCallObserver, MediaCallObserver, CiscoAddressObserver
	{
	/**
	 * Variables
	 */
	private Address line;
	private User user;

	/**
	 * Constructor
	 */
	public Observer(Address line, User user)
		{
		super();
		this.line = line;
		this.user = user;
		
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
				Variables.getLogger().debug("Line "+line.getName()+" event : "+events[i].getClass().getName());
				}
			}
		}


	@Override
	public void callChangedEvent(CallEv[] events)
		{
		
		try
			{
			if(events != null)
				{
				for (int i = 0; i < events.length; i++)
					{
					if(events[i].getID() == TermConnRingingEv.ID)
						{
						/**
						 * To manage incoming calls
						 */
						/**
						 * To capture only the relevant event we check that the
						 * called number is the good one
						 */
						
						CiscoCall localCall = (CiscoCall)((TermConnRingingEv) events[i]).getCall();
						CiscoPartyInfo calledParty = localCall.getCalledPartyInfo();
						CiscoPartyInfo callingParty = localCall.getCurrentCallingPartyInfo();
						
						if(calledParty.getAddress().getName().equals(this.line.getName()))
							{
							Call call = new Call(user,
									line,
									Integer.toString(localCall.getCallID().getGlobalCallID()),
									calledParty,
									callingParty,
									callType.incoming);
							
							if(!CallListManager.isThisCallExisting(call))
								{
								CallListManager.addCall(call);
								ManageUser.processNewCall(user, call);
								}
							}
						}
					else if(events[i].getID() == CallCtlConnEstablishedEv.ID)
						{
						/**
						 * To manage outgoing calls
						 */
						/**
						 * To capture only the relevant event we check that the 
						 * calling number is the good one
						 */
						
						CiscoCall localCall = (CiscoCall)((CallCtlConnEstablishedEv) events[i]).getCall();
						CiscoPartyInfo calledParty = localCall.getCalledPartyInfo();
						CiscoPartyInfo callingParty = localCall.getCurrentCallingPartyInfo();
						
						if(callingParty.getAddress().getName().equals(this.line.getName()))
							{
							Call call = new Call(user,
									line,
									Integer.toString(localCall.getCallID().getGlobalCallID()),
									calledParty,
									callingParty,
									callType.outgoing);
							
							if(!CallListManager.isThisCallExisting(call))
								{
								CallListManager.addCall(call);
								ManageUser.processNewCall(user, call);
								}
							}
						}
					else if(events[i].getID() == CallObservationEndedEv.ID)
						{
						/**
						 * To manage call ending
						 */
						
						CiscoCall localCall = (CiscoCall)((CallObservationEndedEv) events[i]).getCall();
						
						CallListManager.endCall(line.getName(),Integer.toString(localCall.getCallID().getGlobalCallID()));
						}
					
					//Variables.getLogger().debug("### "+events[i].getClass().getName());//Temp
					
					/***********************
					 * In case of advanced debug needs, we can turn on the advanced call debugging
					 * WARNING : Really verbose !
					 */
					if(Variables.isAdvancedLogs())
						{
						if(events[i].isNewMetaEvent())
							{
							Variables.getLogger().debug("Call Meta code : "+JTAPIDecoder.getMetaCode( events[i].getMetaCode() ));
							}
						
						if(events[i] instanceof ConnEv)
							{
							Variables.getLogger().debug(((ConnEv)events[i]).getConnection().getAddress().getName());
							CiscoConnection conn  = (CiscoConnection) ((ConnEv)events[i]).getConnection();
							Variables.getLogger().debug (  " " + conn.getConnectionID());
		                    Variables.getLogger().debug( " " + JTAPIDecoder.getCallCtlCauseFromReason(((CiscoCallEv)events[i]).getCiscoFeatureReason()));
							}
						else if ( events[i] instanceof TermConnEv )
							{
							Variables.getLogger().debug ( ((TermConnEv)events[i]).getTerminalConnection().getTerminal().getName() );
		                    Variables.getLogger().debug( " " + JTAPIDecoder.getCallCtlCauseFromReason(((CiscoCallEv)events[i]).getCiscoFeatureReason()));
							}
						else if ( events[i] instanceof CallEv )
							{
							Variables.getLogger().debug ( "callID=" + ((CallEv)events[i]).getID() );
							if ( events[i] instanceof CiscoCallChangedEv )
								{
								CiscoCallChangedEv ev = (CiscoCallChangedEv)events[i];
								Variables.getLogger().debug ("Surviving= " + ev.getSurvivingCall().getCallID().getCallManagerID() + "/" + ev.getSurvivingCall().getCallID().getGlobalCallID() );
								Variables.getLogger().debug (" origcall= " + ev.getOriginalCall().getCallID().getCallManagerID() + "/" + ev.getOriginalCall().getCallID().getGlobalCallID() );
								if (ev.getConnection() != null )
									{
									 Variables.getLogger().debug(" address= " + ev.getConnection().getAddress().getName());
									 Variables.getLogger().debug(" connectionID = " + ev.getConnection().getConnectionID() );
		
									}
								 if (ev.getTerminalConnection() != null )
									{
									Variables.getLogger().debug (ev.getTerminalConnection().getTerminal().getName());
									}
		
								}
							Variables.getLogger().debug( " " + JTAPIDecoder.getCallCtlCauseFromReason(((CiscoCallEv)events[i]).getCiscoFeatureReason()));
							}
		
						Variables.getLogger().debug ( " Cause: " + JTAPIDecoder.getCause( events[i].getCause() ) );
						if ( events[i] instanceof CallCtlEv )
							{
							Variables.getLogger().debug ( " CallControlCause: " + JTAPIDecoder.getCallCtlCause( ((CallCtlEv)events[i]).getCallControlCause() ) );
							}
						Variables.getLogger().debug ( "\n" );
		               
		                CiscoCall localCall;
		                
		                Variables.getLogger().debug("#ID : "+JTAPIDecoder.getEvent(events[i].getID()));
		                
						switch ( events[i].getID () )
							{
						case CallCtlConnOfferedEv.ID:
		                    localCall = (CiscoCall)((CallCtlConnOfferedEv) events[i]).getCall(); 
		                    printCallInfo(localCall);
		                    Variables.getLogger().debug("CallingPartyIpAddr=" + ((CiscoCallCtlConnOfferedEv)events[i]).getCallingPartyIpAddr());
							offered ( (CallCtlConnOfferedEv) events[i] );                   
							break;
						case CallCtlConnAlertingEv.ID:
							alerting ( (CallCtlConnAlertingEv) events[i] );
		                    localCall = (CiscoCall)((CallCtlConnAlertingEv) events[i]).getCall(); 
		                    printCallInfo(localCall);
							break;
		                case CallCtlConnEstablishedEv.ID:
		                    localCall = (CiscoCall)((CallCtlConnEstablishedEv) events[i]).getCall(); 
		                    printCallInfo(localCall);
		                    break;
						case MediaTermConnDtmfEv.ID:
							digit ( (MediaTermConnDtmfEv) events[i] );
							break;
						default:
							Variables.getLogger().debug("Default event");
							localCall = (CiscoCall)events[i].getCall();
							printCallInfo(localCall);
							}
						}
					}
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : JTAPI : "+e.getMessage(),e);
			}
		}
	
	public void printCallInfo(CiscoCall localCall)
		{
	    if ( localCall != null )
			{
	        CiscoPartyInfo currCalling = localCall.getCurrentCallingPartyInfo();
	        CiscoPartyInfo currCalled = localCall.getCurrentCalledPartyInfo();
	        CiscoPartyInfo lrpInfo = localCall.getLastRedirectingPartyInfo();
	        CiscoPartyInfo calledInfo = localCall.getCalledPartyInfo();
	        if ( currCalling != null )
				{
	            CiscoUrlInfo urlInfo = currCalling.getUrlInfo();
	            Variables.getLogger().debug("Current Calling PartyInfo= " + "Addr= " + currCalling.getAddress().getName() +
	                           " AddrPI=" + currCalling.getAddressPI() +
	                           " DispName=" + currCalling.getDisplayName() +
	                           " DispNamePI=" + currCalling.getDisplayNamePI() +
	                           " Unicode=" + currCalling.getUnicodeDisplayName() +
	                           " locale=" + currCalling.getlocale()); 
	            if ( urlInfo != null )
					{
	                Variables.getLogger().debug(" user=" + urlInfo.getUser() +
	                               " Host=" + urlInfo.getHost() +
	                               " TransType=" + urlInfo.getTransportType() +
	                               " Port=" + urlInfo.getPort() +
	                               " urlType=" + urlInfo.getUrlType() + "\n" );
					}
				}
			else
				{
				Variables.getLogger().debug("Null CurrCalling Info" );
				}
	        if ( currCalled != null )
				{
	            CiscoUrlInfo urlInfo = currCalled.getUrlInfo();
	            Variables.getLogger().debug("Current Called PartyInfo= " + "Addr= " + currCalled.getAddress().getName() +
	                           " AddrPI=" + currCalled.getAddressPI() +
	                           " DispName=" + currCalled.getDisplayName() +
	                           " DispNamePI=" + currCalled.getDisplayNamePI() +
	                           " Unicode=" + currCalled.getUnicodeDisplayName() +
	                           " locale=" + currCalled.getlocale() );
	            if ( urlInfo != null )
					{
	                Variables.getLogger().debug(" user=" + urlInfo.getUser() +
	                               " Host=" + urlInfo.getHost() +
	                               " TransType=" + urlInfo.getTransportType() +
	                               " Port=" + urlInfo.getPort() +
	                               " urlType=" + urlInfo.getUrlType() + "\n" );
					}
	            else
	            	{
	            	Variables.getLogger().debug("Null called urlInfo");
	            	}
				}
			else 
				{
				Variables.getLogger().debug("Null Curr Called Info");
				}
	        Variables.getLogger().debug("Current Calling CallInfo: " + "Addr= " + localCall.getCurrentCallingAddress().getName() +
	                       " AddrPI=" + localCall.getCallingAddressPI() +
	                       " DispName=" + localCall.getCurrentCallingPartyDisplayName() +
	                       " DispNamePI=" + localCall.getCurrentCallingDisplayNamePI() +
	                       " Unicode=" + localCall.getCurrentCallingPartyUnicodeDisplayName() +
	                       " locale=" + localCall.getCurrentCallingPartyUnicodeDisplayNamelocale() + "\n" ); 
	         Variables.getLogger().debug("Current Called CallInfo: " + "Addr= " + localCall.getCurrentCalledAddress().getName() +
	                       " AddrPI=" + localCall.getCalledAddressPI() +
	                       " DispName=" + localCall.getCurrentCalledPartyDisplayName() +
	                       " DispNamePI=" + localCall.getCurrentCalledDisplayNamePI() +
	                       " Unicode=" + localCall.getCurrentCalledPartyUnicodeDisplayName() +
	                       " locale=" + localCall.getCurrentCalledPartyUnicodeDisplayNamelocale() + "\n" ); 
	         if (lrpInfo == null )
				{
	             Variables.getLogger().debug("Null LRP");
				}
			else
				{
	            CiscoUrlInfo urlInfo = lrpInfo.getUrlInfo();
	            Variables.getLogger().debug("LRP PartyInfo= " + "Addr= " + lrpInfo.getAddress().getName() +
	                           " AddrPI=" + lrpInfo.getAddressPI() +
	                           " DispName=" + lrpInfo.getDisplayName() +
	                           " DispNamePI=" + lrpInfo.getDisplayNamePI() +
	                           " Unicode=" + lrpInfo.getUnicodeDisplayName() +
	                           " locale=" + lrpInfo.getlocale() );
	            if ( urlInfo != null )
					{
	                Variables.getLogger().debug(" user=" + urlInfo.getUser() +
	                               " Host=" + urlInfo.getHost() +
	                               " TransType=" + urlInfo.getTransportType() +
	                               " Port=" + urlInfo.getPort() +
	                               " urlType=" + urlInfo.getUrlType() + "\n" );
					}  
				}
	         
	         if (calledInfo == null )
				{
	            Variables.getLogger().debug("Null CalledInfo");
				}
			else
				{
	            CiscoUrlInfo urlInfo = calledInfo.getUrlInfo();
	            Variables.getLogger().debug("Called PartyInfo= " + "Addr= " + calledInfo.getAddress().getName() +
	                           " AddrPI=" + calledInfo.getAddressPI() +
	                           " DispName=" + calledInfo.getDisplayName() +
	                           " DispNamePI=" + calledInfo.getDisplayNamePI() +
	                           " Unicode=" + calledInfo.getUnicodeDisplayName() +
	                           " locale=" + calledInfo.getlocale() );
	            if ( urlInfo != null )
					{
	                Variables.getLogger().debug(" user=" + urlInfo.getUser() +
	                               " Host=" + urlInfo.getHost() +
	                               " TransType=" + urlInfo.getTransportType() +
	                               " Port=" + urlInfo.getPort() +
	                               " urlType=" + urlInfo.getUrlType() + "\n" );
					}
	            else
	            	{
	            	 Variables.getLogger().debug("urlInfo null");
	            	}
				}
			}
		}
	
	void digit ( MediaTermConnDtmfEv ev )
		{
		}

	void offered ( CallCtlConnOfferedEv ev )
		{
		}

	void alerting ( CallCtlConnAlertingEv ev )
		{
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