package trains;

public abstract class TrainState {
	private TrainState() {
	}

	private static final TrainState IN_ARRIVAL = new TrainState() {
		@Override
		public String toString() {
			return "IN_ARRIVAL";
		}

		@Override
		public TrainState stop() {
			return STOPPED;
		}

		@Override
		public TrainState leave() {
			throw new IllegalTrainStateTransitionException("You can't leave while you're arriving");
		}
	};

	private static final TrainState STOPPED = new TrainState() {
		@Override
		public String toString() {
			return "STOPPED";
		}

		@Override
		public TrainState stop() {
			return this;
		}

		@Override
		public TrainState leave() {
			return DEPARTED;
		}
	};

	private static final TrainState DEPARTED = new TrainState() {
		@Override
		public String toString() {
			return "DEPARTED";
		}

		@Override
		public TrainState stop() {
			throw new IllegalTrainStateTransitionException("You can't stop if you've started");
		}

		@Override
		public TrainState leave() {
			return this;
		}
	};

	public static TrainState ofInArrival() {
		return IN_ARRIVAL;
	}

	public static TrainState ofStopped() {
		return STOPPED;
	}

	public static TrainState ofDeparted() {
		return DEPARTED;
	}

	public abstract TrainState stop();

	public abstract TrainState leave();

	public boolean isStopped() {
		return this == STOPPED;
	}

	public boolean isInArrival() {
		return this == IN_ARRIVAL;
	}

	public boolean isDeparted() {
		return this == DEPARTED;
	}
}
