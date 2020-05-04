package main.Control.Handlers;

import main.EntityType;
import main.Control.BasicGameApp;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;

public class PlayerWeapon2Handler extends CollisionHandler {

    public PlayerWeapon2Handler(){
        super(EntityType.PLAYERWEAPON2, EntityType.PLAYER);
    }
    @Override
    protected void onCollisionBegin(Entity playerWeapon2, Entity player){
        FXGL.<BasicGameApp>getAppCast().playerWeapon2On();
        //PlayerWeapon2 = getGameWorld().spawn("FrozenCircle", player.getPosition());
        playerWeapon2.removeFromWorld();
        //Sound freezePowerCollide = getAssetLoader().loadSound("FreezePowerCollide.wav");
        //getAudioPlayer().playSound(freezePowerCollide);
        FXGL.runOnce(() ->{
            FXGL.<BasicGameApp>getAppCast().playerWeapon2Off();
            /** Sets duration of the playerWeapon2 buff*/
            // }, Duration.seconds(14)); //FOR STATION
            }, Duration.seconds(25));
    }
}