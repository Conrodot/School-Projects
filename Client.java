//IMPORTS
import java.io.*;
import java.net.*;

/*
 * This class will create a send and receive Socket, Then create 5 read 
 * and 5 write packets, Next prints out the information of the packets,
 * The packet is then sent to port 23, Wait..., Finally it will print the 
 * received information 
 */
public class Client {
	
	//INSTANCE VARIBLES
	DatagramSocket snrSocket;
	DatagramPacket sPacket;
	DatagramPacket rPacket;
	//String messages to bytes
	String s1 = "TEXT.txt";
	String s2 = "OcTEt";
	byte[] b1 = s1.getBytes();
	byte[] b2 = s2.getBytes();
	byte[] msg = new byte[4 + b1.length + b2.length];
	byte receiveData[] = new byte[4];
	//general length integer
	private int len;
	

	
	public Client(){
		
		try {
			//Create the send and receive Socket bound to any available 
			//port
			snrSocket = new DatagramSocket();
		}
		
		catch (SocketException sE) {
			//Socket cannot be created
			sE.printStackTrace();
			System.exit(1);
		}
	}
	
	/*This class loops 11 times creating 5 read request packets, 5 write 
	 * request packets and 1 error packet.
	 */
	public void snrPackets() {
		
		//Repeat 11 times
		for(int i = 0; i <= 11; i++) {
			
			//CREATING MESSAGE FOR PACKET
			msg[0] = 0;
			//Invalid request packet is made    msg = (0 0)
			if(i == 10) {  
				msg[1] = 0;
			}
			//read request packet is made        msg = (0 1)
			else if(i%2 == 0) {
				msg[1] = 1;
			}
			//Write request packet is made       msg = (0 2)
			else {
				msg[1] = 2;
			}
			
			//Complete msg format
			System.arraycopy(b1, 0, msg, 2, b1.length);
			msg[b1.length + 2] = 0;
			
			System.arraycopy(b2, 0, msg, b1.length + 3, b2.length);
			msg[3 + b1.length + b2.length] = 0;
			
			//SENDING PACKETS
			//The client and server are on the same computer.
			//The destination port number on the destination host (23).
			try {
				sPacket = new DatagramPacket(msg, msg.length,
											 InetAddress.getLocalHost(),
											 23);
			}
			catch(UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			//Print the information put in the packet
			printSPacket();
		
			//Send
			try {
				snrSocket.send(sPacket);
			}
			catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Client: Packet sent to host :)");
			
			//RECEIVING PACKETS
			//A receiving packet is constructed to receive packets with
			//size msg
			rPacket = new DatagramPacket(receiveData, 4);
			//datagram blocked until Socket receives msg.
			try {
				snrSocket.receive(rPacket);
			}
			catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			//Print the received datagram
			printRPacket();
			System.out.print("Containing: ");
		    // Form a String from the byte array.
		    String received = new String(receiveData,0,len);   
		    System.out.println(received);
		}
		
		//All done
		snrSocket.close();
	}
	
	/*This class prints all the current information of put into the 
	 *send packet
	 */
	public void printSPacket() {
		len = sPacket.getLength();
		System.out.println("Client: Sending packet");
	    System.out.println("To host: " + sPacket.getAddress());
	    System.out.println("Destination host port: " + sPacket.getPort());
	    System.out.println("Length: " + len);
	    System.out.print("Containing: ");

	    for(int j = 0; j < msg.length; j++){
	    	System.out.print(msg[j] + " ");
	    }
	    System.out.println();
	}
	
	/*This class prints all the current information of put into the 
	 *received packet
	 */
	public void printRPacket() {
		len = rPacket.getLength();
		System.out.println("Client: Packet received:");
	    System.out.println("From host: " + rPacket.getAddress());
	    System.out.println("Host port: " + rPacket.getPort());
	    System.out.println("Length: " + len);
	}
	
	
	public static void main(String args[])
	{
		Client c = new Client();
		c.snrPackets();
	}

}
