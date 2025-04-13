package de.taktikcrew.lobbysystem.gadgets.meta;

import com.google.common.collect.Maps;
import de.taktikcrew.lobbysystem.gadgets.AbstractGadget;
import de.taktikcrew.lobbysystem.gadgets.boots.*;

import java.util.Map;
import java.util.function.Function;

public class GadgetFactory {

    private static final Map<String, Function<Boolean, AbstractGadget>> factories = Maps.newHashMap();

    static {
        factories.put(AngryBoots.class.getSimpleName(), AngryBoots::new);
        factories.put(BoneBoots.class.getSimpleName(), BoneBoots::new);
        factories.put(BunnyBoots.class.getSimpleName(), BunnyBoots::new);
        factories.put(CloudBoots.class.getSimpleName(), CloudBoots::new);
        factories.put(CookieBoots.class.getSimpleName(), CookieBoots::new);
        factories.put(EnderBoots.class.getSimpleName(), EnderBoots::new);
        factories.put(ExplosiveBoots.class.getSimpleName(), ExplosiveBoots::new);
        factories.put(FireBoots.class.getSimpleName(), FireBoots::new);
        factories.put(GhostBoots.class.getSimpleName(), GhostBoots::new);
        factories.put(IceBoots.class.getSimpleName(), IceBoots::new);
        factories.put(JetPackBoots.class.getSimpleName(), JetPackBoots::new);
        factories.put(LavaBoots.class.getSimpleName(), LavaBoots::new);
        factories.put(LoveBoots.class.getSimpleName(), LoveBoots::new);
        factories.put(MusicBoots.class.getSimpleName(), MusicBoots::new);
        factories.put(RainyBoots.class.getSimpleName(), RainyBoots::new);
        factories.put(SmokeBoots.class.getSimpleName(), SmokeBoots::new);
        factories.put(SpeedBoots.class.getSimpleName(), SpeedBoots::new);
        factories.put(SuperJumpBoots.class.getSimpleName(), SuperJumpBoots::new);
        factories.put(YoloBoots.class.getSimpleName(), YoloBoots::new);
    }

    public static AbstractGadget gadget(String id, boolean active) {
        return factories.getOrDefault(id, _ -> null).apply(active);
    }

}
