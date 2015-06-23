import java.net.*;
import java.util.*;
import java.io.*;
public class ChatClient
{
	static boolean pause=false;
	public static void main(String... s)
	{
		try
		{
			System.out.println("Client Started");	
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("enter your name");
			String name=br.readLine();

			System.out.println("enter server's address");
			String address=br.readLine();
			
			Socket sk=new Socket(address,1500);
			System.out.println("Client Connected");

			DataOutputStream dout=new DataOutputStream(sk.getOutputStream());
			dout.writeUTF(name);
			dout.flush();
			new ReaderThread(sk);
	System.out.println("enter your msg or stop to terminate or pause/resume to pause/resume chat");

			while(true)
			{
			String msg=br.readLine();
			if(!pause)
			{
			dout.writeUTF(msg);
			dout.flush();
			}
				try
				{
					if(msg.equalsIgnoreCase("stop"))
					{
					break;
					}	
					else if(msg.equalsIgnoreCase("pause"))
					{
					pause=true;		
					}
			
					else if(msg.equalsIgnoreCase("resume"))
					{
					//System.out.println("check");
					pause=false;
					}
				}

				catch(Exception e){e.printStackTrace();}
							
			}			
		}

		catch(Exception e)
		{
			//e.printStackTrace();
		}
	}
}

class ReaderThread extends Thread
{
	Socket sk;
	//boolean pause;
	ReaderThread(Socket sk)
	{
		this.sk=sk;
		//this.pause=pause;
		setDaemon(true);
		start();
	}

	public void run()
	{
		try
		{
			
			DataInputStream din=new DataInputStream(sk.getInputStream());
			while(true)
			{
				String msg=din.readUTF();
				if(!ChatClient.pause)
				{
				//String msg=din.readUTF();
				System.out.println(msg);
				//System.out.println("pause:"+ChatClient.pause);
				}
				
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
