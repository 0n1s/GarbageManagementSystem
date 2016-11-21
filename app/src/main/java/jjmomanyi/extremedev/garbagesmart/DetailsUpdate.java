package jjmomanyi.extremedev.garbagesmart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailsUpdate extends AppCompatActivity {
    String email;
    Button btn;
    TextView tt;
    EditText name,phone,password,password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        onstart(email);
        name=(EditText)findViewById(R.id.editText2);
        tt=(TextView)findViewById (R.id.textView);
        tt.setText(email);
        phone=(EditText)findViewById(R.id.editText3);
        password=(EditText)findViewById(R.id.editText4);
        password2=(EditText)findViewById(R.id.editText5);
        btn=(Button)findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwor=password.getText().toString();
                String nameer=name.getText().toString();
                String phoe=phone.getText().toString();

                if (verify()==true)
                {
                    updater(passwor,nameer,phoe,email);
                }


            }
        });


    }
    public  void updater(final String pass,final String name,final String phone,final String email)
    {
        class AddEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                progressDialog = new ProgressDialog(DetailsUpdate.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating your details...");
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("pass",pass);
                params.put("name",name);
                params.put("phone",phone);
                params.put("email",email);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.updatethem, params);
                return res;

            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                Toast.makeText(DetailsUpdate.this, s, Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(s);
                            JSONArray array = json.getJSONArray("result");
                            JSONObject c = array.getJSONObject(0);
                            String succes =c.getString("succes");
                            if(succes.equals("1"))
                            {
                                Toast.makeText(DetailsUpdate.this, "Details updated succesifully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(DetailsUpdate.this, "Failed to update please try again", Toast.LENGTH_SHORT).show();
                            }
                     } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }


                    }
                });

            }
        }
        AddEmployee ae = new AddEmployee();
        ae.execute();

    }
    public boolean verify()
    {
        boolean status=true;

        if(password.getText().toString().isEmpty())
        {
            password.setError("Please enter the password");
            status=false;
        }
        else
        {
            password.setError(null);
        }
        if (password.getText().toString().equals(password2.getText().toString()))
        {
            password.setError(null);
            password2.setError(null);
        }
        else
        {
            password.setError("passwords do not match");
            password2.setError("passwords do not match");
            status=false;
        }

        if (phone.getText().toString().isEmpty())
        {
            phone.setError("Please enter your phone number");
            status=false;

        }
        else
        {
            phone.setError(null);
        }
        if(name.getText().toString().isEmpty())
        {
            name.setError("Please enter your name");
            status=false;

        }
        else
        {
            name.setError(null);

        }
        return status;
    }


    public void onstart(final String email)
    {
        class AddEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(DetailsUpdate.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Fetching your details...");
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("email",email);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.fetchdetails, params);
                return res;
            }
            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(s);
                            JSONArray array = json.getJSONArray("result");
                            JSONObject c = array.getJSONObject(0);
                            String passwordfetched =c.getString("passworc");
                            String namefetched =c.getString("name");
                            String phonefetched =c.getString("phone");
                            name.setText(namefetched);
                            phone.setText(phonefetched);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        }
        AddEmployee ae = new AddEmployee();
        ae.execute();



    }



}
