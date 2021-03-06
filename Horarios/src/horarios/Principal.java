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
import java.awt.HeadlessException;
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
import java.sql.Time;
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
    static Coneccion cc;
    DefaultTableModel modeloTabla;
    static String cedula = "";
    static String nom = "";

    static Connection cn = null;

    public Principal(String ced) {

        try {

            initComponents();
            cc = new Coneccion();
            cn = cc.conecct;
            pnlFondo fondo = new pnlFondo(this.getWidth(), this.getHeight());
            this.add(fondo, BorderLayout.CENTER);

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
            System.out.println("constructor: ");
            cc.conectar();
        }
    }

    public Principal() {

        try {
            initComponents();

            cc = new Coneccion();
            cn = cc.conecct;
            //cc.ArchCedDoce("1805037619");
            pnlFondo fondo = new pnlFondo(this.getWidth(), this.getHeight());
            this.add(fondo, BorderLayout.CENTER);
            lblNomDoc.setText(colocarNomDocente(cc.CI));
            t = new Timer(1000, acciones);
            imageicon = new ImageIcon(this.getClass().getResource("/imagenes/robot.png"));
            this.setIconImage(imageicon.getImage());
            instanciarTray();
            this.setLocationRelativeTo(null);
            noCerrer();
            cargarTablaRecordatorios();
            cedula = cc.CI;

        } catch (Exception ex) {
            System.out.println("constructor");
            cc.conectar();
        }
    }

    public static Date fecha() {
        try {
            cn = cc.conecct;
            String sql = "select current_date";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                return rs.getDate(1);

            }

        } catch (SQLException ex) {
            System.out.println("fecha:" + ex);
            try {
                cn.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex1);
            }
            cc.conectar();

            return null;
        }
        return null;

    }

    public static Time hora() {
        try {
            cn = cc.conecct;
            String sql = "select current_time";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                return rs.getTime(1);

            }

        } catch (SQLException ex) {
            System.out.println("hora2:" + ex);
            try {
                cn.close();
            } catch (SQLException ex1) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex1);
            }
            cc.conectar();
            return null;
        }
        return null;

    }

    public static String colocarNomDocente(String ced) {
        try {

            String sql = "select nom_doc,ape_doc from docentes where ced_doc='" + cedula + "';";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                return rs.getString("nom_doc") + " " + rs.getString("Ape_doc");

            }

        } catch (SQLException ex) {
            System.out.println("colocar nombre");
            cc.conectar();

            return "";
        }
        return "";
    }

    public static ArrayList recHoy(String fecha, String hora) {

        ArrayList des = new ArrayList();
        try {

            String sql = "select * from recordatorios where fec_rec='" + fecha + "' and ced_doc_per='" + cedula + "' and hor_rec = '" + hora + "' order by hor_rec asc";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                des.add(rs.getString("des_rec"));

            }
        } catch (SQLException ex) {
            System.out.println("rechoy");
            cc.conectar();

        }

        return des;
    }

    public static ArrayList JorHoyIni(String dia, String hora) {

        ArrayList des = new ArrayList();
        try {

            String sql = "select * from jornadas where id_dia_per='" + dia + "' and ced_doc_per='" + cedula + "' and hor_emp = '" + hora + "' order by hor_emp asc";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                des.add(rs.getString("des_jor"));

            }
        } catch (SQLException ex) {
            System.out.println("joyhoy");

            cc.conectar();

        }

        return des;
    }

    public static ArrayList JorHoyFin(String dia, String hora) {

        ArrayList des = new ArrayList();
        try {

            String sql = "select * from jornadas where id_dia_per='" + dia + "' and ced_doc_per='" + cedula + "' and hor_ter = '" + hora + "' order by hor_emp asc";

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                des.add(rs.getString("des_jor"));

            }
        } catch (SQLException ex) {
            System.out.println("jornada finr");
            cc.conectar();

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
            System.out.println("cargar tabla");
            cc.conectar();

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
            try {
                ///////Aqui poner todo lo que se va a estar repitiento cada 5 segundos
                Date fecha = fecha();
                Time hora = hora();
//            SimpleDateFormat formateador = new SimpleDateFormat(" dd-MM-yyyy");
                //System.out.println(hora);
//           Calendar cal = Calendar.getInstance();

                SimpleDateFormat formateador2 = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formateador3 = new SimpleDateFormat("HH:mm:ss");
                String fec = formateador2.format(fecha);
                String hor = formateador3.format(hora.getTime());

                lblReloj.setText(hor);
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

                SimpleDateFormat formateadorM = new SimpleDateFormat("mm:ss");
                String minExt = formateadorM.format(hora);
                if (minExt.equals("00:00")) {
                    SimpleDateFormat formateadorH = new SimpleDateFormat("HH");
                    String horExt = formateadorH.format(hora);
                    //System.out.println(horExt);
                    String dia = String.valueOf(fecha.getDay());

                    ArrayList desJorIni = JorHoyIni(dia, horExt + ":00:00");
                    ArrayList desJorFin = JorHoyFin(dia, horExt + ":00:00");
                    if (desJorIni.size() > 0) {
                        for (int i = 0; i < desJorIni.size(); i++) {
                            sonido();
                            sonido();
                            sonido();
                            sonido();
                            JOptionPane.showMessageDialog(null, "Inicio de Jornada: " + desJorIni.get(i));
                        }

                    }

                    if (desJorFin.size() > 0) {
                        for (int i = 0; i < desJorFin.size(); i++) {
                            sonido();
                            sonido();
                            sonido();
                            sonido();
                            JOptionPane.showMessageDialog(null, "Fin de Jornada: " + desJorFin.get(i));
                        }

                    }

                }

            } catch (Exception ef) {
                cc.conectar();
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
        jLabel3 = new javax.swing.JLabel();
        btnJornada = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabelEstadoConecion = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuiHorario = new javax.swing.JMenuItem();
        menuiRecordatorio = new javax.swing.JMenuItem();
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

        lblNomDoc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

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

        lblfecha.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblfecha.setText("fecha");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/alarm-clock.png"))); // NOI18N
        jLabel3.setText(" ");

        btnJornada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnJornada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/calendar.png"))); // NOI18N
        btnJornada.setText("JORNADA");
        btnJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJornadaActionPerformed(evt);
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

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        jButton3.setText("SALIR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabelEstadoConecion.setForeground(new java.awt.Color(51, 255, 51));
        jLabelEstadoConecion.setText("Conectado");

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblReloj, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(lblfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(42, 42, 42))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNomDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnJornada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelEstadoConecion)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnJornada, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblfecha))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel3)))
                        .addGap(18, 20, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addComponent(jLabelEstadoConecion))
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
            this.setVisible(false);
            registrar re = new registrar();
            re.setVisible(true);
        } else {
            lblNomDoc.setText(colocarNomDocente(cc.CI));
            //ocultar();

        }
        t.start();
    }//GEN-LAST:event_formWindowOpened

    private void menuiRecordatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuiRecordatorioActionPerformed
        // TODO add your handling code here:
        this.dispose();
        calendario cal = new calendario(cedula);
        cal.setVisible(true);

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
        this.dispose();
        calendario cal = new calendario(cedula);
        cal.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:

            cc.ArchSimCedDoce();
            this.dispose();
            registrar reg = new registrar();
            reg.setVisible(true);
            // cn.close();
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
    public static javax.swing.JLabel jLabelEstadoConecion;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
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
