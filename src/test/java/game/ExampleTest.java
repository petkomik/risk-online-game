//@author Petko Mikov
package game;
import org.junit.jupiter.api.Test;

import game.models.Lobby;

import static org.junit.jupiter.api.Assertions.*;


class JUnit5ExampleTest {
	 
    @Test
    void justAnExample() {
    	Lobby testLobby = new Lobby();
    	assertTrue(testLobby.getLobbyRank() == 0);
    }	
}