/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spit.matrix15;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE = "com.spit.matrix15.extraImage";
    private static final String EXTRA_TITLE = "com.spit.matrix15.extraTitle";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView descriptionText;
    private TextView priceText;
    private TextView venueText;
    private TextView highlightText1;
    private TextView highlightText2;
    private TextView highlightText3;
    private TextView contactText;
    private TextView emailText;

    public static void navigate(AppCompatActivity activity, View transitionImage, ViewModel viewModel) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_IMAGE, viewModel.getImageUrl());
        intent.putExtra(EXTRA_TITLE, viewModel.getText());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_detail);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String itemTitle = getIntent().getStringExtra(EXTRA_TITLE);
        final Event event = Event.find(Event.class, "event_name = ?", itemTitle).get(0);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event.isFavorite == null) {
                    event.isFavorite = "false";
                }
                if (!event.isFavorite.equals("true")) {
                    event.isFavorite = "true";
                    event.save();
                    Snackbar.make(findViewById(android.R.id.content), itemTitle + " added to favorites", Snackbar.LENGTH_SHORT).show();
                } else {
                    event.isFavorite = "false";
                    event.save();
                    Snackbar.make(findViewById(android.R.id.content), itemTitle + " removed from favorites", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        descriptionText = (TextView) findViewById(R.id.description);
        descriptionText.setText(event.eventDescription);

        priceText = (TextView) findViewById(R.id.txt_reg_fee);
        if(Integer.parseInt(event.fee) != 0)
            priceText.setText("\u20B9 " + event.fee);
        else
            priceText.setText("FREE");

        venueText = (TextView) findViewById(R.id.txt_venue);
        if (event.venue != null)
            venueText.setText(event.venue);

        highlightText1 = (TextView) findViewById(R.id.txt_hglt1);
        if (event.eventHighlight1 != null)
            highlightText1.setText(getString(R.string.bullet) + " " + event.eventHighlight1);

        highlightText2 = (TextView) findViewById(R.id.txt_hglt2);
        if (event.eventHighlight2 != null) {
            highlightText2.setText(getString(R.string.bullet) + " " + event.eventHighlight2);
            highlightText2.setVisibility(View.VISIBLE);
        }

        highlightText3 = (TextView) findViewById(R.id.txt_hglt3);
        if (event.eventHighlight3 != null) {
            highlightText3.setText(getString(R.string.bullet) + " " + event.eventHighlight3);
            highlightText3.setVisibility(View.VISIBLE);
        }

        contactText = (TextView) findViewById(R.id.contacts);
        contactText.setText("Contact: " + event.contact1 + ", " + event.contact2);

        emailText = (TextView) findViewById(R.id.email);
        emailText.setText("Email: " + event.email);

        final ImageView image = (ImageView) findViewById(R.id.image);
        Picasso.with(this)
                .load(getIntent().getStringExtra(EXTRA_IMAGE))
                .placeholder(R.drawable.snackbar_background)
                .fit()
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                applyPalette(palette);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        Log.d("P", "Couldn't load " + getIntent().getStringExtra(EXTRA_IMAGE));
                        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
                        image.setImageBitmap(bitmap);
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                applyPalette(palette);
                            }
                        });
                    }
                });

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(itemTitle);
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.accent));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }
}
