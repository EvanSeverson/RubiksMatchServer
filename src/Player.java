import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.sound.sampled.Line;


public class Player implements Comparable<Player>{
	
	private String name;
	private int id;
	
	private ArrayList<Long> times;
	private double average = Double.MAX_VALUE;
	private double best = Double.MAX_VALUE;
	private Socket socket;
	private PrintStream out;
	private BufferedReader in;
	private Lobby lobby;
	private boolean ready;
	

	public Player(String name, int id, Socket socket) {
		this.name = name;
		times = new ArrayList<>();
		this.socket = socket;
		ready = false;
		try {
			out = new PrintStream(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(inputRunnable).start();
	}
	
	Runnable inputRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				String line;
				while((line = in.readLine()) != null) {
					if(line.equals("TU")) {
						long time = Long.parseLong(in.readLine());
						System.out.println(time);
						in.readLine();
						addTime(time);
						lobby.updatePlayers();
					}
					if(line.equals("READY")) {
						ready = in.readLine().equals("1");
						lobby.checkReady();
					}
				}
			} catch (IOException e) {
			}
			lobby.removePlayer(name);
			lobby.updatePlayers();
			
		}
	};
	
	public String getName() {
		return name;
	}
	
	public double[] getTimes() {
		double[] timesarr = new double[times.size()]; 
		for(int i = 0; i < timesarr.length; i++) {
			timesarr[i] = times.get(i);
		}
		return timesarr;
	}
	
	public void setLobby(Lobby lobby) {
		this.lobby = lobby;
	}
	
	public Lobby getLobby() {
		return lobby;
	}
	
	public void addTime(long time) {
		times.add(time);
		average = 0;
		for(int i = 0; i < times.size(); ++i) {
			average += times.get(i);
		}
		average /= times.size();
		if(time < best) {
			best = time;
		}
	}
	
	public double getAverage() {
		return average;
	}

	@Override
	public int compareTo(Player o) {
		if(average < o.average) {
			return -1;
		}
		if(average > o.average) {
			return 1;
		}
		if(best < o.best) {
			return -1;
		}
		if(best > o.best) {
			return 1;
		}
		return 0;
	}
	
	public void sendMessage(String s) {
		out.println(s);
	}
	
	public boolean getReady() {
		return ready;
	}

}
