import domain.gamestate.GameState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

public class GameServer {

	private ServerSocket serverSocket;
	public static final int DEFAULT_SERVER_PORT = 4477;
	private Hashtable allObjectOutputStreams = new Hashtable();
	private HashMap<Socket, ObjectOutputStream> allOSs = new HashMap<>();

	/**
	 * The constructor of GameServer
	 * 
	 * @param serverPort
	 *            The chosen port for the data flow
	 */
	public GameServer(int serverPort) {
		try {
			serverSocket = new ServerSocket(serverPort);
			System.out.println("Opened up a server socket on " + Inet4Address.getLocalHost());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("GameServer class.Constructor exception on opening a server socket");
		}
		while (true) {
			ListenAndAccept();
		}

	}

	/**
	 * Listens to the line and starts a connection on receiving a request from the
	 * client The connection is started and initiated as a GameServerThread object
	 */
	private void ListenAndAccept() {
		Socket s;
		try {
			s = serverSocket.accept();
			ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
			allObjectOutputStreams.put(s, os);
			allOSs.put(s, os);
			System.out.println("A new player connected with a client on the address of " + s.getRemoteSocketAddress());
			new GameServerThread(s, this);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("GameServer Class.Connection establishment error inside listen and accept function");
		}
	}

	// Enumeration getOutputStreams(){
	// return allObjectOutputStreams.elements();
	// }

	public void broadcastTheObject(GameState outgoingGameState, Socket s2) {
		synchronized (allOSs) {
			for (Socket s : allOSs.keySet()) {
				System.out.println("allOSs size " + allOSs.size());
				System.out.println("Current socket " + s);
				System.out.println("My socket " + s2);
				System.out.println("Are they equal: " + (s == s2));
				if (ServerHost.getInstance().getPlayerAdded() || s != s2) {
					ObjectOutputStream os = allOSs.get(s);
					try {

						System.out.println("Gonderdigim adam: " + s);
						System.out.println("Gonderdigim sey: " + outgoingGameState);

						os.writeObject(outgoingGameState);
						os.flush();
						os.reset();

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		ServerHost.getInstance().setPlayerAdded(false);
	}

	public void removeConnection(Socket s) {

		synchronized (allObjectOutputStreams) {
			System.out.println("Removing connection to " + s);
			allObjectOutputStreams.remove(s);
			try {
				s.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}

	}

}
