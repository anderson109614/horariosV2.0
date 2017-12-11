/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ander
 */
public class Coneccion {

    Connection conecct;
    String ip = "";
    String userBD = "";
    String passBD = "";
    String nomBD = "";
    String CI = "false";
    String Ldatos;

    public Coneccion() {
        datos();
        conectar();

    }

    public Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            conecct = DriverManager.getConnection("jdbc:postgresql://" + ip + ":5432/" + nomBD, userBD, passBD);
            Principal.jLabelEstadoConecion.setText("Conectado");
            Principal.jLabelEstadoConecion.setForeground(Color.green);
//JOptionPane.showMessageDialog(null, "Conexxion Exitosa");
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, ex);
            //JOptionPane.showMessageDialog(null, "Error en la conexión"+ex);
            Principal.jLabelEstadoConecion.setText("Desconectado");
            Principal.jLabelEstadoConecion.setForeground(Color.RED);
            conectar();
        }
        return conecct;

    }

    public void datos() {
        File f = new File("conf.txt");
        String[] datosL = null;
        boolean aux = false;
        if (f.exists()) {
            try {
                Scanner arch = new Scanner(f);
                String linea;

                while (arch.hasNext()) {

                    linea = arch.nextLine();
                    datosL = linea.split("::");
                    if (datosL.length == 5) {
                        ip = datosL[0];
                        userBD = datosL[1];
                        passBD = datosL[2];
                        nomBD = datosL[3];
                        CI = datosL[4];
                        Ldatos=datosL[0]+"::"+datosL[1]+"::"+datosL[2]+"::"+datosL[3]+"::";
                    } else {
                        JOptionPane.showMessageDialog(null, "Problemas en los parametros del archivo conf.txt");
                    }

                   // System.out.println(ip + " " + user + " " + pass);
                }
                arch.close();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Problemas en la lectura del archivo conf.txt");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Archivo conf.txt no existente");
        }

    }
   
   public void ArchCedDoce(String usu) throws FileNotFoundException{
       File f = new File("conf.txt");
        PrintWriter salida = new PrintWriter(f);
        salida.println(Ldatos+usu);
        salida.close();
   }
   public void ArchSimCedDoce( ) throws FileNotFoundException{
       File f = new File("conf.txt");
        PrintWriter salida = new PrintWriter(f);
        salida.println(Ldatos+"false");
        salida.close();
   }
   
    public void generarArchivos(String usu) throws FileNotFoundException {
        File f = new File("jornadas.txt");
        PrintWriter salida = new PrintWriter(f);
        File f2 = new File("recordatorios.txt");
        PrintWriter salida2 = new PrintWriter(f2);
        try {
            Connection con1 = conectar();
            Connection con2 = conectar();
            String sql = "SELECT *"
                    + " from  jornadas "
                    + " WHERE Ced_Doc_Per='" + usu + "'";

            String sql2 = "SELECT *"
                    + " from  recordatorios "
                    + " WHERE r.Ced_Doc_Per='" + usu + "'";

            Statement stm = con1.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            Statement stm2 = con2.createStatement();
            ResultSet rs2 = stm2.executeQuery(sql2);

            while (rs.next()) {
                salida.println(rs.getString(1) + "::"//// guarda los atos de las jormadas 
                        + rs.getString(2) + "::"
                        + rs.getString(3) + "::"
                        + rs.getString(4) + "::"
                        + rs.getString(5) + "::"
                        + rs.getString(6) );
            }

            while (rs2.next()) {
                salida2.println(rs2.getString(1) + "::"
                        + rs2.getString(2) + "::"
                        + rs2.getString(3) + "::"
                        + rs2.getString(4) + "::"
                        + rs2.getString(5) );
            }
            salida2.close();
            salida.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void CargarArchivos() throws FileNotFoundException {
        File f = new File("jornadass.txt");
        File f2 = new File("recordatorios.txt");
        if (f.exists()) {
            Scanner entrada = new Scanner(f);
            while (entrada.hasNext()) {
                String linea = entrada.nextLine();
                String[] datos = linea.split("-");
                 
//                 datos[0]; 
//                 datos[1]; 
//                 datos[2]; 
//                 datos[3]; 
//                 datos[4]; 
//                 datos[5]; 
                  
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo local de horarios");
        }
        if (f2.exists()) {
            Scanner entrada2 = new Scanner(f2);
            while (entrada2.hasNext()) {
                String linea = entrada2.nextLine();
                String[] datos = linea.split("-");
                 
//                 datos[0]; 
//                 datos[1]; 
//                 datos[2]; 
//                 datos[3]; 
//                 datos[4]; 
                  
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo local de recordatorios");
        }
    }
    
    public static void validarCamposSoloLetras(KeyEvent evt,String texto) {
        // TODO add your handling code here:
        char Alfab[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'ñ', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'á', 'é', 'í', 'ó', 'ú', 'Á', 'É', 'Í', 'Ó', 'Ú', 'ü', 'Ü', ' '};
        char c = evt.getKeyChar();
        int x = 0;
        int longitud = Alfab.length;

        for (int i = 0; i < longitud; i++) {
            if (c != (Alfab[i])) {
                x += 1;
            }
        }

        if (x == longitud || texto.length() >= 20) {
            evt.consume();
        }
    }


}
