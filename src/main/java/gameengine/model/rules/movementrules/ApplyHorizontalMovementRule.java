package gameengine.model.rules.movementrules;

import gameengine.controller.GameEngineController;
<<<<<<< HEAD
=======
import gameengine.controller.interfaces.ControlInterface;
>>>>>>> 375b21238c5648174731f58d886ac3721116ad8b
import gameengine.model.boundary.ScreenBoundary;
import objects.GameObject;

/**
 * Created by Soravit on 11/22/2016. Modified by Chalena Scholl
 */
public class ApplyHorizontalMovementRule implements MovementRule{
	@Override
	public void applyRule(GameObject obj, ControlInterface gameMovement, ScreenBoundary gameBoundaries) {
        double moveSpeed = Double.parseDouble(obj.getProperty("horizontalmovement"));
        double newXPos = obj.getXPosition() + moveSpeed;
        
		if(newXPos > obj.getXPosition()){
			gameMovement.moveRight(obj, moveSpeed);
		}
		else if (newXPos < obj.getXPosition()){
			gameMovement.moveLeft(obj, moveSpeed);
		}
	}
}
