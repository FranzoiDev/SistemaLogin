import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class SistemaLogin {

    private JFrame frame;
    private JTextField textFieldLogin;
    private JPasswordField passwordField;
    private Connection connection;

    public SistemaLogin() {
        initialize();
        connectToDatabase();
    }

    private void initialize() {
        frame = new JFrame("Sistema de Login");
        frame.setBounds(100, 100, 400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(50, 50, 80, 20);
        frame.getContentPane().add(lblLogin);

        textFieldLogin = new JTextField();
        textFieldLogin.setBounds(150, 50, 150, 20);
        frame.getContentPane().add(textFieldLogin);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(50, 80, 80, 20);
        frame.getContentPane().add(lblSenha);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 80, 150, 20);
        frame.getContentPane().add(passwordField);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        btnEntrar.setBounds(100, 120, 100, 30);
        frame.getContentPane().add(btnEntrar);

        JButton btnCadastrar = new JButton("Cadastrar Novo Usuário");
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCadastro();
            }
        });
        btnCadastrar.setBounds(210, 120, 160, 30);
        frame.getContentPane().add(btnCadastrar);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost/mapa";
            String user = "seuUsuario";
            String password = "suaSenha";
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login() {
        String login = textFieldLogin.getText();
        String senha = new String(passwordField.getPassword());

        try {
            String query = "SELECT id, nome, login, senha, email FROM usuario WHERE login = ? AND senha = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, senha);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(frame, "Acesso Autorizado");
            } else {
                JOptionPane.showMessageDialog(frame, "Acesso Negado");
            }

            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCadastro() {
        frame.dispose(); // Fecha a tela de login
        CadastroUsuario cadastro = new CadastroUsuario(connection); // Passa a conexão para o construtor
        cadastro.show();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SistemaLogin window = new SistemaLogin();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

class CadastroUsuario {

    private JFrame frame;
    private JTextField textFieldNome;
    private JTextField textFieldLogin;
    private JPasswordField passwordField;
    private JTextField textFieldEmail;
    private Connection connection;

    public CadastroUsuario(Connection connection) {
        this.connection = connection;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Cadastro de Usuário");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(50, 30, 80, 20);
        frame.getContentPane().add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(150, 30, 200, 20);
        frame.getContentPane().add(textFieldNome);

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(50, 60, 80, 20);
        frame.getContentPane().add(lblLogin);

        textFieldLogin = new JTextField();
        textFieldLogin.setBounds(150, 60, 200, 20);
        frame.getContentPane().add(textFieldLogin);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(50, 90, 80, 20);
        frame.getContentPane().add(lblSenha);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 200, 20);
        frame.getContentPane().add(passwordField);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 120, 80, 20);
        frame.getContentPane().add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(150, 120, 200, 20);
        frame.getContentPane().add(textFieldEmail);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarUsuario();
            }
        });
        btnCadastrar.setBounds(150, 160, 100, 30);
        frame.getContentPane().add(btnCadastrar);
    }

    private void cadastrarUsuario() {
        String nome = textFieldNome.getText();
        String login = textFieldLogin.getText();
        String senha = new String(passwordField.getPassword());
        String email = textFieldEmail.getText();

        try {
            String query = "INSERT INTO usuario (nome, login, senha, email) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, senha);
            preparedStatement.setString(4, email);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Cadastro realizado com sucesso.");
                frame.dispose(); // Fecha a janela de registro após o registro bem-sucedido.
            } else {
                JOptionPane.showMessageDialog(frame, "Erro ao cadastrar o usuário.");
            }

            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}

