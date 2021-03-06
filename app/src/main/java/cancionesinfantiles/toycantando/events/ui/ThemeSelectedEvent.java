package cancionesinfantiles.toycantando.events.ui;

import cancionesinfantiles.toycantando.events.AbstractEvent;
import cancionesinfantiles.toycantando.events.EventObserver;
import cancionesinfantiles.toycantando.themes.Theme;

public class ThemeSelectedEvent extends AbstractEvent {

	public static final String TYPE = ThemeSelectedEvent.class.getName();
	public final Theme theme;

	public ThemeSelectedEvent(Theme theme) {
		this.theme = theme;
	}

	@Override
	protected void fire(EventObserver eventObserver) {
		eventObserver.onEvent(this);
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
