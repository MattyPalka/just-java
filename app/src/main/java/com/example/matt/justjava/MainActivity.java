package com.example.matt.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int numberOfCoffees = 0;
    boolean hasWhippedCream = false;
    boolean hasChocolateTopping = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         *This code is to ensure that total message is displayed with correct price at app start
         */
        displayMessage(getString(R.string.order_summary_body, calculatePrice()));
    }

    /**
     *creates summary of the order
     *
     * @return text summary of the order
     */
    private String createOrderSummary(String customer){
        String orderSummary = getString(R.string.order_summary_name, customer) +
                            "\n" + getString(R.string.add_whipped_cream) + hasWhippedCream +
                            "\n" + getString(R.string.add_chocolate) + hasChocolateTopping +
                            "\n" + getString(R.string.quantity_summary, numberOfCoffees) +
                            "\n" + getString(R.string.order_summary_body, calculatePrice()) +
                            "\n" + getString(R.string.thank_you);
        return orderSummary;
    }

    /**
     * This method is called when Whipped Cream checkbox is pressed and displays the  coffee price with topping
     *
     */
    public void checkedWhippedCream(View view){
        hasWhippedCream = ((CheckBox) view).isChecked();
        displayMessage(getString(R.string.order_summary_body, calculatePrice()));
    }

    /**
     * This method is called when chocolate topping checkbox is pressed and adds the price of topping
     *
     */
    public void checkedChocolateTopping(View view){
        hasChocolateTopping = ((CheckBox) view).isChecked();
        displayMessage(getString(R.string.order_summary_body, calculatePrice()));
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        if (numberOfCoffees < 1){
            Toast.makeText(this, R.string.minimal_order, Toast.LENGTH_SHORT).show();
            return;
        }

        EditText nameInput = (EditText) findViewById(R.id.name_text_input);
        String customerName = nameInput.getText().toString();

        composeEmail(new String[]{"justjava@gmail.com"}, getString(R.string.email_subject, customerName), createOrderSummary(customerName));

        //displayMessage(createOrderSummary(calculatePrice(), customerName));

    }

    /**
     * composes the email to send with the coffee order
     * @param adressess receive's email adress
     * @param subject  subject of the email
     * @param emailBody email text with the actual order
     */
    public void composeEmail(String[] adressess, String subject, String emailBody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, adressess);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



    /**
     * Calculates the price of the order.
     */
    private int calculatePrice() {
        int coffeePrice = 5;
        int ToppingPrice = 1;
        if (hasChocolateTopping && hasWhippedCream){
            return numberOfCoffees * (coffeePrice + (ToppingPrice * 2));
        } else if (hasWhippedCream || hasChocolateTopping){
            return numberOfCoffees * (coffeePrice + ToppingPrice);
        } else {
            return numberOfCoffees * coffeePrice;
        }
    }

    /**
     *This method is called when plus button is clicked
     */
    public void increment(View view){
        if (numberOfCoffees >= 100){
            numberOfCoffees = 100;
            Toast.makeText(this,R.string.maximal_order, Toast.LENGTH_SHORT).show();
        } else {
            numberOfCoffees++;
        }
        displayQuantity(numberOfCoffees);
        displayMessage(getString(R.string.order_summary_body, calculatePrice()));
    }

    /**
     * This method is called when minus button is clicked
     */
    public void decrement(View view){
        if (numberOfCoffees <= 0){
            numberOfCoffees = 0;
        } else {
            numberOfCoffees--;
        }
        displayQuantity(numberOfCoffees);
        displayMessage(getString(R.string.order_summary_body, calculatePrice()));
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
}