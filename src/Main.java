public class Main {

	public static void main(String[] args) {
		initializeServer();
	}

	private static void initializeServer() {
		new GameServer(GameServer.DEFAULT_SERVER_PORT);
	}
}
