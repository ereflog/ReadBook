package itts.readbook;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.Toast;

import com.huawei.hms.ads.AdParam;
//import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

public class MainActivity extends Activity {

    public static final int DEFAULT_VIEW = 0x22;

    private static final int REQUEST_CODE_SCAN = 0X01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this);

        // Obtain TopView configured in the XML layout file.
        BannerView topBannerView = findViewById(R.id.hw_banner_view);
        AdParam adParam = new AdParam.Builder().build();
        topBannerView.loadAd(adParam);

        /*// Add BannerView through coding.
        BannerView topBannerView = new BannerView(this);
        topBannerView.setAdId("testw6vs28auh3");
        topBannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        topBannerView.loadAd(adParam);

        LinearLayout rootView = findViewById(R.id.root_view);
        rootView.addView(topBannerView);*/
    }

    public void newViewBtnClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    DEFAULT_VIEW);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions == null ||
                grantResults == null ||
                grantResults.length < 2 ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (requestCode == DEFAULT_VIEW) {
            //start Scankit Activity for scanning barcode
            ScanUtil.startScan(MainActivity.this, REQUEST_CODE_SCAN, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.EAN13_SCAN_TYPE).create());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //receive result after activity finished scanning
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        // Obtain the return value of HmsScan from the value returned by the onActivityResult method
        // by using ScanUtil.RESULT as the key value.
        if (requestCode == REQUEST_CODE_SCAN) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj != null) {
                if (!TextUtils.isEmpty(obj.getOriginalValue())) {
                    //Toast.makeText(this, obj.getOriginalValue(), Toast.LENGTH_SHORT).show();
                    String hasil = obj.getOriginalValue();
                    Intent intent1 = new Intent(getApplicationContext(), SecondActivity.class);
                    intent1.putExtra("isbn_scan", hasil);
                    //Intent intent2 = new Intent(this, SecondActivity.class);
                    startActivity(intent1);
                    //startActivity(intent2);
                }
            }
        }
    }
}
