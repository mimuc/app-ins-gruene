package de.lmu.treeapp.activities.minigames.catchFruits;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.lmu.treeapp.R;
import de.lmu.treeapp.activities.minigames.base.GameActivity_Base;
import de.lmu.treeapp.contentClasses.trees.TreeComponent;
import de.lmu.treeapp.contentData.DataManager;
import de.lmu.treeapp.contentData.database.entities.content.GameCatchFruitsItem;
import de.lmu.treeapp.contentData.database.entities.content.GameCatchFruitsRelations;
import me.samlss.bloom.Bloom;
import me.samlss.bloom.effector.BloomEffector;
import me.samlss.bloom.listener.BloomListener;
import me.samlss.bloom.particle.BloomParticle;
import me.samlss.bloom.shape.ParticleShape;
import me.samlss.bloom.shape.distributor.ParticleShapeDistributor;


public class GameActivity_CatchFruits extends GameActivity_Base {

    //View-Elements
    ProgressBar livebar;
    ProgressBar scorebarLeaf, scorebarFruit;
    TextView tvScore, tvEndScore, tvHighscore, tvEndTitle, tvHighscoreTitle, tvEndLeafScore, tvEndFruitScore, tvEndScoreLabel;
    ConstraintLayout constraintlayout, gameoverFrame;
    ImageView heart, leafIcon, fruitIcon, checkFruit, checkLeaf, squirrelBar;
    Button btnBack, btnRetry;
    Dialog popupGameover;
    Fragment catchFruitFragment;

    //Final-Variables
    private GameCatchFruitsRelations catchFruitsContent;
    private final int goalLeaf = 10;
    private final int goalFruit = 10;
    private final int life = 60000; //milliseconds
    private final int subtractLive = 1000;
    private final int subtractLiveFail = 3000;


