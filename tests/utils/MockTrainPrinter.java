package utils;

public class MockTrainPrinter implements TrainPrinter{	
	private StringBuilder builder = new StringBuilder();

	@Override
	public void print(String message) {
		builder.append(message + "\n");
	}
	
	public String toString() {
		return builder.toString();
	}
}
