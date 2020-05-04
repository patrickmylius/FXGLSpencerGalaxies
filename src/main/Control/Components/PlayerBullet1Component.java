package main.Control.Components;

import main.EntityType;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;

@Required(OwnerComponent.class)
public class PlayerBullet1Component extends Component {

    private OwnerComponent owner;
    private double speed;

    public PlayerBullet1Component(double speed){
        this.speed = speed;
    }
    @Override
    public void onUpdate(double tpf){
        entity.translateY(owner.getValue() == (EntityType.PLAYER) ? -tpf * speed: tpf * speed);
    }
}