    //Variables
    private final List<GameCatchFruitsItem> correctTreeObjs = new ArrayList<>();
    int fullbar = life;
    final Handler fHandler = new Handler();
    final Handler tHandler = new Handler();
    Runnable fRun, tRun;
    private final Random random = new Random();
    private List<GameCatchFruitsItem> catchFruitsObjects;
    private int curScoreLeaf = 0;
    private int curScoreFruit = 0;
    private Animation bounceanim;
    private Animation fadeanim;
    int width;
    int imageSize = 200;
    int idCounter = 0;
    int intervalSize = 1500;
    boolean gameoverFlag = true;
    boolean instructions = true;
    int correctFlag = 0;
    int heartFlag = 0;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    List<ImageButton> items = new ArrayList<>();
    List<GameCatchFruitsItem> trees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catchFruitsContent = (GameCatchFruitsRelations) gameContent;
        catchFruitsObjects = catchFruitsContent.getItems();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String extraString = extras.getString("SHOW_INSTRUCTIONS");
                if (extraString != null && extraString.equals("yes")) {
                    instructions = false;
                }
            }
        }

        getGameState().subscribe(() -> {
            // show instruction screen only for first round
            if (instructions) {
                catchFruitFragment = new CatchFruits_StartScreen();
                Bundle b = new Bundle();
                b.putString("treeName", parentTree.getName());
                catchFruitFragment.setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, catchFruitFragment).commit();
            } else {
                startGame();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game__catch_fruits;
    }

    public void startGame() {

        if (instructions) {
            getSupportFragmentManager().beginTransaction()
                    .detach(catchFruitFragment).commit();
        }

        //Layout
        constraintlayout = findViewById(R.id.catch_fruit_layout);
        popupGameover = new Dialog(this);
        popupGameover.setContentView(R.layout.popup_catch_gameover);
        popupGameover.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameoverFrame = popupGameover.findViewById(R.id.gameover_container);

        //Progressbar
        livebar = findViewById(R.id.livebar);
        scorebarLeaf = findViewById(R.id.scorebar_leaf);
        scorebarLeaf.setMax(goalLeaf);
        scorebarFruit = findViewById(R.id.scorebar_fruit);
        scorebarFruit.setMax(goalFruit);

        //ImageView
        heart = findViewById(R.id.heart_icon);
        leafIcon = findViewById(R.id.leaf_icon);
        fruitIcon = findViewById(R.id.fruit_icon);
        squirrelBar = popupGameover.findViewById(R.id.imageViewBar);
        checkFruit = popupGameover.findViewById(R.id.end_fruit_check_icon);
        checkLeaf = popupGameover.findViewById(R.id.end_leaf_check_icon);

        //Textview
        tvScore = findViewById(R.id.tv_score);
        tvScore.setText(String.valueOf(curScoreFruit + curScoreLeaf));
        tvHighscore = popupGameover.findViewById(R.id.textView_highscore);
        tvHighscore.setText(String.valueOf(gameStateScore.highscore));
        tvEndScore = popupGameover.findViewById(R.id.textView_userScore);
        tvEndScoreLabel = popupGameover.findViewById(R.id.textViewUserScoreLabel);
        tvEndTitle = popupGameover.findViewById(R.id.textView_title_Score);
        tvHighscoreTitle = popupGameover.findViewById(R.id.textView_title_highscore);
        tvEndLeafScore = popupGameover.findViewById(R.id.textView_end_leaf_score);
        tvEndFruitScore = popupGameover.findViewById(R.id.textView_end_fruit_score);


        //Button
        btnBack = popupGameover.findViewById(R.id.btn_catchfruits_back);
        btnRetry = popupGameover.findViewById(R.id.btn_catchfruits_retry);

        btnBack.setOnClickListener(v -> {
            popupGameover.dismiss();
            if (isDone()) {
                onSuccess();
            } else {
                onBackPressed();
            }
        });

        btnRetry.setOnClickListener(v -> {
            popupGameover.dismiss();
            if (isDone()) {
                DataManager.getInstance(getApplicationContext()).setGameCompleted(parentCategory, gameContent.getId(), parentTree).subscribe();
            }
            resetGame();
        });

        //Animation
        Animation heartanim = AnimationUtils.loadAnimation(this, R.anim.heart_beat);
        bounceanim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        fadeanim = AnimationUtils.loadAnimation(this, R.anim.fade);
        heart.startAnimation(heartanim);

        setupCorrectItems();
        generateAllFallingItems(100);
        startTimer();
        automizeFallingFruits();
    }


    /**
     * Generate a new ImageButton with the image of a fruit or a leaf
     *
     * @param drawableID String
     */
    private ImageButton generateItem(String drawableID) {
        int imageId = getResources().getIdentifier(drawableID, "drawable", getPackageName());
        ImageButton imageButton = new ImageButton(GameActivity_CatchFruits.this);
        imageButton.setId(idCounter);
        idCounter++;
        if (drawableID.equals("@drawable/ic_heart")) {
            imageButton.setLayoutParams(new LinearLayout.LayoutParams(imageSize - 50, imageSize - 50));
        } else {
            imageButton.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize));
        }
        imageButton.setImageResource(imageId);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setClickable(true);
        imageButton.setPadding(10, 10, 10, 10);
        imageButton.setFocusable(true);
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setX(getRandomXPosition(imageSize));
        imageButton.setY(-imageSize);
        imageButton.setElevation(0);
        constraintlayout.addView(imageButton);
        return imageButton;
    }

    /**
     * Animates an item to fall to the bottom of the screen
     */
    private void animateItem(final ImageButton card, final GameCatchFruitsItem fruitObject) {
        float bottomOfScreen = getResources().getDisplayMetrics()
                .heightPixels - imageSize;
        ObjectAnimator animation = ObjectAnimator.ofFloat(card, "translationY", bottomOfScreen);
        if (livebar.getProgress() > 50000) {
            animation.setDuration(4500);
        } else if (livebar.getProgress() > 30000) {
            animation.setDuration(4100);
        } else {
            animation.setDuration(3700);
        }
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (constraintlayout.getViewById(card.getId()) != null) {
                    if (fruitObject != null && fruitObject.treeId == treeId) subtractLive(false);
                    destroyItem(card);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                destroyItem(card);
            }
        });
        animation.start();
        card.setOnClickListener(v -> {
            card.setClickable(false);
            if (livebar.getProgress() > 0) { // Disable click-events when Livebar is empty
                if (fruitObject == null || fruitObject.treeId == treeId) {
                    if (fruitObject != null) addScore(fruitObject.treeComponent);
                    else addLive();

                    // particle animation using Bloom (https://github.com/samlss/Bloom)
                    Bloom.with(GameActivity_CatchFruits.this)
                            .setParticleRadius(10)
                            .setShapeDistributor(new ParticleShapeDistributor() {
                                @Override
                                public ParticleShape getShape(BloomParticle particle) {
                                    return new OvalShape(2, 5, particle.getRadius());//set rounded effect.
                                }
                            })
                            .setBloomListener(new BloomListener() {
                                @Override
                                public void onBegin() {
                                    //on bloom begin
                                }

                                @Override
                                public void onEnd() {
                                    animation.cancel();
                                }
                            })
                            .setEffector(new BloomEffector.Builder()
                                    .setDuration(400)
                                    .setScaleRange(0.5f, 0.9f)
                                    .setFadeOut(350)
                                    .setAnchor(imageSize / 2f, imageSize / 2f)
                                    .build())
                            .boom(card);
                    card.setVisibility(View.INVISIBLE);
                } else {
                    // shake animation
                    subtractLive(true);
                    Animation shake;
                    shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    card.startAnimation(shake);
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(50);
                    }
                }
                animation.cancel();

            }

        });
    }


    /**
     * Destroys an item when it is not needed anymore
     */
    private void destroyItem(ImageButton item) {
        constraintlayout.removeView(item);
    }


    /**
     * Generate a certain amount of leafs, fruits and hearts
     */
    private void generateAllFallingItems(int amount) {
        //create Fruit/Leaf and fire Fall-animation
        for (int i = 0; i < amount; i++) {
            GameCatchFruitsItem nextItem;
            ImageButton item;

            if (i - correctFlag >= (random.nextInt(3) + 1)) { //at least every 5. item should be a correct item
                correctFlag = i;
                nextItem = correctTreeObjs.get(random.nextInt(correctTreeObjs.size()));
                item = generateItem(nextItem.content);
            } else if (i >= (life * 0.3 / intervalSize) && i - heartFlag >= random.nextInt(3) + 5) {
                nextItem = null;
                item = generateItem("@drawable/ic_heart");
                heartFlag = i;
            } else {
                nextItem = catchFruitsObjects.get(random.nextInt(catchFruitsObjects.size()));
                item = generateItem(nextItem.content);
            }

            items.add(item);
            trees.add(nextItem);
        }
    }


    /**
     * Automize the falling of the fruits
     */
    private void automizeFallingFruits() {
        fRun = new Runnable() {
            int intervalCounter = 0;
            int fruitInterval = intervalSize;//time interval after which the next fruit starts
            int currentItem = 0;

            @Override
            public void run() {

                if (currentItem == items.size() - 1) {
                    currentItem = 0;
                    items.clear();
                    trees.clear();
                    generateAllFallingItems(30);
                } else {
                    animateItem(items.get(currentItem), trees.get(currentItem));
                }
                currentItem++;


                //shorten intervals between fruit-generation
                if (intervalCounter >= 10 && intervalCounter <= 40) { //after 10 fruits start reducing time interval until its smaller than 1460 ms..else the objects overlap
                    fruitInterval = fruitInterval - intervalCounter;
                } else if (fruitInterval >= 900) {
                    fruitInterval--;
                }

                intervalCounter++;

                fHandler.postDelayed(this, fruitInterval);
            }
        };
        fHandler.post(fRun);
    }

    /**
     * A Timer updates the Progressbar every Millisecond & checks wether a correct object fell out of screen
     */
    private void startTimer() {

        tRun = new Runnable() {
            @Override
            public void run() {
                livebar.setProgress(fullbar);
                if (livebar.getProgress() == 0 && gameoverFlag) {
                    gameoverFlag = false; //gamelflag to make sure, that gameover wont be called more than once.
                    gameover();
                }
                fullbar -= 100;

                tHandler.postDelayed(this, 100);
            }
        };
        tHandler.post(tRun);
    }

    /**
     * If user clicks wrong fruit/leaf Live will be subtracted by a few seconds
     */
    private void subtractLive(boolean falseFruit) {
        int loss = subtractLive;
        if (falseFruit) {
            loss = subtractLiveFail;
        }

        if (fullbar > 0) {
            createStateAnim(heart.getX() + heart.getWidth() + 10, livebar.getY() - 30, "-" + loss / 1000, R.color.red, -50);
        }
        livebar.setProgress(livebar.getProgress() - loss);
        fullbar = livebar.getProgress() - loss;

    }

    /**
     * If user clicks correct fruit/leaf Live will be increased by a few seconds
     */
    private void addLive() {
        fullbar += subtractLive;
        createStateAnim(heart.getX() + heart.getWidth() + 50, livebar.getY() - 30, "+" + subtractLiveFail / 1000, R.color.forest, -50);
        livebar.setProgress(fullbar);
    }

    /***If user clicks correct fruit/leaf score will be increased**/
    private void addScore(TreeComponent component) {
        int scoreTreeObj = 1;
        switch (component) {
            case LEAF:
                curScoreLeaf += scoreTreeObj;
                tvScore.setText(String.valueOf(curScoreFruit + curScoreLeaf));
                scorebarLeaf.setProgress(curScoreLeaf);
                if (curScoreLeaf == goalLeaf) {
                    leafIcon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.forest)));
                    leafIcon.startAnimation(bounceanim);
                }
                break;
            case FRUIT:
                curScoreFruit += scoreTreeObj;
                tvScore.setText(String.valueOf(curScoreFruit + curScoreLeaf));
                scorebarFruit.setProgress(curScoreFruit);
                if (curScoreFruit == goalFruit) {
                    fruitIcon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.forest)));
                    fruitIcon.startAnimation(bounceanim);
                }
                break;
        }
        createStateAnim(tvScore.getX() + tvScore.getWidth() - 50, tvScore.getY(), "+" + scoreTreeObj, R.color.forest, 0);
    }


    private int getRandomXPosition(int imageSize) {
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        int div = width / (imageSize);
        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < div; i++) {
            positions.add(imageSize * i);
        }
        int extra = (width - imageSize) - positions.get(positions.size() - 1);
        int randomPosition = positions.get(random.nextInt(positions.size()));
        return randomPosition + (extra / 2);
    }

    /**
     * Setup the correct Leaf and Fruit as Objects -> later use in automization
     */
    private void setupCorrectItems() {
        for (GameCatchFruitsItem obj : catchFruitsObjects) {
            if (obj.treeId == treeId) {
                correctTreeObjs.add(obj);
            }
        }
    }

    /**
     * Creates a textview which shows the score done or live lost as small animation
     */
    private void createStateAnim(float x, float y, String scoreTxt, int tintColor, int goalY) {
        TextView tvState = new TextView(this);
        tvState.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvState.setGravity(Gravity.CENTER);
        tvState.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        tvState.setText(scoreTxt);
        tvState.setX(x);
        tvState.setY(y);
        tvState.setBackground(ContextCompat.getDrawable(this, R.drawable.card_wanted_circle_icon));
        tvState.setBackgroundTintList(ContextCompat.getColorStateList(this, tintColor));
        tvState.setTextColor(Color.WHITE);
        tvState.setElevation(15);
        constraintlayout.addView(tvState);
        tvState.startAnimation(fadeanim);//start fade-Animation

        ObjectAnimator animation = ObjectAnimator.ofFloat(tvState, "translationY", goalY);
        animation.setDuration(500);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                constraintlayout.removeView(tvState);
            }
        });
        animation.start();
    }

    /**
     * Initialize gameover-screen
     */
    private void gameover() {
        //stop handler
        fHandler.removeCallbacks(fRun);
        tHandler.removeCallbacks(tRun);
        // stop animation of heart-icon
        heart.clearAnimation();
        //Prepare gameover popup
        prepareEndView();
    }

    /**
     * Creates Popup with content depending on scoring
     */
    private void prepareEndView() {
        if ((this).isFinishing()) return; //else App crashes -> due to the dialog

        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GameCatchFruitsItem item = correctTreeObjs.get(random.nextInt(correctTreeObjs.size()));
            int imageId = getResources().getIdentifier(item.content, "drawable", getPackageName());
            ids.add(imageId);
        }


        ImageView starImageView = popupGameover.findViewById(R.id.imageViewStar1);
        starImageView.setImageResource(ids.get(0));
        ImageView starImageView2 = popupGameover.findViewById(R.id.imageViewStar2);
        starImageView2.setImageResource(ids.get(1));
        ImageView starImageView3 = popupGameover.findViewById(R.id.imageViewStar3);
        starImageView3.setImageResource(ids.get(2));
        ImageView starImageView4 = popupGameover.findViewById(R.id.imageViewStar4);
        starImageView4.setImageResource(ids.get(3));
        ImageView starImageView5 = popupGameover.findViewById(R.id.imageViewStar5);
        starImageView5.setImageResource(ids.get(4));


        if (isDone()) {
            tvEndTitle.setText(R.string.popup_win_title_done);
            btnBack.setText(getResources().getString(R.string.button_done));
            squirrelBar.setImageResource(R.drawable.ic_mascott_true_only_bar);
            starImageView.animate()
                    .translationY(2000)
                    .setDuration(2800)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView.setVisibility(View.GONE);
                        }
                    });

            starImageView2.animate()
                    .translationY(2000)
                    .setDuration(3000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView2.setVisibility(View.GONE);
                        }
                    });

            starImageView3.animate()
                    .translationY(2000)
                    .setDuration(2500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView3.setVisibility(View.GONE);
                        }
                    });

            starImageView4.animate()
                    .translationY(2000)
                    .setDuration(2200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView4.setVisibility(View.GONE);
                        }
                    });

            starImageView5.animate()
                    .translationY(2000)
                    .setInterpolator(new AccelerateInterpolator())
                    .setDuration(3000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            starImageView5.setVisibility(View.GONE);
                        }
                    });
        } else {
            squirrelBar.setImageResource(R.drawable.ic_mascott_false_squirrel_bar);
            tvEndTitle.setText(R.string.popup_loose_title_not_sufficient);
            tvEndTitle.setTextColor(getResources().getColor(R.color.asphalt));
            btnBack.setText(getResources().getString(R.string.button_back));
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelTailAnimationNegative, 700);
            (new Handler(Looper.getMainLooper())).postDelayed(this::squirrelBarAnimationNegative, 700);
            starImageView.setVisibility(View.GONE);
            starImageView2.setVisibility(View.GONE);
            starImageView3.setVisibility(View.GONE);
            starImageView4.setVisibility(View.GONE);
            starImageView5.setVisibility(View.GONE);
        }

        if (curScoreLeaf >= goalLeaf) checkLeaf.setVisibility(View.VISIBLE);
        if (curScoreFruit >= goalFruit) checkFruit.setVisibility(View.VISIBLE);

        if (curScoreFruit + curScoreLeaf > gameStateScore.highscore) {
            gameStateScore.highscore = curScoreFruit + curScoreLeaf;
            saveGameState().subscribe();

            tvHighscoreTitle.setText(R.string.game_new_highscore);
            tvEndScore.setVisibility(View.GONE);
            tvEndScoreLabel.setVisibility(View.GONE);
            tvHighscore.setText(String.valueOf(curScoreFruit + curScoreLeaf));
        } else {
            tvHighscoreTitle.setText(R.string.game_no_highscore);
            tvEndScore.setText(String.valueOf(curScoreFruit + curScoreLeaf));
        }
        tvEndLeafScore.setText(getString(R.string.game_score_comparison, curScoreLeaf, goalLeaf));
        tvEndFruitScore.setText(getString(R.string.game_score_comparison, curScoreFruit, goalFruit));

        popupGameover.show();
        Window window = popupGameover.getWindow();
        window.setLayout(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);

    }

    /**
     * Check if the user has won the minigame
     **/
    @Override
    protected boolean isDone() {
        return curScoreLeaf >= goalLeaf && curScoreFruit >= goalFruit;
    }

    private void resetGame() {
        finish();
        String isReset = "yes";
        startActivity(getIntent().putExtra("SHOW_INSTRUCTIONS", isReset));
    }

    private void squirrelBarAnimationNegative() {
        this.squirrelBar = popupGameover.findViewById(R.id.imageViewBar);
        squirrelBar.animate().translationX(15).translationY(10).setDuration(400);
        new Handler(Looper.getMainLooper()).postDelayed(() -> squirrelBar.animate().rotationBy(10).translationX(15).setDuration(400), 10);

    }

    private void squirrelTailAnimationNegative() {
        ImageView squirrelTail = popupGameover.findViewById(R.id.imageViewTail);
        squirrelTail.animate().rotationBy(-10).setDuration(400);

    }
}