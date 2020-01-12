
package poker;

//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

public class House {

	public static void main(String[] args) {
		try {

			// BufferedReader input = new BufferedReader(new
			// InputStreamReader(System.in));

			// Create a repository
			SpaceRepository repository = new SpaceRepository();

			// Create a local space for the chat messages
			SequentialSpace lobby = new SequentialSpace();

			// Add the space to the repository
			repository.add("lobby", lobby);

			// Default value
			String uri = "tcp://127.0.0.1:9001/?keep";

			// Open a gate
			URI myUri = new URI(uri);
			String gateUri = "tcp://" + myUri.getHost() + ":" + myUri.getPort() + "?keep";
			System.out.println("Opening repository gate at " + gateUri + "...");
			repository.addGate(gateUri);

			//
			while (true) {
				Object[] t = lobby.get(new FormalField(String.class), new FormalField(Integer.class),
						new FormalField(Integer.class),new FormalField(Integer.class));
				System.out.println(t[0] + ": Balance: " + t[1] + " playerId: " + t[2] + " " + "bet: " + t[3]);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {

			e.printStackTrace();
		}
	}
}