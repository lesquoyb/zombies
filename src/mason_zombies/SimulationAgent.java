package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.network.Edge;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public abstract class SimulationAgent implements Steppable{




	protected double SAFE = 15;
	protected double depth_of_view = 35;
	protected double max_dist;
	protected MutableDouble2D movement;

	public MutableDouble2D friendsBarycenter(Bag people, Continuous2D yard){
		Double2D me = yard.getObjectLocation(this);


		MutableDouble2D forceVector = new MutableDouble2D();
		MutableDouble2D sumForces = new MutableDouble2D();


		int len = people.size();
		for(int buddy = 0 ; buddy < len; buddy++){

			Edge e = (Edge)(people.get(buddy));
			double buddiness = ((Double)(e.info)).doubleValue();
			Double2D him = yard.getObjectLocation(e.getOtherNode(this));
			if(him != null){
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
		}
		return sumForces;
	}

	abstract protected void positionProcessing(World world) ;

	@Override
	public void step(SimState arg0) {
		World world = (World) arg0;

		max_dist = Math.max(world.yard.height, world.yard.width)/10;
		positionProcessing(world);
		//System.out.println(world.yard.height);
		//movement.setLength(Math.max(Math.min(max_dist, movement.length()), 0));

		movement.multiplyIn(5./max_dist);

		movement.addIn(world.yard.getObjectLocation(this));
		movement.setX(Math.min(Math.max(0, movement.x), world.yard.width));//on ne sort pas de la map
		movement.setY(Math.min(Math.max(0, movement.y), world.yard.height));
		
		world.yard.setObjectLocation(this, new Double2D(movement));
	}




}
