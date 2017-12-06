/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import static horarios.reproducir.getSound;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ander
 */
public class Principal extends javax.swing.JFrame {

    /**
     * Creates new form Principal
     */
    Timer t;
    private ImageIcon imageicon;
    private TrayIcon trayicon;
    private SystemTray systemtray;
    Coneccion cc;
    DefaultTableModel modeloTabla;
    static String cedula = "";
    static String nom = "";

    public Principal(String ced) {

        try {
            initComponents();
            pnlFondo fondo = new pnlFondo(this.getWidth(), this.getHeight());
            this.add(fondo, BorderLayout.CENTER);
            cc = new Coneccion();
            cc.ArchCedDoce(ced);
            t = new Timer(1000, acciones);
            imageicon = new ImageIcon(this.getClass().getResource("/imagenes/robot.png"));
            this.setIconImage(imageicon.getImage());
            instanciarTray();
            this.setLocationRelativeTo(null);
            noCerrer();

            cedula = ced;
            cargarTablaRecordatorios();
            lblNomDoc.setText(colocarNomDocente(ced));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Principal() {

        try {
            initComponents();
            lblNomDoc.setText(colocarNomDocente("1805037619"));

            cc = new Coneccion();
            cc.ArchCedDoce("1805037619");
            t = new Timer(1000, acciones);
            imageicon = new ImageIcon(this.getClass().getResource("/imagenes/robot.png"));
            this.setIconImage(imageicon.getImage());
            instanciarTray();
            this.setLocationRelativeTo(null);
            noCerrer();
            cargarTablaRecordatorios();
            cedula = "1805037619";

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String colocarNomDocente(String ced) {
        try {
            Coneccion cc = new Coneccion();
            Connection cn = cc.conectar();

            String sql = "select nom_doc,ape_doc from docentes where ced_doc='" + cedula + "';";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                return rs.getString("nom_doc") + " " + rs.getString("Ape_doc");

            }
            cn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        return "";
    }

    public static ArrayList recHoy(String fecha, String hora) {

        ArrayList des = new ArrayList();
        try {
            Coneccion cc = new Coneccion();
            Connection cn = cc.conectar();

            String sql = "select * from recordatorios where fec_rec='" + fecha + "' and ced_doc_per='" + cedula + "' and hor_rec = '" + hora + "' order by hor_rec asc";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cn.close();
            while (rs.next()) {
                des.add(rs.getString("des_rec"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);

        }

        return des;
    }

    public static ArrayList JorHoy(String dia, String hora) {

        ArrayList des = new ArrayList();
        try {
            Coneccion cc = new Coneccion();
            Connection cn = cc.conectar();

            String sql = "select * from jornadas where id_dia_per='" + dia + "' and ced_doc_per='" + cedula + "' and hor_emp = '" + hora + "' order by hor_emp asc";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            cn.close();
            while (rs.next()) {
                des.add(rs.getString("des_jor"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);

        }

        return des;
    }

    public void noCerrer() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ocultar();
            }
        });
    }

    public void cargarTablaRecordatorios() {
        String[] titulos = {"DESCRIPCION", "FECHA", "HORA"};
        tblRecordatorio.getTableHeader().setReorderingAllowed(false);
        modeloTabla = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblRecordatorio.setModel(modeloTabla);
        SimpleDateFormat parseador = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat parseador2 = new SimpleDateFormat("H:mm");
        Connection cn = cc.conectar();
        Calendar fecha = Calendar.getInstance();
        String sql = "select * from recordatorios where fec_rec ='" + parseador.format(fecha.getTime()) + "' and ced_doc_per = '" + cedula + "' and hor_rec >= '" + parseador2.format(fecha.getTime()) + "' order by hor_rec asc";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            String[] fila = new String[3];
            while (rs.next()) {
                fila[0] = rs.getString("des_rec");
                fila[1] = rs.getString("fec_rec");
                fila[2] = rs.getString("hor_rec");
                modeloTabla.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR DE BASE: " + ex);
        }

    }

    private void instanciarTray() {
        trayicon = new TrayIcon(imageicon.getImage(), "Horarios", menuDes);
        trayicon.setImageAutoSize(true);
        systemtray = SystemTray.getSystemTray();

    }

    public void ocultar() {

        try {
            if (SystemTray.isSupported()) {
                systemtray.add(trayicon);
                this.setVisible(false);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void mostrar() {
        systemtray.remove(trayicon);
        this.setVisible(true);
    }
    int x = 0;
    private ActionListener acciones = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ///

            ///////Aqui poner todo lo que se va a estar repitiento cada 5 segundos
            SimpleDateFormat formateador = new SimpleDateFormat(" dd-MM-yyyy");
            
            Calendar cal = Calendar.getInstance();
            lblReloj.setText(formateador.format(cal.getTime()));
            SimpleDateFormat formateador2 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat formateador3 = new SimpleDateFormat("H:mm:ss");
            String fec = formateador2.format(cal.getTime());
            String hor = formateador3.format(cal.getTime());
            lblfecha.setText(fec);
            ArrayList des = recHoy(fec, hor);

            if (des.size() > 0) {
                for (int i = 0; i < des.size(); i++) {
                    sonido();
                    sonido();
                    sonido();
                    sonido();
                    JOptionPane.showMessageDialog(null, "Recuerde: " + des.get(i));
                }

            }

            SimpleDateFormat formateadorM = new SimpleDateFormat("mm");
            String minExt = formateadorM.format(cal.getTime()); 
            if (minExt == "00") {
                SimpleDateFormat formateadorH = new SimpleDateFormat("HH");
                String horExt = formateadorH.format(cal.getTime());
                System.out.println(horExt);
                String dia = String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);

                ArrayList desJor = JorHoy(dia, horExt + ":00:00");

                if (desJor.size() > 0) {
                    for (int i = 0; i < desJor.size(); i++) {
                        sonido();
                        sonido();
                        sonido();
                        sonido();
                        JOptionPane.showMessageDialog(null, "Recuerde: " + desJor.get(i));
                    }

                }

            }

            //////
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuDes = new java.awt.PopupMenu();
        menuAbrir = new java.awt.MenuItem();
        lblReloj = new javax.swing.JLabel();
        lblNomDoc = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecordatorio = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblfecha = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnJornada = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuiHorario = new javax.swing.JMenuItem();
        menuiRecordatorio = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        menuDes.setLabel("popupMenu1");

        menuAbrir.setLabel("Abrir");
        menuAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbrirActionPerformed(evt);
            }
        });
        menuDes.add(menuAbrir);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lblReloj.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblReloj.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblNomDoc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNomDoc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblRecordatorio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblRecordatorio);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Próximos Recordatorios del Día");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Usuario:");

        lblfecha.setText(" ");
        lblfecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        jButton3.setText("SALIR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/set-alarm.png"))); // NOI18N
        jButton2.setText("RECORDATORIO");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnJornada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnJornada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/calendar.png"))); // NOI18N
        btnJornada.setText("JORNADA");
        btnJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJornadaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnJornada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnJornada, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/alarm-clock.png"))); // NOI18N
        jLabel3.setText(" ");

        jMenu1.setText("Agregar");

        menuiHorario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/calendar.png"))); // NOI18N
        menuiHorario.setText("Horario");
        menuiHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuiHorarioActionPerformed(evt);
            }
        });
        jMenu1.add(menuiHorario);

        menuiRecordatorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/set-alarm.png"))); // NOI18N
        menuiRecordatorio.setText("Recordatorio");
        menuiRecordatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuiRecordatorioActionPerformed(evt);
            }
        });
        jMenu1.add(menuiRecordatorio);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/usuario.png"))); // NOI18N
        jMenuItem1.setText("Docente");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Salir");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblReloj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblfecha, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(lblNomDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNomDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(lblReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void menuAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbrirActionPerformed
        // TODO add your handling code here:
        mostrar();
    }//GEN-LAST:event_menuAbrirActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        if (cc.CI.equals("false")) {
