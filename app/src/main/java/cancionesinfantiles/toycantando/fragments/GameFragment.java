package cancionesinfantiles.toycantando.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import cancionesinfantiles.toycantando.R;
import cancionesinfantiles.toycantando.common.Shared;
import cancionesinfantiles.toycantando.events.engine.FlipDownCardsEvent;
import cancionesinfantiles.toycantando.events.engine.GameWonEvent;
import cancionesinfantiles.toycantando.events.engine.HidePairCardsEvent;
import cancionesinfantiles.toycantando.model.Game;
import cancionesinfantiles.toycantando.ui.BoardView;
import cancionesinfantiles.toycantando.ui.PopupManager;
import cancionesinfantiles.toycantando.utils.Clock;
import cancionesinfantiles.toycantando.utils.Clock.OnTimerCount;
import cancionesinfantiles.toycantando.utils.FontLoader;
import cancionesinfantiles.toycantando.utils.FontLoader.Font;

public class GameFragment extends BaseFragment {

	private BoardView mBoardView;
	private TextView mTime;
	private ImageView mTimeImage;
	private InterstitialAd mInterstitialAd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.game_fragment, container, false);
		view.setClipChildren(false);
		((ViewGroup)view.findViewById(R.id.game_board)).setClipChildren(false);
		mTime = (TextView) view.findViewById(R.id.time_bar_text);
		mTimeImage = (ImageView) view.findViewById(R.id.time_bar_image);
		FontLoader.setTypeface(Shared.context, new TextView[] {mTime}, Font.GROBOLD);
		mBoardView = BoardView.fromXml(getActivity().getApplicationContext(), view);
		FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.game_container);
		frameLayout.addView(mBoardView);
		frameLayout.setClipChildren(false);

		// build board
		buildBoard();
		Shared.eventBus.listen(FlipDownCardsEvent.TYPE, this);
		Shared.eventBus.listen(HidePairCardsEvent.TYPE, this);
		Shared.eventBus.listen(GameWonEvent.TYPE, this);

		final AdView adView = (AdView) view.findViewById(R.id.adView);
		adView.loadAd(new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build());
		LoadAds();
		return view;
	}
	
	@Override
	public void onDestroy() {
		Shared.eventBus.unlisten(FlipDownCardsEvent.TYPE, this);
		Shared.eventBus.unlisten(HidePairCardsEvent.TYPE, this);
		Shared.eventBus.unlisten(GameWonEvent.TYPE, this);
		super.onDestroy();
	}

	private void buildBoard() {
		try
		{
			Game game = Shared.engine.getActiveGame();
			int time = game.boardConfiguration.time;
			setTime(time);
			mBoardView.setBoard(game);
			startClock(time);
		}
		catch (Exception ex){}
	}
	
	private void setTime(int time) {
		int min = time / 60;
		int sec = time - min*60;
		mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
	}

	private void startClock(int sec) {
		Clock clock = Clock.getInstance();
		clock.startTimer(sec*1000, 1000, new OnTimerCount() {
			
			@Override
			public void onTick(long millisUntilFinished) {
				setTime((int) (millisUntilFinished/1000));
			}
			
			@Override
			public void onFinish() {
				setTime(0);
			}
		});
	}

	@Override
	public void onEvent(GameWonEvent event) {
		mTime.setVisibility(View.GONE);
		mTimeImage.setVisibility(View.GONE);
		PopupManager.showPopupWon(event.gameState);
		displayInterstitial();
	}

	@Override
	public void onEvent(FlipDownCardsEvent event) {
		mBoardView.flipDownAll();
	}

	@Override
	public void onEvent(HidePairCardsEvent event) {
		mBoardView.hideCards(event.id1, event.id2);
	}
	private void LoadAds(){
		mInterstitialAd = new InterstitialAd(getActivity());
		mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_id));
		// Create an ad request.
		AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
		// Start loading the ad now so that it is ready by the time the user is ready to go to
		// the next level.
		mInterstitialAd.loadAd(adRequestBuilder
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build());

	}

	// Invoke displayInterstitial() when you are ready to display an interstitial.
	public void displayInterstitial() {
		if (mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		} else {
			AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
			mInterstitialAd.loadAd(adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
		}
	}
}
