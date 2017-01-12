package mason_zombies;

import sim.engine.SimState;
import sim.util.Double2D;

public class Bullet extends SimulationAgent{

	@Override
	protected void positionProcessing(World world) {
	}
	
	
	Double2D pos;
	double angle;
	
	public Bullet(Double2D from, Double2D to) {
		angle = Math.atan2( to.y - from.y, to.x - from.x );
		if(angle<0)
			angle+= 2*Math.PI;
		pos = from;
		max_dist = 0.5;
	}

	@Override
	public void step(SimState arg0) {
		World world = (World) arg0;
		pos = new Double2D(pos.x + max_dist *  Math.cos(angle), pos.y + max_dist *  Math.sin(angle));
		world.yard.setObjectLocation(this, pos);
		if(pos.x <0 || pos.y < 0 || pos.x > world.width || pos.y > world.height){
			world.remove(this);
		}
		for(Zombie z : world.zombies){
			Double2D zp = world.yard.getObjectLocation(z);
			if(zp.distance(pos) < max_dist){
				world.shot(z, this);
				break;
			}
		}
		
			
			
	}

	
	
}
