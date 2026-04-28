package trains;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class TrainStateTest {

	private TrafficController controller = new MockTrafficController();

	@Test
	public void testInArrival() {
		assertThat(TrainState.ofInArrival().isInArrival()).isTrue();
		assertThat(TrainState.ofInArrival().isStopped()).isFalse();
		assertThat(TrainState.ofInArrival().isDeparted()).isFalse();
	}
	
	@Test
	public void testIsStopped() {
		assertThat(TrainState.ofStopped().isInArrival()).isFalse();
		assertThat(TrainState.ofStopped().isStopped()).isTrue();
		assertThat(TrainState.ofStopped().isDeparted()).isFalse();
	}
	
	@Test
	public void testIsDeparted() {
		assertThat(TrainState.ofDeparted().isInArrival()).isFalse();
		assertThat(TrainState.ofDeparted().isStopped()).isFalse();
		assertThat(TrainState.ofDeparted().isDeparted()).isTrue();
	}
	
	@Test
	public void initialStateIsInArrival() {
		Train t = new PassengerTrain(1, 100);
		assertThat(t.getState().isInArrival()).isTrue();
		assertThat(t.getState().toString()).isEqualTo("IN_ARRIVAL");
	}

	@Test
	public void stopFromInArrivalToStopped() {
		Train t = new PassengerTrain(1, 100);
		t.stop(controller);
		assertThat(t.getState().toString()).isEqualTo("STOPPED");
		assertThat(t.getState().isStopped()).isTrue();
	}
	
	@Test
	public void fromStoppedToDeparted() {
		Train t = new PassengerTrain(1, 100);
		t.stop(controller);
		t.leave(controller);
		assertThat(t.getState().toString()).isEqualTo("DEPARTED");
	}

	@Test
	public void leaveFromInArrivalThrows() {
		Train t = new PassengerTrain(1, 100);
		assertThatThrownBy(() -> t.leave(controller))
			.isInstanceOf(IllegalTrainStateTransitionException.class)
			.hasMessage("You can't leave while you're arriving");
	}
	
	@Test
	public void stopFromDepartedThrows() {
		Train t = new PassengerTrain(1, 100);
		t.stop(controller);
		t.leave(controller);
		assertThatThrownBy(() -> t.stop(controller))
			.isInstanceOf(IllegalTrainStateTransitionException.class)
			.hasMessage("You can't stop if you've started");
	}
}
