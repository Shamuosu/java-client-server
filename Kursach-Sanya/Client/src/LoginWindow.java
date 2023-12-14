import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginWindow extends Component implements ActionListener {
    static JFrame reg = new JFrame();
    JLabel progLabel = new JLabel("Tourism");
    JLabel nLabel = new JLabel("Name:");
    JLabel hLabel = new JLabel("Mode:");
    JLabel pLabel = new JLabel("Password:");
    JRadioButton isGeneral = new JRadioButton("Log in as General manager");
    JTextField name = new JTextField();
    JPasswordField pass = new JPasswordField();
    JTextField hotel = new JTextField();

    JButton submitAuthorization = new JButton("Log in");
    JButton submitRegistration = new JButton("Sign up");

    LoginWindow() {
        reg.setBackground(new Color(228, 239, 216));
        reg.setTitle("Authorization Window");
        reg.setSize(400,600);
        reg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        reg.setResizable(false);
        reg.setLayout(null);
        reg.setVisible(true);
        submitAuthorization.addActionListener(this);
        submitRegistration.addActionListener(this);
        pass.setEchoChar('*');
        reg.add(Button(submitAuthorization, 35, 490, 150, 40));
        reg.add(Button(submitRegistration, 205, 490, 150, 40));
        reg.add(Label(nLabel, 35, 150, 300, 40));
        reg.add(Field(name, 35, 190, 320, 40));
        reg.add(Label(pLabel, 35, 250, 300, 40));
        reg.add(Field(pass, 35, 290, 320, 40));
        isGeneral.setBounds(35, 430, 320, 40);
        reg.add(isGeneral);
        reg.add(Label(hLabel, 35, 350, 300, 40));
        reg.add(Field(hotel, 35, 390, 320, 40));
        reg.add(picLabel(progLabel, 10, 35, 330, 128));
    }

    static JButton Button(JButton b, int x, int y, int w, int h) {
        b.setBounds(x, y, w, h);
        b.setFont(new Font("Inter", Font.PLAIN,20));
        b.setForeground(new Color(90, 98, 90));
        b.setBackground(new Color(217, 217, 217));

        return b;
    }

    JTextField Field(JTextField f, int x, int y, int w, int h){
        f.setBounds(x, y, w, h);
        f.setFont(new Font("Inter", Font.ITALIC,20));
        f.setForeground(new Color(90, 98, 90));
        f.setBackground(new Color(228, 239, 216));

        return f;
    }

    JLabel Label(JLabel l, int x, int y, int w, int h){
        l.setBounds(x, y, w, h);
        l.setFont(new Font("Inter", Font.BOLD, 20));
        l.setForeground(new Color(90, 98, 90));
        return l;
    }
    JLabel picLabel(JLabel l, int x, int y, int w, int h){
        l.setIconTextGap(-25);
        l.setBounds(x, y, w, h);
        l.setFont(new Font("Inter", Font.BOLD, 30));
        l.setForeground(new Color(90, 98, 90));
        return l;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String ht;
        if (isGeneral.isSelected()){
            ht = "general";
        }else {
            ht = hotel.getText();
        }
        String nm = name.getText();
        String ps = String.valueOf(pass.getPassword());
        hotel.setText("");
        name.setText("");
        pass.setText("");
        if ((nm.matches("\\w+")) && (ps.matches("\\w+")) && (!ht.isEmpty())) {
            CommandControl.CmdReviewLogin(cmd, nm, ps, ht, null);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Fields must contain only letters and numbers!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
