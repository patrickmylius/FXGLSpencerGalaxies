package main.Control.Handlers;


import main.EntityType;
import main.Control.BasicGameApp;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;

public class PowerUpHandler extends CollisionHandler {

    public PowerUpHandler(){
        super(EntityType.POWERUP, EntityType.PLAYER);
    }
    @Override
    protected void onCollisionBegin(Entity powerUp, Entity player){
        powerUp.removeFromWorld();
        FXGL.<BasicGameApp>getAppCast().playerPowerUp();
        Sound powerUpCollide = getAssetLoader().loadSound("PowerUpCollide.wav");
        getAudioPlayer().playSound(powerUpCollide);
        FXGL.runOnce(() ->{
            FXGL.<BasicGameApp>getAppCast().playerPowerOff();
            /** Sets duration of the player PoweredUp buff */
            }, Duration.seconds(9));//FOR LAPTOP
    }
}
