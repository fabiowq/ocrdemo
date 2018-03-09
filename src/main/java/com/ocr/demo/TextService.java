package com.ocr.demo;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.tesseract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;

@Component
public class TextService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextService.class);

    public String extract(String imagePath) {
        tesseract.TessBaseAPI tesseractAPI = new tesseract.TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (tesseractAPI.Init(".", "ENG") != 0) {
            RuntimeException e = new RuntimeException("Could not initialize tesseract");
            LOGGER.error("Error initializing tesseract", e);
            throw e;
        }
        // Open input image with leptonica library
        lept.PIX image = pixRead(imagePath);
        tesseractAPI.SetImage(image);
        // Get OCR result
        BytePointer outText = tesseractAPI.GetUTF8Text();

        String string = outText.getString();
        LOGGER.info("OCR output:\n{}", string);

        // Destroy used object and release memory
        tesseractAPI.End();
        outText.deallocate();
        pixDestroy(image);

        return string;
    }


}
