package cancionesinfantiles.toycantando.themes;
import java.util.ArrayList;

import android.graphics.Bitmap;

import cancionesinfantiles.toycantando.common.Shared;
import cancionesinfantiles.toycantando.utils.Utils;
public class Themes {
	public static String URI_DRAWABLE = "drawable://";
	public static Theme createAnimalsTheme() {
		Theme theme = new Theme();
		theme.id = 1;
		theme.name = "Animals";
		theme.backgroundImageUrl = URI_DRAWABLE + "back_animals";
		theme.tileImageUrls = new ArrayList<String>();
		// 40 drawables
		for (int i = 1; i <= 28; i++) {
			theme.tileImageUrls.add(URI_DRAWABLE + String.format("animals_%d", i));
		}
		return theme;
	}
	public static Theme createMonstersTheme() {
		Theme theme = new Theme();
		theme.id = 2;
		theme.name = "Monsters";
		theme.backgroundImageUrl = URI_DRAWABLE + "back_monsters";
		theme.tileImageUrls = new ArrayList<String>();
		// 40 drawables
		for (int i = 1; i <= 40; i++) {
			theme.tileImageUrls.add(URI_DRAWABLE + String.format("mosters_%d", i));
		}
		return theme;
	}
	public static Theme createFoodsTheme() {
		Theme theme = new Theme();
		theme.id = 3;
		theme.name = "Foods";
		theme.backgroundImageUrl = URI_DRAWABLE + "back_foods";
		theme.tileImageUrls = new ArrayList<String>();
		// 40 drawables
		for (int i = 1; i <= 40; i++) {
			theme.tileImageUrls.add(URI_DRAWABLE + String.format("foods_%d", i));
		}
		return theme;
	}
	public static Bitmap getBackgroundImage(Theme theme) {
		String drawableResourceName = theme.backgroundImageUrl.substring(Themes.URI_DRAWABLE.length());
		int drawableResourceId = Shared.context.getResources().getIdentifier(drawableResourceName, "drawable", Shared.context.getPackageName());
		Bitmap bitmap = Utils.scaleDown(drawableResourceId, Utils.screenWidth(), Utils.screenHeight());
		return bitmap;
	}

}
