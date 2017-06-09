package cancionesinfantiles.toycantando;

import android.app.Application;

import cancionesinfantiles.toycantando.utils.FontLoader;

public class GameApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		FontLoader.loadFonts(this);

	}
}
