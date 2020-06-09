package main.Control.Handlers;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.util.Duration;
import main.Control.BasicGameApp;
import main.EntityType;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;

public class ExtraLifeHandler extends CollisionHandler {

    public ExtraLifeHandler(){
        super(EntityType.EXTRALIFE, EntityType.PLAYER);
    }
    @Override
    protected void onCollisionBegin(Entity ExtraLife, Entity player){
        getGameState().increment("lives", +1);
        Sound extraLifePickUp = getAssetLoader().loadSound("extraLifePickUp.wav");
        getAudioPlayer().playSound(extraLifePickUp);
        ExtraLife.removeFromWorld();
    }
}