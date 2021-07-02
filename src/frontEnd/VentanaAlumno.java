/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontEnd;

import backEnd.Alumno;
import backEnd.Conectar;
import backEnd.Reportes.Reportes;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author abran
 */
public class VentanaAlumno extends javax.swing.JFrame {

    private DefaultTableModel modelo;

    public VentanaAlumno() {
        initComponents();
        bloquearBotonesInicio();
        bloquearTextosInicio();
        cargarTablaAlumno();
        this.setLocationRelativeTo(null);
        jtblAlumnos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jtblAlumnos.getSelectedRow() != -1)
                {
                    int fila = jtblAlumnos.getSelectedRow();
                    jtxtCedula.setText(jtblAlumnos.getValueAt(fila, 0).toString().trim());
                    jtxtNombre.setText(jtblAlumnos.getValueAt(fila, 1).toString().trim());
                    jtxtApellido.setText(jtblAlumnos.getValueAt(fila, 2).toString().trim());
                    jtxtDireccion.setText(jtblAlumnos.getValueAt(fila, 3).toString().trim());
                    jtxtTelefono.setText(jtblAlumnos.getValueAt(fila, 4).toString().trim());
                    desbloquearTextosInicio();
                    jtxtCedula.setEnabled(false);
                    desbloquearBotonesGuardar();
                }
            }
        });
    }

    public void bloquearBotonesInicio() {
        jbtnNuevo.setEnabled(true);
        jbtnGuardar.setEnabled(false);
        jbtnActualizar.setEnabled(false);
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(false);
        jbtnSalir.setEnabled(true);
    }

    public void bloquearBotonesActualizar() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(false);
        jbtnActualizar.setEnabled(true);
        jbtnEliminar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
        jbtnSalir.setEnabled(true);
    }

    public void desbloquearBotonesGuardar() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(true);
        jbtnActualizar.setEnabled(true);
        jbtnEliminar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
        jbtnSalir.setEnabled(true);
    }

    public void bloquearTextosInicio() {
        jtxtCedula.setEnabled(false);
        jtxtNombre.setEnabled(false);
        jtxtApellido.setEnabled(false);
        jtxtDireccion.setEnabled(false);
        jtxtTelefono.setEnabled(false);
    }

    public void desbloquearTextosInicio() {
        jtxtCedula.setEnabled(true);
        jtxtNombre.setEnabled(true);
        jtxtApellido.setEnabled(true);
        jtxtDireccion.setEnabled(true);
        jtxtTelefono.setEnabled(true);
    }

    public void limpiarTextosInicio() {
        jtxtCedula.setText("");
        jtxtNombre.setText("");
        jtxtApellido.setText("");
        jtxtDireccion.setText("");
        jtxtTelefono.setText("");
    }

    public boolean validarVacios() {
        boolean lleno = true;
        if (jtxtCedula.getText().isEmpty() || jtxtNombre.getText().isEmpty() || jtxtApellido.getText().isEmpty())
        {

            lleno = false;
            //System.out.println("si entro al if");
        }
        return lleno;
    }

    public int validarTelDire() {
        int lleno = 0;// 0 los dos estan vacios

        if (jtxtDireccion.getText().trim().isEmpty() && jtxtTelefono.getText().trim().length() != 0)//si mi direccion esta vacia y mi telefono lleno
        {
            lleno = 2;
        } else if (jtxtDireccion.getText().trim().length() != 0 && jtxtTelefono.getText().trim().isEmpty())//si mi telefono esta vacio y  direccion lleno
        {
            lleno = 1;
        } else if (!jtxtDireccion.getText().trim().isEmpty() && !jtxtTelefono.getText().trim().isEmpty())//los dos llenos
        {
            lleno = 3;
        }

        return lleno;
    }

    public void GuardarAlumno() {
        if (validarVacios() == true)// no es necesario poner el == porque ya nos devuelve un true
        {
            try
            {
                Alumno alumno;
                String cedula, nombre, apellido, direccion, telefono;
                Conectar cc = new Conectar();
                if (cc.abrirBaseDatos() != null)
                {
                    cedula = jtxtCedula.getText();
                    nombre = jtxtNombre.getText();
                    apellido = jtxtApellido.getText();
                    direccion = "";
                    telefono = "";
                    switch (validarTelDire())
                    {
                        case 0:
                            direccion = "S/N";
                            telefono = "0000000000";
                            System.out.println("caso 0 ");
                            break;
                        case 1:
                            direccion = jtxtDireccion.getText();
                            telefono = "0000000000";
                            System.out.println("caso 1");
                            break;
                        case 2:
                            direccion = "S/N";
                            telefono = jtxtTelefono.getText();
                            System.out.println("caso 2 ");
                            break;
                        case 3:
                            direccion = jtxtDireccion.getText();
                            telefono = jtxtTelefono.getText();
                            System.out.println("caso 3 ");
                            break;
                    }
                    alumno = new Alumno(cedula, nombre, apellido, direccion, telefono);
                    boolean validacion = cc.insertarAlumno(alumno);
                    cargarTablaAlumno();

                    if (validacion)
                    {
                        JOptionPane.showMessageDialog(null, "GUARDADO CORRECTAMENTE");
                        cc.cerrarBaseDatos();
                    } else
                    {
                        JOptionPane.showMessageDialog(null, "NO SE PUEDE GUARDAR");
                    }
                }
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, "NO SE PUEDE CONECTAR A LA BASE DE ");
            }
        } else
        {
            JOptionPane.showMessageDialog(null, "LLENE TODOS LOS CAMPOS DE CEDULA,NOMBRE Y APELLIDO");
        }
    }

    public void cargarTablaAlumno() {
        String[] titulos =
        {
            "cedula", "nombre", "apellido", "direccion", "telefono"
        };
        this.modelo = new DefaultTableModel(null, titulos);
        String[] registros = new String[5];
        try
        {

            Conectar cc = new Conectar();
            if (cc.abrirBaseDatos() != null)
            {
                ArrayList<Alumno> lista = cc.listaAlumnos();
                if (lista != null)
                {
                    for (Alumno est : lista)
                    {//en est se guarda el primer registro de la lista
                        registros[0] = est.getCedula();
                        registros[1] = est.getNombre();
                        registros[2] = est.getApellido();
                        registros[3] = est.getDireccion();
                        registros[4] = est.getTelefono();
                        this.modelo.addRow(registros);
                    }
                    jtblAlumnos.setModel(modelo);
                    cc.cerrarBaseDatos();
                }
            } else
            {
                JOptionPane.showMessageDialog(null, "NO SE PUEDE CONECTAR A LA BASE DE DATOS");
            }

        } catch (Exception e)
        {
            System.out.println("ERROR");
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void actualizarAlumno() {

        String cedula = jtxtCedula.getText();
        String nombre = jtxtNombre.getText();
        String apellido = jtxtApellido.getText();
        String direccion = jtxtDireccion.getText();
        String telefono = jtxtTelefono.getText();
        Alumno alumnoActualizar = new Alumno(cedula, nombre, apellido, direccion, telefono);
        Conectar conexion = new Conectar();
        if (conexion.abrirBaseDatos() != null)//para abrir la base de datos
        {
            if (conexion.actualizarAlumno(alumnoActualizar))
            {
                JOptionPane.showMessageDialog(null, "se Actualizo correctamente");
                cargarTablaAlumno();
                limpiarTextosInicio();
                bloquearTextosInicio();
                bloquearBotonesInicio();
                conexion.cerrarBaseDatos();
            } else
            {
                JOptionPane.showMessageDialog(null, "NO SE PUDO ACTUALIZAR");
            }

        } else
        {
            JOptionPane.showMessageDialog(null, "ERROR NO SE PUEDE ABRIR LA BASE DE DATOS");
        }
    }

    public void eliminarAlumno() {
        Alumno alumnoCedula = new Alumno(jtxtCedula.getText());
        Conectar conexion = new Conectar();

        if (conexion.abrirBaseDatos() != null)
        {
            if (conexion.eliminarAlumno(alumnoCedula))
            {
                JOptionPane.showMessageDialog(null, "Se elimino correctamente");
                cargarTablaAlumno();
                limpiarTextosInicio();
                bloquearTextosInicio();
                bloquearBotonesInicio();
                conexion.cerrarBaseDatos();
            } else
            {
                JOptionPane.showMessageDialog(null, "NO SE PUDO ELIMINAR CORRECTAMENTE", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jtxtNombre = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jtxtDireccion = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtxtTelefono = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jbtnNuevo = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnActualizar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jbtnSalir = new javax.swing.JButton();
        btnReporte = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblAlumnos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Cedula");

        jLabel2.setText("Nombre");

        jLabel3.setText("Apellido");

        jLabel4.setText("Direccion");

        jLabel5.setText("Telefono");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(18, 18, 18)
                            .addComponent(jtxtNombre))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(18, 18, 18)
                            .addComponent(jtxtApellido))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jtxtDireccion)
                                .addComponent(jtxtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(24, 24, 24)
                        .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnActualizar.setText("Actualizar");
        jbtnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnActualizarActionPerformed(evt);
            }
        });

        jbtnEliminar.setText("Eliminar");
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jbtnSalir.setText("Salir");
        jbtnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSalirActionPerformed(evt);
            }
        });

        btnReporte.setText("Reporte");
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnGuardar)
                    .addComponent(jbtnActualizar)
                    .addComponent(jbtnEliminar)
                    .addComponent(jbtnCancelar)
                    .addComponent(jbtnNuevo)
                    .addComponent(jbtnSalir)
                    .addComponent(btnReporte))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jbtnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnActualizar)
                .addGap(6, 6, 6)
                .addComponent(btnReporte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnCancelar)
                .addGap(18, 18, 18)
                .addComponent(jbtnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jtblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtblAlumnos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
//        String pregunta = JOptionPane.showInputDialog(null,"INGRESE NOMBRE");
//        System.out.println(pregunta);
        //String [] lista = {"1","2","3"} ;

        //String pregunta =(String) JOptionPane.showInputDialog(null, "LISTA","PRUEBA",JOptionPane.DEFAULT_OPTION,null,lista ,lista [0]);
        //int opcion = JOptionPane.showOptionDialog(null, "OPCIONES", "eleccion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, lista, lista[0]);
        desbloquearTextosInicio();
        desbloquearBotonesGuardar();
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSalirActionPerformed

        int salir = JOptionPane.showConfirmDialog(null, "¿ESTA SEGURO DE SALIR?", "CONFIRMACION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);//INFORMATION_MESSAGE,

        System.out.println(salir);
        if (salir == 0)
        {

            this.dispose();
        }
    }//GEN-LAST:event_jbtnSalirActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        GuardarAlumno();
        limpiarTextosInicio();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed

        int eliminar = JOptionPane.showConfirmDialog(null, "¿ESTA SEGURO DE ELIMINAR?", "CONFIRMACION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);//INFORMATION_MESSAGE,

        System.out.println(eliminar);
        if (eliminar == 0)
        {

            eliminarAlumno();
        }
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        limpiarTextosInicio();
        bloquearTextosInicio();
        bloquearBotonesInicio();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jbtnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnActualizarActionPerformed

        int confirmar = JOptionPane.showConfirmDialog(null, "¿ESTA SEGURO DE ACTUALIZAR?", "CONFIRMACION", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);//INFORMATION_MESSAGE,

        System.out.println(confirmar);
        if (confirmar == 0)
        {

            actualizarAlumno();
        }
    }//GEN-LAST:event_jbtnActualizarActionPerformed
    public String getCurrentDirectory (){
            return  this.getClass().getClassLoader().getResource("").getPath();
    }
    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        //String url = this.getClass().getClassLoader().getResource("").getPath();
        //String url = new File("frontEnd//Reportes//report1.jrxml").getAbsolutePath();
        //String nombreReporte = "Reportes/report1.jrxml";
        //System.out.println(url);
        
        
        StringBuilder urlBuilder = new StringBuilder( getCurrentDirectory());
        urlBuilder.deleteCharAt(0);
        String nombreReporte = "frontEnd/Reportes/report1.jrxml";
        String url = urlBuilder.toString() +nombreReporte;
        System.out.println(url);
        Reportes reporte = new Reportes(url);
    }//GEN-LAST:event_btnReporteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(VentanaAlumno.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(VentanaAlumno.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(VentanaAlumno.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(VentanaAlumno.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaAlumno().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReporte;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnActualizar;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JButton jbtnSalir;
    private javax.swing.JTable jtblAlumnos;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtDireccion;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtTelefono;
    // End of variables declaration//GEN-END:variables
}
