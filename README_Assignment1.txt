Alex Hoecht
100933730

Sysc 3303
Assignment 1

Files:
	Client.java (Creates the initial read/write requests and sends them to the host as packets. Then the client
			waits until the host sends a new packet back. The packet is then processed and a new packet
			is sent.)

	IntermediateHost.java (Transfers and processes the messages contained in the packets between the client and 
				server.)
	
	Server.java (Receives a packet from the intermediate host and processes the request. An ACK message is then
			packaged and send back to the client, throug the host.)

To run program (Java Application):
	1)Complie Server.java (Start Server)
	2)Compile IntermediateHost.java (Start Host)
	3)Complie Client.java (Start Client)

To reset program:
	Terminate all running programs in this project, then repeat steps to run program.


	
