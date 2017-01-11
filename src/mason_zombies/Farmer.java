package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.network.Edge;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Farmer implements Steppable{

	
	
	final double SAFE = 3;
	final double depth_of_view = 5;
	
	
	@Override
	public void step(SimState state) {
		World world = (World) state;
		
	
		Continuous2D yard = world.yard;
		Double2D me = yard.getObjectLocation(this);
		
		MutableDouble2D sumForces = new MutableDouble2D();
		
		MutableDouble2D forceVector = new MutableDouble2D();
		Bag people = world.friends.getEdges(this, null);
		int len = people.size();
		for(int buddy = 0 ; buddy < len; buddy++){
			
			Edge e = (Edge)(people.get(buddy));
			double buddiness = ((Double)(e.info)).doubleValue();
			Double2D him = yard.getObjectLocation(e.getOtherNode(this));
			
			if (buddiness >= 0) {
				forceVector.setTo((him.x - me.x) * buddiness, (him.y - me.y) * buddiness);
				if (forceVector.length() > depth_of_view)  //cant see you mate
					forceVector.resize(0.0);
				else
					forceVector.setTo((him.x - me.x) * buddiness, (him.y - me.y) * buddiness);
			}
			else {
				forceVector.setTo((him.x - me.x) * buddiness, (him.y - me.y) * buddiness);
				if (forceVector.length() > SAFE)  
					forceVector.resize(0.0);
				else
					forceVector.setTo((him.x - me.x) * buddiness, (him.y - me.y) * buddiness);
			}
			sumForces.addIn(forceVector);
		}

		sumForces.addIn(me);
		sumForces.setX(Math.min(Math.max(0, sumForces.x), yard.width));//on ne sort pas de la map
		sumForces.setY(Math.min(Math.max(0, sumForces.y), yard.height));//on ne sort pas de la map
		
		yard.setObjectLocation(this, new Double2D(sumForces));	
		
	}

}
