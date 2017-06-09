package cancionesinfantiles.toycantando.events;


/**
 * The event that is invoked from the low levels of this game (like cancionesinfantiles.toycantando.engine) and
 * not from the cancionesinfantiles.toycantando.ui.
 * 
 * @author sromku
 */
public interface Event {

	String getType();
	
}
