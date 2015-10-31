/**
* Create new threads that listens on respective ports of a TCP based server
* Qijia (Michael) Jin
* @version 1.0
*
Copyright (C) 2015  Qijia (Michael) Jin

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import java.net.*;
import java.io.*;
import java.util.*;

public class PortAllocation implements Runnable {

	private boolean ACTIVE;
	private ServerSocket SERVER_SOCKET;
	private SocketAddress SERVER_ADDRESS;
	private Integer SOCKET_PORT_NUMBER;
	private Socket SOCKET;
	
	public PortAllocation (Integer spn, List<PortAllocation> PORT_LIST) {
		this.ACTIVE = true;
		this.SOCKET_PORT_NUMBER = spn;
		synchronized (PORT_LIST) {
			PORT_LIST.add(this);
		}
	}
	
	public void run () {
		try {
			this.SERVER_ADDRESS = new InetSocketAddress(InetAddress.getLocalHost(), this.SOCKET_PORT_NUMBER);		//should return localhost
			try {
				this.SERVER_SOCKET = new ServerSocket();
				try {
					this.SERVER_SOCKET.bind(this.SERVER_ADDRESS);
					try {
						System.out.println(InetAddress.getLocalHost());
						System.out.println("Port open at: " + this.SOCKET_PORT_NUMBER);
						this.SOCKET = this.SERVER_SOCKET.accept();
						System.out.println("New Socket Connection has been forked on: " + SOCKET_PORT_NUMBER);
						DataInputStream in = new DataInputStream(this.SOCKET.getInputStream());
						DataOutputStream out = new DataOutputStream(this.SOCKET.getOutputStream());
						while (this.ACTIVE) {
							//do something
						}
						out.close();
						return;
					}
					catch (SocketException se) {
						System.out.println("Socket Exception: " + se.getMessage());
						return;
					}
					catch (IOException ie) {
						System.out.println("I/O Exception: " + ie.getMessage());
						return;
					}
					
					//catch (InterruptedException ie) {}
				}
				catch (IOException ie) {
					System.out.println("I/O Exception: " + ie.getMessage());
					return;
				}
				
			}
			catch (IOException ie) {
				System.out.println("I/O Exception: " + ie.getMessage());
				return;
			}
		}
		catch (UnknownHostException uhe) {
						System.out.println("Unknown Host Exception: " + uhe.getMessage());
						return;
		}
					
	}
}