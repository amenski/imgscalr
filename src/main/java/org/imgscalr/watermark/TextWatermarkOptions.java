package org.imgscalr.watermark;

import java.awt.*;

public class TextWatermarkOptions {
    private final String text;
    private final String fontFamily;
    private final int fontSize;
    private final int style;
    private final Color color;

    public TextWatermarkOptions(String text, String fontFamily, int fontSize, int style, Color color) {
        this.text = text;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.style = style;
        this.color = color;
    }

    public String getText() {
        return this.text;
    }
    public Color getColor() {
        return this.color;
    }
    public Font getFont() {
        return new Font(this.fontFamily, this.style, this.fontSize);
    }
}