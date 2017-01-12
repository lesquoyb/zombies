package mason_zombies;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class Arme implements Steppable{
	MutableDouble2D pos;
	@Override
	public void step(SimState arg0) {
		World world=(World)arg0;
		for(Farmer f : world.farmers){
			if(world.yard.getObjectLocation(f).equals(pos)){
				world.addArmedFarmer(world.yard.getObjectLocation(f));
				world.farmers.remove(f);
				mouv(world);
			}
		}
		
		
	}
	public void mouv(World world){
		pos.x=world.random.nextDouble()*world.width;
		pos.y=world.random.nextDouble()*world.height;
			
			
		}
	
}
