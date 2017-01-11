package mason_zombies;

import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal.simple.*;

public class WorldGUI extends GUIState {

	

	public Display2D display;
	public JFrame displayFrame;
	ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();
	NetworkPortrayal2D buddiesPortrayal = new NetworkPortrayal2D();


	public void start()
	{
		super.start();
		setupPortrayals();
	}
	public void load(SimState state)
	{
		super.load(state);
		setupPortrayals();
	}
	public void setupPortrayals(){
		World world = (World) state;
		// tell the portrayals what to portray and how to portray them
		yardPortrayal.setField( world.yard );
		yardPortrayal.setPortrayalForClass(Farmer.class,new OvalPortrayal2D(new Color(200,115,30)));
		yardPortrayal.setPortrayalForClass(ArmedFarmer.class, new RectanglePortrayal2D(new Color(30,30,160)));
		yardPortrayal.setPortrayalForClass(Zombie.class, new HexagonalPortrayal2D(new Color(30,155,80)));
		buddiesPortrayal.setField( new SpatialNetwork2D( world.yard, world.predators ) );
		buddiesPortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());
		
		// reschedule the displayer
		display.reset();
		display.setBackdrop(Color.white);
		// redraw the display
		display.repaint();
	}
	public void init(Controller c)
	{
		super.init(c);
		display = new Display2D(600,600,this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Schoolyard Display");
		c.registerFrame(displayFrame);        // so the frame appears in the "Display" list
		displayFrame.setVisible(true);
		display.attach( yardPortrayal, "Yard" );
		display.attach( buddiesPortrayal, "Buddies" );
	}



	public void quit()
	{
		super.quit();
		if (displayFrame!=null) displayFrame.dispose();
		displayFrame = null;
		display = null;
	}




	public static void main(String[] args)
	{
		WorldGUI vid = new WorldGUI();
		Console c = new Console(vid);
		c.setVisible(true);
	}
	
	public WorldGUI(SimState state) {
		super(state);
	}
	public WorldGUI(){
		super(new World());
	}


	public static String getName()
	{
		return "Student Schoolyard Cliques";
	}
}
