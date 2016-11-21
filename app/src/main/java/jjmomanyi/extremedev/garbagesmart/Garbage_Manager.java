package jjmomanyi.extremedev.garbagesmart;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Garbage_Manager extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener,LocationListener ,TimePickerDialog.OnTimeSetListener{
    SharedPreferences sharedpreferences;
    EditText datepicker,location2,timepicker;
    Button btn2;
    ImageView imageView2;
    String _Location;
TextView t10,t11,t12,t13,t14,t15,t17,t21;
    int year;
    String email;
    String time;

    String adress="http://192.168.43.184/Garbage/lipanampesa_recent/home.php";
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage__manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(Signin.MyPREFERENCES, Context.MODE_PRIVATE);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder a = new AlertDialog.Builder(Garbage_Manager.this);
                a.setMessage("Are you sure you want to log out?");
                a.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Garbage_Manager.this, Signin.class);
                        startActivity(intent);
                        sharedpreferences.edit().putString("email", "null");
                        sharedpreferences.edit().putString("password", "null").commit();
                        finish();
                    }
                });
                a.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                a.show();


            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        Intent intent = getIntent();
         email = intent.getStringExtra("email");
        Toast.makeText(Garbage_Manager.this,"Welcome "+ email, Toast.LENGTH_SHORT).show();
        onstart(email);
        t10=(TextView)findViewById(R.id.textView10);
        t11=(TextView)findViewById(R.id.onrcharge);
        t12=(TextView)findViewById(R.id.textView12);
        t13=(TextView)findViewById(R.id.textView13);
        timepicker=(EditText)findViewById(R.id.editText) ;
        t14=(TextView)findViewById(R.id.textView14);
        t15=(TextView)findViewById(R.id.textView15);
        t17=(TextView)findViewById(R.id.textView17);
        location2=(EditText)findViewById(R.id.textView21);
        btn2=(Button)findViewById(R.id.button2);
        t21=(TextView)findViewById(R.id.textView21);
        //String email="http://192.168.43.184/stepsfocharity/lipanampesa_recent/home.php";


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Garbage_Manager.this, "hey you", Toast.LENGTH_SHORT).show();



                AlertDialog.Builder a = new AlertDialog.Builder(Garbage_Manager.this);
                a.setMessage("Are you sure you want to pay "+t17.getText()+"? \nNote that the amount will be deducted from your MPESA account");
                a.setCancelable(true);
                a.setPositiveButton("Pay with MPESA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mpesa(t17.getText().toString(),email);
                    }
                });

                a.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                a.show();






            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datepicker.getText().toString().isEmpty())
                {
                    Toast.makeText(Garbage_Manager.this, "Choose date", Toast.LENGTH_SHORT).show();

                    if(t21.getText().toString().isEmpty())
                    {
                        t21.setError("location is missing");
                    }
                }
                else if(t21.getText().toString().isEmpty())
                {
                    t21.setError("location is missing");
                    if(datepicker.getText().toString().isEmpty())
                    {
                        Toast.makeText(Garbage_Manager.this, "Choose date", Toast.LENGTH_SHORT).show();

                    }
                }

                else
                {

                    String date = datepicker.getText().toString();
                    String location=t21.getText().toString();
                    String time=timepicker.getText().toString();
                    requestSender(date, location,time);
                }

            }
        });

        datepicker = (EditText) findViewById(R.id.button);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog=new DatePickerDialog( Garbage_Manager.this, Garbage_Manager.this,  2016,9,4 );
                dialog.show();


            }


        });


        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timer =new TimePickerDialog(Garbage_Manager.this,Garbage_Manager.this,12,12,true);
                timer.show();
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }


    public void requestSender(final String date, final String Location,final String time)
    {
        class AddEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(Garbage_Manager.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Fetching your details...");
                progressDialog.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("date",date);
                params.put("time",time);
                params.put("location",Location);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.requestsubmitter, params);
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
                            int succes =c.getInt("succes");
                           if (succes==1)
                           {
                               Toast.makeText(Garbage_Manager.this, "Submission successful", Toast.LENGTH_SHORT).show();
                               onstart(email);


                           }
                            else
                           {
                               Toast.makeText(Garbage_Manager.this, "Error Please try again", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.garbage__manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.uc) {
            Intent intent = new Intent(Garbage_Manager.this,DetailsUpdate.class);
            intent.putExtra("email",email);
            startActivity(intent);
        } else if (id == R.id.logout)
        {

           // AlertDialog alertDialog= new AlertDialog.Builder(this);
            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setCancelable(true);
            build.setTitle("Are you sure you want to logout?");
            build.setPositiveButton("logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("email", "null");
                    editor.putString("password", "null");
                    editor.commit();

                    Intent intent=new Intent(Garbage_Manager.this, Signin.class);
                    startActivity(intent);
                    Garbage_Manager.this.finish();
                }
            });
            build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            build.show();




        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int DayOfMonth) {

        datepicker.setText(year+"/"+monthOfYear+"/"+DayOfMonth);
    }

    @Override
    public void onLocationChanged(Location location) {

       Double latitude= (location.getLatitude());
        Double longtitude= location.getLongitude();


        Toast.makeText(Garbage_Manager.this, "Location updated", Toast.LENGTH_SHORT).show();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longtitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                _Location = listAddresses.get(0).getAddressLine(0);
                location2.setText(_Location);
            }






        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

        Toast.makeText(Garbage_Manager.this, "Please enable your GPS", Toast.LENGTH_SHORT).show();


    }

    public void onstart(final String email)
    {
        class AddEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(Garbage_Manager.this,
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
                String res = rh.sendPostRequest(Config.fetchone, params);
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
                            String chargemonthly =c.getString("chargemonthly");
                            String chargeon_request =c.getString("chargeon_request");
                            String chargecarried =c.getString("chargecarried");
                            String standard_chargesm =c.getString("standard_chargesm");
                            String standard_charges_onrequest =c.getString("standard_charges_onrequest");
                            String pickupdate =c.getString("pickupdate");

                            t10.setText(standard_chargesm);
                            t11.setText(standard_charges_onrequest);
                            t12.setText(pickupdate);
                            t13.setText(chargecarried);
                            t14.setText(chargemonthly);
                            t15.setText(chargeon_request);
                            Double cc=Double.parseDouble(chargecarried);
                            Double cm=Double.parseDouble(chargemonthly);
                            Double cr=Double.parseDouble(chargeon_request);
                            Double total=cc+cm+cr;

                            t17.setText(Double.toString(total));




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

    public  void mpesa(final String amount,final String ID)
    {



        class AddEmployee extends AsyncTask<Void,Void,String> {
            SweetAlertDialog pDialog = new SweetAlertDialog(Garbage_Manager.this, SweetAlertDialog.PROGRESS_TYPE);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Making your Payement");
                pDialog.setContentText("Please enter your MPESA PIN when prompted");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("ammount", amount );
                params.put("ID", ID );
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(adress, params);
                return res;

            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                Toast.makeText(Garbage_Manager.this, s, Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }
        AddEmployee ae = new AddEmployee();
        ae.execute();



    }






    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        timepicker.setText(i+":"+i1);
    }
}