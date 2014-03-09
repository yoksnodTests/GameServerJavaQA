package gameMechanics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameSessionTest {

    @Test
    public void testSetCountClicks() {
        GameSession gameSession = new GameSession(1);
        gameSession.setCountClicks(1);
        GameSession mockGameSession = mock(GameSession.class);
        when(mockGameSession.getCountClicks()).thenReturn(1);
        assertEquals(gameSession.getCountClicks(), mockGameSession.getCountClicks());
    }

}
