import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    InputStream in;
    OutputStream out;
    String name;
    int bombs;
    int[] yourTable;
    int[] enemyTable;
    int countOfShips;

    Client(Socket socket) throws IOException {
        this.socket = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
        initialSetup();
        this.yourTable = new int[16];
        this.enemyTable = new int[16];
        this.countOfShips=0;

    }

    private void initialSetup() {
        write("What is your nickname: ");
        name = read();
        write("Hi "+name);
    }

    public void write(String str) {
        try {
            out.write((str + "&").getBytes());
            out.flush();//wysylamy na serwer

        } catch (IOException e) {
            System.out.println("Some error with writing!");
        }
    }

    public String getName() {
        return name;
    }

    public String read() {
        String str = "";
        boolean end = false;
        while (!end) {
            try {

                if (in.available() > 0) {
                    int bajt;
                    while ((bajt = in.read()) != 38) {
                        str += (char) bajt;
                    }
                    end = true;
                }

            } catch (IOException e) {
                System.out.println("Error with reading methods");
            }
        }

        return str;
    }

    public void closeIt() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error with closing socket");
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        Thread read = new Thread(() ->
        {
            while (true) {
                try {
                    if (socket.getInputStream().available() > 0) {
                        String str = "";
                        int bajt;
                        while ((bajt = socket.getInputStream().read()) != 38) {
                            str += (char) bajt;
                        }
                        System.out.println(str);
                    }

                } catch (IOException e) {
                    System.out.println("Error with reading in main method");
                }
            }

        });
        read.start();

        Thread write  = new Thread( () ->
        {
            Scanner scan  = new Scanner(System.in);
            while (true){
                String str = scan.nextLine();
                try{
                    socket.getOutputStream().write((str+"&").getBytes());
                    socket.getOutputStream().flush();
                }catch (IOException e){
                    System.out.println("Error with writting in main method");
                }
            }
        });
        write.start();
    }
}