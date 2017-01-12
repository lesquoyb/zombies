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
		sumForces.x=sumForces.x/yard.width;
		sumForces.y=sumForces.y/yard.height;
		return sumForces;

	}


	boolean dead;
	abstract protected void positionProcessing(World world) ;

	@Override
	public void step(SimState arg0) {

		World world = (World) arg0;

		dead = false;
		Double2D me = world.yard.getObjectLocation(this);




		positionProcessing(world);


		if(!dead){
			movement.setLength(Math.min(movement.length(), max_dist));

			//	movement=miniBresenham(me,movement,world, world.obstacles.field);
			movement.addIn(me);


			movement.setX(Math.min(Math.max(0, movement.x), world.yard.width-1));//on ne sort pas de la map
			movement.setY(Math.min(Math.max(0, movement.y), world.yard.height-1));
			world.yard.setObjectLocation(this, new Double2D(movement));
		}

	}

	public MutableDouble2D miniBresenham( Double2D p2,MutableDouble2D p1,World world, int[][] field){
		MutableDouble2D res=new MutableDouble2D();
		int x=(int)Math.round(p1.x),x2=(int)Math.round(p2.x),y=(int)Math.round(p1.y),y2=(int)Math.round(p2.y);
		boolean steep=y-y2 > x-x2;

		if(steep){
			int tmp=x;
			x=y;
			y=tmp;
			tmp=x2;
			x2=y2;
			y2=tmp;

		}
		if(x<x2){

			int tmp=x;
			x=x2;
			x2=tmp;
			tmp=y;
			y=y2;
			y2=tmp;
		}
		res.x=x2;
		res.y=y2;
		Double2D delta=new Double2D(x-x2,y-y2);
		double e=2*delta.y-delta.x;
		for(int i=1;i<delta.x;i++){
			while(e>=0){
				res.y+=1;

				res.setY(Math.min(Math.max(0, movement.y), world.yard.height-1));
				if(field[(int)Math.round(res.x)][(int)Math.round(res.y)]==1)
					res.y-=1;

				e-=2*delta.x;
			}
			res.x+=1;
			res.setX(Math.min(Math.max(0, movement.x), world.yard.width-1));//on ne sort pas de la map
			if(field[(int)Math.round(res.x)][(int)Math.round(res.y)]==1)
				res.x-=1;

			e+=2*delta.y;
		}

		if(steep){
			double tmp=res.x;
			res.x=res.y;
			res.y=tmp;
		}

		return res;
	}


}
