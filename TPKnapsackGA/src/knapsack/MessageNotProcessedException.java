package knapsack;

public class MessageNotProcessedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9020488632238998405L;
	
	private WorkOnPopulationMessage messageLost;
	
	public MessageNotProcessedException(WorkOnPopulationMessage m2) {
		this.messageLost = m2;
	}

	public WorkOnPopulationMessage getMessageLost() {
		return messageLost;
	}

}
