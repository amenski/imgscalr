package org.imgscalr;

import org.imgscalr.util.Pair;
import org.imgscalr.watermark.ImageWatermarkOptions;
import org.imgscalr.watermark.WaterMark;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;

@Ignore // ignored for now
public class ScalrBuilderTest {

    @Test
    public void testWaterMarkWitText() throws Exception {
        System.setProperty(Scalr.DEBUG_PROPERTY_NAME, "true");
        ScalrBuilder builder  = new ScalrBuilder().of("src/test/resources/org/imgscalr/mr-t.jpg")
                .size(400,200)
                .addImageWatermark(Paths.get("src/test/resources/org/imgscalr/watermark.jpg").toFile(), WaterMark.Position.CENTER, 0.5f)
//				.addTextWaterMark("TOP_CENTER", Position.TOP_CENTER, 0.9f)
//				.addTextWaterMark("TOP_LEFT", Position.TOP_LEFT, 0.9f)
//				.addTextWaterMark("TOP_RIGHT", Position.TOP_RIGHT, 0.9f)
//				.addTextWaterMark("CENTER_LEFT", Position.CENTER_LEFT, 0.9f)
//				.addTextWaterMark("CENTER", Position.CENTER, 0.9f)
//				.addTextWaterMark("CENTER_RIGHT", Position.CENTER_RIGHT, 0.9f)
//				.addTextWaterMark("BOTTOM_LEFT", Position.BOTTOM_LEFT, 0.9f)
//				.addTextWaterMark("BOTTOM_CENTER", Position.BOTTOM_CENTER, 0.9f)
//				.addTextWaterMark("BOTTOM_RIGHT", Position.BOTTOM_RIGHT, 0.9f)
                .resize();
        builder.toFile("src/test/resources/org/imgscalr/mr-t-text-water-marked.png");

//		Position p = Position.CENTER;
//		p.calculate(200, 200, 100, 100);

//		BufferedImage result = builder.toBufferedImage();
//		Assert.assertEquals (result.getWidth(), p.getX());
    }


    @Test
    public void testWaterMarkWitText1() throws Exception {
        System.setProperty(Scalr.DEBUG_PROPERTY_NAME, "true");
        ScalrBuilder builder  = new ScalrBuilder().of("src/test/resources/org/imgscalr/mr-t.jpg")
                .size(200,200)
                .addTextWaterMark("TOP_CENTER", WaterMark.Position.TOP_CENTER, 0.9f)
                .addTextWaterMark("TOP_LEFT", WaterMark.Position.TOP_LEFT, 0.9f)
                .addTextWaterMark("TOP_RIGHT", WaterMark.Position.TOP_RIGHT, 0.9f)
                .addTextWaterMark("CENTER_LEFT", WaterMark.Position.CENTER_LEFT, 0.9f)
                .addTextWaterMark("CENTER", WaterMark.Position.CENTER, 0.9f)
                .addTextWaterMark("CENTER_RIGHT", WaterMark.Position.CENTER_RIGHT, 0.9f)
                .addTextWaterMark("BOTTOM_LEFT", WaterMark.Position.BOTTOM_LEFT, 0.9f)
                .addTextWaterMark("BOTTOM_CENTER", WaterMark.Position.BOTTOM_CENTER, 0.9f)
                .addTextWaterMark("BOTTOM_RIGHT", WaterMark.Position.BOTTOM_RIGHT, 0.9f)
                .resize();
//		builder.toFile("src/test/resources/org/imgscalr/mr-t-text-water-marked.png");

        WaterMark.Position p = WaterMark.Position.CENTER;
        p.calculate(200, 200, 100, 100);

        BufferedImage result = builder.toBufferedImage();
    }

    @Test
    public void testWaterMarkWithImage() throws Exception {
        System.setProperty(Scalr.DEBUG_PROPERTY_NAME, "true");
        ImageWatermarkOptions options = new ImageWatermarkOptions(
                Paths.get("src/test/resources/org/imgscalr/watermark.jpg").toFile(),
                30,
                Pair.of(400, 250)
        );
       new ScalrBuilder().of("src/test/resources/org/imgscalr/mr-t.jpg")
                .size(400,250)
                .addImageWatermark(options)
                .resize()
               .toFile("src/test/resources/org/imgscalr/mr-t-text-water-marked.png");
    }

    @Test
    public void testWaterMarkWithImage_2() throws Exception {
        System.setProperty(Scalr.DEBUG_PROPERTY_NAME, "true");
        ImageWatermarkOptions options = new ImageWatermarkOptions(
                Paths.get("src/test/resources/org/imgscalr/watermark.jpg").toFile(),
                10,
                Pair.of(400, 250)
        );
       new ScalrBuilder().of("src/test/resources/org/imgscalr/mr-t.jpg")
                .size(400,250)
                .addImageWatermark(options, WaterMark.Position.TOP_RIGHT, 0.8f)
                .resize()
               .toFile("src/test/resources/org/imgscalr/mr-t-text-water-marked.png");
    }
}
