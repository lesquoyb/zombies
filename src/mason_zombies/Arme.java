package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;


public class Arme extends SimulationAgent{
	@Override
	protected void positionProcessing(World world) {
	}

	MutableDouble2D pos;

	public Arme(){
		pos = new MutableDouble2D();
	}

	@Override
	public void step(SimState arg0) {
		World world=(World)arg0;
		world.yard.setObjectLocation(this, new Double2D(pos));
	}

	public void mouv(World world){
		pos.x=world.random.nextDouble()*world.width;
		pos.y=world.random.nextDouble()*world.height;
	}



}
