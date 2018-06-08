package CommPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Scanner;
import gnu.io.*;
public class SerialComm
{
	static CommPort commPort;
	static Enumeration enumComm;
	static CommPortIdentifier serialPortId;
	static Scanner frmKbd;
	static String portName;
	static OutputStream txData;
	static InputStream rxData;
	static int fb1=0;
	static int fb2=0;
	static int fb3=0;
	static int check=0;
	static String table = "Table 1";
	public static void main(String args[])
	{
		findPorts(); //used to find ports connected to our system
		openPort();  //opens that port which was recently connected
		Thread rxThread = new Thread(new Runnable() /*thread which checks whether 
		there is any input from Arduino
		*/
		{
			public void run()
			{
				while(true)
				{
					try
					{
						rxData = commPort.getInputStream(); //receive data from input port
						int serData;
						
						if((serData = rxData.read()) != -1)
						{
							//System.out.println(serData);//display which button is pressed
							
							if((char)serData == '1' && check == 0)
							{
								fb1++; //when someone press button of feedback 1 it is stored in fb1
								System.out.println(table + ": Feedback: 1 Star");
								check = 1;
							}
							else if((char)serData == '2' && check == 0)
							{
								fb2++;//when someone press button of feedback 2 it is stored in fb2
								System.out.println(table + ": Feedback: 2 Star");
								check = 1;
							}
							else if((char)serData == '3' && check == 0)
							{
								fb3++;//when someone press button of feedback 3 it is stored in fb3
								System.out.println(table + ": Feedback: 3 Star");
								check = 1;
							}
							else if((char)serData == '4')
							{
								System.out.println(table + ": Need Water!");//when someone press button for water
							}
							else if((char)serData == '5')
							{
								System.out.println(table + ": Clean my Table!");//when someone press button for Cleaning table
							}
							else if((char)serData == '6')
							{
								System.out.println(table + ": Order Ready!");
							}
							else if((char)serData == '7')
							{
								System.out.println(table + ": Order Received!");
							}
							else if((char)serData == '8')
							{
								System.out.println(table + ": Total Reviews:");
								System.out.println("\t1 Star: "+fb1);
								System.out.println("\t2 Star: "+fb2);
								System.out.println("\t3 Star: "+fb3);
								System.out.println("\n\tPress 1 to Signal Order Ready \n\tPress 2 to Signal Order Recieved!");
							}
							else if((char)serData == '9')
							{
								System.out.println("You Found Us!!! \n\n\n\t*******************************************");
								System.out.println("\t*Software Created by GraveMind and RoboMex*");
								System.out.println("\t*******************************************");
							}
							else if((char)serData == '0')
							{
								System.out.println("\n\n" + table + ": initiated!");//when table is initiated
								check = 0;
							}

						}
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
			}
		});rxThread.start();
		while(true)
		{
			frmKbd = new Scanner(System.in);
			String c;

			c = "" + frmKbd.next();
			//System.out.print(c);

			if (c.equals("1") || c.equals("2") || c.equals("0") || c.equals(".")) {

				sendToPort(c.charAt(0));
				Thread tmrThread = new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							//Thread.sleep(20000);
							//sendToPort('0');
							System.out.print(" >>> ");
						
						}catch(Exception e){}
					}
				});tmrThread.start();
			}
			else if (c.equals("exit")) {
				System.out.println("\t+========================+");
				System.out.println("\t|Thank You for using Me!!|");
				System.out.println("\t+========================+");
				System.exit(0);
			}
			else
				System.out.println(" >>> Invalid Input~!");
		}
	}
	static void findPorts()
	{ //firstly it identifies the port by creating enum
		enumComm = CommPortIdentifier.getPortIdentifiers();
		while(enumComm.hasMoreElements()) //if enum has ports to display
		{
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) 
			{
				portName = serialPortId.getName();
				//display the available port name
				System.out.println("Available port: "+portName);
				
			}
		}
	}
	static void openPort()
	{ // this function opens the port which was connected latest
		try
		{

			commPort = serialPortId.open(portName, 1000);
			System.out.println("Connected to port: "+portName);//display name of connected port
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	static void sendToPort(char txByte)
	{
		try
		{
			txData = commPort.getOutputStream(); //this is used for glowing led
			txData.write(txByte);
			txData.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
