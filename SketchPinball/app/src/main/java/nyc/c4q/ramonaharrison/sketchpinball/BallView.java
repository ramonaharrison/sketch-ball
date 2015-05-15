package nyc.c4q.ramonaharrison.sketchpinball;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;


public class BallView extends Activity implements SensorEventListener {

    CustomDrawableView mCustomDrawableView = null;
    public float xPosition, xAcceleration,xVelocity = 0.0f;
    public float yPosition, yAcceleration,yVelocity = 0.0f;
    public float xmax,ymax;
    private SensorManager sensorManager = null;
    private Paint color;
    public float frameTime = 0.666f;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

        // set view
        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
        Display display = getWindowManager().getDefaultDisplay();
        xmax = (float)display.getWidth() - 50;
        ymax = (float)display.getHeight() - 50;

        color = new Paint();
        color.setColor(getResources().getColor(android.R.color.holo_green_light));
    }

    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                //Set sensor values as acceleration
                yAcceleration = sensorEvent.values[1];
                xAcceleration = sensorEvent.values[2];
                Log.d("xy", "X Acceleration: " + xAcceleration + "     Y Acceleration: " + yAcceleration);
                updateBall();
            }
        }
    }

    private void updateBall() {


        //Calculate new speed
        xVelocity += (xAcceleration * frameTime);
        yVelocity += (yAcceleration * frameTime);

        //Calc distance travelled in that time
        float xS = (xVelocity/2)*frameTime;
        float yS = (yVelocity/2)*frameTime;

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition -= yS;

        if (xPosition > xmax) {
            xPosition = xmax;
        } else if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition > ymax) {
            yPosition = ymax;
        } else if (yPosition < 0) {
            yPosition = 0;
        }
    }

    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public class CustomDrawableView extends View
    {
        public CustomDrawableView(Context context)
        {
            super(context);
            //Resources res = getResources();
            //Drawable ball = res.getDrawable(R.drawable.ball);
            //Drawable wood = res.getDrawable(R.drawable.wood);
            //final int dstWidth = 50;
            //final int dstHeight = 50;
            //mBitmap = Bitmap.createBitmap(ball.getIntrinsicWidth(), ball.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            //mWood = Bitmap.createBitmap(wood.getIntrinsicWidth(), wood.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            //mBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
            //mWood = BitmapFactory.decodeResource(getResources(), R.drawable.wood);

        }

        protected void onDraw(Canvas canvas)
        {
            //canvas.draw(bitmap, xPosition, yPosition, null);
            canvas.drawCircle(xPosition, yPosition, 25, color);
            invalidate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}