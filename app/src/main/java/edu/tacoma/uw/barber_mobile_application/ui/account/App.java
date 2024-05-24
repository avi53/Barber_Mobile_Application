package edu.tacoma.uw.barber_mobile_application.ui.account;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //TODO: FIX RETURN URL TO RETURN TO THIS APPLICATION
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "AZmXDwN0mhrXXUIdkNXnBo8D_5iTTaeg1Yyv6t07F54N2qDF_W0-Wdu5m7PDIwwuDwpihJqFq2ZCbF_Y",
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                "nativexo://paypalpay"
        ));
    }
}
