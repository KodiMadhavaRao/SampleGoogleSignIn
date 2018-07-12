package com.check.firebasegoogle.app;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 123;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        SignInButton signInButton = new SignInButton(this);
        signInButton.setId(9869);
        signInButton.setVisibility(View.VISIBLE);
        linearLayout.addView(signInButton);
        signInButton.setForegroundGravity(Gravity.CENTER);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
        signInButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout successUi=new LinearLayout(this);
        successUi.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        successUi.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(successUi);
        successUi.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        successUi.setVisibility(View.INVISIBLE);
        successUi.setId(8888);

        ImageView imageView=new ImageView(this);
        imageView.setMinimumHeight(150);
        imageView.setMinimumWidth(150);
        successUi.addView(imageView);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        imageView.setId(132);

        ProgressBar progressBar=new ProgressBar(this);
        progressBar.setIndeterminate(true);
        successUi.addView(progressBar);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(150,150));
        progressBar.setId(999);
        progressBar.setVisibility(View.VISIBLE);

        TextView userName=new TextView(this);
        userName.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f);
        userName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        userName.setId(121);
        userName.setTextColor(Color.BLACK);
        successUi.addView(userName);

        TextView userEmail=new TextView(this);
        userEmail.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        userEmail.setId(122);
        userEmail.setTextColor(Color.BLACK);
        successUi.addView(userEmail);

        setContentView(linearLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onClick(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Test", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account!=null) {
            ((SignInButton) findViewById(9869)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(8888)).setVisibility(View.VISIBLE);
            if (account.getPhotoUrl() != null) {
                ((ProgressBar)findViewById(999)).setVisibility(View.VISIBLE);
                Glide.with(this).load(account.getPhotoUrl()).listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        ((ProgressBar)findViewById(999)).setVisibility(View.GONE);
                        return false;
                    }
                }).into(((ImageView) findViewById(132)));
            } else {
                ((ImageView) findViewById(132)).setImageResource(R.drawable.test);
            }
            ((TextView) findViewById(121)).setText(account.getDisplayName());
            ((TextView) findViewById(122)).setText(account.getEmail());
        }
    }
}
