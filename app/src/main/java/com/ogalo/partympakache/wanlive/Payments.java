package com.ogalo.partympakache.wanlive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class Payments extends AppCompatActivity {
    Double clickcount=0.0;

    Double tickets=1.0;
    private Button change;
    private ImageButton add;
    private Double buyer;
    private ImageButton minus;
    private TextView prices;

    private TextView ticketsi;
    private TextView ticcounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);






Button payment=(Button)findViewById(R.id.payment);


//
//payment.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//
//
//        Toast.makeText(Payments.this, "Shane haas just pressed this button", Toast.LENGTH_SHORT).show();
//
//
//
//
//    }
//});






        Intent i = getIntent();
//        pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
//        success = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        String pricer= i.getStringExtra("price");
        buyer=Double.parseDouble(pricer);
        add=(ImageButton) findViewById(R.id.add);
        minus=(ImageButton) findViewById(R.id.minus);
        change=(Button)findViewById(R.id.payment);
        prices=(TextView)findViewById(R.id.prices);
        ticketsi=(TextView)findViewById(R.id.tickets);
        ticcounter=(TextView)findViewById(R.id.tic_counter);

        change.setText("Ksh. "+buyer.toString());
        ticketsi.setText(tickets.toString());
        prices.setText(buyer.toString());

        setTics(1.0);


        setNum(buyer);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               tickets=++tickets;

                clickcount=clickcount+buyer;
                if(clickcount==1.0)
                {
                    //first time clicked to do this
                    Toast.makeText(getApplicationContext(),"Raila is too old, he should just quit politics!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ticcounter.setText("Tickets");
                    //check how many times clicked and so on
//                    Toast.makeText(getApplicationContext(),"Button clicked count is"+clickcount, Toast.LENGTH_LONG).show();
                    ticketsi.setText(tickets.toString());
                    change.setText(clickcount.toString());
                    prices.setText(clickcount.toString());

                }





            }
        });


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tickets>=1.0)
                {

                    ticcounter.setText("Tickets");



                    tickets=tickets-1.0;

                    clickcount=clickcount-buyer;


                    ticketsi.setText(tickets.toString());
                    change.setText(clickcount.toString());
                    prices.setText(clickcount.toString());







                }
                else
                {
                    new SweetAlertDialog(Payments.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Error?")
                            .setContentText("Please select at least one ticket")
                            .setConfirmText("Okay")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }





            }
        });



        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Double burito=Double.parseDouble(change.getText().toString());

                if(clickcount.equals(0.0))
                {

                    new SweetAlertDialog(Payments.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Invalid")
                            .setContentText("Buy at least one ticket")
                            .setConfirmText("Okay")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }

                else {
                    //WIll implement alertDialog where users can choose paypal or mpesa

//                            success.setTitleText("Error...");
//                            success.setContentText("Select MPESA or PayPal");
//                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.dismissWithAnimation();
//                                    sDialog.dismiss();
//                                    sDialog.cancel();
//                                }
//                            });
//                            success.show();
//



                    new SweetAlertDialog(Payments.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Select Mpesa or Paypal")
                            .setConfirmText("Okay")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();


                }




            }
        });




    }

    public void setNum(Double num)
    {
        this.clickcount=num;

    }
    public void setTics(Double tics)
    {
        this.tickets=tickets;

    }
}
