package trains;

public interface TrainVisitor {
	void visit(PassengerTrain train);
	void visit(FreightTrain train);
}
