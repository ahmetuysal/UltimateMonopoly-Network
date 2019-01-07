import domain.Player;
import domain.gamestate.GameState;
import java.util.Comparator;
import java.util.List;

public class ServerHost {

	private static ServerHost uniqueInstance;

	private GameState sharedGameState;

	private boolean isPlayerAdded = false;

	public boolean getPlayerAdded() {
		return isPlayerAdded;
	}

	public void setPlayerAdded(boolean flag) {
		isPlayerAdded = flag;
	}

	private ServerHost() {
		sharedGameState = new GameState();
		sharedGameState.setLocalIp("trivial");
	}

	public static synchronized ServerHost getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new ServerHost();
		}
		return uniqueInstance;
	}

	public GameState getSharedGameState() {
		return sharedGameState;
	}

	public void setSharedGameState(GameState newGameState) {
		List<Player> players = newGameState.getPlayers();
		if (sharedGameState.getPlayers() == null || sharedGameState.getPlayers().isEmpty()) {
			sharedGameState.setPlayers(newGameState.getPlayers());
			isPlayerAdded = true;
		} else {
			for (Player player : players) {
				int index = sharedGameState.getPlayers().indexOf(player);
				if (index < 0) {
					sharedGameState.getPlayers().add(player);
					isPlayerAdded = true;
				} else {
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
			players.sort(new Comparator<Player>() {
				@Override
				public int compare(Player o1, Player o2) {
					return o1.getNickName().compareTo(o2.getNickName());
				}
			});
		}

		sharedGameState.setCup(newGameState.getCup());
		sharedGameState.setLocalIp(newGameState.getLocalIp());
		sharedGameState.setCurrentPlayerIndex(newGameState.getCurrentPlayerIndex());
		System.out.println("Server has changed its sharedGameState to " + newGameState.getPlayers().toString()
				+ " from " + newGameState.getLocalIp());
	}

}
