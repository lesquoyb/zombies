package mason_zombies;

import sim.util.Bag;
import sim.util.MutableDouble2D;

public class Farmer extends SimulationAgent{

	
	

		
	@Override
	protected void positionProcessing(World world) {
	
		Bag people = world.friends.getEdges(this, null);
		movement = friendsBarycenter(people,  world.yard);
		movement .addIn(friendsBarycenter(world.predators.getEdgesIn(this), world.yard));
		
	}

}
