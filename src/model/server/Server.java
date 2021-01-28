package model.server;

public interface Server {
	public void open(int port, ClientHandler clientHandler);
	public void stop();
}