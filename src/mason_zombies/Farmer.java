package mason_zombies;

import sim.field.network.Edge;
import sim.util.Bag;
import sim.util.Double2D;

public class Farmer extends SimulationAgent{


	public Farmer(){
		max_dist = 0.3;
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
			if( d.distance(me) < dist){
				dist = d.distance(me);
				nearest = a;
			}
		}
		if(nearest != null){
			if(dist < max_dist){
				ArmedFarmer af = world.addArmedFarmer(world.yard.getObjectLocation(this), false);
				for(Object f : world.friends.getEdges(this, people)){
					Farmer e = (Farmer) ((Edge) f).getOtherNode(this);
					world.friends.addEdge(e, af, (Double)((Edge) f).getInfo());
				}
				world.removeFarmer(this);
				nearest.mouv(world);
				dead = true;
			}
			else
				movement.addIn(world.yard.getObjectLocation(nearest).add(me.negate()).multiply(0.3));
			
		}
	}



}
