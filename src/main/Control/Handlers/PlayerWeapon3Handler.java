package main.Control.Handlers;

import main.EntityType;
import main.Control.BasicGameApp;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;

public class PlayerWeapon3Handler extends CollisionHandler {

    public PlayerWeapon3Handler(){
        super(EntityType.PLAYERWEAPON3, EntityType.PLAYER);
    }
    @Override
    protected void onCollisionBegin(Entity playerWeapon3, Entity player){
        FXGL.<BasicGameApp>getAppCast().playerWeapon3On();
        //PlayerWeapon3 = getGameWorld().spawn("FrozenCircle", player.getPosition());
        playerWeapon3.removeFromWorld();
        //Sound freezePowerCollide = getAssetLoader().loadSound("FreezePowerCollide.wav");
        //getAudioPlayer().playSound(freezePowerCollide);
        FXGL.runOnce(() ->{
            FXGL.<BasicGameApp>getAppCast().playerWeapon3Off();
            /** Sets duration of the player Weapon3 buff*/
            // }, Duration.seconds(14)); //FOR STATION
            }, Duration.seconds(45));
    }
}
