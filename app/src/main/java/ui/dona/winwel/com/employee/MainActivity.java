package ui.dona.winwel.com.employee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "employee";

    List<Employee> employees = new ArrayList<>();

    ListView listView;
    EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        adapter = new EmployeeAdapter();
        listView.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        VolleyHelper.getInstance().getAll(this, "/employee/", new VolleyHelper.OnGetDataListener() {
            @Override
            public void onSucess(String data) {
                try {
                    employees.clear();
                    JSONArray jsonArray = new JSONArray(data);
                    for(int i = 0; i < jsonArray.length(); i++){
                        Employee e =  Employee.fromJson((JSONObject)jsonArray.get(i));
                        employees.add(e);
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                Intent intent =  new Intent(MainActivity.this, EmployeeDetailActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class EmployeeAdapter extends BaseAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return employees.size();
        }

        @Override
        public Object getItem(int position) {
            return employees.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView==null)
                convertView = getLayoutInflater().inflate(R.layout.row_employee,null);
            TextView textViewName = convertView.findViewById(R.id.textviewName);
            TextView textViewPhone = convertView.findViewById(R.id.textviewPhoneNumber);

            Employee employee = employees.get(position);

            textViewName.setText( employee.getFirstName() + " " + employee.getLastName());
            textViewPhone.setText(employee.getPhoneNumber());

            convertView.setOnClickListener(this);
            convertView.setClickable(true);
            convertView.setTag(new Integer(position));

            return convertView;
        }

        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            Employee employee = employees.get(position);
            Intent intent =  new Intent(MainActivity.this, EmployeeDetailActivity.class);
            intent.putExtra(EmployeeDetailActivity.EMPLOYEE_JSON, employee.toJson().toString());
            startActivity(intent);

        }
    }
}
