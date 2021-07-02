/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backEnd.Reportes;

import backEnd.Conectar;
import java.io.File;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;


public class Reportes {
    String url;
    JasperReport reporte;
    JasperPrint print;
    public  Reportes (String nombreReporte){
        Conectar cc = new Conectar();
        this.url = nombreReporte;
        System.out.println(url);
        try
        {
             this.reporte= JasperCompileManager.compileReport(url);
            this.print = JasperFillManager.fillReport(reporte, null, cc.abrirBaseDatos());
            JasperViewer.viewReport(print);
        } catch (JRException ex)
        {
            System.out.println(ex);
        } finally{
            cc.cerrarBaseDatos();
        }
    }

    
}
