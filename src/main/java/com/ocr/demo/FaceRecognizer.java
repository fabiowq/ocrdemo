package com.ocr.demo;

import org.apache.commons.lang3.SystemUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Detects faces in an image, draws boxes around them, and writes the results
*/
@Component
public class FaceRecognizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaceRecognizer.class);

    private final CascadeClassifier faceDetector;

    public FaceRecognizer() {
        faceDetector = new CascadeClassifier(getPath("lbpcascade_frontalface.xml"));
    }

    public void recognize(Mat image) {
        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        LOGGER.debug("Detected {} faces", faceDetections.toArray().length);
        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }
    }

    private String getPath(String resourceName) {
        String path = getClass().getResource("/" + resourceName).getPath();
        if (SystemUtils.IS_OS_WINDOWS) {
            // Fix for OpenCV bug https://stackoverflow.com/questions/27344741/opencv-3-0-0-facedetect-sample-fails
            return path.substring(1).replaceAll("%20", " ");
        }
        return path;
    }

}
