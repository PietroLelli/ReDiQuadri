package rq;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static rq.LocalReDiQuadri.INITIAL_LIVES;

public abstract class AbstractTestReDiQuadri {
    private final User pietro = new User("pietro");
    private final User alberto = new User("alberto");
    private final User luca = new User("luca");
    private final User matteo = new User("matteo");
    private final Lobby lobby1 = new Lobby("lobby1");
    private final Lobby lobby2 = new Lobby("lobby2");

    private ReDiQuadri reDiQuadri;

    @BeforeEach
    public final void setup() throws ConflictException, IOException, MissingException {
        beforeCreatingReDiQuadri();
        reDiQuadri = createReDiQuadri();
        reDiQuadri.register(pietro);
        reDiQuadri.register(alberto);
        reDiQuadri.register(luca);
    }

    protected abstract void beforeCreatingReDiQuadri() throws IOException;

    protected abstract ReDiQuadri createReDiQuadri() throws ConflictException;

    @AfterEach
    public final void teardown() throws InterruptedException, MissingException {
        shutdownReDiQuadri(reDiQuadri);
        afterShuttingReDiQuadriDown();
    }

    protected abstract void shutdownReDiQuadri(ReDiQuadri reDiQuadri) throws MissingException;

    protected abstract void afterShuttingReDiQuadriDown() throws InterruptedException, MissingException;

    public void testUserConnectionLobby() throws ConflictException, MissingException {
        reDiQuadri.createLobby(lobby1);
        reDiQuadri.addUserInLobby(lobby1.getId(), pietro);
        reDiQuadri.addUserInLobby(lobby1.getId(), luca);

        assertTrue(reDiQuadri.getLobby("lobby1").users.contains(pietro));
        assertTrue(reDiQuadri.getLobby("lobby1").users.contains(luca));
    }

    public void testUserDisconnectionLobby() throws ConflictException, MissingException {
        reDiQuadri.createLobby(lobby1);
        reDiQuadri.addUserInLobby(lobby1.getId(), pietro);
        reDiQuadri.addUserInLobby(lobby1.getId(), luca);

        reDiQuadri.removeUserFromLobby(luca.getNickname());
        assertFalse(reDiQuadri.getLobby(lobby1.getId()).getUsers().contains(luca));
        reDiQuadri.removeUserFromLobby(pietro.getNickname());
        assertFalse(reDiQuadri.getLobby(lobby1.getId()).getUsers().contains(pietro));
    }

    public void testRegisterError() throws MissingException, ConflictException {
        reDiQuadri.createLobby(lobby1);

        assertThrows(ConflictException.class, () -> reDiQuadri.register(pietro));
        assertThrows(ConflictException.class, () -> reDiQuadri.register(alberto));
    }

    public void testLobbyCreation() throws MissingException, ConflictException {
        reDiQuadri.createLobby(lobby1);
        reDiQuadri.createLobby(lobby2);

        assertEquals(lobby1, reDiQuadri.getLobby(lobby1.getId()));
        assertEquals(lobby2, reDiQuadri.getLobby(lobby2.getId()));
    }

    public void testLobbyCreationError() throws ConflictException, MissingException {
        reDiQuadri.createLobby(lobby1);
        assertThrows(ConflictException.class, () -> reDiQuadri.createLobby(lobby1));
    }

    public void testUserChooseNumber() throws MissingException, ConflictException {
        reDiQuadri.createLobby(lobby1);
        reDiQuadri.addUserInLobby(lobby1.getId(), pietro);
        reDiQuadri.addUserInLobby(lobby1.getId(), alberto);
        reDiQuadri.addUserInLobby(lobby1.getId(), matteo);

        reDiQuadri.addNumber(pietro, 33);
        reDiQuadri.addNumber(alberto, 50);
        reDiQuadri.addNumber(matteo, 67);

        assertTrue(reDiQuadri.getPuntateUsers(lobby1.getId()).contains(33));
        assertTrue(reDiQuadri.getPuntateUsers(lobby1.getId()).contains(50));
        assertTrue(reDiQuadri.getPuntateUsers(lobby1.getId()).contains(67));

        assertFalse(reDiQuadri.getPuntateUsers(lobby1.getId()).contains(20));
    }
    
    public void testWinnerOfRound() throws ConflictException, MissingException {
        reDiQuadri.createLobby(lobby1);
        reDiQuadri.addUserInLobby(lobby1.getId(), pietro);
        reDiQuadri.addUserInLobby(lobby1.getId(), alberto);
        reDiQuadri.addUserInLobby(lobby1.getId(), matteo);
        reDiQuadri.addNumber(pietro, 40);
        reDiQuadri.addNumber(alberto, 50);
        reDiQuadri.addNumber(matteo, 67);
        
        assertEquals(pietro.getNickname(), reDiQuadri.getWinnerOfLobby(lobby1.getId()));
    }
    
    public void testLoseLife() throws ConflictException, MissingException {
        reDiQuadri.createLobby(lobby1);
        reDiQuadri.addUserInLobby(lobby1.getId(), pietro);
        reDiQuadri.addUserInLobby(lobby1.getId(), alberto);
        reDiQuadri.addUserInLobby(lobby1.getId(), matteo);
        reDiQuadri.lostLife(alberto.getNickname(), lobby1.getId());

        assertEquals(INITIAL_LIVES-1, reDiQuadri.getUserById(alberto.getNickname()).getLives());
    }
    
    public void testUserLoserGame() throws ConflictException, MissingException {
        reDiQuadri.createLobby(lobby1);
        reDiQuadri.addUserInLobby(lobby1.getId(), pietro);
        reDiQuadri.addUserInLobby(lobby1.getId(), alberto);
        reDiQuadri.addUserInLobby(lobby1.getId(), matteo);

        reDiQuadri.lostLife(matteo.getNickname(), lobby1.getId());
        reDiQuadri.lostLife(matteo.getNickname(), lobby1.getId());
        reDiQuadri.lostLife(matteo.getNickname(), lobby1.getId());
        reDiQuadri.lostLife(matteo.getNickname(), lobby1.getId());
        reDiQuadri.lostLife(matteo.getNickname(), lobby1.getId());

        assertTrue(reDiQuadri.getEliminatedOfLobby(lobby1.getId()).contains(matteo.getNickname()));
    }
}
