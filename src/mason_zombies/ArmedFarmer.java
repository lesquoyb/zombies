package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.inspector.StreamingPropertyInspector;
import sim.util.MutableDouble2D;

public class ArmedFarmer extends Farmer{

	@Override
	public void step(SimState state) {
		World world = (World) state;
		
		MutableDouble2D c = friendsBarycenter(world.friends.getAllNodes(), world.yard);
		c.addIn(friendsBarycenter(world.predators.getEdgesOut(this), world.yard));

		
	}

}
