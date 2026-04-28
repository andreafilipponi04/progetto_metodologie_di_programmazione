package trains;

public class PassengerTrain extends Train {

	private int seats;

	public PassengerTrain(int id, int seats) {
		super(id);
		this.seats = seats;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
		notifyObservers();
	}

	@Override
	public void accept(TrainVisitor visitor) {
		visitor.visit(this);
	}
}
