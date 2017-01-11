package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Zombie extends SimulationAgent{

	public Zombie() {
		depth_of_view = 100;
	}
	@Override
	protected void positionProcessing(World world) {

		movement = friendsBarycenter(world.predators.getEdgesOut(this), world.yard);
	//	movement.addIn(friendsBarycenter(world.predators.getEdgesIn(this), world.yard).negate());
		
	}

}
