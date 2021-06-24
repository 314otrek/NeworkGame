import java.util.List;

public class Game implements  Runnable{

    public Client c1;
    public Client c2;


    public Game(Client c1, Client c2){
        this.c1=c1;
        this.c2= c2;
        c1.yourTable = new int[16];
        c1.enemyTable = new int[16];
        c2.enemyTable = new int[16];
        c2.yourTable = new int[16];
    }

    public void initialStart(){
//        c1.write("Enemy Board \n");
//        c1.write(displayEnemyTable(c1.enemyTable)); // PLAYER 1 I TABLICA PRZECIWNIKA
        c1.write("Your Board \n");
        c1.write(displayYourTable(c1.yourTable)); // PLAYER 1 I JEGO TABLICA
//        c2.write("Enemy Board \n");
//        c2.write(displayEnemyTable(c2.enemyTable)); // PLAYER2 I TABLICA RPZECIWNIKA
        c2.write("Your Board \n");
        c2.write(displayYourTable(c2.yourTable)); // PLAYER 2  I JEEGO TABLICA
    }
    public void settingShips(){
        boolean shipsOnBoard =false;
        boolean order = true;
        while(!shipsOnBoard){
            if(order){
                try {
                    putShipOnBoard(c1,c2);
                    order =false;
                } catch (Exceptions.WrongPositionException e) {
                    System.out.println("Something wrong with putting ship by 1st player");
                }

            }else{
                try {
                    putShipOnBoard(c2,c1);
                    order = true;
                } catch (Exceptions.WrongPositionException e) {
                    System.out.println("Something wrong with putting ship by 2nd player");
                }
            }
            if(c1.countOfShips==3 && c2.countOfShips==3){
                shipsOnBoard = true;
            }
        }
    }
    public void showTheBoards(){
        c1.write("Enemy Board \n");
       c1.write(displayEnemyTable(c1.enemyTable)); // PLAYER 1 I TABLICA PRZECIWNIKA
        c1.write("Your Board \n");
        c1.write(displayYourTable(c1.yourTable)); // PLAYER 1 I JEGO TABLICA
       c2.write("Enemy Board \n");
       c2.write(displayEnemyTable(c2.enemyTable)); // PLAYER2 I TABLICA RPZECIWNIKA
        c2.write("Your Board \n");
        c2.write(displayYourTable(c2.yourTable)); // PLAYER 2  I JEEGO TABLICA
    }
    public void closingClients(){
        c1.closeIt();
        c2.closeIt();
    }


    @Override
    public void run() {
        initialStart();
        settingShips();
        c1.write("\nShips have been moored");
        c2.write("\nships have benn moored");

        showTheBoards();
        gamePlayPart();
        closingClients();
        System.exit(0);

    }
    public void gamePlayPart(){
        boolean endOfGame = false;
        boolean order = true;
        boolean winner = false;
        while(!endOfGame){
            if(order){
                try{
                    attackTheShip(c1,c2);
                    order =false;
                }catch (Exceptions.WrongPositionException e){
                    System.out.println("Some error with your attack Sir");
                }
            }
            else{
                try{
                    attackTheShip(c2,c1);
                    order = true;
                }catch (Exceptions.WrongPositionException e){
                    System.out.println("Some error with your attack Sir");
                }
            }
            if(c1.countOfShips==0){
                endOfGame = true;
                winner = false; // wygral c2
                showTheBoards();
                winnerIs(c2,c1,winner);
            }
            if(c2.countOfShips ==0){
                endOfGame = true;
                winner = true; // wygrał c1
                showTheBoards();
                winnerIs(c1,c2,winner);
            }

        }

    }
    public void winnerIs(Client c1, Client c2, boolean winner){


        if(winner=true){
            c1.write("\nThe winner is " + c1.getName());
            c2.write("\nUnfortunately, today "+c2.getName()+" is loser");
        }
        if(winner=false)
        {
            c2.write("\n The winner is " + c2.getName());
            c1.write("\nUnfortunately, today "+c1.getName()+" is loser");
        }
    }

    public void attackTheShip(Client cl1, Client cl2) throws Exceptions.WrongPositionException {
        boolean shooted = false;
        while(!shooted) {
            try {
                cl2.write("Wait for enemy attack");
                cl1.write("Select 1 to 16 to find enemy ship");
                String place = cl1.read();
                acceptAblePlace(place);
                killingTheShip(place, cl1, cl2);
                shooted=true;
            }catch (Exceptions.WrongPositionException wpe){
                cl1.write("This field is aledy used or index is out of range \n Try another:");

            }
            cl1.write(displayEnemyTable(cl1.enemyTable));
            cl1.write(displayYourTable(cl1.yourTable));
        }

    }

    public void killingTheShip(String place, Client cl1,Client cl2){
        int field = Integer.parseInt(place);
        if(cl2.yourTable[field-1] ==1){
            cl1.write("Pufff u ve sunk enemy ship!");
            cl2.write("Opps one of your ships on position "+field+" get sunk!");
            cl1.enemyTable[field-1] = 2;
            cl2.yourTable[field-1] =  7;
            cl2.countOfShips--;
        }
        else {
            cl1.enemyTable[field-1]=6;
        }
    }



    private void putShipOnBoard(Client cl1,Client cl2) throws Exceptions.WrongPositionException {

        boolean marked = false;
        while (!marked){
            try {

                cl1.write("Select from 1 to 16 to put your ship on board:");
                cl2.write("Wait for enemy move");
                String place = cl1.read();
                acceptAblePlace(place);
                isAbleToPut(place, cl1, cl2);
                marked = true;

            } catch (Exceptions.WrongPositionException wpe) {
                cl1.write("This field is aledy used or index is out of range \n Try another:");
            }

        cl1.write(displayYourTable(cl1.yourTable));
    }

    }

    public void acceptAblePlace(String place) throws Exceptions.WrongPositionException {
        if(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16").contains(place))
        {}
        else
        {
            throw new Exceptions.WrongPositionException();
        }
    }


    public void isAbleToPut( String place,Client cl1,Client cl2) throws Exceptions.WrongPositionException {
        int position  = Integer.parseInt(place);
        if(cl1.yourTable[position-1] == 0){
            cl1.yourTable[position-1] = 1;//jedynka oznacza ustawianie statku na danym polu
            cl2.enemyTable[position-1] =1; // jedynka żeby ustawić ze w danym polu znajduje sie statek u przeciwnika
            cl1.countOfShips++;
        }else {
            throw new Exceptions.WrongPositionException();
        }

        }


    public String displayYourTable(int[] array){
        String table="";

        for (int i =1;i<=16;i++){
            String pole;
            if(array[i-1] == 1){
                pole= "8";
            }else if(array[i-1]==7){
                pole="*";
            }
            else{
                pole  = " ";
            }
            table+=String.format("%2s",pole);
            if(i%4==0){
                table+="\n-------------------\n";
            }
            else{
                table+=" | ";
            }
        }
        table+="\n";
        return  table;
    }
    public String displayEnemyTable(int[] array){
        String table="";

        for (int i =1;i<=16;i++){
            String pole;
            if(array[i-1] == 2) {
                pole = "*";
            }else if(array[i-1]==6){
                pole = " ";
            }
            else{
                pole  = "X";
            }
            table+=String.format("%2s",pole);
            if(i%4==0){
                table+="\n-------------------\n";
            }
            else{
                table+=" | ";
            }
        }
        table+="\n";
        return  table;
    }
}
