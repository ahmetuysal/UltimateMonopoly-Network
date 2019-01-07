import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Main {

	//static GameState sharedGameState = new GameState();

	public static void main(String[] args) {

		//sharedGameState.setPlayerIndexOfTheCurrentTurn(1);

		//Address
//		String multiCastAddress = "230.0.0.0";
//		final int multiCastPort = 52684;
//
//		//Create Socket
//		System.out.println("Create socket on address " + multiCastAddress + " and port " + multiCastPort + ".");
//		InetAddress group;
//		MulticastSocket s;
//		try {
//
//			group = InetAddress.getByName(multiCastAddress);
//			s = new MulticastSocket(multiCastPort);
//			s.joinGroup(group);
//			//Prepare Data
//			
//			String message = "Hello there!";
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(message);
//			byte[] data = baos.toByteArray();
//
//			//Send data
//			s.send(new DatagramPacket(data, data.length, group, multiCastPort));
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		initializeServer();

	}

	private static void initializeServer() {

		GameServer gameServer = new GameServer(GameServer.DEFAULT_SERVER_PORT);

	}

	//	public synchronized static GameState getSharedGameState() {
	//		return sharedGameState;
	//	}
	//	
	//	public synchronized static void setSharedGameState(GameState newGameState) {
	//		sharedGameState.setContent(newGameState.getContent());
	//		sharedGameState.setPlayerIndexOfTheCurrentTurn(newGameState.getPlayerIndexOfTheCurrentTurn());
	//		sharedGameState.setClientIndex(newGameState.getClientIndex());
	//		System.out.println("Server has changed its sharedGameState");
	//	}

}
