import domain.Player;
import domain.gamestate.GameState;

import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ServerHost {

	private static ServerHost uniqueInstance;

	private GameState sharedGameState;


	private ServerHost() {
		sharedGameState = new GameState();
		sharedGameState.setLocalIp("trivial");

	}

	public static synchronized ServerHost getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new ServerHost();
		}
		return uniqueInstance;
	}

	public GameState getSharedGameState() {
		return sharedGameState;
	}

	public void setSharedGameState(GameState newGameState) {


		List<Player> players = newGameState.getPlayers();
		if (sharedGameState.getPlayers() == null || sharedGameState.getPlayers().isEmpty() )
			sharedGameState.setPlayers(newGameState.getPlayers());
		else {
			for (Player player : players) {
				int index = sharedGameState.getPlayers().indexOf(player);
				if (index < 0) {
					sharedGameState.getPlayers().add(player);
				}
				else {
					Player playerInList = sharedGameState.getPlayers().get(index);
					playerInList.setTotalMoney(player.getTotalMoney());
					playerInList.setReverseDirection(player.isReverseDirection());
					if (player.isInJail()) {
						playerInList.goToJail();
					}
					playerInList.setJailTime(player.getJailTime());
					playerInList.setProperties(player.getProperties());
					playerInList.setCards(player.getCards());
					playerInList.getToken().setLocation(player.getToken().getLocation());
					playerInList.setLocalIp(player.getLocalIp());
				}
			}
		}


		System.out.println("Current player index:"+newGameState.getCurrentPlayerIndex());
		sharedGameState.setCup(newGameState.getCup());
		sharedGameState.setLocalIp(newGameState.getLocalIp());
		sharedGameState.setCurrentPlayerIndex(newGameState.getCurrentPlayerIndex());
		System.out.println("Server has changed its sharedGameState to " + sharedGameState);
	}


}
