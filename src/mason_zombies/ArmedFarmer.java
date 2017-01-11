package mason_zombies;

public class ArmedFarmer extends Farmer{

	
	@Override
	protected void positionProcessing(World world) {
		movement = friendsBarycenter(world.friends.getAllNodes(), world.yard);
		movement.addIn(friendsBarycenter(world.predators.getEdgesOut(this), world.yard));
		
		
	}

}
