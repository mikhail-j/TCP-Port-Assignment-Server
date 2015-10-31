/**
* Main Class for a TCP based server
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
import java.util.*;
import java.io.*;
import java.net.*;

public class MainDataServer {
	private static int DEFAULT_PORT = 2000;						//listen for requests on this port
	private static ServerSocket SERVER_SOCKET;
	private static SocketAddress SERVER_ADDRESS;
	private static boolean isListening;							//should this program still check for pending requests

	public static void main(String[] args) {
		List<PortAllocation> PORT_LIST = Collections.synchronizedList(new ArrayList<PortAllocation>());		//probably want another thread to read in the data
		isListening = true;
		try {
			SERVER_ADDRESS = new InetSocketAddress(InetAddress.getLocalHost(), DEFAULT_PORT);		//should return localhost

			try {
				SERVER_SOCKET = new ServerSocket();
				try {
					SERVER_SOCKET.setReuseAddress(true);			//keep listening to this port
					SERVER_SOCKET.bind(SERVER_ADDRESS);
					try {
						System.out.println(InetAddress.getLocalHost());
						System.out.println("Port open at: " + DEFAULT_PORT);
						while (isListening) {
							synchronized (PORT_LIST) {
								Socket tmp = SERVER_SOCKET.accept();
								BufferedReader buffered_in = new BufferedReader (new InputStreamReader(tmp.getInputStream()));
								BufferedWriter buffered_out = new BufferedWriter (new OutputStreamWriter(tmp.getOutputStream()));
								Integer NEW_PORT = new Integer(PORT_LIST.size() + DEFAULT_PORT + 1);
								
								buffered_out.append(NEW_PORT.toString() + System.getProperty("line.separator"));			//new port address
								buffered_out.flush();
								
								System.out.println(buffered_in.readLine());
								System.out.println("" + NEW_PORT);
								
								new Thread(new PortAllocation(NEW_PORT, PORT_LIST)).start();
								System.out.println("Number of existing sockets: " + PORT_LIST.size());
							}
						}
					}
					catch (SocketException se) {
						System.out.println("Socket Exception: " + se.getMessage());
						return;
					}
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