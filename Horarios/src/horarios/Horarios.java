/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrador
 */
public class Horarios extends javax.swing.JDialog {

    /**
     * Creates new form Horarios
     */
    DefaultTableModel modeloTabla;
    DefaultComboBoxModel modeloComboBox;
    Coneccion cc;
    String cedDoc;
    int pv;

    public Horarios(java.awt.Frame parent, boolean modal,String cedula) {
        super(parent,modal);
        initComponents();
        EliminarSprm(tblHorario);
        cc = new Coneccion();
        cargarComboBoxDias();
        cedDoc = cedula;
        cmbDias.setSelectedIndex(0);
        modeloDeTabla();
        this.setLocationRelativeTo(null);
        pnlFondo fondo = new pnlFondo(this.getWidth(), this.getHeight());
            this.add(fondo, BorderLayout.CENTER);
        //cargarDatosDocente(cedula);

    }

    public Horarios() {
        initComponents();
        cc = new Coneccion();
        cargarComboBoxDias();
        cedDoc = "1805037619";
        cmbDias.setSelectedIndex(0);
        modeloDeTabla();
        pnlFondo fondo = new pnlFondo(this.getWidth(), this.getHeight());
            this.add(fondo, BorderLayout.CENTER);
        //cargarDatosDocente(cedula);

    }

//    public void activarJornada_o_Recordatorio() {
//        // TODO add your handling code here:
//        if (rdbJornada.isSelected()) {
//            fmtHoraFin.setEnabled(true);
//        } else {
//            fmtHoraFin.setText("");
//            fmtHoraFin.setEnabled(false);
//
//        }
//    }
    public boolean Act_tabla(int col, int fil) {
         cc = new Coneccion();
        Connection cn = cc.conectar();
        String sql = "";
//        if (col == 1) {
//
//            sql = "UPDATE JORNADAS SET AUT_MARCA='" + tblHorario.getValueAt(fil, col) + "' "
//                    + "WHERE AUT_PLACA='" + tblHorario.getValueAt(fil, 0) + "'";
//
//        }
     boolean aux = false;
        if (col == 2) {
            if(validarHoraFinMayorHoraIniTabla(fil) && validarHorasJornadaTabla(fil,tblHorario.getValueAt(fil, 0).toString())){
            String h=tblHorario.getValueAt(fil, col).toString().substring(0, 2);
            String ax=h.substring(1);
            if(ax.equals(":")){
                h="0"+h.charAt(0)+":00:00";
            }else{
                h=h+":00:00";
            }
                sql = "UPDATE JORNADAS SET HOR_EMP='" + h + "' "
                    + "WHERE ID_JOR='" + tblHorario.getValueAt(fil, 0) + "'";
            aux=true;
            }
        }
        if (col == 3) {
            if(validarHoraFinMayorHoraIniTabla(fil) && validarHorasJornadaTabla(fil,tblHorario.getValueAt(fil, 0).toString())){
            String h=tblHorario.getValueAt(fil, col).toString().substring(0, 2);
            String ax=h.substring(1);
            if(ax.equals(":")){
                h="0"+h.charAt(0)+":00:00";
            }else{
                h=h+":00:00";
            }
                sql = "UPDATE JORNADAS SET HOR_TER='" + h + "' "
                    + "WHERE ID_JOR='" + tblHorario.getValueAt(fil, 0) + "'";
            aux=true;
            }
        }
        if (col == 4) {

            sql = "UPDATE JORNADAS SET DES_JOR='" + tblHorario.getValueAt(fil, col) + "' "
                    + "WHERE ID_JOR='" + tblHorario.getValueAt(fil, 0) + "'";
            aux=true;
        }
         
        try {
           if(aux){
            PreparedStatement psd = cn.prepareStatement(sql);
            psd.executeUpdate();
            return true;   
           }else{
               return false;
           }
            
        } catch (SQLException ex) {
            return false;
        }

    }
    
