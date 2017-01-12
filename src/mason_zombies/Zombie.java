package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Zombie extends SimulationAgent{

	public Zombie() {
		depth_of_view = 100;
		max_dist = 0.05;
	}



	@Override
	protected void positionProcessing(World world) {

		Farmer nearest = null;
		double dist = Double.MAX_VALUE;
		Double2D me = world.yard.getObjectLocation(this);
		Double2D next;
		for(Farmer f : world.farmers){
			next = world.yard.getObjectLocation(f);
			if(next != null){
				if(me.distance(next) < dist){
					dist = me.distance(next);
					nearest = f;
				}
			}
		}
		for(Farmer f : world.armedFarmers){
			next = world.yard.getObjectLocation(f);

			if(next != null){
				if(me.distance(next) < dist){
					dist = me.distance(next);
					nearest = f;
				}
			}
		}
		if( dist< max_dist*max_dist*2 ){
			world.isEaten(nearest);
		}
		
		Double2D aim = world.yard.getObjectLocation(nearest);
		movement = new MutableDouble2D(me.x - aim.x , me.y - aim.y);
		//	movement.addIn(friendsBarycenter(world.predators.getEdgesIn(this), world.yard).negate());

	}

}
