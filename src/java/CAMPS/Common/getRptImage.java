/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CAMPS.Common;

import CAMPS.Connect.DBConnect;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Pyllgomez
 */
public class getRptImage {
    public static Image getImage(String imageName) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
    {
        DBConnect con = new DBConnect();
        con.getConnection();
        con.read("SELECT org_report_header FROM camps.master_organization mo ");
        con.rs.first();
        InputStream in = new ByteArrayInputStream(con.rs.getBytes("org_report_header"));
        Image image = ImageIO.read(in);
        con.closeConnection();
        return image;
    }
    public static Image getphoto(String imageName) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
    {
        DBConnect con = new DBConnect();
        con.getConnection();
        System.out.println("SELECT photo FROM student_det_view WHERE enroll_no in ('"+imageName+"')");
        con.read("SELECT photo,enroll_no as enroll_photo FROM student_det_view WHERE enroll_no in ('"+imageName+"')");
        con.rs.first();
        InputStream in = new ByteArrayInputStream(con.rs.getBytes(1));
        Image image = ImageIO.read(in);
        con.closeConnection();
        return image;
    }
}