    public void EliminarSprm(JTable a) {
        a.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                pv=1;
                a.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        if (a.getSelectedRow() != -1 && pv==1) {
                            pv++;
                            int fila = a.getSelectedRow();
                            if (e.getKeyChar() == KeyEvent.VK_DELETE) {
                                cc = new Coneccion();
                                Connection cn = cc.conectar();
                                String sql = "";
                                sql = "delete from jornadas where id_jor='" + a.getValueAt(fila, 0).toString() + "'";
                                int opc = JOptionPane.showConfirmDialog(null, "¿Desea Eliminar?");
                                if (opc == 0) {
                                    try {
                                        PreparedStatement pst;
                                        pst = cn.prepareStatement(sql);
                                        pst.executeUpdate();
                                        modeloDeTabla();
                                    } catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(null, ex);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    public boolean validarHoraFinMayorHoraIni() {
        try {
            int horaIni = Integer.valueOf(spnHoraIni.getValue().toString());
            int horaFin = Integer.valueOf(spnHoraFin.getValue().toString());
            if (horaIni >= horaFin) {
                JOptionPane.showMessageDialog(null, "Error: hora fin debe ser mayor a hora inicio");
                return false;
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error de fecha: " + ex);
            return false;
        }

    }
    public boolean validarHoraFinMayorHoraIniTabla(int row) {
        try {
            String h=tblHorario.getValueAt(row, 2).toString().substring(0, 2);
            String ax=h.substring(1);
            String h1=tblHorario.getValueAt(row, 3).toString().substring(0, 2);
            String ax1=h1.substring(1);
            if(ax.equals(":")){
                h="0"+h.charAt(0) ;
            }else{
                h=h  ;
            }
            if(ax1.equals(":")){
                h1="0"+h1.charAt(0) ;
            }else{
                h1=h1 ;
            }
            int horaIni = Integer.valueOf(h);
            int horaFin = Integer.valueOf(h1);
           // System.out.println(horaIni+" "+horaFin);
            if (horaIni >= horaFin) {
                JOptionPane.showMessageDialog(null, "Error: hora fin debe ser mayor a hora inicio");
                return false;
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error de fecha: " + ex);
            return false;
        }

    }

    public boolean validarHorasJornada() {

        try {
            String horaI = (spnHoraIni.getValue().toString());
            String horaF = (spnHoraFin.getValue().toString());
            SimpleDateFormat parseador = new SimpleDateFormat("H:mm");
            //SimpleDateFormat formateador = new SimpleDateFormat("H:mm");
            //Hora Inicio
            Calendar horaIni = Calendar.getInstance();
            Date hi = parseador.parse(horaI + ":00");
            horaIni.setTime(hi);
            //Hora fin
            Calendar horafin = Calendar.getInstance();
            Date hf = parseador.parse(horaF + ":00");
            horafin.setTime(hf);
            //Hora inicio recuperada base
            Calendar horaIniBase = Calendar.getInstance();
            Date hib = null;

            //Hora fin recuperada base
            Calendar horaFinBase = Calendar.getInstance();
            Date hfb = null;

            int inicio, inicioB, finB, numH;
            inicio = 0;
            inicioB = 0;
            finB = 0;
            numH = 0;

            Connection cn = cc.conectar();
            String sql = "select * from jornadas "
                    + "where id_dia_per =" + (cmbDias.getSelectedIndex() + 1) + " "
                    + "and ced_doc_per ='" + cedDoc + "'";
            try {
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    hib = parseador.parse(rs.getString("hor_emp"));
                    horaIniBase.setTime(hib);
                    inicioB = horaIniBase.get(Calendar.HOUR_OF_DAY);

                    hfb = parseador.parse(rs.getString("hor_ter"));
                    horaFinBase.setTime(hfb);
                    finB = horaFinBase.get(Calendar.HOUR_OF_DAY);

                    numH = finB - inicioB;

                    inicio = horaIni.get(Calendar.HOUR_OF_DAY);
                    for (int i = 0; i < numH; i++) {
                        if (inicioB == inicio) {
                            JOptionPane.showMessageDialog(null, "Error: jornada se cruza con otra asignada");
                            return false;
                        }
                        inicioB++;
                    }
                }
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error base: " + ex);
                return false;
            }

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error en las fechas: " + ex);
            return false;
        }
    }
    
    public boolean validarHorasJornadaTabla(int row,String jor) {

        try {
            String h=tblHorario.getValueAt(row, 2).toString().substring(0, 2);
            String ax=h.substring(1);
            String h1=tblHorario.getValueAt(row, 3).toString().substring(0, 2);
            String ax1=h1.substring(1);
            if(ax.equals(":")){
                h="0"+h.charAt(0) ;
            }else{
                h=h;
            }
            if(ax1.equals(":")){
                h1="0"+h1.charAt(0);
            }else{
                h1=h1;
            }
            String horaI = (h);
            String horaF = (h1);
            SimpleDateFormat parseador = new SimpleDateFormat("H:mm");
            //SimpleDateFormat formateador = new SimpleDateFormat("H:mm");
            //Hora Inicio
            Calendar horaIni = Calendar.getInstance();
            Date hi = parseador.parse(horaI + ":00");
            horaIni.setTime(hi);
            //Hora fin
            Calendar horafin = Calendar.getInstance();
            Date hf = parseador.parse(horaF + ":00");
            horafin.setTime(hf);
            //Hora inicio recuperada base
            Calendar horaIniBase = Calendar.getInstance();
            Date hib = null;

            //Hora fin recuperada base
            Calendar horaFinBase = Calendar.getInstance();
            Date hfb = null;

            int inicio, inicioB, finB, numH;
            inicio = 0;
            inicioB = 0;
            finB = 0;
            numH = 0;

            Connection cn = cc.conectar();
            String sql = "select * from jornadas "
                    + "where id_dia_per =" + (cmbDias.getSelectedIndex() + 1) + " "
                    + "and ced_doc_per ='" + cedDoc + "' "
                    + "and id_jor <> '"+jor+"'";
            try {
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    hib = parseador.parse(rs.getString("hor_emp"));
                    horaIniBase.setTime(hib);
                    inicioB = horaIniBase.get(Calendar.HOUR_OF_DAY);

                    hfb = parseador.parse(rs.getString("hor_ter"));
                    horaFinBase.setTime(hfb);
                    finB = horaFinBase.get(Calendar.HOUR_OF_DAY);

                    numH = finB - inicioB;

                    inicio = horaIni.get(Calendar.HOUR_OF_DAY);
                    for (int i = 0; i < numH; i++) {
                        if (inicioB == inicio) {
                            JOptionPane.showMessageDialog(null, "Error: jornada se cruza con otra asignada");
                            return false;
                        }
                        inicioB++;
                    }
                }
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error base: " + ex);
                return false;
            }

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error en las fechas: " + ex);
            return false;
        }
    }

    public void agregarJornada() {
        // TODO add your handling code here:
        String horaI = (spnHoraIni.getValue().toString());
        String horaF = (spnHoraFin.getValue().toString());

        int hi = Integer.valueOf(horaI);
        int hf = Integer.valueOf(horaF);
        int sumaH = 0;
        int hiB = 0;
        int hfB = 0;
        if (hf <= hi) {
            JOptionPane.showMessageDialog(null, "Error de horas");
            spnHoraIni.requestFocus();
        } else if ((hf - hi) > 6) {
            JOptionPane.showMessageDialog(null, "Máximo de horas por jornada 6");
            spnHoraIni.requestFocus();
        } else {
            try {
                sumaH = hf - hi;
                Connection cn = cc.conectar();
                String sql = "select * from jornadas where ced_doc_per='" + cedDoc + "' and id_dia_per =" + (cmbDias.getSelectedIndex() + 1);
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    hiB = Integer.valueOf(rs.getString("hor_emp").substring(0, 2));
                    hfB = Integer.valueOf(rs.getString("hor_ter").substring(0, 2));
                    sumaH += hfB - hiB;
                }

                if (sumaH > 8) {
                    JOptionPane.showMessageDialog(null, "Error: Jornada Completa 8 horas");
                } else {
                    int hin = Integer.valueOf(spnHoraIni.getValue().toString());
                    int hfn = Integer.valueOf(spnHoraFin.getValue().toString());
                    if (hin == 13 || hfn == 14) {
                        JOptionPane.showMessageDialog(null, "Interfiere con hora de almuerzo");
                    } else if (hin == 12 && hfn == 15) {
                        JOptionPane.showMessageDialog(null, "Interfiere con hora de almuerzo");
                    } else {
                        String Hor_Emp, Hor_Ter, Ced_Doc_Per, Des_Jor;
                        if (txtDescripcion.getText().isEmpty()) {
                            Des_Jor = "Sin descripción";
                        } else {
                            Des_Jor = txtDescripcion.getText();
                        }
                        Hor_Emp = spnHoraIni.getValue().toString() + ":00";
                        Hor_Ter = spnHoraFin.getValue().toString() + ":00";
                        Ced_Doc_Per = cedDoc;
                        String sqlI = "insert into jornadas values(nextval('seq_jor')," + (cmbDias.getSelectedIndex() + 1) + ", '" + Hor_Emp + "' ,'" + Hor_Ter + "' ,'" + Ced_Doc_Per + "' , '" + Des_Jor + "')";
                        PreparedStatement psd = cn.prepareStatement(sqlI);

                        int n = psd.executeUpdate();
                        if (n > 0) {
                            JOptionPane.showMessageDialog(null, "Horario ingresado correctamente");
                            modeloDeTabla();
                        }
                        cn.close();
                    }
                }
            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(null, "Error de base: " + ex);
            }
        }

    }

//    public void cargarDatosDocente(String cedula) {
//        Connection cc = cn.conectar();
//        String sql = "Select * from docentes where ced_doc='" + cedula + "'";
//        try {
//            Statement st = cc.createStatement();
//            ResultSet rs = st.executeQuery(sql);
//            while (rs.next()) {
//                txtNombre.setText(rs.getString("nom_doc"));
//                txtApellido.setText(rs.getString("ape_doc"));
//            }
//            st.close();
//            rs.close();
//            cc.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Ocurrió un error en la consulta " + ex);
//        }
//    }
    public void cargarComboBoxDias() {
        modeloComboBox = new DefaultComboBoxModel();
        Connection cn = cc.conectar();
        String sql = "select * from dias";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("Id_Dia");
                String dia = rs.getString("Nom_Dia");
                modeloComboBox.addElement(id + " " + dia);
            }
            cmbDias.setModel(modeloComboBox);
            cn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Horarios.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void modeloDeTabla() {
        String titulos[] = {"ID","Dia", "Hora Inicio", "Hora Fin", "Descripción"};
        modeloTabla = new DefaultTableModel(null, titulos) {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 && column==1) {
                    return false;
                }
                return true;
            
            }
        };
        tblHorario.setModel(modeloTabla);
        tblHorario.getTableHeader().setReorderingAllowed(false);

        try {
            Coneccion cc = new Coneccion();
            Connection cn = cc.conectar();

            String sql = "select J.ID_JOR,j.hor_emp,j.hor_ter,j.des_jor,d.nom_dia\n"
                    + "from jornadas j, dias d\n"
                    + "where j.ced_doc_per='" + cedDoc + "'\n"
                    + "and j.id_dia_per=d.id_dia\n"
                    + "and j.id_dia_per=" + (cmbDias.getSelectedIndex() + 1) + ""
                    + " order by hor_emp asc;";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            String fila[] = new String[5];
            while (rs.next()) {
                fila[0] = rs.getString(1);
                fila[1] = rs.getString(5);
                fila[2] = rs.getString(2);
                fila[3] = rs.getString(3);
                fila[4] = rs.getString(4);
                modeloTabla.addRow(fila);
            }
            modeloTabla.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {// guardado de la iformacion directo del jtable
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int columna = e.getColumn();
                        int fila = e.getFirstRow();
                        if (Act_tabla(columna, fila)) {
                            
                            JOptionPane.showMessageDialog(null, "Actualizacion correcta");
                        }
                        modeloDeTabla();

                    }
                }
            });
            cn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error de base: " + ex);
        }
    }

    public void limpiar() {

        //cmbDias.setSelectedIndex(0);
//        rdbJornada.setSelected(true);
        txtDescripcion.setText("");
//        activarJornada_o_Recordatorio();

    }

