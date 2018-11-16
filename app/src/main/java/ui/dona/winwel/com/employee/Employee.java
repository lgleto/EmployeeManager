package ui.dona.winwel.com.employee;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Employee   {

    int        employeeId  ;
    String     firstName   ;
    String     lastName    ;
    Date       dateOfBirth ;
    String     phoneNumber ;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Employee(int employeeId, String firstName, String lastName, Date dateOfBirth, String phoneNumber) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public Employee() {
        this.employeeId = 0;
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = new Date();
        this.phoneNumber = "";
    }

    static Employee fromJson(JSONObject jsonObject){
        Employee employee =  new Employee();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        try {
            employee.employeeId  = (int)        jsonObject.getInt   ("employeeId" );
            employee.firstName   = (String)     jsonObject.getString("firstName"  );
            employee.lastName    = (String)     jsonObject.getString("lastName"   );
            employee.dateOfBirth = format.parse(jsonObject.getString("dateOfBirth"));
            employee.phoneNumber = (String)     jsonObject.getString("phoneNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return employee;
    }

    JSONObject toJson(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("employeeId" , employeeId );
            jsonObject.put("firstName"  , firstName  );
            jsonObject.put("lastName"   , lastName   );
            jsonObject.put("dateOfBirth", format.format(dateOfBirth));
            jsonObject.put("phoneNumber", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
