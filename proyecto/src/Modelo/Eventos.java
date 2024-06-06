package Modelo;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class Eventos {
    
    private boolean clienteSeleccionado = false;

    public void textKeyPress(KeyEvent evt) {
        char car = evt.getKeyChar();
        if (!clienteSeleccionado && ((car < 'a' || car > 'z') && (car < 'A' || car > 'Z')
                && (car != (char) KeyEvent.VK_BACK_SPACE) && (car != (char) KeyEvent.VK_SPACE))) {
            evt.consume();
        }
    }

    public void numberKeyPress(KeyEvent evt) {
        char car = evt.getKeyChar();
        if (!clienteSeleccionado && ((car < '0' || car > '9') && (car != (char) KeyEvent.VK_BACK_SPACE))) {
            evt.consume();
        }
    }

    public void numberDecimalKeyPress(KeyEvent evt, JTextField textField) {
        char car = evt.getKeyChar();
        if (!clienteSeleccionado && ((car < '0' || car > '9') && textField.getText().contains(".") && (car != (char) KeyEvent.VK_BACK_SPACE))) {
            evt.consume();
        } else if (!clienteSeleccionado && ((car < '0' || car > '9') && (car != '.') && (car != (char) KeyEvent.VK_BACK_SPACE))) {
            evt.consume();
        }
    }
    
    // Método para establecer el estado del cliente seleccionado
    public void setClienteSeleccionado(boolean seleccionado) {
        this.clienteSeleccionado = seleccionado;
    }
}
