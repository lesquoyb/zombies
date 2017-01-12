package mason_zombies;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.grid.IntGrid2D;
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
	public int width=(int)yard.getWidth();
	public int height=(int)yard.getHeight();
	List<Farmer> farmers = new ArrayList<>();
	List<Zombie> zombies = new ArrayList<>();
	List<ArmedFarmer> armedFarmers = new ArrayList<>();
	public IntGrid2D obstacles;
	
	public World(){
		super(System.currentTimeMillis());
	}
	

	public void start(){
		super.start();
		
		yard.clear();
		predators.clear();
		friends.clear();
		obstacles=new IntGrid2D((int)yard.width,(int)yard.height,0);
		setObstacles();
		
		for(int i = 0; i < numFarmers; i++){
			Farmer farmer = new Farmer();
			yard.setObjectLocation(	farmer, 
					new Double2D(random.nextDouble()*yard.getWidth()*0.80+0.1*width,
								 random.nextDouble()* yard.getHeight()*0.80+0.1*height ));
			schedule.scheduleRepeating(farmer);
			friends.addNode(farmer);
			farmers.add(farmer);
		}
		
		for(int i = 0; i < numZombies; i++){
			Zombie zombie = new Zombie();
			yard.setObjectLocation(	zombie, 
									new Double2D(width * 0.85 + random.nextDouble()*width*0.1-0.05*width,
									height*random.nextDouble()*0.8+height*0.1));
			schedule.scheduleRepeating(zombie);
			zombies.add(zombie);
			for(Farmer f : farmers){
				predators.addEdge(zombie, f, 1.);

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
	public void setObstacles(){
		for( int x=(int)(width*0.05);x<width*0.95;x++){
			obstacles.field[x][(int)(height*0.05)]=1;
			obstacles.field[x][(int)(height*0.95)]=1;
		}
		for(int y=(int)(height*0.05);y<=height*0.95;y++){
			obstacles.field[(int)(width*0.05)][y]=1;
			obstacles.field[(int)(width*0.95)][y]=1;
		}
		for(int x=(int)(width*0.05);x<width*0.5-width*0.05;x++){
			obstacles.field[x][(int)(height*0.5)]=1;
			obstacles.field[x+(int)(width*0.25)][(int)(height*0.25)]=1;
			obstacles.field[x+(int)(width*0.25)][(int)(height*0.75)]=1;
		}
		for(int y=(int)(height*0.25);y<=height*0.75;y++){
			obstacles.field[(int)(width*0.5-width*0.05+width*0.25)][y]=1;
			
		}
	}


	public World(long seed) {
		super(seed);
	}

}