//    public boolean validarCampos() {
////        if (rdbJornada.isSelected()) {
//        if (fmtHoraIni.getText().charAt(0) == ' ') {
//            JOptionPane.showMessageDialog(null, "Ingrese la hora de inicio");
//            return false;
//        } else if (fmtHoraFin.getText().charAt(0) == ' ') {
//            JOptionPane.showMessageDialog(null, "Ingrese la hora de fin");
//            return false;
//        } else {
//            return true;
//        }
////        } else {
////            if (fmtHoraIni.getText().charAt(0) == ' ') {
////                JOptionPane.showMessageDialog(null, "Ingrese la hora de inicio");
////                return false;
////            } else {
////                return true;
////            }
//    }
    public void validarEscrituraHoras(KeyEvent evt, JFormattedTextField texto) {
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        int cont = 0;
        boolean verif = false;
        if (c > '2' && texto.getText().charAt(0) == ' ') {
            evt.consume();
        } else if (texto.getText().charAt(0) == '2') {
            cont++;
        }
        if (texto.getText().charAt(0) == ' ') {
            cont = 0;
            verif = false;
        }
        if (cont > 0 && c > '0') {
            evt.consume();
        }
        if (texto.getText().charAt(0) == '0') {
            verif = true;
        }
        if (verif && c < '7') {
            evt.consume();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GrupoJornadaRecordatorio = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel4 = new javax.swing.JLabel();
        cmbDias = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        spnHoraIni = new javax.swing.JSpinner();
        btnAgregar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        spnHoraFin = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHorario = new javax.swing.JTable();

        setTitle("HORARIOS");
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Día:");

        cmbDias.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDiasItemStateChanged(evt);
            }
        });
        cmbDias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDiasActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Hora Inicio:");

        spnHoraIni.setModel(new javax.swing.SpinnerNumberModel(7, 7, 20, 1));

        btnAgregar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        btnAgregar.setText("Guardar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        btnLimpiar.setText("Cancelar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Hora Fin:");

        spnHoraFin.setModel(new javax.swing.SpinnerNumberModel(7, 7, 20, 1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Descripción:");

        txtDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionKeyTyped(evt);
            }
        });

        tblHorario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblHorario);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(spnHoraFin, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spnHoraIni, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cmbDias, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(spnHoraIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(spnHoraFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpiar)
                        .addGap(63, 63, 63)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if (validarHoraFinMayorHoraIni() && validarHorasJornada()) {
            agregarJornada();
            limpiar();
        }

    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:

        this.dispose();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtDescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionKeyTyped
        Coneccion.validarCamposSoloLetras(evt, txtDescripcion.getText());

    }//GEN-LAST:event_txtDescripcionKeyTyped

    private void cmbDiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDiasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDiasActionPerformed

    private void cmbDiasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDiasItemStateChanged
        // TODO add your handling code here:
        modeloDeTabla();
    }//GEN-LAST:event_cmbDiasItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Horarios().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup GrupoJornadaRecordatorio;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbDias;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spnHoraFin;
    private javax.swing.JSpinner spnHoraIni;
    private javax.swing.JTable tblHorario;
    private javax.swing.JTextField txtDescripcion;
    // End of variables declaration//GEN-END:variables
}
