package trains;

public interface TrafficController {
	void authorizeStop(Train train);
	void authorizeDeparture(Train train);
}
