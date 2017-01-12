package mason_zombies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.continuous.Continuous2D;
import sim.field.grid.IntGrid2D;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class World extends SimState{
	

	public Continuous2D yard = new Continuous2D(1.0, 100, 100);
	public int numFarmers = 10;

	public int numArmed = 0;

	public int numZombies = 50;
	public int numWeapons = 1;

	public Network predators = new Network(true);
	public Network friends = new Network(false);
	public int width=(int)yard.getWidth();
	public int height=(int)yard.getHeight();
	List<Arme> arme = new ArrayList<>();
	List<Farmer> farmers = new ArrayList<>();
	List<Zombie> zombies = new ArrayList<>();
	List<ArmedFarmer> armedFarmers = new ArrayList<>();
	List<Arme> weapons = new ArrayList<>();
	
	List<Bullet> bullets = new ArrayList<>();
	
	Map<SimulationAgent, Stoppable> stop = new HashMap();
	public IntGrid2D obstacles;
	
	public World(){
		super(System.currentTimeMillis());
	}
	

	public void remove(Bullet b){
		yard.remove(b);
		bullets.remove(b);
		stop.get(b).stop();
	}
	
	public void removeFarmer(Farmer f){
		stop.get(f).stop();
		farmers.remove(f);
		armedFarmers.remove(f);
		friends.removeNode(f);
		predators.removeNode(f);
		yard.remove(f);
	}
	
	
	public void removeWeapon(Arme w){
		yard.remove(w);
		weapons.remove(w);

		stop.get(w).stop();
		//w.dead = true;

	}
	public void isEaten(Farmer f){
		Double2D c = yard.getObjectLocation(f);
		
		addZombie(new Double2D(c.x +random.nextDouble()*0.03*yard.width, c.y + random.nextDouble()* 0.04*yard.height ));
		
		removeFarmer(f);
	}
	
	public void shot(Zombie z, Bullet b){
		zombies.remove(z);
		predators.removeNode(z);
		yard.remove(z);		
		stop.get(z).stop();
		
		remove(b);
	}
	
	
	public void fire(Double2D from,Double2D to){
		Bullet b = new Bullet(from, to);
		bullets.add(b);
		stop.put(b, schedule.scheduleRepeating(b));
	}
	
	
	private void setFriends(Object friend, Bag friendsBag){

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
	

	public void start(){
		super.start();
		
		weapons.clear();
		yard.clear();
		predators.clear();
		friends.clear();
		farmers.clear();
		armedFarmers.clear();
		stop.clear();
		bullets.clear();
		zombies.clear();
		arme.clear();
		
		obstacles = new IntGrid2D((int)yard.width,(int)yard.height,0);
		//setObstacles();
		
		for(int i = 0; i < numFarmers; i++){
			addFarmer(new Double2D(random.nextDouble()*yard.getWidth()*0.80+0.1*width, random.nextDouble()* yard.getHeight()*0.80+0.1*height ), false);
		}
		
		for(int i = 0; i < numArmed; i++){
			addArmedFarmer(new Double2D(random.nextDouble()*yard.getWidth()*0.80+0.1*width, random.nextDouble()* yard.getHeight()*0.80+0.1*height ), false);
		}
		

		for(int i = 0; i < numZombies; i++){
			addZombie(new Double2D(width * 0.85 + random.nextDouble()*width*0.1-0.05*width, height*random.nextDouble()*0.8+height*0.1));
		}
		

		for(int i = 0; i < numWeapons; i++){
			addWeapon(new Double2D(random.nextDouble()*yard.getWidth()*0.80+0.1*width, random.nextDouble()* yard.getHeight()*0.80+0.1*height ));
		}

		Bag friendsBag = friends.getAllNodes();
		for(Object friend : friendsBag){
			setFriends(friend, friendsBag);			
		}
		
		
		
	}
	
	/*
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
*/

	public World(long seed) {
		super(seed);
	}

	
	public void addFarmer(Double2D pos, boolean friend){
		Farmer farmer = new Farmer();
		yard.setObjectLocation(	farmer, pos);
		stop.put(farmer, schedule.scheduleRepeating(farmer));
		friends.addNode(farmer);
		farmers.add(farmer);
		if(friend){
			setFriends(farmer, friends.getAllNodes());
		}
	}
	private void addZombie(Double2D pos){
		Zombie zombie = new Zombie();
		yard.setObjectLocation(	zombie, pos);
		stop.put(zombie, schedule.scheduleRepeating(zombie));
		zombies.add(zombie);
		for(Farmer f : farmers){
			predators.addEdge(zombie, f, 1.);
		}
	}
	public void addArmedFarmer(Double2D pos, boolean friend){
		ArmedFarmer farmer = new ArmedFarmer();
		yard.setObjectLocation(	farmer, pos);
		stop.put(farmer, schedule.scheduleRepeating(farmer));
		friends.addNode(farmer);
		armedFarmers.add(farmer);
		if(friend){
			setFriends(farmer, friends.getAllNodes());
		}
	}

	public void addWeapon(Double2D pos){
		Arme w = new Arme();
		stop.put(w, schedule.scheduleRepeating(w));
		weapons.add(w);
		yard.setObjectLocation(w, pos);
		
	}

}
