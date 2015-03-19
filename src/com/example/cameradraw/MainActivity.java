package com.example.cameradraw;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;


public class MainActivity extends Activity implements SurfaceHolder.Callback{

	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	private Camera mCamera;
	private boolean isPreviewRunning;
	
//	private Camera camera;
//    private CameraPreview preview;
//    private SurfaceView filtered;
	
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.d("MainActivity", "Could not open camera");
        }
        return c;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
       
     // Create a canvas for drawing stuff on
//        filtered = new SurfaceView(this);
//
//        // Create our Preview view and set it as the content of our activity.
//        preview = new CameraPreview(this, filtered);
//
//        FrameLayout layout = (FrameLayout) findViewById(R.id.camera_preview);
//        layout.addView(preview);
//        layout.addView(filtered);
        //filtered.setZOrderOnTop(true);

    }
    
//    @Override
//    public void onResume() {
//        super.onResume();
//        camera = getCameraInstance();
//        preview.setCamera(camera);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (camera != null) {
//            preview.setCamera(null);
//            camera.release();
//            camera = null;
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
		
        
//        if (isPreviewRunning)
//        {
//            mCamera.stopPreview();
//        }
//
//        Parameters parameters = mCamera.getParameters();
//        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//
//        if(display.getRotation() == Surface.ROTATION_0)
//        {
//            parameters.setPreviewSize(height, width);                           
//            mCamera.setDisplayOrientation(90);
//        }
//
//        if(display.getRotation() == Surface.ROTATION_90)
//        {
//            parameters.setPreviewSize(width, height);                           
//        }
//
//        if(display.getRotation() == Surface.ROTATION_180)
//        {
//            parameters.setPreviewSize(height, width);               
//        }
//
//        if(display.getRotation() == Surface.ROTATION_270)
//        {
//            parameters.setPreviewSize(width, height);
//            mCamera.setDisplayOrientation(180);
//        }
//
//        mCamera.setParameters(parameters);
//        previewCamera();  
        
	}
	
	public void previewCamera()
	{        
	    try 
	    {           
	        mCamera.setPreviewDisplay(surfaceHolder);          
	        mCamera.startPreview();
	        isPreviewRunning = true;
	    }
	    catch(Exception e)
	    {
	        Log.d("MainActivity", "Cannot start preview", e);    
	    }
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		 try {
		 mCamera = Camera.open();
		 mCamera.setDisplayOrientation(90);  
		
			mCamera.setPreviewDisplay(surfaceHolder);
			  mCamera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}          
	   
	        
		// TODO Auto-generated method stub
//		camera = Camera.open();
//        if (camera != null) {
//        try {
//        camera.setPreviewDisplay(surfaceHolder);
//        camera.startPreview();
//        } catch (Exception e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//        }
//        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
