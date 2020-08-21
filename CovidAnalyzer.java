import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CovidAnalyzer {
	final static int analyzerWindowWidth = 650, analyzerWindowHeight = 650;
	//private static final Object EXIT_ON_CLOSE = ;
	JFrame frame;
	ArrayList<Character> towers;
	String currentDate;
	PatientData patientData;
	ArrayList<Patient> patientViewData;
	int activeCases = 0, recoveredCases = 0;

	private void setWelcomeText() {
		// Set welcome text in the frame
		JLabel welcomeText = new JLabel("WELCOME TO COVID ANALYZER", SwingConstants.CENTER);
		welcomeText.setBounds(0,20, analyzerWindowWidth,50);
		welcomeText.setFont(new Font("Verdana", Font.BOLD, 18));
		this.frame.add(welcomeText);
	}

	private void getAnalyticsParameters() {
		this.activeCases = 0;
		this.recoveredCases = 0;

		for(Patient patient: this.patientViewData) {
			switch (patient.status) {
				case "ACTIVE":
					this.activeCases++;
					break;
				case "RECOVERED":
					this.recoveredCases++;
					break;
				default: break;
			}
		}
	}

	private void setAnalyzedParameterPanel() {
		this.getAnalyticsParameters();
		// Set analyzed parameters Active Cases, Recovered Cases
		JLabel activeCaseText = new JLabel("ACTIVE CASES : " + this.activeCases, SwingConstants.LEFT);
		activeCaseText.setBounds(20,90, analyzerWindowWidth / 2,50);
		activeCaseText.setFont(new Font("Verdana", Font.BOLD, 14));
		this.frame.add(activeCaseText);

		JLabel recoveredCaseText = new JLabel("RECOVERED CASES : " + this.recoveredCases, SwingConstants.LEFT);
		recoveredCaseText.setBounds(20 + analyzerWindowWidth / 2,90, analyzerWindowWidth /2,50);
		recoveredCaseText.setFont(new Font("Verdana", Font.BOLD, 14));
		this.frame.add(recoveredCaseText);
	}

	private void setPatientsViewData(DefaultTableModel tableModel) {
//		Get Patients ViewData first

		for(Patient patient: patientViewData) {
			tableModel.addRow(new Object[]{ patient.name, patient.age, patient.tower, patient.reportedDate, patient.recoveryDate,patient.status});
		}
	}

	private void setPatientsViewPanel() {
		JTable patientsTable = new JTable();

		// Create & set model in table
		DefaultTableModel tableModel = new DefaultTableModel();
		String headers[] = { "NAME", "AGE", "TOWER", "REPORTED DATE","RECOVERY DATE","STATUS" };
		tableModel.setColumnIdentifiers(headers);
		setPatientsViewData(tableModel);
		patientsTable.setModel(tableModel);

		JScrollPane scroll = new JScrollPane(patientsTable);
		scroll.setBounds(10, 160, analyzerWindowWidth - 30, analyzerWindowHeight / 2 );
		frame.add(scroll);
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
	private boolean validate(String inputDate) throws ParseException {
		if(inputDate.length()<10)
			return false;
		Date checkDate=convertDate(inputDate);
		Date startDate=convertDate("01-04-2020");
		Date endDate=convertDate("31-08-2020");
		if(checkDate.compareTo(startDate)<0 || checkDate.compareTo(endDate)>0)
			return false;
		return true;
	}
	private JPanel setDatePanel() {
		JPanel datePanel = new JPanel();
		JLabel dateLabel = new JLabel("Set Date: (DD-MM-YYYY)", SwingConstants.LEFT);
		dateLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		datePanel.add(dateLabel);

		JTextField dateInput = new JTextField(currentDate);
		dateInput.setPreferredSize(new Dimension( (analyzerWindowWidth / 3) - 20, 30));
		dateInput.addActionListener(e -> {
			String dateInputString = dateInput.getText();
			if(dateInputString != currentDate) {
				// validate dateInputString
				try {
					if(validate(dateInputString))
					{   // if valid date then change currentDate & trigger UI update for analyticsParameter & Table
						currentDate = dateInputString;
						patientViewData = patientData.getViewData(currentDate);
						resetToDefault();
					}
					//for case when the date is not valid means either not entered properly or before april or after august

				} catch (ParseException ex) {
					ex.printStackTrace();
				}

			}
		});
		datePanel.add(dateInput);

		datePanel.setBounds(10, analyzerWindowHeight * 4 / 5, (analyzerWindowWidth / 3) - 20, analyzerWindowHeight / 5);
		return datePanel;
	}

	private void handleCheckboxToggle(ActionEvent e, char towerClicked){
		int towerIndex = towers.indexOf(towerClicked);

		if(towerIndex == -1) {
			towers.add(towerClicked);
		} else {
			towers.remove(towerIndex);
		}

		try {
			patientViewData = patientData.getViewData(towers, currentDate);
		} catch(ParseException error) {
			System.out.println("Parse Exception when updating data for selected towers");
			error.printStackTrace();
		}

		resetToDefault();
	}

	private void setCheckedState(JCheckBox checkbox, Character presentCheckboxText) {
		int towerIndex = towers.indexOf(presentCheckboxText);

		if(towerIndex != -1) {
			checkbox.setSelected(true);
		}
	}

	private JPanel setTowerPanel() {
		JPanel towerPanel = new JPanel();
		JLabel towerLabel= new JLabel("TOWERS:",SwingConstants.LEFT);
		towerLabel.setFont(new Font("Verdana",Font.BOLD,14));
		towerPanel.add(towerLabel);

		JCheckBox boxA=new JCheckBox("A");
		boxA.setBounds(450,500,20,20);
		boxA.addActionListener(e -> handleCheckboxToggle(e,'A'));
		setCheckedState(boxA, 'A');

		JCheckBox boxB=new JCheckBox("B");
		boxB.setBounds(470,500,20,20);
		boxB.addActionListener(e -> handleCheckboxToggle(e,'B'));
		setCheckedState(boxB, 'B');

		JCheckBox boxC=new JCheckBox("C");
		boxC.setBounds(490,500,20,20);
		boxC.addActionListener(e -> handleCheckboxToggle(e,'C'));
		setCheckedState(boxC, 'C');

		JCheckBox boxD=new JCheckBox("D");
		boxD.setBounds(510,500,20,20);
		boxD.addActionListener(e -> handleCheckboxToggle(e,'D'));
		setCheckedState(boxD, 'D');

		towerPanel.add(boxA);
		towerPanel.add(boxB);
		towerPanel.add(boxC);
		towerPanel.add(boxD);
		towerPanel.setBounds(400, analyzerWindowHeight * 4/5 , (analyzerWindowWidth / 6) -20, analyzerWindowHeight / 4);
		return towerPanel;
	}

	private void setViewControlPanel() {
		JPanel datePanel = setDatePanel();
		JPanel towerPanel = setTowerPanel();
//		Reset to default control
//		JPanel otherControls = setOtherControl();

		frame.add(datePanel);
		frame.add(towerPanel);
	}

	private void setLayout() {
		this.setWelcomeText();
		this.setAnalyzedParameterPanel();
		this.setPatientsViewPanel();
		this.setViewControlPanel();

		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
	}

	private void setFrameWindow() {
		this.setLayout();
		this.frame.setSize(analyzerWindowWidth, analyzerWindowHeight);
		this.frame.setLayout(null);
		this.frame.setVisible(true);
	}

	private void resetToDefault() {
		// clear existing layout from frame
		// reset all the parameters defined
		// trigger setLayout
		frame.getContentPane().removeAll();
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
		setLayout();
	}

	public static void main(String[] args) throws ParseException {
		CovidAnalyzer analyzer = new CovidAnalyzer();
		analyzer.frame = new JFrame();
		analyzer.patientData= new PatientData();
		analyzer.patientViewData = analyzer.patientData.getViewData();
		analyzer.towers = new ArrayList<>();
		analyzer.currentDate = "21-08-2020";
		analyzer.setFrameWindow();
	}
}
