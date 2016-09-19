/*
 * Based on the UDP/IP, This server receives a packet from the server
 * that contains a character string. The string is then echoed back to
 * the client.
 */

//IMPORTS
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Server {
	
	//INSTANCE VARIABLES
	DatagramSocket sendSocket;
	DatagramPacket sendPacket;
	DatagramSocket receiveSocket;
	DatagramPacket receivePacket;
	
	private int len;
	byte[] dataReceived = new byte[50];
	byte[] dataEchoed = new byte[4];
	
	//Constructor of the server that creates the 2 DatagramScokets
	public Server() {
		
		try{
			
			//the server creates a DatagramSocket bound to any available
			// port. (Send Socket)
			//the server creates a DatagramSocket to receive port 69
			// (Receive Socket)
			//sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(69);
		}
		catch(SocketException se){
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/*
	 * This method waits for a packet to be received. Once received,
	 * The packet is then unpacked into a byte array to be analyzed.
	 * Next, The data is printed to the console.
	 * The type of request from the data is determined
	 * and the appropriate datagram packet is sent 
	 * back to the client.
	 * 
	 */
	public void receiveAndEcho(){
		
		while(true){
		
		//RECEIVING PACKET
		//Receiving the packet in a array of bytes (msg is about 17 bytes
		// in length)
		receivePacket = new DatagramPacket(dataReceived,dataReceived.length);
		
		//Wait for packet to be received.
		System.out.println("Server: Waiting for packet...");
		try{
			System.out.println("waiting.................");
			try{
				//Slow down
				Thread.sleep(5000);
			}
			catch(InterruptedException e){
				e.printStackTrace();
				System.exit(1);
			}
			receiveSocket.receive(receivePacket);
		}
		catch(IOException e){
			System.out.print("IO Exception: High");
	        System.out.println("Receive Socket Timed Out :(" + e);
	        e.printStackTrace();
	        System.exit(1);
	    }
		

		//Check if the packet received is in a valid format
		byte[] filenameTest = new byte[1];
		byte[] modeTest = new byte[1];
		int counter = 0;
		for(int i = 2; i < dataReceived.length; i++){
			
			//If a zero is found
			if(dataReceived[i] == 0){
				counter++;
				if(counter == 1){
					filenameTest = Arrays.copyOfRange(dataReceived, 2, i);
				}
				if(counter == 2){
					modeTest = Arrays.copyOfRange(dataReceived, 3 + filenameTest.length, 
													i);
					break;
				}
			}
				
		}
		
		//Once the packet is received, it is processed and printed to the 
		//console.
		processReceived();
		
		//Determine if the request is a read or a write.
		//If its a read echo (0301), if write (0400)
		receivedRequest();
		
		
		//ECHOING PACKET
		//Creating a packet that contains the byte array to be echoed.
		//It is then sent back to the port and address of the sender
		//(the client)
		sendPacket = new DatagramPacket(dataEchoed, dataEchoed.length,
										receivePacket.getAddress(),
										receivePacket.getPort());
		
		//Print the data contained within the echoed packet.
		processEcho();
		
		try{
			sendSocket = new DatagramSocket();
		}
		catch(SocketException se){
			se.printStackTrace();
			System.exit(1);
		}
		
		
		//Send the packet
		try{
			sendSocket.send(sendPacket);
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Server: Packet sent :)");
		
		//All done
		sendSocket.close();
	
		}
	}
	
	public void receivedRequest(){
		
		 //Its a Read request
		if(dataReceived[1] == 1){
			
			dataEchoed[0] = (byte)0;
			dataEchoed[1] = (byte)3;
			dataEchoed[2] = (byte)0;
			dataEchoed[3] = (byte)1;
		}
		 //Its a Write request
		if(dataReceived[1] == 2){
			
			dataEchoed[0] = (byte)0;
			dataEchoed[1] = (byte)4;
			dataEchoed[2] = (byte)0;
			dataEchoed[3] = (byte)0;
		}
		if(dataReceived[1] == 0){
			//Invalid request
			System.out.println();
			System.out.println("SERVER RECEIVED INVALID REQUEST");
			System.exit(1);
		}
	}
	
	//Processes the received datagramPacket and prints it to the console
	public void processReceived(){
		
		len = receivePacket.getLength();
		System.out.println("Server: Packet received!");
	    System.out.println("From host: " + receivePacket.getAddress());
	    System.out.println("Host port: " + receivePacket.getPort());
	    System.out.println("Length: " + len);
	    //Forms a String from the byte array to print
	    String received = new String(dataReceived,0,len);  
	    System.out.println("Containing: " + received );
	}
	
	//Prints the information contained within the echoed packet.
	public void processEcho(){
		
		len = sendPacket.getLength();
		System.out.println( "Server: Sending packet...");
	    System.out.println("To host: " + sendPacket.getAddress());
	    System.out.println("Destination host port: " + sendPacket.getPort()); 
	    System.out.println("Length: " + len);
	    System.out.print("Containing: ");
	    System.out.println(new String(sendPacket.getData(),0,len));
	}
		
	//MAIN FUNCTION
	public static void main(String args[]){
		
		Server s = new Server();
		s.receiveAndEcho();
	}

}
