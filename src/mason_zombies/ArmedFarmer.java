package mason_zombies;

import sim.util.Double2D;

public class ArmedFarmer extends Farmer{

	public ArmedFarmer() {
		max_dist = 0.1;
		last = - limit;
	}
	
	double range = 20;
	int limit = 100;
	long last;
	
	@Override
	protected void positionProcessing(World world) {
		movement = friendsBarycenter(world.friends.getEdges(this, null), world.yard);
		Double2D me = world.yard.getObjectLocation(this);
		for(Zombie zombie : world.zombies){
			Double2D z = world.yard.getObjectLocation(zombie);
			if(z.distance(me) < range && world.schedule.getSteps() - last > limit){
				world.fire(me, z);
				last = world.schedule.getSteps();
			}
		}
	}

}
