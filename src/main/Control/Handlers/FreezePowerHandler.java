package main.Control.Handlers;

import main.EntityType;
import main.Control.BasicGameApp;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;

public class FreezePowerHandler extends CollisionHandler {

    private Entity frozenCircle;

    public FreezePowerHandler(){
        super(EntityType.FREEZEPOWER, EntityType.PLAYER);
    }
    @Override
    protected void onCollisionBegin(Entity freezePower, Entity player){
        FXGL.<BasicGameApp>getAppCast().playerFreezePowerUp();
        //frozenCircle = getGameWorld().spawn("FrozenCircle", player.getPosition());
        freezePower.removeFromWorld();
        Sound freezePowerCollide = getAssetLoader().loadSound("FreezePowerCollide.wav");
        getAudioPlayer().playSound(freezePowerCollide);
        FXGL.runOnce(() ->{
            FXGL.<BasicGameApp>getAppCast().playerFreezePowerOff();
            /** Sets duration of the player FreezePower buff*/
            // }, Duration.seconds(14)); //FOR STATION
            }, Duration.seconds(6));
    }
}
