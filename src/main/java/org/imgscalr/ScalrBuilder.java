package org.imgscalr;

import org.imgscalr.Scalr.Mode;
import org.imgscalr.watermark.ImageWatermarkOptions;
import org.imgscalr.watermark.TextWatermarkOptions;
import org.imgscalr.watermark.WaterMark;
import org.imgscalr.watermark.WaterMark.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScalrBuilder {

    private int width;
    private int height;
    private String outputFormat;
    private BufferedImage bfi;

    public ScalrBuilder of(final String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name can not be null.");
        }
        bfi = ImageIO.read(Files.newInputStream(Paths.get(fileName)));
        return this;
    }

    public ScalrBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ScalrBuilder outputFormat(final String format) {
        this.outputFormat = format;
        return this;
    }

    public ScalrBuilder resize(Mode mode) {
        bfi = Scalr.resize(bfi, mode, width, height);
        return this;
    }

    public ScalrBuilder resize() {
        bfi = Scalr.resize(bfi, Mode.AUTOMATIC, width, height);
        return this;
    }

    public ScalrBuilder addTextWaterMark(final String text, Position position, float opacity) {
        if(text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text can not be null");
        }
        TextWatermarkOptions watermarkOptions = new TextWatermarkOptions(
                text,
                "Arial", 20, Font.CENTER_BASELINE,
                Color.RED);
        WaterMark waterMark = new WaterMark(bfi, position, opacity);
        waterMark.addTextWatermark(watermarkOptions);
        return this;
    }

    public ScalrBuilder addTextWaterMark(final String text, TextWatermarkOptions option) {
        if(text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text can not be null");
        }
        WaterMark waterMark = new WaterMark(bfi);
        waterMark.addTextWatermark(option);
        return this;
    }

    public ScalrBuilder addImageWatermark(File watermark) {
        WaterMark wm = new WaterMark(bfi);
        wm.addImageWatermark(watermark);
        return this;
    }

    public ScalrBuilder addImageWatermark(File watermark, Position position, float opacity) {
        WaterMark wm = new WaterMark(bfi, position, opacity);
        wm.addImageWatermark(watermark);
        return this;
    }

    public ScalrBuilder addImageWatermark(ImageWatermarkOptions options) {
        WaterMark wm = new WaterMark(bfi);
        wm.addImageWatermark(options);
        return this;
    }

    public ScalrBuilder addImageWatermark(ImageWatermarkOptions option, Position position, float opacity) {
        WaterMark wm = new WaterMark(bfi, position, opacity);
        wm.addImageWatermark(option);
        return this;
    }

    public void toFile(final String fileName) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("File name can not be empty");
            }
            if (outputFormat == null || outputFormat.isEmpty()) {
                String format = fileName.substring(fileName.lastIndexOf('.') + 1);
                outputFormat = !format.isEmpty() ? format : "png";
            }
            ImageIO.write(bfi, outputFormat, outputStream);
            outputStream.flush();
            Files.write(Paths.get(fileName), outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error exporting to file.");
        }
    }

    public BufferedImage toBufferedImage() {
        return this.bfi;
    }
}
