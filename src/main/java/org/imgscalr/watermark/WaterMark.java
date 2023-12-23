package org.imgscalr.watermark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class WaterMark {

    private static final Logger logger = LoggerFactory.getLogger(WaterMark.class);

    private final Position position;
    private final BufferedImage bfi;
    private final float opacity;
//    private final int ratio;

    public WaterMark(BufferedImage bfi) {
        this(bfi, Position.CENTER, 1.0f);
    }

    public WaterMark(BufferedImage bfi, Position position, float opacity) {
        this.position = position;
        this.bfi = bfi;
        this.opacity = opacity;
    }

    /**
     * Embeds a textual watermark over a source image
     */
    public void addTextWatermark(TextWatermarkOptions option) {
        Graphics2D g2d = (Graphics2D) bfi.getGraphics();

        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        g2d.setComposite(alphaChannel);
        g2d.setFont(option.getFont());
        g2d.setColor(option.getColor());
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(option.getText(), g2d);

        Pair<Integer, Integer> pos = position.calculate(
                bfi.getWidth(),
                bfi.getHeight(),
                (int) rect.getWidth(),
                (int) rect.getHeight()
        );

        g2d.drawString(option.getText(), pos.getLeft(), pos.getRight());
        g2d.dispose();
    }

    /**
     * Embeds an image watermark over a source image
     *
     * @param file The image file used as the watermark.
     */
    public BufferedImage addImageWatermark(File file) {
        try {
            if(file == null) {
                throw new RuntimeException("Watermark file can't be empty");
            }
            BufferedImage watermarkImage = ImageIO.read(file);

            // initializes necessary graphic properties
            Graphics2D g2d = (Graphics2D) bfi.getGraphics();
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2d.setComposite(alphaChannel);

            Pair<Integer, Integer> pos = position.calculate(
                    bfi.getWidth(),
                    bfi.getHeight(),
                    watermarkImage.getWidth(),
                    watermarkImage.getHeight()
            );

            // paints the image watermark
            g2d.drawImage(watermarkImage, pos.getLeft(), pos.getRight(), null);
            g2d.dispose();
            return bfi;
        } catch (Exception ex) {
            logger.error("Error adding water mark", ex);
            throw new RuntimeException(ex);
        }
    }

    public enum Position {
        TOP_LEFT {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return Pair.of(0 ,0);
            }
        },
        TOP_CENTER {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return Pair.of(
                        (imageWidth - waterMarkWidth) / 2,
                        imageHeight - (imageHeight - waterMarkHeight)
                );
            }
        },
        TOP_RIGHT {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return Pair.of(
                        imageWidth - waterMarkWidth,
                        0
                );
            }
        },
        CENTER_LEFT {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return Pair.of(
                        0,
                        (imageHeight - waterMarkHeight) / 2
                );
            }
        },
        CENTER {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return Pair.of(
                        (imageWidth - waterMarkWidth) / 2,
                        (imageHeight - waterMarkHeight) / 2
                );
            }
        },
        CENTER_RIGHT {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return Pair.of(
                        imageWidth - waterMarkWidth,
                        imageHeight / 2
                );
            }
        },
        BOTTOM_LEFT {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return  Pair.of(
                        0,
                        imageHeight - waterMarkHeight
                );
            }
        },
        BOTTOM_CENTER {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return Pair.of(
                        (imageWidth - waterMarkWidth) / 2,
                        imageHeight - waterMarkHeight
                );
            }
        },
        BOTTOM_RIGHT {
            @Override
            public Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                return Pair.of(
                        imageWidth - waterMarkWidth,
                        imageHeight - waterMarkHeight
                );
            }
        };

        public abstract Pair<Integer, Integer> calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight);

    }
}
