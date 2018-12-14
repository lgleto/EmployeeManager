package ui.dona.winwel.com.employee;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EmployeeDetailActivity extends AppCompatActivity {

    public static final String EMPLOYEE_JSON = "employee_json";
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextPhoneNumber;
    TextView textViewDate;
    Button buttonDate;

    Employee employee=null;

    Date birthDate = new Date();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        editTextFirstName   = findViewById(R.id.editTextFirstName);
        editTextLastName    = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        textViewDate        = findViewById(R.id.textViewDate);

        textViewDate.setText(format.format(birthDate));

        if (getIntent().getExtras().containsKey(EMPLOYEE_JSON)) {
            String employJson = getIntent().getExtras().getString(EMPLOYEE_JSON);
            try {
                JSONObject jsonObjectEmployee = new JSONObject(employJson);
                employee = Employee.fromJson(jsonObjectEmployee);

                editTextFirstName.setText(employee.getFirstName());
                editTextLastName.setText(employee.getLastName());
                editTextPhoneNumber.setText(employee.getPhoneNumber());
                textViewDate.setText(format.format(employee.getDateOfBirth()));
                birthDate = employee.getDateOfBirth();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        buttonDate = findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePicker();
            }
        });
    }

    void dialogDatePicker() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Data de Nascimento");
        final DatePicker datePicker= new DatePicker(this);
        datePicker.setLayoutParams(lp);


        Calendar calendar = new GregorianCalendar();
        calendar.setTime(birthDate);
        int year = calendar.get(Calendar.YEAR);


        datePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        alertDialog.setView(datePicker);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year =  datePicker.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                birthDate = calendar.getTime();
                textViewDate.setText(format.format(birthDate));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_save:

                if (employee==null){
                    // ADD NEW
                    Employee employee = new Employee();
                    employee.setEmployeeId((int)System.currentTimeMillis());
                    employee.setFirstName(editTextFirstName.getText().toString());
                    employee.setLastName(editTextLastName.getText().toString());
                    employee.setPhoneNumber(editTextPhoneNumber.getText().toString());
                    employee.setDateOfBirth(birthDate);
                    VolleyHelper.getInstance().add(this, "/employee/", employee.toJson(), new VolleyHelper.OnAddListener() {
                        @Override
                        public void onSucess(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String response = jsonObject.getString("status");
                                if (response.compareTo("ok")==0){
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Erro ao adicionar!",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getApplicationContext(), "Erro ao adicionar!",Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    //UPDATE
                    employee.setFirstName(editTextFirstName.getText().toString());
                    employee.setLastName(editTextLastName.getText().toString());
                    employee.setPhoneNumber(editTextPhoneNumber.getText().toString());
                    employee.setDateOfBirth(birthDate);
                    VolleyHelper.getInstance().update(this, "/employee/",
                            ""+employee.getEmployeeId(),
                            employee.toJson(),
                            new VolleyHelper.OnAddListener() {
                        @Override
                        public void onSucess(String data) {
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String response = jsonObject.getString("status");
                                if (response.compareTo("ok")==0){
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Erro ao actualizar!",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(getApplicationContext(), "Erro ao actualizar!",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                return true;
            case R.id.menu_delete:
            VolleyHelper.getInstance().delete(this,"/employee/",
                ""+employee.getEmployeeId(),
                    new VolleyHelper.OnAddListener(){

                    @Override
                    public void onSucess(String data) {
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplicationContext(), "Erro ao apagar!",Toast.LENGTH_LONG).show();

                    }
                } );
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
