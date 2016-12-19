package gameengine.network.server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import gameengine.network.ServerMessage;
import gameengine.network.TcpCommand;
import xml.XMLSerializer;

/**
 * This class establishes TCP connection and listens to client side
 * for tasks to do.
 * 
 *  * @author Titas Skrebe
 * 
 * Edited by Eric Song, Ray Song
 * 
 */
class TcpConnection implements Runnable{

	private ServerMain main;
	private Socket socket;
	private XMLSerializer serializer;
//	private TcpCommand myCommands;
	
	TcpConnection(ServerMain main, Socket socket) {
		this.main = main;
		this.socket = socket;
//		myCommands = TcpCommand.GET_ID; //initialized to avoid NPE
		serializer = new XMLSerializer();
	}
	
	@Override
	public void run() {
		try(ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
			while(true){
				String msg = (String)ois.readObject();
				ServerMessage sm;
				try {
					sm = serializer.getServerMessageFromString(msg);
				} catch (Exception e) {
					continue;
				}

				switch(sm.getMessage()){
					case GET_ID:
						oos.writeLong(main.getId());
						break;
					case SEND_COMMAND:
						main.readCommand(sm.getCommand(),(int)sm.getId(),sm.getCharIdx());
						break;
					case GET_ID_IP_PORT: 
						String ipString = socket.getInetAddress().getHostName();
						InetAddress clientIp = InetAddress.getByName(ipString);
						System.err.println(ipString + " " + clientIp);
						main.addressBook(clientIp, sm.getPort());
						break;
					case REMOVE_CHARACTER:
						main.removeCharacter(sm.getId());
						break;
					case PAUSE:
						main.pause();
						break;
					case RESTART:
						main.restart();
						break;
					case SERVER_THREAD_SHUTDOWN:
						main.shutdownServerThread();
						break;
					default:
						break;
				}
				oos.flush();
				
			}
		}catch(IOException | ClassNotFoundException e){
		}
	}

}
