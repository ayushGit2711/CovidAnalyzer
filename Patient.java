import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Patient {
    String name;
    int age;
    char tower;
    String reportedDate;
    String status;
    String recoveryDate;

    public Patient(String name, int age, char tower, String dateString) throws ParseException {
        this.name=name;
        this.age=age;
        this.tower=tower;
        this.reportedDate = dateString;
        this.recoveryDate= setRecoveryDate(dateString);
        setStatus("21-08-2020");
    }

    public Date convertDate(String input) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date dt = format.parse(input);
        Date start=format.parse("01-04-2020");
        Date end=format.parse("31-08-2020");
        if(dt.compareTo(start)<0 || dt.compareTo(end)>0){
            return null;
        }
        return dt;
    }

    public String setRecoveryDate(String input) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(format.parse(input));
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, 21);
        //Date after adding the days to the given date
        String newDate = format.format(c.getTime());
        //here the recover date is stored and it will be returned.
        return newDate;
    }

    public void setStatus(String currentDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = format.parse(currentDate);
        if (d1.compareTo(convertDate(recoveryDate)) > 0) {
            // When  current date is greater than recovery date and his illness date means patient is recovered
            this.status =  "RECOVERED";
        }
        else if(d1.compareTo(convertDate(reportedDate))>=0 && d1.compareTo(convertDate(recoveryDate))<=0){
            this.status = "ACTIVE";
        }
        else{
            this.status = "HEALTHY";
        }
    }
}
