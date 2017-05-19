import java.util.ArrayList;
import java.util.Collections;

import org.kociemba.twophase.Search;
import org.kociemba.twophase.Tools;


public class Lobby {

	private String name;
	private ArrayList<Player> players;
	int maxid;
	
	public Lobby(String name) {
		this.name = name;
		maxid = 0;
		players = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean addPlayer(Player p) {
		if(playerHasName(p.getName())) {
			return false;
		}
		++maxid;
		players.add(p);
		p.setLobby(this);
		return true;
	}
	
	public void removePlayer(String n) {
		int i = 0;
		for(Player p : players) {
			if(p.getName().equals(n)) {
				players.remove(i);
				p.sendMessage("REMOVE");
				return;
			}
			++i;
		}
	}
	
	private boolean playerHasName(String n) {
		for(Player p : players) {
			if(p.getName().equals(n)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void updatePlayers() {
		Collections.sort(players);
		System.out.println("updating " + players.size() + " players");
		for(Player p : players) {
			p.sendMessage("MU");
			for(Player q : players) {
				p.sendMessage(q.getName() + " (avg: " + String.format("%.3f", (float) q.getAverage() / 1000) + ")");
			}
			p.sendMessage(".");
		}
	}
	
	public void newScramble() {
		String scramble = Search.solution(Tools.randomCube(), 25, 5, false);
		for(Player p : players) {
			p.sendMessage("NS");
			p.sendMessage(scramble);
			p.sendMessage(".");
		}
		for(Player p : players) {
			p.sendMessage("R");
			p.sendMessage(".");
		}
	}
	
	public void checkReady() {
		for(Player p : players) {
			if(!p.getReady()) {
				return;
			}
		}
		newScramble();
	}

}
