package com.example.cameradraw;
import android.hardware.Camera;
import android.util.Log;
import java.io.IOException;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Surface;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ImageFormat;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView
    implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = "CameraPreview";

    private Camera mCamera;
    private SurfaceView mFiltered;
    private byte[] mPreviewData;

    // Link to native Halide code
    static {
        //System.loadLibrary("native");
    }
    private static native void processFrame(byte[] src, int w, int h, Surface dst);

    public CameraPreview(Context context, SurfaceView filtered) {
        super(context);
        mFiltered = filtered;
        mFiltered.getHolder().setFormat(ImageFormat.YV12);
        mPreviewData = null;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        getHolder().addCallback(this);
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        if (camera != mCamera) {
            Log.d(TAG, "Unknown Camera!");
            return;
        }
        if (mFiltered.getHolder().getSurface().isValid()) {
            Camera.Size s = camera.getParameters().getPreviewSize();
            processFrame(data, s.width, s.height, mFiltered.getHolder().getSurface());
        } else {
            Log.d(TAG, "Invalid Surface!");
        }

        // re-enqueue this buffer
        camera.addCallbackBuffer(data);
    }

    private void startPreview(SurfaceHolder holder) {
        if (mCamera == null) {
            return;
        }
        try {
            configureCamera();
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private void stopPreview() {
        if (mCamera == null) {
            return;
        }
        try {
              mCamera.stopPreview();
        } catch (Exception e){
              // ignore: tried to stop a non-existent preview
              Log.d(TAG, "tried to stop a non-existent preview");
        }
    }
    private void configureCamera() {
        Camera.Parameters p = mCamera.getParameters();
        Camera.Size s = p.getPreviewSize();
        Log.d(TAG, "Camera Preview Size: " + s.width + "x" + s.height);
        p.setPreviewFormat(ImageFormat.YV12);
        if (mPreviewData == null) {
            int stride = ((s.width + 15) / 16) * 16;
            int y_size = stride * s.height;
            int c_stride = ((stride/2 + 15) / 16) * 16;
            int c_size = c_stride * s.height/2;
            int size = y_size + c_size * 2;
            mPreviewData = new byte[size];
        }
        mCamera.addCallbackBuffer(mPreviewData);
        mCamera.setParameters(p);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        startPreview(holder);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        stopPreview();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d(TAG, "surfaceChanged");
        stopPreview();
        configureCamera();
        startPreview(holder);
    }

    public void setCamera(Camera c) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        mCamera = c;
        if (mCamera != null) {
            startPreview(getHolder());
        }
    }
}
//
//import java.util.List;
//
//import android.content.Context;
//import android.hardware.Camera;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
//
//	private static final String TAG = "CameraPreview";
//
//	private Context mContext;
//	private SurfaceHolder mHolder;
//	private Camera mCamera;
//	private List<Camera.Size> mSupportedPreviewSizes;
//	private Camera.Size mPreviewSize;
//
//	public CameraPreview(Context context, Camera camera) {
//		super(context);
//		mContext = context;
//		mCamera = camera;
//
//		// supported preview sizes
//		mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
//		for (Camera.Size str : mSupportedPreviewSizes)
//			Log.e(TAG, str.width + "/" + str.height);
//
//		// Install a SurfaceHolder.Callback so we get notified when the
//		// underlying surface is created and destroyed.
//		mHolder = getHolder();
//		mHolder.addCallback(this);
//		// deprecated setting, but required on Android versions prior to 3.0
//		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//	}
//
//	public void surfaceCreated(SurfaceHolder holder) {
//		// empty. surfaceChanged will take care of stuff
//	}
//
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		// empty. Take care of releasing the Camera preview in your activity.
//	}
//
//	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//		Log.e(TAG, "surfaceChanged => w=" + w + ", h=" + h);
//		// If your preview can change or rotate, take care of those events here.
//		// Make sure to stop the preview before resizing or reformatting it.
//		if (mHolder.getSurface() == null) {
//			// preview surface does not exist
//			return;
//		}
//
//		// stop preview before making changes
//		try {
//			mCamera.stopPreview();
//		} catch (Exception e) {
//			// ignore: tried to stop a non-existent preview
//		}
//
//		// set preview size and make any resize, rotate or reformatting changes
//		// here
//		// start preview with new settings
//		try {
//			Camera.Parameters parameters = mCamera.getParameters();
//			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//			mCamera.setParameters(parameters);
//			mCamera.setDisplayOrientation(90);
//			mCamera.setPreviewDisplay(mHolder);
//			mCamera.startPreview();
//
//		} catch (Exception e) {
//			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
//		}
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//
//		if (mSupportedPreviewSizes != null) {
//			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
//		}
//
//		float ratio;
//		if (mPreviewSize.height >= mPreviewSize.width)
//			ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
//		else
//			ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;
//
//		// One of these methods should be used, second method squishes preview
//		// slightly
//		setMeasuredDimension(width, (int) (width * ratio));
//		// setMeasuredDimension((int) (width * ratio), height);
//	}
//
//	private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
//		final double ASPECT_TOLERANCE = 0.1;
//		double targetRatio = (double) h / w;
//
//		if (sizes == null)
//			return null;
//
//		Camera.Size optimalSize = null;
//		double minDiff = Double.MAX_VALUE;
//
//		int targetHeight = h;
//
//		for (Camera.Size size : sizes) {
//			double ratio = (double) size.height / size.width;
//			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
//				continue;
//
//			if (Math.abs(size.height - targetHeight) < minDiff) {
//				optimalSize = size;
//				minDiff = Math.abs(size.height - targetHeight);
//			}
//		}
//
//		if (optimalSize == null) {
//			minDiff = Double.MAX_VALUE;
//			for (Camera.Size size : sizes) {
//				if (Math.abs(size.height - targetHeight) < minDiff) {
//					optimalSize = size;
//					minDiff = Math.abs(size.height - targetHeight);
//				}
//			}
//		}
//
//		return optimalSize;
//	}
//}
