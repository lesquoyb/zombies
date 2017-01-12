package mason_zombies;

import sim.util.Bag;

public class Farmer extends SimulationAgent{

	
	public Farmer(){
		max_dist = 3;
	}

	double fear = 2;
	@Override
	protected void positionProcessing(World world) {
	
		Bag people = world.friends.getEdges(this, null);
		movement = friendsBarycenter(people,  world.yard);
		movement.addIn(friendsBarycenter(world.predators.getEdgesIn(this), world.yard).negate().multiplyIn(fear));
		
	}

}
