package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class LoginDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    private boolean isValidEmail(String email) {
        String emailRegex = "^[^@]+@[^@]+\\.[^@]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }



    public login log(String correo, String pass) {
        login l = new login();
        
        if (!isValidEmail(correo)) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un correo electrónico válido.", "Error de formato de correo", JOptionPane.ERROR_MESSAGE);
            return l;
        }

        String sql = "SELECT * FROM usuarios WHERE correo = ? AND pass = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            if (rs.next()) {
                l.setId(rs.getInt("id"));
                l.setNombre(rs.getString("nombre"));
                l.setCorreo(rs.getString("correo"));
                l.setPass(rs.getString("pass"));
                l.setRol(rs.getString("rol"));
                JOptionPane.showMessageDialog(null, "¡Bienvenido!");
            } else {
                JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos.", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return l;
    }

    public boolean Registrar(login reg) {

    if (!isValidPassword(reg.getPass())) {
        JOptionPane.showMessageDialog(null, "Contraseña inválida. Debe tener al menos 8 caracteres.", "Error de validación", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    if (!isValidEmail(reg.getCorreo())) {
        JOptionPane.showMessageDialog(null, "Dirección de correo inválida. Debe contener '@'.", "Error de validación", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    String sql = "INSERT INTO usuarios (nombre, correo, pass, rol) VALUES (?,?,?,?)";
    try (Connection con = cn.getConnection(); 
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, reg.getNombre());
        ps.setString(2, reg.getCorreo());
        ps.setString(3, reg.getPass());
        ps.setString(4, reg.getRol());
        ps.execute();
        return true;
    } catch (SQLException e) {
        System.out.println(e.toString());
        JOptionPane.showMessageDialog(null, "Usuario no registrado.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}


    public List<login> ListarUsuarios() {
        List<login> Lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {               
                login lg = new login();
                lg.setId(rs.getInt("id"));
                lg.setNombre(rs.getString("nombre"));
                lg.setCorreo(rs.getString("correo"));
                lg.setRol(rs.getString("rol"));
                Lista.add(lg);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return Lista;
    }

public boolean eliminarUsuario(int userId, DefaultTableModel modelo, int selectedRow) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();

            modelo.removeRow(selectedRow);

            JOptionPane.showMessageDialog(null, "El usuario ha sido eliminado", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, "Error al eliminar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}