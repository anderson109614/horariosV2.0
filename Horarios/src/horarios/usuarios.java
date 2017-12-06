/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Gaby
 */
public class usuarios extends javax.swing.JDialog {

    /**
     * Creates new form usuarios
     */
    public usuarios(java.awt.Frame parent, boolean modal) {
        super(parent,modal);
        initComponents();
        pnlFondo fondo = new pnlFondo(this.getWidth(), this.getHeight());
        this.add(fondo, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        
    }
//        cargarTablaUsuarios("");
//        
//        
//        tblUsuarios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent lse) {
//                if (tblUsuarios.getSelectedRow() != -1) {
//                    int fila = tblUsuarios.getSelectedRow();
//                    txtCedula.setText(tblUsuarios.getValueAt(fila, 0).toString().trim());
//                    txtNombre.setText(tblUsuarios.getValueAt(fila, 1).toString().trim());
//                    txtApellido.setText(tblUsuarios.getValueAt(fila, 2).toString().trim());
////                    cmbPerfil.setSelectedIndex(jTable1.getValueAt(fila, 3).toString().trim());
////                    txtComtraseña.setText(jTable1.getValueAt(fila, 4).toString().trim());
//                    //jPasswordField2.setText(jTable1.getValueAt(fila, 5).toString().trim());
//                    txtDesBloqueo();
//                    txtCedula.setEnabled(false);
//                    
//                }
//            }
//        });
//        this.setLocationRelativeTo(null);
//    }
    public usuarios() {
        initComponents();
        pnlFondo fondo = new pnlFondo(this.getWidth(), this.getHeight());
        this.add(fondo, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }
//        cargarTablaUsuarios("");
        
        
//        tblUsuarios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent lse) {
//                if (tblUsuarios.getSelectedRow() != -1) {
//                    int fila = tblUsuarios.getSelectedRow();
//                    txtCedula.setText(tblUsuarios.getValueAt(fila, 0).toString().trim());
//                    txtNombre.setText(tblUsuarios.getValueAt(fila, 1).toString().trim());
//                    txtApellido.setText(tblUsuarios.getValueAt(fila, 2).toString().trim());
////                    cmbPerfil.setSelectedIndex(jTable1.getValueAt(fila, 3).toString().trim());
////                    txtComtraseña.setText(jTable1.getValueAt(fila, 4).toString().trim());
//                    //jPasswordField2.setText(jTable1.getValueAt(fila, 5).toString().trim());
//                    txtDesBloqueo();
//                    txtCedula.setEnabled(false);
//                    
//                }
//            }
//        });
//    }
//    DefaultTableModel model;

//    public void cargarTablaUsuarios(String Dato) {
//
//        String[] titulos = {"CEDULA", "NOMBRE", "APELLIDO","CLAVE"};
//        model = new DefaultTableModel(null, titulos) {
//            public boolean isCellEditable(int row, int column) {
//                return false; 
//            }
//        ;
//        };
//        String[] registros = new String[4];
//        Coneccion cc = new Coneccion();
//        Connection cn = cc.conectar();
//        String sql = "";
//        sql = "select *from docentes where ced_doc LIKE'%" + Dato + "%' order by ced_doc";
//        try {
//            Statement psd = cn.createStatement();
//            ResultSet rs = psd.executeQuery(sql);
//            while (rs.next()) {
//
//                registros[0] = rs.getString("ced_doc");
//                registros[1] = rs.getString("nom_doc");
//                registros[2] = rs.getString("ape_doc");
//                registros[3] = rs.getString("pas_doc");
//                model.addRow(registros);
//
//            }
//            tblUsuarios.setModel(model);
//            tblUsuarios.getTableHeader().setReorderingAllowed(false);
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, ex);
//        }
//    }

    public void guardar() {
        if (txtCedula.getText().isEmpty() || verificarCedula(txtCedula.getText())==0) {
            JOptionPane.showMessageDialog(null, "Debe ingresar cédula válida");
            txtCedula.requestFocus();
        } else if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar Nombre");
        } else if (txtApellido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar Apellido");
        } else if (txtComtraseña.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar Contraseña");
        } else if (txtContraseñaCon.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Confirmar Contraseña");
        } else {

            Coneccion cc = new Coneccion();
            Connection cn = cc.conectar();
            String CED_DOC, NOM_DOC, APE_DOC, PAS_DOC;
            CED_DOC = txtCedula.getText().trim().toUpperCase();
            NOM_DOC = txtNombre.getText().trim().toUpperCase();
            APE_DOC = txtApellido.getText().trim().toUpperCase();
            String contraseña= txtComtraseña.getText();
            //Emcriptado
            
            PAS_DOC = DigestUtils.md5Hex(txtComtraseña.getText().trim());
            
            ///////
            if (contraseña.equals(txtContraseñaCon.getText())) {
                //jPasswordField2.getText( ).trim();

                String sql = "";
                sql = "INSERT INTO DOCENTES (CED_DOC, NOM_DOC, APE_DOC, PAS_DOC)"
                        + " VALUES(?,?,?,?)";
                try {
                    PreparedStatement psd = cn.prepareStatement(sql);
                    psd.setString(1, CED_DOC);
                    psd.setString(2, NOM_DOC);
                    psd.setString(3, APE_DOC);
                    psd.setString(4, PAS_DOC);
                    int n = psd.executeUpdate();

                    if (n > 0) {
                        JOptionPane.showMessageDialog(null, "Se insertó la información correctamente");
//                        cargarTablaUsuarios("");
                        txtLimpiar();
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Usuario ya existente");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Las Contraseñas no coinciden");
            }

        }

    }

    public void txtLimpiar() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
//        jTextField4.setText("");
        txtComtraseña.setText("");
        txtContraseñaCon.setText("");
    }

    

