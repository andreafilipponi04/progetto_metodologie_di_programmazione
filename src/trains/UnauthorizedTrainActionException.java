package trains;

public class UnauthorizedTrainActionException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedTrainActionException(String arg0) {
		super(arg0);
	}

}
