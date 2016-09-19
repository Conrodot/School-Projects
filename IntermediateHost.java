
/*
 * 
 */

//IMPORTS
import java.io.*;
import java.net.*;

public class IntermediateHost {

	//INSTANCE VARIABLES
	DatagramSocket port23Socket;
	DatagramPacket port23Packet;
	DatagramSocket SNRSocket;
	DatagramPacket SNRPacket;
	DatagramSocket CHSocket;
	DatagramPacket CHPacket;
	
	
	String request = "";
	byte[] port23data = new byte[50];
	byte[] SNRdata = new byte[4];
	int len;
	
	
	public IntermediateHost(){
		
		try{
		//Create the DatagramSockets to receive port 23,
		//and to send and receive to any general port.
		port23Socket = new DatagramSocket(23);
		SNRSocket = new DatagramSocket();
		}
		catch(SocketException se){
			se.printStackTrace();
			System.exit(1);
		} 
	}
	
	public void hostAlgorithm(){
		
		while(true){
			
		
		port23Packet = new DatagramPacket(port23data,port23data.length);
		
		//Wait for packet to be received.
		System.out.println("Host: Waiting for request...");
		try{
			System.out.println("waiting.................");
			port23Socket.receive(port23Packet);
		}
		catch(IOException e){
			System.out.print("IO Exception: High");
		    System.out.println("Receive Socket Timed Out :(" + e);
		    e.printStackTrace();
		    System.exit(1);
		}
		
		//Print the request from port 23 as a string and as bytes
		printPort23();
	    
		//Send the packet on the SNRSocket to port 69, on this computer
	    SNRPacket = new DatagramPacket(port23data, port23data.length,
										port23Packet.getAddress(),69);
	    try{
			SNRSocket.send(SNRPacket);
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Host: Packet sent to server :)");
		
		//Wait to receive a response from the server
		SNRPacket = new DatagramPacket(SNRdata,4);
		try{
			SNRSocket.receive(SNRPacket);
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		//Print the received packet to the console
		printReceived();
		
		//SENDING the data from the SNRPacket to the client
		try{
			CHSocket = new DatagramSocket();
		}
		catch(SocketException se){
			se.printStackTrace();
			System.exit(1);
		}
		CHPacket = new DatagramPacket(SNRdata, SNRdata.length,
				port23Packet.getAddress(),
				port23Packet.getPort());
		
		//Print the information being sent
		printCH();
		
		try{
			CHSocket.send(CHPacket);
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Host: Packet sent to client :)");
		}
	}
	
	public String getRequest(byte[] b){
		//Its a read
		if(b[1] == 1){
			request = "Read";
		}
		//Its a write
		if(b[1] == 2){
			request = "Write";
		}
		if(b[1] == 0){
			request = "INVALID";
		}
		return request;
	}
	
	//Prints information about the received port23 packet
	public void printPort23(){
			
		//Is the request a read or write
		String s = getRequest(port23data);
		//Set length
		len = port23Packet.getLength();
		System.out.println("Host: Packet received!");
		System.out.println("From client: " + port23Packet.getAddress());
		System.out.println("Client port: " + port23Packet.getPort());
		System.out.println("Length: " + len); 
		System.out.println("Containing a " + s + " request." );
		}
	
	//Prints information about the received SNR packet
	public void printReceived(){
		//Set length
		len = SNRPacket.getLength();
		System.out.println("Client: Packet received:");
	    System.out.println("From host: " + SNRPacket.getAddress());
	    System.out.println("Host port: " + SNRPacket.getPort());
	    System.out.println("Length: " + len);
	}
	
	public void printCH(){
		
		len = CHPacket.getLength();
		System.out.println( "Host: Sending packet...");
	    System.out.println("To client: " + CHPacket.getAddress());
	    System.out.println("Destination host port: " + CHPacket.getPort()); 
	    System.out.println("Length: " + len);
	    System.out.print("Containing: ");
	    System.out.println(new String(CHPacket.getData(),0,len));
	}
	
	public static void main(String args[]){
		IntermediateHost iH = new IntermediateHost();
		iH.hostAlgorithm();
	}
}

