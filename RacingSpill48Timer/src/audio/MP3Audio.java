package audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import adt.Audio;
import jaco.mp3.player.MP3Player;

public class MP3Audio implements Audio {
//	private Media hit;
	private MP3Player mediaPlayer;
//	private AudioClip loopPlayer;

	public MP3Audio(String file) {
		System.out.println("Finding file:");
		System.out.println("\"" + file + "\"");

		InputStream input = getClass().getResourceAsStream(file + ".mp3");
		File tempFile = null;
		try {
			tempFile = File.createTempFile("abc", null);
			FileOutputStream out = new FileOutputStream(tempFile);
			FileUtils.copyInputStreamToFile(input, tempFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		mediaPlayer = new MP3Player();
		mediaPlayer.addToPlayList(tempFile);

		setVolume(1);
	}

	@Override
	public void setVolume(double factor) {
//		mediaPlayer.(GameHandler.volume * factor);		
	}

	@Override
	public void setRate(double rate) {
		// mediaPlayer.setRate(rate);
	}

	public void play() {
		mediaPlayer.play();
	}

	public void stop() {
		if (isPlaying())
			mediaPlayer.stop();
	}

	public void loop() {
		mediaPlayer.play();
//		mediaPlayer.setOnEndOfMedia(new Runnable() {
//			public void run() {
//				mediaPlayer.seek(Duration.ZERO);
//			}
//		});
	}

	public boolean isPlaying() {
		return !mediaPlayer.isPaused();
	}

//	public Media getHit() {
//		return hit;
//	}
//
//	public void setHit(Media hit) {
//		this.hit = hit;
//	}
//
//	public MediaPlayer getMediaPlayer() {
//		return mediaPlayer;
//	}
//
//	public void setMediaPlayer(MediaPlayer mediaPlayer) {
//		this.mediaPlayer = mediaPlayer;
//	}

}