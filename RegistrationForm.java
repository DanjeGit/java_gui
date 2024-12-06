import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class RegistrationForm {
    private JFrame frame;
    private JTextField idField, nameField, addressField, contactField;
    private JRadioButton maleButton, femaleButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public RegistrationForm() {
        
        frame = new JFrame("Registration Form");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

      
        JLabel titleLabel = new JLabel("USER REGISTRATION FORM");
        titleLabel.setBounds(300, 10, 200, 20);
        frame.add(titleLabel);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(50, 50, 100, 25);
        frame.add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 50, 150, 25);
        frame.add(idField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 90, 100, 25);
        frame.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 90, 150, 25);
        frame.add(nameField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(50, 130, 100, 25);
        frame.add(genderLabel);

        maleButton = new JRadioButton("Male");
        maleButton.setBounds(150, 130, 70, 25);
        femaleButton = new JRadioButton("Female");
        femaleButton.setBounds(230, 130, 80, 25);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        frame.add(maleButton);
        frame.add(femaleButton);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 170, 100, 25);
        frame.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(150, 170, 150, 25);
        frame.add(addressField);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setBounds(50, 210, 100, 25);
        frame.add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(150, 210, 150, 25);
        frame.add(contactField);

     
        JButton registerButton = new JButton("REGISTER");
        registerButton.setBounds(50, 260, 120, 30);
        frame.add(registerButton);

        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(200, 260, 120, 30);
        frame.add(exitButton);

      
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Gender", "Address", "Contact"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(350, 50, 400, 200);
        frame.add(scrollPane);

      
        registerButton.addActionListener(e -> registerData());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void registerData() {
        String id = idField.getText();
        String name = nameField.getText();
        String gender = maleButton.isSelected() ? "Male" : (femaleButton.isSelected() ? "Female" : "");
        String address = addressField.getText();
        String contact = contactField.getText();

        if (id.isEmpty() || name.isEmpty() || gender.isEmpty() || address.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

       
        String url = "jdbc:mysql://localhost:3306/registration_data"; // Replace with your DB details
        String user = "root"; 
        String password = ""; 

        String query = "INSERT INTO registration_data (id, name, gender, adress, contact) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users_database?","root","");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, gender);
            stmt.setString(4, address);
            stmt.setString(5, contact);

            stmt.executeUpdate();

          
            tableModel.addRow(new Object[]{id, name, gender, address, contact});

           
        } catch (SQLException ex) {
            ex.printStackTrace();
            //JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}
