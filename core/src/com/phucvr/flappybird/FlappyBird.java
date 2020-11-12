package com.phucvr.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.CircleShape;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapState = 0;
	float birdY;
	int gameState = 0;
	float velocity = 0;
	float gravity = 2;
	Texture topTube;
	Texture bottomTube;
	int numberOfTube = 4;
	float gap = 400;
	float[] tubeOffset = new float[numberOfTube];
	Random random = new Random();
	float[] tubeX = new float[numberOfTube];
	float tubeVelocity = 4;
	float distanceBetweenTubes;

	Texture gameOver;

	int scores = 0;
	int scoringTube = 0;
	BitmapFont bitmapFont;

	ShapeRenderer shapeRenderer;
	Circle circle;

	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");
		gameOver = new Texture("gameOver.png");

		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.WHITE);
		bitmapFont.getData().scale(10);

		shapeRenderer = new ShapeRenderer();
		circle = new Circle();

		topTubeRectangle = new Rectangle[numberOfTube];
		bottomTubeRectangle = new Rectangle[numberOfTube];
		distanceBetweenTubes = (float) (Gdx.graphics.getWidth() / 2 );


		birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;

		for (int i = 0 ; i < numberOfTube ; i++) {

			tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);


			tubeX[i] = (float) (Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth()
								+ i * distanceBetweenTubes);

			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();

		}

	}

	public void gameStart() {
		birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;

		scoringTube = 0;
		scores = 0;
		velocity = 0;

		for (int i = 0 ; i < numberOfTube ; i++) {

			tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);


			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth()
					+ i * distanceBetweenTubes;

			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();

		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth() , Gdx.graphics.getHeight());

		if (gameState == 1) {

			if (Gdx.input.justTouched()) {

				velocity = -25;
			}

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {

				scores++;

				if (scoringTube < numberOfTube - 1) {

					scoringTube++;

				} else {

					scoringTube = 0;

				}

			}

			for (int i = 0 ; i < numberOfTube ; i++) {

				if (tubeX[i] < -topTube.getWidth()) {

					tubeX[i] += numberOfTube * distanceBetweenTubes;

				} else {

					tubeX[i] -= tubeVelocity;

				}

				batch.draw(topTube,tubeX[i],
						Gdx.graphics.getHeight() / 2  + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube,tubeX[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangle[i] = new Rectangle(tubeX[i],
						Gdx.graphics.getHeight() / 2  + gap / 2 + tubeOffset[i],
						topTube.getWidth(),
						topTube.getHeight());
				bottomTubeRectangle[i] = new Rectangle(tubeX[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
						bottomTube.getWidth(),
						bottomTube.getHeight());
			}

			if (birdY > 0) {
				velocity += gravity;
				birdY -= velocity;
			} else {

				gameState = 2;

			}



			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}

		} else if(gameState == 0) {

			if (Gdx.input.justTouched()) {
				gameState = 1;
			}

		} else if (gameState == 2) {

			batch.draw(gameOver,Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2,
					Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);

			if (Gdx.input.justTouched()) {

				gameState = 1;

				gameStart();

			}

		}

		batch.draw(birds[flapState],Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2 ,
				birdY);

		bitmapFont.draw(batch,Integer.toString(scores),200,200);

		batch.end();

		circle.set(Gdx.graphics.getWidth() / 2
					,birdY + birds[flapState].getWidth() / 2
					,birds[flapState].getWidth() / 2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(circle.x,circle.y,circle.radius);
		for (int i = 0 ; i < numberOfTube ; i++) {
			/*
			shapeRenderer.rect(tubeX[i],
					Gdx.graphics.getHeight() / 2  + gap / 2 + tubeOffset[i],
					topTube.getWidth(),
					topTube.getHeight());

			shapeRenderer.rect(tubeX[i],
					Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
					bottomTube.getWidth(),
					bottomTube.getHeight());
			*/
			if (Intersector.overlaps(circle,topTubeRectangle[i])
				|| Intersector.overlaps(circle,bottomTubeRectangle[i]) ) {

				 gameState = 2;

			}

		}
		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
