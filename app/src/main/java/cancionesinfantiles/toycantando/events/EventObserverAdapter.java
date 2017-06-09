package cancionesinfantiles.toycantando.events;

import cancionesinfantiles.toycantando.events.engine.FlipDownCardsEvent;
import cancionesinfantiles.toycantando.events.engine.GameWonEvent;
import cancionesinfantiles.toycantando.events.engine.HidePairCardsEvent;
import cancionesinfantiles.toycantando.events.ui.BackGameEvent;
import cancionesinfantiles.toycantando.events.ui.FlipCardEvent;
import cancionesinfantiles.toycantando.events.ui.NextGameEvent;
import cancionesinfantiles.toycantando.events.ui.ResetBackgroundEvent;
import cancionesinfantiles.toycantando.events.ui.ThemeSelectedEvent;
import cancionesinfantiles.toycantando.events.ui.DifficultySelectedEvent;
import cancionesinfantiles.toycantando.events.ui.StartEvent;


public class EventObserverAdapter implements EventObserver {

	public void onEvent(FlipCardEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(DifficultySelectedEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(HidePairCardsEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(FlipDownCardsEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(StartEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(ThemeSelectedEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(GameWonEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(BackGameEvent event) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void onEvent(NextGameEvent event) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void onEvent(ResetBackgroundEvent event) {
		throw new UnsupportedOperationException();		
	}

}
