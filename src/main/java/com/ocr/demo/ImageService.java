package com.ocr.demo;

import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    private final ImageLoader imageLoader;
    private final GrayScale grayScale;
    private final TextService textService;
    private final String outputDir;

    ImageService(
            ImageLoader imageLoader,
            GrayScale grayScale,
            TextService textService,
            @Value("${output.dir}") String outputDir) {
        this.imageLoader = imageLoader;
        this.grayScale = grayScale;
        this.textService = textService;
        this.outputDir = outputDir;
    }

    public void process() {
        imageLoader.list().stream().forEach(
            imageFile -> {
                Mat image = Imgcodecs.imread(imageFile.getAbsolutePath());
                grayScale.rescale(image);

//                byte[] imageBytes = new byte[(int) (image.total() * image.channels())];
//                image.get(0, 0, imageBytes);
//                textService.extract(imageBytes);

                String filename = FilenameUtils.concat(
                        outputDir,
                        FilenameUtils.getBaseName(imageFile.getName()) + "_" + System.currentTimeMillis() + ".png"
                );
                LOGGER.debug("Writing {}", filename);
                Imgcodecs.imwrite(filename, image, new MatOfInt(Imgcodecs.CV_IMWRITE_PNG_COMPRESSION));

                textService.extract(filename);

            }
        );
    }

}
