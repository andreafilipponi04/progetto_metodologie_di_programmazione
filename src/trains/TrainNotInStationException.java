package trains;

public class TrainNotInStationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public TrainNotInStationException(String arg0) {
		super(arg0);
	}
	
}
