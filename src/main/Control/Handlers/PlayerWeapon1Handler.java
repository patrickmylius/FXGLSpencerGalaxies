package main.Control.Handlers;

import main.EntityType;
import main.Control.BasicGameApp;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;

public class PlayerWeapon1Handler extends CollisionHandler {

    public PlayerWeapon1Handler(){
        super(EntityType.PLAYERWEAPON1, EntityType.PLAYER);
    }
    @Override
    protected void onCollisionBegin(Entity playerWeapon1, Entity player){
        FXGL.<BasicGameApp>getAppCast().playerWeapon1On();
        playerWeapon1.removeFromWorld();
        //Sound freezePowerCollide = getAssetLoader().loadSound("FreezePowerCollide.wav");
        //getAudioPlayer().playSound(freezePowerCollide);
        FXGL.runOnce(() ->{
            FXGL.<BasicGameApp>getAppCast().playerWeapon1Off();
            /** Sets duration of the player weapon1Buff*/
            }, Duration.seconds(18));
    }
}
