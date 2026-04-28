package trains;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;

import org.junit.Test;

public class TrainTest {
	
	private TrafficController controller = new MockTrafficController();
	
	@Test
	public void trainIsInArrivalOnCreation() {
		Train t = new PassengerTrain(1, 10);
		assertThat(t.getState()).isEqualTo(TrainState.ofInArrival());
	}
	
	@Test
	public void stopWithoutControllerThrows() {
		Train t = new PassengerTrain(1, 100);
		assertThatThrownBy(() -> t.stop(null))
			.isInstanceOf(UnauthorizedTrainActionException.class)
			.hasMessage("Unauthorized Stop");
	}
	
	@Test
	public void leaveWithoutControllerThrows() {
		Train t = new PassengerTrain(1, 100);
		t.stop(controller);
		assertThatThrownBy(() -> t.leave(null))
			.isInstanceOf(UnauthorizedTrainActionException.class)
			.hasMessage("Unauthorized Departure");
	}
	
	@Test
	public void testAddRemoveObserver() {
		TrainObserver observer = e -> {};
		Train t = new PassengerTrain(1, 100);
		t.attachObserver(observer);
		Collection<TrainObserver> observers = t.getObservers();
		assertThat(observers).containsExactly(observer);
		t.detachObserver(observer);
		assertThat(observers.size()).isEqualTo(0);
	}
	
	@Test
	public void setSeatsUpdatesValueAndNotifiesObservers() {
		PassengerTrain pt = new PassengerTrain(1, 150);
		MockTrainObserver observer = new MockTrainObserver();
		
		pt.attachObserver(observer);
		pt.setSeats(200);
		
		assertThat(pt.getSeats()).isEqualTo(200);
		assertThat(observer.getWasNotified()).isEqualTo(true);
	}
	
	@Test
	public void setMaxcargoWeightUpdatesValueAndNotifiesObservers() {
		FreightTrain ft = new FreightTrain(2, 1500);
		MockTrainObserver observer = new MockTrainObserver();
		
		ft.attachObserver(observer);
		ft.setMaxCargoWeight(2000);
		
		assertThat(ft.getMaxCargoWeight()).isEqualTo(2000);
		assertThat(observer.getWasNotified()).isEqualTo(true);
	}
}