    public void txtDesBloqueo() {
        txtCedula.setEnabled(true);
        txtNombre.setEnabled(true);
        txtApellido.setEnabled(true);
        txtComtraseña.setEnabled(true);
        txtContraseñaCon.setEnabled(true);
        txtCedula.requestFocus();
    }

    public void BotonesInicio() {
     
        btnAgregar.setEnabled(false);
   
        btnLimpiar.setEnabled(false);
 
        btnSalir.setEnabled(true);
    }

    public void BotonesNuevo() {
        btnAgregar.setEnabled(true);
        btnLimpiar.setEnabled(true);
        btnSalir.setEnabled(true);
    }

    

    
    public static int verificarCedula(String ced) { 
        int par, impar, tot, x, dv, z;
        par = 0;
        impar = 0;
        if (ced.length() == 10) {
            for (int i = 0; i <= 9; i++) {
                if (ced.charAt(i) < '0' || ced.charAt(i) > '9') {
                }
            }
            for (int i = 1; i <= 8; i += 2) {
                par += Character.getNumericValue(ced.charAt(i));
            }
            for (int i = 0; i <= 8; i += 2) {
                x = (Character.getNumericValue(ced.charAt(i))) * 2;
                if (x > 9) {
                    x = x - 9;
                }
                impar += x;
            }
            tot = par + impar;
            z = (((tot / 10) + 1) * 10) - tot;
            dv = Character.getNumericValue(ced.charAt(9));
            if (z == 10) {
                z = 0;
            }
            if (z == dv) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtComtraseña = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        txtContraseñaCon = new javax.swing.JPasswordField();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setTitle("Docentes");
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Cédula:");

        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaKeyTyped(evt);
            }
        });

        btnAgregar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        btnAgregar.setText("Guardar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSalir.setDefaultCapable(false);
        btnSalir.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Nombre:");

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Apellido:");

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Contraseña:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Confirmar Contraseña:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(txtCedula)
                    .addComponent(txtApellido)
                    .addComponent(txtComtraseña)
                    .addComponent(txtContraseñaCon))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimpiar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCedula, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtComtraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtContraseñaCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addGap(34, 34, 34))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
         char c=evt.getKeyChar();  
         if(Character.isDigit(c)) { 
              getToolkit().beep(); 
              evt.consume(); 
              
         }  
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
        char c=evt.getKeyChar();  
         if(Character.isDigit(c)) { 
              getToolkit().beep(); 
              evt.consume(); 
              
         }              // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyTyped
         if (txtCedula.getText().length() > 10) {
            if (!Character.isDigit(evt.getKeyChar())) {
                getToolkit().beep();
                evt.consume();
            }
        }
        
//        char c=evt.getKeyChar();  
//        if(Character.isLetter(c)) { 
//              getToolkit().beep(); 
//              evt.consume(); 
//              
//        }         // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaKeyTyped

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_formAncestorAdded

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        guardar();
        txtLimpiar();
        Principal pri= new Principal(Principal.cedula);
        pri.setVisible(true);
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:

        txtLimpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
        registrar reg=new registrar();
        reg.setVisible(true);        // TODO add your handling code here:
         
    }//GEN-LAST:event_btnSalirActionPerformed
     
             
             
             
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
            java.util.logging.Logger.getLogger(usuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(usuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(usuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(usuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new usuarios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JPasswordField txtComtraseña;
    private javax.swing.JPasswordField txtContraseñaCon;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
