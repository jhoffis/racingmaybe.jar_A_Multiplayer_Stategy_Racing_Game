package elem;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;

import handlers.SceneHandler;
import scenes.Race;
import sun.misc.Queue;

public class RaceVisual extends Canvas {

	private Race race;
	private Player player;
	private GraphicsEnvironment env;
	private GraphicsDevice device;
	private GraphicsConfiguration config;
	private BufferedImage carImage;
	private BufferedImage tachopointer;
	private BufferedImage tachometer;
	private BufferedImage fastness;
	private AffineTransform identity = new AffineTransform();
	private Font font;
	private boolean startCountDown;
	private boolean running;
	private long startTime;
	private int x;
	private int y;
	private int width;
	private int height;
	private int baller = 0;
	private Color ballColor;
	private int widthTachometer;
	private int heightTachometer;
	private int xTachometer;
	private int yTachometer;
	private double scaleXTachopointer;
	private double scaleYTachopointer;
	private int xTachopointer;
	private int yTachopointer;
	private int xGear;
	private int yGear;
	private int xSpeed;
	private int ySpeed;
	private int xDistance;
	private int yDistance;
	private BufferStrategy bs;
	private Animation background;
	private float blurSpeed;
	private int blurShake;
	private Random r;
	private Animation nitros;

	private Queue<Car> finishedPlayers;
	private boolean finished;
	private BufferedImage resBackground;
	private Animation resShadow;
	private boolean playingFinishingCar;
	private Animation resCar;
	private int resCarWidth;
	private int resCarMovement;

	public RaceVisual(Player player, Race race) {
		this.player = player;
		this.race = race;

		widthTachometer = (int) (Race.WIDTH / 5f);
		heightTachometer = (int) (widthTachometer / 311f * 225f);
		xTachometer = Race.WIDTH - (widthTachometer + (Race.WIDTH / 24));
		yTachometer = Race.HEIGHT - (heightTachometer + (Race.HEIGHT / 24));

		xTachopointer = (int) (xTachometer + widthTachometer / 1.6);
		yTachopointer = (int) (yTachometer + heightTachometer / 1.9);
		scaleXTachopointer = widthTachometer / 380.0;
		scaleYTachopointer = heightTachometer / 270.0;
		// var 1.7 og 1.2
		xGear = (int) (xTachometer + widthTachometer / 1.7);
		yGear = (int) (yTachometer + heightTachometer / 1.15);
		xSpeed = (int) (xTachometer + widthTachometer / 10);
		ySpeed = (int) (yTachometer + heightTachometer / 1.2);
		xDistance = 100;
		yDistance = 100;

		background = new Animation("road", 6);
		nitros = new Animation("nitros", 4);
		resShadow = new Animation("res00", 57);

		font = new Font("Calibri", 0, 54);
		r = new Random();
		blurShake = 3;
		blurSpeed = 220;

		finished = false;

		y = 0;
		startTime = 0;
		startCountDown = false;
		try {

			carImage = ImageIO
					.read(RaceVisual.class.getResourceAsStream("/pics/" + player.getCar().getCarStyle() + ".png"));

			fastness = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/fastness.png"));

			tachopointer = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/tacho.png"));
			// 311 x 225 px
			tachometer = ImageIO.read(RaceVisual.class.getResourceAsStream("/pics/tachometer.png"));

		} catch (IOException e) {
			System.err.println("didn't find the picture you were looking for");
			e.printStackTrace();
		}

	}

