package model.server;

public class RunServerMain {
	
    public static void main(String[] args) 
    {   
        Server server = new MySerialServer(); 
        CacheManager cacheManager = new FileCacheManager();
        MyClientHandler clientHandler = new MyClientHandler(cacheManager);
        server.open(6400 ,new ClientHandlerPath(clientHandler));
    }
}