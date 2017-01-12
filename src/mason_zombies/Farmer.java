package mason_zombies;

import sim.util.Bag;

public class Farmer extends SimulationAgent{

	
	

	double fear = 2;
	@Override
	protected void positionProcessing(World world) {
	
		Bag people = world.friends.getEdges(this, null);
		movement = friendsBarycenter(people,  world.yard);
		movement.addIn(friendsBarycenter(world.predators.getEdgesIn(this), world.yard).negate().multiplyIn(fear));
		
	}

}
