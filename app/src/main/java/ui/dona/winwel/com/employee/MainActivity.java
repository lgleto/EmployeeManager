package ui.dona.winwel.com.employee;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribed general";
                        if (!task.isSuccessful()) {
                            msg = "failed to subscribed general";
                        }
                        Log.d(TAG, msg);
                    }
                });


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


/*
                    Intent iHeartBeatService;
                    PendingIntent piHeartBeatService;
                    AlarmManager alarmManager;
                    alarmManager = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
                    iHeartBeatService = new Intent(MainActivity.this, ScheduleService.class);
                    piHeartBeatService = PendingIntent.getService(MainActivity.this, 0, iHeartBeatService, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(piHeartBeatService);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, piHeartBeatService);
*/
                    Intent intentService = new Intent(MainActivity.this, ScheduleService.class);
                    String [] employeesStr = new String[employees.size()];
                    for (int i = 0; i < employees.size(); i++){
                        employeesStr[i]=employees.get(i).toJson().toString();
                    }
                    intentService.putExtra(ScheduleService.EMPLOYEES,employeesStr);
                    startService(intentService);

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
