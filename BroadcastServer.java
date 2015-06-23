import java.util.*;
import java.net.*;
import java.io.*;

class BroadcastServer
{

	public static void main(String... s)
	{
		try
		{
			ArrayList al=new ArrayList();
			ArrayList nal=new ArrayList();

			System.out.println("server started");
			ServerSocket ss=new ServerSocket(1500);
			
			while(true)
			{
				Socket sk=ss.accept();
				al.add(sk);
				new ClientThread(sk,al,nal);
				
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

class ClientThread extends Thread
{
	ArrayList al;
	ArrayList nal;
	Socket sk;
	boolean pause=false;
	ClientThread(Socket sk,ArrayList al,ArrayList nal)
	{
		this.sk=sk;
		this.al=al;
		this.nal=nal;
		start();
	}

	public void run()
	{
		try
		{
			DataInputStream din=new DataInputStream(sk.getInputStream());
			String name=din.readUTF();
			nal.add(name);
			broadcastMsg(name+" connected");

			DataOutputStream dout=new DataOutputStream(sk.getOutputStream());
			String msg="welcome "+name+", now you start chatting";
			dout.writeUTF(msg);
			dout.flush();

			
			while(true)
			{
				msg=din.readUTF();

			try
			{

				if(msg.equalsIgnoreCase("stop"))
				{
					broadcastMsg(name+" disconnected");
					al.remove(sk);
					nal.remove(name);
				}

				else if(msg.equalsIgnoreCase("list"))
				{
					for(Object ob:nal)
					{
						String s=(String)ob;
						dout.writeUTF(s);
						dout.flush();
						
					}
				}

				else if(msg.equalsIgnoreCase("pause"))
				{
					broadcastMsg(name+" paused");
				}

				else if(msg.equalsIgnoreCase("resume"))
				{
					broadcastMsg(name+" resumed");
				}
				
				else
				{
					broadcastMsg(name+": "+ msg);
				}
			}

			catch(Exception e)
			{
				broadcastMsg(name+": "+ msg);
			}
			}
	
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	void broadcastMsg(String msg)
	{
		try
		{
			for(Object ob:al)
			{
				Socket sk=(Socket)ob;
				DataOutputStream dout=new DataOutputStream(sk.getOutputStream());
				dout.writeUTF(msg);
				dout.flush();
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	void broadcastMsg(String msg,Socket sk)
	{
		try
		{
				//Socket sk=(Socket)ob;
				DataOutputStream dout=new DataOutputStream(sk.getOutputStream());
				dout.writeUTF(msg);
				dout.flush();

		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
