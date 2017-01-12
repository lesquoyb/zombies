package mason_zombies;

import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Zombie extends SimulationAgent{

	public Zombie() {
		depth_of_view = 100;
		max_dist = 0.05;
	}



	@Override
	protected void positionProcessing(World world) {

		movement = new MutableDouble2D();
		if(world.friends.getAllNodes().size() > 0){
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

			Double2D aim = world.yard.getObjectLocation(nearest);
			if( dist < max_dist ){
				world.isEaten(nearest);
			}

			
			movement = new MutableDouble2D(aim.x - me.x  , aim.y - me.y );
			
			//	movement.addIn(friendsBarycenter(world.predators.getEdgesIn(this), world.yard).negate());

		}

	}

}
