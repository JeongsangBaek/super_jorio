package com.boontaran.games.supermario;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.boontaran.games.supermario.SuperMario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.igaworks.IgawCommon;
import com.igaworks.liveops.IgawLiveOps;
import com.igaworks.liveops.net.AdbrixRequestManager;

public class AndroidLauncher extends AndroidApplication {
	private FirebaseAnalytics mFirebaseAnalytics;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mListener;
	private static String TAG = "SuperJorio";
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer=false;
		cfg.useCompass=false;

		//igaworks integration
		IgawCommon.startApplication(AndroidLauncher.this);
		IgawCommon.setUserId("hackest@gmaii.com");
		IgawLiveOps.initialize(AndroidLauncher.this);

		initialize(new SuperMario(),cfg);
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

		String id = "hackest";
		String name = "Jeongsang Baek";


		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
		bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
		bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
		mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
		mFirebaseAnalytics.setUserProperty("player_name", name);

		mAuth = FirebaseAuth.getInstance();
		String email = "hackest@gmail.com";
		String password = "hackest1234";
		mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(AndroidLauncher.this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				Log.d(TAG, "signInWithEmail:onComplete" + task.isSuccessful());
				if (!task.isSuccessful()) {
					Log.w(TAG, "signInWithEmail", task.getException());
					Toast.makeText(AndroidLauncher.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		mListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					//user signed in
					Log.d(TAG, "Auth state changed: User signed in");
				} else {
					//user is signed out
					Log.d(TAG, "Auth state changed: User signed in");
				}
			}
		};
		FirebaseCrash.log("Activity created");
		FirebaseCrash.report(new Exception("My first Android non-fatal error"));

		//Netmera
		NetmeraProperties prop = new NetmeraProperties.Builder("");
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

	@Override
	public void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mListener != null) {
			mAuth.removeAuthStateListener(mListener);
		}
	}
}
