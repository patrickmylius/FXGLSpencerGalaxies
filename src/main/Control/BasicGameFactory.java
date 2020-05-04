package main.Control;

import main.EntityType;
import main.Control.Components.BossComponent;
import main.Control.Components.EnemyComponent;
import main.Control.Components.OwnerComponent;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BasicGameFactory implements EntityFactory{

    @Spawns("Player")
    public Entity Player(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(8);
        emitter.setSize(2, 8);
        emitter.setEmissionRate(0.075);
        emitter.setStartColor(Color.WHITESMOKE);
        emitter.setEndColor(Color.WHITESMOKE);
        emitter.setBlendMode(BlendMode.ADD);
        return entityBuilder()
                .type(EntityType.PLAYER)
                .from(data)
                .viewWithBBox("Ship.gif")
                .with(new CollidableComponent(true), new ParticleComponent(emitter))
                .with()
                .build();
    }
    /**
     * Spawns and create, evilPuff
     */
    @Spawns("Enemy")
    public Entity EvilPuff(SpawnData data){
        if (FXGL.getb("freezePoweredUp")){
            return entityBuilder()
                    .type(EntityType.ENEMY)
                    .from(data)
                    .viewWithBBox("enemy.gif")
                    .with(new CollidableComponent(true))
                    /**Sets speed on ENEMY For Laptop 1920x1080*/
                    .with("velocity", new Point2D((Math.random() * 1.25) + 1.5, (Math.random() * 1) + 1.5))
                    /**Sets speed on ENEMY For Station 5760x1080*/
                    //.with("velocity", new Point2D((Math.random() * 0.25) + 0.50, (Math.random() * 0.25) + 0.50))
                    .with(new EnemyComponent(), new OffscreenCleanComponent())
                    .build();
        }
        else
            return entityBuilder()
                    .type(EntityType.ENEMY)
                    .from(data)
                    .viewWithBBox("enemy.gif")
                    .with(new CollidableComponent(true))
                    /**Sets speed on ENEMY For Laptop 1920x1080*/
                    .with("velocity", new Point2D((Math.random() * 1.5) + 3, (Math.random() * 1) + 3))
                    /**Sets speed on ENEMY For Station 5760x1080*/
                    //.with("velocity", new Point2D((Math.random() * 1.25) + 1.50, (Math.random() * 1.25) + 1.50))
                    .with(new EnemyComponent())
                    .build();
    }
    /**
     * Building boss, spawns
     */
    @Spawns("Boss1")
    public Entity Boss1(SpawnData data){
        var hp = new HealthIntComponent(750);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.LIGHTGREEN);
        hpView.setMaxValue(750);
        hpView.setTranslateY(100);
        hpView.setWidth(100);
        hpView.currentValueProperty().bind(hp.valueProperty());
        return entityBuilder()
                .type(EntityType.BOSS1)
                .from(data)
                .viewWithBBox("boss1.gif")
                .view(hpView)
                .with(hp)
                .with(new CollidableComponent(true))
                .with("velocityBoss", new Point2D((Math.random() * 1.5) + 3.00, (Math.random() * 1.5) + 3.00))
                .with(new BossComponent())
                .build();
    }
    @Spawns("Boss2")
    public Entity Boss2(SpawnData data){
        var hp = new HealthIntComponent(3000);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.LIGHTGREEN);
        hpView.setMaxValue(3000);
        hpView.setTranslateY(130);
        hpView.setWidth(120);
        hpView.currentValueProperty().bind(hp.valueProperty());
        return entityBuilder()
                .type(EntityType.BOSS2)
                .from(data)
                .view("newboss2.gif")
                .bbox(new HitBox(BoundingShape.box(100,100)))
                .view(hpView)
                .with(hp)
                .with(new CollidableComponent(true))
                .with(new BossComponent())
                .with("velocityBoss", new Point2D((Math.random() * 1.5) + 3.00, (Math.random() * 1.5) + 3.00))
                .build();
    }
    @Spawns("Boss3")
    public Entity Boss3(SpawnData data){
        var hp = new HealthIntComponent(10000);
        var hpView = new ProgressBar(false);
        hpView.setFill(Color.LIGHTGREEN);
        hpView.setMaxValue(10000);
        hpView.setTranslateY(180);
        hpView.setWidth(140);
        hpView.currentValueProperty().bind(hp.valueProperty());
        return entityBuilder()
                .type(EntityType.BOSS3)
                .from(data)
                .view("boss3.gif")
                .bbox(new HitBox(BoundingShape.box(100,100)))
                .view(hpView)
                .with(hp)
                .with(new CollidableComponent(true))
                .with(new BossComponent())
                .with("velocityBoss", new Point2D((Math.random() * 1.5) + 3.00, (Math.random() * 1.5) + 3.00))
                .build();
    }
    @Spawns("Coin")
    public Entity newCoin(SpawnData data){
        return entityBuilder()
                .from(data)
                .type(EntityType.COIN)
                .at((Math.random() * 600) + (1),
                        (Math.random() * 600) + (1))
                .viewWithBBox("coin.gif")
                .with(new CollidableComponent(true), new KeepOnScreenComponent().bothAxes())
                .build();
    }
    //Building power up
    @Spawns("PowerUp")
    public Entity newPowerUp(SpawnData data){
        return entityBuilder()
                .from(data)
                .type(EntityType.POWERUP)
                .at((Math.random() * 600) + (1),
                        (Math.random() * 600) + (1))
                .viewWithBBox("PowerUp.gif")
                .with(new CollidableComponent(true), new KeepOnScreenComponent().bothAxes())
                .build();
    }
    @Spawns("FreezePower")
    public Entity newFreezePower(SpawnData data){
        return entityBuilder()
                .from(data)
                .type(EntityType.FREEZEPOWER)
                .at((Math.random() * 600) + (1), (Math.random() * 600) + (1))
                .viewWithBBox("playerFreezeBuff.gif")
                .with(new CollidableComponent(true), new KeepOnScreenComponent().bothAxes())
                .build();
    }
    @Spawns("FrozenCircle")
    public Entity frozenCircle(SpawnData data){
        return entityBuilder()
                .type(EntityType.FROZENCIRCLE)
                .with(new FrozenCircleAnimation())
                .at(data.getX() - 450, data.getY() - 450)
                .with(new ExpireCleanComponent(Duration.seconds(6.0)))
                .build();
    }
    @Spawns("PlayerWeapon1")
    public Entity playerWeapon1(SpawnData data){
        return entityBuilder()
                .from(data)
                .type(EntityType.PLAYERWEAPON1)
                .at((Math.random() * 600) + (1), (Math.random() * 600) + (1))
                .viewWithBBox("playerWeapon1.gif")
                .with(new CollidableComponent(true), new KeepOnScreenComponent().bothAxes())
                .build();
    }
    @Spawns("PlayerWeapon2")
    public Entity playerWeapon2(SpawnData data){
        return entityBuilder()
                .from(data)
                .type(EntityType.PLAYERWEAPON2)
                .at((Math.random() * 600) + (1),
                        (Math.random() * 600) + (1))
                .viewWithBBox("playerWeapon2.gif")
                .with(new CollidableComponent(true), new KeepOnScreenComponent().bothAxes())
                .build();
    }
    @Spawns("PlayerWeapon3")
    public Entity playerWeapon3(SpawnData data){
        return entityBuilder()
                .from(data)
                .type(EntityType.PLAYERWEAPON3)
                .at((Math.random() * 600) + (1),
                        (Math.random() * 600) + (1))
                .viewWithBBox("playerWeapon3.gif")
                .with(new CollidableComponent(true), new KeepOnScreenComponent().bothAxes())
                .build();
    }
    @Spawns("Bullet1")
    /** make bullet only spawn for boss fights and their duration. */
    public Entity playerBullet1(SpawnData data){
        Entity owner = data.get("owner");
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.03f));
        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(8);
        emitter.setEmissionRate(0.5);
        emitter.setStartColor(Color.YELLOW);
        emitter.setEndColor(Color.RED);
        emitter.setBlendMode(BlendMode.ADD);
        return entityBuilder()
                .type(EntityType.BULLET1)
                .at(owner.getCenter().add(-3, 18))
                .viewWithBBox(new Rectangle(10, 10, javafx.scene.paint.Color.GOLD))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(data.get("direction"), 400), new ParticleComponent(emitter))
                .with(new OffscreenCleanComponent(), new OwnerComponent(owner.getType()))
                .build();
    }
    @Spawns("Bullet2")
    /** make bullet only spawn for boss fights and their duration. */
    public Entity playerBullet2(SpawnData data){
        Entity owner = data.get("owner");
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.03f));
        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(8);
        emitter.setEmissionRate(0.2);
        emitter.setStartColor(Color.YELLOW);
        emitter.setEndColor(Color.WHITE);
        emitter.setBlendMode(BlendMode.ADD);

        return entityBuilder()
                .type(EntityType.BULLET2)
                .at(owner.getCenter().add(-3, 18))
                .viewWithBBox(new Rectangle(30, 20, Color.GRAY))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(data.get("direction"), 350), new ParticleComponent(emitter))
                .with(new OffscreenCleanComponent(), new OwnerComponent(owner.getType()))
                .build();
    }
    @Spawns("Bullet3")
    /** make bullet only spawn for boss fights and their duration. */
    public Entity playerBullet3(SpawnData data){
        Entity owner = data.get("owner");
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.03f));
        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(5);
        emitter.setEmissionRate(0.5);
        emitter.setStartColor(Color.GREENYELLOW);
        emitter.setEndColor(Color.YELLOW);
        emitter.setBlendMode(BlendMode.ADD);

        return entityBuilder()
                .type(EntityType.BULLET3)
                .at(owner.getCenter().add(-3, 18))
                .viewWithBBox(new Rectangle(20, 15, Color.YELLOW))
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(data.get("direction"), 500), new ParticleComponent(emitter))
                .with(new OffscreenCleanComponent(), new OwnerComponent(owner.getType()))
                .build();
    }
    @Spawns("boss1Bullet")
    public Entity boss1Bullet(SpawnData data){
        Entity owner = data.get("owner");
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.003f));
        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(4);
        emitter.setSize(12, 25);
        emitter.setEmissionRate(0.2);
        emitter.setStartColor(Color.BLUE);
        emitter.setEndColor(Color.YELLOW);
        emitter.setBlendMode(BlendMode.ADD);

        return entityBuilder()
                .type(EntityType.BOSS1BULLET)
                .at(owner.getCenter().add(-3, 18))
                .viewWithBBox("boss1Bullet.gif")
                .with(new ProjectileComponent(data.get("direction"), 350), new CollidableComponent(true), new ParticleComponent(emitter))
                .with(new OffscreenCleanComponent(), new OwnerComponent(owner.getType()))
                .build();
    }
    @Spawns("boss2Bullet")
    public Entity boss2Bullet(SpawnData data){
        Entity owner = data.get("owner");
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.003f));
        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(4);
        emitter.setSize(2, 40);
        emitter.setEmissionRate(0.2);
        emitter.setStartColor(Color.YELLOW);
        emitter.setEndColor(Color.RED);
        emitter.setBlendMode(BlendMode.ADD);

        return entityBuilder()
                .type(EntityType.BOSS2BULLET)
                .at(owner.getCenter().add(-3, 18))
                .viewWithBBox("boss2Bullet.gif")
                .with(new ProjectileComponent(data.get("direction"), 250), new CollidableComponent(true), new ParticleComponent(emitter))
                .with(new OffscreenCleanComponent(), new OwnerComponent(owner.getType()))
                .build();
    }
    @Spawns("boss3Bullet")
    public Entity boss3Bullet(SpawnData data){
        Entity owner = data.get("owner");
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().restitution(1f).density(0.003f));
        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setNumParticles(4);
        emitter.setSize(5, 30);
        emitter.setEmissionRate(0.3);
        emitter.setStartColor(Color.DARKGREEN);
        emitter.setEndColor(Color.DARKGREEN);
        emitter.setBlendMode(BlendMode.ADD);
        return entityBuilder()
                .type(EntityType.BOSS3BULLET)
                .at(owner.getCenter().add(-3, 18))
                .viewWithBBox("boss3bullet.gif")
                .with(new ProjectileComponent(data.get("direction"), 300), new CollidableComponent(true), new ParticleComponent(emitter))
                .with(new OffscreenCleanComponent(), new OwnerComponent(owner.getType()))
                .build();
    }
    @Spawns("ParticleExplosionPowerUp")
    public Entity newParticleExplosionEnemyEaten(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter((200));
        emitter.setStartColor(Color.RED);
        emitter.setEndColor(Color.YELLOW);
        emitter.setNumParticles(4);
        emitter.setSize(10,20);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());

        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }

    @Spawns("ParticleExplosionEnemyWeapon1")
    public Entity newParticleExplosionEnemyWeapon1(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter((200));
        emitter.setStartColor(Color.ORANGERED);
        emitter.setEndColor(Color.YELLOW);
        emitter.setNumParticles(10);
        emitter.setEmissionRate(0.5);
        emitter.setSize(4,20);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());

        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
    @Spawns("ParticleExplosionEnemyWeapon2")
    public Entity newParticleExplosionEnemyWeapon2(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter((300));
        emitter.setStartColor(Color.YELLOW);
        emitter.setEndColor(Color.RED);
        emitter.setNumParticles(8);
        emitter.setEmissionRate(0.7);
        emitter.setSize(5,35);
        emitter.setEmissionRate(0.4);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());

        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
    @Spawns("ParticleExplosionEnemyWeapon3")
    public Entity newParticleExplosionEnemyWeapon3(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter((200));
        emitter.setStartColor(Color.YELLOW);
        emitter.setEndColor(Color.YELLOWGREEN);
        emitter.setNumParticles(8);
        emitter.setSize(3,20);
        emitter.setEmissionRate(0.6);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());

        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
    @Spawns("ParticleExplosionBoss1Dead")
    public Entity newParticleExplosionBoss1(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter((getAppWidth()));
        emitter.setStartColor(Color.YELLOW);
        emitter.setEndColor(Color.BLUE);
        emitter.setNumParticles(20);
        emitter.setSize(4, 50);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());

        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
    @Spawns("ParticleExplosionBoss2Dead")
    public Entity newParticleExplosionBoss2(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter((getAppWidth()));
        emitter.setStartColor(Color.RED);
        emitter.setEndColor(Color.YELLOW);
        emitter.setNumParticles(20);
        emitter.setSize(4, 50);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());

        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
    @Spawns("ParticleExplosionBoss3Dead")
    public Entity newParticleExplosionBoss3(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter((getAppWidth()));
        emitter.setStartColor(Color.GREEN);
        emitter.setEndColor(Color.GREENYELLOW);
        emitter.setNumParticles(20);
        emitter.setSize(4, 50);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());

        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
    @Spawns("ParticleExplosionPlayerShotDead")
    public Entity newParticleExplosionPlayerShotDead(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(((250)));
        emitter.setStartColor(Color.YELLOW);
        emitter.setEndColor(Color.RED);
        emitter.setNumParticles(15);
        emitter.setSize(3, 25);
        emitter.setEmissionRate(10.0);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());
        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
    @Spawns("ParticleExplosionPlayerEaten")
    public Entity newParticleExplosionPlayerEaten(SpawnData data){
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter((150));
        emitter.setStartColor(Color.RED);
        emitter.setEndColor(Color.RED);
        emitter.setNumParticles(10);
        emitter.setSize(3,10);
        emitter.setBlendMode(BlendMode.ADD);
        ParticleComponent particles = new ParticleComponent(emitter);
        particles.setOnFinished(() -> particles.getEntity().removeFromWorld());

        return entityBuilder()
                .from(data)
                .with(particles)
                .build();
    }
    @Spawns("scoreText")
    public Entity newScoreText(SpawnData data){
        String text = data.get("text");
        var e = entityBuilder()
                .from(data)
                .view(getUIFactory().newText(text, Color.GOLD, 24))
                .with(new ExpireCleanComponent(Duration.seconds(4)).animateOpacity())
                .build();
        animationBuilder()
                .duration(Duration.seconds(1))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .translate(e)
                .from(new Point2D(data.getX(), data.getY()))
                .to(new Point2D(data.getX(), data.getY() - 30))
                .buildAndPlay();
        return e;
    }
    @Spawns("entityText")
    public Entity newEntityText(SpawnData data) {
        String text = data.get("text");
        var et = entityBuilder()
                .from(data)
                .view(getUIFactory().newText(text, Color.WHITESMOKE, 18))
                .with(new ExpireCleanComponent(Duration.seconds(5)).animateOpacity())
                .build();
        animationBuilder()
                .duration(Duration.seconds(1)).repeatInfinitely()
                .interpolator(Interpolators.EXPONENTIAL.EASE_IN_OUT())
                .translate(et)
                .from(new Point2D(data.getX(), data.getY()))
                .to(new Point2D(data.getX(), data.getY() + 5))
                .buildAndPlay();
        return et;
    }
    @Spawns("playerBuffText")
    public Entity newPlayerBuffText(SpawnData data){
        String text = data.get("text");
        var pb = entityBuilder()
                .from(data)
                .view(getUIFactory().newText(text, Color.GOLD, 24))
                .with(new ExpireCleanComponent(Duration.seconds(50)).animateOpacity())
                .build();
        animationBuilder()
                .duration(Duration.seconds(0.1)).repeatInfinitely()
                .interpolator(Interpolators.CIRCULAR.EASE_IN_OUT())
                .translate(pb)
                .from(new Point2D(data.getX(), data.getY()))
                .to(new Point2D(data.getX(), data.getY() + 4))
                .buildAndPlay();
        return pb;
    }
    @Spawns("stampedeText")
    public Entity newStampedeText(SpawnData data){
        String text = data.get("text");
        var st = entityBuilder()
                .from(data)
                .view(getUIFactory().newText(text, Color.ANTIQUEWHITE, 24))
                .with(new ExpireCleanComponent(Duration.seconds(50)).animateOpacity())
                .build();
        animationBuilder()
                .duration(Duration.seconds(0.5)).repeatInfinitely()
                .interpolator(Interpolator.DISCRETE)
                .interpolator(Interpolators.CIRCULAR.EASE_IN_OUT())
                .translate(st)
                .from(new Point2D(data.getX(), data.getY()))
                .to(new Point2D(data.getX(), data.getY() + 5))
                .buildAndPlay();
        return st;
    }
    @Spawns("bossText")
    public Entity newBossText(SpawnData data){
        String text = data.get("text");
        var bt = entityBuilder()
                .from(data)
                .view(getUIFactory().newText(text, Color.RED, 26))
                .with(new ExpireCleanComponent(Duration.seconds(500)).animateOpacity())
                .build();
        animationBuilder()
                .duration(Duration.seconds(0.5)).repeatInfinitely()
                .interpolator(Interpolators.CIRCULAR.EASE_IN_OUT())
                .translate(bt)
                .from(new Point2D(data.getX(), data.getY()))
                .to(new Point2D(data.getX(), data.getY() + 5))
                .buildAndPlay();
        return bt;
    }
}
class FrozenCircleAnimation extends Component{
    private AnimatedTexture texture;
    private AnimationChannel frozenCircle;

    public FrozenCircleAnimation(){
        frozenCircle = new AnimationChannel(FXGL.image("frozenCircleStream.png"), 6, 800, 800, Duration.seconds(1), 0, 6);
        texture = new AnimatedTexture(frozenCircle);
    }
    @Override
    public void onAdded(){
        entity.getViewComponent().addChild(texture);
        texture.play();
    }
}

