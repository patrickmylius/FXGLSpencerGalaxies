package main.Control;

import main.Menues.SpencerGameMenu;
import main.Menues.SpencerMainMenu;
import main.Control.Handlers.FreezePowerHandler;
import main.Control.Handlers.PowerUpHandler;
import main.Control.Handlers.PlayerWeapon1Handler;
import main.Control.Handlers.PlayerWeapon2Handler;
import main.Control.Handlers.PlayerWeapon3Handler;
import main.EntityType;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.*;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.TimerAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
    /** FIXES */
    //TODO - Look into all timers, to better the game flow.
    //TODO - Fix frozen Enemies move pattern.
    //TODO - Fix entity pixel move speed and velocity. Game play might be abit to slow.
    //TODO - Fix particelEmitters, makes game burn to much CPU.
    /** DESIGNS */
    //TODO - New HighScore Design
    //TODO - New Icon Design
    /** IMPLEMENTS */
    //TODO - IMPLEMENT CountnDown Buff bar for player.
    //TODO - BOSS1,2,3 Spawn Sound Effect + 3 Theme Songs.
    //TODO - BOSS1,2,3 BULLET SOUND EFFECT
    //TODO - (Explosions sound effects}
    //TODO - Implement Game End/Won at etc points.
public class BasicGameApp extends GameApplication{
    /**
     * Creates window 900x800, setTitle to Basic Game App, Version 0.1
     */
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(900);
        gameSettings.setHeight(800);
        gameSettings.setTitle("SPENCER - Zealand");
        gameSettings.setAppIcon("enemy.png");
        gameSettings.setVersion("1.0");
        gameSettings.setMenuEnabled(true);
        gameSettings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new SpencerMainMenu(MenuType.MAIN_MENU);
            }
            public FXGLMenu newGameMenu() {
                return new SpencerGameMenu(MenuType.GAME_MENU);
            }
        });
    }
    /**
     * Creates variables
     */
    private boolean leftWallTouched;
    private boolean rightWallTouched;
    private boolean topWallTouched;
    private boolean bottomWallTouched;
    private boolean hasPowerUp;
    private boolean safeRespawn;
    private boolean powerUpAlive;
    private boolean hasFreezePower;
    private boolean freezePowerAlive;
    private boolean hasWeapon1Power;
    private boolean Weapon1PowerAlive;
    private boolean hasWeapon2Power;
    private boolean weapon1PowerAlive;
    private boolean weapon1Spawned;
    private boolean weapon2Spawned;
    private boolean weapon3Spawned;
    private boolean weapon2PowerAlive;
    private boolean hasWeapon3Power;
    private boolean weapon3PowerAlive;
    private boolean boss1Spawned;
    private boolean boss1Alive;
    private boolean boss2Spawned;
    private boolean boss2Alive;
    private boolean boss3Spawned;
    private boolean boss3Alive;
    private boolean bossFightOn;
    private boolean canShoot;
    private boolean enemyStampede;

    private Entity enemy;
    private Entity boss1;
    private Entity boss2;
    private Entity boss3;
    private Entity leftWall;
    private Entity rightWall;
    private Entity coin;
    private Entity powerUp;
    private Entity freezePower;
    private Entity playerWeapon1;
    private Entity playerWeapon2;
    private Entity playerWeapon3;
    private Entity player;
    private Entity playerShoot;
    private Entity UIFreezeText;
    private Entity UIPowerText;
    private Entity UIStampedeText;
    private Entity UIBossFightText;
    private Entity UIEntityText;

    private int playerLives;
    public int totalScore;

    public static Music music;
    public static Music menuMusic;

    private TimerAction TimerEnemy;
    private TimerAction TimerCoin;
    private TimerAction TimerPowerUp;
    private TimerAction TimerFreezePowerUp;
    private TimerAction TimerStampede;
    private TimerAction TimerBoss1Bullet;
    private TimerAction TimerBoss2Bullet;
    private TimerAction TimerBoss3Bullet;

    private String playerName;
    private String log = "************************************\nPokeBounce High Scores \n************************************";
    /**
     * initialize game method, spawns evilPuffs, launch spawn sound.
     */
    @Override
    protected void initGame() {
        getAudioPlayer().stopMusic(menuMusic);
        music = getAssetLoader().loadMusic("InGameMusic.mp3");
        getAudioPlayer().playMusic(music);
        getGameWorld().addEntityFactory(new BasicGameFactory());
        Sound evilPuffEntrySound = getAssetLoader().loadSound("NewEvilPuffEntry.wav");
        Sound coinEntrySound = getAssetLoader().loadSound("NewCoinEntry.wav");
        Sound powerUpEntrySound = getAssetLoader().loadSound("PowerUpSpawn.wav");
        Sound freezePowerEntrySound = getAssetLoader().loadSound("FreezePowerSpawn.wav");

        playerLives = 3;
        safeRespawn = false;
        leftWallTouched = false;
        rightWallTouched = false;
        topWallTouched = false;
        bottomWallTouched = false;
        canShoot = true;
        boss1Spawned = false;
        boss1Alive = false;
        boss2Spawned = false;
        boss2Alive = false;
        bossFightOn = false;
        hasFreezePower = false;
        freezePowerAlive = false;
        hasPowerUp = false;
        powerUpAlive = false;
        hasWeapon1Power = false;
        weapon1PowerAlive = false;
        weapon1Spawned = false;
        weapon1PowerAlive = false;
        weapon2PowerAlive = false;
        weapon2Spawned = false;
        hasWeapon2Power = false;
        hasWeapon3Power = false;
        weapon3Spawned = false;
        weapon3PowerAlive = false;
        enemyStampede = false;

        /**FOR STATION 57600x1080 pixels*/
        /** Spawns new EvilPuff every 6 seconds */
        if (!bossFightOn) {
                enemy = getGameWorld().spawn("Enemy", getAppHeight() / (Math.random() * 10) + (1),
                        getAppWidth() / -(Math.random() * 200) + (1));
                TimerEnemy = getGameTimer().runAtInterval(() ->
                {
                    enemy = getGameWorld().spawn("Enemy", getAppHeight() / (Math.random() * 10) + (1),
                            getAppWidth() / (Math.random() * 200) + (1));
                    getAudioPlayer().playSound(evilPuffEntrySound);
                }, Duration.seconds(5));
                TimerEnemy.resume();
                /** Engage Timer for stampede mode, at game start, pause and only unpause, during Weapon Powerups*/
                TimerStampede = getGameTimer().runAtInterval(() ->
                {
                    enemy = getGameWorld().spawn("Enemy", getAppHeight() / (Math.random() * 10) + (1),
                            getAppWidth() / (Math.random() * 200) + (1));
                    getAudioPlayer().playSound(evilPuffEntrySound);
                }, Duration.seconds(2));
                TimerStampede.pause();
            /** Spawns new coin every 8 second*/
                TimerCoin = getGameTimer().runAtInterval(() -> {
                    coin = getGameWorld().spawn("Coin");
                    getAudioPlayer().playSound(coinEntrySound);
                }, Duration.seconds(4));
                TimerCoin.resume();
            /** Spawns powerup every 35 second */
                TimerPowerUp = getGameTimer().runAtInterval(() -> {
                    powerUp = getGameWorld().spawn("PowerUp");
                    UIEntityText = getGameWorld().spawn("entityText", new SpawnData(powerUp.getX(), powerUp.getY() - 10).put("text", "Power"));
                    getAudioPlayer().playSound(powerUpEntrySound);
                    powerUpAlive = true;
                }, Duration.seconds(33));
                TimerPowerUp.resume();
            /** Spawns FreezeUp, every 60 second */
                TimerFreezePowerUp = getGameTimer().runAtInterval(() -> {
                    freezePower = getGameWorld().spawn("FreezePower");
                    UIEntityText = getGameWorld().spawn("entityText", new SpawnData(freezePower.getX() - 10, freezePower.getY() - 10).put("text", "Slow time"));
                    getAudioPlayer().playSound(freezePowerEntrySound);
                    freezePowerAlive = true;
                }, Duration.seconds(18));
                TimerFreezePowerUp.resume();
        }
        /** Create new Entity (Player) */
        player = spawn("Player", new Point2D(300, 300));

        /** Creates frame repeating 1-3 on background and creates view*/
        getGameScene().setBackgroundRepeat("spencerBackground.png");

        /** adds RIGHTWALL as entity*/
        leftWall = entityBuilder()
                .type(EntityType.LEFTWALL)
                .at(-10, 0)
                .bbox(new HitBox(BoundingShape.box(10, 800)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();
        /** adds RIGHTWALL as entity*/
        rightWall = entityBuilder()
                .type(EntityType.RIGHTWALL)
                .at(910, 0)
                .bbox(new HitBox(BoundingShape.box(-10, 800)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();
        /** adds TOPWALL as entity */
        entityBuilder()
                .type(EntityType.TOPWALL)
                .at(0, -10)
                .bbox(new HitBox(BoundingShape.box(900, 10)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();
        /** Implements BOTTOMWALL as entity */
        entityBuilder()
                .type(EntityType.BOTTOMWALL)
                .at(0, 810)
                .bbox(new HitBox(BoundingShape.box(900, -10)))
                .with(new CollidableComponent(true))
                .with(new PhysicsComponent())
                .buildAndAttach();
    }
    /**
     * initializing physics, adding collisionHandler, Player, Coin, Enemy, PowerUp
     */
    @Override
    protected void initPhysics(){
        getPhysicsWorld().addCollisionHandler(new PowerUpHandler());
        getPhysicsWorld().addCollisionHandler(new FreezePowerHandler());
        getPhysicsWorld().addCollisionHandler(new PlayerWeapon1Handler());
        getPhysicsWorld().addCollisionHandler(new PlayerWeapon2Handler());
        getPhysicsWorld().addCollisionHandler(new PlayerWeapon3Handler());
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN){
            @Override
            protected void onCollisionBegin(Entity player, Entity coin){
                onCoinPickup();
                coin.removeFromWorld();
                spawn("scoreText", new SpawnData(coin.getX(), coin.getY()).put("text", "250"));
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BOSS1){
            @Override
            protected void onCollisionBegin(Entity playerEaten, Entity boss1){
                if (!safeRespawn){
                    Sound playerSwallowed = getAssetLoader().loadSound("playerEaten.wav");
                    getAudioPlayer().playSound(playerSwallowed);
                    player.removeFromWorld();
                    spawn("ParticleExplosionPlayerEaten", player.getCenter());
                    onPlayerDeath();
                    if (playerLives > 0){
                        runOnce(() ->{
                            respawn();
                            /** Sets how long player is removed from map, before respawning timer starts*/
                            },Duration.seconds(1.5));
                    }
                }
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BOSS2){
            @Override
            protected void onCollisionBegin(Entity playerEaten, Entity boss2){
                if (!safeRespawn){
                    Sound playerSwallowed = getAssetLoader().loadSound("playerEaten.wav");
                    getAudioPlayer().playSound(playerSwallowed);
                    player.removeFromWorld();
                    spawn("ParticleExplosionPlayerEaten", player.getCenter());
                    onPlayerDeath();
                    if (playerLives > 0){
                        runOnce(() ->{
                            respawn();
                            /** Sets how long player is removed from map, before respawning timer starts*/
                        },Duration.seconds(1.5));
                    }
                }
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BOSS3){
            @Override
            protected void onCollisionBegin(Entity playerEaten, Entity boss3){
                if (!safeRespawn){
                    Sound playerSwallowed = getAssetLoader().loadSound("playerEaten.wav");
                    getAudioPlayer().playSound(playerSwallowed);
                    player.removeFromWorld();
                    spawn("ParticleExplosionPlayerEaten", player.getCenter());
                    onPlayerDeath();
                    if (playerLives > 0){
                        runOnce(() ->{
                            respawn();
                            /** Sets how long player is removed from map, before respawning timer starts*/
                        },Duration.seconds(1.5));
                    }
                }
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET1, EntityType.BOSS1){
            @Override
            protected void onCollisionBegin(Entity playerBullet1, Entity boss1){
                var hp = boss1.getComponent(HealthIntComponent.class);
                if (hp.getValue() > 1){
                    spawn("ParticleExplosionEnemyWeapon1", playerBullet1.getCenter());
                    playerBullet1.removeFromWorld();
                    hp.damage(10);
                    return;
                }
                killBoss1(boss1);
                spawn("scoreText", new SpawnData(boss1.getX(), boss1.getY()).put("text", "1000"));
                playerBullet1.removeFromWorld();
                inc("score", +1000);
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET2, EntityType.BOSS2){
            @Override
            protected void onCollisionBegin(Entity playerBullet2, Entity boss2){
                var hp = boss2.getComponent(HealthIntComponent.class);
                if (hp.getValue() > 1){
                    spawn("ParticleExplosionEnemyWeapon2", playerBullet2.getCenter());
                    playerBullet2.removeFromWorld();
                    hp.damage(50);
                    return;
                }
                killBoss2(boss2);
                spawn("scoreText", new SpawnData(boss2.getX(), boss2.getY()).put("text", "2500"));
                playerBullet2.removeFromWorld();
                inc("score", +2500);
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET3, EntityType.BOSS3){
            @Override
            protected void onCollisionBegin(Entity playerBullet3, Entity boss3){
                var hp = boss3.getComponent(HealthIntComponent.class);
                if (hp.getValue() > 1){
                    spawn("ParticleExplosionEnemyWeapon3", playerBullet3.getCenter());
                    playerBullet3.removeFromWorld();
                    hp.damage(10);
                    return;
                }
                killBoss3(boss3);
                spawn("scoreText", new SpawnData(boss3.getX(), boss3.getY()).put("text", "5000"));
                playerBullet3.removeFromWorld();
                inc("score", +5000);
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET1, EntityType.ENEMY){
            @Override
            protected void onCollisionBegin(Entity playerBullet1, Entity enemy) {
                spawn("ParticleExplosionEnemyWeapon1", enemy.getCenter());
                spawn("scoreText", new SpawnData(enemy.getX(), enemy.getY()).put("text", "250"));
                enemy.removeFromWorld();

                getGameState().increment("score", +250);
                /** adds 250 points to totalScore for the log, every time player eats EvilPuff */
                totalScore = totalScore + 250;
            }
        });

        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET2, EntityType.ENEMY){
            @Override
            protected void onCollisionBegin(Entity playerBullet2, Entity enemy) {
                spawn("ParticleExplosionEnemyWeapon2", enemy.getCenter());
                spawn("scoreText", new SpawnData(enemy.getX(), enemy.getY()).put("text", "250"));
                enemy.removeFromWorld();

                getGameState().increment("score", +250);
                /** adds 250 points to totalScore for the log, every time player eats EvilPuff */
                totalScore = totalScore + 250;
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET3, EntityType.ENEMY){
            @Override
            protected void onCollisionBegin(Entity playerBullet3, Entity enemy) {
                spawn("ParticleExplosionEnemyWeapon2", enemy.getCenter());
                spawn("scoreText", new SpawnData(enemy.getX(), enemy.getY()).put("text", "250"));
                enemy.removeFromWorld();

                getGameState().increment("score", +250);
                /** adds 250 points to totalScore for the log, every time player eats EvilPuff */
                totalScore = totalScore + 250;
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BOSS1BULLET, EntityType.PLAYER){
            @Override
            protected void onCollisionBegin(Entity boss1BulletHit, Entity player){
                if (!safeRespawn) {
                    canShoot = false;
                    spawn("ParticleExplosionPlayerShotDead", player.getCenter());
                    player.removeFromWorld();
                    onPlayerDeath();
                    //If playerLives are higher than 0, call respawn();
                    if (playerLives > 0) {
                        runOnce(() -> {
                            respawn();
                            /** Sets how long player is removed from map, before respawning timer starts*/
                        }, Duration.seconds(1.5));
                    } canShoot = true;
                }
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BOSS2BULLET, EntityType.PLAYER){
            @Override
            protected void onCollisionBegin(Entity boss2BulletHit, Entity player){
                if (!safeRespawn) {
                    canShoot = false;
                    spawn("ParticleExplosionPlayerShotDead", player.getCenter());
                    player.removeFromWorld();
                    onPlayerDeath();
                    //If playerLives are higher than 0, call respawn();
                    if (playerLives > 0) {
                        runOnce(() -> {
                            respawn();
                            /** Sets how long player is removed from map, before respawning timer starts*/
                        }, Duration.seconds(1.5));
                    }
                } canShoot = true;
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BOSS3BULLET, EntityType.PLAYER){
            @Override
            protected void onCollisionBegin(Entity boss3BulletHit, Entity player){
                if (!safeRespawn) {
                    canShoot = false;
                    spawn("ParticleExplosionPlayerShotDead", player.getCenter());
                    player.removeFromWorld();
                    onPlayerDeath();
                    //If playerLives are higher than 0, call respawn();
                    if (playerLives > 0) {
                        runOnce(() -> {
                            respawn();
                            /** Sets how long player is removed from map, before respawning timer starts*/
                        }, Duration.seconds(1.5));
                    }
                } canShoot = true;
            }
        });
/** Handles Collisions with Player and Evil puff and if player is powered up, including saveRespawn*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ENEMY) {
            @Override
            protected void onCollisionBegin(Entity playerEaten, Entity enemy){
                if (!hasPowerUp && !safeRespawn && !hasFreezePower){
                    Sound playerSwallowed = getAssetLoader().loadSound("playerEaten.wav");
                    getAudioPlayer().playSound(playerSwallowed);
                    player.removeFromWorld();
                    spawn("ParticleExplosionPlayerEaten", player.getCenter());
                    onPlayerDeath();
                    //if playerLives are higher than 0, call respawn();
                    if (playerLives > 0) {
                        runOnce(() -> {
                            respawn();
                            /** Sets how long player is removed from map, before respawning timer starts*/
                        }, Duration.seconds(1.5));
                    }
                }
                if (hasPowerUp){
                    Sound evilPuffEaten = getAssetLoader().loadSound("evilPuffEaten.wav");
                    getAudioPlayer().playSound(evilPuffEaten);
                    spawn("ParticleExplosionPowerUp", enemy.getCenter());
                    spawn("scoreText", new SpawnData(enemy.getX(), enemy.getY()).put("text", "500"));
                    enemy.removeFromWorld();
                    getGameState().increment("score", +500);
                    /** adds 500 points to totalScore for the log, every time player eats EvilPuff */
                    totalScore = totalScore + 500;
                }
                if (hasFreezePower){
                    Point2D velocity = enemy.getObject("velocity");
                    enemy.translate(velocity);
                    //If player and enemy collides during freeze powerup, enemy will change direction.
                    enemy.setProperty("velocity", new Point2D( (Math.random() * 1.5) + 7.0, (Math.random() * 1.5) + 7.0));
                    Sound EnemyFreezeKnock = getAssetLoader().loadSound("EnemyFreezeKnock.wav");
                    getAudioPlayer().playSound(EnemyFreezeKnock);
                    spawn("scoreText", new SpawnData(enemy.getX(), enemy.getY()).put("text", "100"));
                    getGameState().increment("score", +100);
                    /** adds 100 points to totalScore for the log, every time player knocks away enemy */
                    totalScore = totalScore + 100;
                }
            }
        });
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.ENEMY, EntityType.ENEMY){
            @Override protected void onCollisionBegin(Entity enemy1, Entity enemy2){
                if (hasFreezePower){
                    spawn("FrozenEnemyExplosion", enemy1.getPosition());
                    spawn("FrozenEnemyExplosion", enemy2.getPosition());
                    enemy1.removeFromWorld();
                    enemy2.removeFromWorld();
                }
            }

        });
        /** Adds unitCollision to left wall and player unit*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.LEFTWALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                leftWallTouched = true;
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                leftWallTouched = false;
            }
        });

        /** Adds unitCollision to right wall and player unit*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.RIGHTWALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                rightWallTouched = true;
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                rightWallTouched = false;
            }
        });

        /** Adds unitCollision to top wall and player unit*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.TOPWALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                topWallTouched = true;
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                topWallTouched = false;
            }
        });

        /** Adds unitCollision to bottom wall and player unit*/
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.BOTTOMWALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                bottomWallTouched = true;
            }
            @Override
            protected void onCollisionEnd(Entity player, Entity wall) {
                bottomWallTouched = false;
            }
        });
    }
    /**
     * Runs theme song, loops until game is closed
     */
    @Override
    protected void onPreInit(){
        BasicGameApp.menuMusic = getAssetLoader().loadMusic("MainMenuMusic.mp3");
        getAudioPlayer().playMusic(BasicGameApp.menuMusic);
    }

    @Override
    protected void initInput(){
        Input input = getInput();

        input.addAction(new UserAction("Move Right"){
            @Override
            protected void onAction(){
                if (rightWallTouched) //If player unit collides with right wall,"Move Right" function stops until false.
                    return;
                    //player.translateX(3); //Move right, 3 pixels, for 5760x1080
                    player.translateX(5); //Move right 5 pixels for 1920x1080
                    //getGameState().increment("pixelsMoved", +3); Tracks pixels moved right
            }
        }, KeyCode.D);

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                if (leftWallTouched) //If player unit collides with left wall,"Move Left" function stops until false.
                    return;
                //player.translateX(-3); //move left -3 pixels. for 5760x1080
                player.translateX(-5); //move left -5 pixels, for 1920x1080
                //getGameState().increment("pixelsMoved", +3); Tracks pixels moved left
            }
        }, KeyCode.A);

        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                if (topWallTouched) //If player unit collides with top wall,"Move Up" function stops until false.
                    return;
                //player.translateY(-3); //move -3 pixels up, for 5760x1080
                player.translateY(-5); //move -5 pixels up
            }
        }, KeyCode.W);

        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                if (bottomWallTouched) //If player unit collides with bottom wall,"Move Down" function stops until false.
                    return;
                //player.translateY(3); //move 3 pixels down, for 5760x1080
                player.translateY(5); //move down 5 pixels, for 1920x1080
                //getGameState().increment("pixelsMoved", +3); Tracks pixels moved down
            }
        }, KeyCode.S);

            input.addAction(new UserAction("Shoot") {
                @Override
                protected void onAction(){
                    if (hasWeapon1Power && !bossFightOn){
                        if (canShoot && !safeRespawn){
                            canShoot = false;
                            Point2D point2D = player.getPosition();
                            SpawnData spawnData = new SpawnData(point2D);
                            Point2D direction = getInput().getVectorToMouse(point2D).normalize();
                            spawnData.put("direction", direction);
                            spawnData.put("owner", player);
                            spawn("Bullet1", spawnData);
                            runOnce(() -> {
                                canShoot = true;
                                /** Sets timer for shooting */
                            }, Duration.seconds(0.5));
                        }
                    }
                    if (hasWeapon2Power && !bossFightOn){
                        if (canShoot && !safeRespawn){
                            canShoot = false;
                            Point2D point2D = player.getPosition();
                            SpawnData spawnData = new SpawnData(point2D);
                            Point2D direction = getInput().getVectorToMouse(point2D).normalize();
                            spawnData.put("direction", direction);
                            spawnData.put("owner", player);
                            spawn("Bullet2", spawnData);
                            runOnce(() -> {
                                canShoot = true;
                                /** Sets timer for shooting */
                            }, Duration.seconds(1));
                        }
                    }
                    if (hasWeapon3Power && !bossFightOn){
                        if (canShoot && !safeRespawn){
                            canShoot = false;
                            Point2D point2D = player.getPosition();
                            SpawnData spawnData = new SpawnData(point2D);
                            Point2D direction = getInput().getVectorToMouse(point2D).normalize();
                            spawnData.put("direction", direction);
                            spawnData.put("owner", player);
                            spawn("Bullet3", spawnData);
                            runOnce(() -> {
                                canShoot = true;
                                /** Sets timer for shooting */
                            }, Duration.seconds(0.1));
                        }
                    }
                    if (bossFightOn){
                        if (canShoot && boss1Alive && !safeRespawn){
                            canShoot = false;
                            Point2D point2D = player.getPosition();
                            SpawnData spawnData = new SpawnData(point2D);
                            Point2D direction = getInput().getVectorToMouse(point2D).normalize();
                            spawnData.put("direction", direction);
                            spawnData.put("owner", player);
                            spawn("Bullet1", spawnData);
                            runOnce(() -> {
                                 canShoot = true;
                                 /** Sets timer for shooting */
                                 }, Duration.seconds(0.4));
                        }
                        if (canShoot && boss2Alive && !safeRespawn){
                            canShoot = false;
                            Point2D point2D = player.getPosition();
                            SpawnData spawnData = new SpawnData(point2D);
                            Point2D direction = getInput().getVectorToMouse(point2D).normalize();
                            spawnData.put("direction", direction);
                            spawnData.put("owner", player);
                            spawn("Bullet2", spawnData);
                            runOnce(() -> {
                                canShoot = true;
                                /** Sets timer for shooting */
                            }, Duration.seconds(0.6));
                        }
                        if (canShoot && boss3Alive && !safeRespawn){
                            canShoot = false;
                            Point2D point2D = player.getPosition();
                            SpawnData spawnData = new SpawnData(point2D);
                            Point2D direction = getInput().getVectorToMouse(point2D).normalize();
                            spawnData.put("direction", direction);
                            spawnData.put("owner", player);
                            spawn("Bullet3", spawnData);
                            runOnce(() -> {
                                canShoot = true;
                                /** Sets timer for shooting */
                            }, Duration.seconds(0.1));
                        }

                    }
                }
            }, MouseButton.PRIMARY);
        }

    @Override
    protected void onUpdate(double trf){
        if (playerLives == 0){
            runOnce(() ->{
                Sound gameOver = getAssetLoader().loadSound("GameOver.wav");
                getAudioPlayer().playSound(gameOver);
                getAudioPlayer().stopMusic(music);
                getDisplay().showInputBoxWithCancel("Write the name to be added to the highscore.", p -> !p.isEmpty(), (String) -> {
                    setPlayerName(String);
                    if (String.isEmpty()) {
                        System.out.println("Score not saved.");
                        totalScore = 0;
                        }
                    else{
                        String playerLog = "\nPlayer " + playerName + ": your score ended as: ";
                        System.out.println(log + totalScore);
                        log = log + playerLog + totalScore + "\n************************************";
                        saveToFile(totalScore);
                        }
                        getGameController().gotoMainMenu();
                        getAudioPlayer().playMusic(menuMusic);
                    });
                }, Duration.seconds(2.0));
    }
        if (hasPowerUp){
        player.getViewComponent().clearChildren();
        player.getViewComponent().addChild(FXGL.texture("ShipPower.gif"));
    }
        if (hasFreezePower){
            player.getViewComponent().clearChildren();
            player.getViewComponent().addChild(FXGL.texture("ShipFreeze.gif"));
        }
        if (hasWeapon1Power || hasWeapon2Power || hasWeapon3Power){
            player.getViewComponent().clearChildren();
            player.getViewComponent().addChild(FXGL.texture("ShipWeapon.gif"));
        }
        if (safeRespawn){
        player.getViewComponent().clearChildren();
        player.getViewComponent().addChild(FXGL.texture("ShipRespawn.gif"));
    }
        if (!safeRespawn && !hasPowerUp && !hasFreezePower && !hasWeapon1Power && !hasWeapon2Power && !hasWeapon3Power){
        player.getViewComponent().clearChildren();
        player.getViewComponent().addChild(FXGL.texture("Ship.gif"));
    }
        if (powerUpAlive){
            runOnce(() -> {
                powerUp.removeFromWorld();
                powerUpAlive = false;
                /** PowerUp visible in UI, timer set to 8 sec*/
                }, Duration.seconds(6));
        }
        if (freezePowerAlive){
            runOnce(() -> {
                freezePower.removeFromWorld();
                freezePowerAlive = false;
                /** FreezePower visible in UI, timer set to 8 sec*/
                }, Duration.seconds(8));
        }
        if (weapon1PowerAlive){
            runOnce(() -> {
                playerWeapon1.removeFromWorld();
                weapon1PowerAlive = false;
                }, Duration.seconds(10));
        }
        if (weapon2PowerAlive){
            runOnce(() -> {
                playerWeapon2.removeFromWorld();
                weapon2PowerAlive = false;
            }, Duration.seconds(12));
        }
        if (weapon3PowerAlive){
            runOnce(() -> {
                playerWeapon3.removeFromWorld();
                weapon3PowerAlive = false;
            }, Duration.seconds(15));
        }
        if (totalScore > 7000 && totalScore < 8000 && !weapon1Spawned){
            playerWeapon1 = getGameWorld().spawn("PlayerWeapon1");
            UIEntityText = getGameWorld().spawn("entityText", new SpawnData(playerWeapon1.getX()-3, playerWeapon1.getY() - 10).put("text", "Fireball"));
            weapon1PowerAlive = true;
            weapon1Spawned = true;

        }
        if (totalScore > 29000 && totalScore < 30000 && !weapon2Spawned){
            playerWeapon2 = getGameWorld().spawn("PlayerWeapon2");
            UIEntityText = getGameWorld().spawn("entityText", new SpawnData(playerWeapon2.getX()-3, playerWeapon2.getY() - 10).put("text", "Missiles"));
            weapon2PowerAlive = true;
            weapon2Spawned = true;
        }
        if (totalScore > 70000 && totalScore < 71000 && !weapon3Spawned){
            playerWeapon3 = getGameWorld().spawn("PlayerWeapon3");
            UIEntityText = getGameWorld().spawn("entityText", new SpawnData(playerWeapon3.getX()-3, playerWeapon3.getY() - 10).put("text", "Lightning"));
            weapon3PowerAlive = true;
            weapon3Spawned = true;
        }
        //if (totalScore > 500 && totalScore < 1500 && !boss1Spawned){
        if (totalScore > 19000 && totalScore < 19700 && !boss1Spawned){
            bossFightClearRoom();
            boss1 = getGameWorld().spawn("Boss1", getAppHeight() / 2, getAppWidth() / 2);
            boss1Alive = true;
            boss1Spawned = true;
            TimerBoss1Bullet = getGameTimer().runAtInterval(() ->
            {
                Point2D point2D = boss1.getPosition();
                SpawnData spawnData = new SpawnData(point2D);
                Point2D direction = player.getPosition().subtract(point2D);
                spawnData.put("direction", direction);
                spawnData.put("owner", boss1);
                spawn("boss1Bullet", spawnData);
            }, Duration.seconds(1.75));
        }
        if (totalScore > 49000 && totalScore < 50000 && !boss2Spawned){
            bossFightClearRoom();
            boss2 = getGameWorld().spawn("Boss2", getAppHeight() / 2, getAppWidth() / 2);
            boss2Alive = true;
            boss2Spawned = true;
            TimerBoss2Bullet = getGameTimer().runAtInterval(() ->
            {
                Point2D point2D = boss2.getPosition();
                SpawnData spawnData = new SpawnData(point2D);
                Point2D direction = player.getPosition().subtract(point2D);
                spawnData.put("direction", direction);
                spawnData.put("owner", boss2);
                spawn("boss2Bullet", spawnData);
            }, Duration.seconds(1.5));
        }
        if (totalScore > 94000 && totalScore < 95000 && !boss3Spawned){
            bossFightClearRoom();
            boss3 = getGameWorld().spawn("Boss3", getAppHeight() / 2, getAppWidth() / 2);
            boss3Alive = true;
            boss3Spawned = true;
            TimerBoss3Bullet = getGameTimer().runAtInterval(() ->
            {
                Point2D point2D = boss3.getPosition();
                SpawnData spawnData = new SpawnData(point2D);
                Point2D direction = player.getPosition().subtract(point2D);
                spawnData.put("direction", direction);
                spawnData.put("owner", boss3);
                spawn("boss3Bullet", spawnData);
            }, Duration.seconds(1));
        }
    }
    @Override
    protected void initUI(){
        /** Implements Bouncing text and lives to UI*/
        var score = getUIFactory().newText("", 24);
        score.textProperty().bind(getip("score").asString("Score: [%d]"));
        getGameState().addListener("score", (prev, now) -> {
            animationBuilder()
                    .duration(Duration.seconds(0.3))
                    .interpolator(Interpolators.BOUNCE.EASE_OUT())
                    .repeat(2)
                    .autoReverse(true)
                    .scale(score)
                    .from(new Point2D(1, 1))
                    .to(new Point2D(1.2, 1.2))
                    .buildAndPlay();
        });
        var lives = getUIFactory().newText("", 24);
        lives.textProperty().bind(getip("lives").asString("Lives: [%d]"));
        getGameState().addListener("lives", (prev, now) -> {
            animationBuilder()
                    .duration(Duration.seconds(0.3))
                    .interpolator(Interpolators.BOUNCE.EASE_OUT())
                    .repeat(2)
                    .autoReverse(true)
                    .scale(lives)
                    .from(new Point2D(1, 1))
                    .to(new Point2D(1.2, 1.2))
                    .buildAndPlay();
        });
        var gameState = getUIFactory().newText("", 24);
        gameState.textProperty().bind(getip("gameState").asString("Game State:"));

        addUINode(score, 15, 750);
        addUINode(lives,775, 750);
        addUINode(gameState,10 , 40);
    }
    /**
     * Coin point method, increases score by 250 points.
     */
    public void onCoinPickup(){
        Sound coinPickedUp = getAssetLoader().loadSound("CoinPickedUp.wav");
        getAudioPlayer().playSound(coinPickedUp);
        getGameState().increment("score", +250);
        /** adds 250 points to totalScore for the log, every time player picks up coin */
        totalScore = totalScore + 250;
    }
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
    /**
     * Player death method, decreases lives by 1, if player collides with Evil Puff
     */
    public void onPlayerDeath(){
        canShoot = false;
        getGameState().increment("lives", -1);
        playerLives--;
    }
    public void playerPowerUp(){
        UIPowerText = getGameWorld().spawn("playerBuffText", new SpawnData(200,40).put("text", "UNIVERSAL POWERS CONSUME ENEMIES"));
        hasPowerUp = true;
        TimerPowerUp.pause();
        TimerFreezePowerUp.pause();
        getAudioPlayer().stopMusic(music);
        FXGL.runOnce(() ->{
            Sound poweredUp = getAssetLoader().loadSound("PoweredUp.wav");
            getAudioPlayer().playSound(poweredUp);
            /** Set "PoweredUp.wav to start playing, after 1 sec delay */
            }, Duration.seconds(1));
        FXGL.set("poweredUp", true);
    }

    public void playerPowerOff(){
        UIPowerText.removeFromWorld();
        hasPowerUp = false;
        TimerPowerUp.resume();
        TimerFreezePowerUp.resume();
        getAudioPlayer().playMusic(music);
        Sound poweredUp = getAssetLoader().loadSound("PoweredUp.wav");
        getAudioPlayer().stopSound(poweredUp);
        FXGL.set("poweredUp", false);
    }
    public void playerFreezePowerUp(){
        UIFreezeText = getGameWorld().spawn("playerBuffText", new SpawnData(200, 40).put("text", "ENEMY FROZEN KNOCK EM OUT"));
        hasFreezePower = true;
        TimerPowerUp.pause();
        TimerFreezePowerUp.pause();
        getAudioPlayer().stopMusic(music);
        FXGL.runOnce(() ->{
            Sound freezeOn = getAssetLoader().loadSound("FreezeOn.wav");
            getAudioPlayer().playSound(freezeOn);
            /** Sets duration of the player FreezeOnBuff */
            }, Duration.seconds(1));
        List<Entity> evilPuff = getGameWorld().getEntitiesFiltered(p -> p.isType(EntityType.ENEMY));
        for (int i = 0; i <evilPuff.size() ; i++){
            evilPuff.get(i).setProperty("velocity", new Point2D((Math.random() * 0.25) + 0.50, (Math.random() * 0.25) + 0.50));
        }
        FXGL.set("freezePoweredUp", true);
    }
    public void playerFreezePowerOff(){
        hasFreezePower = false;
        UIFreezeText.removeFromWorld();
        TimerPowerUp.resume();
        TimerFreezePowerUp.resume();
        getAudioPlayer().playMusic(music);
        Sound freezeOn = getAssetLoader().loadSound("FreezeOn.wav");
        getAudioPlayer().stopSound(freezeOn);
        List<Entity> evilPuff = getGameWorld().getEntitiesFiltered(p -> p.isType(EntityType.ENEMY));
        for (int i = 0; i < evilPuff.size() ; i++){
            evilPuff.get(i).setProperty("velocity", new Point2D((Math.random() * 1.5) + 4, (Math.random() * 1.5) + 4));
        }
        FXGL.set("freezePoweredUp", false);
    }
    public void playerWeapon1On(){
        UIStampedeText = getGameWorld().spawn("stampedeText", new SpawnData(200, 40).put("text", "ENEMY ON STAMPEDE USE FIREBALL"));
        hasWeapon1Power = true;
        enemyStampede = true;
        //getAudioPlayer().stopMusic(music);
        TimerPowerUp.pause();
        TimerFreezePowerUp.pause();
        TimerStampede.resume();
        FXGL.set("enemyStampede", true);
        FXGL.set("playerWeapon1Up", true);
    }
    public void playerWeapon1Off(){
        UIStampedeText.removeFromWorld();
        hasWeapon1Power = false;
        enemyStampede = false;
        TimerPowerUp.resume();
        TimerFreezePowerUp.resume();
        //getAudioPlayer().playMusic(music);
        TimerStampede.pause();
        FXGL.set("playerWeapon1Up", false);
        FXGL.set("enemyStampede", false);
    }
    public void playerWeapon2On(){
        UIStampedeText = getGameWorld().spawn("stampedeText", new SpawnData(200, 40).put("text", "ENEMY ON STAMPEDE USE CANNON"));
        hasWeapon2Power = true;
        enemyStampede = true;
        TimerPowerUp.pause();
        TimerFreezePowerUp.pause();
        //getAudioPlayer().stopMusic(music);
        TimerStampede.resume();
        FXGL.set("enemyStampede", true);
        FXGL.set("playerWeapon2Up", true);
    }
    public void playerWeapon2Off(){
        UIStampedeText.removeFromWorld();
        hasWeapon2Power = false;
        enemyStampede = false;
        TimerPowerUp.resume();
        TimerFreezePowerUp.resume();
        //getAudioPlayer().playMusic(music);
        TimerStampede.pause();
        FXGL.set("playerWeapon2Up", false);
        FXGL.set("enemyStampede", false);
    }
    public void playerWeapon3On(){
        UIStampedeText = getGameWorld().spawn("stampedeText", new SpawnData(200, 40).put("text", "ENEMY ON STAMPEDE USE LIGHTNING"));
        hasWeapon3Power = true;
        enemyStampede = true;
        TimerPowerUp.pause();
        TimerFreezePowerUp.pause();
        //getAudioPlayer().stopMusic(music);
        TimerStampede.resume();
        FXGL.set("enemyStampede", true);
        FXGL.set("playerWeapon3Up", true);
    }
    public void playerWeapon3Off(){
        UIStampedeText.removeFromWorld();
        hasWeapon3Power = false;
        enemyStampede = false;
        TimerPowerUp.resume();
        TimerFreezePowerUp.resume();
        playerPowerOff();
        //getAudioPlayer().playMusic(music);
        TimerStampede.pause();
        FXGL.set("playerWeapon3Up", false);
        FXGL.set("enemyStampede", false);
    }
    public void bossFightClearRoom(){
        bossFightOn = true;
        enemyStampede = false;
        FXGL.set("bossFightOn", true);
        TimerCoin.pause();
        TimerPowerUp.pause();
        TimerFreezePowerUp.pause();
        TimerStampede.pause();
        TimerEnemy.pause();
        List<Entity> enemies = getGameWorld().getEntitiesFiltered(p -> p.isType(EntityType.ENEMY) || p.isType(EntityType.FREEZEPOWER) || p.isType(EntityType.POWERUP) || p.isType(EntityType.COIN) || p.isType(EntityType.PLAYERWEAPON2) || p.isType(EntityType.PLAYERWEAPON1) || p.isType(EntityType.PLAYERWEAPON3));
        for (int i = 0; i <enemies.size() ; i++){
            enemies.get(i).removeFromWorld();
        }
        UIBossFightText = getGameWorld().spawn("bossText", new SpawnData(200, 40).put("text", "BOSS MODE, USE FIREARMS"));
    }
    public void killBoss1(Entity boss1){
        UIBossFightText.removeFromWorld();
        spawn("ParticleExplosionBoss1Dead", boss1.getCenter());
        boss1.removeFromWorld();
        FXGL.set("bossFightOn", false);
        bossFightOn = false;
        boss1Spawned = true;
        boss1Alive = false;
        TimerBoss1Bullet.pause();
        runOnce(()-> {
            TimerEnemy.resume();
            TimerCoin.resume();
            TimerPowerUp.resume();
            TimerFreezePowerUp.resume();
            getAudioPlayer().playMusic(music);
        }, Duration.seconds(5));
    }
    public void killBoss2(Entity boss2){
        UIBossFightText.removeFromWorld();
        spawn("ParticleExplosionBoss2Dead", boss2.getCenter());
        boss2.removeFromWorld();
        FXGL.set("bossFightOn", false);
        bossFightOn = false;
        boss2Spawned = true;
        boss2Alive = false;
        TimerBoss2Bullet.pause();
        runOnce(()-> {
            TimerEnemy.resume();
            TimerCoin.resume();
            TimerPowerUp.resume();
            TimerFreezePowerUp.resume();
            getAudioPlayer().playMusic(music);
        }, Duration.seconds(5));
    }
    public void killBoss3(Entity boss3){
        UIBossFightText.removeFromWorld();
        spawn("ParticleExplosionBoss3Dead", boss3.getCenter());
        boss3.removeFromWorld();
        FXGL.set("bossFightOn", false);
        bossFightOn = false;
        boss3Spawned = true;
        boss3Alive = false;
        TimerBoss3Bullet.pause();
        runOnce(()-> {
            TimerEnemy.resume();
            TimerCoin.resume();
            TimerPowerUp.resume();
            TimerFreezePowerUp.resume();
            getAudioPlayer().playMusic(music);
        }, Duration.seconds(5));
    }
    /****
     * Map Strings to Scene
     */
    @Override
    protected void initGameVars(Map<String, Object> vars){
        vars.put("score", 0);
        vars.put("lives", 3);
        vars.put("scoreText", 0);
        vars.put("gameState", 0);

        vars.put("poweredUp", false);
        vars.put("safeRespawn", false);
        vars.put("freezePoweredUp", false);
        vars.put("bossFightOn", false);
        vars.put("enemyStampede", false);
        vars.put("playerWeapon1Up", false);
        vars.put("playerWeapon2Up", false);
        vars.put("playerWeapon3Up", false);
    }
    private void respawn(){
        safeRespawn = true;
        player = spawn("Player", 300, 300);
        Sound respawn = getAssetLoader().loadSound("Respawn.wav");
        getAudioPlayer().playSound(respawn);
        FXGL.runOnce(() ->{
            safeRespawn = false;
            /** Sets respawn timer, where player is not able to collide with evilPuffs for the duration*/
            }, Duration.seconds(3));
    }
    /**
     * saves players score to file method
     */
    public void saveToFile(int totalScore){
        try {
            File file = new File("src/main/Control/HighScoreLog/TotalScore.txt"); //LAPTOP.
            //File file = new File("resources/TotalScore.txt"); // For JAR
            if (file.exists()){
                if (SpencerMainMenu.getHighScoreMap().containsKey(playerName)) {
                    if (SpencerMainMenu.getHighScoreMap().get(playerName) > totalScore) {
                        System.out.println("Old high score not beaten, score not saved");
                    }
                    else {
                        BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
                        String text = "\nPlayer " + playerName + ": your score ended as: " + totalScore + "\n************************************";
                        output.append(text);
                        output.close();
                    }
                }
                else {
                    BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
                    String text = "\nPlayer " + playerName + ": your score ended as: " + totalScore + "\n************************************";
                    output.append(text);
                    output.close();
                }
            }
            else{
                PrintWriter output = new PrintWriter(file);
                output.print(log);
                output.close();
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Sorry, Highscore save was failed, try again! ");
            e.printStackTrace();
            }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}