	public void tick() {
		if (!finished) {
			if (player.getCar().isGas() && !player.getCar().isClutch()) {
				if (player.getCar().isNOSON()) {
					y = -15;
					x = -16;
					width = Race.WIDTH + 32;
					height = Race.HEIGHT + 16;
					nitros.setCurrentFrame(r.nextInt(4));
				} else {
					y = -9;
					x = -8;
					width = Race.WIDTH + 16;
					height = Race.HEIGHT + 9;
				}
			} else {
				y = -2;
				x = -8;
				width = Race.WIDTH + 16;
				height = Race.HEIGHT + 9;
			}

			background.setCurrentFrame((background.getCurrentFrame() + player.getCar().getSpeedActual() / 100)
					% background.getFrameCount());
		} else {
			if (playingFinishingCar) {
				resShadow.incrementCurrentFrame();

				if (resShadow.getCurrentFrame() == 0) {
					playingFinishingCar = false;
				}
			}

			if (!playingFinishingCar && !finishedPlayers.isEmpty()) {
				try {
					finishedPlayers.dequeue();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				playingFinishingCar = true;
			}
		}
	}

	public void playFinishScene(boolean cheated) {

		finishedPlayers = new Queue<Car>();

		finished = true;

		carImage = null;
		tachopointer = null;
		tachometer = null;
		fastness = null;

		
		resCarWidth = (int) (Race.WIDTH * 1.16f);
		resCarMovement = (resCarWidth + Race.WIDTH) / resShadow.getFrameCount();

		if (!cheated)
			finishedPlayers.enqueue(player.getCar());

	}

	public void addFinish() {
		// FIXME
		finishedPlayers.enqueue(player.getCar());
	}

	public void render(Graphics g) {
		try {
			bs = this.getBufferStrategy();
			if (bs == null) {
				this.createBufferStrategy(3);
				return;
			}
			g = bs.getDrawGraphics();

			Graphics2D g2d = (Graphics2D) g;

			if (!finished)
				raceScene(g2d);
			else
				finishedScene(g2d);

		} finally {
			if (g != null) {
				g.dispose();
			}
		}
		bs.show();
		Toolkit.getDefaultToolkit().sync();
	}

	private void finishedScene(Graphics2D g2d) {

		g2d.drawImage(resShadow.getFrame(), 0, 0, Race.WIDTH, Race.HEIGHT, null);

	}

	private void raceScene(Graphics2D g2d) {
		g2d.drawImage(background.getFrame(), 0, 0, Race.WIDTH, Race.HEIGHT, null);

		shakeImage(g2d, carImage, x, y, width, height, (float) player.getCar().getSpeedActual(), blurSpeed / 2,
				blurSpeed * 1.5f, blurShake * 2);

		if (player.getCar().isNOSON()) {
			blur(g2d, nitros.getFrame(), 0, 0, Race.WIDTH, Race.HEIGHT,
					(float) player.getCar().getNosStrengthStandard(), 0f, 2.5f, blurShake);
		}

		if (player.getCar().getSpeedActual() > blurSpeed)
			blur(g2d, fastness, 0, 0, Race.WIDTH, Race.HEIGHT, (float) player.getCar().getSpeedActual(), blurSpeed,
					100f, blurShake);

		g2d.setFont(font);

		// DEBUG
//		drawDebug(g, 300);

		// Prerace stuff
		drawRaceHUD(g2d);

		drawTachometer(g2d);

		drawInfoHUD(g2d);
	}

	private void blur(Graphics2D g2d, BufferedImage img, int x, int y, int width, int height, float comparedValue,
			float fromValue, float tillAdditionalValue, int shake) {
		float alpha = alpha(comparedValue, fromValue, tillAdditionalValue, shake);

		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(ac);

		float xShake = shake(shake, alpha);
		float yShake = shake(shake, alpha);

		g2d.drawImage(img, -shake + x, -shake + y, (width + 2 * shake) + (int) xShake,
				(height + 2 * shake) + (int) yShake, null);
		g2d.setComposite(ac.derive(1f));
	}

	private void shakeImage(Graphics2D g2d, BufferedImage img, int x, int y, int width, int height, float comparedValue,
			float fromValue, float tillAdditionalValue, int shake) {
		float alpha = alpha(comparedValue, fromValue, tillAdditionalValue, shake);
		float xShake = shake(shake, alpha);
		float yShake = shake(shake, alpha);

		g2d.drawImage(img, -shake + x, -shake + y, (width + 2 * shake) + (int) xShake,
				(height + 2 * shake) + (int) yShake, null);
	}

	private float shake(int amount) {
		return r.nextInt(amount * 2) - amount;
	}

	private float shake(int amount, float alpha) {
		return shake(amount) * alpha;
	}

	private float alpha(float comparedValue, float fromValue, float tillAdditionalValue, int shake) {
		float alpha = (comparedValue - fromValue) / tillAdditionalValue;

		if (alpha > 1f)
			alpha = 1f;

		alpha += (shake(shake) / tillAdditionalValue) * alpha;

		if (alpha > 1f)
			alpha = 1f;
		else if (alpha < 0f) {
			alpha = 0f;
		}
		return alpha;
	}

	private void drawDebug(Graphics2D g, int h) {
		g.setColor(Color.green);
		g.drawString("SpeedActual: " + String.valueOf(player.getCar().getSpeedActual()), 100, 100 + h);
		g.drawString("Tachometer rotation: " + String.valueOf(player.getCar().getTachometer()), 100, 175 + h);
		g.drawString("SpeedLinear: " + String.valueOf(player.getCar().getSpeedLinear()), 100, 125 + h);
		g.drawString("Gear: " + String.valueOf(player.getCar().getGear()), 100, 150 + h);
		g.drawString("Clutch: " + String.valueOf(player.getCar().isClutch()), 100, 200 + h);

		g.drawString("Place: " + String.valueOf(race.getCurrentPlace()), 100, 250 + h);
		g.drawString("Distance: " + String.valueOf(race.getCurrentLength()), 100, 300 + h);
		g.drawString("Distance covered: " + String.valueOf(player.getCar().getDistance()), 100, 350 + h);
		g.drawString(String.valueOf(System.currentTimeMillis() - startTime), 100, 375 + h);

	}

	private void drawRaceHUD(Graphics2D g) {
		if (!running) {
			g.drawString("Wait until everyone is ready", Race.WIDTH / 2 - 100, Race.HEIGHT / 6);
		}

		g.setColor(ballColor);

		if (player.getCar().isGearTooHigh())
			g.drawString("YOU'VE SHIFTED TOO EARLY!!!", Race.WIDTH / 2 - 100, Race.HEIGHT / 7);

		for (int i = 0; i < baller; i++) {
			g.fillOval(Race.WIDTH / 2 + (-100 + (75 * i)), Race.HEIGHT / 3, 50, 50);
		}

		if (race.isCheating()) {
			g.drawString("You went too early!!!", Race.WIDTH / 2 - 50, Race.HEIGHT / 8);
		}
	}

	/**
	 * Angles, nitros, gear
	 */
	private void drawTachometer(Graphics2D g) {

		g.drawImage(tachometer, xTachometer, yTachometer, widthTachometer, heightTachometer, null);

		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		trans.translate(xTachopointer,
				yTachopointer - (tachopointer.getHeight() * (0.005 * player.getCar().getTachometer() + 0.85)));
		trans.scale(scaleXTachopointer, scaleYTachopointer);
		trans.rotate(Math.toRadians(player.getCar().getTachometer()));
		g.drawImage(tachopointer, trans, this);

		g.setColor(Color.white);
		g.drawString(String.format("%.0f", player.getCar().getSpeedActual()), xSpeed, ySpeed);
		if (player.getCar().getGear() > 0)
			g.drawString(String.valueOf(player.getCar().getGear()), xGear, yGear);
		else
			g.drawString("N", xGear, yGear);
	}

	private void drawInfoHUD(Graphics2D g) {
		g.drawString("Place: " + String.valueOf(race.getCurrentPlace()), xDistance, yDistance);
		g.drawString("Distance: " + String.valueOf(race.getCurrentLength()), xDistance, yDistance + 50);
		g.drawString("Distance covered: " + String.format("%.0f", player.getCar().getDistance()), xDistance,
				yDistance + 100);
		if (System.currentTimeMillis() - startTime - 3000 < 1000000
				&& System.currentTimeMillis() - startTime - 3000 >= 0)
			g.drawString(
					"Time in sec: " + String.format("%.2f",
							Float.valueOf(System.currentTimeMillis() - startTime - 3000) / 1000),
					xDistance, yDistance + 150);

		g.drawString("NOS bottles left: " + String.valueOf(player.getCar().getNosAmountLeft()), xDistance,
				yDistance + 200);

	}

	public boolean isStartCountDown() {
		return startCountDown;
	}

	public void setStartCountDown(boolean startCountDown) {
		this.startCountDown = startCountDown;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Color getBallColor() {
		return ballColor;
	}

	public void setBallColor(Color ballColor) {
		this.ballColor = ballColor;
	}

	public void setBallCount(int i) {
		baller = i;
	}

}
