package com.gadgetmart.ui.payment;

import android.content.Context;

import com.gadgetmart.R;
import com.gadgetmart.ui.checkout.CheckoutActivity;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

public class PaymentActivity {

    CheckoutActivity context;
    public PaymentActivity(CheckoutActivity context) {
        this.context = context;
    }


    public void setUpPayUMoney(PayUmoneySdkInitializer.PaymentParam param,int style, boolean isScreenOverride){
        PayUmoneyFlowManager.startPayUMoneyFlow(param,context, style,isScreenOverride);
    }
}
