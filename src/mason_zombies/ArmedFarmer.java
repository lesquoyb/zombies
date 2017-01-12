package mason_zombies;

public class ArmedFarmer extends Farmer{

	public ArmedFarmer() {
		depth_of_view = 3*max_dist;
	}
	
	@Override
	protected void positionProcessing(World world) {
		movement = friendsBarycenter(world.friends.getAllNodes(), world.yard);
		movement.addIn(friendsBarycenter(world.predators.getEdgesOut(this), world.yard));	
	}

}
