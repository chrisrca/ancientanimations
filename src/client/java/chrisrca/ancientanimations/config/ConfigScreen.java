package chrisrca.ancientanimations.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends GameOptionsScreen {

    public ConfigScreen(Screen parent, Config config) {
        super(parent, MinecraftClient.getInstance().options, Text.literal("Ancient Animations"));
        this.config = config;
    }

    public ConfigScreen(Config config) {
        this(null, config);
    }

    private final Config config;

    private static SliderWidget makeSlider(String label, double value, double min, double max, SliderConsumer consumer) {
        double normalised = (value - min) / (max - min);
        return new SliderWidget(0, 0, 150, 20, Text.literal(label), normalised) {
            @Override
            protected void updateMessage() {
                double real = this.value * (max - min) + min;
                setMessage(Text.literal(label + String.format(
                        (max - min) > 10 ? " %.1f°" : " %.3f", real)));
            }
            @Override
            protected void applyValue() {
                consumer.accept(this.value * (max - min) + min);
            }
            { updateMessage(); }
        };
    }

    @FunctionalInterface
    interface SliderConsumer { void accept(double v); }

    private void resetSwing() {
        Config defaults = new Config();
        config.swingTransX = defaults.swingTransX;
        config.swingTransY = defaults.swingTransY;
        config.swingTransZ = defaults.swingTransZ;
        config.swingRotX   = defaults.swingRotX;
        config.swingRotY   = defaults.swingRotY;
        config.swingRotZ   = defaults.swingRotZ;
        config.swingRotY2  = defaults.swingRotY2;
        rebuildBody();
    }

    private void resetItem() {
        Config defaults = new Config();
        config.itemPosX  = defaults.itemPosX;
        config.itemPosY  = defaults.itemPosY;
        config.itemPosZ  = defaults.itemPosZ;
        config.itemRotX  = defaults.itemRotX;
        config.itemRotY  = defaults.itemRotY;
        config.itemRotZ  = defaults.itemRotZ;
        config.itemScale = defaults.itemScale;
        rebuildBody();
    }

    private void resetGeneral() {
        Config defaults = new Config();
        config.swingSpeedMultiplier = defaults.swingSpeedMultiplier;
        rebuildBody();
    }

    private void rebuildBody() {
        double scrollAmount = ((EntryListWidget<?>) body).getScrollY();
        remove(body);
        body = new OptionListWidget(client, this.width, this);
        addOptions();
        body.position(this.width, this.layout);
        addDrawableChild(body);
        ((EntryListWidget<?>) body).setScrollY(scrollAmount);
    }

    @Override
    protected void addOptions() {
        body.addWidgetEntry(
                new net.minecraft.client.gui.widget.TextWidget(Text.literal("Swing Animation"), client.textRenderer),
                null
        );

        body.addWidgetEntry(
                makeSlider("Trans X", config.swingTransX, -1, 1, v -> config.swingTransX = v),
                makeSlider("Trans Y", config.swingTransY, -1, 1, v -> config.swingTransY = v)
        );
        body.addWidgetEntry(
                makeSlider("Trans Z", config.swingTransZ, -1, 1, v -> config.swingTransZ = v),
                makeSlider("Rot X",   config.swingRotX,   -180, 180, v -> config.swingRotX = v)
        );
        body.addWidgetEntry(
                makeSlider("Rot Y",  config.swingRotY,  -180, 180, v -> config.swingRotY  = v),
                makeSlider("Rot Z",  config.swingRotZ,  -180, 180, v -> config.swingRotZ  = v)
        );
        body.addWidgetEntry(
                makeSlider("Rot Y2", config.swingRotY2, -180, 180, v -> config.swingRotY2 = v),
                ButtonWidget.builder(Text.literal("Reset Swing"), button -> resetSwing()).build()
        );

        body.addWidgetEntry(
                new net.minecraft.client.gui.widget.TextWidget(Text.literal("Item Transform"), client.textRenderer),
                null
        );

        body.addWidgetEntry(
                makeSlider("Pos X", config.itemPosX, -1, 1, v -> config.itemPosX = v),
                makeSlider("Pos Y", config.itemPosY, -1, 1, v -> config.itemPosY = v)
        );
        body.addWidgetEntry(
                makeSlider("Pos Z",      config.itemPosZ,  -1,   1,   v -> config.itemPosZ  = v),
                makeSlider("Item Rot X", config.itemRotX, -180, 180,  v -> config.itemRotX  = v)
        );
        body.addWidgetEntry(
                makeSlider("Item Rot Y", config.itemRotY, -180, 180, v -> config.itemRotY = v),
                makeSlider("Item Rot Z", config.itemRotZ, -180, 180, v -> config.itemRotZ = v)
        );
        body.addWidgetEntry(
                makeSlider("Scale", config.itemScale, 0.1, 3.0, v -> config.itemScale = v),
                ButtonWidget.builder(Text.literal("Reset Item"), button -> resetItem()).build()
        );

        body.addWidgetEntry(
                new net.minecraft.client.gui.widget.TextWidget(Text.literal("General"), client.textRenderer),
                null
        );

        body.addWidgetEntry(
                makeSlider("Swing Speed", config.swingSpeedMultiplier, 0.1, 5.0, v -> config.swingSpeedMultiplier = v),
                ButtonWidget.builder(Text.literal("Reset General"), button -> resetGeneral()).build()
        );
    }

    @Override
    public void close() {
        try {
            Config.save(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.close();
    }
}