package rq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import static rq.LocalReDiQuadri.INITIAL_LIVES;

public class Client {
    static RemoteReDiQuadri remote = new RemoteReDiQuadri("localhost", 10000);
    static User user = null;
    static String nickname = null;
    static String lobbyId;
    public static final int MAX_LOBBY_SIZE = 5;
    static Scanner scanner = new Scanner(System.in);
    static Timer timerClient;

    public static void main(String[] args) throws ConflictException, MissingException {

        timerClient = new Timer();
        timerClient.schedule(new TimerTask() {
            @Override
            public void run() {
                if(nickname != null) {
                    try {
                        remote.setUserLastSignal(nickname);
                    } catch (Exception e) {
                        System.exit(0);
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 300);

        System.out.println(
                "  _____            _ _    ____                  _      _ \n" +
                " |  __ \\          | (_)  / __ \\                | |    (_)\n" +
                " | |__) |___    __| |_  | |  | |_   _  __ _  __| |_ __ _ \n" +
                " |  _  // _ \\  / _` | | | |  | | | | |/ _` |/ _` | '__| |\n" +
                " | | \\ \\  __/ | (_| | | | |__| | |_| | (_| | (_| | |  | |\n" +
                " |_|  \\_\\___|  \\__,_|_|  \\___\\_\\\\__,_|\\__,_|\\__,_|_|  |_|\n" +
                "                                                         \n" +
                "                                                         ");

        boolean logged = false;
        while (!logged) {
            System.out.println("Inserisci il tuo nickname: ");
            nickname = scanner.nextLine();
            if(!nickname.isBlank()){
                try {
                    user = new User(nickname);
                    remote.register(user);
                    logged = true;
                } catch (ConflictException e) {
                    System.out.println("Nickname gia' presente, scegline un altro! \n");
                }
            }
        }
        System.out.println("Benvenuto " + nickname +"\n");
        boolean playNextGame = true;
        while (playNextGame) {
            boolean lobbyScelta = false;
            try {
                while (!lobbyScelta) {
                    List<Lobby> lobbiesAvailable = remote.getLobbiesAvailable();
                    lobbyId = null;
                    if(lobbiesAvailable.size() == 0) {
                        //creazione nuova lobby
                        boolean lobbyCreated = false;

                        while (!lobbyCreated) {
                            System.out.println("Non ci sono lobby disponibili, digita c per creare una nuova lobby oppure a per aggiornare le lobby disponibili.");
                            lobbyId = scanner.nextLine();
                            if(lobbyId.equals("c")){
                                System.out.println("\nNome per la nuova lobby: ");
                                lobbyId = scanner.nextLine();
                                if(!lobbyId.isBlank()) {
                                    List<User> userList = new ArrayList<>();
                                    Lobby l = new Lobby(lobbyId, userList);
                                    try {
                                        remote.createLobby(l);
                                        //remote.addUserInLobby(lobbyId, user);
                                        System.out.println("lobby: " + lobbyId+" creata con successo!\n");
                                        //System.out.println("Attendi l'arrivo degli altri partecipanti...");
                                        lobbyCreated = true;
                                        //remote.setUserLastSignal(nickname);
                                    } catch (ConflictException e) {
                                        System.out.println("Lobby gia' presente, scegli un altro nome! \n");
                                    }
                                } else {
                                    System.out.println("Errore input, digita c per creare una nuova lobby\n");
                                }

                            } else if (lobbyId.equals("a")) {
                                lobbiesAvailable = remote.getLobbiesAvailable();
                                if(lobbiesAvailable.size()!=0)
                                    lobbyCreated = true;
                            } else {
                                System.out.println("Errore input, digita c per creare una nuova lobby\n");
                            }
                        }
                    }

                    while(!lobbyScelta) {
                        lobbiesAvailable = remote.getLobbiesAvailable();
                        System.out.println("Elenco delle Lobby disponibili: ");
                        for (Lobby lobby : lobbiesAvailable) {
                            System.out.println(lobby.id + " [" + lobby.users.size() + "/" + MAX_LOBBY_SIZE + "]");
                        }
                        System.out.println("\nDigita il nome di una lobby per entarci, digita c per crearne una nuova oppure digita a per aggiornare le lobby disponibili ");
                        lobbyId = scanner.nextLine();

                        //creazione nuova lobby
                        if (lobbyId.equals("c")) {
                            boolean lobbyCreated = false;
                            while (!lobbyCreated) {
                                System.out.println("\nNome per la nuova lobby: ");
                                lobbyId = scanner.nextLine();
                                if (!lobbyId.isBlank()) {
                                    List<User> userList = new ArrayList<>();
                                    user.setLives(INITIAL_LIVES);
                                    Lobby l = new Lobby(lobbyId, userList);
                                    //remote.setUserLastSignal(nickname);
                                    try {
                                        remote.createLobby(l);
                                        remote.addUserInLobby(lobbyId, user);
                                        System.out.println("lobby: " + lobbyId + " creata con successo!\n");
                                        System.out.println("Attendi l'arrivo degli altri partecipanti...");
                                        lobbyCreated = true;
                                        lobbyScelta = true;
                                    } catch (ConflictException e) {
                                        System.out.println("Lobby gia' presente, scegli un altro nome! \n");
                                    } catch (MissingException e) {
                                        System.out.println("Non e' possibile entrare nella lobby selezionata \n");
                                    }
                                }
                            }
                        } else if (lobbyId.equals("a")) {
                            lobbiesAvailable = remote.getLobbiesAvailable();
                        } else {
                            //entra in una lobby esistente
                            boolean correctLobby = false;
                            while (!correctLobby) {
                                try {
                                    remote.addUserInLobby(lobbyId, user);
                                    correctLobby = true;
                                    //remote.setUserLastSignal(nickname);

                                } catch (MissingException e) {
                                    System.out.println("Non e' possibile entrare nella lobby selezionata \n");
                                    System.out.println("Inserisci il nome di una lobby esistente");
                                    lobbyId = scanner.nextLine();
                                }
                            }
                            System.out.println("Benvenuto nella lobby " + lobbyId + "!\n");
                            System.out.println("Attendi l'arrivo degli altri partecipanti...");
                            lobbyScelta = true;
                        }
                    }
                }

                //attesa partecipanti
                boolean readyToStart = false;
                boolean first3 = true;
                boolean first4 = true;
                while (!readyToStart){
                    Thread.sleep(1000);
                    //remote.setUserLastSignal(nickname);
                    //se sono il creatore della lobby
                    if(nickname.equals(remote.getLobby(lobbyId).users.get(0).getNickname())) {
                        if(remote.getLobby(lobbyId).users.size() == 3 && first3){
                            first3 = false;
                            //remote.setUserLastSignal(nickname);
                            System.out.println("Sono presenti 3 giocatori nella lobby:\n premi 1 per iniziare la partita\n premi un tasto qualsiasi per attendere altri giocatori.");
                            if(getInputAdmin().equals("1")) {
                                readyToStart = true;
                                remote.setLobbyUnavailable(lobbyId);
                            }
                            System.out.println("In attesa di altri giocatori...");
                        }
                        if(remote.getLobby(lobbyId).users.size() == 4 && first4 && !readyToStart){
                            first4 = false;
                            //remote.setUserLastSignal(nickname);
                            System.out.println("Sono presenti 4 giocatori nella lobby\n premi 1 per iniziare la partita\n premi un tasto qualsiasi per attendere altri giocatori.");
                            if(getInputAdmin().equals("1")) {
                                readyToStart = true;
                                remote.setLobbyUnavailable(lobbyId);
                            }
                            System.out.println("In attesa di altri giocatori...");
                        }
                    }
                    if(!remote.getLobby(lobbyId).available){
                        readyToStart = true;
                    }
                }
                System.out.println("La partita sta per iniziare!");
                int round = 1;
                boolean gameFinished = false;
                boolean userExit = false;

                System.out.println("\nINIZIO PARTITA!\nQueste sono le regole: \n-Ogni giocatore inizia la partita con 5 vite, \n ad ogni round ogni giocatore deve scegliere un numero da 0 a 100.\n Il sistema calcola la media dei numeri inseriti da tutti i giocatori,\n" +
                        " la moltiplica per 0.8, chi si avvicina di piu' al numero calcolato vince e gli altri perdono una vita.\n");
                if(remote.getLobby(lobbyId).users.size() == 4) {
                    System.out.println("Regola aggiuntiva 4 giocatori: \n -Se due giocatori scelgono lo stesso numero gli viene detratta una vita.");
                } else if(remote.getLobby(lobbyId).users.size() == 3) {
                    System.out.println("Regola aggiuntiva 4 giocatori: \n -Se due giocatori scelgono lo stesso numero gli viene detratta una vita.");

                    System.out.println("Regola aggiuntiva 3 giocatori: \n -Scegliere il numero esatto fa perdere 2 vite agli altri giocatori.");
                }
                while (!gameFinished) {
                    remote.removeLosersFromLobby(lobbyId);
                    //remote.setUserLastSignal(nickname);
                    System.out.println("\nROUND " + round);
                    Thread.sleep(500);

                    remote.clearWinner(lobbyId);
                    List<Integer> puntate = new ArrayList<>();
                    int chosenNumber = 0;
                    userExit = false;
                    Thread.sleep(2000);
                    int numPlayer = remote.getLobby(lobbyId).users.size();

                    //regola aggiuntiva 2 giocatori
                    boolean numberIsCorrect = false;
                    if(remote.getLobby(lobbyId).users.size() == 2) {
                        while (!numberIsCorrect) {
                            System.out.println("Inserisci il tuo numero: ");
                            try {
                                String chosenNumberString = getInput();
                                if(!chosenNumberString.isBlank()) {
                                    chosenNumber = Integer.parseInt(chosenNumberString);
                                    user = remote.getUserById(nickname);
                                    remote.addNumber(user, chosenNumber);
                                    numberIsCorrect = true;
                                    //remote.setUserLastSignal(nickname);
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Errore, devi inserire un numero compreso tra 0 e 100!");
                            }
                        }
                        String winner = remote.getWinnerOfLobby(lobbyId);
                        List<String> losers = new ArrayList<>();
                        if (winner.equals("")) {
                            System.out.println("Attendi gli altri giocatori...");
                            boolean roundFinished = false;
                            while (!roundFinished) {
                                //remote.setUserLastSignal(nickname);
                                Thread.sleep(1000);
                                winner = remote.getWinnerOfLobby(lobbyId);
                                losers = remote.getEliminatedOfLobby(lobbyId);

                                if (!winner.equals("")) {
                                    roundFinished = true;
                                } else if(!losers.isEmpty()) {
                                    roundFinished = true;
                                    gameFinished = true;
                                    userExit = true;
                                    System.out.println( "\n" + losers.get(0) + " ha abbandonato la partita!\nComplimenti, sei il vincitore della lobby");
                                    //remote.removeLobbyById(lobbyId);
                                }
                            }
                        }
                        puntate = remote.getPuntateUsers(lobbyId);
                        if(chosenNumber == 0 && puntate.contains(100)) {
                            System.out.println("Hai scelto 0 e il tuo avversario ha scelto 100, hai perso una vita\n");
                            Thread.sleep(1000);
                            remote.lostLife(nickname, lobbyId);
                            User userLoser = remote.getUserById(nickname);
                            System.out.println("I giocatori hanno scelto i seguenti numeri: ");
                            for(int puntata : puntate) {
                                System.out.print(puntata+" ");
                            }
                            System.out.println();
                            if (userLoser.getLives() <= 0) {
                                System.out.println("Hai terminato le vite");
                                remote.removeUserFromLobby(nickname);
                                gameFinished = true;
                            } else {
                                System.out.println(userLoser.getLives() + " vite rimaste");
                            }
                        } else if (chosenNumber == 100 && puntate.contains(0)) {
                            System.out.println("Hai vinto! \nHai scelto 100 e il tuo avversario ha scelto 0");
                        } else {
                            int countNum=0;
                            for (Integer num : puntate) {
                                if (chosenNumber == num) {
                                    countNum++;
                                }
                            }
                            if(countNum>1) {
                                System.out.println("Tu e il tuo avversario avete scelto lo stesso numero. \n Nessuno dei due perde la vita!");
                                String nicknameAdmin = remote.getLobby(lobbyId).users.get(0).getNickname();
                                Thread.sleep(1000);
                                if(nicknameAdmin.equals(nickname)) {
                                    remote.clearPuntateUsers(lobbyId);
                                }
                            }
                            else if (!userExit){
                                //regola classica 2 giocatori
                                if (!winner.equals(user.getNickname())) {
                                    User userLoser = remote.getUserById(user.getNickname());
                                    Thread.sleep(1000);
                                    remote.lostLife(nickname, lobbyId);
                                    System.out.println("\nIl vincitore e' " + winner + "\n");
                                    System.out.println("I giocatori hanno scelto i seguenti numeri: ");
                                    for(int puntata : puntate) {
                                        System.out.print(puntata+" ");
                                    }
                                    System.out.println("");
                                    userLoser = remote.getUserById(user.getNickname());
                                    if (userLoser.getLives() <= 0) {
                                        System.out.println("Hai terminato le vite");
                                        remote.removeUserFromLobby(nickname);
                                        gameFinished = true;
                                    } else {
                                        System.out.println("Non ti sei avvicinato abbastanza al numero corretto, perdi una vita!\n");
                                        System.out.println(remote.getUserById(nickname).getLives() + " vite rimaste");
                                    }
                                } else {
                                    losers = remote.getEliminatedOfLobby(lobbyId);
                                    System.out.println("Hai vinto!\n");
                                    if(losers.isEmpty()){
                                        System.out.println("I giocatori hanno scelto i seguenti numeri: ");
                                        for(int puntata : puntate) {
                                            System.out.print(puntata+" ");
                                        }
                                    } else {
                                        System.out.println("Il giocatore " + losers.get(0) + " ha abbandonato la partita!");
                                    }
                                    System.out.println("");
                                }
                            }

                        }
                    //3,4,5 giocatori
                    } else {
                        while (!numberIsCorrect) {
                            System.out.println("Inserisci il tuo numero: ");
                            try {
                                String chosenNumberString = getInput();
                                if(!chosenNumberString.isBlank()) {
                                    chosenNumber = Integer.parseInt(chosenNumberString);
                                    user = remote.getUserById(nickname);
                                    remote.addNumber(user, chosenNumber);
                                    numberIsCorrect = true;
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Errore, devi inserire un numero compreso tra 0 e 100!");
                            }
                        }
                        String winner = remote.getWinnerOfLobby(lobbyId);
                        List<String> losers = remote.getEliminatedOfLobby(lobbyId);
                        if (winner.equals("")) {
                            System.out.println("Attendi gli altri giocatori...");
                            boolean roundFinished = false;
                            while (!roundFinished) {
                                winner = remote.getWinnerOfLobby(lobbyId);
                                losers = remote.getEliminatedOfLobby(lobbyId);

                                /*System.out.println("numPlayer: "+numPlayer);
                                System.out.println("loserSize: "+losers.size());
                                System.out.println("puntateUserSize: "+remote.getPuntateUsers(lobbyId).size());
                                 */
                                if(!losers.isEmpty()) {
                                    if(numPlayer == (losers.size() + remote.getPuntateUsers(lobbyId).size())) {
                                        roundFinished = true;
                                        userExit = true;
                                        for(String loser : losers) {
                                            System.out.println( "\nIl giocatore " + loser + " ha abbandonato la partita!");
                                        }
                                    }
                                } else if (!winner.equals("")) {
                                    Thread.sleep(1000);
                                    losers = remote.getEliminatedOfLobby(lobbyId);

                                    if(numPlayer == (losers.size() + remote.getPuntateUsers(lobbyId).size())) {
                                        roundFinished = true;
                                    }
                                }
                            }
                        } else if(!losers.isEmpty()) {
                            userExit = true;
                            for(String loser : losers) {
                                System.out.println( "\nIl giocatore " + loser + " ha abbandonato la partita!");
                            }
                        }
                        if(!userExit) {
                            if(winner.equals(nickname)) {
                                System.out.println("\nHai vinto!\n");
                                puntate = remote.getPuntateUsers(lobbyId);
                                Thread.sleep(1000);
                                System.out.println("I giocatori hanno scelto i seguenti numeri: ");
                                for(int puntata : puntate) {
                                    System.out.print(puntata+" ");
                                }
                                System.out.println("");
                            } else {
                                System.out.println("\nIl vincitore e' " + winner + "\n");
                                puntate = remote.getPuntateUsers(lobbyId);
                                Thread.sleep(1000);
                                System.out.println("I giocatori hanno scelto i seguenti numeri: ");
                                for(int puntata : puntate) {
                                    System.out.print(puntata+" ");
                                }
                                System.out.println("");
                            }
                            //regola aggiuntiva 4 giocatori
                            if(remote.getLobby(lobbyId).users.size() == 4 || remote.getLobby(lobbyId).users.size() == 3) {
                                int countNum=0;
                                for (Integer num : puntate) {
                                    if (chosenNumber == num) {
                                        countNum++;
                                    }
                                }
                                if(countNum > 1) {
                                    remote.lostLife(user.getNickname(), lobbyId);
                                    System.out.println("Hai scelto lo stesso numero di un altro giocatore, perdi una vita!");
                                }
                            }
                            if (!winner.equals(user.getNickname())) {
                                User userLoser = remote.getUserById(user.getNickname());
                                //18,24,28 per testarlo
                                if(remote.getLobby(lobbyId).users.size() == 3) {
                                    puntate = remote.getPuntateUsers(lobbyId);
                                    Thread.sleep(1000);
                                    if(puntate.contains(remote.getCorrectNumberOfLobby(lobbyId))) {
                                        remote.lostLife(nickname, lobbyId);
                                        System.out.println("Un giocatore ha indovinato il numero corretto, perdi una vita!");
                                    }
                                }
                                remote.lostLife(nickname, lobbyId);
                                if (userLoser.getLives() <= 1) {
                                    System.out.println("\nHai terminato le vite");
                                    remote.removeUserFromLobby(nickname);
                                    gameFinished = true;
                                } else {
                                    System.out.println("\nNon ti sei avvicinato abbastanza al numero corretto, perdi una vita!\n");
                                    System.out.println(remote.getUserById(nickname).getLives() + " vite rimaste");
                                }
                            }
                        }
                    }
                    Thread.sleep(1000);
                    List<String> eliminated = remote.getEliminatedOfLobby(lobbyId);
                    Thread.sleep(1000);

                    if (!eliminated.isEmpty() && !gameFinished && remote.getLobby(lobbyId).users.size() != 1) {
                        if(!userExit) {
                            for (String eliminate : eliminated) {
                                System.out.println("Il giocatore " + eliminate + " ha terminato le vite!\n");
                            }
                            Thread.sleep(1000);
                        }
                        if(remote.getLobby(lobbyId).users.size()==4) {
                            System.out.println("I gocatori sopravvissuti sono 4.\n\nViene aggiunta una nuova regola:\n -se due giocatori scelgono lo stesso numero gli viene detratta una vita.");
                        } else if (remote.getLobby(lobbyId).users.size()==3) {
                            System.out.println("I gocatori sopravvissuti sono 3.\n\nViene aggiunta una nuova regola:\n -scegliere il numero esatto fa perdere 2 vite agli altri giocatori.");
                        } else if (remote.getLobby(lobbyId).users.size()==2) {
                            System.out.println("I gocatori sopravvissuti sono 2.\n\nViene aggiunta una nuova regola oltre a quella iniziale:\n -se un giocatore sceglie 0 l avversario vince se sceglie 100.");
                        }
                    }
                    if (remote.getLobby(lobbyId).users.size() == 1 && !gameFinished) {
                        System.out.println("Complimenti, sei il vincitore della lobby!");
                        Thread.sleep(1500);
                        remote.removeLobbyById(lobbyId);
                        //remote.removeUserFromLobby(nickname);
                        gameFinished = true;
                    } else if (remote.getLobby(lobbyId).users.size() == 1 && gameFinished) {
                        remote.removeLobbyById(lobbyId);
                    }
                    round++;
                }
                //remote.setUserLastSignal(nickname);
                Thread.sleep(1000);
                System.out.println("Digita c per continuare a giocare oppure un altro tasto per uscire");
                String restart = scanner.nextLine();
                if(!restart.equals("c")) {
                    playNextGame = false;
                    remote.removeUserById(nickname);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("errore argomenti");
            } catch (MissingException e) {
                throw new MissingException(e);
            } catch (ConflictException e) {
                throw new ConflictException(e);
            } catch (Exception e) {
                System.exit(0);
                throw new RuntimeException(e);
            }
        }
        System.out.println("finish");
        timerClient.cancel();
    }
    public static String getInput()
    {
        String str = "";
        BufferedReader in;
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                System.out.println( "Tempo scaduto, sarai eliminato dalla lobby" );
                try {
                    if(remote.getLobby(lobbyId).users.size() == 1) {
                        remote.removeLobbyById(lobbyId);
                    } else {
                        remote.removeUserFromLobby(nickname);
                    }
                    remote.removeUserById(nickname);

                    Thread.sleep(500);
                    System.exit(0);

                } catch (MissingException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Timer timer = new Timer();
        try {
            timer.schedule( task, 30*1000 );
            in = new BufferedReader(new InputStreamReader( System.in ) );
            str = in.readLine();
            timer.cancel();
            System.out.println( "Hai scelto il numero: "+ str );
            return str;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getInputAdmin()
    {
        String str = "";
        BufferedReader in;
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                System.out.println( "Tempo scaduto, sarai eliminato dalla lobby" );
                try {
                    remote.removeUserFromLobby(nickname);
                    remote.removeUserById(nickname);
                    Thread.sleep(500);
                    System.exit(0);
                } catch (MissingException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Timer timer = new Timer();
        try {
            timer.schedule( task, 30*1000 );
            in = new BufferedReader(new InputStreamReader( System.in ) );
            str = in.readLine();
            timer.cancel();
            return str;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
