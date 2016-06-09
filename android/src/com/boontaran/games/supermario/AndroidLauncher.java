package com.boontaran.games.supermario;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.boontaran.games.supermario.SuperMario;
import com.igaworks.IgawCommon;
import com.igaworks.liveops.IgawLiveOps;
import com.igaworks.liveops.net.AdbrixRequestManager;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer=false;
		cfg.useCompass=false;

		//igaworks integration
		IgawCommon.startApplication(AndroidLauncher.this);
		IgawCommon.setUserId("user_hackest");
		IgawLiveOps.initialize(AndroidLauncher.this);

		initialize(new SuperMario(),cfg);
	}
	@Override
	protected void onResume() {
		super.onResume();
		IgawCommon.startSession(AndroidLauncher.this);
		IgawLiveOps.resume(AndroidLauncher.this);
	}

	@Override
	protected  void onPause() {
		super.onPause();
		IgawCommon.endSession();
	}

}
