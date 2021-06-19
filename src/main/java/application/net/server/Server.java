package application.net.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//siccome voglio sia registrarmi che autenticarmi dal client devo definire un protocollo

public class Server implements Runnable
{
	private ServerSocket server;
	private ExecutorService executor;
	
	
	public void startServer()
	{
		try 
		{
			server= new ServerSocket(8000);
			executor=Executors.newCachedThreadPool();
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR WHILE SERVER CREATION (PORT CAN BE ALREADY USED)");
		}
		Thread t= new Thread(this);
		t.start();
	}
	@Override
	public void run() 
	{
		
		while(!Thread.currentThread().isInterrupted())
		{
			try 
			{
				System.out.println("[SERVER] I'm waiting for new connections...");
				Socket socket= server.accept();
				System.out.println("[SERVER] Client with Inet Address: "+ socket.getInetAddress()+" connected...");
				
				MessagesHandler m= new MessagesHandler(socket);
				executor.submit(m);
			} catch (IOException e) {
				System.out.println("ERROR WHILE CONNECTION WAITING");
			}
		}
		
		
	}
	
	

}
