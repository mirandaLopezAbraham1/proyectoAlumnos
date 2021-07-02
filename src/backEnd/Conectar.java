/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backEnd;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Conectar {

    private String baseDatos;
    private String url;
    private String user;
    private String pass;
    private String puerto;
    private Connection conexion;

    public Conectar() {
        this.baseDatos = "software";
        this.puerto = "3306";
        this.url = "jdbc:mysql://localhost:" + puerto + "/" + baseDatos;//funciona si me mandan todo los datos de la url
        this.user = "root";
        this.pass = "";
        this.conexion = null;

    }

    public Conectar(String baseDatos, String url, String user, String pass, String puerto) {
        this.baseDatos = baseDatos;
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.puerto = puerto;
        this.conexion = null;
    }

    public Connection abrirBaseDatos() {//metodo = acciones que hace las clases

        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            this.conexion = (Connection) DriverManager.getConnection(this.url, this.user, this.pass);
            if (this.conexion != null)
            {
                //JOptionPane.showMessageDialog(null,"CONEXION EXITOSA");
            }
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        return this.conexion;

    }

    public void cerrarBaseDatos() {
        this.conexion = null;
        System.out.println("BASE DE DATOS DESCONECTADA");
    }

    public boolean  eliminarAlumno (Alumno alumno){
        boolean eliminar = false;
        
        try{
            String sql = " ";
            sql = "delete from alumnos where cedula ='" + alumno.getCedula() + "';";
            PreparedStatement psd = (PreparedStatement) this.conexion.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0)
            {
                eliminar = true;
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
        return eliminar ;
        
    }
    public boolean actualizarAlumno(Alumno alumno){
        boolean actualizar =false;       
        try{
        String sql = "";
            sql = "update alumnos set nombre='" +alumno.getNombre() + "', "
                    + "apellido ='" + alumno.getApellido() + "', "
                    + "direccion='" +alumno.getDireccion()+ "', "
                    + "telefono='" + alumno.getTelefono() + "' "
                    + "where cedula='" + alumno.getCedula()+ "';";
             PreparedStatement psd = (PreparedStatement) this.conexion.prepareStatement(sql);
                int n = psd.executeUpdate();//el n representa la filas q mandamos
                 if (n > 0)
                {
                    actualizar = true;
                }
                
        }catch(Exception e){
            System.out.println(e);
        } 
        return actualizar;
        
    }
    public ArrayList<Alumno> listaAlumnos() {
        ArrayList<Alumno> alumnos = null;

        String sql = "";
        sql = "select *from alumnos";
        try
        {
            alumnos = new ArrayList<Alumno>();
            Statement st = this.conexion.createStatement();//nos proporciona  metodos  para ejecutar consultas dentro en la base de datos
            ResultSet rs = st.executeQuery(sql);//me devuelve la consulta realizada por el sql (QUERY)
            String cedula, nombre, apellido, direccion, telefono;
            while (rs.next())
            {
                cedula = rs.getString("cedula");
                nombre = rs.getString("nombre");
                apellido = rs.getString("apellido");
                direccion = rs.getString("direccion");
                telefono = rs.getString("telefono");
                Alumno alumno = new Alumno(cedula, nombre, apellido, direccion, telefono);
                alumnos.add(alumno);

            }

        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
        return alumnos;
    }

    public boolean insertarAlumno(Alumno alumno) {
        boolean insertar = false;//guardar estado
        try
        {
            String sql = "";
            sql = "Insert into alumnos"
                    + "(cedula,nombre,apellido,direccion,telefono)"
                    + "values (?,?,?,?,?)";
            System.out.println(sql);
            PreparedStatement psd = (PreparedStatement) this.conexion.prepareStatement(sql);//hace que se quede abierta la senial con la base de datos
            psd.setString(1, alumno.getCedula());
            psd.setString(2, alumno.getNombre());
            psd.setString(3, alumno.getApellido());
            psd.setString(4, alumno.getDireccion());
            psd.setString(5, alumno.getTelefono());
            int n = psd.executeUpdate();
            if (n > 0)
            {
                insertar = true;

            }
        } catch (Exception ex)
        {
            System.out.println(ex);
        }
        return insertar;

    }

    public boolean insertarDatos(String cedula, String nombre, String apellido, String direccion, String telefono) {
        boolean insertar = false;
        try
        {
            String sql = " ";
            sql = "Insert into alumnos "
                    + "(cedula,nombre,apellido,direccion,telefono) "
                    + "values(?,?,?,?,?)";//ESCUCHE A LOS VALORES QUE ESTAN
            System.out.println(sql);
            PreparedStatement psd = (PreparedStatement) this.conexion.prepareStatement(sql);//HACE QUE SE PREPARON LOS DATOS 
            psd.setString(1, cedula);
            psd.setString(2, nombre);
            psd.setString(3, apellido);
            psd.setString(4, direccion);
            psd.setString(5, telefono);
            int n = psd.executeUpdate();
            if (n > 0)
            {
                insertar = true;

            }
        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
        return insertar;

    }
}
