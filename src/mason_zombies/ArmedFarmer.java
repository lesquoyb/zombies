package mason_zombies;

import sim.util.Double2D;

public class ArmedFarmer extends Farmer{

	int remaining_shots;
	public ArmedFarmer() {
		max_dist = 2;
		last = - limit;
		remaining_shots = 2;
		fear =  0;
		depth_of_view = 10 * max_dist;
	}

	double range = 20;
	int limit = 100;
	long last;

	@Override
	protected void positionProcessing(World world) {
		movement = friendsBarycenter(world.friends.getEdges(this, null),  world.yard);
		if(!dead){
			Double2D me = world.yard.getObjectLocation(this);
			for(Zombie zombie : world.zombies){
				Double2D z = world.yard.getObjectLocation(zombie);
				if(z.distance(me) < range && world.schedule.getSteps() - last > limit){
					world.fire(me, z);
					last = world.schedule.getSteps();
					remaining_shots --;
					if (remaining_shots == 0){
						world.addFarmer(me, true);
						world.removeFarmer(this);
						dead = true;
					}
				}
			}
		}
	}

}
