package mason_zombies;

import sim.util.Bag;
import sim.util.Double2D;

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
		Arme nearest = null;
		double dist = Double.MAX_VALUE;
		Double2D me = world.yard.getObjectLocation(this);
		for(Arme a : world.weapons){
			Double2D d = world.yard.getObjectLocation(a);
			if(d.distance(me) < 10 && d.distance(me) < dist){
				dist = d.distance(me);
				nearest = a;
			}
		}
		if(nearest != null){
			if(dist < 1){
				world.addArmedFarmer(me, true);
				world.removeFarmer(this);
				world.removeWeapon(nearest);
				world.addWeapon(new Double2D(world.random.nextDouble()*world.yard.getWidth()*0.80+0.1*world.width, world.random.nextDouble()* world.yard.getHeight()*0.80+0.1*world.height ));
				dead = true;
			}
			else
				movement.addIn(world.yard.getObjectLocation(nearest).add(me.negate()).multiply(1));
			
		}
	}



}
