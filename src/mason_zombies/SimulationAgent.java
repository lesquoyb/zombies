package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.network.Edge;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public abstract class SimulationAgent implements Steppable{




	protected double max_dist = 3;
	protected double SAFE = 3*max_dist;
	protected double depth_of_view = 7*max_dist;
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

		Double2D me = world.yard.getObjectLocation(this);

		
		positionProcessing(world);


		movement.multiplyIn(5./max_dist);
	
		movement.setLength(Math.min(movement.length(), max_dist));

		movement.addIn(world.yard.getObjectLocation(this));
		movement.setX(Math.min(Math.max(0, movement.x), world.yard.width));//on ne sort pas de la map
		movement.setY(Math.min(Math.max(0, movement.y), world.yard.height));
		movement=miniBrasenham(me,movement,world);
		
		world.yard.setObjectLocation(this, new Double2D(movement));
	}
	
	public MutableDouble2D miniBrasenham( Double2D p2,MutableDouble2D p1,World world){
		MutableDouble2D res=new MutableDouble2D();
		res.x=p2.x;
		res.y=p2.y;
		Double2D delta=new Double2D(p1.x-p2.x,p1.y-p2.y);
		double e=2*delta.y-delta.x;
		for(int i=1;i<delta.x;i++){
			while(e>=0){
				if(world.obstacles.field[(int)res.x][(int)res.y+1]==0)
					res.y+=1;
				e-=2*delta.x;
			}
			if(world.obstacles.field[(int)res.x+1][(int)res.y]==0)
				res.x+=1;
			e+=2*delta.y;
		}
		return res;
	}


}
