package jjmomanyi.extremedev.garbagesmart;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Signin extends AppCompatActivity  {

    EditText _emailText,_passwordText;
    Button _loginButton;
    CheckBox checkBox;
    public static final String MyPREFERENCES = "MyPrefs";
    TextView _signupLink;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        checkBox= (CheckBox)findViewById(R.id.checkBox);
        _emailText=(EditText)findViewById(R.id.input_email);
        _passwordText=(EditText)findViewById(R.id.input_password);
        _loginButton=(Button)findViewById(R.id.btn_login);
        _signupLink=(TextView)findViewById(R.id.link_signup);

        _signupLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(Signin.this, SignUp.class);
                        startActivity(intent);
                    }
                }
        );


        String email= sharedpreferences.getString("email", "null");
        String password=sharedpreferences.getString("password","null");

        if(email.equals("null")||password.equals("null"))
        {
           // Toast.makeText(Signin.this, "I Just Logged out", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent =new Intent(Signin.this,Garbage_Manager.class);
            intent.putExtra("email",email);
            startActivity(intent);
            finish();

        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });}


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        loginnow(email,password);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {


            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }



    public void onLoginFailed() {


        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
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


    public void loginnow (final String email,final String password)

    {
        class AddEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(Signin.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.LOGIn, params);
                return res;

            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                login_verify(s,email,password);
                Toast.makeText(Signin.this, s, Toast.LENGTH_SHORT).show();

            }
        }
        AddEmployee ae = new AddEmployee();
        ae.execute();


    }

    public void login_verify(String s,String email,String password)
    {
        try {
            JSONObject json = new JSONObject(s);
            JSONArray array = json.getJSONArray("result");
            JSONObject c = array.getJSONObject(0);
            String succes =c.getString("succes");
            if (succes.equals("1"))
            {

                if (checkBox.isChecked())
                {
                    rememberme(email, password);
                }
                else
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("email", "null");
                    editor.putString("password", "null");
                    editor.commit();

                }

                Intent intent=new Intent(Signin.this,Garbage_Manager.class);
                intent.putExtra("email",email);
                startActivity(intent);
                finish();


            }
            else if (succes.equals("0"))
            {
                Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            Toast.makeText(Signin.this, e.toString(), Toast.LENGTH_SHORT).show(); e.printStackTrace();
        }


    }

    public void rememberme(final String email,final String password)
    {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();

    }





}

