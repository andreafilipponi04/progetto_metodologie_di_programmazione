package trains;

public class FreightTrain extends Train {

	private int maxCargoWeight;

	public FreightTrain(int id, int maxCargoWeight) {
		super(id);
		this.maxCargoWeight = maxCargoWeight;
	}

	public int getMaxCargoWeight() {
		return maxCargoWeight;
	}

	public void setMaxCargoWeight(int maxCargoWeight) {
		this.maxCargoWeight = maxCargoWeight;
		notifyObservers();
	}

	@Override
	public void accept(TrainVisitor visitor) {
		visitor.visit(this);
	}
}
