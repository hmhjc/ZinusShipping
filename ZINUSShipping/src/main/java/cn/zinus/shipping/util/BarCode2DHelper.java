package cn.zinus.shipping.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zebra.adc.decoder.Barcode2DWithSoft;


/**
 * Developer:Spring
 * DataTime :2017/10/23 11:38
 * Main Change:
 */

public class BarCode2DHelper extends Activity {

    private Context _context;
    public Barcode2DWithSoft mReader;
    public BarCode2DHelper(Context context)
    {
        _context = context;
    }

    public void iniBarCode2D()
    {
        DisableBarCode2D();
        try {
            mReader = Barcode2DWithSoft.getInstance();
        } catch (Exception ex) {

            Toast.makeText(_context, ex.getMessage(),
                    Toast.LENGTH_SHORT).show();

            return;
        }

        if (mReader != null) {
            new InitTask().execute();
        }
    }

    public void DisableBarCode2D()
    {
        if (mReader != null) {
            mReader.close();
        }
    }

    public Barcode2DWithSoft.ScanCallback mScanCallback = new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] data) {

//				Log.i("ErDSoftScanFragment", "onScanComplete() i=" + i);

            if (length < 1) {

//					editText1.append(getString(R.string.yid_msg_scan_fail) + "\n");
//					Toast.makeText(_context, "Read fail",Toast.LENGTH_SHORT).show();
                return;
            }

            try
            {
                String barCode = new String(data);
                View rootview =((Activity)_context).getCurrentFocus();
                View v = rootview.findFocus();
                EditText eidt = (EditText)v;
                eidt.setText(barCode);
                Log.e("barCode",barCode);
                eidt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                eidt.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            }
            catch(Exception e)
            {

            }




        }
    };

    /**
     * �豸�ϵ��첽��
     *
     * @author liuruifeng
     */
    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        protected Boolean doInBackground(String... params) {


            boolean result = false;

            if (mReader != null) {
                result = mReader.open(_context);

                if (result) {
                    mReader.setParameter(324, 1);
                    mReader.setParameter(300, 0); // Snapshot Aiming
                    mReader.setParameter(361, 0); // Image Capture Illumination
                }
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (!result) {



                Toast.makeText(_context, "init fail",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (mReader != null) {

                    mReader.setScanCallback(mScanCallback);
                }
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new ProgressDialog(_context);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        DisableBarCode2D();
    }



}
