package info.leonardofontana.modemme.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by tetsu on 19/10/2015.
 */
public class VolleyRequestQueue {
    private static VolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    protected final int IMAGE_WIDTH = 800;
    protected final int MAX_HEIGHT = 450;

    private VolleyRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(
                                url,
                                filterImage(bitmap)
                        );
                    }

                    private Bitmap filterImage(Bitmap in){
                        //todo: creare filtro per resize immagini
                        /*Matrix transform = new Matrix();
                        float scala = in.getWidth()/IMAGE_WIDTH;
                        transform.preScale(scala,scala);
                        transform.postTranslate(
                                0f,
                                ((scala - 1) * in.getHeight()) / 2f
                        );
                        Paint paint = new Paint();
                        paint.setFilterBitmap(true);
                        Bitmap out = Bitmap.createBitmap(
                                IMAGE_WIDTH,
                                (int) (in.getHeight()*scala),
                                Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(out);
                        canvas.drawBitmap(in, transform, paint);
                        return (out.getHeight() > MAX_HEIGHT) ?
                                //todo: Settare i valori per centrare l'immagine
                                Bitmap.createBitmap(out, 0, 0, IMAGE_WIDTH, MAX_HEIGHT):
                                out;
                        */
                        return in;
                    }
                });
    }



    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }



}
