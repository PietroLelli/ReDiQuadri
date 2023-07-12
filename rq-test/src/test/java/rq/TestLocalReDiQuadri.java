package rq;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestLocalReDiQuadri extends AbstractTestReDiQuadri{
    @Override
    protected void beforeCreatingReDiQuadri() throws IOException {

    }

    @Override
    protected ReDiQuadri createReDiQuadri() throws ConflictException {
        return new LocalReDiQuadri();
    }

    @Override
    protected void shutdownReDiQuadri(ReDiQuadri reDiQuadri) throws MissingException {
       reDiQuadri.resetAllLobbies();
    }

    @Override
    protected void afterShuttingReDiQuadriDown() throws InterruptedException {

    }
    @Override
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
