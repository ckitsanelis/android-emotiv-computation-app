package gr.asteras.thinkflash.Features;

import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;


public class FlashAndSound {

    private Boolean flash;
    private Boolean music;
    private String cameraId;
    private CameraManager camManager;
    private Camera camera;
    private Camera.Parameters params;
    private MediaPlayer mp;

    public FlashAndSound(MediaPlayer mediaplayer) {
        this.mp = mediaplayer;
        initCamera();
        flash = false;
        music = false;
    }

    public FlashAndSound(MediaPlayer mediaplayer,CameraManager cameramanager) {
        mp = mediaplayer;
        camManager = cameramanager;
        initCamera();
        flash = false;
        music = false;
    }

    public void startMusic() {
        if(!music) {
            mp.start();
            music = true;
        }
    }

    public void startFlash() {
        if(!flash) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (camera == null || params == null)
                    initCamera();
                try {
                    params = camera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(params);
                    camera.startPreview();
                } catch (Exception e) {
                    Log.i("Start Error", e.getMessage());
                }

            } else {
                try {
                    camManager.setTorchMode(cameraId, true);
                } catch (CameraAccessException e) {
                    Log.i("Camera access error", e.getMessage());
                }
            }
            flash = true;
        }
    }

    public void startBoth() {
        startFlash();
        startMusic();
    }

    public void stop(Boolean musicStop, Boolean flashStop) {
        if(musicStop && music) {
            mp.pause();
            mp.seekTo(0);
            music = false;
        }
        if(flashStop && flash) {
            closeCamera();
            flash = false;
        }
    }

    private void initCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.i("Camera open error", e.getMessage());
            }
        }
        else {
            try {
                cameraId = camManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                Log.i("Camera access error", e.getMessage());
            }
        }
    }

    public void closeCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if(camera == null || params == null)
                return;
            try {
                params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {
                Log.i("Stop error", e.getMessage());
            }
        }
        else {
            try {
                camManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                Log.i("Camera access error", e.getMessage());
            }
        }
    }
}