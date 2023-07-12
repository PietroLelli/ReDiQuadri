package rq;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestRemoteReDiQuadri  extends AbstractTestReDiQuadri {
    private static final int port = 10000;
    private ReDiQuadriService service;

    @Override
    protected void beforeCreatingReDiQuadri() throws IOException {
        service = new ReDiQuadriService(port);
        service.start();
    }

    @Override
    protected ReDiQuadri createReDiQuadri() throws ConflictException {
        return new RemoteReDiQuadri("localhost", port);
    }

    @Override
    protected void shutdownReDiQuadri(ReDiQuadri reDiQuadri) throws MissingException {
        reDiQuadri.resetAllLobbies();
    }

    @Override
    protected void afterShuttingReDiQuadriDown() {
        service.stop();
    }

    @Test
    public void testUserConnectionLobby() throws ConflictException, MissingException {
        super.testUserConnectionLobby();
    }

    @Override
    @Test
    public void testUserDisconnectionLobby() throws ConflictException, MissingException {
        super.testUserDisconnectionLobby();
    }

    @Override
    @Test
    public void testRegisterError() throws MissingException, ConflictException {
        super.testRegisterError();
    }

    @Override
    @Test
    public void testLobbyCreation() throws MissingException, ConflictException {
        super.testLobbyCreation();
    }

    @Override
    @Test
    public void testLobbyCreationError() throws ConflictException, MissingException {
        super.testLobbyCreationError();
    }

    @Override
    @Test
    public void testUserChooseNumber() throws MissingException, ConflictException {
        super.testUserChooseNumber();
    }
    @Override
    @Test
    public void testWinnerOfRound() throws MissingException, ConflictException {
        super.testWinnerOfRound();
    }
    @Override
    @Test
    public void testLoseLife() throws MissingException, ConflictException {
        super.testLoseLife();
    }
    @Override
    @Test
    public void testUserLoserGame() throws MissingException, ConflictException {
        super.testUserLoserGame();
    }
}
