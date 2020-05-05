package main.Control.Components;

import main.EntityType;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;

@Required(OwnerComponent.class)
public class BossBulletComponent extends Component {
    private OwnerComponent owner;
    private double speed;
    public BossBulletComponent(double speed) {
        this.speed = speed;
    }

    @Override
    public void onUpdate(double tpf){
        entity.translateY(owner.getValue() == (EntityType.BOSS1) ? -tpf * speed: tpf * speed);
    }
}
