import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class PatientData {
	HashMap<Character, ArrayList<Patient>> patients;
	String[] names = {"Flora",	"Denys",	"Jim",	"Hazel",	"Carey",	"David",	"Kevin",	"Tom",	"Bob",
			"Rachel",	"Thomas",	"Mary",	"Smith",	"Pearson",	"Anderson",	"Johnson",	"Robert",	"Julie",
			"Edith",	"John"};

	int[] age = {6,	24,	42,	87,	72,	7,	37,	67,	74,	48,	21,	17,	89,	47,	62,	10,	50,	86,	42,	95};

	Character[] towers = {'A',	'B',	'C',	'D',	'A',	'B',	'D',	'D',	'A',	'C',	'C',	'D',
			'A',	'B',	'B',	'D',	'A',	'B',	'D',	'D'};

	String[] dates = {"01-04-2020",	"01-04-2020",	"18-05-2020",	"23-06-2020",	"01-06-2020",	"14-06-2020",
			"05-06-2020",	"20-06-2020",	"04-07-2020",	"24-07-2020",	"11-06-2020",	"21-06-2020",
			"07-08-2020",	"04-06-2020",	"27-07-2020",	"01-08-2020",	"09-08-2020",	"02-05-2020",
			"07-06-2020",	"01-06-2020"};

	public PatientData() throws ParseException
	{
		patients = new HashMap<Character, ArrayList<Patient>>();

		// Initialised buckets for towers
		patients.put('A', new ArrayList<Patient>());
		patients.put('B', new ArrayList<Patient>());
		patients.put('C', new ArrayList<Patient>());
		patients.put('D', new ArrayList<Patient>());

		// Create patients & add them to respective buckets
		for(int i=0; i< 20; i++) {
			Patient patient = new Patient(names[i], age[i], towers[i], dates[i]);
			patients.get(towers[i]).add(patient);
		}
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
	public ArrayList<Patient> getViewData(){
		ArrayList<Patient> patientToView = new ArrayList<>();
		for(Character tower: patients.keySet()) {
			ArrayList<Patient> towerPatients = patients.get(tower);
			patientToView.addAll(towerPatients);
		}
		return patientToView;
	}

	public ArrayList<Patient> getViewData(ArrayList<Character> currentTowers){
		ArrayList<Patient> patientToView = new ArrayList<>();
		for(Character currentTower: currentTowers) {
			if(patients.containsKey(currentTower)) {
				patientToView.addAll(patients.get(currentTower));
			}
		}
		return patientToView;
	}

	public ArrayList<Patient> getViewData(String currentDate) throws ParseException {
		Date currDate=convertDate(currentDate);
		ArrayList<Patient> patientToView = new ArrayList<>();
		for(Character tower: patients.keySet()) {
			ArrayList<Patient> towerPatients = patients.get(tower);
			for(Patient patient: towerPatients) {
				Date patientDate=convertDate(patient.reportedDate);
				if(patientDate.compareTo(currDate)<=0) {
					patient.setStatus(currentDate);
					patientToView.add(patient);
				}
			}
		}
		return patientToView;
	}

	public ArrayList<Patient> getViewData(ArrayList<Character> currentTowers, String currentDate) throws ParseException {
		if(currentTowers.size() == 0) {
			return getViewData(currentDate);
		}

		Date currDate=convertDate(currentDate);
		ArrayList<Patient> patientToView = new ArrayList<>();
		for(Character currentTower: currentTowers) {
			if(patients.containsKey(currentTower)) {
				ArrayList<Patient> towerPatients = patients.get(currentTower);
				for(Patient patient: towerPatients) {
					Date patientDate=convertDate(patient.reportedDate);
					if(patientDate.compareTo(currDate)<=0) {
						patient.setStatus(currentDate);
						patientToView.add(patient);
					}
				}
			}
		}

		return patientToView;
	}
}
