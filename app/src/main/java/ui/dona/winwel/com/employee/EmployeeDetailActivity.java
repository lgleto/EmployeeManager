package ui.dona.winwel.com.employee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class EmployeeDetailActivity extends AppCompatActivity {

    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextPhoneNumber;
    TextView textViewDate;
    Button buttonDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        textViewDate = findViewById(R.id.textViewDate);
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
                Employee employee = new Employee();
                employee.setEmployeeId((int)System.currentTimeMillis());
                employee.setFirstName(editTextFirstName.getText().toString());
                employee.setLastName(editTextLastName.getText().toString());
                employee.setPhoneNumber(editTextPhoneNumber.getText().toString());
                employee.setDateOfBirth(new Date());


VolleyHelper.getInstance().add(this, "/employee/", employee.toJson(), new VolleyHelper.OnAddListener() {
    @Override
    public void onSucess(String data) {

    }

    @Override
    public void onError(String error) {

    }
});

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
