package org.imgscalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.imgscalr.Scalr.log;

public class WaterMark {

    private final Position position;
    private BufferedImage bfi;
    private final float opacity;

    public WaterMark(Position position, BufferedImage bfi, float opacity) {
        this.position = position;
        this.bfi = bfi;
        this.opacity = opacity;
    }

    /**
     * Embeds a textual watermark over a source image
     *
     * @param text The text to be embedded as watermark.
     */
    public BufferedImage addTextWatermark(String text, Color color) {
        Graphics2D g2d = (Graphics2D) bfi.getGraphics();

        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
        g2d.setComposite(alphaChannel);
        g2d.setColor(color);
        g2d.setFont(new Font("Arial", Font.ITALIC, 64));
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

        position.calculate(
                bfi.getWidth(),
                bfi.getHeight(),
                (int) rect.getWidth(),
                (int) rect.getHeight()
        );

        g2d.drawString(text, position.x, position.y);
        g2d.dispose();
        return bfi;
    }

    /**
     * Embeds an image watermark over a source image
     *
     * @param watermarkImageFile The image file used as the watermark.
     */
    public BufferedImage addImageWatermark(File watermarkImageFile) throws IOException {
        try {
            BufferedImage watermarkImage = ImageIO.read(watermarkImageFile);

            // initializes necessary graphic properties
            Graphics2D g2d = (Graphics2D) bfi.getGraphics();
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2d.setComposite(alphaChannel);

            position.calculate(
                    bfi.getWidth(),
                    bfi.getHeight(),
                    watermarkImage.getWidth(),
                    watermarkImage.getHeight()
            );

            // paints the image watermark
            g2d.drawImage(watermarkImage, position.x, position.y, null);
            g2d.dispose();
            return bfi;
        } catch (IOException ex) {
            log(0, "Error adding water mark %s", ex.getMessage());
            throw ex;
        }
    }

    /**
     * water-mark positions
     *
     */
    public interface IPosition {
        void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight);
    }

    public enum Position implements IPosition {
        TOP_LEFT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = 0;
                y = 0;
            }
        },
        TOP_CENTER {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = (imageWidth - waterMarkWidth) / 2;
                y = 0;
            }
        },
        TOP_RIGHT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = imageWidth - waterMarkWidth;
                y = 0;
            }
        },
        MIDDLE_LEFT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = 0;
                y = (imageHeight - waterMarkHeight) / 2;
            }
        },
        CENTER {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = (imageWidth - waterMarkWidth) / 2;
                y = (imageHeight - waterMarkHeight) / 2;
            }
        },
        MIDDLE_RIGHT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = imageWidth - waterMarkWidth;
                y = (imageHeight - waterMarkHeight) / 2;
            }
        },
        BOTTOM_LEFT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = 0;
                y = imageHeight - waterMarkHeight;
            }
        },
        BOTTOM_CENTER {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = (imageWidth - waterMarkWidth) / 2;
                y = imageHeight - waterMarkWidth;
            }
        },
        BOTTOM_RIGHT {
            @Override
            public void calculate(int imageWidth, int imageHeight, int waterMarkWidth, int waterMarkHeight) {
                x = imageWidth - waterMarkWidth;
                y = imageHeight - waterMarkHeight;
            }
        };

        public int x;
        public int y;
    }
}
