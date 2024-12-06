import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserForm extends JFrame {

   
    private JTextField nameField, mobileField, addressField;
    private JRadioButton maleButton, femaleButton;
    private JComboBox<String> dayComboBox, monthComboBox, yearComboBox;
    private ButtonGroup genderGroup;
    private JTextArea textArea;
    private JButton submitButton, resetButton;

    
    public UserForm() {
       
        setTitle("User Data Form");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 20);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 20, 150, 20);
        add(nameField);

        JLabel mobileLabel = new JLabel("Mobile:");
        mobileLabel.setBounds(20, 50, 100, 20);
        add(mobileLabel);

        mobileField = new JTextField();
        mobileField.setBounds(120, 50, 150, 20);
        add(mobileField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(20, 80, 100, 20);
        add(genderLabel);

        maleButton = new JRadioButton("Male");
        maleButton.setBounds(120, 80, 70, 20);
        femaleButton = new JRadioButton("Female");
        femaleButton.setBounds(190, 80, 80, 20);

        genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        add(maleButton);
        add(femaleButton);

        JLabel dobLabel = new JLabel("DOB:");
        dobLabel.setBounds(20, 110, 100, 20);
        add(dobLabel);
         yearComboBox = new JComboBox<>(generateYears());
        yearComboBox.setBounds(260, 110, 70, 20);
        add(yearComboBox);
        monthComboBox = new JComboBox<>(generateMonths());
        monthComboBox.setBounds(180, 110, 70, 20);
        add(monthComboBox);
        dayComboBox = new JComboBox<>(generateDays());
        dayComboBox.setBounds(120, 110, 50, 20);
        add(dayComboBox);

        

       

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 140, 100, 20);
        add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(120, 140, 150, 20);
        add(addressField);

        
        textArea = new JTextArea();
        textArea.setBounds(300, 20, 250, 200);
        textArea.setEditable(false);
        add(textArea);

        
        submitButton = new JButton("Submit");
        submitButton.setBounds(40, 180, 110, 40);
        add(submitButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(180, 190, 100, 30);
        add(resetButton);

       
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Submit();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleReset();
            }
        });
    }

    private String[] generateYears() {
        String[] years = new String[50];
        int currentYear = 1970;
        for (int i = 0; i < 50; i++) {
            years[i] = String.valueOf(currentYear + i);
        }
        return years;
    }
    
    
   
    private String[] generateMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "Sept", "October", "November", "December"};
    }
    
    private String[] generateDays() {
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            days[i - 1] = String.valueOf(i);
        }
        return days;
    }


   

 
    private void Submit() {
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String gender = maleButton.isSelected() ? "Male" : (femaleButton.isSelected() ? "Female" : "");
         String year = (String) yearComboBox.getSelectedItem();
        String month = (String) monthComboBox.getSelectedItem();
        String day = (String) dayComboBox.getSelectedItem();
        String address = addressField.getText();

        if (name.isEmpty() || mobile.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all the areas!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dob = year + "-" + month + "-" + day;

        try (Connection conn = ToDatabase();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO userdata (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, mobile);
            stmt.setString(3, gender);
            stmt.setString(4, dob);
            stmt.setString(5, address);
            stmt.executeUpdate();

            textArea.append("Name: " + name + "\nMobile: " + mobile + "\nGender: " + gender +
                    "\nDOB: " + dob + "\nAddress: " + address + "\n\n");

            
        } catch (SQLException ex) {
            ex.printStackTrace();
          
        }
    }

   
    private void handleReset() {
        nameField.setText("");
        mobileField.setText("");
        genderGroup.clearSelection();
        dayComboBox.setSelectedIndex(0);
        monthComboBox.setSelectedIndex(0);
        yearComboBox.setSelectedIndex(0);
        addressField.setText("");
        
    }

   
    private Connection ToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/info_details";
            String user = "root"; 
            String password = ""; 
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserForm.class.getName()).log(Level.SEVERE, null, ex);
            }

            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserForm frame = new UserForm();
            frame.setVisible(true);
        });
    }
}
