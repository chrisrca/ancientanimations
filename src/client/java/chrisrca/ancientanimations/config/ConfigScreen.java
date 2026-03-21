package chrisrca.ancientanimations.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    public Config config;

    private SliderWidget transXSlider, transYSlider, transZSlider;
    private SliderWidget rotXSlider, rotYSlider, rotZSlider, rotY2Slider;

    private SliderWidget itemPosXSlider, itemPosYSlider, itemPosZSlider;
    private SliderWidget itemRotXSlider, itemRotYSlider, itemRotZSlider;
    private SliderWidget itemScaleSlider;

    public ConfigScreen(Config config) {
        super(Text.empty());
        this.config = config;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        assert client != null;

        String swingTitle = "Swing Animation";
        context.drawText(client.textRenderer, swingTitle,
                width / 4 - client.textRenderer.getWidth(swingTitle) / 2, 8, -1, false);

        String itemTitle = "Item Transform";
        context.drawText(client.textRenderer, itemTitle,
                width * 3 / 4 - client.textRenderer.getWidth(itemTitle) / 2, 8, -1, false);
    }

    @Override
    protected void init() {
        int sliderW = 200;
        int startY = 30;
        int spacing = 25;

        int lx = width / 4 - sliderW / 2;

        transXSlider = new SliderWidget(lx, startY, sliderW, 20,
                Text.literal("Trans X: "), (config.swingTransX + 1.0) / 2.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Trans X: " + String.format("%.3f", value * 2.0 - 1.0)));
            }
            @Override protected void applyValue() { config.swingTransX = value * 2.0 - 1.0; }
            { updateMessage(); }
        };

        transYSlider = new SliderWidget(lx, startY + spacing, sliderW, 20,
                Text.literal("Trans Y: "), (config.swingTransY + 1.0) / 2.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Trans Y: " + String.format("%.3f", value * 2.0 - 1.0)));
            }
            @Override protected void applyValue() { config.swingTransY = value * 2.0 - 1.0; }
            { updateMessage(); }
        };

        transZSlider = new SliderWidget(lx, startY + spacing * 2, sliderW, 20,
                Text.literal("Trans Z: "), (config.swingTransZ + 1.0) / 2.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Trans Z: " + String.format("%.3f", value * 2.0 - 1.0)));
            }
            @Override protected void applyValue() { config.swingTransZ = value * 2.0 - 1.0; }
            { updateMessage(); }
        };

        rotXSlider = new SliderWidget(lx, startY + spacing * 3, sliderW, 20,
                Text.literal("Rot X: "), (config.swingRotX + 180.0) / 360.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Rot X: " + String.format("%.1f", value * 360.0 - 180.0) + "°"));
            }
            @Override protected void applyValue() { config.swingRotX = value * 360.0 - 180.0; }
            { updateMessage(); }
        };

        rotYSlider = new SliderWidget(lx, startY + spacing * 4, sliderW, 20,
                Text.literal("Rot Y: "), (config.swingRotY + 180.0) / 360.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Rot Y: " + String.format("%.1f", value * 360.0 - 180.0) + "°"));
            }
            @Override protected void applyValue() { config.swingRotY = value * 360.0 - 180.0; }
            { updateMessage(); }
        };

        rotZSlider = new SliderWidget(lx, startY + spacing * 5, sliderW, 20,
                Text.literal("Rot Z: "), (config.swingRotZ + 180.0) / 360.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Rot Z: " + String.format("%.1f", value * 360.0 - 180.0) + "°"));
            }
            @Override protected void applyValue() { config.swingRotZ = value * 360.0 - 180.0; }
            { updateMessage(); }
        };

        rotY2Slider = new SliderWidget(lx, startY + spacing * 6, sliderW, 20,
                Text.literal("Rot Y2: "), (config.swingRotY2 + 180.0) / 360.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Rot Y2: " + String.format("%.1f", value * 360.0 - 180.0) + "°"));
            }
            @Override protected void applyValue() { config.swingRotY2 = value * 360.0 - 180.0; }
            { updateMessage(); }
        };

        int rx = width * 3 / 4 - sliderW / 2;

        itemPosXSlider = new SliderWidget(rx, startY, sliderW, 20,
                Text.literal("Pos X: "), (config.itemPosX + 1.0) / 2.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Pos X: " + String.format("%.3f", value * 2.0 - 1.0)));
            }
            @Override protected void applyValue() { config.itemPosX = value * 2.0 - 1.0; }
            { updateMessage(); }
        };

        itemPosYSlider = new SliderWidget(rx, startY + spacing, sliderW, 20,
                Text.literal("Pos Y: "), (config.itemPosY + 1.0) / 2.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Pos Y: " + String.format("%.3f", value * 2.0 - 1.0)));
            }
            @Override protected void applyValue() { config.itemPosY = value * 2.0 - 1.0; }
            { updateMessage(); }
        };

        itemPosZSlider = new SliderWidget(rx, startY + spacing * 2, sliderW, 20,
                Text.literal("Pos Z: "), (config.itemPosZ + 1.0) / 2.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Pos Z: " + String.format("%.3f", value * 2.0 - 1.0)));
            }
            @Override protected void applyValue() { config.itemPosZ = value * 2.0 - 1.0; }
            { updateMessage(); }
        };

        itemRotXSlider = new SliderWidget(rx, startY + spacing * 3, sliderW, 20,
                Text.literal("Item Rot X: "), (config.itemRotX + 180.0) / 360.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Item Rot X: " + String.format("%.1f", value * 360.0 - 180.0) + "°"));
            }
            @Override protected void applyValue() { config.itemRotX = value * 360.0 - 180.0; }
            { updateMessage(); }
        };

        itemRotYSlider = new SliderWidget(rx, startY + spacing * 4, sliderW, 20,
                Text.literal("Item Rot Y: "), (config.itemRotY + 180.0) / 360.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Item Rot Y: " + String.format("%.1f", value * 360.0 - 180.0) + "°"));
            }
            @Override protected void applyValue() { config.itemRotY = value * 360.0 - 180.0; }
            { updateMessage(); }
        };

        itemRotZSlider = new SliderWidget(rx, startY + spacing * 5, sliderW, 20,
                Text.literal("Item Rot Z: "), (config.itemRotZ + 180.0) / 360.0) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Item Rot Z: " + String.format("%.1f", value * 360.0 - 180.0) + "°"));
            }
            @Override protected void applyValue() { config.itemRotZ = value * 360.0 - 180.0; }
            { updateMessage(); }
        };

        // Scale: range 0.1 to 3.0
        itemScaleSlider = new SliderWidget(rx, startY + spacing * 6, sliderW, 20,
                Text.literal("Scale: "), (config.itemScale - 0.1) / 2.9) {
            @Override protected void updateMessage() {
                setMessage(Text.literal("Scale: " + String.format("%.2f", value * 2.9 + 0.1)));
            }
            @Override protected void applyValue() { config.itemScale = value * 2.9 + 0.1; }
            { updateMessage(); }
        };

        this.addDrawableChild(transXSlider);
        this.addDrawableChild(transYSlider);
        this.addDrawableChild(transZSlider);
        this.addDrawableChild(rotXSlider);
        this.addDrawableChild(rotYSlider);
        this.addDrawableChild(rotZSlider);
        this.addDrawableChild(rotY2Slider);

        this.addDrawableChild(itemPosXSlider);
        this.addDrawableChild(itemPosYSlider);
        this.addDrawableChild(itemPosZSlider);
        this.addDrawableChild(itemRotXSlider);
        this.addDrawableChild(itemRotYSlider);
        this.addDrawableChild(itemRotZSlider);
        this.addDrawableChild(itemScaleSlider);
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