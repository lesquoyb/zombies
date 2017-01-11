package mason_zombies;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;

public class World extends SimState{
	

	public Continuous2D yard = new Continuous2D(1.0, 100, 100);
	public int numFarmers = 50;
	public int numZombies = 10;
	double forceToSchoolMultiplier = 0.01;
	double randomMultiplier = 0.1;
	public Network predators = new Network(true);
	public Network friends = new Network(false);
	List<Farmer> farmers = new ArrayList<>();
	List<Zombie> zombies = new ArrayList<>();
	List<ArmedFarmer> armedFarmers = new ArrayList<>();
	
	
	public World(){
		super(System.currentTimeMillis());
	}
	

	public void start(){
		super.start();
		
		yard.clear();
		predators.clear();
		friends.clear();

		for(int i = 0; i < numFarmers; i++){
			Farmer farmer = new Farmer();
			yard.setObjectLocation(	farmer, 
					new Double2D(yard.getWidth() * 0.5 + random.nextDouble() - 0.5,
							yard.getHeight() * 0.5 + random.nextDouble() - 0.5));
			schedule.scheduleRepeating(farmer);
			friends.addNode(farmer);
			farmers.add(farmer);
		}
		
		for(int i = 0; i < numZombies; i++){
			Zombie zombie = new Zombie();
			yard.setObjectLocation(	zombie, 
									new Double2D(yard.getWidth() * 0.5 + random.nextDouble() - 0.5,
									yard.getHeight() * 0.5 + random.nextDouble() - 0.5));
			schedule.scheduleRepeating(zombie);
			zombies.add(zombie);
			for(Farmer f : farmers){
				predators.addEdge(zombie, f, null);
			}
			
		}

		Bag friendsBag = friends.getAllNodes();
		for(int i = 0; i < friendsBag.size(); i++){
			Object friend = friendsBag.get(i);

			//amis
			Object friendB = null;
			do
				friendB = friendsBag.get(random.nextInt(friendsBag.numObjs));
			while (friend == friendB);
			
			double buddiness = random.nextDouble();
			friends.addEdge(friend, friendB, new Double(buddiness));

			
			//pas amis
			do
				friendB = friendsBag.get(random.nextInt(friendsBag.numObjs));
			while (friend == friendB);
			buddiness = random.nextDouble();
			friends.addEdge(friend, friendB, new Double( -buddiness));
		}
		
		
		
	}



	public World(long seed) {
		super(seed);
	}

}
