package jjmomanyi.extremedev.garbagesmart;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;


public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignupActivity";



    TextView _loginLink;
    Button _signupButton;
    EditText _nameText,_emailText,_passwordText,pass2,location,phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _loginLink=(TextView)findViewById(R.id.link_login);
        _signupButton=(Button)findViewById(R.id.btn_signup);
        _nameText=(EditText) findViewById(R.id.input_name);
        _emailText=(EditText) findViewById(R.id.input_email);
        _passwordText=(EditText) findViewById(R.id.input_password);
        pass2=(EditText)findViewById(R.id.password2);
        location=(EditText)findViewById(R.id.location);
        phone=(EditText)findViewById(R.id.phone_number);

        _loginLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


    }


    public void signup()
    {
        Log.d(TAG, "Signup");

        if (!validate())
        {
            onSignupFailed();
            return;
        }
        else {
            //_signupButton.setEnabled(false);
            String name = _nameText.getText().toString();
            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();
            //int permissionCheck = ContextCompat.checkSelfPermission(this,
            // Manifest.permission.INTERNET);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // Toast.makeText(SignUp.this, "Permission denied. You can't be registered", Toast.LENGTH_SHORT).show();
                    register(_nameText.getText().toString(), _emailText.getText().toString(), phone.getText().toString(), location.getText().toString(), _passwordText.getText().toString());
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SignUp.this, "Permission denied. You can't be registered", Toast.LENGTH_SHORT).show();
                    //finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void register(final String name,final String email,final String phone,final String location,final String password)
    {
        class AddEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(SignUp.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("email",email);
                params.put("phone",phone);
                params.put("location",location);
                params.put("password",password);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;

            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                Toast.makeText(SignUp.this,s, Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(s);
                            JSONArray array = json.getJSONArray("result");
                            JSONObject c = array.getJSONObject(0);
                            String succes =c.getString("succes");
                            if (succes.equals("1"))
                            {
                                onSignupSuccess();
                            }
                            else if (succes.equals("0"))
                            {
                                onSignupFailed();
                            }


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

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(getBaseContext(), "Account creation Succesful, Please log in", Toast.LENGTH_LONG).show();
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Account creation failed", Toast.LENGTH_SHORT).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String phoneno=phone.getText().toString();

        if(phoneno.length()!=10 || phoneno.isEmpty())
        {
            phone.setError("Enter a valid phone number");
        }
        else
        {
            phone.setError(null);
        }
        if (location.getText().toString().isEmpty())
        {
            location.setError("Enter a valid location name");
        }
        else
        {
            location.setError(null);
        }

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() )
        {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else
        {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}
