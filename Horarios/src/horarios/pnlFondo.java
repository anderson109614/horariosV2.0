package horarios;


import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Anderson
 */
public class pnlFondo extends javax.swing.JPanel {
pnlFondo(int x,int y){
    
    this.setSize(x,y);
//    this.setSize(1366, 768);
//    Icon imgFondo = new ImageIcon(getClass().getResource("/img/fondo.jpg"));
    
}   

@Override
public void paintComponent(Graphics g){
    Dimension tamaño= getSize();
    ImageIcon fondo= new ImageIcon(getClass().getResource("/imagenes/fondo.jpg"));
    g.drawImage(fondo.getImage(), 0, 0, tamaño.width,tamaño.height,null);
    setOpaque(false);
    super.paintComponent(g);
}
}
