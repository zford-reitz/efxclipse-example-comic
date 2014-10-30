package ece2014.app.transitions;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Transitions {

	public static Transition createBatmanTransition(Node node) {
		
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), node);
		scaleTransition.setFromX(1);
		scaleTransition.setFromY(1);
		scaleTransition.setToX(0);
		scaleTransition.setToY(0);
		
		RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.2), node);
		rotateTransition.setByAngle(360);
		rotateTransition.setCycleCount(5);
		rotateTransition.setInterpolator(Interpolator.LINEAR);
		
		return new ParallelTransition(scaleTransition, rotateTransition);
	}
	
}
