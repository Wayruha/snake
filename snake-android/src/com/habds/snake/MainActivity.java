package com.habds.snake;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.habds.snake.RootGame;

public class MainActivity extends AndroidApplication implements IActivityRequestHandler {
	protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                adView.setVisibility(View.GONE);

            if (msg.what == 1) {
                adView.setVisibility(View.VISIBLE);
                AdRequest adRequest = new AdRequest();
                adView.loadAd(adRequest);
            }
        }
    };
    private AdView adView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
      //создЄм главный слой
        RelativeLayout layout = new RelativeLayout(this);
        //устанавливаем флаги, которые устанавливались в методе initialize() вместо нас
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        //представление дл€ LibGDX
        View gameView = initializeForView(new RootGame(this), cfg);

        //представление и настройка AdMob
        adView = new AdView(this, AdSize.BANNER, "ca-app-pub-5800651875796716/8438341780");
        AdRequest adRequest = new AdRequest();
        //adRequest.addTestDevice("5E3A238B325C5A8EA1BDEDC739DF5F1B");
        adView.loadAd(adRequest);
        //добавление представление игрык слою
        layout.addView(gameView);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //добавление представление рекламы к слою
        layout.addView(adView, adParams);

        //всЄ соедин€ем в одной слое
        setContentView(layout);
    }

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? 1 : 0);
	}
} 