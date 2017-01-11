package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Zombie extends SimulationAgent{

	@Override
	protected void positionProcessing(World world) {

		movement = friendsBarycenter(world.predators.getEdgesOut(this), world.yard);
		movement.addIn(friendsBarycenter(world.predators.getEdgesIn(this), world.yard).negate());
		
	}

}
