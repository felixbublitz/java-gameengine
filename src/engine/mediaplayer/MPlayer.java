package engine.mediaplayer;

import java.util.ArrayList;

import engine.datatypes.Ressource;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MPlayer implements Runnable {

	ArrayList<MediaPlayer> mediaPlayer;

	public MPlayer() {
		mediaPlayer = new ArrayList<MediaPlayer>();
		new JFXPanel();
	}

	public void stop(MediaPlayer player) {
		int id = mediaPlayer.indexOf(player);
		if (id != -1) {
			mediaPlayer.get(id).stop();
			mediaPlayer.remove(id);
		}
	}

	public void stopAll() {
		for (MediaPlayer mediaPlayer : this.mediaPlayer) {
			mediaPlayer.stop();
		}
		this.mediaPlayer.clear();
	}

	public void pauseAll() {
		for (MediaPlayer mediaPlayer : this.mediaPlayer) {
			mediaPlayer.pause();
		}
	}

	public void resumeAll() {
		for (MediaPlayer mediaPlayer : this.mediaPlayer) {
			mediaPlayer.play();
		}
	}

	public MediaPlayer play(Ressource ressource) {
		return this.play(ressource, null);
	}

	public MediaPlayer play(Ressource ressource, boolean loop) {
		if (loop) {
			return this.play(ressource, this);
		} else {
			return this.play(ressource);
		}
	}

	public MediaPlayer play(Ressource ressource, Runnable onComplete) {
		MediaPlayer mediaPlayer = new MediaPlayer(ressource.getMedia());

		if (onComplete != null) {
			mediaPlayer.setOnEndOfMedia(onComplete);
		}

		this.mediaPlayer.add(mediaPlayer);
		int id = this.mediaPlayer.indexOf(mediaPlayer);
		this.mediaPlayer.get(id).play();
		return this.mediaPlayer.get(id);
	}

	@Override
	public void run() {
		for (int i = 0; i < this.mediaPlayer.size(); i++) {
			if (this.mediaPlayer.get(i).getStopTime().compareTo(this.mediaPlayer.get(i).getCurrentTime()) == 0) {
				if (this.mediaPlayer.get(i).getOnEndOfMedia() == this) {
					this.mediaPlayer.get(i).seek(new Duration(0));
				}
			}
		}
	}

}
