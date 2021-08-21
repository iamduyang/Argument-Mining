package yang.RL;

public class Observation 
{
	private float[] currentState;
	private int currentAction;
	private float reward;
	private float[] nextState;
	private int nextAction;
	
	public Observation( float[] aCState, int aCAct, float aReward, float[] aNState, int aNAct )
	{
		currentState = aCState.clone();
		currentAction = aCAct;
		reward = aReward;
		nextState = aNState.clone();
		nextAction = aNAct;
	}
	
	public float[] getCurrentState()
	{
		return currentState;
	}
	
	public float[] getNextState()
	{
		return nextState;
	}
	
	public int getCurrentAction()
	{
		return currentAction;
	}
	
	public int getNextAction()
	{
		return nextAction;
	}
	
	public float getReward()
	{
		return reward;
	}

}
