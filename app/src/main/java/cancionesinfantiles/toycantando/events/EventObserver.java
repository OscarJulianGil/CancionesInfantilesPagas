package cancionesinfantiles.toycantando.events;

import cancionesinfantiles.toycantando.events.engine.FlipDownCardsEvent;
import cancionesinfantiles.toycantando.events.engine.GameWonEvent;
import cancionesinfantiles.toycantando.events.engine.HidePairCardsEvent;
import cancionesinfantiles.toycantando.events.ui.BackGameEvent;
import cancionesinfantiles.toycantando.events.ui.DifficultySelectedEvent;
import cancionesinfantiles.toycantando.events.ui.FlipCardEvent;
import cancionesinfantiles.toycantando.events.ui.NextGameEvent;
import cancionesinfantiles.toycantando.events.ui.ResetBackgroundEvent;
import cancionesinfantiles.toycantando.events.ui.StartEvent;
import cancionesinfantiles.toycantando.events.ui.ThemeSelectedEvent;


public interface EventObserver {

	void onEvent(FlipCardEvent event);

	void onEvent(DifficultySelectedEvent event);

	void onEvent(HidePairCardsEvent event);

	void onEvent(FlipDownCardsEvent event);

	void onEvent(StartEvent event);

	void onEvent(ThemeSelectedEvent event);

	void onEvent(GameWonEvent event);

	void onEvent(BackGameEvent event);

	void onEvent(NextGameEvent event);

	void onEvent(ResetBackgroundEvent event);

}
