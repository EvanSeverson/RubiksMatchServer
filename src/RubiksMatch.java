import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class RubiksMatch {
	
	private static ArrayList<Lobby> lobbies;
	
	public static void main(String[] args) {
		try {
			lobbies = new ArrayList<>();
			ServerSocket ss = new ServerSocket(8080);
			while(true){
				Socket s = ss.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintStream out = new PrintStream(s.getOutputStream());
				if(!in.readLine().equals("suh dude")) {
					s.close();
					continue;
				}
				String lobbyName = in.readLine();
				String name = in.readLine();
				Lobby lobby = null;
				for(Lobby l : lobbies) {
					if(l.getName().equals(lobbyName)) {
						lobby = l;
						break;
					}
				}
				if(lobby == null) {
					lobby = new Lobby(lobbyName);
				}
				lobbies.add(lobby);
				if(lobby.addPlayer(new Player(name, lobby.maxid + 1, s))) {
					out.println("SUCCESS");
				} else {
					out.println("ERROR 1");
					continue;
				}
				lobby.updatePlayers();
				for(Player p : lobby.getPlayers()) {
					System.out.println(p.getName());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