//            JOptionPane.showMessageDialog(null, "Bienvenido.....!!");

        } else {
            lblNomDoc.setText(colocarNomDocente(cc.CI));
            ocultar();

        }
        t.start();
    }//GEN-LAST:event_formWindowOpened

    private void menuiRecordatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuiRecordatorioActionPerformed
        // TODO add your handling code here:
        new recordatorios(null, true, cedula).setVisible(true);

    }//GEN-LAST:event_menuiRecordatorioActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        try {
//            // TODO add your handling code here:
//            cc.ArchSimCedDoce();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_formWindowClosing

    private void menuiHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuiHorarioActionPerformed
        new Horarios(null, true, cedula).setVisible(true);
    }//GEN-LAST:event_menuiHorarioActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:

        cargarTablaRecordatorios();


    }//GEN-LAST:event_formWindowGainedFocus

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if(cedula.equals("1805037619")){
            new usuarios(null, true).setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Error no cuenta con los permisos necesarios");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        try {
            // TODO add your handling code here:
            cc.ArchSimCedDoce();
            this.dispose();
            entrar ent = new entrar();
            ent.setVisible(true);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            cc.ArchSimCedDoce();
            this.dispose();
            entrar ent = new entrar();
            ent.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_jMenu2MouseClicked

    private void btnJornadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJornadaActionPerformed
         new Horarios(null, true, cedula).setVisible(true);
    }//GEN-LAST:event_btnJornadaActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new recordatorios(null, true, cedula).setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            cc.ArchSimCedDoce();
            this.dispose();
            registrar reg = new registrar();
            reg.setVisible(true);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void sonido() {
        // TODO add your handling code here:

        Clip sonido;
        try {
            sonido = AudioSystem.getClip();
            File a = new File("alarm.wav");
            sonido.open(AudioSystem.getAudioInputStream(a));
            sonido.start();
            sonido.loop(10);
            Thread.sleep(1000); // 1000 milisegundos (10 segundos)
            sonido.close();
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnJornada;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNomDoc;
    private javax.swing.JLabel lblReloj;
    private javax.swing.JLabel lblfecha;
    private java.awt.MenuItem menuAbrir;
    private java.awt.PopupMenu menuDes;
    private javax.swing.JMenuItem menuiHorario;
    private javax.swing.JMenuItem menuiRecordatorio;
    private javax.swing.JTable tblRecordatorio;
    // End of variables declaration//GEN-END:variables
}
