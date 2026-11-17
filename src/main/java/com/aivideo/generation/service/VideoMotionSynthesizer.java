package com.aivideo.generation.service;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class VideoMotionSynthesizer {

    private static final Logger log = LoggerFactory.getLogger(VideoMotionSynthesizer.class);

    private final boolean ffmpegAvailable;

    public VideoMotionSynthesizer() {
        this.ffmpegAvailable = checkFfmpegAvailable();
        log.info("VideoMotionSynthesizer initialized. Native FFmpeg available: [{}]", this.ffmpegAvailable);
    }

    private boolean checkFfmpegAvailable() {
        try {
            Process process = new ProcessBuilder("ffmpeg", "-version").start();
            boolean finished = process.waitFor(3, TimeUnit.SECONDS);
            return finished && process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] synthesizeMotionVideo(byte[] imageBytes, String cameraMovement, int durationSeconds, String resolution) {
        if (durationSeconds <= 0) durationSeconds = 6;
        if (durationSeconds > 15) durationSeconds = 15;

        if (ffmpegAvailable) {
            try {
                return synthesizeWithFfmpeg(imageBytes, cameraMovement, durationSeconds, resolution);
            } catch (Exception e) {
                log.warn("FFmpeg synthesis encountered an error, falling back to pure Java encoder: {}", e.getMessage());
            }
        }

        return synthesizeWithJCodec(imageBytes, cameraMovement, durationSeconds, resolution);
    }

    private byte[] synthesizeWithFfmpeg(byte[] imageBytes, String cameraMovement, int durationSeconds, String resolution) throws IOException, InterruptedException {
        String id = UUID.randomUUID().toString();
        File tempInput = File.createTempFile("synth-in-" + id, ".png");
        File tempOutput = File.createTempFile("synth-out-" + id, ".mp4");

        try {
            Files.write(tempInput.toPath(), imageBytes);

            int width = 1280;
            int height = 720;
            if ("1080p".equalsIgnoreCase(resolution)) {
                width = 1920;
                height = 1080;
            }

            String mov = (cameraMovement != null) ? cameraMovement.toUpperCase() : "DOLLY";
            String filter;

            switch (mov) {
                case "PAN_LEFT":
                case "PAN":
                    filter = String.format("zoompan=z=1.12:d=%d:x='max(0,x-1.2)':y='ih/2-(ih/zoom/2)':s=%dx%d:fps=24",
                            durationSeconds * 24, width, height);
                    break;
                case "PAN_RIGHT":
                    filter = String.format("zoompan=z=1.12:d=%d:x='min((iw-iw/zoom),(x+1.2))':y='ih/2-(ih/zoom/2)':s=%dx%d:fps=24",
                            durationSeconds * 24, width, height);
                    break;
                case "ZOOM_OUT":
                    filter = String.format("zoompan=z='if(lte(zoom,1.0),1.22,max(1.001,zoom-0.0015))':d=%d:x='iw/2-(iw/zoom/2)':y='ih/2-(ih/zoom/2)':s=%dx%d:fps=24",
                            durationSeconds * 24, width, height);
                    break;
                case "TILT":
                case "PEDESTAL":
                    filter = String.format("zoompan=z=1.12:d=%d:x='iw/2-(iw/zoom/2)':y='min((ih-ih/zoom),(y+1.0))':s=%dx%d:fps=24",
                            durationSeconds * 24, width, height);
                    break;
                case "STATIC":
                    filter = String.format("scale=%d:%d:force_original_aspect_ratio=decrease,pad=%d:%d:(ow-iw)/2:(oh-ih)/2",
                            width, height, width, height);
                    break;
                case "ZOOM_IN":
                case "DOLLY":
                case "ORBIT":
                default:
                    filter = String.format("zoompan=z='min(zoom+0.0012,1.25)':d=%d:x='iw/2-(iw/zoom/2)':y='ih/2-(ih/zoom/2)':s=%dx%d:fps=24",
                            durationSeconds * 24, width, height);
                    break;
            }

            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-y",
                    "-loop", "1",
                    "-i", tempInput.getAbsolutePath(),
                    "-vf", filter,
                    "-c:v", "libx264",
                    "-t", String.valueOf(durationSeconds),
                    "-pix_fmt", "yuv420p",
                    "-movflags", "+faststart",
                    tempOutput.getAbsolutePath()
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();
            boolean completed = process.waitFor(60, TimeUnit.SECONDS);

            if (completed && process.exitValue() == 0 && tempOutput.exists() && tempOutput.length() > 0) {
                log.info("FFmpeg successfully rendered MP4 video ({} bytes)", tempOutput.length());
                return Files.readAllBytes(tempOutput.toPath());
            }

            throw new RuntimeException("FFmpeg process failed with exit code: " + process.exitValue());
        } finally {
            if (tempInput.exists()) tempInput.delete();
            if (tempOutput.exists()) tempOutput.delete();
        }
    }

    private byte[] synthesizeWithJCodec(byte[] imageBytes, String cameraMovement, int durationSeconds, String resolution) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("jcodec-synth-", ".mp4");
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (src == null) {
                throw new IllegalStateException("Failed to decode image bytes for video synthesis");
            }

            int targetW = 1280;
            int targetH = 720;
            if ("1080p".equalsIgnoreCase(resolution)) {
                targetW = 1920;
                targetH = 1080;
            }

            int fps = 12;
            int totalFrames = durationSeconds * fps;
            AWTSequenceEncoder encoder = AWTSequenceEncoder.createSequenceEncoder(tempFile, fps);

            String mov = (cameraMovement != null) ? cameraMovement.toUpperCase() : "DOLLY";

            for (int i = 0; i < totalFrames; i++) {
                double progress = (double) i / (double) totalFrames;
                BufferedImage frame = new BufferedImage(targetW, targetH, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = frame.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                double scale = 1.0;
                double tx = 0;
                double ty = 0;

                if ("ZOOM_IN".equals(mov) || "DOLLY".equals(mov) || "ORBIT".equals(mov)) {
                    scale = 1.0 + (progress * 0.18);
                    tx = -((scale - 1.0) * targetW) / 2.0;
                    ty = -((scale - 1.0) * targetH) / 2.0;
                } else if ("ZOOM_OUT".equals(mov)) {
                    scale = 1.18 - (progress * 0.18);
                    tx = -((scale - 1.0) * targetW) / 2.0;
                    ty = -((scale - 1.0) * targetH) / 2.0;
                } else if ("PAN_LEFT".equals(mov) || "PAN".equals(mov)) {
                    scale = 1.10;
                    tx = (progress * (targetW * 0.10)) - (targetW * 0.05);
                    ty = -((scale - 1.0) * targetH) / 2.0;
                } else if ("PAN_RIGHT".equals(mov)) {
                    scale = 1.10;
                    tx = -((scale - 1.0) * targetW) / 2.0 - (progress * (targetW * 0.05));
                    ty = -((scale - 1.0) * targetH) / 2.0;
                } else {
                    scale = 1.05 + (progress * 0.05);
                    tx = -((scale - 1.0) * targetW) / 2.0;
                    ty = -((scale - 1.0) * targetH) / 2.0;
                }

                AffineTransform at = new AffineTransform();
                at.translate(tx, ty);
                at.scale(scale * ((double) targetW / src.getWidth()), scale * ((double) targetH / src.getHeight()));

                g.drawImage(src, at, null);
                g.dispose();

                encoder.encodeImage(frame);
            }

            encoder.finish();
            log.info("JCodec pure Java encoder rendered MP4 video ({} bytes)", tempFile.length());
            return Files.readAllBytes(tempFile.toPath());
        } catch (Exception e) {
            log.error("JCodec video synthesis failed: {}", e.getMessage(), e);
            throw new RuntimeException("Video synthesis failed: " + e.getMessage(), e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
