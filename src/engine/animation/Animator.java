package engine.animation;

import java.util.ArrayList;

import engine.interfaces.AnimationInterface;
import engine.objects2D.GameObject;

public class Animator implements AnimationInterface {

	ArrayList<Animation> animation;

	public Animator() {
		this.animation = new ArrayList<Animation>();
	}

	public void animate(GameObject gameObject, Animation animation, AnimationInterface animationInterface) {
		animation.setTargetInterface(animationInterface);
		this.animate(gameObject, animation);
	}

	public void applyInterpolation(float interpolationFactor) {
		if (animation != null) {
			for (int i = 0; i < animation.size(); i++) {
				animation.get(i).applyInterpolation(interpolationFactor);
			}
		}
	}

	public void stopAll() {
		animation.clear();
	}

	public void animate(GameObject gameObject, Animation animation) {
		animation.setTargetObject(gameObject);
		animation.setAnimationInterface(this);
		this.animation.add(animation);
	}

	public void update() {
		if (animation != null) {
			for (int i = 0; i < animation.size(); i++) {
				animation.get(i).update();
			}
		}
	}

	@Override
	public void finished(Animation animation) {
		this.animation.remove(animation);
		if (animation.getTargetInterface() != null) {
			animation.getTargetInterface().finished(animation);
		}

	}

}
