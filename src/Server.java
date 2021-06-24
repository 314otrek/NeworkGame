import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Socket socket=null;

        while (true){
            System.out.println("waiting for player1 ...");
            socket =serverSocket.accept();
            Client p1 = new Client(socket);
            System.out.println("Hi " + p1.getName()+", waiting for 2nd Player");

            socket = serverSocket.accept();
            Client p2 = new Client(socket);
            System.out.println("Hi "+ p2.getName()+"\n Okey we are ready to play :)");

            Game game = new Game(p1,p2);
            game.run();
            p1.closeIt();
            p2.closeIt();
        }

    }
}
