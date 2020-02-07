package com.alex.wassgar.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Used to store client connection socket and streams
 *
 * @author Alexandre
 */
public class ClientConnection
	{
	/**
	 * Variables
	 */
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public ClientConnection(Socket s) throws IOException
		{
		this.socket = s;
		in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());
		}
	
	public void close() throws IOException
		{
		in.close();
		out.close();
		socket.close();
		}

	public Socket getSocket()
		{
		return socket;
		}

	public void setSocket(Socket socket)
		{
		this.socket = socket;
		}

	public ObjectInputStream getIn()
		{
		return in;
		}

	public void setIn(ObjectInputStream in)
		{
		this.in = in;
		}

	public ObjectOutputStream getOut()
		{
		return out;
		}

	public void setOut(ObjectOutputStream out)
		{
		this.out = out;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
