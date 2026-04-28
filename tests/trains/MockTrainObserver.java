package trains;

public class MockTrainObserver implements TrainObserver{

	private boolean wasNotified = false;
	
	@Override
	public void update(Train train) {
		this.wasNotified = true;
	}
	
	public boolean getWasNotified() {
		return wasNotified;
	}
}
