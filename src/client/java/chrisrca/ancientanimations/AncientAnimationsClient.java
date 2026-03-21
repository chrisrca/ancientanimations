package chrisrca.ancientanimations;

import chrisrca.ancientanimations.config.Config;
import chrisrca.ancientanimations.config.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AncientAnimationsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("AncientAnimations");

    private static final KeyBinding.Category CATEGORY = new KeyBinding.Category(
            Identifier.of("ancientanimations", "ancientanimations")
    );

    public KeyBinding bind;
    private static AncientAnimationsClient instance;
    public Config config;
    public static List<Item> items = new ArrayList<>();

    public static float swing17 = 0f;
    public static float prevSwing17 = 0f;
    private static int swing17Int = 0;
    private static boolean swing17InProgress = false;

    public AncientAnimationsClient() {
        instance = this;
    }

    public static AncientAnimationsClient getInstance() {
        return instance;
    }

    public static boolean isSword(Item item) {
        return item.getDefaultStack().isIn(ItemTags.SWORDS);
    }

    public static boolean isTool(Item item) {
        ItemStack stack = item.getDefaultStack();
        return stack.isIn(ItemTags.SWORDS)
                || stack.isIn(ItemTags.AXES)
                || stack.isIn(ItemTags.PICKAXES)
                || stack.isIn(ItemTags.SHOVELS)
                || stack.isIn(ItemTags.HOES)
                || item == Items.BOW
                || item == Items.CROSSBOW
                || item == Items.TRIDENT;
    }

    public static boolean isEatingOrDrinking(Item item, ItemStack stack) {
        net.minecraft.item.consume.UseAction action = item.getUseAction(stack);
        return action == net.minecraft.item.consume.UseAction.EAT
                || action == net.minecraft.item.consume.UseAction.DRINK;
    }

    public static boolean swing17InProgress() {
        return swing17InProgress;
    }

    public static void trigger17Swing() {
        int max = getSwingDuration();
        if (!swing17InProgress || swing17Int >= (max >> 1) || swing17Int < 0) {
            swing17InProgress = true;
            swing17Int = -1;
        }
    }

    public static float getSwing17Progress(float partialTicks) {
        float delta = swing17 - prevSwing17;
        if (delta < 0f) delta += 1f;
        return prevSwing17 + delta * partialTicks;
    }

    private static int getSwingDuration() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return 6;
        if (mc.player.hasStatusEffect(StatusEffects.HASTE)) {
            int amp = mc.player.getStatusEffect(StatusEffects.HASTE).getAmplifier();
            return Math.max(1, 5 - amp);
        }
        if (mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            int amp = mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier();
            return 6 + amp * 2;
        }
        return 6;
    }

    @Override
    public void onInitializeClient() {
        for (Field f : Items.class.getFields()) {
            try {
                Object output = f.get(null);
                if (output instanceof Item item) {
                    items.add(item);
                }
            } catch (IllegalAccessException ignored) {}
        }

        try {
            config = Config.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        bind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ancientanimations.settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                CATEGORY
        ));

        LOGGER.info("AncientAnimations starting!");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            prevSwing17 = swing17;
            int max = getSwingDuration();
            if (swing17InProgress) {
                ++swing17Int;
                if (swing17Int >= max) {
                    swing17Int = 0;
                    swing17InProgress = false;
                }
            } else {
                swing17Int = 0;
            }
            swing17 = (float) swing17Int / (float) max;

            while (bind.wasPressed()) {
                client.setScreen(new ConfigScreen(config));
            }
        });
    }
}