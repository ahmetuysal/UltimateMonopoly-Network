import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import domain.gamestate.GameState;

public class GameServerThread extends Thread {
	
	protected ObjectInputStream is;
    //protected ObjectOutputStream os;
    protected Socket s;
    protected GameServer gameServer;
    
    /**
     * Creates a server thread on the input socket
     * @param s input socket to create a thread on
     */
    public GameServerThread(Socket s, GameServer gameServer)
    {
        this.s = s;
        this.gameServer = gameServer;
        start();
    }
    
    /**
     * The server thread, runs always
     */
    public void run()
    {
        try
        {
            is = new ObjectInputStream(s.getInputStream());     
        }
        catch (IOException e)
        {
            System.err.println("GameServer Thread. Run. IO error in GameServer thread");
        }

        try
        {
            Object object = null;
            try {
                System.out.println("is run waiting");
                object = is.readObject();
                System.out.println("is run once");
            } catch (ClassNotFoundException e) {
                System.out.println("read Object calismadi");
                e.printStackTrace();
            }
            
            GameState incomingGameState = ((GameState)object);
            
            while (true)
            {

                System.out.println("----SEND-START------->>-----------");
                System.out.println(incomingGameState.getPlayers().size());
                ServerHost.getInstance().setSharedGameState(incomingGameState);
                gameServer.broadcastTheObject(ServerHost.getInstance().getSharedGameState(),s);
                System.out.println("----SEND-OVER-------<<----------");


                try {
                    object = is.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                
                incomingGameState = ((GameState)object);
            }
            
        }
        catch (IOException e)
        {
            System.err.println("GameStateServer Thread. Run. IO Error/ Client terminated abruptly");
        }
        catch (NullPointerException e)
        {
            System.err.println("GameStateServer Thread. Run.Client Closed");
        } finally
        {
            gameServer.removeConnection(s);
            try
            {
                System.out.println("Closing the connection");
                if (is != null)
                {
                    is.close();
                    System.err.println(" Socket Input Stream Closed");
                }
                if (s != null)
                {
                    s.close();
                    System.err.println("Socket Closed");
                }

            }
            catch (IOException ie)
            {
                System.err.println("Socket Close Error");
            }
        } //end finally
    }

}
